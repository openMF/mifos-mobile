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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import mifos_mobile.feature.guarantor.generated.resources.Res
import mifos_mobile.feature.guarantor.generated.resources.guarantor_created_successfully
import mifos_mobile.feature.guarantor.generated.resources.guarantor_updated_successfully
import org.jetbrains.compose.resources.StringResource
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.GuarantorRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize
import org.mifos.mobile.core.model.entity.guarantor.GuarantorApplicationPayload
import org.mifos.mobile.core.model.entity.guarantor.GuarantorPayload
import org.mifos.mobile.core.model.entity.guarantor.GuarantorTemplatePayload
import org.mifos.mobile.core.ui.utils.BaseViewModel

/**
 * Currently we do not get back any response from the guarantorApi, hence we are using FakeRemoteDataSource
 * to show a list of guarantors. You can look at the implementation of [GuarantorRepository] for better understanding
 */

internal class AddGuarantorViewModel(
    private val guarantorRepositoryImp: GuarantorRepository,
    networkMonitor: NetworkMonitor,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<> {

    private val index = savedStateHandle.getStateFlow(key = Constants.INDEX, initialValue = -1)
    private val loanId = savedStateHandle.getStateFlow<Long>(
        key = Constants.LOAN_ID,
        initialValue = -1,
    )

    private val mGuarantorState = MutableStateFlow<GuarantorAddUiState>(GuarantorAddUiState.Loading)
    var guarantorItem: StateFlow<GuarantorPayload?> = MutableStateFlow(null)

    init {
        fetchGuarantorItem()
    }

    private val guarantorTemplate = loanId.flatMapLatest { id ->
        guarantorRepositoryImp.getGuarantorTemplate(id)
    }
        .asResult()
        .map { result ->
            when (result) {
                is Result.Success -> GuarantorAddUiState.Template(result.data)
                is Result.Loading -> GuarantorAddUiState.Loading
                is Result.Error -> GuarantorAddUiState.Error(result.exception.message)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = GuarantorAddUiState.Loading,
        )

    val guarantorUiState = merge(guarantorTemplate, mGuarantorState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = GuarantorAddUiState.Loading,
        )

    fun createGuarantor(payload: GuarantorApplicationPayload) {
        viewModelScope.launch {
            when (val result = guarantorRepositoryImp.createGuarantor(loanId.value, payload)) {
                is DataState.Error -> mGuarantorState.value =
                    GuarantorAddUiState.Error(result.message)

                DataState.Loading -> mGuarantorState.value = GuarantorAddUiState.Loading

                is DataState.Success -> mGuarantorState.value =
                    GuarantorAddUiState.Success(Res.string.guarantor_created_successfully)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun fetchGuarantorItem() {
        if (index.value > -1) {
            guarantorItem = loanId
                .flatMapLatest { loanId ->
                    guarantorRepositoryImp.getGuarantorList(loanId = loanId)
                }.asResult().map { result ->
                    when (result) {
                        is Result.Success -> result.data?.filter { it?.status == true }
                            ?.get(index = index.value)

                        else -> null
                    }
                }.stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5000),
                    initialValue = null,
                )
        }
    }

    fun updateGuarantor(payload: GuarantorApplicationPayload) {
        viewModelScope.launch {

            when (val result = guarantorRepositoryImp.updateGuarantor(
                payload,
                loanId.value,
                guarantorItem.value?.id,
            )) {
                is DataState.Error -> mGuarantorState.value =
                    GuarantorAddUiState.Error(result.exception.message)

                is DataState.Success -> mGuarantorState.value =
                    GuarantorAddUiState.Success(Res.string.guarantor_updated_successfully)

                DataState.Loading -> mGuarantorState.value = GuarantorAddUiState.Loading
            }
        }
    }

}

@Parcelize
data class AddGuarrantorState (
    val index : Int = -1,
    val loanId : Long? = -1L,
    val dialogState : DialogState?,
    val isOnline : Boolean = false,
) : Parcelable {

    sealed interface DialogState : Parcelable {

        @Parcelize
        data object Loading : DialogState

        @Parcelize
        data class Error(val message : String) : DialogState
    }
}
internal sealed class GuarantorAddUiState {
    data object Loading : GuarantorAddUiState()
    data class Error(val message: String?) : GuarantorAddUiState()
    data class Template(val guarantorTemplatePayload: GuarantorTemplatePayload?) :
        GuarantorAddUiState()

    data class Success(val messageStringRes: StringResource) : GuarantorAddUiState()
}

sealed interface AddGuarantorEvent {
    data object NavigateBack : AddGuarantorEvent
    data class Submit(val payload: GuarantorApplicationPayload) : AddGuarantorEvent
    data class ShowToast(val message: String?) : AddGuarantorEvent
}

sealed interface AddGuarantorAction {
    data object NavigateBack : AddGuarantorAction
    data object OnSubmitClick : AddGuarantorAction
}