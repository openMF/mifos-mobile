package org.mifos.mobile.ui.views

import org.mifos.mobile.models.accounts.loan.LoanWithAssociations
import org.mifos.mobile.ui.views.base.MVPView

/**
 * Created by dilpreet on 4/3/17.
 */
interface LoanAccountsTransactionView : MVPView {
    fun showUserInterface()
    fun showLoanTransactions(loanWithAssociations: LoanWithAssociations?)
    fun showEmptyTransactions(loanWithAssociations: LoanWithAssociations?)
    fun showErrorFetchingLoanAccountsDetail(message: String?)
}