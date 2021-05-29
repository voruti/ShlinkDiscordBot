package voruti.shlinkdiscordbot.utility;

import net.dv8tion.jda.api.entities.Message;

import java.net.http.HttpRequest;
import java.util.List;
import java.util.Map;

public final class StaticMethods {

    private StaticMethods() {
    }

    public static boolean messageMatchesCmd(Message msg, List<String> cmdList) {
        final String msgContent = msg.getContentRaw();
        return cmdList.parallelStream()
                .anyMatch(msgContent::contains);
    }

    public static String generatePostBody(String longUrl, String customSlug) {
        String customSlugPart = "";
        if (customSlug != null) {
            customSlugPart = String.format(Constants.PART_CUSTOM_SLUG, customSlug);
        }
        return String.format(Constants.POST_BODY, longUrl, customSlugPart);
    }

    public static HttpRequest.BodyPublisher buildJsonFromMap(Map<Object, Object> data) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(",");
            }
            stringBuilder.append("\"" + entry.getKey().toString() + "\"");
            stringBuilder.append(":");
            stringBuilder.append("\"" + entry.getValue().toString() + "\"");
        }
        stringBuilder.append("}");
        return HttpRequest.BodyPublishers.ofString(stringBuilder.toString());
    }
}
