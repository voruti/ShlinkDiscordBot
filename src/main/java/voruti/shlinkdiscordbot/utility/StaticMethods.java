package voruti.shlinkdiscordbot.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 * Class to contain frequently used (static) methods that require no context and can be accessed from any other class.
 */
public final class StaticMethods {

    private static ObjectMapper objectMapper = new ObjectMapper();


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
                .anyMatch(msgContent::startsWith);
    }

    /**
     * Creates a JSON {@link String} from a {@link Map}. Only works for primitive types as key and value inside the {@link Map}.
     *
     * @param objectObjectMap the {@link Map} that should be converted to a JSON {@link String}
     * @throws IllegalArgumentException if the {@link Map} can't be converted to JSON
     */
    public static String buildJsonFromMap(Map<Object, Object> objectObjectMap) throws IllegalArgumentException {
        try {
            return objectMapper.writeValueAsString(objectObjectMap);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Hides parts of the input {@link String}.
     *
     * @param secret the {@link String} to hide parts from
     * @return the {@link String} with some parts hidden
     */
    public static String hideSecret(@Nullable String secret) {
        if (secret == null) {
            return null;
        }

        int proportionalLength = (int) (0.2 * secret.length());
        if (proportionalLength < 2) {
            proportionalLength = 2;
        }
        if (proportionalLength > 4) {
            proportionalLength = 4;
        }
        String prefix = secret.substring(0, proportionalLength);
        String suffix = secret.substring(secret.length() - proportionalLength);
        return prefix + "***" + suffix;
    }
}
