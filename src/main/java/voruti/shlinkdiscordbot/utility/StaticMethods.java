package voruti.shlinkdiscordbot.utility;

import net.dv8tion.jda.api.entities.Message;

import java.util.List;
import java.util.Map;

/**
 * Class to contain frequently used (static) methods that require no context and can be accessed from any other class.
 */
public final class StaticMethods {

    /**
     * "Disabled" constructor.
     */
    private StaticMethods() {
    }


    /**
     * Tests {@link Message} if it matches a command list from {@link Constants}.
     *
     * @param msg     the {@link Message} to test
     * @param cmdList a {@link List} of {@link String Strings} that represent permitted commands
     * @return true, if the {@link Message} matches, false otherwise
     */
    public static boolean messageMatchesCmd(Message msg, List<String> cmdList) {
        final String msgContent = msg.getContentRaw();
        return cmdList.parallelStream()
                .anyMatch(msgContent::contains);
    }

    /**
     * Creates a JSON {@link String} from a {@link Map}. Only works for primitive types as key and value inside the {@link Map}.
     *
     * @param data the {@link Map} that should be converted to a JSON {@link String}
     * @return the JSON {@link String}
     */
    public static String buildJsonFromMap(Map<Object, Object> data) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            if (stringBuilder.length() > 1) {
                stringBuilder.append(",");
            }
            stringBuilder
                    .append("\"")
                    .append(entry.getKey().toString())
                    .append("\"")
                    .append(":")
                    .append("\"")
                    .append(entry.getValue().toString())
                    .append("\"");
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
