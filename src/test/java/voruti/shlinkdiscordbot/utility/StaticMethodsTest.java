package voruti.shlinkdiscordbot.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StaticMethodsTest {

    private static ObjectMapper objectMapper;


    @BeforeAll
    static void beforeAll() {
        objectMapper = new ObjectMapper();
    }


    @Test
    void messageMatchesCmd_Help() {
        // arrange:
        Message message1 = new MessageBuilder().append("!help").build();
        Message message2 = new MessageBuilder().append("!hilfe").build();
        Message message3 = new MessageBuilder().append("!help abc").build();
        Message message4 = new MessageBuilder().append("!help!").build();
        Message message5 = new MessageBuilder().append("help!").build();
        Message message6 = new MessageBuilder().append("!abc").build();

        // act:
        boolean result1 = StaticMethods.messageMatchesCmd(message1, Constants.HELP_CMD);
        boolean result2 = StaticMethods.messageMatchesCmd(message2, Constants.HELP_CMD);
        boolean result3 = StaticMethods.messageMatchesCmd(message3, Constants.HELP_CMD);
        boolean result4 = StaticMethods.messageMatchesCmd(message4, Constants.HELP_CMD);
        boolean result5 = StaticMethods.messageMatchesCmd(message5, Constants.HELP_CMD);
        boolean result6 = StaticMethods.messageMatchesCmd(message6, Constants.HELP_CMD);

        // assert:
        assertTrue(result1);
        assertTrue(result2);
        assertTrue(result3);
        assertTrue(result4);
        assertFalse(result5);
        assertFalse(result6);
    }

    @Test
    void messageMatchesCmd_AddShlink() {
        // arrange:
        Message message1 = new MessageBuilder().append("!addsl").build();
        Message message2 = new MessageBuilder().append("!addShlink").build();
        Message message3 = new MessageBuilder().append("!addShlink https://www.example.com example").build();
        Message message4 = new MessageBuilder().append("!addsl example.com").build();
        Message message5 = new MessageBuilder().append("addsl!").build();
        Message message6 = new MessageBuilder().append("!add Shlink").build();

        // act:
        boolean result1 = StaticMethods.messageMatchesCmd(message1, Constants.ADD_SHLINK_CMD);
        boolean result2 = StaticMethods.messageMatchesCmd(message2, Constants.ADD_SHLINK_CMD);
        boolean result3 = StaticMethods.messageMatchesCmd(message3, Constants.ADD_SHLINK_CMD);
        boolean result4 = StaticMethods.messageMatchesCmd(message4, Constants.ADD_SHLINK_CMD);
        boolean result5 = StaticMethods.messageMatchesCmd(message5, Constants.ADD_SHLINK_CMD);
        boolean result6 = StaticMethods.messageMatchesCmd(message6, Constants.ADD_SHLINK_CMD);

        // assert:
        assertTrue(result1);
        assertTrue(result2);
        assertTrue(result3);
        assertTrue(result4);
        assertFalse(result5);
        assertFalse(result6);
    }


    @Test
    void buildJsonFromMap_Default() throws JsonProcessingException {
        // arrange:
        Map<Object, Object> map = new HashMap<>();
        map.put("longUrl", "example.com");
        map.put("customSlug", "example");
        map.put("findIfExists", true);
        map.put("validateUrl", true);
        String jsonExpected = "{" +
                "\"longUrl\":\"example.com\"," +
                "\"customSlug\":\"example\"," +
                "\"findIfExists\":\"true\"," +
                "\"validateUrl\":\"true\"" +
                "}";

        // act:
        String jsonResult = StaticMethods.buildJsonFromMap(map);

        // assert:
        assertEquals(objectMapper.readTree(jsonExpected), objectMapper.readTree(jsonResult));
    }
}
