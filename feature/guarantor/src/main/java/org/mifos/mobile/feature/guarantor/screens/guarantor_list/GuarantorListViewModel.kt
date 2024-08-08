package org.mifos.mobile.feature.guarantor.screens.guarantor_list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.Constants.LOAN_ID
import org.mifos.mobile.core.data.repositories.GuarantorRepository
import org.mifos.mobile.core.model.entity.guarantor.GuarantorPayload
import org.mifos.mobile.core.network.asResult
import org.mifos.mobile.core.network.Result
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

    private val _loanId = savedStateHandle.getStateFlow<String?>(key = LOAN_ID, initialValue = null)

    val loanId: StateFlow<Long> = _loanId
        .flatMapLatest { flowOf(it?.toLongOrNull() ?: -1L) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, -1L)

    val guarantorUiState = loanId
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
