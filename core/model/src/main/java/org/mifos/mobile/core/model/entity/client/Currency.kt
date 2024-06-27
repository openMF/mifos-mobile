package org.mifos.mobile.core.model.entity.client

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Currency(
    var code: String? = null,

    var name: String? = null,

    var decimalPlaces: Int? = null,

    var displaySymbol: String? = null,

    var nameCode: String? = null,

    var displayLabel: String? = null,
) : Parcelable
