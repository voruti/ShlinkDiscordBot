package voruti.shlinkdiscordbot.utility;

import okhttp3.MediaType;

import java.util.Arrays;
import java.util.List;

/**
 * Class to contain (static) constant values that can be accessed from any other class.
 */
public final class Constants {

    /**
     * The prefix used for all bot commands.
     */
    public static final String CMD_CHAR = "!";
    /**
     * {@link String Strings} the user can use for the "help" command.
     */
    public static final List<String> HELP_CMD = Arrays.asList(
            CMD_CHAR + "was", CMD_CHAR + "what",
            CMD_CHAR + "hilfe", CMD_CHAR + "help"
    );
    /**
     * {@link String Strings} the user can use for the "add new Shlink" command.
     */
    public static final List<String> ADD_SHLINK_CMD = Arrays.asList(
            CMD_CHAR + "addShlink",
            CMD_CHAR + "addshlink",
            CMD_CHAR + "addSL", CMD_CHAR + "addsl"
    );
    /**
     * Version of Shlink's REST-API that should be used.
     */
    public static final short API_VERSION = 2;
    /**
     * Url to POST the configuration for new short links to.
     */
    public static final String POST_URL = "/rest/v" + API_VERSION + "/short-urls";
    /**
     * Header name for the api key.
     */
    public static final String API_KEY_HEADER = "X-Api-Key";
    /**
     * JSON {@link MediaType} for the JSON body of requests and responses.
     */
    public static final MediaType TYPE_JSON = MediaType.get("application/json; charset=utf-8");


    /**
     * "Disabled" constructor.
     */
    private Constants() {
    }
}
