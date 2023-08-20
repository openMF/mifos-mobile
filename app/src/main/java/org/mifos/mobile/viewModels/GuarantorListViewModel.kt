package org.mifos.mobile.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.mifos.mobile.repositories.GuarantorRepository
import org.mifos.mobile.utils.GuarantorUiState
import javax.inject.Inject

@HiltViewModel
class GuarantorListViewModel @Inject constructor(private val guarantorRepositoryImp: GuarantorRepository) :
    ViewModel() {

    private val _guarantorUiState = MutableStateFlow<GuarantorUiState>(GuarantorUiState.Loading)
    val guarantorUiState: StateFlow<GuarantorUiState> = _guarantorUiState

    fun getGuarantorList(loanId: Long) {
        viewModelScope.launch {
            _guarantorUiState.value = GuarantorUiState.Loading
            guarantorRepositoryImp.getGuarantorList(loanId)
            ?.catch { e ->
                _guarantorUiState.value = GuarantorUiState.ShowError(e.message)
            }?.collect {
                _guarantorUiState.value =
                    GuarantorUiState.ShowGuarantorListSuccessfully(it)
            }
        }
    }
}