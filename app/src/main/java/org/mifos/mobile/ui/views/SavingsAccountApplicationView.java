package org.mifos.mobile.ui.views;

/*
 * Created by saksham on 30/June/2018
 */

import org.mifos.mobile.models.templates.savings.SavingsAccountTemplate;
import org.mifos.mobile.ui.views.base.MVPView;

public interface SavingsAccountApplicationView extends MVPView {

    void showUserInterfaceSavingAccountApplication(SavingsAccountTemplate template);
    void showSavingsAccountApplicationSuccessfully();
    void showUserInterfaceSavingAccountUpdate(SavingsAccountTemplate template);
    void showSavingsAccountUpdateSuccessfully();
    void showError(String error);
    void showMessage(String showMessage);
}
