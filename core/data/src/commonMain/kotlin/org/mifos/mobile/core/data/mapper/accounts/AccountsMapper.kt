package org.mifos.mobile.core.data.mapper.accounts

import org.mifos.mobile.core.model.entity.client.ClientAccounts
import org.mifos.mobile.core.network.dto.accounts.AccountsResponseDto


fun AccountsResponseDto.toModel() : ClientAccounts =
    ClientAccounts(
        loanAccounts = loanAccounts.map { it.toModel() },
        savingsAccounts = savingsAccounts?.map { it.toModel() },
        shareAccounts = shareAccounts.map { it.toModel() }
    )











