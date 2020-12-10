package org.mifos.mobile.models.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Identifier(
        var idType: IdentifierType? = null,
        var idValue: String? = null) : Parcelable