package org.mifos.selfserviceapp.ui.views;

import org.mifos.selfserviceapp.models.accounts.loan.LoanAccount;
import org.mifos.selfserviceapp.ui.views.base.MVPView;

/**
 * Created by dilpreet on 4/3/17.
 */

public interface LoanAccountsTransactionView extends MVPView {

    void showLoanAccountsDetail(LoanAccount loanAccount);

    void showErrorFetchingLoanAccountsDetail(String message);
}
