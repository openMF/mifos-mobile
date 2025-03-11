/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.beneficiary.beneficiaryDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mifos_mobile.feature.beneficiary.generated.resources.Res
import mifos_mobile.feature.beneficiary.generated.resources.delete_beneficiary_confirmation
import org.jetbrains.compose.resources.getString
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.BeneficiaryRepository
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.feature.beneficiary.navigation.BENEFICIARY_ID

internal class BeneficiaryDetailViewModel(
    private val beneficiaryRepositoryImp: BeneficiaryRepository,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<BeneficiaryDetailState, BeneficiaryDetailEvent, BeneficiaryDetailAction>(
    initialState = BeneficiaryDetailState(
        beneficiaryDialog = null,
        beneficiaryId = savedStateHandle.getStateFlow<Int?>(
            key = BENEFICIARY_ID,
            initialValue = null,
        ).value,
    ),
) {

    init {
        loadBeneficiary()
    }

    private fun updateState(update: (BeneficiaryDetailState) -> BeneficiaryDetailState) {
        mutableStateFlow.update(update)
    }

    private fun setDialogState(dialogState: BeneficiaryDetailState.DialogState?) {
        updateState { it.copy(beneficiaryDialog = dialogState) }
    }

    private fun loadBeneficiary() {
        updateState {
            it.copy(
                beneficiaryDialog = BeneficiaryDetailState.DialogState.Loading,
            )
        }
        viewModelScope.launch {
            beneficiaryRepositoryImp.beneficiaryList()
                .catch { e ->
                    setDialogState(
                        BeneficiaryDetailState.DialogState.Error(
                            e.message ?: "Error loading beneficiary",
                        ),
                    )
                }.collect { beneficiary ->
                    handleResponse(beneficiary)
                }
        }
    }

    private fun handleResponse(beneficiary: DataState<List<Beneficiary>>) {
        when (beneficiary) {
            DataState.Loading -> {
                setDialogState(BeneficiaryDetailState.DialogState.Loading)
            }
            is DataState.Error -> {
                setDialogState(
                    BeneficiaryDetailState.DialogState.Error(
                        beneficiary.message,
                    ),
                )
            }
            is DataState.Success -> {
                updateState { currentState ->
                    currentState.copy(
                        beneficiaryDialog = null,
                        beneficiary = beneficiary.data.find { it.id == currentState.beneficiaryId },
                    )
                }
            }
        }
    }

    private fun deleteBeneficiary(beneficiaryId: Int?) {
        viewModelScope.launch {
//            val errorMsg = getString(Res.string.error_deleting_beneficiary)
            setDialogState(BeneficiaryDetailState.DialogState.Loading)
            val response = beneficiaryRepositoryImp.deleteBeneficiary(beneficiaryId?.toLong())
            when (response) {
                DataState.Loading -> {
                    setDialogState(BeneficiaryDetailState.DialogState.Loading)
                }
                is DataState.Error -> {
                    setDialogState(
                        BeneficiaryDetailState.DialogState.Error(
                            response.message,
                        ),
                    )
                }
                is DataState.Success -> {
                    setDialogState(null)
                    sendEvent(BeneficiaryDetailEvent.ShowToast(response.data))
                    delay(1500)
                    sendEvent(BeneficiaryDetailEvent.Navigate)
                }
            }
        }
    }

    override fun handleAction(action: BeneficiaryDetailAction) {
        when (action) {
            is BeneficiaryDetailAction.DeleteBeneficiary -> deleteBeneficiary(action.beneficiaryId)
            is BeneficiaryDetailAction.OnUpdateBeneficiary -> sendEvent(
                BeneficiaryDetailEvent.UpdateBeneficiary(action.beneficiary),
            )
            BeneficiaryDetailAction.OnNavigate -> sendEvent(BeneficiaryDetailEvent.Navigate)
            is BeneficiaryDetailAction.ErrorDialogDismiss -> updateState { it.copy(beneficiaryDialog = null) }
            BeneficiaryDetailAction.ShowDeleteConfirmation -> showDeleteConfirmation()
            BeneficiaryDetailAction.OnRefresh -> loadBeneficiary()
        }
    }

    private fun showDeleteConfirmation() {
        viewModelScope.launch {
            val message = getString(Res.string.delete_beneficiary_confirmation)
            setDialogState(
                BeneficiaryDetailState.DialogState.Confirmation(
                    message,
                ),
            )
        }
    }
}

@Parcelize
data class BeneficiaryDetailState(
    val beneficiaryId: Int? = null,
    val beneficiary: Beneficiary? = null,
    val beneficiaryDialog: DialogState?,
) : Parcelable {
    sealed interface DialogState : Parcelable {
        @Parcelize
        data class Error(val message: String) : DialogState

        @Parcelize
        data object Loading : DialogState

        @Parcelize
        data class Confirmation(val message: String) : DialogState
    }
}

sealed interface BeneficiaryDetailEvent {
    data class ShowToast(val message: String) : BeneficiaryDetailEvent
    data object Navigate : BeneficiaryDetailEvent
    data class UpdateBeneficiary(val beneficiary: Beneficiary?) : BeneficiaryDetailEvent
}

sealed interface BeneficiaryDetailAction {
    data object OnRefresh : BeneficiaryDetailAction
    data class OnUpdateBeneficiary(val beneficiary: Beneficiary?) : BeneficiaryDetailAction
    data class DeleteBeneficiary(val beneficiaryId: Int?) : BeneficiaryDetailAction
    data object OnNavigate : BeneficiaryDetailAction
    data object ErrorDialogDismiss : BeneficiaryDetailAction
    data object ShowDeleteConfirmation : BeneficiaryDetailAction
}
