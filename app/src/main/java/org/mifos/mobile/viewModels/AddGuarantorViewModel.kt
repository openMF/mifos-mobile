package org.mifos.mobile.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.mifos.mobile.models.guarantor.GuarantorApplicationPayload
import org.mifos.mobile.repositories.GuarantorRepository
import org.mifos.mobile.ui.enums.GuarantorState
import org.mifos.mobile.utils.GuarantorUiState
import javax.inject.Inject

@HiltViewModel
class AddGuarantorViewModel @Inject constructor(private val guarantorRepositoryImp: GuarantorRepository) :
    ViewModel() {

    private val _guarantorUiState = MutableStateFlow<GuarantorUiState>(GuarantorUiState.Loading)
    val guarantorUiState: StateFlow<GuarantorUiState> = _guarantorUiState

    fun getGuarantorTemplate(state: GuarantorState?, loanId: Long?) {
        viewModelScope.launch {
            _guarantorUiState.value = GuarantorUiState.Loading
            guarantorRepositoryImp.getGuarantorTemplate(loanId)?.catch { e ->
                _guarantorUiState.value = GuarantorUiState.ShowError(e.message)
            }?.collect {
                if (state === GuarantorState.CREATE) {
                    _guarantorUiState.value = GuarantorUiState.ShowGuarantorApplication(it)
                } else if (state === GuarantorState.UPDATE) {
                    _guarantorUiState.value = GuarantorUiState.ShowGuarantorUpdation(it)
                }
            }
        }
    }

    fun createGuarantor(loanId: Long?, payload: GuarantorApplicationPayload?) {
        viewModelScope.launch {
            _guarantorUiState.value = GuarantorUiState.Loading
            guarantorRepositoryImp.createGuarantor(loanId, payload)?.catch { e ->
                _guarantorUiState.value = GuarantorUiState.ShowError(e.message)
            }?.collect {
                _guarantorUiState.value =
                    GuarantorUiState.SubmittedSuccessfully(it?.string(), payload)
            }
        }
    }

    fun updateGuarantor(
        payload: GuarantorApplicationPayload?,
        loanId: Long?,
        guarantorId: Long?,
    ) {
        viewModelScope.launch {
            _guarantorUiState.value = GuarantorUiState.Loading
            guarantorRepositoryImp.updateGuarantor(payload, loanId, guarantorId)?.catch { e ->
                _guarantorUiState.value = GuarantorUiState.ShowError(e.message)
            }?.collect {
                _guarantorUiState.value =
                    GuarantorUiState.GuarantorUpdatedSuccessfully(it?.string())
            }
        }
    }
}