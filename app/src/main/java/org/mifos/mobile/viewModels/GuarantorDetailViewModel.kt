package org.mifos.mobile.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.mifos.mobile.repositories.GuarantorRepository
import org.mifos.mobile.utils.GuarantorUiState
import javax.inject.Inject

@HiltViewModel
class GuarantorDetailViewModel @Inject constructor(private val guarantorRepositoryImp: GuarantorRepository) :
    ViewModel() {

    private val _guarantorUiState = MutableLiveData<GuarantorUiState>()
    val guarantorUiState: LiveData<GuarantorUiState> get() = _guarantorUiState

    fun deleteGuarantor(loanId: Long?, guarantorId: Long?) {
        viewModelScope.launch {
            _guarantorUiState.value = GuarantorUiState.Loading
            val response = guarantorRepositoryImp.deleteGuarantor(loanId, guarantorId)
            try {
                if (response?.isSuccessful == true) {
                    _guarantorUiState.value =
                        GuarantorUiState.GuarantorDeletedSuccessfully(response.body()?.string())
                } else {
                    _guarantorUiState.value =
                        GuarantorUiState.ShowError(response?.errorBody()?.string())
                }
            } catch (e: Throwable) {
                _guarantorUiState.value = GuarantorUiState.ShowError(e.message)
            }
        }
    }
}