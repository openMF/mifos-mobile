package org.mifos.mobile.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.mifos.mobile.R
import org.mifos.mobile.repositories.BeneficiaryRepository
import org.mifos.mobile.utils.BeneficiaryUiState
import javax.inject.Inject

@HiltViewModel
class BeneficiaryListViewModel @Inject constructor(private val beneficiaryRepositoryImp: BeneficiaryRepository) :
    ViewModel() {

    private val _beneficiaryUiState =
        MutableStateFlow<BeneficiaryUiState>(BeneficiaryUiState.Initial)
    val beneficiaryUiState: StateFlow<BeneficiaryUiState> get() = _beneficiaryUiState

    fun loadBeneficiaries() {
        viewModelScope.launch {
            _beneficiaryUiState.value = BeneficiaryUiState.Loading
            beneficiaryRepositoryImp.beneficiaryList().catch {
                _beneficiaryUiState.value = BeneficiaryUiState.ShowError(R.string.beneficiaries)
            }.collect {
                _beneficiaryUiState.value = BeneficiaryUiState.ShowBeneficiaryList(it)
            }
        }
    }
}