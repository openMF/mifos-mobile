/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.recent.transaction.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.mifos.mobile.core.data.repository.RecentTransactionRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.feature.recent.transaction.utils.RecentTransactionState
import org.mifos.mobile.feature.recent.transaction.utils.RecentTransactionState.Loading

class RecentTransactionViewModel(
    private val recentTransactionRepositoryImpl: RecentTransactionRepository,
    networkMonitor: NetworkMonitor,
    userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {

    private val clientId = requireNotNull(userPreferencesRepository.clientId.value)
    private val limit = 50

    private val _recentTransactionUiState = MutableStateFlow<RecentTransactionState>(Loading)
    val recentTransactionUiState = _recentTransactionUiState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> get() = _isRefreshing.asStateFlow()

    private val _isPaginating = MutableStateFlow(false)
    val isPaginating: StateFlow<Boolean> get() = _isPaginating.asStateFlow()

    val isNetworkAvailable = networkMonitor.isOnline
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

    init {
        loadInitialTransactions()
    }

    fun refresh() {
        _isRefreshing.value = true
        loadInitialTransactions()
    }

    fun loadPaginatedTransactions(offset: Int) {
        _isPaginating.value = true
        loadRecentTransactions(clientId, offset, limit)
    }

    fun loadInitialTransactions() {
        _recentTransactionUiState.value = Loading
        loadRecentTransactions(clientId, 0, limit)
    }

    private fun loadRecentTransactions(
        clientId: Long?,
        offset: Int?,
        limit: Int?,
    ) {
        viewModelScope.launch {
            recentTransactionRepositoryImpl.recentTransactions(
                clientId,
                offset,
                limit,
            ).catch {
                _recentTransactionUiState.value = RecentTransactionState.Error
            }
                .collect { recentTransactions ->
                    val recentTransactionsList = recentTransactions.data?.pageItems
                    _recentTransactionUiState.value = if (recentTransactionsList.isNullOrEmpty()) {
                        RecentTransactionState.Empty
                    } else {
                        RecentTransactionState.Success(
                            transactions = recentTransactionsList,
                            canPaginate = recentTransactionsList.isNotEmpty(),
                        )
                    }
                    _isPaginating.value = false
                    _isRefreshing.value = false
                }
        }
    }
}
