package org.mifos.selfserviceapp.api.services;

import org.mifos.selfserviceapp.api.ApiEndPoints;
import org.mifos.selfserviceapp.data.accounts.SavingAccount;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @author Vishwajeet
 * @since 21/6/16.
 */
public interface SavingAccountsListService {
    @GET(ApiEndPoints.CLIENTS + "/{clientId}/accounts?fields=savingsAccounts")
    Call<SavingAccount> getSavingAccountsList(@Path("clientId") int clientId);
}
