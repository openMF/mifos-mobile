package org.mifos.mobile.feature.beneficiary.beneficiary_application

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.mifos.mobile.core.data.repositories.BeneficiaryRepository
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.model.entity.beneficiary.BeneficiaryPayload
import org.mifos.mobile.core.model.entity.beneficiary.BeneficiaryUpdatePayload
import org.mifos.mobile.core.model.entity.templates.beneficiary.BeneficiaryTemplate
import org.mifos.mobile.core.model.enums.BeneficiaryState
import org.mifos.mobile.feature.beneficiary.R
import javax.inject.Inject

@HiltViewModel
class BeneficiaryApplicationViewModel @Inject constructor(private val beneficiaryRepositoryImp: BeneficiaryRepository) :
    ViewModel() {

    private val _beneficiaryUiState = MutableStateFlow<BeneficiaryApplicationUiState>(
        BeneficiaryApplicationUiState.Loading
    )
    val beneficiaryUiState: StateFlow<BeneficiaryApplicationUiState> get() = _beneficiaryUiState

    private val _beneficiaryState: MutableStateFlow<BeneficiaryState> = MutableStateFlow(
        BeneficiaryState.CREATE_QR)
    val beneficiaryState: StateFlow<BeneficiaryState> get() = _beneficiaryState

    private var _beneficiary: MutableStateFlow<Beneficiary?> = MutableStateFlow(null)
    val beneficiary: StateFlow<Beneficiary?> get() = _beneficiary

    fun initArgs(beneficiaryState: BeneficiaryState, beneficiary: Beneficiary?) {
        _beneficiaryState.value = beneficiaryState
        _beneficiary.value = beneficiary
    }

    fun loadBeneficiaryTemplate() {
        viewModelScope.launch {
                _beneficiaryUiState.value = BeneficiaryApplicationUiState.Loading
            beneficiaryRepositoryImp.beneficiaryTemplate().catch {
               _beneficiaryUiState.value =
                   BeneficiaryApplicationUiState.Error(R.string.error_fetching_beneficiary_template)
            }.collect {
                _beneficiaryUiState.value = BeneficiaryApplicationUiState.Template(it)
            }
        }
    }

    fun submitBeneficiary(beneficiaryPayload: BeneficiaryPayload) {
        when(beneficiaryState.value) {
            org.mifos.mobile.core.model.enums.BeneficiaryState.UPDATE -> updateBeneficiary(
                beneficiaryId = beneficiary.value?.id?.toLong(),
                payload = BeneficiaryUpdatePayload(
                    name = beneficiaryPayload.name,
                    transferLimit = beneficiaryPayload.transferLimit ?: 0F
                )
            )
            else -> createBeneficiary(payload = beneficiaryPayload)
        }
    }

    private fun createBeneficiary(payload: BeneficiaryPayload?) {
        viewModelScope.launch {
            _beneficiaryUiState.value = BeneficiaryApplicationUiState.Loading
            beneficiaryRepositoryImp.createBeneficiary(payload).catch {
                _beneficiaryUiState.value =
                    BeneficiaryApplicationUiState.Error(R.string.error_creating_beneficiary)
            }.collect {
                _beneficiaryUiState.value = BeneficiaryApplicationUiState.Success
            }
        }
    }

    private fun updateBeneficiary(beneficiaryId: Long?, payload: BeneficiaryUpdatePayload?) {
        viewModelScope.launch {
            _beneficiaryUiState.value = BeneficiaryApplicationUiState.Loading
            beneficiaryRepositoryImp.updateBeneficiary(beneficiaryId, payload).catch {
                _beneficiaryUiState.value =
                    BeneficiaryApplicationUiState.Error(R.string.error_updating_beneficiary)
            }.collect {
                _beneficiaryUiState.value = BeneficiaryApplicationUiState.Success
            }
        }
    }

}

sealed class BeneficiaryApplicationUiState {
    data class Template(val beneficiaryTemplate: BeneficiaryTemplate): BeneficiaryApplicationUiState()
    data object Loading: BeneficiaryApplicationUiState()
    data object Success: BeneficiaryApplicationUiState()
    data class Error(val errorResId: Int): BeneficiaryApplicationUiState()
}

