package org.mifos.mobile.ui.views;

import org.mifos.mobile.ui.views.base.MVPView;

/**
 * Created by dilpreet on 31/7/17.
 */

public interface RegistrationVerificationView extends MVPView {

    void showVerifiedSuccessfully();

    void showError(String msg);
}
