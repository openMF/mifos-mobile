package org.mifos.selfserviceapp.api;

import org.mifos.selfserviceapp.api.services.AuthenticationService;
import org.mifos.selfserviceapp.api.services.ClientChargeService;
import org.mifos.selfserviceapp.api.services.ClientService;
import org.mifos.selfserviceapp.api.services.LoanAccountsListService;
import org.mifos.selfserviceapp.api.services.RecentTransactionsService;
import org.mifos.selfserviceapp.api.services.SavingAccountsListService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

/**
 * @author Vishwajeet
 * @since 13/6/16
 */
public class BaseApiManager {

    private static BaseURL baseUrl = new BaseURL();
    private static final String BASE_URL = baseUrl.getUrl();

    private static Retrofit retrofit;
    private static AuthenticationService authenticationApi;
    private static ClientService clientsApi;
    private static SavingAccountsListService savingAccountsListApi;
    private static LoanAccountsListService loanAccountsListApi;
    private static RecentTransactionsService recentTransactionsApi;
    private static ClientChargeService clientChargeApi;

    public BaseApiManager() {
        String authToken = "";
        createService(authToken);
    }

    private static void init() {
        authenticationApi = createApi(AuthenticationService.class);
        clientsApi = createApi(ClientService.class);
        savingAccountsListApi = createApi(SavingAccountsListService.class);
        loanAccountsListApi = createApi(LoanAccountsListService.class);
        recentTransactionsApi = createApi(RecentTransactionsService.class);
        clientChargeApi = createApi(ClientChargeService.class);
    }

    private static <T> T createApi(Class<T> clazz) {
        return retrofit.create(clazz);
    }

    public static void createService(String authToken) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(new ApiRequestInterceptor(authToken))
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
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

    public SavingAccountsListService getSavingAccountsListApi() {
        return savingAccountsListApi;
    }

    public LoanAccountsListService getLoanAccountsListApi() {
        return loanAccountsListApi;
    }

    public RecentTransactionsService getRecentTransactionsApi() {
        return recentTransactionsApi;
    }

    public ClientChargeService getClientChargeApi() {
        return clientChargeApi;
    }
}
