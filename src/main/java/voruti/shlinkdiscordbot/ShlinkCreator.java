package voruti.shlinkdiscordbot;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import voruti.shlinkdiscordbot.utility.Constants;
import voruti.shlinkdiscordbot.utility.StaticMethods;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ShlinkCreator extends ListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShlinkCreator.class);

    private final long botId;
    private final String shlinkUrl;
    private final String shlinkApiKey;

    public ShlinkCreator(long botId, String shlinkUrl, String shlinkApiKey) {
        this.botId = botId;
        this.shlinkUrl = shlinkUrl;
        this.shlinkApiKey = shlinkApiKey;
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

                String filledPostBody = StaticMethods.generatePostBody(longUrl, customSlug);

                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(shlinkUrl + Constants.POST_URL))
                        .POST(HttpRequest.BodyPublishers.ofString(filledPostBody))
                        .build();

                HttpResponse<String> response;
                try {
                    response = client.send(request, HttpResponse.BodyHandlers.ofString());
                } catch (IOException | InterruptedException e) {
                    channel.sendMessage("Error on connection to Shlink server!").queue();
                    return;
                }

                channel.sendMessage("TODO: " + response).queue();
                return;
            }
        }
    }
}
