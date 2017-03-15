package org.mifos.selfserviceapp.ui.views;

import org.mifos.selfserviceapp.models.templates.loans.LoanTemplate;
import org.mifos.selfserviceapp.ui.views.base.MVPView;

/**
 * Created by Rajan Maurya on 06/03/17.
 */

public interface LoanApplicationMvpView extends MVPView {

    void showUserInterface();

    void showLoanTemplate(LoanTemplate loanTemplate);

    void showLoanTemplateByProduct(LoanTemplate loanTemplate);

    void showLoanAccountCreatedSuccessfully();

    void showError(String message);
}
