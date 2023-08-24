package org.mifos.mobile.models.templates.loans

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RepaymentFrequencyDaysOfWeekTypeOptions(

    var id: Int? = null,

    var code: String? = null,

    var value: String? = null,
) : Parcelable
