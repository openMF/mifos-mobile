package org.mifos.mobile.feature.about.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.model.enums.AboutUsListItemId
import org.mifos.mobile.feature.about.ui.AboutUsScreen
import org.mifos.mobile.feature.about.ui.PrivacyPolicyScreen

fun NavController.navigateToAboutUsScreen() {
    navigate(AboutUsNavigation.AboutUsScreen.route)
}

fun NavGraphBuilder.aboutUsNavGraph(
    navController: NavController,
    navigateToOssLicense: () -> Unit
) {
    navigation(
        startDestination = AboutUsNavigation.AboutUsScreen.route,
        route = AboutUsNavigation.AboutUsBase.route,
    ) {
        aboutUsScreenRoute(
            navigateToPrivacyPolicy = { navController.navigate(AboutUsNavigation.PrivacyPolicyScreen.route) },
            navigateToOssLicense = navigateToOssLicense
        )

        privacyPolicyScreenRoute(
            navigateBack = navController::popBackStack
        )
    }
}

fun NavGraphBuilder.aboutUsScreenRoute(
    navigateToPrivacyPolicy: () -> Unit,
    navigateToOssLicense: () -> Unit
) {
    composable(
        route = AboutUsNavigation.AboutUsScreen.route,
    ) {
        val context = LocalContext.current

        AboutUsScreen(
            navigateToItem = {
                navigateToItem(
                    context = context,
                    aboutUsItem = it.itemId,
                    navigateToOssLicense = navigateToOssLicense,
                    navigateToPrivacyPolicy = navigateToPrivacyPolicy
                )
            }
        )
    }
}

fun NavGraphBuilder.privacyPolicyScreenRoute(
    navigateBack: () -> Unit
) {
    composable(
        route = AboutUsNavigation.PrivacyPolicyScreen.route,
    ) {
        PrivacyPolicyScreen(
            navigateBack = navigateBack
        )
    }
}

private fun navigateToItem(
    context: Context,
    aboutUsItem: AboutUsListItemId,
    navigateToPrivacyPolicy: () -> Unit,
    navigateToOssLicense: () -> Unit
) {
    when (aboutUsItem) {
        AboutUsListItemId.OFFICE_WEBSITE -> {
            startActivity(context, Constants.WEBSITE_LINK)
        }

        AboutUsListItemId.LICENSES -> {
            startActivity(context, Constants.LICENSE_LINK)
        }

        AboutUsListItemId.PRIVACY_POLICY -> {
            navigateToPrivacyPolicy()
        }

        AboutUsListItemId.SOURCE_CODE -> {
            startActivity(context, Constants.SOURCE_CODE_LINK)
        }

        AboutUsListItemId.LICENSES_STRING_WITH_VALUE -> {
            navigateToOssLicense()
        }

        AboutUsListItemId.APP_VERSION_TEXT -> Unit
    }
}

fun startActivity(context: Context, uri: String) {
    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))
}