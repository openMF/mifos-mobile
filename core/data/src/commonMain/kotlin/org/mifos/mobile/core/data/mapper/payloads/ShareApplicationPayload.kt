package org.mifos.mobile.core.data.mapper.payloads

import org.mifos.mobile.core.model.entity.payload.ShareApplicationPayload
import org.mifos.mobile.core.network.dto.payloads.ShareApplicationPayloadDto

fun ShareApplicationPayload.toDto(): ShareApplicationPayloadDto =
    ShareApplicationPayloadDto(
        productId = productId,
        unitPrice = unitPrice,
        requestedShares = requestedShares,
        submittedDate = submittedDate,
        savingsAccountId = savingsAccountId,
        applicationDate = applicationDate,
        locale = locale,
        dateFormat = dateFormat,
        clientId = clientId
    )
