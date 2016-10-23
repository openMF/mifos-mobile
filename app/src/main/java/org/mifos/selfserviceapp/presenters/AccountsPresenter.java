package org.mifos.selfserviceapp.presenters;

import android.content.Context;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.api.DataManager;
import org.mifos.selfserviceapp.injection.ApplicationContext;
import org.mifos.selfserviceapp.models.client.ClientAccounts;
import org.mifos.selfserviceapp.presenters.base.BasePresenter;
import org.mifos.selfserviceapp.ui.views.AccountsView;
import org.mifos.selfserviceapp.utils.Constants;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Rajan Maurya on 23/10/16.
 */

public class AccountsPresenter extends BasePresenter<AccountsView> {

    private final DataManager dataManager;

    @Inject
    public AccountsPresenter(@ApplicationContext Context context, DataManager dataManager) {
        super(context);
        this.dataManager = dataManager;
    }

    @Override
    public void attachView(AccountsView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    public void loadClientAccounts() {
        Call<ClientAccounts> accountsCall = dataManager.getClientAccounts();
        getMvpView().showProgress();
        accountsCall.enqueue(new Callback<ClientAccounts>() {
            @Override
            public void onResponse(Response<ClientAccounts> response) {
                getMvpView().hideProgress();
                if (response.isSuccess()) {
                    getMvpView().showSavingsAccounts(response.body().getSavingsAccounts());
                    getMvpView().showLoanAccounts(response.body().getLoanAccounts());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                getMvpView().hideProgress();
                getMvpView().showError(context.getString(R.string.error_fetching_accounts));
            }
        });
    }

    public void loadAccounts(final String accountType) {
        final Call<ClientAccounts> accounts = dataManager.getAccounts(accountType);
        getMvpView().showProgress();
        accounts.enqueue(new Callback<ClientAccounts>() {
            @Override
            public void onResponse(Response<ClientAccounts> response) {
                getMvpView().hideProgress();
                if (response.isSuccess()) {
                    switch (accountType) {
                        case Constants.SAVINGS_ACCOUNTS:
                            getMvpView().showSavingsAccounts(response.body().getSavingsAccounts());
                            break;
                        case Constants.LOAN_ACCOUNTS:
                            getMvpView().showLoanAccounts(response.body().getLoanAccounts());
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                getMvpView().hideProgress();
                getMvpView().showError(context.getString(R.string.error_fetching_accounts));
            }
        });
    }


}
