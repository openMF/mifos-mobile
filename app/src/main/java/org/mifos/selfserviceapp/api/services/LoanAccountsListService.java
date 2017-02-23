package org.mifos.selfserviceapp.api.services;

import org.mifos.selfserviceapp.api.ApiEndPoints;
import org.mifos.selfserviceapp.models.accounts.loan.LoanAccount;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * @author Vishwajeet
 * @since 23/6/16.
 */
public interface LoanAccountsListService {

    @GET(ApiEndPoints.LOANS + "/{loanId}/")
    Observable<LoanAccount> getLoanAccountsDetail(@Path("loanId") long loanId);
}
