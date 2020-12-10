package org.mifos.mobile.models.entity

import com.google.gson.annotations.SerializedName

data class RegistrationEntity(
        @SerializedName("accountId")
        val accountNumber: String? = null,
        val idType: IdentifierType? = null,
        val idValue: String? = null)