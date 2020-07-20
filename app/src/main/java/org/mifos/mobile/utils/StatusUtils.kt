package org.mifos.mobile.utils

import android.content.Context
import androidx.core.content.ContextCompat

import org.mifos.mobile.R
import org.mifos.mobile.models.CheckboxStatus

import java.util.*

/**
 * Created by dilpreet on 3/7/17.
 */
object StatusUtils {

    fun getSavingsAccountStatusList(context: Context?): List<CheckboxStatus> {
        val arrayList = ArrayList<CheckboxStatus>()
        arrayList.add(CheckboxStatus(context?.getString(R.string.active), ContextCompat.getColor(context!!, R.color.deposit_green)))
        arrayList.add(CheckboxStatus(context?.getString(R.string.approved), ContextCompat.getColor(context!!, R.color.light_green)))
        arrayList.add(CheckboxStatus(context?.getString(R.string.approval_pending), ContextCompat
                .getColor(context!!, R.color.light_yellow)))
        arrayList.add(CheckboxStatus(context.getString(R.string.matured), ContextCompat.getColor(context, R.color.red_light)))
        arrayList.add(CheckboxStatus(context.getString(R.string.closed), ContextCompat.getColor(context, R.color.black)))
        return arrayList
    }

    fun getLoanAccountStatusList(context: Context?): List<CheckboxStatus> {
        val arrayList = ArrayList<CheckboxStatus>()
        arrayList.add(CheckboxStatus(context?.getString(R.string.in_arrears), ContextCompat.getColor(context!!, R.color.red)))
        arrayList.add(CheckboxStatus(context.getString(R.string.active), ContextCompat.getColor(context, R.color.deposit_green)))
        arrayList.add(CheckboxStatus(context.getString(R.string.waiting_for_disburse),
                ContextCompat.getColor(context, R.color.blue)))
        arrayList.add(CheckboxStatus(context.getString(R.string.approval_pending), ContextCompat
                .getColor(context, R.color.light_yellow)))
        arrayList.add(CheckboxStatus(context.getString(R.string.overpaid), ContextCompat.getColor(context, R.color.purple)))
        arrayList.add(CheckboxStatus(context.getString(R.string.closed), ContextCompat.getColor(context, R.color.black)))
        arrayList.add(CheckboxStatus(context.getString(R.string.withdrawn), ContextCompat.getColor(context, R.color.gray_dark)))
        return arrayList
    }

    fun getShareAccountStatusList(context: Context?): List<CheckboxStatus> {
        val arrayList = ArrayList<CheckboxStatus>()
        arrayList.add(CheckboxStatus(context?.getString(R.string.active), ContextCompat.getColor(context!!, R.color.deposit_green)))
        arrayList.add(CheckboxStatus(context.getString(R.string.approved), ContextCompat.getColor(context, R.color.light_green)))
        arrayList.add(CheckboxStatus(context.getString(R.string.approval_pending), ContextCompat
                .getColor(context, R.color.light_yellow)))
        arrayList.add(CheckboxStatus(context.getString(R.string.closed), ContextCompat.getColor(context, R.color.light_blue)))
        return arrayList
    }

    fun getSavingsAccountTransactionList(context: Context?): List<CheckboxStatus> {
        val arrayList = ArrayList<CheckboxStatus>()
        arrayList.add(CheckboxStatus(context?.getString(R.string.deposit), ContextCompat
                .getColor(context!!, R.color.deposit_green)))
        arrayList.add(CheckboxStatus(context.getString(R.string.dividend_payout), ContextCompat
                .getColor(context, R.color.red_light)))
        arrayList.add(CheckboxStatus(context.getString(R.string.withdrawal), ContextCompat
                .getColor(context, R.color.red_light)))
        arrayList.add(CheckboxStatus(context.getString(R.string.interest_posting),
                ContextCompat.getColor(context, R.color.green_light)))
        arrayList.add(CheckboxStatus(context.getString(R.string.fee_deduction), ContextCompat
                .getColor(context, R.color.red_light)))
        arrayList.add(CheckboxStatus(context.getString(R.string.withdrawal_transfer),
                ContextCompat.getColor(context, R.color.red_light)))
        arrayList.add(CheckboxStatus(context.getString(R.string.rejected_transfer),
                ContextCompat.getColor(context, R.color.green_light)))
        arrayList.add(CheckboxStatus(context.getString(R.string.overdraft_fee), ContextCompat
                .getColor(context, R.color.red_light)))
        return arrayList
    }
}