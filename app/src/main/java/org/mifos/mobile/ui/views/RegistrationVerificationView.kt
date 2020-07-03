package org.mifos.mobile.ui.views

import org.mifos.mobile.ui.views.base.MVPView

/**
 * Created by dilpreet on 31/7/17.
 */
interface RegistrationVerificationView : MVPView {
    fun showVerifiedSuccessfully()
    fun showError(msg: String?)
}