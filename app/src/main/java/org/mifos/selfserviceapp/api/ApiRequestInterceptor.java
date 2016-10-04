/*
 * This project is licensed under the open source MPL V2.
 * See https://github.com/openMF/android-client/blob/master/LICENSE.md
 */

package org.mifos.selfserviceapp.api;


import android.text.TextUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;

/**
 * @author Vishwajeet
 * @since 21/06/16
 */
public class ApiRequestInterceptor implements Interceptor {

    private static final String HEADER_TENANT = "Fineract-Platform-TenantId";
    private static final String HEADER_AUTH = "Authorization";
    private String authToken;

    public ApiRequestInterceptor(String authToken) {
        this.authToken = authToken;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request chainRequest = chain.request();
        Builder builder = chainRequest.newBuilder()
                .header(HEADER_TENANT, "default");

        if (!TextUtils.isEmpty(authToken)) {
            builder.header(HEADER_AUTH, authToken);
        }

        Request request = builder.build();
        return chain.proceed(request);
    }
}
