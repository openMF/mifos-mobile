package org.mifos.mobile.ui.views

import org.mifos.mobile.ui.views.base.MVPView

/*
* Created by saksham on 13/July/2018
*/   interface UpdatePasswordView : MVPView {
    fun showError(message: String?)
    fun showPasswordUpdatedSuccessfully()
}