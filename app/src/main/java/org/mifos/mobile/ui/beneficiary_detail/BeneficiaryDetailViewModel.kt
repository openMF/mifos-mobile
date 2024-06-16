package org.mifos.mobile.ui.beneficiary_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.mifos.mobile.R
import org.mifos.mobile.models.beneficiary.Beneficiary
import org.mifos.mobile.repositories.BeneficiaryRepository
import javax.inject.Inject

@HiltViewModel
class BeneficiaryDetailViewModel @Inject constructor(
    private val beneficiaryRepositoryImp: BeneficiaryRepository
) : ViewModel() {

    var beneficiary = MutableStateFlow<Beneficiary?>(null)

    private val _beneficiaryDetailsUiStates =
        MutableStateFlow<BeneficiaryDetailsUiState>(BeneficiaryDetailsUiState.Initial)
    val beneficiaryDetailsUiStates: StateFlow<BeneficiaryDetailsUiState> get() = _beneficiaryDetailsUiStates

    fun setBeneficiary(beneficiary: Beneficiary?) {
        if (beneficiary == null) {
            _beneficiaryDetailsUiStates.value = BeneficiaryDetailsUiState.ShowError(R.string.something_went_wrong)
        }
        this.beneficiary.value = beneficiary
    }

    fun getBeneficiary(): Beneficiary? {
        return beneficiary.value
    }

    fun deleteBeneficiary(beneficiaryId: Long?) {
        viewModelScope.launch {
            _beneficiaryDetailsUiStates.value = BeneficiaryDetailsUiState.Loading
            beneficiaryRepositoryImp.deleteBeneficiary(beneficiaryId).catch {
                _beneficiaryDetailsUiStates.value =
                    BeneficiaryDetailsUiState.ShowError(R.string.error_deleting_beneficiary)
            }.collect {
                _beneficiaryDetailsUiStates.value = BeneficiaryDetailsUiState.DeletedSuccessfully
            }
        }
    }
}



sealed class BeneficiaryDetailsUiState {
    object Initial : BeneficiaryDetailsUiState()
    object Loading : BeneficiaryDetailsUiState()
    object DeletedSuccessfully : BeneficiaryDetailsUiState()
    data class ShowError(val message: Int) : BeneficiaryDetailsUiState()
}

