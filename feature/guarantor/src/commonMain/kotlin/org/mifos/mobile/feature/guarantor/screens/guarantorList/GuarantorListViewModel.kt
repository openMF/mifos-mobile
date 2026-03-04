/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.guarantor.screens.guarantorList

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mifos_mobile.feature.guarantor.generated.resources.Res
import mifos_mobile.feature.guarantor.generated.resources.internet_not_connected
import org.jetbrains.compose.resources.getString
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
 * ViewModel for managing state and business logic of "Guarantor List" screen.
 *
 * This ViewModel handles displaying the list of guarantors for a loan, including:
 * - Fetching guarantor list from the repository
 * - Managing network connectivity status
 * - Handling UI states and error conditions
 * - Providing navigation to add and detail guarantor screens
 *
 * @property guarantorRepositoryImp Repository for guarantor-related API operations.
 * @property savedStateHandle Handle to saved state for retrieving navigation arguments.
 * @property networkMonitor Utility to monitor network connectivity status.
 */
internal class GuarantorListViewModel(
    private val guarantorRepositoryImp: GuarantorRepository,
    savedStateHandle: SavedStateHandle,
    networkMonitor: NetworkMonitor,
) : BaseViewModel<GuarantorListState, GuarantorListEvent, GuarantorListAction>(
    initialState = GuarantorListState(
        dialogState = null,
        loanId = savedStateHandle.getStateFlow<Long?>(
            key = LOAN_ID,
            initialValue = null,
        ).value?.toLong(),
    ),
) {

    init {
        viewModelScope.launch {
            networkMonitor.isOnline.collect { isConnected ->
                updateState { it.copy(isOnline = isConnected) }
                if (!isConnected) {
                    sendEvent(GuarantorListEvent.ShowToast(getString(Res.string.internet_not_connected)))
                }
            }
        }
        getGuarantorList()
    }

    /**
     * Helper function to update [GuarantorListState].
     *
     * @param update A lambda function that takes the current state and returns an updated state.
     */
    private fun updateState(update: (GuarantorListState) -> GuarantorListState) {
        mutableStateFlow.update(update)
    }

    /**
     * Fetches the list of guarantors from the repository.
     * Updates UI state based on the result (loading, error, or success).
     * Filters for active guarantors and updates the state accordingly.
     */
    private fun getGuarantorList() {
        viewModelScope.launch {
            state.loanId?.let { loanId ->
                updateState { it.copy(dialogState = GuarantorListState.DialogState.Loading) }

                guarantorRepositoryImp.getGuarantorList(loanId)
                    .collect { result ->
                        updateState { currentState ->
                            when (result) {
                                is DataState.Error -> {
                                    currentState.copy(
                                        dialogState = GuarantorListState.DialogState.ShowToast(
                                            result.message,
                                        ),
                                    )
                                }

                                is DataState.Loading -> {
                                    currentState.copy(dialogState = GuarantorListState.DialogState.Loading)
                                }

                                is DataState.Success -> {
                                    currentState.copy(
                                        dialogState = null,
                                        guarantorList = result.data?.filter { it?.status == true },
                                    )
                                }
                            }
                        }
                    }
            }
        }
    }

    /**
     * Handles incoming actions from the UI or internal events within the ViewModel.
     *
     * @param action The [GuarantorListAction] to be processed.
     */
    override fun handleAction(action: GuarantorListAction) {
        when (action) {
            is GuarantorListAction.OnAddGuarantor -> sendEvent(
                GuarantorListEvent.AddGuarantor(state.loanId!!),
            )

            is GuarantorListAction.OnGuarantorClicked -> sendEvent(
                GuarantorListEvent.GuarantorClicked(action.index, state.loanId!!),
            )

            is GuarantorListAction.OnNavigateBackClick -> sendEvent(GuarantorListEvent.NavigateBack)

            is GuarantorListAction.DismissDialog -> {
                updateState { it.copy(dialogState = null) }
            }
        }
    }
}

/**
 * Represents the UI state for the "Guarantor List" screen.
 *
 * @property loanId The ID of the loan for which guarantors are being managed.
 * @property dialogState The current dialog state (loading, error, or null).
 * @property isOnline The network connectivity status.
 * @property guarantorList The list of guarantors to display.
 */
@Parcelize
data class GuarantorListState(
    val loanId: Long? = -1L,
    val dialogState: DialogState? = null,
    val isOnline: Boolean = false,
    @IgnoredOnParcel
    val guarantorList: List<GuarantorPayload?>? = null,
) : Parcelable {

    /**
     * Sealed interface representing possible dialog states for the "Guarantor List" screen.
     */
    sealed interface DialogState : Parcelable {
        @Parcelize
        data object Loading : DialogState

        @Parcelize
        data class ShowToast(val message: String) : DialogState
    }
}

/**
 * Sealed interface representing events that can be emitted from the "Guarantor List" ViewModel.
 * These events are typically used to trigger navigation or show one-time messages.
 */
sealed interface GuarantorListEvent {
    data object NavigateBack : GuarantorListEvent
    data class ShowToast(val message: String) : GuarantorListEvent
    data class AddGuarantor(val value: Long) : GuarantorListEvent
    data class GuarantorClicked(val index: Int, val loanId: Long) : GuarantorListEvent
}

/**
 * Sealed interface representing actions that can be dispatched to the "Guarantor List" ViewModel.
 * These actions can originate from the UI or be used internally by the ViewModel.
 */
sealed interface GuarantorListAction {
    data object OnNavigateBackClick : GuarantorListAction
    data class OnGuarantorClicked(val index: Int) : GuarantorListAction
    data object OnAddGuarantor : GuarantorListAction
    data object DismissDialog : GuarantorListAction
}
