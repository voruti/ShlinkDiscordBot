package voruti.shlinkdiscordbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import voruti.shlinkdiscordbot.utility.StaticMethods;

import javax.security.auth.login.LoginException;

/**
 * Program starter. Registers the Discord bot and adds the {@link ShlinkCreator} {@link net.dv8tion.jda.api.hooks.ListenerAdapter ListenerAdapter}.
 */
public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);


    /**
     * Starts the Discord bot and adds the {@link ShlinkCreator}.
     *
     * @throws InterruptedException if this thread is interrupted while waiting
     */
    public Main() throws InterruptedException {
        String discordBotToken = System.getenv("DISCORD_BOT_TOKEN");
        String shlinkUrl = System.getenv("SHLINK_URL");
        String shlinkApiKey = System.getenv("SHLINK_API_KEY");

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Using Discord bot token {}", StaticMethods.hideSecret(discordBotToken));
            LOGGER.debug("Using Shlink url {}", shlinkUrl);
            LOGGER.debug("Using Shlink api key {}", StaticMethods.hideSecret(shlinkApiKey));
        }

        try {
            JDA jda = JDABuilder.createDefault(discordBotToken)
                    .build();
            jda.awaitReady();

            jda.addEventListener(new ShlinkCreator(jda, shlinkUrl, shlinkApiKey));
            LOGGER.info("ShlinkCreator event listener added");
        } catch (LoginException | IllegalArgumentException e) {
            LOGGER.error("Exception occurred", e);
        }
    }


    public static void main(String[] args) throws InterruptedException {
        LOGGER.info("Starting {}", Main.class.getName());
        new Main();
        LOGGER.info("Finished start of {}", Main.class.getName());
    }
}
