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
/**
 * A sealed class representing the distinct user-facing actions available for a savings account.
 * Each action is defined as a `data object` and encapsulates UI-related metadata.
 *
 * This class is used to dynamically generate UI components, such as a list of action buttons or cards.
 *
 * @property title The string resource for the action's primary title.
 * @property subTitle The string resource for the action's descriptive subtitle or tip.
 * @property icon The [ImageVector] used to visually represent the action.
 * @property route A unique string identifier used for navigation or logic handling related to the action.
 */
sealed class SavingsActionItems(
    val title: StringResource,
    val subTitle: StringResource,
    val icon: ImageVector,
    val route: String,
) {
    /** Represents the action to initiate a money transfer from the account. */
    data object Transfer : SavingsActionItems(
        title = Res.string.feature_account_action_transfer,
        subTitle = Res.string.feature_account_action_transfer_tip,
        icon = MifosIcons.MoneyHand,
        route = Constants.TRANSFER,
    )

    /** Represents the action to view the transaction history of the account. */
    data object Transactions : SavingsActionItems(
        title = Res.string.feature_account_action_transactions,
        subTitle = Res.string.feature_account_action_transactions_tip,
        icon = MifosIcons.ChatHistory,
        route = Constants.TRANSACTIONS,
    )

    /** Represents the action to view any charges applied to the account. */
    data object Charges : SavingsActionItems(
        title = Res.string.feature_account_action_charges,
        subTitle = Res.string.feature_account_action_charges_tip,
        icon = MifosIcons.ReceiptMoney,
        route = Constants.CHARGES,
    )

    /** Represents the action to display a QR code associated with the account for receiving payments. */
    data object QrCode : SavingsActionItems(
        title = Res.string.feature_account_action_qr,
        subTitle = Res.string.feature_account_action_qr_tip,
        icon = MifosIcons.QrCode,
        route = Constants.QR_CODE,
    )
}

/**
 * An immutable list containing the standard set of actions available for a savings account.
 * This list is used to populate UI elements that display all possible account actions in a predefined order.
 */
internal val savingsAccountActions: ImmutableList<SavingsActionItems> = persistentListOf(
    SavingsActionItems.Transfer,
    SavingsActionItems.Transactions,
    SavingsActionItems.Charges,
    SavingsActionItems.QrCode,
)
