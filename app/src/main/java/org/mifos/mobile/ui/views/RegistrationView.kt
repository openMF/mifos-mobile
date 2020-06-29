package org.mifos.mobile.ui.views

import org.mifos.mobile.ui.views.base.MVPView

/**
 * Created by dilpreet on 31/7/17.
 */
interface RegistrationView : MVPView {
    fun showRegisteredSuccessfully()
    fun showError(msg: String?)
}