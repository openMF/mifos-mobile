package org.mifos.mobile.ui.views

import org.mifos.mobile.ui.views.base.MVPView

/*
* Created by saksham on 25/July/2018
*/   interface GuarantorDetailView : MVPView {
    fun guarantorDeletedSuccessfully(message: String?)
    fun showError(message: String?)
}