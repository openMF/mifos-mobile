package org.mifos.selfserviceapp.login;

import android.util.Log;

import org.mifos.selfserviceapp.data.User;
import org.mifos.selfserviceapp.api.DataManager;
import org.mifos.selfserviceapp.presenters.BasePresenter;
import org.mifos.selfserviceapp.utils.PrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Vishwajeet
 * @since 05/06/16
 */

public class LoginPresenter extends BasePresenter<LoginView> {

    DataManager mDataManager;

    protected LoginPresenter(DataManager dataManager) {
       mDataManager = dataManager;
    }


    public void login(String username, String password) {
        Call<User> call = mDataManager.login(username, password);
        getMvpView().showProgress();
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Response<User> response) {
                getMvpView().hideProgress();
                getMvpView().onLoginSuccessful(response);
            }

            @Override
            public void onFailure(Throwable t) {
                getMvpView().hideProgress();
                Log.e("Error","Error loading response from server");
            }
        });
    }

    public void setUserInfo(PrefManager prefManager, Response<User> response) {
        // Saving userID
        prefManager.setUserId(response.body().getUserId());
        // Saving user's token
        prefManager.saveToken(response.body().getBase64EncodedAuthenticationKey());
    }
}
