/*
 * This project is licensed under the open source MPL V2.
 * See https://github.com/openMF/android-client/blob/master/LICENSE.md
 */

package org.mifos.selfserviceapp.utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by ishankhanna on 30/06/14.
 */
public class MFDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    public static final String TAG = "MFDatePicker";
    static String dateSet;
    static Calendar calendar;

    static {

        calendar = Calendar.getInstance();
        dateSet = new StringBuilder()
                .append(calendar.get(Calendar.DAY_OF_MONTH) < 10 ?
                        ("0" + calendar.get(Calendar.DAY_OF_MONTH))
                        : calendar.get(Calendar.DAY_OF_MONTH))
                .append("-")
                .append(calendar.get(Calendar.MONTH) + 1 < 10 ?
                        ("0" + (calendar.get(Calendar.MONTH) + 1))
                        : calendar.get(Calendar.MONTH) + 1)
                .append("-")
                .append(calendar.get(Calendar.YEAR))
                .toString();
    }

    OnDatePickListener onDatePickListener;

    public MFDatePicker() {

    }

    public static MFDatePicker newInsance(Fragment fragment) {
        MFDatePicker mfDatePicker = new MFDatePicker();
        mfDatePicker.onDatePickListener = (OnDatePickListener) fragment;
        return mfDatePicker;
    }

    public static String getDatePickedAsString() {
        return dateSet;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                this, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        dialog.getDatePicker().setMaxDate(new Date().getTime());
        return dialog;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        //TODO Fix Single digit problem that fails with the locale
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        Date date = calendar.getTime();
        onDatePickListener.onDatePicked(DateFormat.format("dd-MM-yyyy", date).toString());

    }

    public void setOnDatePickListener(OnDatePickListener onDatePickListener) {
        this.onDatePickListener = onDatePickListener;
    }

    public interface OnDatePickListener {
        public void onDatePicked(String date);
    }


}
