/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.beneficiary.beneficiaryList

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.BeneficiaryRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.model.entity.templates.beneficiary.BeneficiaryTemplate
import org.mifos.mobile.core.ui.utils.BaseViewModel

internal class BeneficiaryListViewModel(
    private val beneficiaryRepositoryImp: BeneficiaryRepository,
    private val networkMonitor: NetworkMonitor,
) : BaseViewModel<BeneficiaryListState, BeneficiaryListEvent, BeneficiaryListAction>(
    initialState = BeneficiaryListState(dialogState = null),
) {

    init {
        observeNetworkStatus()
        fetchBeneficiaries()
        getBeneficiaryList()
    }

    override fun handleAction(action: BeneficiaryListAction) {
        when (action) {
            is BeneficiaryListAction.RefreshBeneficiaries -> fetchBeneficiaries()

            is BeneficiaryListAction.OnAddBeneficiaryClicked -> handleAddBeneficiaryClick()

            is BeneficiaryListAction.OnBeneficiaryItemClick -> handleBeneficiaryItemClick(action)

            is BeneficiaryListAction.OnNavigate -> handleNavigate()

            BeneficiaryListAction.ToggleFilter -> handleToggleFilterDialog()

            BeneficiaryListAction.GetFilterResults -> handleGetFilterResults()

            BeneficiaryListAction.ResetFilters -> resetFilters()

            BeneficiaryListAction.DismissDialog -> dismissDialog()

            is BeneficiaryListAction.OnAccountChange -> handleAccountChange(action)

            is BeneficiaryListAction.OnOfficeChange -> handleOfficeChange(action)

            is BeneficiaryListAction.Internal.ReceiveBeneficiaryResult -> processBeneficiaryList(action.beneficiaryList)

            BeneficiaryListAction.LoadBeneficiaries -> fetchBeneficiaries()
        }
    }

    private fun updateState(update: (BeneficiaryListState) -> BeneficiaryListState) {
        mutableStateFlow.update(update)
    }

    private fun observeNetworkStatus() {
        viewModelScope.launch {
            networkMonitor.isOnline
                .map(Boolean::not)
                .distinctUntilChanged()
                .collect { isOffline ->
                    updateState {
                        it.copy(
                            networkUnavailable = isOffline,
                            dialogState = if (isOffline) {
                                BeneficiaryListState.DialogState.Error("")
                            } else {
                                null
                            },
                        )
                    }
                }
        }
    }

    private fun fetchBeneficiaries() {
        updateState {
            it.copy(
                dialogState = BeneficiaryListState.DialogState.Loading,
            )
        }
        viewModelScope.launch {
            beneficiaryRepositoryImp.beneficiaryList().catch { e ->
                updateState {
                    it.copy(
                        dialogState = BeneficiaryListState.DialogState.Error(
                            e.message.toString(),
                        ),
                    )
                }
            }.collect { beneficiaryList ->
                sendAction(BeneficiaryListAction.Internal.ReceiveBeneficiaryResult(beneficiaryList))
            }
        }
    }
    private fun processBeneficiaryList(beneficiaryList: DataState<List<Beneficiary>>) {
        when (beneficiaryList) {
            DataState.Loading -> updateState {
                it.copy(
                    dialogState = BeneficiaryListState.DialogState.Loading,
                )
            }

            is DataState.Success -> {
                updateState {
                    it.copy(
                        dialogState = null,
                        beneficiaries = beneficiaryList.data,
                        filteredBeneficiaries = beneficiaryList.data,
                        isEmpty = beneficiaryList.data.isEmpty(),
                        selectedOffices = emptySet(),
                        selectedAccounts = emptySet(),
                        isFilteredEmpty = false,
                    )
                }
                getOffices(beneficiaryList.data)
            }

            is DataState.Error -> {
                updateState {
                    it.copy(
                        dialogState = BeneficiaryListState.DialogState.Error(
                            beneficiaryList.message,
                        ),
                    )
                }
            }
        }
    }

    private fun handleAddBeneficiaryClick() {
        sendEvent(BeneficiaryListEvent.AddBeneficiaryClicked)
    }

    private fun handleBeneficiaryItemClick(action: BeneficiaryListAction.OnBeneficiaryItemClick) {
        sendEvent(BeneficiaryListEvent.BeneficiaryItemClick(action.position))
    }

    private fun handleNavigate() {
        sendEvent(BeneficiaryListEvent.Navigate)
    }

    private fun handleGetFilterResults() {
        val filteredAccounts = if (state.selectedAccounts.isNotEmpty()) {
            state.beneficiaries.filter {
                state.selectedAccounts.contains(it.accountType?.value)
            }
        } else {
            state.beneficiaries
        }

        val filteredOffices = if (state.selectedOffices.isNotEmpty()) {
            filteredAccounts.filter {
                state.selectedOffices.contains(it.officeName)
            }
        } else {
            filteredAccounts
        }

        updateState {
            it.copy(
                filteredBeneficiaries = filteredOffices,
                dialogState = null,
                isFilteredEmpty = filteredOffices.isEmpty(),
            )
        }
    }

    private fun handleAccountChange(action: BeneficiaryListAction.OnAccountChange) {
        val currentAccounts = state.selectedAccounts
        val updatedAccounts = if (currentAccounts.contains(action.account)) {
            currentAccounts.minus(action.account)
        } else {
            currentAccounts.plus(action.account)
        }

        updateState {
            it.copy(selectedAccounts = updatedAccounts)
        }
    }

    private fun handleOfficeChange(action: BeneficiaryListAction.OnOfficeChange) {
        val currentOffices = state.selectedOffices
        val updatedOffices = if (currentOffices.contains(action.office)) {
            currentOffices.minus(action.office)
        } else {
            currentOffices.plus(action.office)
        }

        updateState {
            it.copy(selectedOffices = updatedOffices)
        }
    }

    private fun getBeneficiaryList() {
        viewModelScope.launch {
            beneficiaryRepositoryImp.beneficiaryTemplate().collect { result ->
                when (result) {
                    is DataState.Error -> {}
                    DataState.Loading -> {}
                    is DataState.Success -> {
                        updateState {
                            it.copy(
                                template = result.data,
                            )
                        }
                    }
                }
            }
        }
    }

    private fun handleToggleFilterDialog() {
        updateState {
            it.copy(
                dialogState = BeneficiaryListState.DialogState.Filters,
            )
        }
    }

    private fun getOffices(beneficiaries: List<Beneficiary>) {
        val offices = beneficiaries.map {
            it.officeName
        }.distinct()
        updateState {
            it.copy(
                offices = offices,
            )
        }
    }

    private fun resetFilters() {
        updateState {
            it.copy(
                selectedOffices = emptySet(),
                selectedAccounts = emptySet(),
                filteredBeneficiaries = it.beneficiaries,
            )
        }
    }

    private fun dismissDialog() {
        updateState {
            it.copy(
                dialogState = null,
            )
        }
    }
}

data class BeneficiaryListState(
    val networkUnavailable: Boolean = false,
    val isRefreshing: Boolean = false,
    val beneficiaries: List<Beneficiary> = emptyList(),
    val template: BeneficiaryTemplate? = null,
    val selectedAccounts: Set<String> = emptySet(),
    val selectedOffices: Set<String> = emptySet(),
    val offices: List<String?> = emptyList(),
    val isEmpty: Boolean = false,
    val isFilteredEmpty: Boolean = false,
    val filteredBeneficiaries: List<Beneficiary> = emptyList(),
    val dialogState: DialogState?,
) {
    sealed interface DialogState {

        data class Error(val message: String) : DialogState

        data object Loading : DialogState

        data object Filters : DialogState
    }

    val isAnyFilterSelected = selectedAccounts.isNotEmpty() || selectedOffices.isNotEmpty()
}

sealed interface BeneficiaryListAction {
    data object RefreshBeneficiaries : BeneficiaryListAction
    data object OnAddBeneficiaryClicked : BeneficiaryListAction
    data class OnBeneficiaryItemClick(val position: Long) : BeneficiaryListAction
    data object OnNavigate : BeneficiaryListAction
    data object ToggleFilter : BeneficiaryListAction
    data object ResetFilters : BeneficiaryListAction
    data object GetFilterResults : BeneficiaryListAction
    data object DismissDialog : BeneficiaryListAction
    data class OnAccountChange(val account: String) : BeneficiaryListAction
    data class OnOfficeChange(val office: String) : BeneficiaryListAction
    data object LoadBeneficiaries : BeneficiaryListAction

    sealed interface Internal : BeneficiaryListAction {

        data class ReceiveBeneficiaryResult(
            val beneficiaryList: DataState<List<Beneficiary>>,
        ) : Internal
    }
}

sealed interface BeneficiaryListEvent {
    data object AddBeneficiaryClicked : BeneficiaryListEvent
    data class BeneficiaryItemClick(val position: Long) : BeneficiaryListEvent
    data object Navigate : BeneficiaryListEvent
}
