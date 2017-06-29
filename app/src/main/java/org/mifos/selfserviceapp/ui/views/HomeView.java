package org.mifos.selfserviceapp.ui.views;

import android.graphics.Bitmap;

import org.mifos.selfserviceapp.models.client.Client;
import org.mifos.selfserviceapp.ui.views.base.MVPView;

/**
 * Created by dilpreet on 19/6/17.
 */

public interface HomeView extends MVPView {

    void showUserInterface();

    void showLoanAccountDetails(double totalLoanAmount);

    void showSavingAccountDetails(double totalSavingAmount);

    void showUserDetails(Client client);

    void showUserImage(Bitmap bitmap);

    void showError(String errorMessage);

}
