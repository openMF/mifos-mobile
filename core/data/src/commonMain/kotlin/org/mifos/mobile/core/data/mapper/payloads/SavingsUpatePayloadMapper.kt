package org.mifos.mobile.core.data.mapper.payloads

import org.mifos.mobile.core.model.entity.accounts.savings.SavingsAccountUpdatePayload
import org.mifos.mobile.core.network.dto.payloads.SavingsAccountUpdatePayloadDto

fun SavingsAccountUpdatePayload.toDto() : SavingsAccountUpdatePayloadDto =
    SavingsAccountUpdatePayloadDto(
        clientId = clientId,
        productId = productId
    )