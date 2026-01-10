package org.mifos.mobile.core.data.mapper.payloads

import org.mifos.mobile.core.model.entity.register.UserVerify
import org.mifos.mobile.core.network.dto.payloads.UserVerifyPayloadDto

fun UserVerify.toDto() : UserVerifyPayloadDto =
    UserVerifyPayloadDto(
        requestId = requestId,
        authenticationToken = authenticationToken,
    )