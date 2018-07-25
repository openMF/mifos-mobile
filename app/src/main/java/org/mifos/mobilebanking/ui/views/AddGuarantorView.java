package org.mifos.mobilebanking.ui.views;

/*
 * Created by saksham on 23/July/2018
 */

import org.mifos.mobilebanking.models.guarantor.GuarantorApplicationPayload;
import org.mifos.mobilebanking.models.guarantor.GuarantorTemplatePayload;
import org.mifos.mobilebanking.ui.views.base.MVPView;

public interface AddGuarantorView extends MVPView {

    void updatedSuccessfully(String message);
    void submittedSuccessfully(String message, GuarantorApplicationPayload payload);
    void showGuarantorUpdation(GuarantorTemplatePayload template);
    void showGuarantorApplication(GuarantorTemplatePayload template);
    void showError(String message);

}
