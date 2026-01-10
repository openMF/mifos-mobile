package org.mifos.mobile.core.data.mapper.payloads

import org.mifos.mobile.core.model.entity.notification.NotificationRegisterPayload
import org.mifos.mobile.core.network.dto.payloads.NotificationRegisterPayloadDto

fun NotificationRegisterPayload.toDto() : NotificationRegisterPayloadDto =
    NotificationRegisterPayloadDto(
        clientId = clientId,
        registrationId = registrationId,
    )