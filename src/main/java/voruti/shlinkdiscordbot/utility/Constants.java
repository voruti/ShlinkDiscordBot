package voruti.shlinkdiscordbot.utility;

import java.util.Arrays;
import java.util.List;

public final class Constants {

    public static final String CMD_CHAR = "!";
    public static final List<String> HELP_CMD = Arrays.asList(
            CMD_CHAR + "was", CMD_CHAR + "what",
            CMD_CHAR + "hilfe", CMD_CHAR + "help"
    );

    private Constants() {
    }
}
