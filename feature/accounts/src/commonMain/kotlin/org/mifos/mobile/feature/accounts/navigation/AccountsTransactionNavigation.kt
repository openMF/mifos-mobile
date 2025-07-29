package org.mifos.mobile.feature.accounts.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.ui.composableWithSlideTransitions
import org.mifos.mobile.feature.accounts.screen.AccountsScreen
import org.mifos.mobile.feature.accounts.screen.TransactionScreen

@Serializable
data class AccountTransactionsNavRoute(
    val accountType: String,
    val accountId:Long,
)

fun NavController.navigateToAccountTransactionsScreen(
    accountType: String,
    accountId:Long,
    navOptions: NavOptions? = null,
) {
    this.navigate(AccountTransactionsNavRoute(accountType,accountId), navOptions)
}

fun NavGraphBuilder.accountTransactionsDestination(
    navigateBack: () -> Unit,
) {
    composableWithSlideTransitions<AccountTransactionsNavRoute> {
        TransactionScreen(navigateBack)
    }
}
