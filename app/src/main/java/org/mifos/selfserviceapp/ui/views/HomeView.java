package org.mifos.selfserviceapp.ui.views;

import org.mifos.selfserviceapp.models.accounts.loan.LoanAccount;
import org.mifos.selfserviceapp.models.accounts.share.ShareAccount;
import org.mifos.selfserviceapp.models.accounts.savings.SavingAccount;
import org.mifos.selfserviceapp.models.accounts.savings.SavingsWithAssociations;
import org.mifos.selfserviceapp.models.client.Client;
import org.mifos.selfserviceapp.ui.views.base.MVPView;
import java.util.List;

/**
 * Created by michaelsosnick on 1/1/17.
 */

public interface HomeView extends MVPView {

    void showInfo(List<LoanAccount> loanAccounts,
                  List<SavingAccount> savingAccounts,
                  List<ShareAccount> shareAccounts);

    /**
     * Should be called when saving account list can successfully accessed
     * from the server to display the total saving amount on the screen.
     *
     * @param savingAccounts object containing the list of saving accounts,
     *                    received from server.
     */

    void showSavingTotal(List<SavingAccount> savingAccounts);

    /**
     * Should be called when share account list can successfully accessed
     * from the server to display the total saving amount on the screen.
     *
     * @param shareAccounts object containing the list of share accounts,
     *                    received from server.
     */

    void showShareTotal(List<ShareAccount> shareAccounts);

    /**
     * Should be called when loan account list can successfully accessed
     * from the server to display the total saving amount on the screen.
     *
     * @param loanAccounts object containing the list of share accounts,
     *                    received from server.
     */

    void showLoanTotal(List<LoanAccount> loanAccounts);

    /**
     * Should be called when loan account object can successfully accessed
     * from the server to display loan account details on the screen.
     *
     * @param loanAccount object containing details of each loan account,
     *                    received from server.
     */
    void showLoanAccountsDetail(LoanAccount loanAccount);

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
    void showSavingAccountsDetail(SavingsWithAssociations savingAccount);

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
