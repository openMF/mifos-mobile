package org.mifos.mobile.ui.views;

/*
 * Created by saksham on 23/July/2018
 */

import org.mifos.mobile.models.guarantor.GuarantorApplicationPayload;
import org.mifos.mobile.models.guarantor.GuarantorTemplatePayload;
import org.mifos.mobile.ui.views.base.MVPView;

public interface AddGuarantorView extends MVPView {

    void updatedSuccessfully(String message);
    void submittedSuccessfully(String message, GuarantorApplicationPayload payload);
    void showGuarantorUpdation(GuarantorTemplatePayload template);
    void showGuarantorApplication(GuarantorTemplatePayload template);
    void showError(String message);

}
