package org.mifos.selfserviceapp.presenters;

import android.content.Context;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.api.DataManager;
import org.mifos.selfserviceapp.data.FundTransfer.FundTransferRequest;
import org.mifos.selfserviceapp.data.FundTransfer.FundTransferResponse;
import org.mifos.selfserviceapp.data.FundTransfer.FundTransferTemplate;
import org.mifos.selfserviceapp.data.FundTransfer.FundTransferTemplateResponse;
import org.mifos.selfserviceapp.injection.ActivityContext;
import org.mifos.selfserviceapp.presenters.base.BasePresenter;
import org.mifos.selfserviceapp.ui.views.FundTransferView;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Vishwajeet
 * @since 22/8/16
 */

public class FundTransferPresenter extends BasePresenter<FundTransferView> {
    private DataManager dataManager;

    /**
     * Initialises the FundTransferPresenter by automatically injecting an instance of
     * {@link DataManager} and {@link Context}.
     *
     * @param dataManager DataManager class that provides access to the data
     *                    via the API.
     * @param context     Context of the view attached to the presenter. In this case
     *                    it is that of an {@link android.support.v7.app.AppCompatActivity}
     */

    @Inject
    public FundTransferPresenter(DataManager dataManager, @ActivityContext Context context) {
        super(context);
        this.dataManager = dataManager;
    }

    public void loadAccountTransferTemplate() {
        Call<FundTransferTemplateResponse> call = dataManager.getFundTransferTemplate();
        getMvpView().hideProgress();

        call.enqueue(new Callback<FundTransferTemplateResponse>() {
            @Override
            public void onResponse(Response<FundTransferTemplateResponse> response) {
                getMvpView().hideProgress();

                if (response.code() == 200) {
                    FundTransferTemplateResponse fundTransfer = response.body();
                    //  List<LoanAccount> loanAccountsList = response.body().getLoanAccounts();
                    if (fundTransfer != null) {
                        getMvpView().showFundTransferTemplate(fundTransfer);
                    }

                } else if (response.code() >= 400 && response.code() < 500) {
                    getMvpView().showErrorFetchingFundTransfers(context.getString(R.string.error_loan_accounts_list_loading));
                } else if (response.code() == 500) {
                    getMvpView().showErrorFetchingFundTransfers(context.getString(R.string.error_internal_server));
                }
            }

            @Override
            public void onFailure(Throwable t) {
                getMvpView().hideProgress();
                getMvpView().showErrorFetchingFundTransfers(context.getString(R.string.error_message_server));
            }
        });
    }

    public void submitTransfer(FundTransferRequest fundTransferRequest) {
        Call<FundTransferResponse> call = dataManager.submitTransfer(fundTransferRequest);
        getMvpView().showProgress();

        call.enqueue(new Callback<FundTransferResponse>() {
            @Override
            public void onResponse(Response<FundTransferResponse> response) {
                if (response.code() == 200) {
                    FundTransferResponse fundTransferResponse = response.body();
                    if (fundTransferResponse != null) {
                        getMvpView().submitPayment(fundTransferResponse);
                    }

                } else if (response.code() >= 400 && response.code() < 500) {
                    getMvpView().showErrorFetchingFundTransfers(context.getString(R.string.error_fund_transfer));
                } else if (response.code() == 500) {
                    getMvpView().showErrorFetchingFundTransfers(context.getString(R.string.error_internal_server));
                }
            }

            @Override
            public void onFailure(Throwable t) {
                getMvpView().hideProgress();
                getMvpView().showErrorFetchingFundTransfers(context.getString(R.string.error_message_server));
            }
        });
    }
}
