package org.mifos.mobile.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.mifos.mobile.models.guarantor.GuarantorApplicationPayload
import org.mifos.mobile.repositories.GuarantorRepository
import org.mifos.mobile.ui.enums.GuarantorState
import org.mifos.mobile.utils.GuarantorUiState
import javax.inject.Inject

@HiltViewModel
class AddGuarantorViewModel @Inject constructor(private val guarantorRepositoryImp: GuarantorRepository) :
    ViewModel() {

    private val _guarantorUiState = MutableLiveData<GuarantorUiState>()
    val guarantorUiState: LiveData<GuarantorUiState> get() = _guarantorUiState

    fun getGuarantorTemplate(state: GuarantorState?, loanId: Long?) {
        viewModelScope.launch {
            _guarantorUiState.value = GuarantorUiState.Loading
            val response = guarantorRepositoryImp.getGuarantorTemplate(loanId)
            try {
                if (response?.isSuccessful == true) {
                    if (state === GuarantorState.CREATE) {
                        _guarantorUiState.value =
                            GuarantorUiState.ShowGuarantorApplication(response.body())
                    } else if (state === GuarantorState.UPDATE) {
                        _guarantorUiState.value =
                            GuarantorUiState.ShowGuarantorUpdation(response.body())
                    }
                }
            } catch (e: Throwable) {
                _guarantorUiState.value = GuarantorUiState.ShowError(e.message)
            }
        }
    }

    fun createGuarantor(loanId: Long?, payload: GuarantorApplicationPayload?) {
        viewModelScope.launch {
            _guarantorUiState.value = GuarantorUiState.Loading
            val response = guarantorRepositoryImp.createGuarantor(loanId, payload)
            try {
                if (response?.isSuccessful == true) {
                    _guarantorUiState.value =
                        GuarantorUiState.SubmittedSuccessfully(response.body()?.string(), payload)
                }
            } catch (e: Throwable) {
                _guarantorUiState.value = GuarantorUiState.ShowError(e.message)
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
            val response = guarantorRepositoryImp.updateGuarantor(payload, loanId, guarantorId)
            try {
                if (response?.isSuccessful == true) {
                    _guarantorUiState.value =
                        GuarantorUiState.GuarantorUpdatedSuccessfully(response.body()?.string())
                }

            } catch (e: Throwable) {
                _guarantorUiState.value = GuarantorUiState.ShowError(e.message)
            }
        }
    }
}