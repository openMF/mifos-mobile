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
import mifos_mobile.feature.accounts.generated.resources.feature_account_matured
import mifos_mobile.feature.accounts.generated.resources.feature_account_overpaid
import mifos_mobile.feature.accounts.generated.resources.feature_account_rejected
import mifos_mobile.feature.accounts.generated.resources.feature_account_withdrawn
import org.mifos.mobile.feature.accounts.model.CheckboxStatus

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
                Res.string.feature_account_matured,
            ),
            CheckboxStatus(
                Res.string.feature_account_closed,
            ),
            CheckboxStatus(
                Res.string.feature_account_rejected,
            ),
        )
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
        )
    }
}
