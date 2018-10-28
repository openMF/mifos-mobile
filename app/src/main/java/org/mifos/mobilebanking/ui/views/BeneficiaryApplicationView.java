package org.mifos.mobilebanking.ui.views;

import org.mifos.mobilebanking.models.templates.beneficiary.BeneficiaryTemplate;
import org.mifos.mobilebanking.ui.views.base.MVPView;

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
