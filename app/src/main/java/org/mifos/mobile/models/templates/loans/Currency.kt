package org.mifos.mobile.models.templates.loans

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Rajan Maurya on 16/07/16.
 */

@Parcelize
data class Currency(

        var code: String? = null,

        var name: String? = null,

        var decimalPlaces: Double? = null,

        var inMultiplesOf: Int? = null,

        var displaySymbol: String? = null,

        var nameCode: String? = null,

        var displayLabel: String? = null
) : Parcelable