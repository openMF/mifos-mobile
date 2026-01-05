/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.testing.fake

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.AccountsRepository
import org.mifos.mobile.core.model.entity.client.ClientAccounts

/**
 * Fake implementation of [AccountsRepository] for testing.
 *
 * Usage:
 * ```kotlin
 * val fakeRepo = FakeAccountsRepository()
 *
 * // Set accounts data
 * fakeRepo.setAccounts(DataState.Success(testAccounts))
 *
 * // Use in tests
 * val viewModel = AccountsViewModel(fakeRepo)
 * ```
 */
class FakeAccountsRepository : AccountsRepository {

    private val accountsState = MutableStateFlow<DataState<ClientAccounts>>(
        DataState.Success(ClientAccounts()),
    )

    // Track calls for verification
    var loadAccountsCallCount = 0
        private set
    var lastClientId: Long? = null
        private set
    var lastAccountType: String? = null
        private set

    fun setAccounts(result: DataState<ClientAccounts>) {
        accountsState.value = result
    }

    fun emitLoading() {
        accountsState.value = DataState.Loading
    }

    fun emitSuccess(accounts: ClientAccounts) {
        accountsState.value = DataState.Success(accounts)
    }

    fun emitError(error: Throwable) {
        accountsState.value = DataState.Error(error)
    }

    fun reset() {
        accountsState.value = DataState.Success(ClientAccounts())
        loadAccountsCallCount = 0
        lastClientId = null
        lastAccountType = null
    }

    override fun loadAccounts(clientId: Long?, accountType: String?): Flow<DataState<ClientAccounts>> {
        loadAccountsCallCount++
        lastClientId = clientId
        lastAccountType = accountType
        return accountsState.asStateFlow()
    }
}
