/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.third.party.transfer.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.navigation
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.model.entity.payload.ReviewTransferPayload
import org.mifos.mobile.feature.third.party.transfer.thirdPartyTransfer.TptScreenRoute
import org.mifos.mobile.feature.third.party.transfer.thirdPartyTransfer.tptScreenDestination

/**
 * Sealed class representing the possible navigation destinations from the TPT screen.
 */
sealed class TptNavigationDestination {
    /**
     * Navigation destination for the Notification screen.
     */
    data object Notification : TptNavigationDestination()

    /**
     * Navigation destination for the Transfer Process screen.
     *
     * @param payload The payload containing the details of the transfer to be reviewed.
     */
    class TransferProcess(val payload: ReviewTransferPayload) : TptNavigationDestination()

    /**
     * Navigation destination for the Add Beneficiary screen.
     */
    data object AddBeneficiaryScreen : TptNavigationDestination()
}

/**
 * A type alias for the navigator function that handles navigation to a [TptNavigationDestination].
 */
typealias TptNavigator = (TptNavigationDestination) -> Unit

/**
 * The route for the Third Party Transfer navigation graph.
 */
@Serializable
data object ThirdPartyTransferNavGraphRoute

/**
 * Navigates to the TPT navigation graph.
 *
 * @param navOptions The navigation options to apply to this navigation.
 */
fun NavController.navigateToTptGraph(navOptions: NavOptions? = null) {
    this.navigate(
        ThirdPartyTransferNavGraphRoute,
        navOptions = navOptions,
    )
}

/**
 * Defines the TPT navigation graph.
 *
 * @param onNavigate The navigator function to handle navigation to other screens.
 */
fun NavGraphBuilder.tptGraphDestination(
    onNavigate: TptNavigator,
) {
    navigation<ThirdPartyTransferNavGraphRoute>(
        startDestination = TptScreenRoute,
    ) {
        tptScreenDestination(
            onNavigate = onNavigate,
        )
    }
}
