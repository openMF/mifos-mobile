package org.mifos.selfserviceapp.home;

import org.mifos.selfserviceapp.data.accounts.LoanAccount;
import org.mifos.selfserviceapp.ui.views.base.MVPView;

import retrofit2.Response;

/**
 * Created by vjs3 on 23/6/16.
 */

public interface LoanAccountsListView extends MVPView {

    void showLoanAccounts(Response<LoanAccount> response);

    void showErrorFetchingLoanAccounts(String string);

    void showProgress();

    void hideProgress();
}
