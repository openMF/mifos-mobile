package org.mifos.mobile.ui.views;

import android.graphics.Bitmap;

import org.mifos.mobile.models.client.Client;
import org.mifos.mobile.ui.views.base.MVPView;

/**
 * Created by dilpreet on 19/6/17.
 */

public interface HomeOldView extends MVPView {

    void showUserInterface();

    void showLoanAccountDetails(double totalLoanAmount);

    void showSavingAccountDetails(double totalSavingAmount);

    void showUserDetails(Client client);

    void showUserImage(Bitmap bitmap);

    void showNotificationCount(int count);

    void showError(String errorMessage);

}