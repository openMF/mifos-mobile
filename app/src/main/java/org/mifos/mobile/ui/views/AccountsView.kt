package org.mifos.mobile.ui.views

import org.mifos.mobile.models.accounts.loan.LoanAccount
import org.mifos.mobile.models.accounts.savings.SavingAccount
import org.mifos.mobile.models.accounts.share.ShareAccount
import org.mifos.mobile.ui.views.base.MVPView

/**
 * Created by Rajan Maurya on 23/10/16.
 */
interface AccountsView : MVPView {
    fun showLoanAccounts(loanAccounts: List<LoanAccount?>?)
    fun showSavingsAccounts(savingAccounts: List<SavingAccount?>?)
    fun showShareAccounts(shareAccounts: List<ShareAccount?>?)
    fun showError(errorMessage: String?)
}