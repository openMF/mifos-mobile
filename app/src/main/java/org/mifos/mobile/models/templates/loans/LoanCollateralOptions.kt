package org.mifos.mobile.models.templates.loans

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Rajan Maurya on 16/07/16.
 */

@Parcelize
data class LoanCollateralOptions(

        var id: Int? = null,

        var name: String? = null,

        var position: Int? = null,

        var description: String? = null,

        var active: Boolean? = null
) : Parcelable