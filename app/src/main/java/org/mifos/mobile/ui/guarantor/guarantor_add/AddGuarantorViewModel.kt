package org.mifos.mobile.ui.guarantor.guarantor_add

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.mifos.mobile.R
import org.mifos.mobile.models.guarantor.GuarantorApplicationPayload
import org.mifos.mobile.models.guarantor.GuarantorPayload
import org.mifos.mobile.models.guarantor.GuarantorTemplatePayload
import org.mifos.mobile.repositories.GuarantorRepository
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.Result
import org.mifos.mobile.utils.asResult
import javax.inject.Inject

/**
 * Currently we do not get back any response from the guarantorApi, hence we are using FakeRemoteDataSource
 * to show a list of guarantors. You can look at the implementation of [GuarantorRepository] for better understanding
 */

@HiltViewModel
class AddGuarantorViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val guarantorRepositoryImp: GuarantorRepository
) : ViewModel() {

    val index = savedStateHandle.getStateFlow(key = Constants.INDEX, initialValue = -1)
    private val loanId = savedStateHandle.getStateFlow<Long>(key = Constants.LOAN_ID, initialValue = -1)

    private val _guarantorState = MutableStateFlow<GuarantorAddUiState>(GuarantorAddUiState.Loading)
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
        initialValue = GuarantorAddUiState.Loading
    )

    val guarantorUiState = merge(guarantorTemplate, _guarantorState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = GuarantorAddUiState.Loading
        )

    fun createGuarantor(payload: GuarantorApplicationPayload) {
        viewModelScope.launch {
            _guarantorState.value = GuarantorAddUiState.Loading
            guarantorRepositoryImp.createGuarantor(payload = payload, loanId = loanId.value)?.catch { e ->
                _guarantorState.value = GuarantorAddUiState.Error(e.message)
            }?.collect {
                _guarantorState.value = GuarantorAddUiState.Success(R.string.guarantor_created_successfully)
            }
        }
    }

    private fun fetchGuarantorItem() {
        if(index.value > -1) {
            guarantorItem = loanId
                .flatMapLatest { loanId ->
                    guarantorRepositoryImp.getGuarantorList(loanId = loanId)
                }.asResult().map { result ->
                    when (result) {
                        is Result.Success -> result.data?.filter { it?.status == true }?.get(index = index.value)
                        else -> null
                    }
                }.stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5000),
                    initialValue = null
                )
        }
    }

    fun updateGuarantor(payload: GuarantorApplicationPayload){
        viewModelScope.launch {
            _guarantorState.value = GuarantorAddUiState.Loading
            guarantorRepositoryImp.updateGuarantor(payload, loanId.value, guarantorItem.value?.id)?.catch { e ->
                _guarantorState.value = GuarantorAddUiState.Error(e.message)
            }?.collect {
                _guarantorState.value = GuarantorAddUiState.Success(R.string.guarantor_updated_successfully)
            }
        }
    }
}


sealed class GuarantorAddUiState {
    data object Loading : GuarantorAddUiState()
    data class Error(val message: String?) : GuarantorAddUiState()
    data class Template(val guarantorTemplatePayload: GuarantorTemplatePayload?) : GuarantorAddUiState()
    data class Success(val messageResId: Int) : GuarantorAddUiState()
}