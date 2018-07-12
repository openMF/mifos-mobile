package org.mifos.mobilebanking.ui.views;

/*
 * Created by saksham on 02/July/2018
 */

import org.mifos.mobilebanking.ui.views.base.MVPView;

public interface SavingsAccountWithdrawView extends MVPView {

    void showUserInterface();
    void submitWithdrawSavingsAccount();
    void showSavingsAccountWithdrawSuccessfully();
    void showMessage(String message);
    void showError(String error);
}
