package org.mifos.mobile.models.mifoserror

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Arg(
    var value: String? = null,
) : Parcelable
