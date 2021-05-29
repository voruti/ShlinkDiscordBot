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
            CMD_CHAR + "addShlink", CMD_CHAR + "add Shlink",
            CMD_CHAR + "addshlink", CMD_CHAR + "add shlink",
            CMD_CHAR + "addSL"
    );

    private Constants() {
    }
}
