/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.savings.savingsAccountApplication

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import mifos_mobile.feature.savings.generated.resources.Res
import mifos_mobile.feature.savings.generated.resources.new_saving_account_created_successfully
import mifos_mobile.feature.savings.generated.resources.saving_account_updated_successfully
import mifos_mobile.feature.savings.generated.resources.select_product_id
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.data.repository.SavingsAccountRepository
import org.mifos.mobile.core.datastore.UserPreferencesDataSource
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsAccountApplicationPayload
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsAccountUpdatePayload
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsWithAssociations
import org.mifos.mobile.core.model.entity.templates.savings.SavingsAccountTemplate
import org.mifos.mobile.core.model.enums.SavingsAccountState
import org.mifos.mobile.feature.savings.savingsAccountApplication.SavingsAccountApplicationUiState.Loading

internal class SavingsAccountApplicationViewModel(
    private val savingsAccountRepositoryImp: SavingsAccountRepository,
    private val preferencesHelper: UserPreferencesDataSource,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val clientId get() = preferencesHelper.clientId

    private val savingsId =
        savedStateHandle.getStateFlow(key = Constants.SAVINGS_ID, initialValue = -1L)

    private val savingsAccountState = savedStateHandle.getStateFlow(
        key = Constants.SAVINGS_ACCOUNT_STATE,
        initialValue = SavingsAccountState.CREATE.name,
    ).value.let { SavingsAccountState.valueOf(it) }

    private val _savingsAccountApplicationUiState =
        MutableStateFlow<SavingsAccountApplicationUiState>(Loading)
    val savingsAccountApplicationUiState = _savingsAccountApplicationUiState.asStateFlow()

    private val _savingsWithAssociations = MutableStateFlow<SavingsWithAssociations?>(null)
    private val savingsWithAssociations = _savingsWithAssociations.asStateFlow()

    init {
        observeSavingsWithAssociations()
        loadSavingsAccountApplicationTemplate()
    }

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeSavingsWithAssociations() {
        viewModelScope.launch {
            savingsId
                .flatMapLatest { id ->
                    savingsAccountRepositoryImp.getSavingsWithAssociations(id, Constants.TRANSACTIONS)
                }
                .collect { dataState ->
                    when (dataState) {
                        is DataState.Success -> {
                            _savingsWithAssociations.value = dataState.data
                        }
                        is DataState.Loading -> {
                            _savingsAccountApplicationUiState.value = Loading
                        }
                        is DataState.Error -> {
                            _savingsAccountApplicationUiState.value =
                                SavingsAccountApplicationUiState.Error(dataState.exception.message)
                        }
                    }
                }
        }
    }

    private fun loadSavingsAccountApplicationTemplate() {
        viewModelScope.launch {
            _savingsAccountApplicationUiState.value = Loading
            val clientIdValue = clientId.firstOrNull()
            if (clientIdValue == null) {
                _savingsAccountApplicationUiState.value =
                    SavingsAccountApplicationUiState.Error("Client ID is null")
                return@launch
            }

            savingsAccountRepositoryImp.getSavingAccountApplicationTemplate(clientIdValue)
                .catch { e ->
                    _savingsAccountApplicationUiState.value =
                        SavingsAccountApplicationUiState.Error(
                            e.message ?: "Unknown error",
                        )
                }
                .collect { response ->
                    when (response) {
                        is DataState.Error -> {
                            _savingsAccountApplicationUiState.value =
                                SavingsAccountApplicationUiState.Error("Failed to load template. Data is null.")
                        }
                        DataState.Loading -> {
                            Loading
                        }
                        is DataState.Success -> {
                            _savingsAccountApplicationUiState.value =
                                SavingsAccountApplicationUiState.ShowUserInterface(
                                    response.data,
                                    savingsAccountState,
                                )
                        }
                    }
                }
        }
    }

    private fun submitSavingsAccountApplication(payload: SavingsAccountApplicationPayload?) {
        viewModelScope.launch {
            _savingsAccountApplicationUiState.value = Loading
            val response = savingsAccountRepositoryImp.submitSavingAccountApplication(payload)
            when (response) {
                is DataState.Error -> {
                    _savingsAccountApplicationUiState.value =
                        SavingsAccountApplicationUiState.Error(response.message)
                }
                DataState.Loading -> Loading
                is DataState.Success -> {
                    val messageRes = when (savingsAccountState) {
                        SavingsAccountState.CREATE -> Res.string.new_saving_account_created_successfully
                        else -> Res.string.saving_account_updated_successfully
                    }

                    _eventFlow.emit(UiEvent.ShowSnackbar(getString(messageRes)))
                    delay(1500)
                    _eventFlow.emit(UiEvent.NavigateBack)
                }
            }
        }
    }

    private fun updateSavingsAccount(accountId: Long?, payload: SavingsAccountUpdatePayload?) {
        viewModelScope.launch {
            if (accountId == -1L) {
                _eventFlow.emit(UiEvent.ShowSnackbar(getString(Res.string.select_product_id)))
                return@launch
            }
            val response = savingsAccountRepositoryImp.updateSavingsAccount(accountId, payload)

            when (response) {
                is DataState.Error -> {
                    _eventFlow.emit(UiEvent.ShowSnackbar(response.exception.message ?: "Unknown error"))
                }
                DataState.Loading -> {
                    Loading
                }
                is DataState.Success -> {
                    val messageRes = when (savingsAccountState) {
                        SavingsAccountState.CREATE -> Res.string.new_saving_account_created_successfully
                        else -> Res.string.saving_account_updated_successfully
                    }

                    _eventFlow.emit(UiEvent.ShowSnackbar(getString(messageRes)))
                    delay(1500)
                    _eventFlow.emit(UiEvent.NavigateBack)
                }
            }
        }
    }

    fun onRetry() {
        loadSavingsAccountApplicationTemplate()
    }

    fun onSubmit(productId: Int, clientId: Int, showToast: (StringResource) -> Unit) {
        if (savingsAccountState == SavingsAccountState.CREATE) {
            submitSavingsAccount(productId = productId, clientId = clientId, showToast)
        } else {
            updateSavingAccount(productId = productId, clientId = clientId)
        }
    }

    private fun updateSavingAccount(productId: Int, clientId: Int) {
        val payload = SavingsAccountUpdatePayload(
            clientId = clientId.toLong(),
            productId = productId.toLong(),
        )
        updateSavingsAccount(savingsWithAssociations.value?.id, payload)
    }

    private fun submitSavingsAccount(productId: Int, clientId: Int, showToast: (StringResource) -> Unit) {
        if (productId == -1) {
            showToast(Res.string.select_product_id)
            return
        }
        val payload = SavingsAccountApplicationPayload(
            clientId = clientId,
            productId = if (productId != -1) productId else null,
            submittedOnDate = DateHelper.getSpecificFormat(DateHelper.FULL_MONTH, DateHelper.formattedFullDate),
        )
        submitSavingsAccountApplication(payload)
    }
}

internal sealed class SavingsAccountApplicationUiState {
    data object Loading : SavingsAccountApplicationUiState()
    data class Error(val errorMessage: String?) : SavingsAccountApplicationUiState()
    data class ShowUserInterface(
        val template: SavingsAccountTemplate,
        val requestType: SavingsAccountState,
    ) :
        SavingsAccountApplicationUiState()
}

internal sealed class UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent()
    data object NavigateBack : UiEvent()
}
