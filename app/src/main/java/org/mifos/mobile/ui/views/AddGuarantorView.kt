package org.mifos.mobile.ui.views

import org.mifos.mobile.models.guarantor.GuarantorApplicationPayload
import org.mifos.mobile.models.guarantor.GuarantorTemplatePayload
import org.mifos.mobile.ui.views.base.MVPView

/*
* Created by saksham on 23/July/2018
*/   interface AddGuarantorView : MVPView {
    fun updatedSuccessfully(message: String?)
    fun submittedSuccessfully(message: String?, payload: GuarantorApplicationPayload?)
    fun showGuarantorUpdation(template: GuarantorTemplatePayload?)
    fun showGuarantorApplication(template: GuarantorTemplatePayload?)
    fun showError(message: String?)
}