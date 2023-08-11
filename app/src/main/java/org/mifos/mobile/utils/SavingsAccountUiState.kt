package org.mifos.mobile.utils

import org.mifos.mobile.models.accounts.savings.SavingsWithAssociations
import org.mifos.mobile.models.accounts.savings.Transactions
import org.mifos.mobile.models.templates.account.AccountOptionsTemplate
import org.mifos.mobile.models.templates.savings.SavingsAccountTemplate

sealed class SavingsAccountUiState {
    object Loading : SavingsAccountUiState()
    object Error : SavingsAccountUiState()
    data class SuccessLoadingSavingsWithAssociations(val savingAccount: SavingsWithAssociations) :
        SavingsAccountUiState()

    data class ShowFilteredTransactionsList(val savingAccountsTransactionList: List<Transactions?>?) :
        SavingsAccountUiState()

    object SavingsAccountUpdateSuccess : SavingsAccountUiState()
    object SavingsAccountApplicationSuccess : SavingsAccountUiState()
    data class ShowUserInterfaceSavingAccountUpdate(val template: SavingsAccountTemplate) :
        SavingsAccountUiState()

    data class ShowUserInterfaceSavingAccountApplication(val template: SavingsAccountTemplate) :
        SavingsAccountUiState()

    data class ErrorMessage(val error: Throwable) : SavingsAccountUiState()
    object HideProgress : SavingsAccountUiState()
    object SavingsAccountWithdrawSuccess : SavingsAccountUiState()

    data class ShowSavingsAccountTemplate(val accountOptionsTemplate: AccountOptionsTemplate) : SavingsAccountUiState()
}
