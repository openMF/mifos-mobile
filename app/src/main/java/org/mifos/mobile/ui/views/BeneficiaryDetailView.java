package org.mifos.mobile.ui.views;

import org.mifos.mobile.ui.views.base.MVPView;

/**
 * Created by dilpreet on 16/6/17.
 */

public interface BeneficiaryDetailView extends MVPView {

    void showUserInterface();

    void showBeneficiaryDeletedSuccessfully();

    void showError(String msg);
}
