package voruti.shlinkdiscordbot;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
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

public class ShlinkCreator extends ListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShlinkCreator.class);

    private final long botId;
    private final String shlinkUrl;
    private final String shlinkApiKey;

    // one instance, reuse
    private final OkHttpClient httpClient;

    public ShlinkCreator(long botId, String shlinkUrl, String shlinkApiKey) {
        this.botId = botId;
        this.shlinkUrl = shlinkUrl;
        this.shlinkApiKey = shlinkApiKey;

        this.httpClient = new OkHttpClient();
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getAuthor().getIdLong() != botId) {
            LOGGER.debug("Received message: {}", event.getMessage());

            Message msg = event.getMessage();
            MessageChannel channel = event.getChannel();

            // respond with help text, if requested:
            if (StaticMethods.messageMatchesCmd(msg, Constants.HELP_CMD)) {
                channel.sendMessage(Constants.CMD_CHAR + "addShlink <long URL> [custom slug]\t:\tCreate a short link from <long URL> with optional [custom slug].").queue();
                return;
            }

            // add short url:
            if (StaticMethods.messageMatchesCmd(msg, Constants.ADD_SHLINK_CMD)) {
                String[] cmdSplit = msg.getContentRaw().split(" ");
                if (cmdSplit.length < 2) {
                    channel.sendMessage("Wrong usage of command; see " + Constants.CMD_CHAR + "help").queue();
                    return;
                }
                String longUrl = cmdSplit[1];
                String customSlug = null;
                if (cmdSplit.length > 2) {
                    customSlug = cmdSplit[2];
                }

                // form parameters
                Map<Object, Object> data = new HashMap<>();
                data.put("longUrl", longUrl);
                if (customSlug != null) {
                    data.put("customSlug", customSlug);
                }
                data.put("findIfExists", true);
                data.put("validateUrl", true);

                RequestBody body = RequestBody.create(StaticMethods.buildJsonFromMap(data), Constants.TYPE_JSON);
                Request request = new Request.Builder()
                        .url(shlinkUrl + Constants.POST_URL)
                        .header(Constants.API_KEY_HEADER, shlinkApiKey)
                        .post(body)
                        .build();

                Response response;
                try {
                    response = httpClient.newCall(request).execute();
                } catch (IOException e) {
                    LOGGER.warn("Error on connecting to Shlink server", e);
                    channel.sendMessage("Error on connecting to Shlink server!").queue();
                    return;
                }

                channel.sendMessage("TODO: " + response).queue();
                return;
            }
        }
    }
}
