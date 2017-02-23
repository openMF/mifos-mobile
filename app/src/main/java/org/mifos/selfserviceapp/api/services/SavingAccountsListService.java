package org.mifos.selfserviceapp.api.services;

import org.mifos.selfserviceapp.api.ApiEndPoints;
import org.mifos.selfserviceapp.models.accounts.savings.SavingAccount;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * @author Vishwajeet
 * @since 21/6/16.
 */
public interface SavingAccountsListService {

    @GET(ApiEndPoints.SAVINGS_ACCOUNTS + "/{accountId}/")
    Observable<SavingAccount> getSavingAccountsDetail(@Path("accountId") long accountId);

}
