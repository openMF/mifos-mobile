package org.mifos.mobile.utils

import android.content.Context
import androidx.core.content.ContextCompat
import org.mifos.mobile.R
import org.mifos.mobile.core.model.entity.CheckboxStatus

/**
 * Created by dilpreet on 3/7/17.
 */
object StatusUtils {

    fun getSavingsAccountTransactionList(context: Context?): List<CheckboxStatus> {
        val arrayList = ArrayList<CheckboxStatus>()
        arrayList.add(
            CheckboxStatus(
                context?.getString(R.string.feature_account_deposit),
                ContextCompat
                    .getColor(context!!, R.color.deposit_green),
            ),
        )
        arrayList.add(
            CheckboxStatus(
                context.getString(R.string.feature_account_dividend_payout),
                ContextCompat
                    .getColor(context, R.color.red_light),
            ),
        )
        arrayList.add(
            CheckboxStatus(
                context.getString(R.string.feature_account_withdrawal),
                ContextCompat
                    .getColor(context, R.color.red_light),
            ),
        )
        arrayList.add(
            CheckboxStatus(
                context.getString(R.string.feature_account_interest_posting),
                ContextCompat.getColor(context, R.color.green_light),
            ),
        )
        arrayList.add(
            CheckboxStatus(
                context.getString(R.string.feature_account_fee_deduction),
                ContextCompat
                    .getColor(context, R.color.red_light),
            ),
        )
        arrayList.add(
            CheckboxStatus(
                context.getString(R.string.feature_account_withdrawal_transfer),
                ContextCompat.getColor(context, R.color.red_light),
            ),
        )
        arrayList.add(
            CheckboxStatus(
                context.getString(R.string.feature_account_rejected_transfer),
                ContextCompat.getColor(context, R.color.green_light),
            ),
        )
        arrayList.add(
            CheckboxStatus(
                context.getString(R.string.feature_account_overdraft_fee),
                ContextCompat
                    .getColor(context, R.color.red_light),
            ),
        )
        return arrayList
    }
}
