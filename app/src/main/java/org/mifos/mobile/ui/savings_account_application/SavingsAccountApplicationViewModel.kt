package org.mifos.mobile.ui.savings_account_application

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.mifos.mobile.R
import org.mifos.mobile.api.local.PreferencesHelper
import org.mifos.mobile.models.accounts.savings.SavingsAccountApplicationPayload
import org.mifos.mobile.models.accounts.savings.SavingsAccountUpdatePayload
import org.mifos.mobile.models.accounts.savings.SavingsWithAssociations
import org.mifos.mobile.models.templates.savings.SavingsAccountTemplate
import org.mifos.mobile.repositories.SavingsAccountRepository
import org.mifos.mobile.ui.enums.SavingsAccountState
import org.mifos.mobile.utils.DateHelper
import org.mifos.mobile.utils.getTodayFormatted
import javax.inject.Inject

@HiltViewModel
class SavingsAccountApplicationViewModel @Inject constructor(
    private val savingsAccountRepositoryImp: SavingsAccountRepository,
    private val preferencesHelper: PreferencesHelper
) : ViewModel() {

    val savingsAccountApplicationUiState: State<SavingsAccountApplicationUiState> get() = _savingsAccountApplicationUiState
    private val _savingsAccountApplicationUiState =
        mutableStateOf<SavingsAccountApplicationUiState>(SavingsAccountApplicationUiState.Loading)

    private val savingsAccountState get() = _savingsAccountState
    private var _savingsAccountState: SavingsAccountState = SavingsAccountState.CREATE

    val savingsWithAssociations get() = _savingsWithAssociations
    private var _savingsWithAssociations: SavingsWithAssociations? = null

    private val clientId get() = preferencesHelper.clientId

    fun loadSavingsAccountApplicationTemplate() {
        viewModelScope.launch {
            _savingsAccountApplicationUiState.value = SavingsAccountApplicationUiState.Loading
            savingsAccountRepositoryImp.getSavingAccountApplicationTemplate(clientId).catch { e ->
                _savingsAccountApplicationUiState.value =
                    SavingsAccountApplicationUiState.Error(e.message)
            }.collect {
                _savingsAccountApplicationUiState.value =
                    SavingsAccountApplicationUiState.ShowUserInterface(it, savingsAccountState)
            }
        }
    }

    fun submitSavingsAccountApplication(payload: SavingsAccountApplicationPayload?) {
        viewModelScope.launch {
            _savingsAccountApplicationUiState.value = SavingsAccountApplicationUiState.Loading
            savingsAccountRepositoryImp.submitSavingAccountApplication(payload).catch { e ->
                _savingsAccountApplicationUiState.value =
                    SavingsAccountApplicationUiState.Error(e.message)
            }.collect {
                _savingsAccountApplicationUiState.value =
                    SavingsAccountApplicationUiState.Success(savingsAccountState)
            }
        }
    }

    fun updateSavingsAccount(accountId: Long?, payload: SavingsAccountUpdatePayload?) {
        viewModelScope.launch {
            _savingsAccountApplicationUiState.value = SavingsAccountApplicationUiState.Loading
            savingsAccountRepositoryImp.updateSavingsAccount(accountId, payload).catch { e ->
                _savingsAccountApplicationUiState.value =
                    SavingsAccountApplicationUiState.Error(e.message)
            }.collect {
                _savingsAccountApplicationUiState.value =
                    SavingsAccountApplicationUiState.Success(savingsAccountState)
            }
        }
    }

    fun setSavingsAccountState(savingsAccountState: SavingsAccountState) {
        _savingsAccountState = savingsAccountState
    }

    fun setSavingsWithAssociations(savingsAssociations: SavingsWithAssociations?) {
        _savingsWithAssociations = savingsAssociations
    }

    fun onRetry() {
        loadSavingsAccountApplicationTemplate()
    }

    fun onSubmit(productId: Int, clientId: Int, showToast: (Int) -> Unit) {
        if (savingsAccountState == SavingsAccountState.CREATE) {
            submitSavingsAccount(productId = productId, clientId = clientId, showToast = showToast)
        } else {
            updateSavingAccount(productId = productId, clientId = clientId)
        }
    }

    private fun updateSavingAccount(productId: Int, clientId: Int) {
        val payload = SavingsAccountUpdatePayload()
        payload.clientId = clientId.toLong()
        payload.productId = productId.toLong()
        updateSavingsAccount(savingsWithAssociations?.id, payload)
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
        payload.submittedOnDate = DateHelper.getSpecificFormat(DateHelper.FORMAT_dd_MMMM_yyyy, getTodayFormatted(),)
        submitSavingsAccountApplication(payload)
    }
}

sealed class SavingsAccountApplicationUiState {
    data object Loading : SavingsAccountApplicationUiState()
    data class Error(val errorMessage: String?) : SavingsAccountApplicationUiState()
    data class  Success(val requestType: SavingsAccountState) : SavingsAccountApplicationUiState()
    data class ShowUserInterface(val template: SavingsAccountTemplate, val requestType: SavingsAccountState) :
        SavingsAccountApplicationUiState()
}

