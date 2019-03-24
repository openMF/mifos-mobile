package org.mifos.mobile.ui.views;

import org.mifos.mobile.models.templates.beneficiary.BeneficiaryTemplate;
import org.mifos.mobile.ui.views.base.MVPView;

/**
 * Created by dilpreet on 16/6/17.
 */

public interface BeneficiaryApplicationView extends MVPView {

    void showUserInterface();

    void showBeneficiaryTemplate(BeneficiaryTemplate beneficiaryTemplate);

    void showBeneficiaryCreatedSuccessfully();

    void showBeneficiaryUpdatedSuccessfully();

    void showError(String msg);

    void setVisibility(int state);
}
