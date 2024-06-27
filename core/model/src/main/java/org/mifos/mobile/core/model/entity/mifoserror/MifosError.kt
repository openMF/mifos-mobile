package org.mifos.mobile.core.model.entity.mifoserror

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MifosError(
    var developerMessage: String? = null,
    var httpStatusCode: String? = null,
    var defaultUserMessage: String? = null,
    var userMessageGlobalisationCode: String? = null,
    var errors: List<Errors> = ArrayList(),
) : Parcelable
