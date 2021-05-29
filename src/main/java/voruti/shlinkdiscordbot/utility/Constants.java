package voruti.shlinkdiscordbot.utility;

import java.util.Arrays;
import java.util.List;

public final class Constants {

    public static final String CMD_CHAR = "!";
    public static final List<String> HELP_CMD = Arrays.asList(
            CMD_CHAR + "was", CMD_CHAR + "what",
            CMD_CHAR + "hilfe", CMD_CHAR + "help"
    );
    public static final List<String> ADD_SHLINK_CMD = Arrays.asList(
            CMD_CHAR + "addShlink",
            CMD_CHAR + "addshlink",
            CMD_CHAR + "addSL", CMD_CHAR + "addsl"
    );
    public static final short API_VERSION = 2;
    public static final String POST_URL = "/rest/v" + API_VERSION + "/short-urls";
    public static final String POST_BODY = "{\"longUrl\": \"%1$s\", \"customSlug\": \"%2$s\", \"findIfExists\": true, \"validateUrl\": true}";

    private Constants() {
    }
}
