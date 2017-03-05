package org.mifos.selfserviceapp.api;

import org.mifos.selfserviceapp.api.local.DatabaseHelper;
import org.mifos.selfserviceapp.api.local.PreferencesHelper;
import org.mifos.selfserviceapp.models.Charge;
import org.mifos.selfserviceapp.models.Page;
import org.mifos.selfserviceapp.models.Transaction;
import org.mifos.selfserviceapp.models.User;
import org.mifos.selfserviceapp.models.accounts.loan.LoanAccount;
import org.mifos.selfserviceapp.models.accounts.loan.LoanWithAssociations;
import org.mifos.selfserviceapp.models.accounts.savings.SavingsWithAssociations;
import org.mifos.selfserviceapp.models.client.Client;
import org.mifos.selfserviceapp.models.client.ClientAccounts;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author Vishwajeet
 * @since 13/6/16.
 */
@Singleton
public class DataManager {

    private final PreferencesHelper preferencesHelper;
    private final BaseApiManager baseApiManager;
    private final DatabaseHelper databaseHelper;
    private final long clientId;

    @Inject
    public DataManager(PreferencesHelper preferencesHelper, BaseApiManager baseApiManager,
            DatabaseHelper databaseHelper) {
        this.preferencesHelper = preferencesHelper;
        this.baseApiManager = baseApiManager;
        this.databaseHelper = databaseHelper;
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

    public Observable<Page<Charge>> getClientCharges(long clientId) {
        return baseApiManager.getClientChargeApi().getClientChargeList(clientId)
                .concatMap(new Func1<Page<Charge>, Observable<? extends Page<Charge>>>() {
                    @Override
                    public Observable<? extends Page<Charge>> call(Page<Charge> chargePage) {
                        return databaseHelper.syncCharges(chargePage);
                    }
                });
    }

    public Observable<SavingsWithAssociations> getSavingsWithAssociations(long accountId,
            String associationType) {
        return baseApiManager
                .getSavingAccountsListApi().getSavingsWithAssociations(accountId, associationType);
    }

    public Observable<LoanAccount> getLoanAccountDetails(long loanId) {
        return baseApiManager.getLoanAccountsListApi().getLoanAccountsDetail(loanId);
    }

    public Observable<LoanWithAssociations> getLoanWithAssociations(String associationType,
            long loanId) {
        return baseApiManager.getLoanAccountsListApi()
                .getLoanWithAssociations(loanId, associationType);
    }

    public PreferencesHelper getPreferencesHelper() {
        return preferencesHelper;
    }

    public Observable<Page<Charge>> getClientLocalCharges() {
        return databaseHelper.getClientCharges();
    }
}
