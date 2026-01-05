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
import org.mifos.mobile.core.data.repository.HomeRepository
import org.mifos.mobile.core.model.entity.client.Client
import org.mifos.mobile.core.model.entity.client.ClientAccounts

/**
 * Fake implementation of [HomeRepository] for testing.
 *
 * Usage:
 * ```kotlin
 * val fakeRepo = FakeHomeRepository()
 *
 * // Set success response
 * fakeRepo.setClientAccounts(DataState.Success(testClientAccounts))
 *
 * // Emit loading state first, then success
 * fakeRepo.emitClientAccountsLoading()
 * fakeRepo.emitClientAccountsSuccess(testClientAccounts)
 *
 * // Use in tests
 * val viewModel = HomeViewModel(fakeRepo)
 * ```
 */
class FakeHomeRepository : HomeRepository {

    private val _clientAccountsFlow = MutableStateFlow<DataState<ClientAccounts>>(
        DataState.Success(ClientAccounts()),
    )
    private val _currentClientFlow = MutableStateFlow<DataState<Client>>(
        DataState.Success(createDefaultClient()),
    )
    private val _clientImageFlow = MutableStateFlow<DataState<String>>(
        DataState.Success(""),
    )
    private val _unreadNotificationsFlow = MutableStateFlow<DataState<Int>>(
        DataState.Success(0),
    )

    fun setClientAccounts(result: DataState<ClientAccounts>) {
        _clientAccountsFlow.value = result
    }

    fun emitClientAccountsLoading() {
        _clientAccountsFlow.value = DataState.Loading
    }

    fun emitClientAccountsSuccess(accounts: ClientAccounts) {
        _clientAccountsFlow.value = DataState.Success(accounts)
    }

    fun emitClientAccountsError(error: Throwable) {
        _clientAccountsFlow.value = DataState.Error(error)
    }

    fun setCurrentClient(result: DataState<Client>) {
        _currentClientFlow.value = result
    }

    fun setClientImage(result: DataState<String>) {
        _clientImageFlow.value = result
    }

    fun setUnreadNotificationsCount(count: Int) {
        _unreadNotificationsFlow.value = DataState.Success(count)
    }

    fun reset() {
        _clientAccountsFlow.value = DataState.Success(ClientAccounts())
        _currentClientFlow.value = DataState.Success(createDefaultClient())
        _clientImageFlow.value = DataState.Success("")
        _unreadNotificationsFlow.value = DataState.Success(0)
    }

    override fun clientAccounts(clientId: Long): Flow<DataState<ClientAccounts>> {
        return _clientAccountsFlow.asStateFlow()
    }

    override fun currentClient(clientId: Long): Flow<DataState<Client>> {
        return _currentClientFlow.asStateFlow()
    }

    override fun clientImage(clientId: Long): Flow<DataState<String>> {
        return _clientImageFlow.asStateFlow()
    }

    override fun unreadNotificationsCount(): Flow<DataState<Int>> {
        return _unreadNotificationsFlow.asStateFlow()
    }

    private fun createDefaultClient(): Client {
        return Client(
            id = 1,
            displayName = "Test User",
            firstname = "Test",
            lastname = "User",
        )
    }
}
