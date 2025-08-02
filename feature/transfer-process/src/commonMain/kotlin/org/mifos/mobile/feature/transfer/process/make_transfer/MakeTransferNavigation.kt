package org.mifos.mobile.feature.transfer.process.make_transfer

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.ui.composableWithSlideTransitions

@Serializable
data object MakeTransferRoute

fun NavController.navigateToMakeTransferScreen(navOptions: NavOptions? = null) =
    navigate(MakeTransferRoute, navOptions)

fun NavGraphBuilder.makeTransferDestination(
    navigateBack: () -> Unit,
) {
    composableWithSlideTransitions<MakeTransferRoute> {
        MakeTransferScreen(navigateBack)
    }
}