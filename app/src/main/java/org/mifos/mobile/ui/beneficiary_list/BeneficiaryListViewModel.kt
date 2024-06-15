package org.mifos.mobile.ui.beneficiary_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.mifos.mobile.R
import org.mifos.mobile.models.beneficiary.Beneficiary
import org.mifos.mobile.repositories.BeneficiaryRepository
import javax.inject.Inject



@HiltViewModel
class BeneficiaryListViewModel @Inject constructor(private val beneficiaryRepositoryImp: BeneficiaryRepository) :
    ViewModel() {

    private val _beneficiaryListUiState =
        MutableStateFlow<BeneficiaryListUiState>(BeneficiaryListUiState.Initial)
    val beneficiaryListUiState: StateFlow<BeneficiaryListUiState> get() = _beneficiaryListUiState

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> get() = _isRefreshing.asStateFlow()

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.emit(true)
            loadBeneficiaries()
            _isRefreshing.emit(false)
        }
    }

    fun loadBeneficiaries() {
        viewModelScope.launch {
            _beneficiaryListUiState.value = BeneficiaryListUiState.Loading
            beneficiaryRepositoryImp.beneficiaryList().catch {
                _beneficiaryListUiState.value = BeneficiaryListUiState.ShowError(R.string.beneficiaries)
            }.collect { beneficiaryList->
                if(beneficiaryList.isEmpty()){
                    _beneficiaryListUiState.value = BeneficiaryListUiState.EmptyBeneficiaryList
                }
                else{
                    _beneficiaryListUiState.value = BeneficiaryListUiState.ShowBeneficiaryList(beneficiaryList)
                }
            }
        }
    }
}


sealed class BeneficiaryListUiState{
    object Initial : BeneficiaryListUiState()
    object Loading : BeneficiaryListUiState()
    object EmptyBeneficiaryList : BeneficiaryListUiState()
    data class ShowError(val message: Int) : BeneficiaryListUiState()
    data class ShowBeneficiaryList(val beneficiaries: List<Beneficiary>) : BeneficiaryListUiState()

}

