package org.mifos.mobile.core.network.dto.payloads

import kotlinx.serialization.Serializable

@Serializable
data class RegisterPayloadDto(

    val username: String? = null,

    val firstName: String? = null,

    val middleName: String? = null,

    val lastName: String? = null,

    val email: String? = null,

    val mobileNumber: String? = null,

    val accountNumber: String? = null,

    val password: String? = null,

    val authenticationMode: String? = null,
)
