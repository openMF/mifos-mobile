package org.mifos.selfserviceapp.api;

import org.mifos.selfserviceapp.api.services.AuthenticationService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

/**
 * @author Vishwajeet
 * @since 13/6/16
 */

public class BaseApiManager {

    private BaseURL baseUrl = new BaseURL();
    private final String BASE_URL = baseUrl.getUrl();

    private AuthenticationService authenticationApi;

    public BaseApiManager(){
        authenticationApi = createApi(AuthenticationService.class, BASE_URL);
    }

    public OkHttpClient getOkHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        return client;
    }

    private <T> T createApi(Class<T> clazz, String baseUrl) {

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getOkHttpClient())
                .build()
                .create(clazz);
    }

    protected AuthenticationService getAuthenticationApi(){
        return authenticationApi;
    }
}
