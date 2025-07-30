/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.home

import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import mifos_mobile.feature.home.generated.resources.Res
import mifos_mobile.feature.home.generated.resources.feature_home_apply_for_loan
import mifos_mobile.feature.home.generated.resources.feature_home_beneficiary
import mifos_mobile.feature.home.generated.resources.feature_home_charges
import mifos_mobile.feature.home.generated.resources.feature_home_faq
import mifos_mobile.feature.home.generated.resources.feature_home_loan_accounts
import mifos_mobile.feature.home.generated.resources.feature_home_saving_accounts
import mifos_mobile.feature.home.generated.resources.feature_home_share_accounts
import mifos_mobile.feature.home.generated.resources.feature_home_transaction_history
import org.jetbrains.compose.resources.StringResource
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.designsystem.icon.MifosIcons

internal sealed class ServiceItem(
    val title: StringResource,
    val icon: ImageVector,
    val route: String,
) {
    data object SavingsAccount : ServiceItem(
        title = Res.string.feature_home_saving_accounts,
        icon = MifosIcons.SavingsAccount,
        route = Constants.SAVINGS_ACCOUNT,
    )

    data object LoanAccount : ServiceItem(
        title = Res.string.feature_home_loan_accounts,
        icon = MifosIcons.LoanAccount,
        route = Constants.LOAN_ACCOUNT,
    )

    data object ShareAccount : ServiceItem(
        title = Res.string.feature_home_share_accounts,
        icon = MifosIcons.ShareAccount,
        route = "ShareAccountRoute",
    )

    data object ApplyForLoan : ServiceItem(
        title = Res.string.feature_home_apply_for_loan,
        icon = MifosIcons.ApplyForLoan,
        route = "ApplyForLoanRoute",
    )

    data object TransactionHistory : ServiceItem(
        title = Res.string.feature_home_transaction_history,
        icon = MifosIcons.TransactionHistory,
        route = "TransactionHistoryRoute",
    )

    data object Charges : ServiceItem(
        title = Res.string.feature_home_charges,
        icon = MifosIcons.Charges,
        route = "ChargesRoute",
    )

    data object Beneficiary : ServiceItem(
        title = Res.string.feature_home_beneficiary,
        icon = MifosIcons.Beneficiary,
        route = "BeneficiaryRoute",
    )

    data object Faq : ServiceItem(
        title = Res.string.feature_home_faq,
        icon = MifosIcons.Faq,
        route = "FaqRoute",
    )
}

internal val serviceCards: ImmutableList<ServiceItem> = persistentListOf(
    ServiceItem.SavingsAccount,
    ServiceItem.LoanAccount,
    ServiceItem.ShareAccount,
    ServiceItem.ApplyForLoan,
    ServiceItem.TransactionHistory,
    ServiceItem.Charges,
    ServiceItem.Beneficiary,
    ServiceItem.Faq,
)
