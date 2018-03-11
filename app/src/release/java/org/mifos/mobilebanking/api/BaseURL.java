package org.mifos.mobilebanking.api;

/**
 * @author Vishwajeet
 * @since 09/06/16
 */

public class BaseURL {

    public static final String API_ENDPOINT = "demo.openmf.org";
    public static final String API_PATH = "/fineract-provider/api/v1/self/";
    public static final String PROTOCOL_HTTPS = "https://";

    private String url;

    public String getUrl() {
        if (url == null) {
            return PROTOCOL_HTTPS + API_ENDPOINT + API_PATH;
        }
        return url;
    }

    public String getDefaultBaseUrl() {
        return PROTOCOL_HTTPS + API_ENDPOINT;
    }

    public String getUrl(String endpoint) {
        return endpoint + API_PATH;
    }
}
