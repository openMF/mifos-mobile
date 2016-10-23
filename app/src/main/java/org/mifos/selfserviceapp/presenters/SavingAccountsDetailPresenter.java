package org.mifos.selfserviceapp.presenters;

import android.content.Context;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.api.DataManager;
import org.mifos.selfserviceapp.models.accounts.savings.SavingAccount;
import org.mifos.selfserviceapp.injection.ActivityContext;
import org.mifos.selfserviceapp.presenters.base.BasePresenter;
import org.mifos.selfserviceapp.ui.views.SavingAccountsDetailView;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Vishwajeet
 * @since 18/8/16.
 */

public class SavingAccountsDetailPresenter extends BasePresenter<SavingAccountsDetailView> {
    private DataManager dataManager;

    /**
     * Initialises the SavingAccountsDetailPresenter by automatically injecting an instance of
     * {@link DataManager} and {@link Context}.
     *
     * @param dataManager DataManager class that provides access to the data
     *                    via the API.
     * @param context     Context of the view attached to the presenter. In this case
     *                    it is that of an {@link android.support.v7.app.AppCompatActivity}
     */
    @Inject
    public SavingAccountsDetailPresenter(DataManager dataManager,
            @ActivityContext Context context) {
        super(context);
        this.dataManager = dataManager;
    }

    /**
     * Load details of a particular saving account from the server and notify the view
     * to display it. Notify the view, in case there is any error in fetching
     * the details from server.
     */
    public void loadSavingAccountDetails(long accountId) {
        Call<SavingAccount> call = dataManager.getSavingAccountDetails(accountId);
        getMvpView().showProgress();
        call.enqueue(new Callback<SavingAccount>() {
            @Override
            public void onResponse(Response<SavingAccount> response) {
                getMvpView().hideProgress();

                if (response.code() == 200) {
                    final SavingAccount savingAccount = response.body();
                    if (savingAccount != null) {
                        getMvpView().showSavingAccountsDetail(savingAccount);
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
