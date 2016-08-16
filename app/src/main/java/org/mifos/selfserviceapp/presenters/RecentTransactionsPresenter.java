package org.mifos.selfserviceapp.presenters;

import android.content.Context;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.api.DataManager;
import org.mifos.selfserviceapp.data.Transaction;
import org.mifos.selfserviceapp.data.accounts.LoanAccount;
import org.mifos.selfserviceapp.injection.ActivityContext;
import org.mifos.selfserviceapp.presenters.base.BasePresenter;
import org.mifos.selfserviceapp.ui.views.RecentTransactionsView;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Vishwajeet
 * @since 10/08/16
 */
public class RecentTransactionsPresenter extends BasePresenter<RecentTransactionsView> {
    private DataManager dataManager;

    /**
     * Initialises the RecentTransactionsPresenter by automatically injecting an instance of
     * {@link DataManager} and {@link Context}.
     *
     * @param dataManager DataManager class that provides access to the data
     *                    via the API.
     * @param context     Context of the view attached to the presenter. In this case
     *                    it is that of an {@link android.support.v7.app.AppCompatActivity}
     */

    @Inject
    public RecentTransactionsPresenter(DataManager dataManager, @ActivityContext Context context) {
        super(context);
        this.dataManager = dataManager;
    }

    public void loadRecentTransactions(int clientId) {
        Call<Transaction> call = dataManager.getRecentTransactions(clientId);
        getMvpView().showProgress();

        call.enqueue(new Callback<Transaction>() {
            @Override
            public void onResponse(Response<Transaction> response) {
                getMvpView().hideProgress();

                if (response.code() == 200) {
                    Transaction recentTransaction = response.body();
                    List<Transaction> recentTransactionsList = response.body().getPageItems();
                    if (recentTransaction != null) {
                        getMvpView().showRecentTransactions(recentTransactionsList);
                    }

                } else if (response.code() >= 400 && response.code() < 500) {
                    getMvpView().showErrorFetchingRecentTransactions(context.getString(R.string.error_recent_transactions_loading));
                } else if (response.code() == 500) {
                    getMvpView().showErrorFetchingRecentTransactions(context.getString(R.string.error_internal_server));
                }
            }

            @Override
            public void onFailure(Throwable t) {
                getMvpView().hideProgress();
                getMvpView().showErrorFetchingRecentTransactions(context.getString(R.string.error_message_server));
            }
        });
    }
}

