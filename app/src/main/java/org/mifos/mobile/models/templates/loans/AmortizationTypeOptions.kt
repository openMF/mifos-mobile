package org.mifos.mobile.models.templates.loans

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Rajan Maurya on 16/07/16.
 */

@Parcelize
data class AmortizationTypeOptions(

        var id: Int? = null,

        var code: String,

        var value: String
) : Parcelable