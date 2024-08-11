package org.mifos.mobile.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import org.mifos.mobile.core.common.Constants.CURR_PASSWORD
import org.mifos.mobile.core.common.Constants.INTIAL_LOGIN
import org.mifos.mobile.core.common.Constants.IS_TO_UPDATE_PASS_CODE
import org.mifos.mobile.core.model.enums.AccountType
import org.mifos.mobile.core.model.enums.ChargeType
import org.mifos.mobile.feature.about.navigation.aboutUsNavGraph
import org.mifos.mobile.feature.about.navigation.navigateToAboutUsScreen
import org.mifos.mobile.feature.account.navigation.clientAccountsNavGraph
import org.mifos.mobile.feature.account.navigation.navigateToClientAccountsScreen
import org.mifos.mobile.feature.auth.navigation.authenticationNavGraph
import org.mifos.mobile.feature.auth.navigation.navigateToLoginScreen
import org.mifos.mobile.feature.client_charge.navigation.clientChargeNavGraph
import org.mifos.mobile.feature.client_charge.navigation.navigateToClientChargeScreen
import org.mifos.mobile.feature.beneficiary.navigation.BeneficiaryNavigation
import org.mifos.mobile.feature.beneficiary.navigation.beneficiaryNavGraph
import org.mifos.mobile.feature.beneficiary.navigation.navigateToAddBeneficiaryScreen
import org.mifos.mobile.feature.beneficiary.navigation.navigateToBeneficiaryApplicationScreen
import org.mifos.mobile.feature.beneficiary.navigation.navigateToBeneficiaryListScreen
import org.mifos.mobile.feature.guarantor.navigation.guarantorNavGraph
import org.mifos.mobile.feature.guarantor.navigation.navigateToGuarantorScreen
import org.mifos.mobile.feature.help.navigation.helpNavGraph
import org.mifos.mobile.feature.help.navigation.navigateToHelpScreen
import org.mifos.mobile.feature.home.navigation.HomeDestinations
import org.mifos.mobile.feature.home.navigation.homeNavGraph
import org.mifos.mobile.feature.loan.navigation.loanNavGraph
import org.mifos.mobile.feature.loan.navigation.navigateToLoanApplication
import org.mifos.mobile.feature.loan.navigation.navigateToLoanDetailScreen
import org.mifos.mobile.feature.location.navigation.locationsNavGraph
import org.mifos.mobile.feature.location.navigation.navigateToLocationsScreen
import org.mifos.mobile.feature.notification.navigation.navigateToNotificationScreen
import org.mifos.mobile.feature.notification.navigation.notificationNavGraph
import org.mifos.mobile.feature.recent_transaction.navigation.navigateToRecentTransaction
import org.mifos.mobile.feature.recent_transaction.navigation.recentTransactionNavGraph
import org.mifos.mobile.feature.settings.navigation.navigateToSettings
import org.mifos.mobile.feature.settings.navigation.settingsNavGraph
import org.mifos.mobile.feature.third.party.transfer.third_party_transfer.navigation.navigateToThirdPartyTransfer
import org.mifos.mobile.feature.third.party.transfer.third_party_transfer.navigation.thirdPartyTransferNavGraph
import org.mifos.mobile.feature.transfer.process.navigation.transferProcessNavGraph
import org.mifos.mobile.feature.update_password.navigation.navigateToUpdatePassword
import org.mifos.mobile.feature.update_password.navigation.updatePasswordNavGraph
import org.mifos.mobile.feature.user_profile.navigation.navigateToUserProfile
import org.mifos.mobile.feature.user_profile.navigation.userProfileNavGraph
import org.mifos.mobile.feature.qr.navigation.QrNavigation
import org.mifos.mobile.feature.qr.navigation.navigateToQrDisplayScreen
import org.mifos.mobile.feature.qr.navigation.navigateToQrImportScreen
import org.mifos.mobile.feature.qr.navigation.navigateToQrReaderScreen
import org.mifos.mobile.feature.qr.navigation.qrNavGraph
import org.mifos.mobile.feature.savings.navigation.SavingsNavigation
import org.mifos.mobile.feature.savings.navigation.navigateToSavingsApplicationScreen
import org.mifos.mobile.feature.savings.navigation.navigateToSavingsDetailScreen
import org.mifos.mobile.feature.savings.navigation.navigateToSavingsMakeTransfer
import org.mifos.mobile.feature.savings.navigation.savingsNavGraph
import org.mifos.mobile.feature.transfer.process.navigation.navigateToTransferProcessScreen
import org.mifos.mobile.ui.activities.PassCodeActivity
import org.mifos.mobile.ui.activities.PrivacyPolicyActivity
import android.provider.Settings
import android.widget.Toast
import org.mifos.mobile.R
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.Constants.TRANSFER_PAY_FROM
import org.mifos.mobile.core.common.Constants.TRANSFER_PAY_TO
import org.mifos.mobile.core.model.enums.TransferType
import org.mifos.mobile.ui.activities.HomeActivity


@Composable
fun RootNavGraph(
    startDestination: String,
    navController: NavHostController,
) {
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        homeNavGraph(
            onNavigate = { handleHomeNavigation(navController, it, context) },
            callHelpline = { callHelpline(context) },
            mailHelpline = { mailHelpline(context) }
        )

        authenticationNavGraph(
            navController = navController,
            navigateToPasscodeScreen = { startPassCodeActivity(context = context) }
        )

        guarantorNavGraph(
            navController = navController,
        )

        loanNavGraph(
            navController = navController,
            viewQr = navController::navigateToQrDisplayScreen,
            viewGuarantor = navController::navigateToGuarantorScreen,
            viewCharges = navController::navigateToClientChargeScreen,
            makePayment = navController::navigateToSavingsMakeTransfer
        )

        userProfileNavGraph(
            navController = navController,
            navigateToChangePassword = navController::navigateToUpdatePassword
        )

        updatePasswordNavGraph(
            navController = navController,
        )

        thirdPartyTransferNavGraph(
            navController = navController,
            addBeneficiary = navController::navigateToAddBeneficiaryScreen,
            reviewTransfer = navController::navigateToTransferProcessScreen
        )

        settingsNavGraph(
            navController = navController,
            changePassword = navController::navigateToUpdatePassword,
            changePasscode = { navigateToUpdatePasscodeActivity(it, context) },
            navigateToLoginScreen = navController::navigateToLoginScreen,
            languageChanged = { startActivity(context, HomeActivity::class.java) }
        )

        recentTransactionNavGraph(
            navigateBack = navController::popBackStack
        )

        notificationNavGraph(
            navController = navController
        )

        locationsNavGraph(
            navController = navController
        )

        helpNavGraph(
            findLocations = navController::navigateToLocationsScreen,
            navigateBack = navController::popBackStack,
            callHelpline = { callHelpline(context) },
            mailHelpline = { mailHelpline(context) }
        )

        clientChargeNavGraph(
            navigateBack = navController::popBackStack
        )

        aboutUsNavGraph(
            navController = navController,
            navigateToOssLicense = { startActivity(context, OssLicensesMenuActivity::class.java) }
        )

        transferProcessNavGraph (
            navController = navController
        )

        beneficiaryNavGraph(
            navController = navController,
            openQrImportScreen = navController::navigateToQrImportScreen,
            openQrReaderScreen = navController::navigateToQrReaderScreen
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
            reviewTransfer = navController::navigateToTransferProcessScreen
        )

        clientAccountsNavGraph(
            navController = navController,
            navigateToLoanApplicationScreen = navController::navigateToLoanApplication,
            navigateToSavingsApplicationScreen = navController::navigateToSavingsApplicationScreen,
            navigateToAccountDetail = { accountType, id ->
                when(accountType) {
                    AccountType.SAVINGS -> navController.navigateToSavingsDetailScreen(savingsId = id)
                    AccountType.LOAN -> navController.navigateToLoanDetailScreen(loanId = id)
                    AccountType.SHARE -> {}
                }
            }
        )
    }
}

fun handleHomeNavigation(
    navController: NavHostController,
    homeDestinations: HomeDestinations,
    context: Context
) {
    when (homeDestinations) {
        HomeDestinations.HOME -> Unit
        HomeDestinations.ACCOUNTS -> navController.navigateToClientAccountsScreen()
        HomeDestinations.LOAN_ACCOUNT -> navController.navigateToClientAccountsScreen(accountType = AccountType.LOAN)
        HomeDestinations.SAVINGS_ACCOUNT -> navController.navigateToClientAccountsScreen(accountType = AccountType.SAVINGS)
        HomeDestinations.RECENT_TRANSACTIONS -> navController.navigateToRecentTransaction()
        HomeDestinations.CHARGES -> navController.navigateToClientChargeScreen(ChargeType.CLIENT)
        HomeDestinations.THIRD_PARTY_TRANSFER -> navController.navigateToThirdPartyTransfer()
        HomeDestinations.SETTINGS -> navController.navigateToSettings()
        HomeDestinations.ABOUT_US -> navController.navigateToAboutUsScreen()
        HomeDestinations.HELP -> navController.navigateToHelpScreen()
        HomeDestinations.SHARE -> { shareApp(context) }
        HomeDestinations.APP_INFO -> { openAppInfo(context) }
        HomeDestinations.LOGOUT -> navController.navigateToLoginScreen()
        HomeDestinations.TRANSFER -> navController.navigateToSavingsMakeTransfer(accountId = 1, transferType = TRANSFER_PAY_TO)
        HomeDestinations.BENEFICIARIES -> navController.navigateToBeneficiaryListScreen()
        HomeDestinations.SURVEY -> Unit
        HomeDestinations.NOTIFICATIONS -> navController.navigateToNotificationScreen()
        HomeDestinations.PROFILE -> navController.navigateToUserProfile()
    }
}

fun <T : Activity> startActivity(context: Context, clazz: Class<T>) {
    context.startActivity(Intent(context, clazz))
}

private fun startPassCodeActivity(context: Context) {
    val intent = Intent(context, PassCodeActivity::class.java)
    intent.putExtra(INTIAL_LOGIN, true)
    context.startActivity(intent)
}

private fun navigateToUpdatePasscodeActivity(passcode: String, context: Context) {
    val intent = Intent(context, PassCodeActivity::class.java).apply {
        putExtra(CURR_PASSWORD, passcode)
        putExtra(IS_TO_UPDATE_PASS_CODE, true)
    }
    context.startActivity(intent)
}

private fun callHelpline(context: Context) {
    val intent = Intent(Intent.ACTION_DIAL)
    intent.data = Uri.parse("tel:" + context.getString(org.mifos.mobile.feature.home.R.string.help_line_number))
    context.startActivity(intent)
}

private fun mailHelpline(context: Context) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:")
        putExtra(Intent.EXTRA_EMAIL, arrayOf(context.getString(org.mifos.mobile.feature.home.R.string.contact_email)))
        putExtra(Intent.EXTRA_SUBJECT, context.getString(org.mifos.mobile.feature.home.R.string.user_query))
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
