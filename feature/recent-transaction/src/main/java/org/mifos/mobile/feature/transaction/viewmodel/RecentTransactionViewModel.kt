/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.transaction.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.mifos.mobile.core.data.repository.RecentTransactionRepository
import org.mifos.mobile.core.model.entity.Transaction
import org.mifos.mobile.feature.transaction.utils.RecentTransactionState
import org.mifos.mobile.feature.transaction.utils.RecentTransactionState.Loading
import javax.inject.Inject

@HiltViewModel
internal class RecentTransactionViewModel @Inject constructor(
    private val repository: RecentTransactionRepository,
) : ViewModel() {

    private val limit = 50
    private var transactions: MutableList<Transaction> = mutableListOf()

    private val _recentTransactionUiState = MutableStateFlow<RecentTransactionState>(Loading)
    val recentTransactionUiState = _recentTransactionUiState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> get() = _isRefreshing.asStateFlow()

    private val _isPaginating = MutableStateFlow(false)
    val isPaginating: StateFlow<Boolean> get() = _isPaginating.asStateFlow()

    init {
        loadInitialTransactions()
    }

    fun refresh() {
        _isRefreshing.value = true
        loadInitialTransactions()
    }

    fun loadPaginatedTransactions(offset: Int) {
        _isPaginating.value = true
        transactions.clear()
        loadRecentTransactions(offset)
    }

    fun loadInitialTransactions() {
        _recentTransactionUiState.value = Loading
        loadRecentTransactions(0)
    }

    private fun loadRecentTransactions(offset: Int) {
        viewModelScope.launch {
            repository.recentTransactions(offset, limit)
                .catch {
                    _recentTransactionUiState.value = RecentTransactionState.Error(it.message)
                }
                .collect {
                    transactions.plus(it.pageItems)
                    _recentTransactionUiState.value = RecentTransactionState.Success(
                        transactions = transactions,
                        canPaginate = it.pageItems.isNotEmpty(),
                    )
                    _isPaginating.emit(false)
                    _isRefreshing.emit(false)
                }
        }
    }
}
