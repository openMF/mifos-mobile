package org.mifos.mobile.feature.settings.about

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import org.mifos.mobile.core.ui.composableWithPushTransitions
import org.mifos.mobile.feature.settings.componenets.SettingsItems

internal fun NavGraphBuilder.aboutDestination(
    onBackClick: () -> Unit,
) {
    composableWithPushTransitions<SettingsItems.AboutUs> {
//        AboutScreen(
//            onBackClick = onBackClick,
//        )
    }
}

internal fun NavController.navigateToAbout(navOptions: NavOptions? = null) =
    navigate(SettingsItems.AboutUs, navOptions)