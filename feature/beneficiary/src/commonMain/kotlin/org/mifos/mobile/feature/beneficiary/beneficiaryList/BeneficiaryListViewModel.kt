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
                BeneficiaryListEvent.AddBeneficiaryClicked,
            )

            is BeneficiaryListAction.OnBeneficiaryItemClick -> sendEvent(
                BeneficiaryListEvent.BeneficiaryItemClick(action.position),
            )

            is BeneficiaryListAction.OnNavigate -> sendEvent(
                BeneficiaryListEvent.Navigate,
            )
        }
    }
}

data class BeneficiaryListState(
    val networkUnavailable: Boolean = false,
    val isRefreshing: Boolean = false,
    val beneficiaries: List<Beneficiary> = emptyList(),
    val isEmpty:Boolean=false,
    val filteredBeneficiaries: List<Beneficiary> = emptyList(),
    val dialogState: DialogState?,
) {
    sealed interface DialogState {

        data class Error(val message: String) : DialogState

        data object Loading : DialogState
    }
}

sealed interface BeneficiaryListAction {
    data object RefreshBeneficiaries : BeneficiaryListAction
    data object OnAddBeneficiaryClicked : BeneficiaryListAction
    data class OnBeneficiaryItemClick(val position: Long) : BeneficiaryListAction
    data object OnNavigate : BeneficiaryListAction
}

sealed interface BeneficiaryListEvent {
    data object AddBeneficiaryClicked : BeneficiaryListEvent
    data class BeneficiaryItemClick(val position: Long) : BeneficiaryListEvent
    data object Navigate : BeneficiaryListEvent
}
