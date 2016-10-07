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

    private final PrefManager prefManager;
    private final BaseApiManager baseApiManager;

    @Inject
    public DataManager(PrefManager prefManager, BaseApiManager baseApiManager) {
        this.prefManager = prefManager;
        this.baseApiManager = baseApiManager;
    }

    public Call<User> login(String username, String password) {
        return baseApiManager.getAuthenticationApi().authenticate(username, password);
    }

    public Call<Client> getClients() {
        return baseApiManager.getClientsApi().getAllClients();
    }

    public Call<SavingAccountsListResponse> getSavingAccounts(long clientId) {
        return baseApiManager.getSavingAccountsListApi().getSavingAccountsList(clientId);
    }

    public Call<LoanAccountsListResponse> getLoanAccounts(long clientId) {
        return baseApiManager.getLoanAccountsListApi().getLoanAccountsList(clientId);
    }

    public Call<TransactionsListResponse> getRecentTransactions(long clientId) {
        return baseApiManager.getRecentTransactionsApi().getRecentTransactionsList(clientId);
    }

    public Call<ChargeListResponse> getClientCharges(long clientId) {
        return baseApiManager.getClientChargeApi().getClientChargeList(clientId);
    }

    public Call<SavingAccount> getSavingAccountDetails(long accountId) {
        return baseApiManager.getSavingAccountsListApi().getSavingAccountsDetail(accountId);
    }

    public Call<LoanAccount> getLoanAccountDetails(long loanId) {
        return baseApiManager.getLoanAccountsListApi().getLoanAccountsDetail(loanId);
    }

    public PrefManager getPrefManager() {
        return prefManager;
    }
}
