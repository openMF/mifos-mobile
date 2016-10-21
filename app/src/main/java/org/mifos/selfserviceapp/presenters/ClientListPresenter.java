package org.mifos.selfserviceapp.presenters;

import android.content.Context;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.api.DataManager;
import org.mifos.selfserviceapp.models.Client;
import org.mifos.selfserviceapp.injection.ActivityContext;
import org.mifos.selfserviceapp.presenters.base.BasePresenter;
import org.mifos.selfserviceapp.ui.views.ClientListView;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Vishwajeet
 * @since 14/07/16
 */

public class ClientListPresenter extends BasePresenter<ClientListView> {
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
    public ClientListPresenter(DataManager dataManager, @ActivityContext Context context) {
        super(context);
        this.dataManager = dataManager;
    }

    /**
     * Load list of client id's attached to particular self-service user from the server
     * and notify the view to display it. Notify the view, in case there is any error in fetching
     * the list from server.
     */
    public void loadClients() {
        Call<Client> call = dataManager.getClients();
        getMvpView().showProgress();
        call.enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Response<Client> response) {
                getMvpView().hideProgress();

                if (response.code() == 200) {
                    final Client client = response.body();
                    List<Client> clientList = response.body().getPageItems();
                    if (client != null) {
                        getMvpView().showClients(clientList);
                    }
                } else if (response.code() >= 400 && response.code() < 500) {
                    getMvpView().showErrorFetchingClients(
                            context.getString(R.string.error_client_loading));
                } else if (response.code() == 500) {
                    getMvpView().showErrorFetchingClients(
                            context.getString(R.string.error_internal_server));
                }
            }

            @Override
            public void onFailure(Throwable t) {
                getMvpView().hideProgress();
                getMvpView().showErrorFetchingClients(
                        context.getString(R.string.error_message_server));
            }
        });
    }
}
