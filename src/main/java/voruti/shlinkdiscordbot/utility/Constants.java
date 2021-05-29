package voruti.shlinkdiscordbot.utility;

import okhttp3.MediaType;

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
    public static final String POST_BODY = "{\"longUrl\": \"%1$s\",%2$s \"findIfExists\": true, \"validateUrl\": true}";
    public static final String PART_CUSTOM_SLUG = " \"customSlug\": \"%1$s\",";
    public static final String API_KEY_HEADER = "X-Api-Key";
    public static final MediaType TYPE_JSON = MediaType.get("application/json; charset=utf-8");

    private Constants() {
    }
}
