package org.mifos.mobile.feature.client_charge.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import org.mifos.mobile.core.common.Constants.CHARGE_TYPE
import org.mifos.mobile.core.model.entity.client.ClientType
import org.mifos.mobile.core.model.enums.ChargeType
import org.mifos.mobile.feature.client_charge.screens.ClientChargeScreen

fun NavController.navigateToClientChargeScreen(
    chargeType: ChargeType,
) {
    navigate(ClientChargeNavigation.ClientChargeBase.passArguments(chargeType))
}

fun NavGraphBuilder.clientChargeNavGraph(
    navigateBack: () -> Unit,
) {
    navigation(
        startDestination = ClientChargeNavigation.ClientChargeScreen.route,
        route = ClientChargeNavigation.ClientChargeBase.route,
    ) {
        clientChargeScreenRoute(
            navigateBack = navigateBack,
        )
    }
}

fun NavGraphBuilder.clientChargeScreenRoute(
    navigateBack: () -> Unit,
) {
    composable(
        route = ClientChargeNavigation.ClientChargeScreen.route,
        arguments = listOf(navArgument(CHARGE_TYPE) { type = NavType.StringType })
    ) {
        ClientChargeScreen(
            navigateBack = navigateBack,
        )
    }
}