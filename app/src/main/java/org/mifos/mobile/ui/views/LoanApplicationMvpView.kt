package org.mifos.mobile.ui.views

import org.mifos.mobile.models.templates.loans.LoanTemplate
import org.mifos.mobile.ui.views.base.MVPView

/**
 * Created by Rajan Maurya on 06/03/17.
 */
interface LoanApplicationMvpView : MVPView {
    fun showUserInterface()
    fun showLoanTemplate(loanTemplate: LoanTemplate?)
    fun showUpdateLoanTemplate(loanTemplate: LoanTemplate?)
    fun showLoanTemplateByProduct(loanTemplate: LoanTemplate?)
    fun showUpdateLoanTemplateByProduct(loanTemplate: LoanTemplate?)
    fun showError(message: String?)
}