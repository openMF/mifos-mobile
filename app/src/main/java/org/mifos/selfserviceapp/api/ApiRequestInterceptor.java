/*
 * This project is licensed under the open source MPL V2.
 * See https://github.com/openMF/android-client/blob/master/LICENSE.md
 */

package org.mifos.selfserviceapp.api;


import android.content.Context;
import android.util.Log;

import org.mifos.selfserviceapp.utils.PrefManager;

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

    public static final String HEADER_TENANT = "Fineract-Platform-TenantId";
    public static final String HEADER_AUTH = "Authorization";
    private Context context;

    public ApiRequestInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request chainrequest = chain.request();
        Builder builder = chainrequest.newBuilder()
                .header(HEADER_TENANT, "default");

        if (PrefManager.getInstance(context).isAuthenticated()) {
            builder.header(HEADER_AUTH, PrefManager.getInstance(context).getToken());
            Log.v("TAG", PrefManager.getInstance(context).getToken());
        }

        Request request = builder.build();
        return chain.proceed(request);
    }
}
