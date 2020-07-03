package org.mifos.mobile.ui.views

import org.mifos.mobile.models.guarantor.GuarantorPayload
import org.mifos.mobile.ui.views.base.MVPView

/*
* Created by saksham on 24/July/2018
*/   interface GuarantorListView : MVPView {
    fun showGuarantorListSuccessfully(list: List<GuarantorPayload?>?)
    fun showError(message: String?)
}