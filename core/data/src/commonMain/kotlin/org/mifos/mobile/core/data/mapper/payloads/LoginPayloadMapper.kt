package org.mifos.mobile.core.data.mapper.payloads

import org.mifos.mobile.core.model.entity.payload.LoginPayload
import org.mifos.mobile.core.network.dto.payloads.LoginPayloadDto

fun LoginPayload.toDto(): LoginPayloadDto =
    LoginPayloadDto(
        username = username,
        password = password,
    )