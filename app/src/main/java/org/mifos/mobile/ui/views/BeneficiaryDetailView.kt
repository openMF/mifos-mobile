package org.mifos.mobile.ui.views

import org.mifos.mobile.ui.views.base.MVPView

/**
 * Created by dilpreet on 16/6/17.
 */
interface BeneficiaryDetailView : MVPView {
    fun showUserInterface()
    fun showBeneficiaryDeletedSuccessfully()
    fun showError(msg: String?)
}