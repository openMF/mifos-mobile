//package org.mifos.mobile.viewModels
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.catch
//import kotlinx.coroutines.launch
//import org.mifos.mobile.R
//import org.mifos.mobile.repositories.BeneficiaryRepository
//import org.mifos.mobile.ui.beneficiary_list.BeneficiaryUiState
//import javax.inject.Inject
//
//@HiltViewModel
//class BeneficiaryDetailViewModel @Inject constructor(private val beneficiaryRepositoryImp: BeneficiaryRepository) :
//    ViewModel() {
//
//    private val _beneficiaryUiState =
//        MutableStateFlow<BeneficiaryUiState>(BeneficiaryUiState.Initial)
//    val beneficiaryUiState: StateFlow<BeneficiaryUiState> get() = _beneficiaryUiState
//
//    fun deleteBeneficiary(beneficiaryId: Long?) {
//        viewModelScope.launch {
//            _beneficiaryUiState.value = BeneficiaryUiState.Loading
//            beneficiaryRepositoryImp.deleteBeneficiary(beneficiaryId).catch {
//                _beneficiaryUiState.value =
//                    BeneficiaryUiState.ShowError(R.string.error_deleting_beneficiary)
//            }.collect {
//                _beneficiaryUiState.value = BeneficiaryUiState.DeletedSuccessfully
//            }
//        }
//    }
//}