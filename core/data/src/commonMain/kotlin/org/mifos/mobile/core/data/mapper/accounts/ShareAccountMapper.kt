/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.data.mapper.accounts

import org.mifos.mobile.core.model.entity.accounts.share.Currency
import org.mifos.mobile.core.model.entity.accounts.share.ShareAccount
import org.mifos.mobile.core.model.entity.accounts.share.Status
import org.mifos.mobile.core.model.entity.accounts.share.Timeline
import org.mifos.mobile.core.network.dto.currency.CurrencyResponseDto
import org.mifos.mobile.core.network.dto.shareAccount.ShareAccountResponseDto
import org.mifos.mobile.core.network.dto.shareAccount.ShareStatusResponseDto
import org.mifos.mobile.core.network.dto.shareAccount.ShareTimelineResponseDto

fun ShareAccountResponseDto.toModel(): ShareAccount =
    ShareAccount(
        id = id,
        accountNo = accountNo,
        totalApprovedShares = totalApprovedShares,
        totalPendingForApprovalShares = totalPendingForApprovalShares,
        productId = productId,
        productName = productName,
        shortProductName = shortProductName,
        status = status?.toModel(),
        currency = currency?.toShareCurrencyModel(),
        timeline = timeline?.toModel(),
    )

fun ShareStatusResponseDto.toModel(): Status =
    Status(
        id = id,
        code = code,
        value = value,
        submittedAndPendingApproval = submittedAndPendingApproval,
        approved = approved,
        rejected = rejected,
        active = active,
        closed = closed,
    )

fun ShareTimelineResponseDto.toModel(): Timeline =
    Timeline(
        submittedOnDate = submittedOnDate,
        submittedByUsername = submittedByUsername,
        submittedByFirstname = submittedByFirstname,
        submittedByLastname = submittedByLastname,
        approvedDate = approvedDate,
        approvedByUsername = approvedByUsername,
        approvedByFirstname = approvedByFirstname,
        approvedByLastname = approvedByLastname,
        activatedDate = activatedDate,
        activatedByUsername = activatedByUsername,
        activatedByFirstname = activatedByFirstname,
        activatedByLastname = activatedByLastname,
    )

fun CurrencyResponseDto.toShareCurrencyModel(): Currency =
    Currency(
        code = code,
        name = name,
        decimalPlaces = decimalPlaces,
        inMultiplesOf = inMultiplesOf,
        displaySymbol = displaySymbol,
        nameCode = nameCode,
        displayLabel = displayLabel,
    )
