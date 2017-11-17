package org.mifos.mobilebanking.ui.views;

import org.mifos.mobilebanking.models.templates.account.AccountOptionsTemplate;
import org.mifos.mobilebanking.ui.views.base.MVPView;

/**
 * Created by Rajan Maurya on 10/03/17.
 */

public interface SavingsMakeTransferMvpView extends MVPView {

    void showUserInterface();

    void showSavingsAccountTemplate(AccountOptionsTemplate accountOptionsTemplate);

    void showToaster(String message);

    void showError(String message);

    void showProgressDialog();

    void hideProgressDialog();
}
