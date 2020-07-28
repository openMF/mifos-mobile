package org.mifos.mobile.models

import com.google.gson.annotations.SerializedName
import org.mifos.mobile.models.client.IdentifierType

data class RegistrationEntity(
        @SerializedName("accountId")
        val accountNumber: String?= null,
        @SerializedName("idType")
        val idType: IdentifierType?= null,
        @SerializedName("idValue")
        val idValue: String?= null)