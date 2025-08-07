package org.mifos.mobile.feature.settings.appInfo

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import org.mifos.mobile.core.ui.composableWithPushTransitions
import org.mifos.mobile.feature.settings.componenets.SettingsItems

internal fun NavController.navigateToAppInfo(navOptions: NavOptions? = null) =
    navigate(SettingsItems.AppInfo, navOptions)

internal fun NavGraphBuilder.appInfoDestination(
    onBackClick: () -> Unit,
    navigateToPrivacyPolicy: () -> Unit,
    navigateToTermsAndConditions: () -> Unit,
) {
    composableWithPushTransitions<SettingsItems.AppInfo> {
        AppInfoScreen(
            onBackClick = onBackClick,
            navigateToPrivacyPolicy = navigateToPrivacyPolicy,
            navigateToTermsAndConditions = navigateToTermsAndConditions,
        )
    }
}