package org.mifos.mobile.feature.charge.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.ui.composableWithStayTransitions
import org.mifos.mobile.feature.charge.screens.ChargeDetailScreen

@Serializable
data object ChargesDetailsRoute

fun NavGraphBuilder.chargesDetailsDestination(
    navigateToQrScreen: () -> Unit,
) {
    composableWithStayTransitions<ChargesDetailsRoute> {
        ChargeDetailScreen()
    }
}

fun NavController.navigateToChargesDetailsScreen() {
    this.navigate(ChargesDetailsRoute)
}