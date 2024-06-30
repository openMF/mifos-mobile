/*
 * This project is licensed under the open source MPL V2.
 * See https://github.com/openMF/android-client/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.common.utils

import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Calendar
import java.util.Locale

enum class DatePickerConstrainType {
    ONLY_FUTURE_DAYS, ONLY_PAST_DAYS, NONE
}

fun getDatePickerDialog(
    selectedInstant: Instant = Instant.now(),
    constrainType: DatePickerConstrainType = DatePickerConstrainType.NONE,
    onDatePick: (dateLong: Long) -> Unit = {},
): MaterialDatePicker<Long> {
    val constrainsBuilder = CalendarConstraints.Builder().apply {
        when (constrainType) {
            DatePickerConstrainType.ONLY_FUTURE_DAYS -> setValidator(DateValidatorPointForward.now())
            DatePickerConstrainType.ONLY_PAST_DAYS -> setValidator(DateValidatorPointBackward.now())
            DatePickerConstrainType.NONE -> {}
        }
    }

    val dialog = MaterialDatePicker.Builder.datePicker()
        .setSelection(selectedInstant.toEpochMilli())
        .setCalendarConstraints(constrainsBuilder.build())
        .build()
    dialog.addOnPositiveButtonClickListener {
        onDatePick(it)
    }
    return dialog
}

fun getTodayFormatted(): String =
    SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Calendar.getInstance().time)
