package org.mifos.mobile.models.mifoserror


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Arg(
        var value: String? = null
) : Parcelable
