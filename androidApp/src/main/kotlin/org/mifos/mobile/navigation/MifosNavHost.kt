/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import org.mifos.mobile.HomeActivity
import org.mifos.mobile.R
import org.mifos.mobile.core.common.Constants.TRANSFER_PAY_TO
import org.mifos.mobile.core.model.enums.AccountType
import org.mifos.mobile.core.model.enums.ChargeType
import org.mifos.mobile.feature.about.navigation.aboutUsNavGraph
import org.mifos.mobile.feature.about.navigation.navigateToAboutUsScreen
import org.mifos.mobile.feature.account.navigation.clientAccountsNavGraph
import org.mifos.mobile.feature.account.navigation.navigateToClientAccountsScreen
import org.mifos.mobile.feature.auth.navigation.navigateToLoginScreen
import org.mifos.mobile.feature.beneficiary.navigation.beneficiaryNavGraph
import org.mifos.mobile.feature.beneficiary.navigation.navigateToAddBeneficiaryScreen
import org.mifos.mobile.feature.beneficiary.navigation.navigateToBeneficiaryApplicationScreen
import org.mifos.mobile.feature.beneficiary.navigation.navigateToBeneficiaryListScreen
import org.mifos.mobile.feature.charge.navigation.clientChargeNavGraph
import org.mifos.mobile.feature.charge.navigation.navigateToClientChargeScreen
import org.mifos.mobile.feature.guarantor.navigation.guarantorNavGraph
import org.mifos.mobile.feature.guarantor.navigation.navigateToGuarantorScreen
import org.mifos.mobile.feature.help.navigation.helpNavGraph
import org.mifos.mobile.feature.help.navigation.navigateToHelpScreen
import org.mifos.mobile.feature.home.navigation.HomeDestinations
import org.mifos.mobile.feature.home.navigation.HomeNavigation
import org.mifos.mobile.feature.home.navigation.homeNavGraph
import org.mifos.mobile.feature.loan.navigation.loanNavGraph
import org.mifos.mobile.feature.loan.navigation.navigateToLoanApplication
import org.mifos.mobile.feature.loan.navigation.navigateToLoanDetailScreen
import org.mifos.mobile.feature.location.navigation.locationsNavGraph
import org.mifos.mobile.feature.location.navigation.navigateToLocationsScreen
import org.mifos.mobile.feature.notification.navigation.navigateToNotificationScreen
import org.mifos.mobile.feature.notification.navigation.notificationNavGraph
import org.mifos.mobile.feature.qr.navigation.navigateToQrDisplayScreen
import org.mifos.mobile.feature.qr.navigation.navigateToQrImportScreen
import org.mifos.mobile.feature.qr.navigation.navigateToQrReaderScreen
import org.mifos.mobile.feature.qr.navigation.qrNavGraph
import org.mifos.mobile.feature.savings.navigation.navigateToSavingsApplicationScreen
import org.mifos.mobile.feature.savings.navigation.navigateToSavingsDetailScreen
import org.mifos.mobile.feature.savings.navigation.navigateToSavingsMakeTransfer
import org.mifos.mobile.feature.savings.navigation.savingsNavGraph
import org.mifos.mobile.feature.settings.navigation.navigateToSettings
import org.mifos.mobile.feature.settings.navigation.settingsNavGraph
import org.mifos.mobile.feature.third.party.transfer.navigation.navigateToThirdPartyTransfer
import org.mifos.mobile.feature.third.party.transfer.navigation.thirdPartyTransferNavGraph
import org.mifos.mobile.feature.transaction.navigation.navigateToRecentTransaction
import org.mifos.mobile.feature.transaction.navigation.recentTransactionNavGraph
import org.mifos.mobile.feature.transfer.process.navigation.navigateToTransferProcessScreen
import org.mifos.mobile.feature.transfer.process.navigation.transferProcessNavGraph
import org.mifos.mobile.feature.update.password.navigation.navigateToUpdatePassword
import org.mifos.mobile.feature.update.password.navigation.updatePasswordNavGraph
import org.mifos.mobile.feature.user.profile.navigation.navigateToUserProfile
import org.mifos.mobile.feature.user.profile.navigation.userProfileNavGraph

@Composable
fun MifosNavHost(
    onClickLogout: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        route = MifosNavGraph.MAIN_GRAPH,
        startDestination = HomeNavigation.HomeBase.route,
        modifier = modifier,
    ) {
        homeNavGraph(
            onNavigate = { handleHomeNavigation(navController, it, onClickLogout, context) },
            callHelpline = { callHelpline(context) },
            mailHelpline = { mailHelpline(context) },
        )

        guarantorNavGraph(navController = navController)

        loanNavGraph(
            navController = navController,
            viewQr = navController::navigateToQrDisplayScreen,
            viewGuarantor = navController::navigateToGuarantorScreen,
            viewCharges = navController::navigateToClientChargeScreen,
            makePayment = navController::navigateToSavingsMakeTransfer,
        )

        userProfileNavGraph(
            navigateBack = navController::popBackStack,
            navigateToChangePassword = navController::navigateToUpdatePassword,
        )

        updatePasswordNavGraph(navigateBack = navController::popBackStack)

        thirdPartyTransferNavGraph(
            navigateBack = navController::popBackStack,
            addBeneficiary = navController::navigateToAddBeneficiaryScreen,
            reviewTransfer = navController::navigateToTransferProcessScreen,
        )

        settingsNavGraph(
            navigateBack = navController::popBackStack,
            changePassword = navController::navigateToUpdatePassword,
            changePasscode = {},
            navigateToLoginScreen = navController::navigateToLoginScreen,
            languageChanged = { startActivity(context, HomeActivity::class.java) },
        )

        recentTransactionNavGraph(navigateBack = navController::popBackStack)

        notificationNavGraph(navigateBack = navController::popBackStack)

        locationsNavGraph()

        helpNavGraph(
            findLocations = navController::navigateToLocationsScreen,
            navigateBack = navController::popBackStack,
            callHelpline = { callHelpline(context) },
            mailHelpline = { mailHelpline(context) },
        )

        clientChargeNavGraph(navigateBack = navController::popBackStack)

        aboutUsNavGraph(
            navController = navController,
            navigateToOssLicense = {
                context.startActivity(Intent(context, OssLicensesMenuActivity::class.java))
            },
        )

        transferProcessNavGraph(navigateBack = navController::popBackStack)

        beneficiaryNavGraph(
            navController = navController,
            openQrImportScreen = navController::navigateToQrImportScreen,
            openQrReaderScreen = navController::navigateToQrReaderScreen,
        )

        qrNavGraph(
            navController = navController,
            openBeneficiaryApplication = navController::navigateToBeneficiaryApplicationScreen,
        )

        savingsNavGraph(
            navController = navController,
            viewCharges = navController::navigateToClientChargeScreen,
            viewQrCode = navController::navigateToQrDisplayScreen,
            callHelpline = { callHelpline(context) },
            reviewTransfer = navController::navigateToTransferProcessScreen,
        )

        clientAccountsNavGraph(
            navController = navController,
            navigateToLoanApplicationScreen = navController::navigateToLoanApplication,
            navigateToSavingsApplicationScreen = navController::navigateToSavingsApplicationScreen,
            navigateToAccountDetail = { accountType, id ->
                when (accountType) {
                    AccountType.SAVINGS -> navController.navigateToSavingsDetailScreen(savingsId = id)
                    AccountType.LOAN -> navController.navigateToLoanDetailScreen(loanId = id)
                    AccountType.SHARE -> {}
                }
            },
        )
    }
}

fun handleHomeNavigation(
    navController: NavHostController,
    homeDestinations: HomeDestinations,
    onClickLogout: () -> Unit,
    context: Context,
) {
    when (homeDestinations) {
        HomeDestinations.HOME -> Unit
        HomeDestinations.ACCOUNTS -> navController.navigateToClientAccountsScreen()
        HomeDestinations.LOAN_ACCOUNT -> {
            navController.navigateToClientAccountsScreen(accountType = AccountType.LOAN)
        }

        HomeDestinations.SAVINGS_ACCOUNT -> {
            navController.navigateToClientAccountsScreen(accountType = AccountType.SAVINGS)
        }

        HomeDestinations.RECENT_TRANSACTIONS -> navController.navigateToRecentTransaction()
        HomeDestinations.CHARGES -> navController.navigateToClientChargeScreen(ChargeType.CLIENT)
        HomeDestinations.THIRD_PARTY_TRANSFER -> navController.navigateToThirdPartyTransfer()
        HomeDestinations.SETTINGS -> navController.navigateToSettings()
        HomeDestinations.ABOUT_US -> navController.navigateToAboutUsScreen()
        HomeDestinations.HELP -> navController.navigateToHelpScreen()
        HomeDestinations.SHARE -> {
            shareApp(context)
        }

        HomeDestinations.APP_INFO -> {
            openAppInfo(context)
        }

        HomeDestinations.LOGOUT -> onClickLogout.invoke()
        HomeDestinations.TRANSFER -> navController.navigateToSavingsMakeTransfer(
            accountId = 1,
            transferType = TRANSFER_PAY_TO,
        )

        HomeDestinations.BENEFICIARIES -> navController.navigateToBeneficiaryListScreen()
        HomeDestinations.SURVEY -> Unit
        HomeDestinations.NOTIFICATIONS -> navController.navigateToNotificationScreen()
        HomeDestinations.PROFILE -> navController.navigateToUserProfile()
    }
}

fun <T : Activity> startActivity(context: Context, clazz: Class<T>) {
    context.startActivity(Intent(context, clazz))
}

private fun callHelpline(context: Context) {
    val intent = Intent(Intent.ACTION_DIAL)
    intent.data =
        Uri.parse("tel:" + context.getString(org.mifos.mobile.feature.home.R.string.help_line_number))
    context.startActivity(intent)
}

private fun mailHelpline(context: Context) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:")
        putExtra(
            Intent.EXTRA_EMAIL,
            arrayOf(context.getString(org.mifos.mobile.feature.home.R.string.contact_email)),
        )
        putExtra(
            Intent.EXTRA_SUBJECT,
            context.getString(org.mifos.mobile.feature.home.R.string.user_query),
        )
    }
    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(
            context,
            context.getString(org.mifos.mobile.feature.home.R.string.no_app_to_support_action),
            Toast.LENGTH_SHORT,
        ).show()
    }
}

private fun openAppInfo(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.parse("package:${context.packageName}")
    }
    context.startActivity(intent)
}

private fun shareApp(context: Context) {
    val shareText = context.getString(
        R.string.playstore_link,
        context.getString(R.string.share_msg),
        context.packageName,
    )
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, shareText)
    }
    context.startActivity(Intent.createChooser(intent, context.getString(R.string.choose)))
}
