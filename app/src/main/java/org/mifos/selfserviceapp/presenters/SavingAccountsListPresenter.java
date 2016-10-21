package org.mifos.selfserviceapp.presenters;

import android.content.Context;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.api.DataManager;
import org.mifos.selfserviceapp.models.accounts.SavingAccount;
import org.mifos.selfserviceapp.models.accounts.SavingAccountsListResponse;
import org.mifos.selfserviceapp.injection.ActivityContext;
import org.mifos.selfserviceapp.presenters.base.BasePresenter;
import org.mifos.selfserviceapp.ui.views.SavingAccountsListView;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SavingAccountsListPresenter extends BasePresenter<SavingAccountsListView> {
    private DataManager dataManager;

    /**
     * Initialises the ClientListPresenter by automatically injecting an instance of
     * {@link DataManager} and {@link Context}.
     *
     * @param dataManager DataManager class that provides access to the data
     *                    via the API.
     * @param context     Context of the view attached to the presenter. In this case
     *                    it is that of an {@link android.support.v7.app.AppCompatActivity}
     */

    @Inject
    public SavingAccountsListPresenter(DataManager dataManager, @ActivityContext Context context) {
        super(context);
        this.dataManager = dataManager;
    }

    public void loadSavingAccountsList(long clientId) {
        Call<SavingAccountsListResponse> call = dataManager.getSavingAccounts(clientId);
        getMvpView().showProgress();

        call.enqueue(new Callback<SavingAccountsListResponse>() {
            @Override
            public void onResponse(Response<SavingAccountsListResponse> response) {
                getMvpView().hideProgress();

                if (response.code() == 200) {
                    SavingAccountsListResponse savingAccount = response.body();
                    List<SavingAccount> savingAccountsList = response.body().getSavingsAccounts();
                    if (savingAccount != null) {
                        getMvpView().showSavingAccounts(savingAccountsList);
                    }

                } else if (response.code() >= 400 && response.code() < 500) {
                    getMvpView().showErrorFetchingSavingAccounts(
                            context.getString(R.string.error_saving_accounts_list_loading));
                } else if (response.code() == 500) {
                    getMvpView().showErrorFetchingSavingAccounts(
                            context.getString(R.string.error_internal_server));
                }
            }

            @Override
            public void onFailure(Throwable t) {
                getMvpView().hideProgress();
                getMvpView().showErrorFetchingSavingAccounts(
                        context.getString(R.string.error_message_server));
            }
        });
    }
}