

package org.mifos.mobile.feature.loanaccount.loanAccountSummary

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.ui.composableWithSlideTransitions
import org.mifos.mobile.feature.loanaccount.loanAccount.LoanAccountScreen

@Serializable
data class AccountSummaryRoute(
    val accountId: Long
)

fun NavController.navigateToLoanAccountSummaryScreen(
    accountId: Long,
    navOptions: NavOptions? = null
) {
    navigate(AccountSummaryRoute(accountId), navOptions)
}

fun NavGraphBuilder.loanAccountSummaryDestination(
    navigateBack: () -> Unit,
) {
    composableWithSlideTransitions<AccountSummaryRoute> {
        LoanAccountSummaryScreen(
            navigateBack = navigateBack,
        )
    }
}
