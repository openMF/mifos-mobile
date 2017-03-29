package org.mifos.selfserviceapp.api.services;

import org.mifos.selfserviceapp.api.ApiEndPoints;
import org.mifos.selfserviceapp.models.accounts.loan.LoanAccount;
import org.mifos.selfserviceapp.models.accounts.loan.LoanWithAssociations;
import org.mifos.selfserviceapp.models.payload.LoansPayload;
import org.mifos.selfserviceapp.models.templates.loans.LoanTemplate;

import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
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

    @GET(ApiEndPoints.LOANS + "/template?templateType=individual")
    Observable<LoanTemplate> getLoanTemplate(@Query("clientId") long clientId);

    @GET(ApiEndPoints.LOANS + "/template?templateType=individual")
    Observable<LoanTemplate> getLoanTemplateByProduct(
            @Query("clientId") long clientId,
            @Query("productId") Integer productId);

    @POST(ApiEndPoints.LOANS)
    Observable<ResponseBody> createLoansAccount(@Body LoansPayload loansPayload);
}
