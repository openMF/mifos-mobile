package org.mifos.mobile.core.data.mapper.payloads

import org.mifos.mobile.core.model.entity.accounts.savings.SavingsAccountWithdrawPayload
import org.mifos.mobile.core.network.dto.payloads.SavingsAccountWithdrawPayloadDto

fun SavingsAccountWithdrawPayload.toDto() : SavingsAccountWithdrawPayloadDto =
    SavingsAccountWithdrawPayloadDto(
        locale = locale,
        dateFormat = dateFormat,
        withdrawnOnDate = withdrawnOnDate,
        note = note
    )