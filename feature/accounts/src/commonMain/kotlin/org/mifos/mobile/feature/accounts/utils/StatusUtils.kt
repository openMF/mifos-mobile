/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.accounts.utils

import mifos_mobile.feature.accounts.generated.resources.Res
import mifos_mobile.feature.accounts.generated.resources.feature_account_active
import mifos_mobile.feature.accounts.generated.resources.feature_account_approval_pending
import mifos_mobile.feature.accounts.generated.resources.feature_account_approved
import mifos_mobile.feature.accounts.generated.resources.feature_account_closed
import mifos_mobile.feature.accounts.generated.resources.feature_account_disburse
import mifos_mobile.feature.accounts.generated.resources.feature_account_in_arrears
import mifos_mobile.feature.accounts.generated.resources.feature_account_overpaid
import mifos_mobile.feature.accounts.generated.resources.feature_account_rejected
import mifos_mobile.feature.accounts.generated.resources.feature_account_withdrawn
import mifos_mobile.feature.accounts.generated.resources.feature_savings_filter_active_account
import mifos_mobile.feature.accounts.generated.resources.feature_savings_filter_approved_account
import mifos_mobile.feature.accounts.generated.resources.feature_savings_filter_bank_account
import mifos_mobile.feature.accounts.generated.resources.feature_savings_filter_closed_account
import mifos_mobile.feature.accounts.generated.resources.feature_savings_filter_group_account
import mifos_mobile.feature.accounts.generated.resources.feature_savings_filter_matured_account
import mifos_mobile.feature.accounts.generated.resources.feature_savings_filter_nb_account
import mifos_mobile.feature.accounts.generated.resources.feature_savings_filter_pending_account
import mifos_mobile.feature.accounts.generated.resources.feature_savings_filter_wallet_account
import org.mifos.mobile.feature.accounts.model.CheckboxStatus
import org.mifos.mobile.feature.accounts.model.FilterType

/**
 * Utility object that provides predefined checkbox options for different account types.
 *
 * These checkboxes are used in filtering loan, savings, and share accounts based on their statuses.
 */
object StatusUtils {

    /**
     * Retrieves the list of available checkbox statuses for savings accounts.
     *
     * @return A list of [CheckboxStatus] representing different savings account statuses.
     */
    internal fun getSavingsAccountCheckboxes(): List<CheckboxStatus> {
        val accountTypes = listOf(
            CheckboxStatus(Res.string.feature_savings_filter_wallet_account, type = FilterType.ACCOUNT_TYPE),
            CheckboxStatus(Res.string.feature_savings_filter_bank_account, type = FilterType.ACCOUNT_TYPE),
            CheckboxStatus(Res.string.feature_savings_filter_group_account, type = FilterType.ACCOUNT_TYPE),
            CheckboxStatus(Res.string.feature_savings_filter_nb_account, type = FilterType.ACCOUNT_TYPE),
        )

        val accountStatuses = listOf(
            CheckboxStatus(Res.string.feature_savings_filter_active_account, type = FilterType.ACCOUNT_STATUS),
            CheckboxStatus(Res.string.feature_savings_filter_pending_account, type = FilterType.ACCOUNT_STATUS),
            CheckboxStatus(Res.string.feature_savings_filter_closed_account, type = FilterType.ACCOUNT_STATUS),
            CheckboxStatus(Res.string.feature_savings_filter_matured_account, type = FilterType.ACCOUNT_STATUS),
            CheckboxStatus(Res.string.feature_savings_filter_approved_account, type = FilterType.ACCOUNT_STATUS),
        )

        return accountTypes + accountStatuses
    }

    /**
     * Retrieves the list of available checkbox statuses for loan accounts.
     *
     * @return A list of [CheckboxStatus] representing different loan account statuses.
     */
    internal fun getLoanAccountCheckboxes(): List<CheckboxStatus> {
        return listOf(
            CheckboxStatus(
                Res.string.feature_account_active,
            ),
            CheckboxStatus(
                Res.string.feature_account_disburse,
            ),
            CheckboxStatus(
                Res.string.feature_account_approval_pending,
            ),
            CheckboxStatus(
                Res.string.feature_account_overpaid,
            ),
            CheckboxStatus(
                Res.string.feature_account_closed,
            ),
            CheckboxStatus(
                Res.string.feature_account_in_arrears,
            ),
            CheckboxStatus(
                Res.string.feature_account_withdrawn,
            ),
        )
    }

    /**
     * Retrieves the list of available checkbox statuses for share accounts.
     *
     * @return A list of [CheckboxStatus] representing different share account statuses.
     */
    internal fun getShareAccountCheckboxes(): List<CheckboxStatus> {
        return listOf(
            CheckboxStatus(
                Res.string.feature_account_active,
            ),
            CheckboxStatus(
                Res.string.feature_account_approved,
            ),
            CheckboxStatus(
                Res.string.feature_account_approval_pending,
            ),
            CheckboxStatus(
                Res.string.feature_account_closed,
            ),
            CheckboxStatus(
                Res.string.feature_account_rejected,
            ),
        )
    }
}
