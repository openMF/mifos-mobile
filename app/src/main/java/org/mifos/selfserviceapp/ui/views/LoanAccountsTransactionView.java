package org.mifos.selfserviceapp.ui.views;

import org.mifos.selfserviceapp.models.accounts.loan.LoanWithAssociations;
import org.mifos.selfserviceapp.ui.views.base.MVPView;

/**
 * Created by dilpreet on 4/3/17.
 */

public interface LoanAccountsTransactionView extends MVPView {

    void showUserInterface();

    void showLoanTransactions(LoanWithAssociations loanWithAssociations);

    void showEmptyTransactions(LoanWithAssociations loanWithAssociations);

    void showErrorFetchingLoanAccountsDetail(String message);
}
