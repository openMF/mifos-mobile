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

/**
 * Navigation route for the Upload Documents screen.
 */
@Serializable
data object UploadDocsRoute

/**
 * Navigates to the Upload Documents screen.
 *
 * @param navOptions Optional [NavOptions] to configure navigation behavior.
 */
fun NavController.navigateToUploadDocsScreen(navOptions: NavOptions? = null) {
    this.navigate(route = UploadDocsRoute, navOptions = navOptions)
}

/**
 * Adds the Upload Documents destination to the navigation graph.
 *
 * @param navigateBack Callback invoked when user requests to navigate back.
 * @param navigateToNext Callback invoked to proceed to the next screen in the flow.
 * @param navigateToPreviewDoc Callback invoked to preview uploaded documents.
 */
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
