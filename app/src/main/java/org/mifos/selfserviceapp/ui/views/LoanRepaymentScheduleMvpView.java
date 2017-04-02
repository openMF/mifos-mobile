package org.mifos.selfserviceapp.ui.views;

import org.mifos.selfserviceapp.models.accounts.loan.LoanWithAssociations;
import org.mifos.selfserviceapp.ui.views.base.MVPView;

/**
 * Created by Rajan Maurya on 03/03/17.
 */

public interface LoanRepaymentScheduleMvpView extends MVPView {

    void showUserInterface();

    void showLoanRepaymentSchedule(LoanWithAssociations loanWithAssociations);

    void showEmptyRepaymentsSchedule(LoanWithAssociations loanWithAssociations);

    void showError(String message);
}