/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.settings.language

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import org.mifos.mobile.core.ui.composableWithPushTransitions
import org.mifos.mobile.feature.settings.componenets.SettingsItems

internal fun NavController.navigateToLanguageScreen(navOptions: NavOptions? = null) {
    this.navigate(SettingsItems.Language, navOptions)
}

internal fun NavGraphBuilder.languageDestination(
    navigateBack: () -> Unit,
) {
    composableWithPushTransitions<SettingsItems.Language> {
        LanguageScreen(
            navigateBack = navigateBack,
        )
    }
}
