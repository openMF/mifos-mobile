/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
@file:Suppress("MatchingDeclarationName")

package org.mifos.mobile.feature.status.navigation

import androidx.navigation.NavGraphBuilder
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.ui.composableWithStayTransitions
import org.mifos.mobile.feature.status.StatusScreen

@Serializable
data class StatusNavigationRoute(
    val eventType: String,
    val eventDestination: String,
    val title: String,
    val subtitle: String,
    val buttonText: String,
)

fun NavGraphBuilder.statusDestination(
    navigateToDestination: (String) -> Unit,
) {
    composableWithStayTransitions<StatusNavigationRoute> {
        StatusScreen(
            navigateToDestination = navigateToDestination,
        )
    }
}
