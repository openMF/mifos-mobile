package org.mifos.selfserviceapp.presenters;

import android.content.Context;
import android.widget.TextView;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.api.DataManager;
import org.mifos.selfserviceapp.injection.ActivityContext;
import org.mifos.selfserviceapp.injection.ApplicationContext;
import org.mifos.selfserviceapp.models.Page;
import org.mifos.selfserviceapp.models.accounts.loan.LoanAccount;
import org.mifos.selfserviceapp.models.accounts.savings.SavingAccount;
import org.mifos.selfserviceapp.models.client.Client;
import org.mifos.selfserviceapp.models.client.ClientAccounts;
import org.mifos.selfserviceapp.presenters.base.BasePresenter;
import org.mifos.selfserviceapp.ui.views.HomeView;
import org.mifos.selfserviceapp.utils.Constants;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by michaelsosnick on 1/1/17.
 */

public class HomePresenter extends BasePresenter<HomeView> {

    private final DataManager dataManager;

    @Inject
    public HomePresenter(@ApplicationContext Context context, DataManager dataManager) {
        super(context);
        this.dataManager = dataManager;
    }

    @Override
    public void attachView(HomeView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
    }


    /**
     * This method fetching the Client, associated with current Access Token.
     */
    public void loadClient() {
        Call<Page<Client>> call = dataManager.getClients();
        getMvpView().showProgress();
        call.enqueue(new Callback<Page<Client>>() {
            @Override
            public void onResponse(Response<Page<Client>> response) {
                if (response.isSuccess() && response.body().getPageItems().size() != 0) {
                    getMvpView().showClientInfo(response.body().getPageItems().get(0));
                } else {
                    getMvpView().showError(context
                            .getString(R.string.error_client_not_found));
                }
            }

            @Override
            public void onFailure(Throwable t) {
                getMvpView().showError(context.getString(R.string.error_fetching_client));
                getMvpView().hideProgress();
            }
        });
    }

    public void loadInfo() {
        Call<ClientAccounts> accountsCall = dataManager.getClientAccounts();
        getMvpView().showProgress();
        accountsCall.enqueue(new Callback<ClientAccounts>() {
            @Override
            public void onResponse(Response<ClientAccounts> response) {
                getMvpView().hideProgress();
                if (response.isSuccess()) {
                    getMvpView().showInfo(response.body().getLoanAccounts(),
                            response.body().getSavingsAccounts());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                getMvpView().hideProgress();
                getMvpView().showError(context.getString(R.string.error_fetching_accounts));
            }
        });
    }

        /**
         * Load details of a particular loan account from the server and notify the view
         * to display it. Notify the view, in case there is any error in fetching
         * the details from server.
         */
    public void loadLoanAccountDetails(long loanId, final TextView tv) {
        Call<LoanAccount> call = dataManager.getLoanAccountDetails(loanId);
        getMvpView().showProgress();
        call.enqueue(new Callback<LoanAccount>() {
            @Override
            public void onResponse(Response<LoanAccount> response) {
                getMvpView().hideProgress();

                if (response.code() == 200) {
                    final LoanAccount loanAccount = response.body();
                    if (loanAccount != null) {
                        getMvpView().showLoanAccountsDetail(loanAccount, tv);
                    }
                } else if (response.code() >= 400 && response.code() < 500) {
                    getMvpView().showErrorFetchingLoanAccountsDetail(
                            context.getString(R.string.error_loan_account_details_loading));
                } else if (response.code() == 500) {
                    getMvpView().showErrorFetchingLoanAccountsDetail(
                            context.getString(R.string.error_internal_server));
                }
            }

            @Override
            public void onFailure(Throwable t) {
                getMvpView().hideProgress();
                getMvpView().showErrorFetchingLoanAccountsDetail(
                        context.getString(R.string.error_message_server));
            }
        });
    }


    /**
     * Load details of a particular saving account from the server and notify the view
     * to display it. Notify the view, in case there is any error in fetching
     * the details from server.
     */
    public void loadSavingAccountDetails(long accountId, final TextView tv) {
        Call<SavingAccount> call = dataManager.getSavingAccountDetails(accountId);
        getMvpView().showProgress();
        call.enqueue(new Callback<SavingAccount>() {
            @Override
            public void onResponse(Response<SavingAccount> response) {
                getMvpView().hideProgress();

                if (response.code() == 200) {
                    final SavingAccount savingAccount = response.body();
                    if (savingAccount != null) {
                        getMvpView().showSavingAccountsDetail(savingAccount, tv);
                    }
                } else if (response.code() >= 400 && response.code() < 500) {
                    getMvpView().showErrorFetchingSavingAccountsDetail(
                            context.getString(R.string.error_saving_account_details_loading));
                } else if (response.code() == 500) {
                    getMvpView().showErrorFetchingSavingAccountsDetail(
                            context.getString(R.string.error_internal_server));
                }
            }

            @Override
            public void onFailure(Throwable t) {
                getMvpView().hideProgress();
                getMvpView().showErrorFetchingSavingAccountsDetail(
                        context.getString(R.string.error_message_server));
            }
        });
    }


}
