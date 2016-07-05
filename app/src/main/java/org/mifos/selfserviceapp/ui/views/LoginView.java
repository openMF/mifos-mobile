package org.mifos.selfserviceapp.ui.views;

import org.mifos.selfserviceapp.ui.views.base.MVPView;

/**
 * @author Vishwajeet
 * @since 05/06/16
 */

public interface LoginView extends MVPView {

    void onLoginSuccessful(String userName);

    void onLoginError(Throwable throwable);

    void showProgress();

    void hideProgress();

}
