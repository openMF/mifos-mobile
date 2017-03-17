package org.mifos.selfserviceapp.ui.views;

import android.graphics.Bitmap;

import org.mifos.selfserviceapp.ui.views.base.MVPView;

/**
 * Created by f390 on 14/3/17.
 */

public interface HomeView extends MVPView {
    /**
     * This implements HomeView ,
     * it shows user Image and name in nav bar
     *
     * @param
     */
    void showMessage(String errorMessage);

    //gets user image and name
    void setUserName(String userName);

    void setUserImage(Bitmap image);

}
