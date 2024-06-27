package org.mifos.mobile.core.model.entity.mifoserror

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Arg(
    var value: String? = null,
) : Parcelable
