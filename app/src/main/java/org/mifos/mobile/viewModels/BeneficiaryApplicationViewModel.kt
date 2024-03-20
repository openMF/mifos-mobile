package org.mifos.mobile.viewModels

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
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

    private val _beneficiaryUiState =
        MutableStateFlow<BeneficiaryUiState>(BeneficiaryUiState.Initial)
    val beneficiaryUiState: StateFlow<BeneficiaryUiState> get() = _beneficiaryUiState

    fun loadBeneficiaryTemplate() {
        viewModelScope.launch {
                _beneficiaryUiState.value = BeneficiaryUiState.Loading
            beneficiaryRepositoryImp.beneficiaryTemplate().catch {
               _beneficiaryUiState.value =
                    BeneficiaryUiState.ShowError(R.string.error_fetching_beneficiary_template)
            }.onCompletion {
                _beneficiaryUiState.value = BeneficiaryUiState.SetVisibility(View.VISIBLE)
            }.collect {
                _beneficiaryUiState.value = BeneficiaryUiState.ShowBeneficiaryTemplate(it)
            }
        }
    }

    fun createBeneficiary(payload: BeneficiaryPayload?) {
        viewModelScope.launch {
            _beneficiaryUiState.value = BeneficiaryUiState.Loading
            beneficiaryRepositoryImp.createBeneficiary(payload).catch {
                _beneficiaryUiState.value =
                    BeneficiaryUiState.ShowError(R.string.error_creating_beneficiary)
            }.collect {
                _beneficiaryUiState.value = BeneficiaryUiState.CreatedSuccessfully
            }
        }
    }

    fun updateBeneficiary(beneficiaryId: Long?, payload: BeneficiaryUpdatePayload?) {
        viewModelScope.launch {
            _beneficiaryUiState.value = BeneficiaryUiState.Loading
            beneficiaryRepositoryImp.updateBeneficiary(beneficiaryId, payload).catch {
                _beneficiaryUiState.value =
                    BeneficiaryUiState.ShowError(R.string.error_updating_beneficiary)
            }.collect {
                _beneficiaryUiState.value = BeneficiaryUiState.UpdatedSuccessfully
            }

        }
    }
}