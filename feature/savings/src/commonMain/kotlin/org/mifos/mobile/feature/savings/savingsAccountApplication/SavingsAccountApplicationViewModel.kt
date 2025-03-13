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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import mifos_mobile.feature.savings.generated.resources.Res
import mifos_mobile.feature.savings.generated.resources.select_product_id
import org.jetbrains.compose.resources.StringResource
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.common.FileUtils.Companion.logger
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

    init {
        loadSavingsAccountApplicationTemplate()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val savingsWithAssociations: StateFlow<SavingsWithAssociations?> = savingsId
        .flatMapLatest { id ->
            savingsAccountRepositoryImp.getSavingsWithAssociations(id, Constants.TRANSACTIONS)
                .map { dataState ->
                    when (dataState) {
                        is DataState.Success -> dataState.data
                        else -> null
                    }
                }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null,
        )

    private fun loadSavingsAccountApplicationTemplate() {
        viewModelScope.launch {
            logger.d("_savingsAccountApplicationUiState: $_savingsAccountApplicationUiState")
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
                    if (response.data != null) {
                        _savingsAccountApplicationUiState.value =
                            SavingsAccountApplicationUiState.ShowUserInterface(
                                response.data!!,
                                savingsAccountState,
                            )
                    } else {
                        _savingsAccountApplicationUiState.value =
                            SavingsAccountApplicationUiState.Error("Failed to load template. Data is null.")
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
                DataState.Loading -> TODO()
                is DataState.Success -> {
                    _savingsAccountApplicationUiState.value =
                        SavingsAccountApplicationUiState.Success(savingsAccountState)
                }
            }
        }
    }

    private fun updateSavingsAccount(accountId: Long?, payload: SavingsAccountUpdatePayload?) {
        viewModelScope.launch {
            _savingsAccountApplicationUiState.value = Loading
            val response = savingsAccountRepositoryImp.updateSavingsAccount(accountId, payload)

            when (response) {
                is DataState.Error -> {
                    _savingsAccountApplicationUiState.value =
                        SavingsAccountApplicationUiState.Error(response.message)
                }
                DataState.Loading -> {
                    Loading
                }
                is DataState.Success -> {
                    _savingsAccountApplicationUiState.value =
                        SavingsAccountApplicationUiState.Success(savingsAccountState)
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
    data class Success(val requestType: SavingsAccountState) : SavingsAccountApplicationUiState()
    data class ShowUserInterface(
        val template: SavingsAccountTemplate,
        val requestType: SavingsAccountState,
    ) :
        SavingsAccountApplicationUiState()
}
