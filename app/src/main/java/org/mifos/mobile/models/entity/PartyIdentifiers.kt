package org.mifos.mobile.models.entity

import com.google.gson.annotations.SerializedName

data class PartyIdentifiers(
        @SerializedName("identifiers")
        val identifierList: List<Identifier>? = null
)