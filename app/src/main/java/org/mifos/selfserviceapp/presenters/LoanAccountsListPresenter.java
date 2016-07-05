package org.mifos.selfserviceapp.presenters;

import android.util.Log;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.api.DataManager;
import org.mifos.selfserviceapp.data.accounts.LoanAccount;
import org.mifos.selfserviceapp.ui.views.LoanAccountsListView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Vishwajeet
 * @since 21/6/16.
 */

public class LoanAccountsListPresenter extends BasePresenter<LoanAccountsListView>{
    DataManager mDataManager;

    public LoanAccountsListPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    public void fetchLoanAccounts(int id) {
        Call<LoanAccount> call = mDataManager.getLoanAccounts(id);
        getMvpView().showProgress();
        call.enqueue(new Callback<LoanAccount>() {
            @Override
            public void onResponse(Response<LoanAccount> response) {
                getMvpView().hideProgress();
                getMvpView().showLoanAccounts(response);
            }

            @Override
            public void onFailure(Throwable t) {
                getMvpView().hideProgress();
                getMvpView().showErrorFetchingLoanAccounts(context.getString(R.string.error_fetching_loan_accounts_list));
                Log.e("Error",context.getString(R.string.error_message_server));
            }
        });
    }
}
