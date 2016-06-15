package org.mifos.selfserviceapp.login;

import org.mifos.selfserviceapp.data.User;
import org.mifos.selfserviceapp.ui.views.base.MVPView;

import retrofit2.Response;

/**
 * @author Vishwajeet
 * @since 05/06/16
 */

public interface LoginView extends MVPView {

    void onLoginSuccessful(Response<User> response);

    void onLoginError(Throwable throwable);

    void showProgress();

    void hideProgress();

}
