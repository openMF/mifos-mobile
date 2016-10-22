package org.mifos.selfserviceapp.presenters;

import android.content.Context;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.api.DataManager;
import org.mifos.selfserviceapp.models.accounts.LoanAccount;
import org.mifos.selfserviceapp.injection.ActivityContext;
import org.mifos.selfserviceapp.presenters.base.BasePresenter;
import org.mifos.selfserviceapp.ui.views.LoanAccountsDetailView;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Vishwajeet
 * @since 19/08/16
 */

public class LoanAccountsDetailPresenter extends BasePresenter<LoanAccountsDetailView> {
    private DataManager dataManager;

    /**
     * Initialises the LoanAccountDetailsPresenter by automatically injecting an instance of
     * {@link DataManager} and {@link Context}.
     *
     * @param dataManager DataManager class that provides access to the data
     *                    via the API.
     * @param context     Context of the view attached to the presenter. In this case
     *                    it is that of an {@link android.support.v7.app.AppCompatActivity}
     */
    @Inject
    public LoanAccountsDetailPresenter(DataManager dataManager, @ActivityContext Context context) {
        super(context);
        this.dataManager = dataManager;
    }

    /**
     * Load details of a particular loan account from the server and notify the view
     * to display it. Notify the view, in case there is any error in fetching
     * the details from server.
     */
    public void loadLoanAccountDetails(long loanId) {
        Call<LoanAccount> call = dataManager.getLoanAccountDetails(loanId);
        getMvpView().showProgress();
        call.enqueue(new Callback<LoanAccount>() {
            @Override
            public void onResponse(Response<LoanAccount> response) {
                getMvpView().hideProgress();

                if (response.code() == 200) {
                    final LoanAccount loanAccount = response.body();
                    if (loanAccount != null) {
                        getMvpView().showLoanAccountsDetail(loanAccount);
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
}
