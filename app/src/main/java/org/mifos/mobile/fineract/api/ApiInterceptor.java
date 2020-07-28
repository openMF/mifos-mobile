package org.mifos.mobilewallet.core.data.fineract.api;

import android.text.TextUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;

/**
 * Created by naman on 17/6/17.
 */

public class ApiInterceptor implements Interceptor {

    public static final String HEADER_TENANT = "Fineract-Platform-TenantId";
    public static final String HEADER_AUTH = "Authorization";
    private String authToken;
    private String headerTenant;

    public ApiInterceptor(String authToken, String headerTenant) {
        this.authToken = authToken;
        this.headerTenant = headerTenant;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request chainRequest = chain.request();
        Builder builder = chainRequest.newBuilder()
                .header(HEADER_TENANT, headerTenant);

        if (!TextUtils.isEmpty(authToken)) {
            builder.header(HEADER_AUTH, authToken);
        }

        Request request = builder.build();
        return chain.proceed(request);
    }
}
