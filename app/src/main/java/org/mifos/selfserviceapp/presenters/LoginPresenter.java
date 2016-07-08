package org.mifos.selfserviceapp.presenters;

import android.content.Context;
import android.util.Log;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.api.DataManager;
import org.mifos.selfserviceapp.data.User;
import org.mifos.selfserviceapp.presenters.base.BasePresenter;
import org.mifos.selfserviceapp.ui.views.LoginView;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Vishwajeet
 * @since 05/06/16
 */
public class LoginPresenter extends BasePresenter<LoginView> {

    private DataManager mDataManager;
    private Context context;

    @Inject
    public LoginPresenter(DataManager dataManager) {
        this.mDataManager = dataManager;
    }

    public void login(String username, String password) {
        Call<User> call = mDataManager.login(username, password);
        getMvpView().showProgress();
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Response<User> response) {
                getMvpView().hideProgress();
                getMvpView().onLoginSuccessful(response.body().getUserName());
                setUserInfo(response);
            }

            @Override
            public void onFailure(Throwable t) {
                getMvpView().hideProgress();
                getMvpView().onLoginError(t);
                Log.e("Error",context.getString(R.string.error_message_server));
            }
        });
    }

    public void setUserInfo(Response<User> response) {
        // Saving userID
//        getPref().setUserId(response.body().getUserId());
        // Saving user's token
//        getPref().saveToken("Basic "+ response.body().getBase64EncodedAuthenticationKey());
    }

}
