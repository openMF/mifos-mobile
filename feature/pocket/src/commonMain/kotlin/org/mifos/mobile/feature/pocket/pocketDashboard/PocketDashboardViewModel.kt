/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.pocket.pocketDashboard

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mifos_mobile.feature.pocket.generated.resources.Res
import mifos_mobile.feature.pocket.generated.resources.feature_pocket_error_load_accounts
import mifos_mobile.feature.pocket.generated.resources.feature_pocket_unknown_account
import mifos_mobile.feature.pocket.generated.resources.feature_pocket_unknown_status
import org.jetbrains.compose.resources.getString
import org.mifos.mobile.core.common.CurrencyFormatter
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.PocketRepository
import org.mifos.mobile.core.data.util.NetworkUnavailableException
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.core.model.entity.pocket.AccountStatus
import org.mifos.mobile.core.model.entity.pocket.DetailedPocketAccount
import org.mifos.mobile.core.model.enums.AccountType
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.core.ui.utils.ScreenUiState

internal class PocketDashboardViewModel(
    private val pocketRepository: PocketRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
) : BaseViewModel<PocketDashboardState, PocketDashboardEvent, PocketDashboardAction>(
    initialState = PocketDashboardState(
        clientId = requireNotNull(userPreferencesRepository.clientId.value),
    ),
) {
    private var loadJob: Job? = null

    init {
        viewModelScope.launch {
            pocketRepository.resetPocketCache()
            loadPocketData()
        }
    }

    private fun updateState(update: (PocketDashboardState) -> PocketDashboardState) {
        mutableStateFlow.update(update)
    }

    private fun loadPocketData(forceRefresh: Boolean = false) {
        loadJob?.cancel()

        loadJob = viewModelScope.launch {
            val clientId = state.clientId
            pocketRepository.getDetailedPocketAccounts(clientId, forceRefresh)
                .collect { dataState ->
                    trySendAction(PocketDashboardAction.Internal.ReceiveAccounts(dataState))
                }
        }
    }

    override fun handleAction(action: PocketDashboardAction) {
        when (action) {
            is PocketDashboardAction.Internal.ReceiveAccounts -> handleReceivedAccounts(action.dataState)
            PocketDashboardAction.NavigateBack -> sendEvent(PocketDashboardEvent.NavigateBack)
            PocketDashboardAction.ManagePocket -> sendEvent(PocketDashboardEvent.ManagePocket)
            PocketDashboardAction.LinkFirstAccount -> sendEvent(PocketDashboardEvent.ManagePocket)
            PocketDashboardAction.Retry -> retry()
            is PocketDashboardAction.NavigateToLoanDetail -> {
                sendEvent(PocketDashboardEvent.NavigateToLoanDetail(action.accountId))
            }
            is PocketDashboardAction.NavigateToSavingsDetail -> {
                sendEvent(PocketDashboardEvent.NavigateToSavingsDetail(action.accountId))
            }
            is PocketDashboardAction.NavigateToShareDetail -> {
                sendEvent(PocketDashboardEvent.NavigateToShareDetail(action.accountId))
            }
            PocketDashboardAction.Refresh -> refresh()
        }
    }

    private fun refresh() {
        updateState { it.copy(isRefreshing = true) }
        loadPocketData(forceRefresh = true)
    }

    private fun retry() {
        updateState { it.copy(uiState = ScreenUiState.Loading) }
        loadPocketData(forceRefresh = true)
    }

    private fun handleReceivedAccounts(dataState: DataState<List<DetailedPocketAccount>>) {
        viewModelScope.launch {
            when (dataState) {
                is DataState.Loading -> {
                    if (!state.isRefreshing) {
                        updateState { it.copy(uiState = ScreenUiState.Loading) }
                    }
                }

                is DataState.Error -> {
                    val isNetworkError = dataState.exception is NetworkUnavailableException

                    if (isNetworkError) {
                        updateState {
                            it.copy(
                                uiState = ScreenUiState.Network,
                                networkStatus = false,
                                isRefreshing = false,
                            )
                        }
                    } else {
                        updateState {
                            it.copy(
                                uiState = ScreenUiState.Error(Res.string.feature_pocket_error_load_accounts),
                                isRefreshing = false,
                            )
                        }
                    }
                }

                is DataState.Success -> {
                    val detailedAccounts = dataState.data

                    suspend fun mapToUiModel(detailed: DetailedPocketAccount): DetailedPocket {
                        val balanceStr = if (detailed.status == AccountStatus.ACTIVE) {
                            if (detailed.balance != null) {
                                CurrencyFormatter.format(
                                    detailed.balance,
                                    detailed.currencyCode,
                                    detailed.decimalPlaces,
                                )
                            } else {
                                ""
                            }
                        } else {
                            detailed.status?.name ?: getString(Res.string.feature_pocket_unknown_status)
                        }

                        return DetailedPocket(
                            accountId = detailed.pocket.accountId,
                            name = detailed.productName ?: getString(Res.string.feature_pocket_unknown_account),
                            accountNumber = detailed.pocket.accountNumber,
                            balanceOrStatus = balanceStr,
                            status = detailed.status ?: AccountStatus.UNKNOWN,
                        )
                    }

                    val loanList = mutableListOf<DetailedPocket>()
                    val savingsList = mutableListOf<DetailedPocket>()
                    val shareList = mutableListOf<DetailedPocket>()
                    for (account in detailedAccounts) {
                        when (account.pocket.accountType) {
                            AccountType.LOAN -> loanList.add(mapToUiModel(account))
                            AccountType.SAVINGS -> savingsList.add(mapToUiModel(account))
                            AccountType.SHARE -> shareList.add(mapToUiModel(account))
                        }
                    }

                    val balancesByCurrency = detailedAccounts
                        .filter { it.status == AccountStatus.ACTIVE && it.balance != null && it.currencyCode != null }
                        .groupBy { it.currencyCode!! }
                        .map { (currencyCode, accounts) ->
                            val sum = accounts.sumOf { it.balance ?: 0.0 }
                            CurrencyFormatter.format(
                                sum,
                                currencyCode,
                                accounts.first().decimalPlaces,
                            )
                        }

                    val formattedTotal = if (balancesByCurrency.isNotEmpty()) {
                        balancesByCurrency.joinToString("\n")
                    } else {
                        val sampleAccount = detailedAccounts.firstOrNull { it.currencyCode != null }
                        CurrencyFormatter.format(
                            0.0,
                            sampleAccount?.currencyCode,
                            sampleAccount?.decimalPlaces,
                        )
                    }

                    if (loanList.isEmpty() && shareList.isEmpty() && savingsList.isEmpty()) {
                        updateState {
                            it.copy(
                                uiState = ScreenUiState.Empty,
                                isRefreshing = false,
                            )
                        }
                    } else {
                        updateState {
                            it.copy(
                                uiState = ScreenUiState.Success,
                                totalBalance = formattedTotal,
                                loanAccounts = loanList,
                                savingsAccounts = savingsList,
                                shareAccounts = shareList,
                                isRefreshing = false,
                            )
                        }
                    }
                }
            }
        }
    }
}
data class PocketDashboardState(
    val clientId: Long = 0,
    val totalBalance: String = "$ 0",
    val loanAccounts: List<DetailedPocket> = emptyList(),
    val savingsAccounts: List<DetailedPocket> = emptyList(),
    val shareAccounts: List<DetailedPocket> = emptyList(),
    val uiState: ScreenUiState = ScreenUiState.Loading,
    val networkStatus: Boolean = true,
    val isRefreshing: Boolean = false,
)
data class DetailedPocket(
    val accountId: Long,
    val name: String,
    val accountNumber: String,
    val balanceOrStatus: String,
    val status: AccountStatus,
)

internal sealed interface PocketDashboardEvent {
    data object NavigateBack : PocketDashboardEvent
    data object ManagePocket : PocketDashboardEvent
    data class NavigateToLoanDetail(val accountId: Long) : PocketDashboardEvent
    data class NavigateToSavingsDetail(val accountId: Long) : PocketDashboardEvent
    data class NavigateToShareDetail(val accountId: Long) : PocketDashboardEvent
}

internal sealed interface PocketDashboardAction {
    data object NavigateBack : PocketDashboardAction
    data class NavigateToLoanDetail(val accountId: Long) : PocketDashboardAction
    data class NavigateToSavingsDetail(val accountId: Long) : PocketDashboardAction
    data class NavigateToShareDetail(val accountId: Long) : PocketDashboardAction
    data object ManagePocket : PocketDashboardAction
    data object LinkFirstAccount : PocketDashboardAction
    data object Refresh : PocketDashboardAction
    data object Retry : PocketDashboardAction

    sealed interface Internal : PocketDashboardAction {
        data class ReceiveAccounts(
            val dataState: DataState<List<DetailedPocketAccount>>,
        ) : Internal
    }
}
