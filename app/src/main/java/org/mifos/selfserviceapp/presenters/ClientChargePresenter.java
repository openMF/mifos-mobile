package org.mifos.selfserviceapp.presenters;

import android.content.Context;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.api.DataManager;
import org.mifos.selfserviceapp.models.Charge;
import org.mifos.selfserviceapp.models.ChargeListResponse;
import org.mifos.selfserviceapp.injection.ActivityContext;
import org.mifos.selfserviceapp.presenters.base.BasePresenter;
import org.mifos.selfserviceapp.ui.views.ClientChargeView;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Vishwajeet
 * @since 17/8/16.
 */
public class ClientChargePresenter extends BasePresenter<ClientChargeView> {
    private DataManager dataManager;

    /**
     * Initialises the ClientChargePresenter by automatically injecting an instance of
     * {@link DataManager} and {@link Context}.
     *
     * @param dataManager DataManager class that provides access to the data
     *                    via the API.
     * @param context     Context of the view attached to the presenter. In this case
     *                    it is that of an {@link android.support.v7.app.AppCompatActivity}
     */

    @Inject
    public ClientChargePresenter(DataManager dataManager, @ActivityContext Context context) {
        super(context);
        this.dataManager = dataManager;
    }

    public void loadClientCharges(long clientId) {
        Call<ChargeListResponse> call = dataManager.getClientCharges(clientId);
        getMvpView().showProgress();

        call.enqueue(new Callback<ChargeListResponse>() {
            @Override
            public void onResponse(Response<ChargeListResponse> response) {
                getMvpView().hideProgress();

                if (response.code() == 200) {
                    ChargeListResponse clientCharge = response.body();
                    List<Charge> clientChargeList = response.body().getPageItems();
                    if (clientCharge != null) {
                        getMvpView().showClientCharges(clientChargeList);
                    }

                } else if (response.code() >= 400 && response.code() < 500) {
                    getMvpView().showErrorFetchingClientCharges(
                            context.getString(R.string.error_client_charge_loading));
                } else if (response.code() == 500) {
                    getMvpView().showErrorFetchingClientCharges(
                            context.getString(R.string.error_internal_server));
                }
            }

            @Override
            public void onFailure(Throwable t) {
                getMvpView().hideProgress();
                getMvpView().showErrorFetchingClientCharges(
                        context.getString(R.string.error_message_server));
            }
        });
    }
}
