package org.mifos.mobile.ui.views;

import android.graphics.Bitmap;

import org.mifos.mobile.models.client.Client;
import org.mifos.mobile.ui.views.base.MVPView;

/**
 * Created by naman on 07/04/17.
 */

public interface UserDetailsView extends MVPView {

    void showUserDetails(Client client);

    void showUserImage(Bitmap bitmap);

    void showError(String  message);
}
