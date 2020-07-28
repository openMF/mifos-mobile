package org.mifos.mobile.models.client

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Rajan Maurya on 23/02/17.
 */

@Parcelize
data class Type(
        var id: Int? = null,
        var code: String,

        var value: String
) : Parcelable