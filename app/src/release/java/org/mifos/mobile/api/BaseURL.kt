package org.mifos.mobile.api

import org.mifos.mobile.api.BaseURL

/**
 * @author Vishwajeet
 * @since 09/06/16
 */
class BaseURL {
    val url: String? = null
        get() = field
                ?: PROTOCOL_HTTPS + API_ENDPOINT + API_PATH
    val defaultBaseUrl: String
        get() = PROTOCOL_HTTPS + API_ENDPOINT

    fun getUrl(endpoint: String): String {
        return endpoint + API_PATH
    }

    companion object {
        const val API_ENDPOINT = "mobile.openmf.io"
        const val API_PATH = "/fineract-provider/api/v1/self/"
        const val PROTOCOL_HTTPS = "https://"
    }
}