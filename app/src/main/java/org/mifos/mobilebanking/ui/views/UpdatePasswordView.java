package org.mifos.mobilebanking.ui.views;

/*
 * Created by saksham on 13/July/2018
 */

import org.mifos.mobilebanking.ui.views.base.MVPView;

public interface UpdatePasswordView extends MVPView {

    void showError(String message);

    void showPasswordUpdatedSuccessfully();
}
