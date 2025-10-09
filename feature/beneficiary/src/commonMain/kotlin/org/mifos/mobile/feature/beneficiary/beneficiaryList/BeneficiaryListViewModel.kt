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
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.io.IOException
import mifos_mobile.feature.beneficiary.generated.resources.Res
import mifos_mobile.feature.beneficiary.generated.resources.beneficiary
import mifos_mobile.feature.beneficiary.generated.resources.feature_generic_error_server
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.BeneficiaryRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.model.entity.templates.beneficiary.BeneficiaryTemplate
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.core.ui.utils.ScreenUiState

/**
 * ViewModel for BeneficiaryListScreen
 *
 * @param beneficiaryRepositoryImp The repository for beneficiary data.
 * @param networkMonitor The monitor for network connectivity.
 */
internal class BeneficiaryListViewModel(
    private val beneficiaryRepositoryImp: BeneficiaryRepository,
    private val networkMonitor: NetworkMonitor,
) : BaseViewModel<BeneficiaryListState, BeneficiaryListEvent, BeneficiaryListAction>(
    initialState = BeneficiaryListState(),
) {

    /**
     * Initialize the view model.
     */
    init {
        observeNetwork()
    }

    /**
     * Initialize the view model.
     */
    init {
        observeNetwork()
    }

    /**
     * Observe network and dispatch status changes into actions
     */
    private fun observeNetwork() {
        viewModelScope.launch {
            networkMonitor.isOnline
                .distinctUntilChanged()
                .collect { isOnline ->
                    sendAction(BeneficiaryListAction.ReceiveNetworkStatus(isOnline))
                }
        }
    }

    /**
     * Handle network state changes like LoanAccountDetailsViewModel
     */
    private fun handleNetworkStatus(isOnline: Boolean) {
        updateState { it.copy(networkStatus = isOnline) }

        viewModelScope.launch {
            if (!isOnline) {
                updateState { current ->
                    if (current.uiState is ScreenUiState.Loading ||
                        current.uiState is ScreenUiState.Error ||
                        current.uiState is ScreenUiState.Network
                    ) {
                        current.copy(
                            uiState = ScreenUiState.Network,
                        )
                    } else {
                        current
                    }
                }
            } else {
                fetchBeneficiaries()
                getBeneficiaryList()
            }
        }
    }

    override fun handleAction(action: BeneficiaryListAction) {
        when (action) {
            is BeneficiaryListAction.ReceiveNetworkStatus -> handleNetworkStatus(action.isOnline)

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

    /**
     * Update the state of the view model.
     *
     * @param update The function to update the state.
     */
    private fun updateState(update: (BeneficiaryListState) -> BeneficiaryListState) {
        mutableStateFlow.update(update)
    }

    /**
     * Fetch the list of beneficiaries from the repository.
     */
    private fun fetchBeneficiaries() {
        updateState {
            it.copy(
                uiState = ScreenUiState.Loading,
            )
        }
        viewModelScope.launch {
            beneficiaryRepositoryImp.beneficiaryList().collect { beneficiaryList ->
                sendAction(BeneficiaryListAction.Internal.ReceiveBeneficiaryResult(beneficiaryList))
            }
        }
    }

    /**
     * Process the list of beneficiaries.
     *
     * @param beneficiaryList The list of beneficiaries.
     */
    private fun processBeneficiaryList(beneficiaryList: DataState<List<Beneficiary>>) {
        when (beneficiaryList) {
            DataState.Loading -> updateState {
                it.copy(
                    uiState = ScreenUiState.Loading,
                )
            }

            is DataState.Success -> {
                val list = beneficiaryList.data

                updateState {
                    if (list.isEmpty()) {
                        it.copy(
                            uiState = ScreenUiState.Empty,
                        )
                    } else {
                        it.copy(
                            beneficiaries = list,
                            filteredBeneficiaries = list,
                            selectedOffices = emptySet(),
                            selectedAccounts = emptySet(),
                            isFilteredEmpty = false,
                            uiState = ScreenUiState.Success,
                        )
                    }
                }

                if (list.isNotEmpty()) {
                    getOffices(list)
                }
            }

            is DataState.Error -> {
                updateState {
                    it.copy(
                        uiState = if (beneficiaryList.exception is IOException) {
                            ScreenUiState.Network
                        } else {
                            ScreenUiState.Error(Res.string.feature_generic_error_server)
                        },
                    )
                }
            }
        }
    }

    /**
     * Handle the click on the add beneficiary button.
     */
    private fun handleAddBeneficiaryClick() {
        sendEvent(BeneficiaryListEvent.AddBeneficiaryClicked)
    }

    /**
     * Handle the click on a beneficiary item.
     *
     * @param action The action to handle.
     */
    private fun handleBeneficiaryItemClick(action: BeneficiaryListAction.OnBeneficiaryItemClick) {
        sendEvent(BeneficiaryListEvent.BeneficiaryItemClick(action.position))
    }

    /**
     * Handle the click on the navigate button.
     */
    private fun handleNavigate() {
        sendEvent(BeneficiaryListEvent.Navigate)
    }

    /**
     * Handle the click on the get filter results button.
     */
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

    /**
     * Handle the change of the selected accounts.
     *
     * @param action The action to handle.
     */
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

    /**
     * Handle the change of the selected offices.
     *
     * @param action The action to handle.
     */
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

    /**
     * Handle the click on the toggle filter dialog button.
     */
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

    /**
     * Reset the filters.
     */
    private fun resetFilters() {
        updateState {
            it.copy(
                selectedOffices = emptySet(),
                selectedAccounts = emptySet(),
                filteredBeneficiaries = it.beneficiaries,
            )
        }
    }

    /**
     * Dismiss the dialog.
     */
    private fun dismissDialog() {
        updateState {
            it.copy(
                dialogState = null,
            )
        }
    }
}

/**
 * State class for the beneficiary list screen.
 *
 * @param networkStatus The network status.
 * @param isRefreshing Whether the screen is refreshing.
 * @param beneficiaries The list of beneficiaries.
 * @param template The beneficiary template.
 * @param selectedAccounts The selected accounts.
 * @param selectedOffices The selected offices.
 * @param offices The list of offices.
 * @param isEmpty Whether the list of beneficiaries is empty.
 * @param isFilteredEmpty Whether the filtered list of beneficiaries is empty.
 * @param filteredBeneficiaries The filtered list of beneficiaries.
 * @param dialogState The dialog state.
 * @param uiState The screen UI state.
 */

data class BeneficiaryListState(
    val networkStatus: Boolean = false,
    val isRefreshing: Boolean = false,
    val beneficiaries: List<Beneficiary> = emptyList(),
    val template: BeneficiaryTemplate? = null,
    val selectedAccounts: Set<String> = emptySet(),
    val selectedOffices: Set<String> = emptySet(),
    val offices: List<String?> = emptyList(),
    val isEmpty: Boolean = false,
    val isFilteredEmpty: Boolean = false,
    val filteredBeneficiaries: List<Beneficiary> = emptyList(),
    val dialogState: DialogState? = null,
    val uiState: ScreenUiState = ScreenUiState.Loading,
) {
    sealed interface DialogState {

        data object Filters : DialogState
    }

    val isAnyFilterSelected = selectedAccounts.isNotEmpty() || selectedOffices.isNotEmpty()
}

/**
 * Action class for the beneficiary list screen.
 *
 * @param RefreshBeneficiaries Refresh the list of beneficiaries.
 * @param OnAddBeneficiaryClicked Add a new beneficiary.
 * @param OnBeneficiaryItemClick Click on a beneficiary item.
 * @param OnNavigate Navigate to another screen.
 * @param ToggleFilter Toggle the filter dialog.
 * @param ResetFilters Reset the filters.
 * @param GetFilterResults Get the filter results.
 * @param DismissDialog Dismiss the dialog.
 * @param OnAccountChange Change the selected account.
 */
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
    data class ReceiveNetworkStatus(val isOnline: Boolean) : BeneficiaryListAction

    sealed interface Internal : BeneficiaryListAction {
        data class ReceiveBeneficiaryResult(
            val beneficiaryList: DataState<List<Beneficiary>>,
        ) : Internal
    }
}

/**
 * Event class for the beneficiary list screen.
 *
 * @param AddBeneficiaryClicked Add a new beneficiary.
 * @param BeneficiaryItemClick Click on a beneficiary item.
 * @param Navigate Navigate to another screen.
 */
sealed interface BeneficiaryListEvent {
    data object AddBeneficiaryClicked : BeneficiaryListEvent
    data class BeneficiaryItemClick(val position: Long) : BeneficiaryListEvent
    data object Navigate : BeneficiaryListEvent
}
