package org.mifos.mobile.models.templates.loans

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Rajan Maurya on 16/07/16.
 */

@Parcelize
data class RepaymentFrequencyTypeOptions(
        var id: Int? = null,

        var code: String? = null,

        var value: String? = null
) : Parcelable