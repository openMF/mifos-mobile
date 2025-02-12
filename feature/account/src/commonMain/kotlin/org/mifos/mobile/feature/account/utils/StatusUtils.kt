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

import androidx.compose.ui.graphics.Color
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
import org.mifos.mobile.feature.account.Black
import org.mifos.mobile.feature.account.Blue
import org.mifos.mobile.feature.account.DepositGreen
import org.mifos.mobile.feature.account.LightBlack
import org.mifos.mobile.feature.account.LightBlue
import org.mifos.mobile.feature.account.LightGreen
import org.mifos.mobile.feature.account.LightYellow
import org.mifos.mobile.feature.account.Purple
import org.mifos.mobile.feature.account.Red
import org.mifos.mobile.feature.account.RedLight

object StatusUtils {

    internal fun getSavingsAccountStatusList(): List<CheckboxStatus> {
        val arrayList = ArrayList<CheckboxStatus>()

        arrayList.add(
            CheckboxStatus(
                Res.string.feature_account_active,
                DepositGreen,
            ),
        )
        arrayList.add(
            CheckboxStatus(
                Res.string.feature_account_approved,
                LightGreen,
            ),
        )
        arrayList.add(
            CheckboxStatus(
                Res.string.feature_account_approval_pending,
                LightYellow,
            ),
        )
        arrayList.add(
            CheckboxStatus(
                Res.string.feature_account_matured,
                RedLight,
            ),
        )
        arrayList.add(
            CheckboxStatus(
                Res.string.feature_account_closed,
                Black,
            ),
        )

        return arrayList
    }

    internal fun getLoanAccountStatusList(): List<CheckboxStatus> {
        val arrayList = ArrayList<CheckboxStatus>()
        arrayList.add(
            CheckboxStatus(
                Res.string.feature_account_in_arrears,
                Red,
            ),
        )
        arrayList.add(
            CheckboxStatus(
                Res.string.feature_account_active,
                DepositGreen,
            ),
        )
        arrayList.add(
            CheckboxStatus(
                Res.string.feature_account_disburse,
                Blue,
            ),
        )
        arrayList.add(
            CheckboxStatus(
                Res.string.feature_account_approval_pending,
                LightYellow,
            ),
        )
        arrayList.add(
            CheckboxStatus(
                Res.string.feature_account_overpaid,
                Purple,
            ),
        )
        arrayList.add(
            CheckboxStatus(
                Res.string.feature_account_closed,
                Black,
            ),
        )
        arrayList.add(
            CheckboxStatus(
                Res.string.feature_account_withdrawn,
                LightBlack,
            ),
        )
        return arrayList
    }

    internal fun getShareAccountStatusList(): List<CheckboxStatus> {
        val arrayList = ArrayList<CheckboxStatus>()
        arrayList.add(
            CheckboxStatus(
                Res.string.feature_account_active,
                DepositGreen,
            ),
        )
        arrayList.add(
            CheckboxStatus(
                Res.string.feature_account_approved,
                LightGreen,
            ),
        )
        arrayList.add(
            CheckboxStatus(
                Res.string.feature_account_approval_pending,
                LightYellow,
            ),
        )
        arrayList.add(
            CheckboxStatus(
                Res.string.feature_account_closed,
                LightBlue,
            ),
        )
        return arrayList
    }
}

internal data class CheckboxStatus(
    val status: StringResource?,
    val color: Color,
    val isChecked: Boolean = false,
)
