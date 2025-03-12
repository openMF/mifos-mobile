/*
 * Copyright 2024 Mifos Initiative
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

    private fun updateState(update: (AddGuarantorState) -> AddGuarantorState) {
        mutableStateFlow.update(update)
    }

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

    sealed interface DialogState : Parcelable {

        @Parcelize
        data object Loading : DialogState

        @Parcelize
        data class Error(val message: String) : DialogState
    }
}

sealed interface AddGuarantorEvent {
    data object NavigateBack : AddGuarantorEvent
    data class ShowToast(val message: String) : AddGuarantorEvent
    data class Success(val message: String) : AddGuarantorEvent
}

sealed interface AddGuarantorAction {
    data class OnFirstNameChange(val firstName: String) : AddGuarantorAction
    data class OnLastnameChange(val lastname: String) : AddGuarantorAction
    data class OnCityChange(val city: String) : AddGuarantorAction
    data class SetGuarantortype(val type: GuarantorType) : AddGuarantorAction

    data object NavigateBack : AddGuarantorAction
    data class ValidateFields(val payload: GuarantorApplicationPayload?) : AddGuarantorAction
    data object DismissDialog : AddGuarantorAction
}
