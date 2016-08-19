package org.mifos.selfserviceapp.api;

import org.mifos.selfserviceapp.data.ChargeListResponse;
import org.mifos.selfserviceapp.data.Client;
import org.mifos.selfserviceapp.data.TransactionsListResponse;
import org.mifos.selfserviceapp.data.User;
import org.mifos.selfserviceapp.data.accounts.LoanAccount;
import org.mifos.selfserviceapp.data.accounts.LoanAccountsListResponse;
import org.mifos.selfserviceapp.data.accounts.SavingAccount;
import org.mifos.selfserviceapp.data.accounts.SavingAccountsListResponse;
import org.mifos.selfserviceapp.utils.PrefManager;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;

/**
 * @author Vishwajeet
 * @since 13/6/16.
 */
@Singleton
public class DataManager {

    private BaseApiManager baseApiManager;

    private final PrefManager prefManager;

    @Inject
    public DataManager(PrefManager prefManager) {
        this.prefManager = prefManager;

        if (prefManager.isAuthenticated()) {
            baseApiManager = new BaseApiManager(prefManager.getToken());
        } else {
            baseApiManager = new BaseApiManager();
        }
    }

    public Call<User> login(String username, String password) {
        return baseApiManager.getAuthenticationApi().authenticate(username, password);
    }

    public Call<Client> getClients() {
        return baseApiManager.getClientsApi().getAllClients();
    }

    public Call<SavingAccountsListResponse> getSavingAccounts(int clientId) {
        return baseApiManager.getSavingAccountsListApi().getSavingAccountsList(clientId);
    }

    public Call<LoanAccountsListResponse> getLoanAccounts(int clientId) {
        return baseApiManager.getLoanAccountsListApi().getLoanAccountsList(clientId);
    }

    public Call<TransactionsListResponse> getRecentTransactions(int clientId) {
        return baseApiManager.getRecentTransactionsApi().getRecentTransactionsList(clientId);
    }

    public Call<ChargeListResponse> getClientCharges(int clientId) {
        return baseApiManager.getClientChargeApi().getClientChargeList(clientId);
    }

    public Call<SavingAccount> getSavingAccountDetails(int accountId) {
        return baseApiManager.getSavingAccountsListApi().getSavingAccountsDetail(accountId);
    }

    public Call<LoanAccount> getLoanAccountDetails(int loanId) {
        return baseApiManager.getLoanAccountsListApi().getLoanAccountsDetail(loanId);
    }

    public PrefManager getPrefManager() {
        return prefManager;
    }

    /**
     * This method is used to re-initialise the {@link BaseApiManager} with the
     * authentication token, so that the Header can be added to the subsequent
     * requests that we make with it.
     */
    public void authenticateApiManager() {
        baseApiManager = new BaseApiManager(prefManager.getToken());
    }
}
