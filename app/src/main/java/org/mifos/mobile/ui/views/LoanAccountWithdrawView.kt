package org.mifos.mobile.ui.views

import org.mifos.mobile.ui.views.base.MVPView

/**
 * Created by dilpreet on 7/6/17.
 */
interface LoanAccountWithdrawView : MVPView {
    fun showLoanAccountWithdrawSuccess()
    fun showLoanAccountWithdrawError(message: String?)
}