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

package org.mifos.mobile.feature.loan.application.uploadDocs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.ui.composableWithPushTransitions

@Serializable
data object UploadDocsRoute

fun NavController.navigateToUploadDocsScreen(navOptions: NavOptions? = null) {
    this.navigate(route = UploadDocsRoute, navOptions = navOptions)
}

fun NavGraphBuilder.uploadDocsDestination(
    navigateBack: () -> Unit,
    navigateToNext: () -> Unit,
    navigateToPreviewDoc: () -> Unit,
) {
    composableWithPushTransitions<UploadDocsRoute> {
        UploadDocsScreen(
            navigateBack = navigateBack,
            navigateToNext = navigateToNext,
            navigateToPreviewDoc = navigateToPreviewDoc,
        )
    }
}
