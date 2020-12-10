package org.mifos.mobile.models.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PartyIdInfo(
        var partyIdType: IdentifierType? = null,
        var partyIdentifier: String? = null) : Parcelable