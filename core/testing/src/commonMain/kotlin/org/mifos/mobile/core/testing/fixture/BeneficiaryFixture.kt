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

import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.model.entity.beneficiary.BeneficiaryPayload
import org.mifos.mobile.core.model.entity.templates.account.AccountType

/**
 * Test fixtures for [Beneficiary] entity.
 *
 * Usage:
 * ```kotlin
 * val testBeneficiary = BeneficiaryFixture.createDefault()
 * val beneficiaryList = BeneficiaryFixture.createList(count = 5)
 * val customBeneficiary = BeneficiaryFixture.create(name = "Custom", accountNumber = "123")
 * ```
 */
object BeneficiaryFixture {

    fun createDefault(): Beneficiary = Beneficiary(
        id = 1L,
        name = "Test Beneficiary",
        officeName = "Test Office",
        clientName = "Test Client",
        accountNumber = "ACC001",
        accountType = AccountTypeFixture.createSavings(),
        transferLimit = 10000.0,
    )

    fun createList(count: Int = 3): List<Beneficiary> = (1..count).map { index ->
        create(
            id = index.toLong(),
            name = "Beneficiary $index",
            accountNumber = "ACC${index.toString().padStart(3, '0')}",
        )
    }

    fun create(
        id: Long = 1L,
        name: String = "Test Beneficiary",
        officeName: String = "Test Office",
        clientName: String = "Test Client",
        accountNumber: String = "ACC001",
        transferLimit: Double = 10000.0,
    ): Beneficiary = Beneficiary(
        id = id,
        name = name,
        officeName = officeName,
        clientName = clientName,
        accountNumber = accountNumber,
        accountType = AccountTypeFixture.createSavings(),
        transferLimit = transferLimit,
    )

    fun createPayload(
        name: String = "Test Beneficiary",
        accountNumber: String = "ACC001",
        transferLimit: Int = 10000,
    ): BeneficiaryPayload = BeneficiaryPayload(
        name = name,
        accountNumber = accountNumber,
        transferLimit = transferLimit,
    )
}

/**
 * Helper for creating account type fixtures.
 */
object AccountTypeFixture {
    fun createSavings() = AccountType(
        id = 2,
        code = "accountType.savings",
        value = "Savings",
    )

    fun createLoan() = AccountType(
        id = 1,
        code = "accountType.loan",
        value = "Loan",
    )
}
