package org.mifos.mobile.ui.views

import org.mifos.mobile.models.beneficiary.Beneficiary
import org.mifos.mobile.ui.views.base.MVPView

/**
 * Created by dilpreet on 14/6/17.
 */
interface BeneficiariesView : MVPView {
    fun showUserInterface()
    fun showError(msg: String?)
    fun showBeneficiaryList(beneficiaryList: List<Beneficiary?>?)
}