/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.testing.util

/**
 * TestTags for UI testing across all features.
 *
 * Naming convention: {feature}:{component}:{element}
 * Example: "auth:login:usernameField"
 *
 * Usage in Compose:
 * ```kotlin
 * TextField(
 *     modifier = Modifier.testTag(TestTags.Auth.USERNAME_FIELD)
 * )
 * ```
 *
 * Usage in tests:
 * ```kotlin
 * composeTestRule.onNodeWithTag(TestTags.Auth.USERNAME_FIELD).assertIsDisplayed()
 * ```
 */
object TestTags {

    object Auth {
        private const val PREFIX = "auth"

        // Login Screen
        const val LOGIN_SCREEN = "$PREFIX:login:screen"
        const val USERNAME_FIELD = "$PREFIX:login:usernameField"
        const val PASSWORD_FIELD = "$PREFIX:login:passwordField"
        const val LOGIN_BUTTON = "$PREFIX:login:loginButton"
        const val ERROR_MESSAGE = "$PREFIX:login:errorMessage"
        const val LOADING_INDICATOR = "$PREFIX:login:loading"
        const val REGISTER_LINK = "$PREFIX:login:registerLink"
        const val FORGOT_PASSWORD_LINK = "$PREFIX:login:forgotPasswordLink"

        // Registration Screen
        const val REGISTRATION_SCREEN = "$PREFIX:registration:screen"
        const val ACCOUNT_NUMBER_FIELD = "$PREFIX:registration:accountNumber"
        const val FIRST_NAME_FIELD = "$PREFIX:registration:firstName"
        const val LAST_NAME_FIELD = "$PREFIX:registration:lastName"
        const val EMAIL_FIELD = "$PREFIX:registration:email"
        const val MOBILE_FIELD = "$PREFIX:registration:mobile"
        const val REGISTER_BUTTON = "$PREFIX:registration:registerButton"

        // OTP Screen
        const val OTP_SCREEN = "$PREFIX:otp:screen"
        const val OTP_FIELD = "$PREFIX:otp:otpField"
        const val VERIFY_BUTTON = "$PREFIX:otp:verifyButton"
        const val RESEND_BUTTON = "$PREFIX:otp:resendButton"

        // Password Screen
        const val PASSWORD_SCREEN = "$PREFIX:password:screen"
        const val NEW_PASSWORD_FIELD = "$PREFIX:password:newPassword"
        const val CONFIRM_PASSWORD_FIELD = "$PREFIX:password:confirmPassword"
        const val SET_PASSWORD_BUTTON = "$PREFIX:password:setButton"
    }

    object Home {
        private const val PREFIX = "home"

        const val SCREEN = "$PREFIX:screen"
        const val USER_NAME = "$PREFIX:userName"
        const val CLIENT_IMAGE = "$PREFIX:clientImage"
        const val LOAN_BALANCE = "$PREFIX:loanBalance"
        const val SAVINGS_BALANCE = "$PREFIX:savingsBalance"
        const val SHARE_BALANCE = "$PREFIX:shareBalance"
        const val QUICK_ACTIONS = "$PREFIX:quickActions"
        const val RECENT_TRANSACTIONS = "$PREFIX:recentTransactions"
        const val REFRESH_INDICATOR = "$PREFIX:refreshIndicator"
    }

    object Accounts {
        private const val PREFIX = "accounts"

        const val SCREEN = "$PREFIX:screen"
        const val TAB_BAR = "$PREFIX:tabBar"
        const val SAVINGS_TAB = "$PREFIX:tab:savings"
        const val LOANS_TAB = "$PREFIX:tab:loans"
        const val SHARES_TAB = "$PREFIX:tab:shares"
        const val ACCOUNT_LIST = "$PREFIX:accountList"
        const val ACCOUNT_ITEM = "$PREFIX:accountItem"
        const val EMPTY_STATE = "$PREFIX:emptyState"
        const val ERROR_STATE = "$PREFIX:errorState"
        const val LOADING_STATE = "$PREFIX:loadingState"
    }

    object SavingsAccount {
        private const val PREFIX = "savings"

        const val DETAIL_SCREEN = "$PREFIX:detail:screen"
        const val ACCOUNT_NUMBER = "$PREFIX:detail:accountNumber"
        const val BALANCE = "$PREFIX:detail:balance"
        const val STATUS = "$PREFIX:detail:status"
        const val TRANSACTION_LIST = "$PREFIX:detail:transactionList"
        const val DEPOSIT_BUTTON = "$PREFIX:detail:depositButton"
        const val WITHDRAW_BUTTON = "$PREFIX:detail:withdrawButton"
        const val TRANSFER_BUTTON = "$PREFIX:detail:transferButton"

        const val DEPOSIT_SCREEN = "$PREFIX:deposit:screen"
        const val AMOUNT_FIELD = "$PREFIX:deposit:amountField"
        const val SUBMIT_BUTTON = "$PREFIX:deposit:submitButton"

        const val WITHDRAW_SCREEN = "$PREFIX:withdraw:screen"
    }

    object LoanAccount {
        private const val PREFIX = "loan"

        const val DETAIL_SCREEN = "$PREFIX:detail:screen"
        const val ACCOUNT_NUMBER = "$PREFIX:detail:accountNumber"
        const val PRINCIPAL = "$PREFIX:detail:principal"
        const val OUTSTANDING = "$PREFIX:detail:outstanding"
        const val STATUS = "$PREFIX:detail:status"
        const val SCHEDULE_TAB = "$PREFIX:detail:scheduleTab"
        const val TRANSACTIONS_TAB = "$PREFIX:detail:transactionsTab"
        const val REPAYMENT_BUTTON = "$PREFIX:detail:repaymentButton"

        const val SCHEDULE_SCREEN = "$PREFIX:schedule:screen"
        const val SCHEDULE_LIST = "$PREFIX:schedule:list"
        const val SCHEDULE_ITEM = "$PREFIX:schedule:item"

        const val SUMMARY_SCREEN = "$PREFIX:summary:screen"
    }

    object ShareAccount {
        private const val PREFIX = "share"

        const val DETAIL_SCREEN = "$PREFIX:detail:screen"
        const val ACCOUNT_NUMBER = "$PREFIX:detail:accountNumber"
        const val SHARES_COUNT = "$PREFIX:detail:sharesCount"
        const val STATUS = "$PREFIX:detail:status"
    }

    object Beneficiary {
        private const val PREFIX = "beneficiary"

        const val LIST_SCREEN = "$PREFIX:list:screen"
        const val BENEFICIARY_LIST = "$PREFIX:list:beneficiaryList"
        const val BENEFICIARY_ITEM = "$PREFIX:list:beneficiaryItem"
        const val ADD_FAB = "$PREFIX:list:addFab"
        const val EMPTY_STATE = "$PREFIX:list:emptyState"

        const val ADD_SCREEN = "$PREFIX:add:screen"
        const val NAME_FIELD = "$PREFIX:add:nameField"
        const val ACCOUNT_NUMBER_FIELD = "$PREFIX:add:accountNumberField"
        const val TRANSFER_LIMIT_FIELD = "$PREFIX:add:transferLimitField"
        const val SAVE_BUTTON = "$PREFIX:add:saveButton"

        const val DETAIL_SCREEN = "$PREFIX:detail:screen"
        const val EDIT_BUTTON = "$PREFIX:detail:editButton"
        const val DELETE_BUTTON = "$PREFIX:detail:deleteButton"
    }

    object Transfer {
        private const val PREFIX = "transfer"

        const val SCREEN = "$PREFIX:screen"
        const val FROM_ACCOUNT = "$PREFIX:fromAccount"
        const val TO_ACCOUNT = "$PREFIX:toAccount"
        const val AMOUNT_FIELD = "$PREFIX:amountField"
        const val REMARK_FIELD = "$PREFIX:remarkField"
        const val TRANSFER_BUTTON = "$PREFIX:transferButton"

        const val CONFIRMATION_SCREEN = "$PREFIX:confirmation:screen"
        const val CONFIRM_BUTTON = "$PREFIX:confirmation:confirmButton"
        const val CANCEL_BUTTON = "$PREFIX:confirmation:cancelButton"

        const val SUCCESS_SCREEN = "$PREFIX:success:screen"
        const val DONE_BUTTON = "$PREFIX:success:doneButton"
    }

    object RecentTransaction {
        private const val PREFIX = "recentTransaction"

        const val SCREEN = "$PREFIX:screen"
        const val TRANSACTION_LIST = "$PREFIX:transactionList"
        const val TRANSACTION_ITEM = "$PREFIX:transactionItem"
        const val EMPTY_STATE = "$PREFIX:emptyState"
        const val FILTER_BUTTON = "$PREFIX:filterButton"
    }

    object Notification {
        private const val PREFIX = "notification"

        const val SCREEN = "$PREFIX:screen"
        const val NOTIFICATION_LIST = "$PREFIX:notificationList"
        const val NOTIFICATION_ITEM = "$PREFIX:notificationItem"
        const val EMPTY_STATE = "$PREFIX:emptyState"
        const val MARK_READ_BUTTON = "$PREFIX:markReadButton"
    }

    object Settings {
        private const val PREFIX = "settings"

        const val SCREEN = "$PREFIX:screen"
        const val LANGUAGE_OPTION = "$PREFIX:languageOption"
        const val THEME_OPTION = "$PREFIX:themeOption"
        const val PASSCODE_OPTION = "$PREFIX:passcodeOption"
        const val NOTIFICATION_OPTION = "$PREFIX:notificationOption"
        const val ABOUT_OPTION = "$PREFIX:aboutOption"
        const val LOGOUT_BUTTON = "$PREFIX:logoutButton"

        const val CHANGE_PASSWORD_SCREEN = "$PREFIX:changePassword:screen"
        const val OLD_PASSWORD_FIELD = "$PREFIX:changePassword:oldPassword"
        const val NEW_PASSWORD_FIELD = "$PREFIX:changePassword:newPassword"
        const val CONFIRM_PASSWORD_FIELD = "$PREFIX:changePassword:confirmPassword"
        const val SUBMIT_BUTTON = "$PREFIX:changePassword:submitButton"
    }

    object Passcode {
        private const val PREFIX = "passcode"

        const val SCREEN = "$PREFIX:screen"
        const val PASSCODE_DOTS = "$PREFIX:passcodeDots"
        const val KEYPAD = "$PREFIX:keypad"
        const val DELETE_BUTTON = "$PREFIX:deleteButton"
        const val BIOMETRIC_BUTTON = "$PREFIX:biometricButton"
        const val FORGOT_PASSCODE_LINK = "$PREFIX:forgotPasscodeLink"
    }

    object Guarantor {
        private const val PREFIX = "guarantor"

        const val LIST_SCREEN = "$PREFIX:list:screen"
        const val GUARANTOR_LIST = "$PREFIX:list:guarantorList"
        const val GUARANTOR_ITEM = "$PREFIX:list:guarantorItem"
        const val ADD_FAB = "$PREFIX:list:addFab"

        const val ADD_SCREEN = "$PREFIX:add:screen"
        const val FIRST_NAME_FIELD = "$PREFIX:add:firstName"
        const val LAST_NAME_FIELD = "$PREFIX:add:lastName"
        const val SAVE_BUTTON = "$PREFIX:add:saveButton"

        const val DETAIL_SCREEN = "$PREFIX:detail:screen"
    }

    object QR {
        private const val PREFIX = "qr"

        const val SCREEN = "$PREFIX:screen"
        const val QR_IMAGE = "$PREFIX:qrImage"
        const val SHARE_BUTTON = "$PREFIX:shareButton"
        const val DOWNLOAD_BUTTON = "$PREFIX:downloadButton"

        const val SCANNER_SCREEN = "$PREFIX:scanner:screen"
        const val CAMERA_PREVIEW = "$PREFIX:scanner:cameraPreview"
        const val FLASH_BUTTON = "$PREFIX:scanner:flashButton"
    }

    object Location {
        private const val PREFIX = "location"

        const val SCREEN = "$PREFIX:screen"
        const val MAP_VIEW = "$PREFIX:mapView"
        const val BRANCH_LIST = "$PREFIX:branchList"
        const val BRANCH_ITEM = "$PREFIX:branchItem"
    }

    object ClientCharge {
        private const val PREFIX = "clientCharge"

        const val SCREEN = "$PREFIX:screen"
        const val CHARGE_LIST = "$PREFIX:chargeList"
        const val CHARGE_ITEM = "$PREFIX:chargeItem"
        const val EMPTY_STATE = "$PREFIX:emptyState"
    }

    object Dashboard {
        private const val PREFIX = "dashboard"

        const val SCREEN = "$PREFIX:screen"
        const val OVERVIEW_CARD = "$PREFIX:overviewCard"
        const val QUICK_ACTIONS = "$PREFIX:quickActions"
    }

    // Common components used across features
    object Common {
        private const val PREFIX = "common"

        const val LOADING_SCREEN = "$PREFIX:loadingScreen"
        const val ERROR_SCREEN = "$PREFIX:errorScreen"
        const val RETRY_BUTTON = "$PREFIX:retryButton"
        const val BACK_BUTTON = "$PREFIX:backButton"
        const val TOOLBAR = "$PREFIX:toolbar"
        const val SNACKBAR = "$PREFIX:snackbar"
        const val DIALOG = "$PREFIX:dialog"
        const val BOTTOM_SHEET = "$PREFIX:bottomSheet"
        const val PULL_REFRESH = "$PREFIX:pullRefresh"
    }
}
