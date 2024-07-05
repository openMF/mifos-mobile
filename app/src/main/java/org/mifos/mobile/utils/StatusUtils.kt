package org.mifos.mobile.utils

import android.content.Context
import androidx.core.content.ContextCompat
import org.mifos.mobile.R
import org.mifos.mobile.core.model.entity.CheckboxStatus
import org.mifos.mobile.ui.getThemeAttributeColor

/**
 * Created by dilpreet on 3/7/17.
 */
object StatusUtils {

    fun getSavingsAccountTransactionList(context: Context?): List<CheckboxStatus> {
        val arrayList = ArrayList<CheckboxStatus>()
        arrayList.add(
            CheckboxStatus(
                context?.getString(R.string.deposit),
                ContextCompat
                    .getColor(context!!, R.color.deposit_green),
            ),
        )
        arrayList.add(
            CheckboxStatus(
                context.getString(R.string.dividend_payout),
                ContextCompat
                    .getColor(context, R.color.red_light),
            ),
        )
        arrayList.add(
            CheckboxStatus(
                context.getString(R.string.withdrawal),
                ContextCompat
                    .getColor(context, R.color.red_light),
            ),
        )
        arrayList.add(
            CheckboxStatus(
                context.getString(R.string.interest_posting),
                ContextCompat.getColor(context, R.color.green_light),
            ),
        )
        arrayList.add(
            CheckboxStatus(
                context.getString(R.string.fee_deduction),
                ContextCompat
                    .getColor(context, R.color.red_light),
            ),
        )
        arrayList.add(
            CheckboxStatus(
                context.getString(R.string.withdrawal_transfer),
                ContextCompat.getColor(context, R.color.red_light),
            ),
        )
        arrayList.add(
            CheckboxStatus(
                context.getString(R.string.rejected_transfer),
                ContextCompat.getColor(context, R.color.green_light),
            ),
        )
        arrayList.add(
            CheckboxStatus(
                context.getString(R.string.overdraft_fee),
                ContextCompat
                    .getColor(context, R.color.red_light),
            ),
        )
        return arrayList
    }
}
