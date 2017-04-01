package org.mifos.selfserviceapp.ui.views;

import android.graphics.Bitmap;

import org.mifos.selfserviceapp.models.client.Client;
import org.mifos.selfserviceapp.ui.views.base.MVPView;

/**
 * Created by f390 on 14/3/17.
 */

public interface UserDetailsView extends MVPView {
    void gotoLoginPage();

    void setupProfilePage(Client currentClient);

    void setUserImage(Bitmap image);

    void showMessage(String message);
}
