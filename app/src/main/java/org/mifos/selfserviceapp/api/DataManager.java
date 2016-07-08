package org.mifos.selfserviceapp.api;

import org.mifos.selfserviceapp.data.Client;
import org.mifos.selfserviceapp.data.User;
import org.mifos.selfserviceapp.data.accounts.LoanAccount;
import org.mifos.selfserviceapp.data.accounts.SavingAccount;
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

    private final BaseApiManager baseApiManager;

    private final PrefManager prefManager;

    @Inject
    public DataManager(PrefManager prefManager){
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

    public Call<SavingAccount> getSavingAccounts(int id) {
        return baseApiManager.getSavingAccountsListApi().getSavingAccountsList(id);
    }

    public Call<LoanAccount> getLoanAccounts(int id) {
        return baseApiManager.getLoanAccountsListApi().getLoanAccountsList(id);
    }
}
