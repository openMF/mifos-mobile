package org.mifos.mobile.utils

import org.mifos.mobile.core.model.entity.accounts.loan.LoanWithAssociations
import org.mifos.mobile.core.model.entity.templates.loans.LoanTemplate

sealed class LoanUiState {
    object Loading : LoanUiState()
    object WithdrawSuccess : LoanUiState()
    data class ShowError(val message: Int) : LoanUiState()
    data class ShowLoan(val loanWithAssociations: LoanWithAssociations) : LoanUiState()
    data class ShowEmpty(val loanWithAssociations: LoanWithAssociations) : LoanUiState()
    data class ShowLoanTemplate(val template: LoanTemplate) : LoanUiState()
    data class ShowUpdateLoanTemplate(val template: LoanTemplate) : LoanUiState()
    data class ShowLoanTemplateByProduct(val template: LoanTemplate) : LoanUiState()
    data class ShowUpdateLoanTemplateByProduct(val template: LoanTemplate) : LoanUiState()
}