package org.mifos.mobile.utils;

import android.content.Context;

import org.mifos.mobile.R;
import org.mifos.mobile.models.CheckboxStatus;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;

/**
 * Created by dilpreet on 3/7/17.
 */

public class StatusUtils {
    public static List<CheckboxStatus> getSavingsAccountStatusList(Context context) {
        ArrayList<CheckboxStatus> arrayList = new ArrayList<>();
        arrayList.add(new CheckboxStatus(context.getString(R.string.active), ContextCompat.
                getColor(context, R.color.deposit_green)));
        arrayList.add(new CheckboxStatus(context.getString(R.string.approved), ContextCompat.
                getColor(context, R.color.light_green)));
        arrayList.add(new CheckboxStatus(context.getString(R.string.approval_pending), ContextCompat
                .getColor(context, R.color.light_yellow)));
        arrayList.add(new CheckboxStatus(context.getString(R.string.matured), ContextCompat.
                getColor(context, R.color.red_light)));
        arrayList.add(new CheckboxStatus(context.getString(R.string.closed), ContextCompat.
                getColor(context, R.color.black)));

        return arrayList;
    }

    public static List<CheckboxStatus> getLoanAccountStatusList(Context context) {
        ArrayList<CheckboxStatus> arrayList = new ArrayList<>();
        arrayList.add(new CheckboxStatus(context.getString(R.string.in_arrears), ContextCompat.
                getColor(context, R.color.red)));
        arrayList.add(new CheckboxStatus(context.getString(R.string.active), ContextCompat.
                getColor(context, R.color.deposit_green)));
        arrayList.add(new CheckboxStatus(context.getString(R.string.waiting_for_disburse),
                ContextCompat.getColor(context, R.color.blue)));
        arrayList.add(new CheckboxStatus(context.getString(R.string.approval_pending), ContextCompat
                .getColor(context, R.color.light_yellow)));
        arrayList.add(new CheckboxStatus(context.getString(R.string.overpaid), ContextCompat.
                getColor(context, R.color.purple)));
        arrayList.add(new CheckboxStatus(context.getString(R.string.closed), ContextCompat.
                getColor(context, R.color.black)));
        arrayList.add(new CheckboxStatus(context.getString(R.string.withdrawn), ContextCompat.
                getColor(context, R.color.gray_dark)));

        return arrayList;
    }

    public static List<CheckboxStatus> getShareAccountStatusList(Context context) {
        ArrayList<CheckboxStatus> arrayList = new ArrayList<>();
        arrayList.add(new CheckboxStatus(context.getString(R.string.active), ContextCompat.
                getColor(context, R.color.deposit_green)));
        arrayList.add(new CheckboxStatus(context.getString(R.string.approved), ContextCompat.
                getColor(context, R.color.light_green)));
        arrayList.add(new CheckboxStatus(context.getString(R.string.approval_pending), ContextCompat
                .getColor(context, R.color.light_yellow)));
        arrayList.add(new CheckboxStatus(context.getString(R.string.closed), ContextCompat.
                getColor(context, R.color.light_blue)));

        return arrayList;
    }

    public static List<CheckboxStatus> getSavingsAccountTransactionList(Context context) {
        ArrayList<CheckboxStatus> arrayList = new ArrayList<>();
        arrayList.add(new CheckboxStatus(context.getString(R.string.deposit), ContextCompat
                .getColor(context, R.color.deposit_green)));
        arrayList.add(new CheckboxStatus(context.getString(R.string.dividend_payout), ContextCompat
                .getColor(context, R.color.red_light)));
        arrayList.add(new CheckboxStatus(context.getString(R.string.withdrawal), ContextCompat
                .getColor(context, R.color.red_light)));
        arrayList.add(new CheckboxStatus(context.getString(R.string.interest_posting),
                ContextCompat.getColor(context, R.color.green_light)));
        arrayList.add(new CheckboxStatus(context.getString(R.string.fee_deduction), ContextCompat
                .getColor(context, R.color.red_light)));
        arrayList.add(new CheckboxStatus(context.getString(R.string.withdrawal_transfer),
                ContextCompat.getColor(context, R.color.red_light)));
        arrayList.add(new CheckboxStatus(context.getString(R.string.rejected_transfer),
                ContextCompat.getColor(context, R.color.green_light)));
        arrayList.add(new CheckboxStatus(context.getString(R.string.overdraft_fee), ContextCompat
                .getColor(context, R.color.red_light)));

        return arrayList;

    }
}
