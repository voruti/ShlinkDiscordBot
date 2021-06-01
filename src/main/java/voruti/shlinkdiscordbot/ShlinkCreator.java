package voruti.shlinkdiscordbot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
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


    public ShlinkCreator(long botId, String shlinkUrl, String shlinkApiKey) {
        this.botId = botId;
        this.shlinkUrl = shlinkUrl;
        this.shlinkApiKey = shlinkApiKey;

        this.httpClient = new OkHttpClient();
        this.objectMapper = new ObjectMapper();
    }


    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().getIdLong() != botId) {
            Message msg = event.getMessage();
            LOGGER.debug("Received message: {}", msg);

            MessageChannel channel = event.getChannel();

            // respond with help text, if requested:
            if (StaticMethods.messageMatchesCmd(msg, Constants.HELP_CMD)) {
                channel.sendMessage(Constants.CMD_CHAR + "addShlink <long URL> [custom slug]\t:\tCreate a short link from <long URL> with optional [custom slug].").queue();
            } else

                // add short url:
                if (StaticMethods.messageMatchesCmd(msg, Constants.ADD_SHLINK_CMD)) {
                    channel.sendMessage(addShortUrl(msg.getContentRaw())).queue();
                }
        }
    }

    /**
     * Create a new short link from {@code messageText} and return {@link String} as answer.
     *
     * @param messageText the message text
     * @return a {@link String} containing an answer (error messages, final short link, etc.)
     */
    private String addShortUrl(String messageText) {
        String[] cmdSplit = messageText.split(" ");
        if (cmdSplit.length < 2) {
            return "Wrong usage of command; see " + Constants.CMD_CHAR + "help";
        }
        String longUrl = cmdSplit[1];
        if (!longUrl.startsWith("http")) {
            longUrl = "http://" + longUrl;
        }
        String customSlug = null;
        if (cmdSplit.length > 2) {
            customSlug = cmdSplit[2];
        }
        LOGGER.debug("longUrl: {}, customSlug: {}", longUrl, customSlug);

        // json structure:
        Map<Object, Object> data = new HashMap<>();
        data.put("longUrl", longUrl);
        if (customSlug != null) {
            data.put("customSlug", customSlug);
        }
        data.put("findIfExists", true);
        data.put("validateUrl", true);
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
