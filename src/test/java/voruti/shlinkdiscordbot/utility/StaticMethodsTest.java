package voruti.shlinkdiscordbot.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StaticMethodsTest {

    private static ObjectMapper objectMapper;


    @BeforeAll
    static void beforeAll() {
        objectMapper = new ObjectMapper();
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
                "\"findIfExists\":true," +
                "\"validateUrl\":true" +
                "}";

        // act:
        String jsonResult = StaticMethods.buildJsonFromMap(map);

        // assert:
        assertEquals(objectMapper.readTree(jsonExpected), objectMapper.readTree(jsonResult));
    }


    @ParameterizedTest
    @CsvSource({
            "example@example.com, exa***com",
            "E0-E2-3B-C7-3F-1E, E0-***-1E",
            "Ip0864vBxZMT9KVD-dePvitWL9BF8XhyC-1W4v13TsBpzjv9eM, Ip08***v9eM"
    })
    void hideSecret_ExampleMail(String secret, String hiddenExpected) {
        // act:
        String hiddenResult = StaticMethods.hideSecret(secret);

        // assert:
        assertEquals(hiddenExpected, hiddenResult);
    }

    @Test
    void hideSecret_Null() {
        // arrange:
        String secret = null;
        String hiddenExpected = null;

        // act:
        String hiddenResult = StaticMethods.hideSecret(secret);

        // assert:
        assertEquals(hiddenExpected, hiddenResult);
    }
}
