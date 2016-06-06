package org.mifos.selfserviceapp.api;

import org.mifos.selfserviceapp.data.Client;
import org.mifos.selfserviceapp.data.User;
import org.mifos.selfserviceapp.data.accounts.LoanAccount;
import org.mifos.selfserviceapp.data.accounts.SavingAccount;

import retrofit2.Call;

/**
 * @author Vishwajeet
 * @since 13/6/16.
 */
public class DataManager {

    public final BaseApiManager mBaseApiManager;

    public DataManager(BaseApiManager baseApiManager){
        mBaseApiManager = baseApiManager;
    }

    public Call<User> login(String username, String password) {
        return mBaseApiManager.getAuthenticationApi().authenticate(username, password);
    }

    public Call<Client> getClients() {
        return mBaseApiManager.getClientsApi().getAllClients();
    }

    public Call<SavingAccount> getSavingAccounts(int id) {
        return mBaseApiManager.getSavingAccountsListApi().getSavingAccountsList(id);
    }

    public Call<LoanAccount> getLoanAccounts(int id) {
        return mBaseApiManager.getLoanAccountsListApi().getLoanAccountsList(id);
    }
}
