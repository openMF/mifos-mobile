package org.mifos.selfserviceapp.ui.views;

import org.mifos.selfserviceapp.models.templates.account.AccountOptionsTemplate;
import org.mifos.selfserviceapp.ui.views.base.MVPView;

/**
 * Created by Rajan Maurya on 10/03/17.
 */

public interface SavingsMakeTransferMvpView extends MVPView {

    void showUserInterface();

    void makeTransfer();

    void showSavingsAccountTemplate(AccountOptionsTemplate accountOptionsTemplate);

    void showTransferredSuccessfully();

    void showToaster(String message);

    void showError(String message);

    void showProgressDialog();

    void hideProgressDialog();
}
