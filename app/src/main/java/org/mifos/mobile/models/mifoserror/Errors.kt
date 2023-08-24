package org.mifos.mobile.models.mifoserror

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Errors(
    var developerMessage: String? = null,
    var defaultUserMessage: String? = null,
    var userMessageGlobalisationCode: String? = null,
    var parameterName: String? = null,
) : Parcelable
