package org.mifos.selfserviceapp.home;

import android.util.Log;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.api.DataManager;
import org.mifos.selfserviceapp.data.Client;
import org.mifos.selfserviceapp.presenters.BasePresenter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**
 * @author Vishwajeet
 * @since 19/06/16
 */

public class ClientListPresenter extends BasePresenter<ClientListMvpView>{

    DataManager mDataManager;

    public ClientListPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    public void loadClients() {
        Call<Client> call = mDataManager.getClients();
        getMvpView().showProgress();
        call.enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Response<Client> response) {
                getMvpView().hideProgress();
                getMvpView().showClients(response);
            }

            @Override
            public void onFailure(Throwable t) {
                getMvpView().hideProgress();
                getMvpView().showErrorFetchingClients(context.getString(R.string.error_fetching_client_list));
                Log.e("Error",context.getString(R.string.error_message_server));
            }
        });
    }
}
