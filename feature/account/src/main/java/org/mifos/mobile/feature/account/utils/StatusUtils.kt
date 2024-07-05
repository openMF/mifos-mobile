package org.mifos.mobile.feature.account.utils

import android.content.Context
import androidx.core.content.ContextCompat
import org.mifos.mobile.feature.account.R
import org.mifos.mobile.core.model.entity.CheckboxStatus
import org.mifos.mobile.ui.getThemeAttributeColor

/**
 * Created by dilpreet on 3/7/17.
 */
object StatusUtils {

    fun getSavingsAccountStatusList(context: Context?): List<CheckboxStatus> {
        val arrayList = ArrayList<CheckboxStatus>()
        arrayList.add(
            CheckboxStatus(
                context?.getString(R.string.active),
                ContextCompat.getColor(context!!, R.color.deposit_green),
            ),
        )
        arrayList.add(
            CheckboxStatus(
                context.getString(R.string.approved),
                ContextCompat.getColor(context, R.color.light_green),
            ),
        )
        arrayList.add(
            CheckboxStatus(
                context.getString(R.string.approval_pending),
                ContextCompat
                    .getColor(context, R.color.light_yellow),
            ),
        )
        arrayList.add(
            CheckboxStatus(
                context.getString(R.string.matured),
                ContextCompat.getColor(context, R.color.red_light),
            ),
        )
        arrayList.add(
            CheckboxStatus(
                context.getString(R.string.closed),
                context.getThemeAttributeColor(R.attr.colorOnSurface),
            ),
        )or
        return arrayList
    }

    fun getLoanAccountStatusList(context: Context?): List<CheckboxStatus> {
        val arrayList = ArrayList<CheckboxStatus>()
        arrayList.add(
            CheckboxStatus(
                context?.getString(R.string.in_arrears),
                ContextCompat.getColor(context!!, R.color.red),
            ),
        )
        arrayList.add(
            CheckboxStatus(
                context.getString(R.string.active),
                ContextCompat.getColor(context, R.color.deposit_green),
            ),
        )
        arrayList.add(
            CheckboxStatus(
                context.getString(R.string.waiting_for_disburse),
                ContextCompat.getColor(context, R.color.blue),
            ),
        )
        arrayList.add(
            CheckboxStatus(
                context.getString(R.string.approval_pending),
                ContextCompat
                    .getColor(context, R.color.light_yellow),
            ),
        )
        arrayList.add(
            CheckboxStatus(
                context.getString(R.string.overpaid),
                ContextCompat.getColor(context, R.color.purple),
            ),
        )
        arrayList.add(
            CheckboxStatus(
                context.getString(R.string.closed),
                context.getThemeAttributeColor(R.attr.colorOnSurface),
            ),
        )
        arrayList.add(
            CheckboxStatus(
                context.getString(R.string.withdrawn),
                context.getThemeAttributeColor(R.attr.colorOnSurfaceVariant),
            ),
        )
        return arrayList
    }

    fun getShareAccountStatusList(context: Context?): List<CheckboxStatus> {
        val arrayList = ArrayList<CheckboxStatus>()
        arrayList.add(
            CheckboxStatus(
                context?.getString(R.string.active),
                ContextCompat.getColor(context!!, R.color.deposit_green),
            ),
        )
        arrayList.add(
            CheckboxStatus(
                context.getString(R.string.approved),
                ContextCompat.getColor(context, R.color.light_green),
            ),
        )
        arrayList.add(
            CheckboxStatus(
                context.getString(R.string.approval_pending),
                ContextCompat
                    .getColor(context, R.color.light_yellow),
            ),
        )
        arrayList.add(
            CheckboxStatus(
                context.getString(R.string.closed),
                ContextCompat.getColor(context, R.color.light_blue),
            ),
        )
        return arrayList
    }
}
