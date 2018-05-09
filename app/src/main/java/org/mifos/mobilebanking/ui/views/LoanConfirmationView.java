package org.mifos.mobilebanking.ui.views;

import org.mifos.mobilebanking.ui.views.base.MVPView;

/*
 * Created by saksham on 08/May/2018
 */

public interface LoanConfirmationView extends MVPView {

    void showError(String message);

    void showLoanAccountCreatedSuccessfully();

    void showLoanAccountUpdatedSuccessfully();

}
