package org.mifos.selfserviceapp.api;

import org.mifos.selfserviceapp.models.ChargeListResponse;
import org.mifos.selfserviceapp.models.Page;
import org.mifos.selfserviceapp.models.client.Client;
import org.mifos.selfserviceapp.models.TransactionsListResponse;
import org.mifos.selfserviceapp.models.User;
import org.mifos.selfserviceapp.models.accounts.LoanAccount;
import org.mifos.selfserviceapp.models.accounts.LoanAccountsListResponse;
import org.mifos.selfserviceapp.models.accounts.SavingAccount;
import org.mifos.selfserviceapp.models.accounts.SavingAccountsListResponse;
import org.mifos.selfserviceapp.api.local.PreferencesHelper;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;

/**
 * @author Vishwajeet
 * @since 13/6/16.
 */
@Singleton
public class DataManager {

    private final PreferencesHelper mPreferencesHelper;
    private final BaseApiManager baseApiManager;

    @Inject
    public DataManager(PreferencesHelper preferencesHelper, BaseApiManager baseApiManager) {
        this.mPreferencesHelper = preferencesHelper;
        this.baseApiManager = baseApiManager;
    }

    public Call<User> login(String username, String password) {
        return baseApiManager.getAuthenticationApi().authenticate(username, password);
    }

    public Call<Page<Client>> getClients() {
        return baseApiManager.getClientsApi().getClients();
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

    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }
}
