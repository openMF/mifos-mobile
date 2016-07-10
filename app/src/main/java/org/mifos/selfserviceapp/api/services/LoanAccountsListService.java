package org.mifos.selfserviceapp.api.services;

import org.mifos.selfserviceapp.api.ApiEndPoints;
import org.mifos.selfserviceapp.data.accounts.LoanAccount;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by vjs3 on 23/6/16.
 */

public interface LoanAccountsListService {
    @GET(ApiEndPoints.CLIENTS + "/{clientId}/accounts?fields=loanAccounts")
    Call<LoanAccount> getLoanAccountsList(@Path("clientId") int clientId);
}
