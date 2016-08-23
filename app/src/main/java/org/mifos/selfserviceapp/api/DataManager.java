package org.mifos.selfserviceapp.api;

import org.mifos.selfserviceapp.data.ChargeListResponse;
import org.mifos.selfserviceapp.data.Client;
import org.mifos.selfserviceapp.data.FundTransfer.FundTransferTemplateResponse;
import org.mifos.selfserviceapp.data.TransactionsListResponse;
import org.mifos.selfserviceapp.data.User;
import org.mifos.selfserviceapp.data.FundTransfer.FundTransferRequest;
import org.mifos.selfserviceapp.data.FundTransfer.FundTransferResponse;
import org.mifos.selfserviceapp.data.FundTransfer.FundTransferTemplate;
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

    public Call<FundTransferTemplateResponse> getFundTransferTemplate() {
        return baseApiManager.getFundTransferApi().getAccountTransferTemplate();
    }

    public Call<FundTransferResponse> submitTransfer(FundTransferRequest fundTransferRequest) {
        return baseApiManager.getFundTransferApi().submitTransfer(fundTransferRequest);
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
