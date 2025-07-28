/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.savingsaccount.components

import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import mifos_mobile.feature.savings_account.generated.resources.Res
import mifos_mobile.feature.savings_account.generated.resources.feature_account_action_charges
import mifos_mobile.feature.savings_account.generated.resources.feature_account_action_charges_tip
import mifos_mobile.feature.savings_account.generated.resources.feature_account_action_deposit
import mifos_mobile.feature.savings_account.generated.resources.feature_account_action_deposit_tip
import mifos_mobile.feature.savings_account.generated.resources.feature_account_action_qr
import mifos_mobile.feature.savings_account.generated.resources.feature_account_action_qr_tip
import mifos_mobile.feature.savings_account.generated.resources.feature_account_action_transactions
import mifos_mobile.feature.savings_account.generated.resources.feature_account_action_transactions_tip
import mifos_mobile.feature.savings_account.generated.resources.feature_account_action_transfer
import mifos_mobile.feature.savings_account.generated.resources.feature_account_action_transfer_tip
import org.jetbrains.compose.resources.StringResource
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.designsystem.icon.MifosIcons

// TODO add some constants or enum class for routes and use
internal sealed class SavingsActionItems(
    val title: StringResource,
    val subTitle: StringResource,
    val icon: ImageVector,
    val route: String,
) {
    data object Deposit : SavingsActionItems(
        title = Res.string.feature_account_action_deposit,
        subTitle = Res.string.feature_account_action_deposit_tip,
        icon = MifosIcons.Money,
        route = Constants.DEPOSIT,
    )

    data object Transfer : SavingsActionItems(
        title = Res.string.feature_account_action_transfer,
        subTitle = Res.string.feature_account_action_transfer_tip,
        icon = MifosIcons.MoneyHand,
        route = Constants.TRANSFER,
    )

    data object Transactions : SavingsActionItems(
        title = Res.string.feature_account_action_transactions,
        subTitle = Res.string.feature_account_action_transactions_tip,
        icon = MifosIcons.ChatHistory,
        route = Constants.TRANSACTIONS,
    )

    data object Charges : SavingsActionItems(
        title = Res.string.feature_account_action_charges,
        subTitle = Res.string.feature_account_action_charges_tip,
        icon = MifosIcons.ReceiptMoney,
        route = Constants.CHARGES,
    )

    data object QrCode : SavingsActionItems(
        title = Res.string.feature_account_action_qr,
        subTitle = Res.string.feature_account_action_qr_tip,
        icon = MifosIcons.QrCode,
        route = Constants.QR_CODE,
    )
}

internal val savingsAccountActions: ImmutableList<SavingsActionItems> = persistentListOf(
    SavingsActionItems.Deposit,
    SavingsActionItems.Transfer,
    SavingsActionItems.Transactions,
    SavingsActionItems.Charges,
    SavingsActionItems.QrCode,
)
