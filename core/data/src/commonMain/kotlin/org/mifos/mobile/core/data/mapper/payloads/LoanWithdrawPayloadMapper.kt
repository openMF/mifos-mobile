package org.mifos.mobile.core.data.mapper.payloads

import org.mifos.mobile.core.model.entity.accounts.loan.LoanWithdraw
import org.mifos.mobile.core.network.dto.payloads.LoanWithdrawPayloadDto


fun LoanWithdraw.toDto() : LoanWithdrawPayloadDto =
    LoanWithdrawPayloadDto(
        withdrawnOnDate = withdrawnOnDate,
        note = note,
        dateFormat = dateFormat,
        locale = locale,
    )