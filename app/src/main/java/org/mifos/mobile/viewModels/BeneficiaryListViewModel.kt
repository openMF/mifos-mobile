package org.mifos.mobile.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.mifos.mobile.R
import org.mifos.mobile.repositories.BeneficiaryRepository
import org.mifos.mobile.utils.BeneficiaryUiState
import javax.inject.Inject

@HiltViewModel
class BeneficiaryListViewModel @Inject constructor(private val beneficiaryRepositoryImp: BeneficiaryRepository) :
    ViewModel() {

    private val _beneficiaryUiState = MutableLiveData<BeneficiaryUiState>()
    val beneficiaryUiState: LiveData<BeneficiaryUiState> get() = _beneficiaryUiState

    fun loadBeneficiaries() {
        viewModelScope.launch {
            _beneficiaryUiState.value = BeneficiaryUiState.Loading
            val response = beneficiaryRepositoryImp.beneficiaryList()
            if (response?.isSuccessful == true) {
                _beneficiaryUiState.value =
                    response.body()?.let { BeneficiaryUiState.ShowBeneficiaryList(it) }
            } else {
                _beneficiaryUiState.value = BeneficiaryUiState.ShowError(R.string.beneficiaries)
            }
        }
    }
}