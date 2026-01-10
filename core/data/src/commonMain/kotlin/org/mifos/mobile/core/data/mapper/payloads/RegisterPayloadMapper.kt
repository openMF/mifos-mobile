package org.mifos.mobile.core.data.mapper.payloads

import org.mifos.mobile.core.model.entity.register.RegisterPayload
import org.mifos.mobile.core.network.dto.payloads.RegisterPayloadDto

fun RegisterPayload.toDto() : RegisterPayloadDto =
    RegisterPayloadDto(
        firstName = firstName,
        lastName = lastName,
        username = username,
        email = email,
        password = password,
    )
