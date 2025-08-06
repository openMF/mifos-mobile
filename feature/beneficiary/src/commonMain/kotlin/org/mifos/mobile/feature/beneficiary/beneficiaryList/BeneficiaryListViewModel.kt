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
import co.touchlab.kermit.Logger
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
import org.mifos.mobile.feature.beneficiary.beneficiaryList.BeneficiaryListEvent.*

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
                processBeneficiaryList(beneficiaryList)
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
                        isEmpty = beneficiaryList.data.isEmpty()
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

    override fun handleAction(action: BeneficiaryListAction) {
        when (action) {
            is BeneficiaryListAction.RefreshBeneficiaries -> fetchBeneficiaries()

            is BeneficiaryListAction.OnAddBeneficiaryClicked -> sendEvent(
                AddBeneficiaryClicked,
            )

            is BeneficiaryListAction.OnBeneficiaryItemClick -> sendEvent(
                BeneficiaryItemClick(action.position),
            )

            is BeneficiaryListAction.OnNavigate -> sendEvent(
                Navigate,
            )

            BeneficiaryListAction.ToggleFilter -> handleToggleFilterDialog()

            BeneficiaryListAction.GetFilterResults -> {

            }

            BeneficiaryListAction.ResetFilters -> resetFilters()

            BeneficiaryListAction.DismissDialog -> dismissDialog()

            is BeneficiaryListAction.OnAccountChange -> {
                val currentAccounts=state.selectedAccounts
                if(currentAccounts.contains(action.account)){
                    updateState {
                        it.copy(
                            selectedAccounts = currentAccounts.minus(action.account)
                        )
                    }
                }
                else{
                    updateState {
                        it.copy(
                            selectedAccounts = currentAccounts.plus(action.account)
                        )
                    }
                }
            }

            is BeneficiaryListAction.OnOfficeChange -> {
                val currentOffices=state.selectedOffices
                if(currentOffices.contains(action.office)){
                    updateState {
                        it.copy(
                            selectedOffices = currentOffices.minus(action.office)
                        )
                    }
                    }
                else{
                    updateState {
                        it.copy(
                            selectedOffices = currentOffices.plus(action.office)
                        )
                    }
                }
            }
        }
    }

    private fun getBeneficiaryList(){
        viewModelScope.launch {
            beneficiaryRepositoryImp.beneficiaryTemplate().collect { result->
                when(result){
                    is DataState.Error -> {}
                    DataState.Loading -> {}
                    is DataState.Success -> {
                        updateState {
                            it.copy(
                                template = result.data
                            )
                        }
                        Logger.e("Revanth"){
                            result.data.toString()
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

    private fun getOffices(beneficiaries: List<Beneficiary>){
        val offices=beneficiaries.map {
            it.officeName
        }.distinct()
        updateState {
            it.copy(
                offices=offices
            )
        }
        Logger.e("Revanth"){
            offices.toString()
        }
    }

    private fun resetFilters(){
        updateState {
            it.copy(
                selectedOffices = emptySet(),
                selectedAccounts = emptySet(),
                filteredBeneficiaries = it.beneficiaries
            )
        }
    }

    private fun dismissDialog(){
        updateState {
            it.copy(
                dialogState = null
            )
        }
    }
}

data class BeneficiaryListState(
    val networkUnavailable: Boolean = false,
    val isRefreshing: Boolean = false,
    val beneficiaries: List<Beneficiary> = emptyList(),
    val template: BeneficiaryTemplate? = null,
    val selectedAccounts:Set<String> =emptySet(),
    val selectedOffices : Set<String> =emptySet(),
    val offices:List<String?> =emptyList(),
    val isEmpty:Boolean=false,
    val filteredBeneficiaries: List<Beneficiary> = emptyList(),
    val dialogState: DialogState?,
) {
    sealed interface DialogState {

        data class Error(val message: String) : DialogState

        data object Loading : DialogState

        data object Filters : DialogState
    }

    val isAnyFilterSelected=selectedAccounts.isNotEmpty()||selectedOffices.isNotEmpty()
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
}

sealed interface BeneficiaryListEvent {
    data object AddBeneficiaryClicked : BeneficiaryListEvent
    data class BeneficiaryItemClick(val position: Long) : BeneficiaryListEvent
    data object Navigate : BeneficiaryListEvent
}
