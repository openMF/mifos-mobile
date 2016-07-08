package org.mifos.selfserviceapp.presenters;

import android.util.Log;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.api.DataManager;
import org.mifos.selfserviceapp.data.accounts.SavingAccount;
import org.mifos.selfserviceapp.presenters.base.BasePresenter;
import org.mifos.selfserviceapp.ui.views.SavingAccountsListView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Vishwajeet
 * @since 21/6/16.
 */
public class SavingAccountsListPresenter extends BasePresenter<SavingAccountsListView> {
    DataManager mDataManager;

    public SavingAccountsListPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    public void fetchSavingAccounts(int id) {
        Call<SavingAccount> call = mDataManager.getSavingAccounts(id);
        getMvpView().showProgress();
        call.enqueue(new Callback<SavingAccount>() {
            @Override
            public void onResponse(Response<SavingAccount> response) {
                getMvpView().hideProgress();
                getMvpView().showSavingAccounts(response);
            }

            @Override
            public void onFailure(Throwable t) {
                getMvpView().hideProgress();
                getMvpView().showErrorFetchingSavingAccounts(context.getString(R.string.error_fetching_saving_accounts_list));
                Log.e("Error",context.getString(R.string.error_message_server));
            }
        });
    }
}
