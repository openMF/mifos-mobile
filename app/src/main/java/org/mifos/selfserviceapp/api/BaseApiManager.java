package org.mifos.selfserviceapp.api;

import android.content.Context;

import org.mifos.selfserviceapp.api.services.AuthenticationService;
import org.mifos.selfserviceapp.api.services.ClientService;
import org.mifos.selfserviceapp.api.services.LoanAccountsListService;
import org.mifos.selfserviceapp.api.services.SavingAccountsListService;
import org.mifos.selfserviceapp.home.SavingAccountsListFragment;

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
    private ClientService clientsApi;
    private SavingAccountsListService savingAccountsListApi;
    private LoanAccountsListService loanAccountsListApi;
    private Context context;

    public BaseApiManager(Context context){
        this.context = context;
        authenticationApi = createApi(AuthenticationService.class, BASE_URL);
        clientsApi = createApi(ClientService.class, BASE_URL);
        savingAccountsListApi = createApi(SavingAccountsListService.class, BASE_URL);
        loanAccountsListApi = createApi(LoanAccountsListService.class, BASE_URL);
    }

    public OkHttpClient getOkHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).addInterceptor(new ApiRequestInterceptor(context)).build();

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

    public AuthenticationService getAuthenticationApi(){
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

}
