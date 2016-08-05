package org.mifos.selfserviceapp.ui.views;

import org.mifos.selfserviceapp.data.accounts.LoanAccount;
import org.mifos.selfserviceapp.ui.views.base.MVPView;

import java.util.List;

/**
 * @author Vishwajeet
 * @since 30/07/16.
 */

public interface LoanAccountsListView extends MVPView {

    /**
     * Should be called if there is any error from client side in loading the client's loan accounts list from server.
     * Reason for error should be mentioned clearly to the user.
     *
     * @param message Error message to display showing reason of failure in loading client list
     */
    void showErrorFetchingLoanAccounts(String message);

    void showLoanAccounts(List<LoanAccount> loanAccountsList);
}

