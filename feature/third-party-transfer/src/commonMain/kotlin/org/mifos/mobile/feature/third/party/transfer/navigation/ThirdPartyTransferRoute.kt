/*
 * Copyright 2026 Mifos Initiative
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
 * Sealed class representing the possible navigation destinations within the Third Party Transfer feature.
 */
sealed class TptNavigationDestination {
    /** Represents the notification screen. */
    object Notification : TptNavigationDestination()

    /** Represents the transfer process screen, carrying a [ReviewTransferPayload]. */
    class TransferProcess(val payload: ReviewTransferPayload) : TptNavigationDestination()

    /** Represents the add beneficiary screen. */
    object AddBeneficiaryScreen : TptNavigationDestination()
}

/**
 * Type alias for a navigator function that handles [TptNavigationDestination].
 */
typealias TptNavigator = (TptNavigationDestination) -> Unit

/**
 * Serializable data object representing the root route for the Third Party Transfer navigation graph.
 */
@Serializable
data object ThirdPartyTransferNavGraphRoute

/**
 * Navigates to the Third Party Transfer navigation graph.
 *
 * @param navOptions Optional navigation options for the graph.
 */
fun NavController.navigateToTptGraph(navOptions: NavOptions? = null) {
    this.navigate(
        ThirdPartyTransferNavGraphRoute,
        navOptions = navOptions,
    )
}

/**
 * Builds the navigation graph for the Third Party Transfer feature.
 *
 * @param onNavigate A [TptNavigator] function to handle navigation events within the graph.
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
