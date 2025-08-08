package org.mifos.mobile.feature.settings.faq

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import org.mifos.mobile.core.ui.composableWithPushTransitions
import org.mifos.mobile.feature.settings.componenets.SettingsItems

internal fun NavGraphBuilder.faqDestination(
    onBackClick: () -> Unit,
    contact: () -> Unit,
) {
    composableWithPushTransitions<SettingsItems.FAQ> {
        FaqScreen(
            onNavigateBack = onBackClick,
            onClickHelp = contact,
        )
    }
}
internal fun NavController.navigateToFaq(navOptions: NavOptions? = null) =
    navigate(SettingsItems.FAQ, navOptions)
