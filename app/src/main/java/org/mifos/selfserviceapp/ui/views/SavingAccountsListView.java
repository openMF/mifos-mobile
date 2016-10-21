package org.mifos.selfserviceapp.ui.views;

import org.mifos.selfserviceapp.models.accounts.SavingAccount;
import org.mifos.selfserviceapp.ui.views.base.MVPView;

import java.util.List;

/**
 * @author Vishwajeet
 * @since 06/08/16.
 */
public interface SavingAccountsListView extends MVPView {

    /**
     * Should be called if there is any error from client side in loading the client's loan
     * accounts
     * list from server.
     * Reason for error should be mentioned clearly to the user.
     *
     * @param message Error message to display showing reason of failure in loading client list
     */
    void showErrorFetchingSavingAccounts(String message);

    /**
     * Use to display List of saving accounts for the respective clients.
     *
     * @param savingAccountsList List containing saving accounts of a particular client
     */
    void showSavingAccounts(List<SavingAccount> savingAccountsList);
}
