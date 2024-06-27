package org.mifos.mobile.ui.beneficiary_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.mifos.mobile.core.data.repositories.BeneficiaryRepository
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import javax.inject.Inject

@HiltViewModel
class BeneficiaryListViewModel @Inject constructor(private val beneficiaryRepositoryImp: BeneficiaryRepository) :
    ViewModel() {

    private val _beneficiaryListUiState = MutableStateFlow<BeneficiaryListUiState>(BeneficiaryListUiState.Loading)
    val beneficiaryListUiState: StateFlow<BeneficiaryListUiState> get() = _beneficiaryListUiState

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> get() = _isRefreshing.asStateFlow()

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.emit(true)
            loadBeneficiaries()
        }
    }

    fun loadBeneficiaries() {
        viewModelScope.launch {
            _beneficiaryListUiState.value = BeneficiaryListUiState.Loading
            beneficiaryRepositoryImp.beneficiaryList().catch {
                _beneficiaryListUiState.value = BeneficiaryListUiState.Error(it.message)
            }.collect { beneficiaryList->
                _beneficiaryListUiState.value = BeneficiaryListUiState.Success(beneficiaryList)
                _isRefreshing.emit(false)
            }
        }
    }
}


sealed class BeneficiaryListUiState{
    data object Loading : BeneficiaryListUiState()
    data class Error(val message: String?) : BeneficiaryListUiState()
    data class Success(val beneficiaries: List<Beneficiary>) : BeneficiaryListUiState()
}

