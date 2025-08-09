package org.mifos.mobile.feature.settings.profile

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import org.mifos.mobile.core.ui.composableWithPushTransitions
import org.mifos.mobile.feature.settings.componenets.SettingsItems

internal fun NavController.navigateToProfile(navOptions: NavOptions? = null) =
    navigate(SettingsItems.Profile, navOptions)

internal fun NavGraphBuilder.profileDestination(
    onBackClick: () -> Unit,
) {
    composableWithPushTransitions<SettingsItems.Profile> {
        UpdateProfileScreen(
            navigateBack = onBackClick,
        )
    }
}