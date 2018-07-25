package org.mifos.mobilebanking.api.services;

import org.mifos.mobilebanking.api.ApiEndPoints;
import org.mifos.mobilebanking.models.accounts.loan.LoanAccount;
import org.mifos.mobilebanking.models.accounts.loan.LoanWithAssociations;
import org.mifos.mobilebanking.models.accounts.loan.LoanWithdraw;
import org.mifos.mobilebanking.models.payload.LoansPayload;
import org.mifos.mobilebanking.models.templates.loans.LoanTemplate;
import org.mifos.mobilebanking.utils.Constants;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

    @PUT(ApiEndPoints.LOANS + "/{loanId}/")
    Observable<ResponseBody> updateLoanAccount(@Path("loanId") long loanId,
                                              @Body LoansPayload loansPayload);

    @POST(ApiEndPoints.LOANS + "/{loanId}?command=withdrawnByApplicant")
    Observable<ResponseBody> withdrawLoanAccount(@Path(Constants.LOAN_ID) long loanId,
                                                 @Body LoanWithdraw loanWithdraw);
}
