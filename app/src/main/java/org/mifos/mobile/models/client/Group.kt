package org.mifos.mobile.models.client

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by dilpreet on 10/7/17.
 */
@Parcelize
data class Group(
        var id: Int,
        var accountNo: String? = null,
        var name: String? = null
): Parcelable
