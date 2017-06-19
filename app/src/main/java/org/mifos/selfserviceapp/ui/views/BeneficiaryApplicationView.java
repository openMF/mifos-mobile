package org.mifos.selfserviceapp.ui.views;

import org.mifos.selfserviceapp.models.templates.beneficiary.BeneficiaryTemplate;
import org.mifos.selfserviceapp.ui.views.base.MVPView;

/**
 * Created by dilpreet on 16/6/17.
 */

public interface BeneficiaryApplicationView extends MVPView {

    void showUserInterface();

    void showBeneficiaryTemplate(BeneficiaryTemplate beneficiaryTemplate);

    void showBeneficiaryCreatedSuccessfully();

    void showBeneficiaryUpdatedSuccessfully();

    void showError(String msg);
}
