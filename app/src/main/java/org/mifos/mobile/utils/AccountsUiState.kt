package org.mifos.mobile.utils

import org.mifos.mobile.models.accounts.loan.LoanAccount
import org.mifos.mobile.models.accounts.savings.SavingAccount
import org.mifos.mobile.models.accounts.share.ShareAccount

sealed class AccountsUiState {
    object Error : AccountsUiState()
    object Loading : AccountsUiState()
    data class ShowSavingsAccounts(val savingAccounts: List<SavingAccount?>?) : AccountsUiState()
    data class ShowLoanAccounts(val loanAccounts: List<LoanAccount?>?) : AccountsUiState()
    data class ShowShareAccounts(val shareAccounts: List<ShareAccount?>?) : AccountsUiState()
}