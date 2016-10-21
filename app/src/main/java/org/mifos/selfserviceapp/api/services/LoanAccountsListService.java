package org.mifos.selfserviceapp.api.services;

import org.mifos.selfserviceapp.api.ApiEndPoints;
import org.mifos.selfserviceapp.models.accounts.LoanAccount;
import org.mifos.selfserviceapp.models.accounts.LoanAccountsListResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @author Vishwajeet
 * @since 23/6/16.
 */
public interface LoanAccountsListService {
    @GET(ApiEndPoints.CLIENTS + "/{clientId}/accounts?fields=loanAccounts")
    Call<LoanAccountsListResponse> getLoanAccountsList(@Path("clientId") long clientId);

    @GET(ApiEndPoints.LOANS + "/{loanId}/")
    Call<LoanAccount> getLoanAccountsDetail(@Path("loanId") long loanId);
}
