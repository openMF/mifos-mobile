/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.testing.fixture

import org.mifos.mobile.core.model.entity.accounts.loan.LoanAccount
import org.mifos.mobile.core.model.entity.accounts.savings.SavingAccount
import org.mifos.mobile.core.model.entity.accounts.share.ShareAccount
import org.mifos.mobile.core.model.entity.client.ClientAccounts

/**
 * Test fixtures for [ClientAccounts] entity.
 *
 * Usage:
 * ```kotlin
 * val emptyAccounts = ClientAccountsFixture.createEmpty()
 * val accountsWithData = ClientAccountsFixture.createWithSampleData()
 * val customAccounts = ClientAccountsFixture.create(
 *     loanAccounts = listOf(loanAccount),
 *     savingsAccounts = listOf(savingsAccount),
 * )
 * ```
 */
object ClientAccountsFixture {

    fun createEmpty(): ClientAccounts = ClientAccounts(
        loanAccounts = emptyList(),
        savingsAccounts = emptyList(),
        shareAccounts = emptyList(),
    )

    fun createWithSampleData(): ClientAccounts = ClientAccounts(
        loanAccounts = listOf(
            createSampleLoanAccount(id = 1L, accountNo = "LOAN001"),
            createSampleLoanAccount(id = 2L, accountNo = "LOAN002"),
        ),
        savingsAccounts = listOf(
            createSampleSavingsAccount(id = 1L, accountNo = "SAV001"),
            createSampleSavingsAccount(id = 2L, accountNo = "SAV002"),
        ),
        shareAccounts = listOf(
            createSampleShareAccount(id = 1L, accountNo = "SHARE001"),
        ),
    )

    fun createWithLoansOnly(): ClientAccounts = ClientAccounts(
        loanAccounts = listOf(
            createSampleLoanAccount(id = 1L, accountNo = "LOAN001"),
        ),
        savingsAccounts = emptyList(),
        shareAccounts = emptyList(),
    )

    fun createWithSavingsOnly(): ClientAccounts = ClientAccounts(
        loanAccounts = emptyList(),
        savingsAccounts = listOf(
            createSampleSavingsAccount(id = 1L, accountNo = "SAV001"),
        ),
        shareAccounts = emptyList(),
    )

    fun create(
        loanAccounts: List<LoanAccount> = emptyList(),
        savingsAccounts: List<SavingAccount> = emptyList(),
        shareAccounts: List<ShareAccount> = emptyList(),
    ): ClientAccounts = ClientAccounts(
        loanAccounts = loanAccounts,
        savingsAccounts = savingsAccounts,
        shareAccounts = shareAccounts,
    )

    private fun createSampleLoanAccount(
        id: Long,
        accountNo: String,
    ): LoanAccount = LoanAccount(
        id = id,
        accountNo = accountNo,
        productName = "Test Loan Product",
        loanProductName = "Test Loan",
        currency = null,
        timeline = null,
    )

    private fun createSampleSavingsAccount(
        id: Long,
        accountNo: String,
    ): SavingAccount = SavingAccount(
        id = id,
        accountNo = accountNo,
        productName = "Test Savings Product",
        accountBalance = 1000.0,
    )

    private fun createSampleShareAccount(
        id: Long,
        accountNo: String,
    ): ShareAccount = ShareAccount(
        id = id,
        accountNo = accountNo,
        productName = "Test Share Product",
    )
}
