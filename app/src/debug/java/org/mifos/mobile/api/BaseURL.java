package org.mifos.mobile.api;

/**
 * @author Vishwajeet
 * @since 09/06/16
 */

public class BaseURL {

    public static final String API_ENDPOINT = "mobile.openmf.org";
    public static final String API_PATH = "/fineract-provider/api/v1/self/";
    public static final String PROTOCOL_HTTPS = "https://";

    public static final String PAYMENT_HUB_API_ENDPOINT = "buffalo.mlabs.dpc.hu";
    public static final String PAYMENT_HUB_API_PATH = "/api/";


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

    public String getPaymentHubUrl() {
        return PROTOCOL_HTTPS + PAYMENT_HUB_API_ENDPOINT + PAYMENT_HUB_API_PATH;
    }
}
