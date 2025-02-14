/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.account.utils

import mifos_mobile.feature.account.generated.resources.Res
import mifos_mobile.feature.account.generated.resources.feature_account_active
import mifos_mobile.feature.account.generated.resources.feature_account_approval_pending
import mifos_mobile.feature.account.generated.resources.feature_account_approved
import mifos_mobile.feature.account.generated.resources.feature_account_closed
import mifos_mobile.feature.account.generated.resources.feature_account_disburse
import mifos_mobile.feature.account.generated.resources.feature_account_in_arrears
import mifos_mobile.feature.account.generated.resources.feature_account_matured
import mifos_mobile.feature.account.generated.resources.feature_account_overpaid
import mifos_mobile.feature.account.generated.resources.feature_account_withdrawn
import org.jetbrains.compose.resources.StringResource

object StatusUtils {

    internal fun getSavingsAccountStatusList(): List<CheckboxStatus> {
        val arrayList = ArrayList<CheckboxStatus>()

        arrayList.add(
            CheckboxStatus(
                Res.string.feature_account_active,
            ),
        )
        arrayList.add(
            CheckboxStatus(
                Res.string.feature_account_approved,
            ),
        )
        arrayList.add(
            CheckboxStatus(
                Res.string.feature_account_approval_pending,
            ),
        )
        arrayList.add(
            CheckboxStatus(
                Res.string.feature_account_matured,
            ),
        )
        arrayList.add(
            CheckboxStatus(
                Res.string.feature_account_closed,
            ),
        )

        return arrayList
    }

    internal fun getLoanAccountStatusList(): List<CheckboxStatus> {
        val arrayList = ArrayList<CheckboxStatus>()
        arrayList.add(
            CheckboxStatus(
                Res.string.feature_account_in_arrears,
            ),
        )
        arrayList.add(
            CheckboxStatus(
                Res.string.feature_account_active,
            ),
        )
        arrayList.add(
            CheckboxStatus(
                Res.string.feature_account_disburse,
            ),
        )
        arrayList.add(
            CheckboxStatus(
                Res.string.feature_account_approval_pending,
            ),
        )
        arrayList.add(
            CheckboxStatus(
                Res.string.feature_account_overpaid,
            ),
        )
        arrayList.add(
            CheckboxStatus(
                Res.string.feature_account_closed,
            ),
        )
        arrayList.add(
            CheckboxStatus(
                Res.string.feature_account_withdrawn,
            ),
        )
        return arrayList
    }

    internal fun getShareAccountStatusList(): List<CheckboxStatus> {
        val arrayList = ArrayList<CheckboxStatus>()
        arrayList.add(
            CheckboxStatus(
                Res.string.feature_account_active,
            ),
        )
        arrayList.add(
            CheckboxStatus(
                Res.string.feature_account_approved,
            ),
        )
        arrayList.add(
            CheckboxStatus(
                Res.string.feature_account_approval_pending,
            ),
        )
        arrayList.add(
            CheckboxStatus(
                Res.string.feature_account_closed,
            ),
        )
        return arrayList
    }
}

internal data class CheckboxStatus(
    val status: StringResource?,
    val isChecked: Boolean = false,
)
