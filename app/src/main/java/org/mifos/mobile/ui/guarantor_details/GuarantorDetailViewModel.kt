package org.mifos.mobile.ui.guarantor_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.mifos.mobile.models.guarantor.GuarantorPayload
import org.mifos.mobile.repositories.GuarantorRepository
import org.mifos.mobile.ui.enums.LoanState
import org.mifos.mobile.utils.GuarantorUiState
import org.mifos.mobile.utils.RxBus
import org.mifos.mobile.utils.RxBus.publish
import org.mifos.mobile.utils.RxEvent
import javax.inject.Inject

/**
 * Currently we do not get back any response from the guarantorApi, hence we are using FakeRemoteDataSource
 * to show a list of guarantors. You can look at the implementation of [GuarantorRepository] for better understanding
 */

@HiltViewModel
class GuarantorDetailViewModel @Inject constructor(private val guarantorRepositoryImp: GuarantorRepository) :
    ViewModel() {

    private val _guarantorUiState = MutableStateFlow<GuarantorDetailUiState>(GuarantorDetailUiState.Initial)
    val guarantorUiState: StateFlow<GuarantorDetailUiState> = _guarantorUiState

    private val _guarantorUiData = MutableStateFlow(GuarantorPayload())
    val guarantorUiData: StateFlow<GuarantorPayload> = _guarantorUiData

    private var _loanId: Long = 0
    val loanId get() = _loanId

    private var _index: Int = 0
    val index get() = _index

    fun deleteGuarantor() {
        viewModelScope.launch {
            _guarantorUiState.value = GuarantorDetailUiState.Loading
            guarantorRepositoryImp.deleteGuarantor(loanId, guarantorUiData.value.id)?.catch { e ->
                _guarantorUiState.value = GuarantorDetailUiState.ShowError(e.message)
            }?.collect {
                publish(RxEvent.DeleteGuarantorEvent(index))
                _guarantorUiState.value = GuarantorDetailUiState.GuarantorDeletedSuccessfully(it?.string())
            }
        }
    }

    fun setGuarantorData(guarantorPayload: GuarantorPayload?) {
        _guarantorUiData.value = guarantorPayload ?: GuarantorPayload()
    }

    fun setLoanId(id: Long?) {
        this._loanId = id ?: 0
    }

    fun setIndex(index: Int?) {
        this._index = index ?: 0
    }
}

sealed class GuarantorDetailUiState {
    data class GuarantorDeletedSuccessfully(val message: String?): GuarantorDetailUiState()
    data class ShowError(val message: String?): GuarantorDetailUiState()
    data object Initial: GuarantorDetailUiState()
    data object Loading: GuarantorDetailUiState()
}
