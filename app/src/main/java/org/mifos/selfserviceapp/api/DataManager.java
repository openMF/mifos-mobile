package org.mifos.selfserviceapp.api;

import org.mifos.selfserviceapp.api.local.PreferencesHelper;
import org.mifos.selfserviceapp.models.ChargeListResponse;
import org.mifos.selfserviceapp.models.Page;
import org.mifos.selfserviceapp.models.Transaction;
import org.mifos.selfserviceapp.models.User;
import org.mifos.selfserviceapp.models.accounts.loan.LoanAccount;
import org.mifos.selfserviceapp.models.accounts.savings.SavingAccount;
import org.mifos.selfserviceapp.models.client.Client;
import org.mifos.selfserviceapp.models.client.ClientAccounts;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

/**
 * @author Vishwajeet
 * @since 13/6/16.
 */
@Singleton
public class DataManager {

    private final PreferencesHelper preferencesHelper;
    private final BaseApiManager baseApiManager;
    private final long clientId;

    @Inject
    public DataManager(PreferencesHelper preferencesHelper, BaseApiManager baseApiManager) {
        this.preferencesHelper = preferencesHelper;
        this.baseApiManager = baseApiManager;
        clientId = this.preferencesHelper.getClientId();
    }

    public Observable<User> login(String username, String password) {
        return baseApiManager.getAuthenticationApi().authenticate(username, password);
    }

    public Observable<Page<Client>> getClients() {
        return baseApiManager.getClientsApi().getClients();
    }

    public Observable<ClientAccounts> getClientAccounts() {
        return baseApiManager.getClientsApi().getClientAccounts(clientId);
    }

    public Observable<ClientAccounts> getAccounts(String accountType) {
        return baseApiManager.getClientsApi().getAccounts(clientId, accountType);
    }

    public Observable<Page<Transaction>> getRecentTransactions(int offset, int limit) {
        return baseApiManager.getRecentTransactionsApi()
                .getRecentTransactionsList(clientId, offset, limit);
    }

    public Observable<ChargeListResponse> getClientCharges(long clientId) {
        return baseApiManager.getClientChargeApi().getClientChargeList(clientId);
    }

    public Observable<SavingAccount> getSavingAccountDetails(long accountId) {
        return baseApiManager.getSavingAccountsListApi().getSavingAccountsDetail(accountId);
    }

    public Observable<LoanAccount> getLoanAccountDetails(long loanId) {
        return baseApiManager.getLoanAccountsListApi().getLoanAccountsDetail(loanId);
    }

    public PreferencesHelper getPreferencesHelper() {
        return preferencesHelper;
    }
}
