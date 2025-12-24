/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.shareaccount.component

import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import mifos_mobile.feature.share_account.generated.resources.Res
import mifos_mobile.feature.share_account.generated.resources.feature_share_account_action_charges
import mifos_mobile.feature.share_account.generated.resources.feature_share_account_action_charges_subtitle
import mifos_mobile.feature.share_account.generated.resources.feature_share_account_action_qr_code
import mifos_mobile.feature.share_account.generated.resources.feature_share_account_action_qr_code_subtitle
import mifos_mobile.feature.share_account.generated.resources.feature_share_account_action_transactions
import mifos_mobile.feature.share_account.generated.resources.feature_share_account_action_transactions_subtitle
import org.jetbrains.compose.resources.StringResource
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.designsystem.icon.MifosIcons

/**
 * Defines the actionable items available for a Share Account.
 */
sealed interface ShareActionItems {
    val title: StringResource
    val subTitle: StringResource
    val icon: ImageVector
    val route: String

    data object Charges : ShareActionItems {
        override val title: StringResource = Res.string.feature_share_account_action_charges
        override val subTitle: StringResource = Res.string.feature_share_account_action_charges_subtitle
        override val icon: ImageVector = MifosIcons.Charges
        override val route: String = Constants.CHARGES
    }

    data object Transactions : ShareActionItems {
        override val title: StringResource = Res.string.feature_share_account_action_transactions
        override val subTitle: StringResource = Res.string.feature_share_account_action_transactions_subtitle
        override val icon: ImageVector = MifosIcons.TransactionHistory
        override val route: String = Constants.TRANSACTIONS
    }

    data object QrCode : ShareActionItems {
        override val title: StringResource = Res.string.feature_share_account_action_qr_code
        override val subTitle: StringResource = Res.string.feature_share_account_action_qr_code_subtitle
        override val icon: ImageVector = MifosIcons.QrCode
        override val route: String = Constants.QR_CODE
    }
}

/**
 * A persistent list of all possible share account actions.
 * This is used as the default state in the ViewModel.
 */
val shareAccountActions: PersistentList<ShareActionItems> = persistentListOf(
    ShareActionItems.Charges,
    ShareActionItems.Transactions,
    ShareActionItems.QrCode,
)
