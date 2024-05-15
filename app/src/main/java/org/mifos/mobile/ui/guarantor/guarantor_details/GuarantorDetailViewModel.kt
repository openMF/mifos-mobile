package org.mifos.mobile.ui.guarantor.guarantor_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.mifos.mobile.R
import org.mifos.mobile.models.guarantor.GuarantorPayload
import org.mifos.mobile.repositories.GuarantorRepository
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.Result
import org.mifos.mobile.utils.asResult
import javax.inject.Inject

/**
 * Currently we do not get back any response from the guarantorApi, hence we are using FakeRemoteDataSource
 * to show a list of guarantors. You can look at the implementation of [GuarantorRepository] for better understanding
 */

@HiltViewModel
class GuarantorDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val guarantorRepositoryImp: GuarantorRepository
) : ViewModel() {

    val index = savedStateHandle.getStateFlow(key = Constants.INDEX, initialValue = -1)
    val loanId = savedStateHandle.getStateFlow<Long>(key = Constants.LOAN_ID, initialValue = -1)

    private val _guarantorDeleteState =
        MutableStateFlow<GuarantorDetailUiState>(GuarantorDetailUiState.Loading)
    val guarantorDeleteState: StateFlow<GuarantorDetailUiState> = _guarantorDeleteState

    private var guarantorItem = loanId
        .flatMapLatest { loanId ->
            guarantorRepositoryImp.getGuarantorList(loanId = loanId)
        }.asResult().map { result ->
            when (result) {
                is Result.Success -> GuarantorDetailUiState.ShowDetail(guarantorItem = result.data?.filter { it?.status == true }?.get(index = index.value))
                is Result.Loading -> GuarantorDetailUiState.Loading
                is Result.Error -> GuarantorDetailUiState.Error(result.exception.message)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(300),
            initialValue = GuarantorDetailUiState.Loading
        )

    val guarantorUiState: StateFlow<GuarantorDetailUiState> = merge(
        guarantorItem,
        guarantorDeleteState
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(300),
        initialValue = GuarantorDetailUiState.Loading
    )

    fun deleteGuarantor(guarantorId: Long) {
        viewModelScope.launch {
            _guarantorDeleteState.value = GuarantorDetailUiState.Loading
            guarantorRepositoryImp.deleteGuarantor(
                loanId = loanId.value,
                guarantorId = guarantorId
            ).catch { e ->
                _guarantorDeleteState.value = GuarantorDetailUiState.Error(e.message)
            }.collect { response ->
                _guarantorDeleteState.value = GuarantorDetailUiState.GuarantorDeletedSuccessfully(R.string.guarantor_deleted_successfully)
            }
        }
    }
}

sealed class GuarantorDetailUiState {
    data class GuarantorDeletedSuccessfully(val messageResId: Int) : GuarantorDetailUiState()
    data class Error(val message: String?) : GuarantorDetailUiState()
    data class ShowDetail(val guarantorItem: GuarantorPayload?) : GuarantorDetailUiState()
    data object Loading : GuarantorDetailUiState()
}
