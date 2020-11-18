package org.mifos.mobile.models.accounts.loan.calendardata

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Rajan Maurya on 04/03/17.
 */

@Parcelize
data class RepeatsOnNthDayOfMonth(
        var id: Int? = null,

        var code: String? = null,

        var value: String? = null
) : Parcelable