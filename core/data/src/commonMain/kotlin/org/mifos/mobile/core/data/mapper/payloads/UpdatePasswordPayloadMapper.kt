package org.mifos.mobile.core.data.mapper.payloads

import org.mifos.mobile.core.model.entity.UpdatePasswordPayload
import org.mifos.mobile.core.network.dto.payloads.UpdatePasswordPayloadDto


fun UpdatePasswordPayload.toDto() : UpdatePasswordPayloadDto =
    UpdatePasswordPayloadDto(
        password = password,
        repeatPassword = repeatPassword,
    )