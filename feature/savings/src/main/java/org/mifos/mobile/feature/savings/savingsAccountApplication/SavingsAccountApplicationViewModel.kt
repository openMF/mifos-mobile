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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.utils.DateHelper
import org.mifos.mobile.core.common.utils.getTodayFormatted
import org.mifos.mobile.core.data.repository.SavingsAccountRepository
import org.mifos.mobile.core.datastore.PreferencesHelper
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsAccountApplicationPayload
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsAccountUpdatePayload
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsWithAssociations
import org.mifos.mobile.core.model.entity.templates.savings.SavingsAccountTemplate
import org.mifos.mobile.core.model.enums.SavingsAccountState
import org.mifos.mobile.feature.savings.R
import org.mifos.mobile.feature.savings.savingsAccountApplication.SavingsAccountApplicationUiState.Loading
import javax.inject.Inject

@HiltViewModel
internal class SavingsAccountApplicationViewModel @Inject constructor(
    private val savingsAccountRepositoryImp: SavingsAccountRepository,
    private val preferencesHelper: PreferencesHelper,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val clientId get() = preferencesHelper.clientId

    private val savingsId =
        savedStateHandle.getStateFlow(key = Constants.SAVINGS_ID, initialValue = -1L)
    private val savingsAccountState = savedStateHandle.getStateFlow(
        key = Constants.SAVINGS_ACCOUNT_STATE,
        initialValue = SavingsAccountState.CREATE,
    )

    private val _savingsAccountApplicationUiState =
        MutableStateFlow<SavingsAccountApplicationUiState>(Loading)
    val savingsAccountApplicationUiState = _savingsAccountApplicationUiState.asStateFlow()

    private val savingsWithAssociations: StateFlow<SavingsWithAssociations?> = savingsId
        .flatMapLatest {
            savingsAccountRepositoryImp.getSavingsWithAssociations(
                savingsId.value,
                Constants.TRANSACTIONS,
            )
        }
        .also { loadSavingsAccountApplicationTemplate() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null,
        )

    private fun loadSavingsAccountApplicationTemplate() {
        viewModelScope.launch {
            _savingsAccountApplicationUiState.value = Loading
            savingsAccountRepositoryImp.getSavingAccountApplicationTemplate(clientId).catch { e ->
                _savingsAccountApplicationUiState.value =
                    SavingsAccountApplicationUiState.Error(e.message)
            }.collect {
                _savingsAccountApplicationUiState.value =
                    SavingsAccountApplicationUiState.ShowUserInterface(
                        it,
                        savingsAccountState.value,
                    )
            }
        }
    }

    private fun submitSavingsAccountApplication(payload: SavingsAccountApplicationPayload?) {
        viewModelScope.launch {
            _savingsAccountApplicationUiState.value = Loading
            savingsAccountRepositoryImp.submitSavingAccountApplication(payload).catch { e ->
                _savingsAccountApplicationUiState.value =
                    SavingsAccountApplicationUiState.Error(e.message)
            }.collect {
                _savingsAccountApplicationUiState.value =
                    SavingsAccountApplicationUiState.Success(savingsAccountState.value)
            }
        }
    }

    private fun updateSavingsAccount(accountId: Long?, payload: SavingsAccountUpdatePayload?) {
        viewModelScope.launch {
            _savingsAccountApplicationUiState.value = Loading
            savingsAccountRepositoryImp.updateSavingsAccount(accountId, payload).catch { e ->
                _savingsAccountApplicationUiState.value =
                    SavingsAccountApplicationUiState.Error(e.message)
            }.collect {
                _savingsAccountApplicationUiState.value =
                    SavingsAccountApplicationUiState.Success(savingsAccountState.value)
            }
        }
    }

    fun onRetry() {
        loadSavingsAccountApplicationTemplate()
    }

    fun onSubmit(productId: Int, clientId: Int, showToast: (Int) -> Unit) {
        if (savingsAccountState.value == SavingsAccountState.CREATE) {
            submitSavingsAccount(productId = productId, clientId = clientId, showToast = showToast)
        } else {
            updateSavingAccount(productId = productId, clientId = clientId)
        }
    }

    private fun updateSavingAccount(productId: Int, clientId: Int) {
        val payload = SavingsAccountUpdatePayload()
        payload.clientId = clientId.toLong()
        payload.productId = productId.toLong()
        updateSavingsAccount(savingsWithAssociations.value?.id, payload)
    }

    private fun submitSavingsAccount(productId: Int, clientId: Int, showToast: (Int) -> Unit) {
        val payload = SavingsAccountApplicationPayload()
        payload.clientId = clientId
        if (productId != -1) {
            payload.productId = productId
        } else {
            showToast(R.string.select_product_id)
            return
        }
        payload.submittedOnDate =
            DateHelper.getSpecificFormat(DateHelper.FORMAT_MMMM, getTodayFormatted())
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
