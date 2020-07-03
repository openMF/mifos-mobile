package org.mifos.mobile.ui.views

import org.mifos.mobile.models.accounts.loan.LoanWithAssociations
import org.mifos.mobile.ui.views.base.MVPView

/**
 * @author Vishwajeet
 * @since 19/08/16
 */
interface LoanAccountsDetailView : MVPView {
    /**
     * Should be called when loan account object can successfully accessed
     * from the server to display loan account details on the screen.
     *
     * @param loanWithAssociations object containing details of each loan account,
     * received from server.
     */
    fun showLoanAccountsDetail(loanWithAssociations: LoanWithAssociations?)

    /**
     * Should be called if there is any error from client side in getting
     * loan account object from server.
     *
     *
     * Reason for error should be mentioned clearly to the user.
     *
     * @param message Error message to display showing reason of failure in getting
     * loan account object
     */
    fun showErrorFetchingLoanAccountsDetail(message: String?)
}