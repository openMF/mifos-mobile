package org.mifos.mobile.core.data.mapper.payloads

import org.mifos.mobile.core.model.entity.beneficiary.BeneficiaryUpdatePayload
import org.mifos.mobile.core.network.dto.payloads.BeneficiaryUpdatePayloadDto


fun BeneficiaryUpdatePayload.toDto(): BeneficiaryUpdatePayloadDto =
    BeneficiaryUpdatePayloadDto (
        name = name,
        transferLimit = transferLimit
    )