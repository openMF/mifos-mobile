package org.mifos.selfserviceapp.api.services;

import org.mifos.selfserviceapp.api.ApiEndPoints;
import org.mifos.selfserviceapp.models.accounts.loan.LoanAccount;
import org.mifos.selfserviceapp.models.accounts.loan.LoanWithAssociations;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author Vishwajeet
 * @since 23/6/16.
 */
public interface LoanAccountsListService {

    @GET(ApiEndPoints.LOANS + "/{loanId}/")
    Observable<LoanAccount> getLoanAccountsDetail(@Path("loanId") long loanId);

    @GET(ApiEndPoints.LOANS + "/{loanId}")
    Observable<LoanWithAssociations> getLoanWithAssociations(
            @Path("loanId") long loanId,
            @Query("associations") String associationType);

}
