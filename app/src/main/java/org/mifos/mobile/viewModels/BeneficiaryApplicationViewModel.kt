package org.mifos.mobile.viewModels

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.mifos.mobile.R
import org.mifos.mobile.models.beneficiary.BeneficiaryPayload
import org.mifos.mobile.models.beneficiary.BeneficiaryUpdatePayload
import org.mifos.mobile.repositories.BeneficiaryRepository
import org.mifos.mobile.utils.BeneficiaryUiState
import javax.inject.Inject

@HiltViewModel
class BeneficiaryApplicationViewModel @Inject constructor(private val beneficiaryRepositoryImp: BeneficiaryRepository) :
    ViewModel() {

    private val _beneficiaryUiState = MutableLiveData<BeneficiaryUiState>()
    val beneficiaryUiState: LiveData<BeneficiaryUiState> get() = _beneficiaryUiState

    fun loadBeneficiaryTemplate() {
        viewModelScope.launch {
            _beneficiaryUiState.value = BeneficiaryUiState.Loading
            val response = beneficiaryRepositoryImp.beneficiaryTemplate()
            if (response?.isSuccessful == true) {
                _beneficiaryUiState.value =
                    response.body()?.let { BeneficiaryUiState.ShowBeneficiaryTemplate(it) }
                _beneficiaryUiState.value = BeneficiaryUiState.SetVisibility(View.VISIBLE)
            } else {
                _beneficiaryUiState.value =
                    BeneficiaryUiState.ShowError(R.string.error_fetching_beneficiary_template)
            }
        }
    }

    fun createBeneficiary(payload: BeneficiaryPayload?) {
        viewModelScope.launch {
            _beneficiaryUiState.value = BeneficiaryUiState.Loading
            val response = beneficiaryRepositoryImp.createBeneficiary(payload)
            if (response?.isSuccessful == true) {
                _beneficiaryUiState.value = BeneficiaryUiState.CreatedSuccessfully
            } else {
                _beneficiaryUiState.value =
                    BeneficiaryUiState.ShowError(R.string.error_creating_beneficiary)
            }
        }
    }

    fun updateBeneficiary(beneficiaryId: Long?, payload: BeneficiaryUpdatePayload?) {
        viewModelScope.launch {
            _beneficiaryUiState.value = BeneficiaryUiState.Loading
            val response = beneficiaryRepositoryImp.updateBeneficiary(beneficiaryId, payload)
            if (response?.isSuccessful == true) {
                _beneficiaryUiState.value = BeneficiaryUiState.UpdatedSuccessfully
            } else {
                _beneficiaryUiState.value =
                    BeneficiaryUiState.ShowError(R.string.error_updating_beneficiary)
            }
        }
    }
}