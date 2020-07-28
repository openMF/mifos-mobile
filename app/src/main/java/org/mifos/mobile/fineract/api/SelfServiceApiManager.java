package org.mifos.mobilewallet.core.data.fineract.api;

import org.mifos.mobilewallet.core.data.fineract.api.services.AuthenticationService;
import org.mifos.mobilewallet.core.data.fineract.api.services.BeneficiaryService;
import org.mifos.mobilewallet.core.data.fineract.api.services.ClientService;
import org.mifos.mobilewallet.core.data.fineract.api.services.RegistrationService;
import org.mifos.mobilewallet.core.data.fineract.api.services.SavingsAccountsService;
import org.mifos.mobilewallet.core.data.fineract.api.services.ThirdPartyTransferService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by naman on 20/8/17.
 */

public class SelfServiceApiManager {

    public static final String DEFAULT = "default";
    private static BaseURL baseUrl = new BaseURL();
    private static final String BASE_URL = baseUrl.getSelfServiceUrl();

    private static Retrofit retrofit;
    private static AuthenticationService authenticationApi;
    private static ClientService clientsApi;
    private static SavingsAccountsService savingAccountsListApi;
    private static RegistrationService registrationAPi;
    private static BeneficiaryService beneficiaryApi;
    private static ThirdPartyTransferService thirdPartyTransferApi;

    public SelfServiceApiManager() {
        String authToken = "";
        createService(authToken);
    }

    private static void init() {
        authenticationApi = createApi(AuthenticationService.class);
        clientsApi = createApi(ClientService.class);
        savingAccountsListApi = createApi(SavingsAccountsService.class);
        registrationAPi = createApi(RegistrationService.class);
        beneficiaryApi = createApi(BeneficiaryService.class);
        thirdPartyTransferApi = createApi(ThirdPartyTransferService.class);
    }

    private static <T> T createApi(Class<T> clazz) {
        return retrofit.create(clazz);
    }

    public static void createService(String authToken) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .addInterceptor(new ApiInterceptor(authToken, DEFAULT))
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();
        init();
    }

    public AuthenticationService getAuthenticationApi() {
        return authenticationApi;
    }

    public ClientService getClientsApi() {
        return clientsApi;
    }

    public SavingsAccountsService getSavingAccountsListApi() {
        return savingAccountsListApi;
    }

    public RegistrationService getRegistrationAPi() {
        return registrationAPi;
    }

    public BeneficiaryService getBeneficiaryApi() {
        return beneficiaryApi;
    }

    public ThirdPartyTransferService getThirdPartyTransferApi() {
        return thirdPartyTransferApi;
    }

}
