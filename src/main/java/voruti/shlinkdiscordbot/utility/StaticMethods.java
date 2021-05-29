package voruti.shlinkdiscordbot.utility;

import net.dv8tion.jda.api.entities.Message;

import java.util.List;

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
}
