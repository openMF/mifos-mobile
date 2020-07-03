package org.mifos.mobile.ui.views

import org.mifos.mobile.models.accounts.loan.LoanWithAssociations
import org.mifos.mobile.ui.views.base.MVPView

/**
 * Created by Rajan Maurya on 03/03/17.
 */
interface LoanRepaymentScheduleMvpView : MVPView {
    fun showUserInterface()
    fun showLoanRepaymentSchedule(loanWithAssociations: LoanWithAssociations?)
    fun showEmptyRepaymentsSchedule(loanWithAssociations: LoanWithAssociations?)
    fun showError(message: String?)
}