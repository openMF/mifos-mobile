package org.mifos.mobile.core.model.entity.templates.savings

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChargeTimeType(
    var id: Float = 0.toFloat(),
    var code: String? = null,
    var value: String? = null,
) : Parcelable
