package org.mifos.mobile.models.client

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Rajan Maurya on 22/10/16.
 */

@Parcelize
data class Status(
        var id: Int? = null,
        var code: String? = null,

        var value: String? = null
) : Parcelable