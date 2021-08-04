package voruti.shlinkdiscordbot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import voruti.shlinkdiscordbot.utility.Constants;
import voruti.shlinkdiscordbot.utility.StaticMethods;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Listens to "help" and "add new Shlink" commands and processes them.
 */
public class ShlinkCreator extends ListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShlinkCreator.class);

    private final long botId;
    private final String shlinkUrl;
    private final String shlinkApiKey;

    // one instance, reuse
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;


    public ShlinkCreator(JDA jda, long botId, String shlinkUrl, String shlinkApiKey) {
        this.botId = botId;
        this.shlinkUrl = shlinkUrl;
        this.shlinkApiKey = shlinkApiKey;

        this.httpClient = new OkHttpClient();
        this.objectMapper = new ObjectMapper();

        jda.updateCommands()
                .addCommands(
                        new CommandData("shlink", "Creates a new Shlink (a short link).")
                                .addOptions(
                                        new OptionData(
                                                OptionType.STRING,
                                                "long_url",
                                                "The (long) URL that should be shortened"
                                        ).setRequired(true)
                                )
                                .addOptions(
                                        new OptionData(
                                                OptionType.STRING,
                                                "custom_slug",
                                                String.format(
                                                        "The custom slug; i.e. \"example\" will result in %sexample",
                                                        shlinkUrl.endsWith("/") ? shlinkUrl : shlinkUrl + "/"
                                                )
                                        )
                                )
                )
                .queue();
        LOGGER.info("Slash commands updated");
    }


    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        LOGGER.debug("Received slash command: {}", event.getName());

        switch (event.getName()) {
            case "shlink":
                // Tell discord we received the command, send a thinking... message to the user:
                event.deferReply().queue();

                OptionMapping optionMappingLongUrl = event.getOption("long_url");
                OptionMapping optionMappingCustomSlug = event.getOption("custom_slug");

                event.getHook().sendMessage(addShortUrl(
                        Objects.requireNonNull(optionMappingLongUrl).getAsString(),
                        optionMappingCustomSlug == null ? null : optionMappingCustomSlug.getAsString())
                ).queue();
                break;

            default:
                event.reply("I can't handle that command right now :(").setEphemeral(true).queue();
        }
    }

    /**
     * Create a new short link from {@code messageText} and return {@link String} as answer.
     *
     * @param longUrl    the long URL that should be shortened
     * @param customSlug an optional custom text that should be part of the short/new URL; {@code null} to unset
     * @return a {@link String} containing an answer (error messages, final short link, etc.)
     */
    private String addShortUrl(String longUrl, String customSlug) {
        // validate longUrl (from https://regexr.com/3e6m0):
        if (!longUrl.matches("(http(s)?://.)?(www\\.)?[-a-zA-Z0-9@:%._+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_+.~#?&/=]*)")) {
            LOGGER.info("Invalid URL \"{}\"", longUrl);
            return String.format("Invalid URL \"%s\"!", longUrl);
        }

        if (!longUrl.startsWith("http")) {
            longUrl = "http://" + longUrl;
        }

        // json structure:
        Map<Object, Object> data = new HashMap<>();
        data.put("longUrl", longUrl);
        if (customSlug != null) {
            data.put("customSlug", customSlug);
        }
        data.put("findIfExists", true);
        //data.put("validateUrl", true);
        String jsonBody;
        try {
            jsonBody = StaticMethods.buildJsonFromMap(data);
        } catch (IllegalArgumentException e) {
            LOGGER.warn("Error on creating JSON for request", e);
            return "Error on creating JSON for request!";
        }
        LOGGER.debug("jsonBody: {}", jsonBody);

        RequestBody body = RequestBody.create(jsonBody, Constants.TYPE_JSON);
        Request request = new Request.Builder()
                .url(shlinkUrl + Constants.POST_URL)
                .header(Constants.API_KEY_HEADER, shlinkApiKey)
                .post(body)
                .build();

        String responseJson = null;
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String bodyDetails = null;
                try {
                    responseJson = Objects.requireNonNull(response.body()).string();
                    bodyDetails = objectMapper.readTree(responseJson).path("detail").asText();
                } catch (IOException | NullPointerException ignored) {
                }

                LOGGER.info("Error with Shlink's response {} with body {} and bodyDetails {}", response, responseJson, bodyDetails);
                return "Error with Shlink's response: \"" + bodyDetails + "\"!";
            }

            try {
                responseJson = Objects.requireNonNull(response.body()).string();
                LOGGER.debug("responseJson: {}", responseJson);
            } catch (IOException | NullPointerException e) {
                LOGGER.warn("Error on extracting response", e);
                return "Error on extracting response!";
            }
        } catch (IOException e) {
            LOGGER.warn("Error on connecting to Shlink server", e);
            return "Error on connecting to Shlink server!";
        }

        String extractedShortUrl;
        try {
            extractedShortUrl = objectMapper.readTree(responseJson).path("shortUrl").asText();
        } catch (JsonProcessingException e) {
            LOGGER.warn("Error on parsing json", e);
            return "Error on parsing json!";
        }

        LOGGER.debug("extractedShortUrl: {}", extractedShortUrl);
        return "Here you go: " + extractedShortUrl;
    }
}
