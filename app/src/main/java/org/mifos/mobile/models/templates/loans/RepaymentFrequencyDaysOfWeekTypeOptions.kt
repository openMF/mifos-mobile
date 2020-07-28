package org.mifos.mobile.models.templates.loans

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RepaymentFrequencyDaysOfWeekTypeOptions(

        var id: Int? = null,

        var code: String,

        var value: String
) : Parcelable