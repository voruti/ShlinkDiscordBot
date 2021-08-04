package voruti.shlinkdiscordbot.utility;

import okhttp3.MediaType;

/**
 * Class to contain (static) constant values that can be accessed from any other class.
 */
public final class Constants {

    /**
     * Version of Shlink's REST-API that should be used.
     */
    public static final short API_VERSION = 2;
    /**
     * Url to POST the configuration for new short links to.
     */
    public static final String POST_URL = "/rest/v" + API_VERSION + "/short-urls";
    /**
     * Header name for the api key.
     */
    public static final String API_KEY_HEADER = "X-Api-Key";
    /**
     * JSON {@link MediaType} for the JSON body of requests and responses.
     */
    public static final MediaType TYPE_JSON = MediaType.get("application/json; charset=utf-8");


    /**
     * "Disabled" constructor.
     */
    private Constants() {
    }
}
