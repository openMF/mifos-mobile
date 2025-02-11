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

import android.content.Context
import androidx.core.content.ContextCompat
import org.mifos.mobile.core.model.entity.CheckboxStatus
import org.mifos.mobile.feature.account.R

object StatusUtils {

    fun getSavingsAccountStatusList(context: Context?): List<CheckboxStatus> {
        val arrayList = ArrayList<CheckboxStatus>()

        arrayList.add(
            CheckboxStatus(
                context?.getString(R.string.feature_account_active),
                ContextCompat.getColor(context!!, R.color.deposit_green),
            ),
        )
        arrayList.add(
            CheckboxStatus(
                context.getString(R.string.feature_account_approved),
                ContextCompat.getColor(context, R.color.light_green),
            ),
        )
        arrayList.add(
            CheckboxStatus(
                context.getString(R.string.feature_account_approval_pending),
                ContextCompat
                    .getColor(context, R.color.light_yellow),
            ),
        )
        arrayList.add(
            CheckboxStatus(
                context.getString(R.string.feature_account_matured),
                ContextCompat.getColor(context, R.color.red_light),
            ),
        )
        arrayList.add(
            CheckboxStatus(
                context.getString(R.string.feature_account_closed),
                ContextCompat.getColor(context, R.color.black),
            ),
        )

        return arrayList
    }

    fun getLoanAccountStatusList(context: Context?): List<CheckboxStatus> {
        val arrayList = ArrayList<CheckboxStatus>()
        arrayList.add(
            CheckboxStatus(
                context?.getString(R.string.feature_account_in_arrears),
                ContextCompat.getColor(context!!, R.color.red),
            ),
        )
        arrayList.add(
            CheckboxStatus(
                context.getString(R.string.feature_account_active),
                ContextCompat.getColor(context, R.color.deposit_green),
            ),
        )
        arrayList.add(
            CheckboxStatus(
                context.getString(R.string.feature_account_disburse),
                ContextCompat.getColor(context, R.color.blue),
            ),
        )
        arrayList.add(
            CheckboxStatus(
                context.getString(R.string.feature_account_approval_pending),
                ContextCompat
                    .getColor(context, R.color.light_yellow),
            ),
        )
        arrayList.add(
            CheckboxStatus(
                context.getString(R.string.feature_account_overpaid),
                ContextCompat.getColor(context, R.color.purple),
            ),
        )
        arrayList.add(
            CheckboxStatus(
                context.getString(R.string.feature_account_closed),
                ContextCompat.getColor(context, R.color.black),
            ),
        )
        arrayList.add(
            CheckboxStatus(
                context.getString(R.string.feature_account_withdrawn),
                ContextCompat.getColor(context, R.color.light_black),
            ),
        )
        return arrayList
    }

    fun getShareAccountStatusList(context: Context?): List<CheckboxStatus> {
        val arrayList = ArrayList<CheckboxStatus>()
        arrayList.add(
            CheckboxStatus(
                context?.getString(R.string.feature_account_active),
                ContextCompat.getColor(context!!, R.color.deposit_green),
            ),
        )
        arrayList.add(
            CheckboxStatus(
                context.getString(R.string.feature_account_approved),
                ContextCompat.getColor(context, R.color.light_green),
            ),
        )
        arrayList.add(
            CheckboxStatus(
                context.getString(R.string.feature_account_approval_pending),
                ContextCompat
                    .getColor(context, R.color.light_yellow),
            ),
        )
        arrayList.add(
            CheckboxStatus(
                context.getString(R.string.feature_account_closed),
                ContextCompat.getColor(context, R.color.light_blue),
            ),
        )
        return arrayList
    }
}
