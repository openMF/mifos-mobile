/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.guarantor.screens.guarantorDetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mifos_mobile.feature.guarantor.generated.resources.Res
import mifos_mobile.feature.guarantor.generated.resources.guarantor_deleted_successfully
import org.jetbrains.compose.resources.getString
import org.mifos.mobile.core.common.Constants.INDEX
import org.mifos.mobile.core.common.Constants.LOAN_ID
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.GuarantorRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.model.IgnoredOnParcel
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize
import org.mifos.mobile.core.model.entity.guarantor.GuarantorPayload
import org.mifos.mobile.core.ui.utils.BaseViewModel

/**
 * Currently we do not get back any response from the guarantorApi, hence we are using FakeRemoteDataSource
 * to show a list of guarantors. You can look at the implementation of [GuarantorRepository] for better understanding
 */

internal class GuarantorDetailViewModel(
    private val guarantorRepositoryImp: GuarantorRepository,
    savedStateHandle: SavedStateHandle,
    networkMonitor: NetworkMonitor,
) : BaseViewModel<GuarantorDetailState, GuarantorDetailEvent, GuarantorDetailAction>(
    initialState = GuarantorDetailState(
        dialogState = null,
        loanId = savedStateHandle.getStateFlow<Long?>(LOAN_ID, null).value,
        index = savedStateHandle.getStateFlow<Int?>(INDEX, null).value,
    ),
) {

    init {
        viewModelScope.launch {
            networkMonitor.isOnline.collect { isConnected ->
                updateState { it.copy(isOnline = isConnected) }
            }
        }
        getGuarantorItem()
    }

    private fun updateState(update: (GuarantorDetailState) -> GuarantorDetailState) {
        mutableStateFlow.update(update)
    }

    private fun getGuarantorItem() {
        viewModelScope.launch {
            state.loanId?.let {
                updateState { state -> state.copy(dialogState = GuarantorDetailState.DialogState.Loading) }

                guarantorRepositoryImp.getGuarantorList(loanId = it)
                    .collect { result ->

                        updateState { currentState ->
                            when (result) {
                                is DataState.Error -> {
                                    currentState.copy(
                                        dialogState = GuarantorDetailState.DialogState.ShowToast(
                                            result.message,
                                        ),
                                    )
                                }

                                is DataState.Loading -> {
                                    currentState.copy(dialogState = GuarantorDetailState.DialogState.Loading)
                                }

                                is DataState.Success -> {
                                    currentState.copy(
                                        dialogState = null,
                                        guarantor = result.data?.filter { it?.status == true }
                                            ?.get(index = state.index ?: -1),
                                    )
                                }
                            }
                        }
                    }
            }
        }
    }

    private fun deleteGuarantor(guarantorId: Long) {
        viewModelScope.launch {
            when (
                val result = guarantorRepositoryImp.deleteGuarantor(
                    loanId = state.loanId,
                    guarantorId = guarantorId,
                )
            ) {
                is DataState.Error -> {
                    updateState {
                        it.copy(dialogState = GuarantorDetailState.DialogState.ShowToast(result.message))
                    }
                }

                is DataState.Loading -> {
                    updateState { it.copy(dialogState = GuarantorDetailState.DialogState.Loading) }
                }

                is DataState.Success -> {
                    val msg = getString(Res.string.guarantor_deleted_successfully)
                    updateState {
                        it.copy(dialogState = GuarantorDetailState.DialogState.ShowToast(msg))
                    }
                }
            }
        }
    }

    override fun handleAction(action: GuarantorDetailAction) {
        when (action) {
            is GuarantorDetailAction.DeleteGuarantor -> state.guarantor?.id?.let {
                deleteGuarantor(
                    it,
                )
            }

            is GuarantorDetailAction.NavigateBack -> sendEvent(GuarantorDetailEvent.NavigateBack)

            is GuarantorDetailAction.UpdateMenuDialogValue -> updateState { it.copy(showDialog = !state.showDialog) }

            is GuarantorDetailAction.UpdateGuarantor -> sendEvent(
                GuarantorDetailEvent.UpdateGuarantor(
                    state.index ?: -1,
                    state.loanId ?: -1,
                ),
            )

            is GuarantorDetailAction.DismissDialog -> {
                updateState { it.copy(dialogState = null) }
            }
        }
    }
}

@Parcelize
data class GuarantorDetailState(
    val loanId: Long? = null,
    val index: Int? = null,
    val dialogState: DialogState?,
    @IgnoredOnParcel
    val guarantor: GuarantorPayload? = null,
    val isOnline: Boolean = false,
    val showDialog: Boolean = false,
) : Parcelable {
    sealed interface DialogState : Parcelable {

        @Parcelize
        data object Loading : DialogState

        @Parcelize
        data class ShowToast(val message: String) : DialogState
    }
}

sealed interface GuarantorDetailEvent {
    data object NavigateBack : GuarantorDetailEvent
    data class ShowToast(val message: String) : GuarantorDetailEvent
    data class UpdateGuarantor(val index: Int, val loanId: Long) : GuarantorDetailEvent
}

sealed interface GuarantorDetailAction {
    data object NavigateBack : GuarantorDetailAction
    data object DeleteGuarantor : GuarantorDetailAction
    data object UpdateGuarantor : GuarantorDetailAction
    data object UpdateMenuDialogValue : GuarantorDetailAction
    data object DismissDialog : GuarantorDetailAction
}
