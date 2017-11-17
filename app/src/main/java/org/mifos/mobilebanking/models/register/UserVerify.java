package org.mifos.mobilebanking.models.register;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dilpreet on 31/7/17.
 */

public class UserVerify {

    @SerializedName("requestId")
    private String requestId;

    @SerializedName("authenticationToken")
    private String authenticationToken;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getAuthenticationToken() {
        return authenticationToken;
    }

    public void setAuthenticationToken(String authenticationToken) {
        this.authenticationToken = authenticationToken;
    }
}
