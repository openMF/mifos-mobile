/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loanaccount.component

import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import mifos_mobile.feature.loan_account.generated.resources.Res
import mifos_mobile.feature.loan_account.generated.resources.feature_account_action_charges
import mifos_mobile.feature.loan_account.generated.resources.feature_account_action_charges_tip
import mifos_mobile.feature.loan_account.generated.resources.feature_account_action_loan_summary
import mifos_mobile.feature.loan_account.generated.resources.feature_account_action_loan_summary_tip
import mifos_mobile.feature.loan_account.generated.resources.feature_account_action_make_payment
import mifos_mobile.feature.loan_account.generated.resources.feature_account_action_make_payment_tip
import mifos_mobile.feature.loan_account.generated.resources.feature_account_action_qr
import mifos_mobile.feature.loan_account.generated.resources.feature_account_action_qr_tip
import mifos_mobile.feature.loan_account.generated.resources.feature_account_action_repayment_schedule
import mifos_mobile.feature.loan_account.generated.resources.feature_account_action_repayment_schedule_tip
import mifos_mobile.feature.loan_account.generated.resources.feature_account_action_transactions
import mifos_mobile.feature.loan_account.generated.resources.feature_account_action_transactions_tip
import org.jetbrains.compose.resources.StringResource
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.designsystem.icon.MifosIcons

sealed class LoanActionItems(
    val title: StringResource,
    val subTitle: StringResource,
    val icon: ImageVector,
    val route: String,
) {
    data object MakePayment : LoanActionItems(
        title = Res.string.feature_account_action_make_payment,
        subTitle = Res.string.feature_account_action_make_payment_tip,
        icon = MifosIcons.Money,
        route = Constants.MAKE_PAYMENT,
    )

    data object LoanSummary : LoanActionItems(
        title = Res.string.feature_account_action_loan_summary,
        subTitle = Res.string.feature_account_action_loan_summary_tip,
        icon = MifosIcons.MoneyHand,
        route = Constants.LOAN_SUMMARY,
    )

    data object RepaymentSchedule : LoanActionItems(
        title = Res.string.feature_account_action_repayment_schedule,
        subTitle = Res.string.feature_account_action_repayment_schedule_tip,
        icon = MifosIcons.MoneyHand,
        route = Constants.REPAYMENT_SCHEDULE,
    )

    data object Transactions : LoanActionItems(
        title = Res.string.feature_account_action_transactions,
        subTitle = Res.string.feature_account_action_transactions_tip,
        icon = MifosIcons.ChatHistory,
        route = Constants.TRANSACTIONS,
    )

    data object Charges : LoanActionItems(
        title = Res.string.feature_account_action_charges,
        subTitle = Res.string.feature_account_action_charges_tip,
        icon = MifosIcons.ReceiptMoney,
        route = Constants.CHARGES,
    )

    data object QrCode : LoanActionItems(
        title = Res.string.feature_account_action_qr,
        subTitle = Res.string.feature_account_action_qr_tip,
        icon = MifosIcons.QrCode,
        route = Constants.QR_CODE,
    )
}

internal val loanAccountActions: ImmutableList<LoanActionItems> = persistentListOf(
    LoanActionItems.MakePayment,
    LoanActionItems.LoanSummary,
    LoanActionItems.RepaymentSchedule,
    LoanActionItems.Transactions,
    LoanActionItems.Charges,
    LoanActionItems.QrCode,
)
