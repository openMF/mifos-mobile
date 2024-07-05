package org.mifos.mobile.feature.account.utils

import org.mifos.mobile.core.model.entity.accounts.loan.LoanAccount
import org.mifos.mobile.core.model.entity.accounts.savings.SavingAccount
import org.mifos.mobile.core.model.entity.accounts.share.ShareAccount


sealed class AccountState {
    data object Error : AccountState()
    data object Loading : AccountState()
    data class ShowSavingsAccounts(val savingAccounts: List<SavingAccount>?) : AccountState()
    data class ShowLoanAccounts(val loanAccounts: List<LoanAccount>?) : AccountState()
    data class ShowShareAccounts(val shareAccounts: List<ShareAccount>?) : AccountState()
}