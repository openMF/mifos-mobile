package org.mifos.selfserviceapp.ui.views;

import android.widget.TextView;

import org.mifos.selfserviceapp.models.accounts.loan.LoanAccount;
import org.mifos.selfserviceapp.models.accounts.savings.SavingAccount;
import org.mifos.selfserviceapp.models.client.Client;
import org.mifos.selfserviceapp.ui.views.base.MVPView;
import java.util.List;

/**
 * Created by michaelsosnick on 1/1/17.
 */

public interface HomeView extends MVPView {

    void showInfo(List<LoanAccount> loanAccounts, List<SavingAccount> savingAccounts);

    /**
     * Should be called when loan account object can successfully accessed
     * from the server to display loan account details on the screen.
     *
     * @param loanAccount object containing details of each loan account,
     *                    received from server.
     */
    void showLoanAccountsDetail(LoanAccount loanAccount, TextView tv);

    /**
     * Should be called if there is any error from client side in getting
     * loan account object from server.
     * <p>
     * Reason for error should be mentioned clearly to the user.
     *
     * @param message Error message to display showing reason of failure in getting
     *                loan account object
     */
    void showErrorFetchingLoanAccountsDetail(String message);

    /**
     * Should be called when saving account object can successfully accessed
     * from the server to display saving account details on the screen.
     *
     * @param savingAccount object containing details of each saving account,
     *                      received from server.
     */
    void showSavingAccountsDetail(SavingAccount savingAccount, TextView tv);

    /**
     * Should be called when client object can successfully accessed
     * from the server to display client details on the screen.
     *
     * @param client object containing details of the client
     *                      received from server.
     */
    void showClientInfo(Client client);

    /**
     * Should be called if there is any error from client side in getting
     * saving account object from server.
     *
     * Reason for error should be mentioned clearly to the user.
     *
     * @param message Error message to display showing reason of failure in getting
     *                saving account object
     */
    void showErrorFetchingSavingAccountsDetail(String message);

    void showError(String errorMessage);
}
