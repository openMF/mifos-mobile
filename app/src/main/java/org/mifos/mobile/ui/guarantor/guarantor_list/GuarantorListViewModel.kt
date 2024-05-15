package org.mifos.mobile.ui.guarantor.guarantor_list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.mifos.mobile.models.guarantor.GuarantorPayload
import org.mifos.mobile.repositories.GuarantorRepository
import org.mifos.mobile.utils.Constants.LOAN_ID
import org.mifos.mobile.utils.Result
import org.mifos.mobile.utils.asResult
import javax.inject.Inject

/**
 * Currently we do not get back any response from the guarantorApi, hence we are using FakeRemoteDataSource
 * to show a list of guarantors. You can look at the implementation of [GuarantorRepository] for better understanding
 */

@HiltViewModel
class GuarantorListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val guarantorRepositoryImp: GuarantorRepository
) : ViewModel() {

    val guarantorUiState =
        savedStateHandle.getStateFlow<Long>(key = LOAN_ID, initialValue = -1)
            .flatMapLatest { loanId ->
                guarantorRepositoryImp.getGuarantorList(loanId = loanId)
            }
            .asResult()
            .map { result ->
                when (result) {
                    is Result.Success -> GuarantorListUiState.Success(result.data?.filter { it?.status == true })
                    is Result.Loading -> GuarantorListUiState.Loading
                    is Result.Error -> GuarantorListUiState.Error
                }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = GuarantorListUiState.Loading,
            )

}

sealed class GuarantorListUiState {
    data object Loading : GuarantorListUiState()
    data object Error : GuarantorListUiState()
    data class Success(val list: List<GuarantorPayload?>?) : GuarantorListUiState()
}
