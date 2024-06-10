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
import org.mifos.mobile.models.templates.beneficiary.BeneficiaryTemplate
import org.mifos.mobile.repositories.BeneficiaryRepository
import javax.inject.Inject



@HiltViewModel
class BeneficiaryListViewModel @Inject constructor(private val beneficiaryRepositoryImp: BeneficiaryRepository) :
    ViewModel() {

    private val _beneficiaryUiState =
        MutableStateFlow<BeneficiaryUiState>(BeneficiaryUiState.Initial)
    val beneficiaryUiState: StateFlow<BeneficiaryUiState> get() = _beneficiaryUiState

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
            _beneficiaryUiState.value = BeneficiaryUiState.Loading
            beneficiaryRepositoryImp.beneficiaryList().catch {
                _beneficiaryUiState.value = BeneficiaryUiState.ShowError(R.string.beneficiaries)
            }.collect {
                _beneficiaryUiState.value = BeneficiaryUiState.ShowBeneficiaryList(it)
            }
        }
    }
}




sealed class BeneficiaryUiState {
    object Initial : BeneficiaryUiState()
    object Loading : BeneficiaryUiState()
    object CreatedSuccessfully : BeneficiaryUiState()
    object UpdatedSuccessfully : BeneficiaryUiState()
    object DeletedSuccessfully : BeneficiaryUiState()
    data class ShowError(val message: Int) : BeneficiaryUiState()
    data class SetVisibility(val visibility: Int) : BeneficiaryUiState()
    data class ShowBeneficiaryTemplate(val beneficiaryTemplate: BeneficiaryTemplate) :
        BeneficiaryUiState()

    data class ShowBeneficiaryList(val beneficiaries: List<Beneficiary?>) : BeneficiaryUiState()

}



//val beneficiaryList = listOf(
//    Beneficiary(
//        id = 1,
//        name = "John Doe",
//        officeName = "Mifos Head Office",
//        clientName = "Jane Smith",
//        accountType = null,
//        accountNumber = "1234567890",
//        transferLimit = 1000.00
//    ),
//    Beneficiary(
//        id = 2,
//        name = "Alice Johnson",
//        officeName = "Mifos Branch 1",
//        clientName = "Bob Smith",
//        accountType = null,
//        accountNumber = "0987654321",
//        transferLimit = 500.00
//    ),
//    Beneficiary(
//        id = 3,
//        name = "Michael Brown",
//        officeName = "Mifos Branch 2",
//        clientName = "Sarah Jones",
//        accountType = null,
//        accountNumber = "9876543210",
//        transferLimit = 2000.00
//    ),
//    Beneficiary(
//        id = 4,
//        name = "David Williams",
//        officeName = "Mifos Head Office",
//        clientName = "Emily Miller",
//        accountType = null,
//        accountNumber = "1011121314",
//        transferLimit = 750.00
//    )
//)
