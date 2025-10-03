/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.savingsaccount.savingsAccountDetails

import org.mifos.mobile.core.common.Constants

// This defines the four specific options required for the menu.
sealed class SavingsDetailsOption(val label: String, val route: String) {
    data object Transactions : SavingsDetailsOption("Transactions", Constants.TRANSACTIONS)
    data object Charges : SavingsDetailsOption("Charges", Constants.CHARGES)
    data object QrCode : SavingsDetailsOption("QR Code", Constants.QR_CODE)
    data object TransactionInfo : SavingsDetailsOption("Transaction Info", Constants.TRANSACTION_INFO)
}

// A list to iterate over when building the UI.
val SAVINGS_DETAILS_OPTIONS = listOf(
    SavingsDetailsOption.Transactions,
    SavingsDetailsOption.Charges,
    SavingsDetailsOption.QrCode,
    SavingsDetailsOption.TransactionInfo,
)
