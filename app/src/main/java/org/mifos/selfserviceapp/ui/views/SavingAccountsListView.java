package org.mifos.selfserviceapp.ui.views;

import org.mifos.selfserviceapp.data.accounts.SavingAccount;
import org.mifos.selfserviceapp.ui.views.base.MVPView;

import java.util.List;

/**
 * Created by vjs3 on 6/8/16.
 */
public interface SavingAccountsListView extends MVPView{

    /**
     * Should be called if there is any error from client side in loading the client's loan accounts list from server.
     * Reason for error should be mentioned clearly to the user.
     *
     * @param message Error message to display showing reason of failure in loading client list
     */
    void showErrorFetchingSavingAccounts(String message);

    void showSavingAccounts(List<SavingAccount> savingAccountsList);
}
