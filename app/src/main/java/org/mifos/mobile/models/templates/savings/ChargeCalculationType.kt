package org.mifos.mobile.models.templates.savings

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChargeCalculationType(
    var id: Float = 0.toFloat(),
    var code: String? = null,
    var value: String? = null,
) : Parcelable
