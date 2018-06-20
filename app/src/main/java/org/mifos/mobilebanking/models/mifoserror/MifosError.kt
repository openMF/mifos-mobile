package org.mifos.mobilebanking.models.mifoserror


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

import java.util.ArrayList

@Parcelize
data class MifosError(
        var developerMessage: String? = null,
        var httpStatusCode: String? = null,
        var defaultUserMessage: String? = null,
        var userMessageGlobalisationCode: String? = null,
        var errors: List<Errors> = ArrayList()
) : Parcelable