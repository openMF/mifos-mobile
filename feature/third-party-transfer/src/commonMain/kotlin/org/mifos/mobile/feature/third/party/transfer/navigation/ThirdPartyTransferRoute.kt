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

sealed class TptNavigationDestination {
    // Add more as needed
    object Notification : TptNavigationDestination()
    class TransferProcess(val payload: ReviewTransferPayload) : TptNavigationDestination()
    object AddBeneficiaryScreen : TptNavigationDestination()
}

typealias TptNavigator = (TptNavigationDestination) -> Unit

@Serializable
data object ThirdPartyTransferNavGraphRoute

fun NavController.navigateToTptGraph(navOptions: NavOptions? = null) {
    this.navigate(
        ThirdPartyTransferNavGraphRoute,
        navOptions = navOptions,
    )
}

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
