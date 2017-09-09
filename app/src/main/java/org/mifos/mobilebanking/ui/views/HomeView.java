package org.mifos.mobilebanking.ui.views;

import android.graphics.Bitmap;

import org.mifos.mobilebanking.ui.views.base.MVPView;

/**
 * Created by dilpreet on 19/6/17.
 */

public interface HomeView extends MVPView {

    void showUserDetails(String userName);

    void showUserImageTextDrawable();

    void showUserImage(Bitmap bitmap);

    void showNotificationCount(int count);

    void showError(String errorMessage);

    void showUserImageNotFound();

}
