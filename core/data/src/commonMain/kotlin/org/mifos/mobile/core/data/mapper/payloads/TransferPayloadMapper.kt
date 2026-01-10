package org.mifos.mobile.core.data.mapper.payloads

import org.mifos.mobile.core.model.entity.payload.TransferPayload
import org.mifos.mobile.core.network.dto.payloads.TransferPayloadDto

fun TransferPayload.toDto(): TransferPayloadDto =
    TransferPayloadDto(
        fromOfficeId = fromOfficeId,
        fromClientId = fromClientId,
        fromAccountType = fromAccountType,
        fromAccountId = fromAccountId,
        toOfficeId = toOfficeId,
        toClientId = toClientId,
        toAccountType = toAccountType,
        toAccountId = toAccountId,
        transferDate = transferDate,
        transferAmount = transferAmount,
        transferDescription = transferDescription,
        dateFormat = dateFormat,
        locale = locale
    )