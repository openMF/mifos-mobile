/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.auth.privacyNotice

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.ui.composableWithStayTransitions

@Serializable
data class PrivacyNoticeNavigation(
    val nextRoute: String? = null,
)

fun NavController.navigateToPrivacyNoticeScreen(
    nextRoute: String? = null,
    navOptions: NavOptions? = null,
) {
    this.navigate(PrivacyNoticeNavigation(nextRoute), navOptions)
}

fun NavGraphBuilder.privacyNoticeDestination(
    onAcceptClick: () -> Unit,
    onDeclineClick: () -> Unit,
) {
    composableWithStayTransitions<PrivacyNoticeNavigation> {
        PrivacyNoticeScreen(
            onAcceptClick = onAcceptClick,
            onDeclineClick = onDeclineClick,
        )
    }
}
