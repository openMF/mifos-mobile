package org.mifos.mobile.feature.beneficiary.beneficiary_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.data.repositories.BeneficiaryRepository
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.feature.beneficiary.R
import org.mifos.mobile.feature.beneficiary.navigation.BENEFICIARY_ID
import javax.inject.Inject

@HiltViewModel
class BeneficiaryDetailViewModel @Inject constructor(
    private val beneficiaryRepositoryImp: BeneficiaryRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _beneficiaryDetailsUiStates =
        MutableStateFlow<BeneficiaryDetailsUiState>(BeneficiaryDetailsUiState.Initial)
    val beneficiaryDetailsUiStates: StateFlow<BeneficiaryDetailsUiState> get() = _beneficiaryDetailsUiStates

    private val beneficiaryId = savedStateHandle.getStateFlow<Int?>(key = BENEFICIARY_ID, initialValue = null)

    val beneficiary: StateFlow<Beneficiary?> = beneficiaryId
        .flatMapLatest {
            beneficiaryRepositoryImp.beneficiaryList()
        }.map { beneficiaryList ->
            beneficiaryId.value?.let { beneficiaryId ->
                beneficiaryList.find { it.id == beneficiaryId }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

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
    data object Initial : BeneficiaryDetailsUiState()
    data object Loading : BeneficiaryDetailsUiState()
    data object DeletedSuccessfully : BeneficiaryDetailsUiState()
    data class ShowError(val message: Int) : BeneficiaryDetailsUiState()
}

