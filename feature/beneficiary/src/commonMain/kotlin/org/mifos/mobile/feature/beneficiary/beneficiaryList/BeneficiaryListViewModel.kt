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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.BeneficiaryRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.model.IgnoredOnParcel
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.ui.utils.BaseViewModel

internal class BeneficiaryListViewModel(
    private val beneficiaryRepositoryImp: BeneficiaryRepository,
    private val networkMonitor: NetworkMonitor,
) : BaseViewModel<BeneficiaryListState, BeneficiaryListEvent, BeneficiaryListAction>(
    initialState = BeneficiaryListState(dialogState = null),
) {

    init {
        viewModelScope.launch {
            networkMonitor.isOnline.collect { isConnected ->
                updateState { it.copy(isOnline = isConnected) }
            }
        }
        fetchBeneficiaries(isRefreshing = false)
    }

    private fun updateState(update: (BeneficiaryListState) -> BeneficiaryListState) {
        mutableStateFlow.update(update)
    }

    private fun fetchBeneficiaries(isRefreshing: Boolean) {
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
                if (isRefreshing) {
                    sendEvent(BeneficiaryListEvent.ShowToast("Beneficiaries refreshed successfully"))
                }
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
            BeneficiaryListAction.LoadBeneficiaries -> fetchBeneficiaries(isRefreshing = false)
            BeneficiaryListAction.RefreshBeneficiaries -> fetchBeneficiaries(isRefreshing = true)
            BeneficiaryListAction.OnAddBeneficiaryClicked -> sendEvent(
                BeneficiaryListEvent.AddBeneficiaryClicked,
            )
            is BeneficiaryListAction.OnBeneficiaryItemClick -> sendEvent(
                BeneficiaryListEvent.BeneficiaryItemClick(action.position),
            )
            BeneficiaryListAction.OnNavigate -> sendEvent(
                BeneficiaryListEvent.Navigate,
            )
        }
    }
}

@Parcelize
data class BeneficiaryListState(
    val isOnline: Boolean = false,
    val isRefreshing: Boolean = false,
    @IgnoredOnParcel
    val beneficiaries: List<Beneficiary> = emptyList(),
    val dialogState: DialogState?,
) : Parcelable {
    sealed interface DialogState : Parcelable {
        @Parcelize
        data class Error(val message: String) : DialogState

        @Parcelize
        data object Loading : DialogState
    }
}

sealed interface BeneficiaryListAction {
    data object LoadBeneficiaries : BeneficiaryListAction
    data object RefreshBeneficiaries : BeneficiaryListAction
    data object OnAddBeneficiaryClicked : BeneficiaryListAction
    data class OnBeneficiaryItemClick(val position: Int) : BeneficiaryListAction
    data object OnNavigate : BeneficiaryListAction
}

sealed interface BeneficiaryListEvent {
    data class ShowToast(val message: String) : BeneficiaryListEvent
    data object AddBeneficiaryClicked : BeneficiaryListEvent
    data class BeneficiaryItemClick(val position: Int) : BeneficiaryListEvent
    data object Navigate : BeneficiaryListEvent
}
