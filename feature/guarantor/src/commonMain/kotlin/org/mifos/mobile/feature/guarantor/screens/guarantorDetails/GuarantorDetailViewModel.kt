/*
 * Copyright 2026 Mifos Initiative
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

/**
 * ViewModel for managing state and business logic of "Guarantor Detail" screen.
 *
 * This ViewModel handles displaying guarantor details and managing operations like:
 * - Fetching specific guarantor data from the repository
 * - Deleting guarantors with proper confirmation flow
 * - Managing network connectivity status
 * - Handling UI states and error conditions
 * - Providing navigation to update guarantor screen
 *
 * @property guarantorRepositoryImp Repository for guarantor-related API operations.
 * @property savedStateHandle Handle to saved state for retrieving navigation arguments.
 * @property networkMonitor Utility to monitor network connectivity status.
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

    /**
     * Helper function to update [GuarantorDetailState].
     *
     * @param update A lambda function that takes the current state and returns an updated state.
     */
    private fun updateState(update: (GuarantorDetailState) -> GuarantorDetailState) {
        mutableStateFlow.update(update)
    }

    /**
     * Fetches specific guarantor data from the repository.
     * Updates UI state based on the result (loading, error, or success).
     * Filters for active guarantors and selects the one at the specified index.
     */
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

    /**
     * Deletes a guarantor using the repository.
     * Updates UI state based on the API response and sends success events.
     *
     * @param guarantorId The ID of the guarantor to be deleted.
     */
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

    /**
     * Handles incoming actions from the UI or internal events within the ViewModel.
     *
     * @param action The [GuarantorDetailAction] to be processed.
     */
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

/**
 * Represents the UI state for the "Guarantor Detail" screen.
 *
 * @property loanId The ID of the loan for which guarantors are being managed.
 * @property index The index of the guarantor being viewed (null for new guarantors).
 * @property dialogState The current dialog state (loading, error, or null).
 * @property guarantor The guarantor data being displayed.
 * @property isOnline The network connectivity status.
 * @property showDialog Boolean indicating whether the delete confirmation dialog should be shown.
 */
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
    /**
     * Sealed interface representing possible dialog states for the "Guarantor Detail" screen.
     */
    sealed interface DialogState : Parcelable {

        @Parcelize
        data object Loading : DialogState

        @Parcelize
        data class ShowToast(val message: String) : DialogState
    }
}

/**
 * Sealed interface representing events that can be emitted from the "Guarantor Detail" ViewModel.
 * These events are typically used to trigger navigation or show one-time messages.
 */
sealed interface GuarantorDetailEvent {
    data object NavigateBack : GuarantorDetailEvent
    data class ShowToast(val message: String) : GuarantorDetailEvent
    data class UpdateGuarantor(val index: Int, val loanId: Long) : GuarantorDetailEvent
}

/**
 * Sealed interface representing actions that can be dispatched to the "Guarantor Detail" ViewModel.
 * These actions can originate from the UI or be used internally by the ViewModel.
 */
sealed interface GuarantorDetailAction {
    data object NavigateBack : GuarantorDetailAction
    data object DeleteGuarantor : GuarantorDetailAction
    data object UpdateGuarantor : GuarantorDetailAction
    data object UpdateMenuDialogValue : GuarantorDetailAction
    data object DismissDialog : GuarantorDetailAction
}
