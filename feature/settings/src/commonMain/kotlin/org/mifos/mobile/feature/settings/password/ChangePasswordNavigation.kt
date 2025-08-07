package org.mifos.mobile.feature.settings.password

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import org.mifos.mobile.core.ui.composableWithPushTransitions
import org.mifos.mobile.feature.settings.componenets.SettingsItems

fun NavController.navigateToUpdatePassword(navOptions: NavOptions? = null) =
    navigate(SettingsItems.Password, navOptions)

internal fun NavGraphBuilder.changePasswordDestination(
    onBackClick: () -> Unit,
    navigateToLogin: () -> Unit,
) {
    composableWithPushTransitions<SettingsItems.Password> {
        ChangePasswordScreen(
            navigateBack = onBackClick,
            navigateToLogin = navigateToLogin,
        )
    }
}