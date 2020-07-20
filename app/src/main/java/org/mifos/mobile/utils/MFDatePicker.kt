/*
 * This project is licensed under the open source MPL V2.
 * See https://github.com/openMF/android-client/blob/master/LICENSE.md
 */
package org.mifos.mobile.utils

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import org.mifos.mobile.R
import java.util.*

/**
 * Created by ishankhanna on 30/06/14.
 */
class MFDatePicker : DialogFragment(), OnDateSetListener {
    private var startDateString: String? = null
    private var startDate: Long = 0
    private var datePickerType = ALL_DAYS

    companion object {
        const val TAG = "MFDatePicker"
        var datePickedAsString: String? = null
        var mCalendar: Calendar? = null

        /* Constants used to select which type of date picker is being called */
        const val PREVIOUS_DAYS = 1 // only past days
        const val FUTURE_DAYS = 2 // only future days
        const val ALL_DAYS = 3 // any day can be picked
        fun newInstance(fragment: Fragment?, datePickerType: Int, active: Boolean): MFDatePicker {
            val mfDatePicker = MFDatePicker()
            val args = Bundle()
            args.putInt(Constants.DATE_PICKER_TYPE, datePickerType)
            mfDatePicker.arguments = args
            mfDatePicker.onDatePickListener = fragment as OnDatePickListener?
            if (!active) {
                mCalendar = Calendar.getInstance()
            }
            return mfDatePicker
        }

        init {
            mCalendar = Calendar.getInstance()
            datePickedAsString = StringBuilder()
                    .append(if (mCalendar?.get(Calendar.DAY_OF_MONTH)!! < 10) "0" + mCalendar?.get(Calendar.DAY_OF_MONTH) else mCalendar?.get(Calendar.DAY_OF_MONTH))
                    .append("-")
                    .append(if (mCalendar?.get(Calendar.MONTH)!! + 1 < 10) "0" + (mCalendar?.get(Calendar.MONTH)!! + 1) else mCalendar?.get(Calendar.MONTH)!! + 1)
                    .append("-")
                    .append(mCalendar?.get(Calendar.YEAR)!!)
                    .toString()
        }
    }

    private var onDatePickListener: OnDatePickListener? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = DatePickerDialog(activity,
                R.style.MaterialDatePickerTheme,
                this,
                mCalendar!![Calendar.YEAR],
                mCalendar!![Calendar.MONTH],
                mCalendar!![Calendar.DAY_OF_MONTH])
        val args = arguments
        datePickerType = args!!.getInt(Constants.DATE_PICKER_TYPE, ALL_DAYS)
        when (datePickerType) {
            FUTURE_DAYS -> dialog.datePicker.minDate = Date().time
            PREVIOUS_DAYS -> dialog.datePicker.maxDate = Date().time
        }
        return dialog
    }

    override fun onDateSet(datePicker: DatePicker, year: Int, month: Int, day: Int) {
        //TODO Fix Single digit problem that fails with the locale
        val calendar = mCalendar
        calendar!![year, month] = day
        val date = calendar.time
        onDatePickListener!!.onDatePicked(DateFormat.format("dd-MM-yyyy", date).toString().also { startDateString = it })
        startDate = DateHelper.getDateAsLongFromString(startDateString, "dd-MM-yyyy")
    }

    fun setOnDatePickListener(onDatePickListener: OnDatePickListener?) {
        this.onDatePickListener = onDatePickListener
    }

    interface OnDatePickListener {
        fun onDatePicked(date: String?)
    }
}