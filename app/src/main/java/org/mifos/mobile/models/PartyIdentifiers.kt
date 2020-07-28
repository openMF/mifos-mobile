package org.mifos.mobile.models

import com.google.gson.annotations.SerializedName
import org.mifos.mobile.models.client.Identifier

data class PartyIdentifiers(
        @SerializedName("identifiers")
        val identifierList: List<Identifier>? = null
)