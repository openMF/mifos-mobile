/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.guarantor.screens.guarantorAdd

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mifos_mobile.feature.guarantor.generated.resources.Res
import mifos_mobile.feature.guarantor.generated.resources.guarantor_created_successfully
import mifos_mobile.feature.guarantor.generated.resources.guarantor_updated_successfully
import mifos_mobile.feature.guarantor.generated.resources.internet_not_connected
import org.jetbrains.compose.resources.getString
import org.mifos.mobile.core.common.Constants.INDEX
import org.mifos.mobile.core.common.Constants.LOAN_ID
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.GuarantorRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.model.IgnoredOnParcel
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize
import org.mifos.mobile.core.model.entity.guarantor.GuarantorApplicationPayload
import org.mifos.mobile.core.model.entity.guarantor.GuarantorPayload
import org.mifos.mobile.core.model.entity.guarantor.GuarantorTemplatePayload
import org.mifos.mobile.core.model.entity.guarantor.GuarantorType
import org.mifos.mobile.core.ui.utils.BaseViewModel

/**
 * Currently we do not get back any response from the guarantorApi, hence we are using FakeRemoteDataSource
 * to show a list of guarantors. You can look at the implementation of [GuarantorRepository] for better understanding
 */

/**
 * ViewModel for managing the state and business logic of the "Add Guarantor" screen.
 *
 * This ViewModel handles the creation and editing of guarantors for loans, including:
 * - Fetching guarantor templates and existing guarantor data
 * - Validating form input fields
 * - Creating new guarantors and updating existing ones
 * - Managing network connectivity status
 * - Handling UI states and error conditions
 *
 * @property guarantorRepositoryImp Repository for guarantor-related API operations.
 * @property networkMonitor Utility to monitor network connectivity status.
 * @property savedStateHandle Handle to saved state for retrieving navigation arguments.
 */
internal class AddGuarantorViewModel(
    private val guarantorRepositoryImp: GuarantorRepository,
    networkMonitor: NetworkMonitor,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<AddGuarantorState, AddGuarantorEvent, AddGuarantorAction>(
    initialState = AddGuarantorState(
        index = savedStateHandle.getStateFlow<Int>(key = INDEX, initialValue = -1).value,
        loanId = savedStateHandle.getStateFlow<Long?>(key = LOAN_ID, initialValue = null).value,
        dialogState = null,
    ),
) {

    init {
        viewModelScope.launch {
            networkMonitor.isOnline.collect { isConnected ->
                updateState { it.copy(isOnline = isConnected) }
                if (!isConnected) {
                    sendEvent(AddGuarantorEvent.ShowToast(getString(Res.string.internet_not_connected)))
                }
            }
        }
        viewModelScope.launch {
            coroutineScope {
                launch { getGuarantorItem() }
                launch { getGuarantorTemplate() }
            }
        }
    }

    /**
     * Helper function to update the [AddGuarantorState].
     *
     * @param update A lambda function that takes the current state and returns an updated state.
     */
    private fun updateState(update: (AddGuarantorState) -> AddGuarantorState) {
        mutableStateFlow.update(update)
    }

    /**
     * Fetches the guarantor template from the repository.
     * Updates the UI state based on the result (loading, error, or success).
     */
    private fun getGuarantorTemplate() {
        viewModelScope.launch {
            guarantorRepositoryImp.getGuarantorTemplate(state.loanId).collect { result ->

                updateState { currentState ->
                    when (result) {
                        is DataState.Error -> {
                            currentState.copy(
                                dialogState = AddGuarantorState.DialogState.Error(result.message),
                            )
                        }

                        is DataState.Loading -> {
                            currentState.copy(dialogState = AddGuarantorState.DialogState.Loading)
                        }

                        is DataState.Success -> currentState.copy(
                            dialogState = null,
                            templatePayload = result.data,
                        )
                    }
                }
            }
        }
    }

    /**
     * Validates the form fields for guarantor creation/editing.
     * Checks that first name, last name, and guarantor type are not empty.
     *
     * @return True if all fields are valid, false otherwise with appropriate error state.
     */
    private fun validateFields(): Boolean {
        return when {
            state.firstName.isEmpty() -> {
                updateState {
                    it.copy(
                        dialogState = AddGuarantorState.DialogState.Error
                            ("First name can not be empty"),
                    )
                }
                false
            }

            state.lastName.isEmpty() -> {
                updateState {
                    it.copy(
                        dialogState = AddGuarantorState.DialogState.Error
                            ("Last name can't be empty"),
                    )
                }
                false
            }

            state.guarantorType.value.isNullOrEmpty() -> {
                updateState {
                    it.copy(
                        dialogState = AddGuarantorState.DialogState.Error(
                            "Guarantor type can not be empty or null",
                        ),
                    )
                }
                false
            }

            else -> true
        }
    }

    /**
     * Creates a new guarantor using the repository.
     * Updates UI state based on the API response and sends success events.
     *
     * @param payload The guarantor data to be created.
     */
    private fun createGuarantor(payload: GuarantorApplicationPayload) {
        viewModelScope.launch {
            when (val result = guarantorRepositoryImp.createGuarantor(state.loanId, payload)) {
                is DataState.Error -> {
                    updateState { it.copy(dialogState = AddGuarantorState.DialogState.Error(result.message)) }
                }

                is DataState.Loading -> {
                    updateState {
                        it.copy(dialogState = AddGuarantorState.DialogState.Loading)
                    }
                }

                is DataState.Success -> {
                    sendEvent(AddGuarantorEvent.Success(getString(Res.string.guarantor_created_successfully)))
                }
            }
        }
    }

    /**
     * Fetches existing guarantor data for editing.
     * Only called when the index is >= 0 (edit mode).
     */
    private fun getGuarantorItem() {
        viewModelScope.launch {
            if (state.index >= 0) {
                state.loanId?.let { loanId ->
                    guarantorRepositoryImp.getGuarantorList(loanId = loanId).collect { result ->

                        updateState { currentState ->
                            when (result) {
                                is DataState.Error -> {
                                    currentState.copy(
                                        dialogState = AddGuarantorState.DialogState.Error(
                                            result.message,
                                        ),
                                    )
                                }

                                is DataState.Loading -> {
                                    currentState.copy(dialogState = AddGuarantorState.DialogState.Loading)
                                }

                                is DataState.Success -> {
                                    currentState.copy(
                                        dialogState = null,
                                        guarantorItem = result.data?.filter { it?.status == true }
                                            ?.get(index = state.index),
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Updates an existing guarantor using the repository.
     * Updates UI state based on the API response and sends success events.
     *
     * @param payload The guarantor data to be updated.
     */
    private fun updateGuarantor(payload: GuarantorApplicationPayload) {
        viewModelScope.launch {
            when (
                val result = guarantorRepositoryImp.updateGuarantor(
                    payload,
                    state.loanId,
                    state.guarantorItem?.id,
                )
            ) {
                is DataState.Error -> {
                    updateState {
                        it.copy(
                            dialogState = AddGuarantorState.DialogState.Error(
                                result.exception.message ?: result.message,
                            ),
                        )
                    }
                }

                is DataState.Success -> {
                    updateState { it.copy(dialogState = null) }
                    sendEvent(AddGuarantorEvent.Success(getString(Res.string.guarantor_updated_successfully)))
                }

                is DataState.Loading -> {
                    updateState { it.copy(dialogState = AddGuarantorState.DialogState.Loading) }
                }
            }
        }
    }

    /**
     * Handles incoming actions from the UI or internal events within the ViewModel.
     *
     * @param action The [AddGuarantorAction] to be processed.
     */
    override fun handleAction(action: AddGuarantorAction) {
        when (action) {
            is AddGuarantorAction.NavigateBack -> sendEvent(AddGuarantorEvent.NavigateBack)

            is AddGuarantorAction.ValidateFields -> {
                val isValidated = validateFields()
                if (isValidated) {
                    when (action) {
                        null -> action.payload?.let { createGuarantor(it) }
                        else -> action.payload?.let { updateGuarantor(it) }
                    }
                }
            }

            is AddGuarantorAction.DismissDialog -> {
                updateState { it.copy(dialogState = null) }
            }

            is AddGuarantorAction.OnCityChange -> {
                mutableStateFlow.update {
                    it.copy(city = action.city)
                }
            }

            is AddGuarantorAction.OnFirstNameChange -> {
                mutableStateFlow.update {
                    it.copy(firstName = action.firstName)
                }
            }

            is AddGuarantorAction.OnLastnameChange -> {
                mutableStateFlow.update {
                    it.copy(lastName = action.lastname)
                }
            }

            is AddGuarantorAction.SetGuarantortype -> {
                mutableStateFlow.update {
                    it.copy(guarantorType = action.type)
                }
            }
        }
    }
}

/**
 * Represents the UI state for the "Add Guarantor" screen.
 *
 * @property index The index of the guarantor being edited (-1 for new guarantors).
 * @property loanId The ID of the loan for which guarantors are being managed.
 * @property dialogState The current dialog state (loading, error, or null).
 * @property isOnline The network connectivity status.
 * @property firstName The first name input value for the guarantor form.
 * @property lastName The last name input value for the guarantor form.
 * @property city The city input value for the guarantor form.
 * @property guarantorItem The existing guarantor data when editing (null for new guarantors).
 * @property templatePayload The guarantor template payload containing available options.
 * @property guarantorType The selected guarantor type from the template options.
 */
@Parcelize
data class AddGuarantorState(
    val index: Int = -1,
    val loanId: Long? = -1L,
    val dialogState: DialogState?,
    val isOnline: Boolean = false,
    var firstName: String = "",
    var lastName: String = "",
    var city: String = "",
    @IgnoredOnParcel
    val guarantorItem: GuarantorPayload? = null,
    @IgnoredOnParcel
    val templatePayload: GuarantorTemplatePayload? = null,
    @IgnoredOnParcel
    var guarantorType: GuarantorType = GuarantorType(),
) : Parcelable {

    /**
     * Sealed interface representing possible dialog states for the "Add Guarantor" screen.
     */
    sealed interface DialogState : Parcelable {

        @Parcelize
        data object Loading : DialogState

        @Parcelize
        data class Error(val message: String) : DialogState
    }
}

/**
 * Sealed interface representing events that can be emitted from the "Add Guarantor" ViewModel.
 * These events are typically used to trigger navigation or show one-time messages.
 */
sealed interface AddGuarantorEvent {
    data object NavigateBack : AddGuarantorEvent
    data class ShowToast(val message: String) : AddGuarantorEvent
    data class Success(val message: String) : AddGuarantorEvent
}

/**
 * Sealed interface representing actions that can be dispatched to the "Add Guarantor" ViewModel.
 * These actions can originate from the UI or be used internally by the ViewModel.
 */
sealed interface AddGuarantorAction {
    data class OnFirstNameChange(val firstName: String) : AddGuarantorAction
    data class OnLastnameChange(val lastname: String) : AddGuarantorAction
    data class OnCityChange(val city: String) : AddGuarantorAction
    data class SetGuarantortype(val type: GuarantorType) : AddGuarantorAction

    data object NavigateBack : AddGuarantorAction
    data class ValidateFields(val payload: GuarantorApplicationPayload?) : AddGuarantorAction
    data object DismissDialog : AddGuarantorAction
}
