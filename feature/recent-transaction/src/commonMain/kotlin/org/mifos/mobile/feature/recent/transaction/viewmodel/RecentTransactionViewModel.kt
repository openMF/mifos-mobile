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
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
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
            recentTransactionRepositoryImpl.recentTransactions(clientId, offset, limit)
                .onStart {
                    if (!_isRefreshing.value && !_isPaginating.value) {
                        _recentTransactionUiState.value = Loading
                    }
                }
                .catch {
                    _recentTransactionUiState.value = RecentTransactionState.Error
                }
                .onCompletion {
                    _isPaginating.value = false
                    _isRefreshing.value = false
                }
                .collect { recentTransactions ->
                    val items = recentTransactions.data?.pageItems

                    if (items == null) {
                        return@collect
                    }

                    val isInitialLoad = offset == 0

                    _recentTransactionUiState.value = when {
                        items.isNotEmpty() -> {
                            RecentTransactionState.Success(
                                transactions = items,
                                canPaginate = items.size >= (limit ?: 50),
                            )
                        }
                        isInitialLoad -> {
                            RecentTransactionState.Empty
                        }
                        else -> {
                            // Retain existing UI state if paginating with no new data
                            _recentTransactionUiState.value
                        }
                    }
                }
        }
    }
}
