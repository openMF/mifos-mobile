package org.mifos.mobile.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.mifos.mobile.R
import org.mifos.mobile.repositories.LoanRepository
import org.mifos.mobile.ui.enums.LoanState
import org.mifos.mobile.utils.LoanUiState
import javax.inject.Inject

@HiltViewModel
class LoanApplicationViewModel @Inject constructor(private val loanRepositoryImp: LoanRepository) :
    ViewModel() {

    private val _loanUiState = MutableLiveData<LoanUiState>()
    val loanUiState: LiveData<LoanUiState> get() = _loanUiState

    fun loadLoanApplicationTemplate(loanState: LoanState) {
        viewModelScope.launch {
            _loanUiState.value = LoanUiState.Loading
            val response = loanRepositoryImp.template()
            if (response?.isSuccessful == true) {
                if (loanState === LoanState.CREATE) {
                    _loanUiState.value = response.body()
                        ?.let { LoanUiState.ShowLoanTemplateByProduct(it) }
                } else {
                    _loanUiState.value = response.body()
                        ?.let { LoanUiState.ShowUpdateLoanTemplateByProduct(it) }
                }
            } else {
                _loanUiState.value = LoanUiState.ShowError(R.string.error_fetching_template)
            }
        }
    }

    fun loadLoanApplicationTemplateByProduct(productId: Int?, loanState: LoanState?) {
        viewModelScope.launch {
            _loanUiState.value = LoanUiState.Loading
            val response = loanRepositoryImp.getLoanTemplateByProduct(productId)
            if (response?.isSuccessful == true) {
                if (loanState === LoanState.CREATE) {
                    _loanUiState.value = response.body()?.let { LoanUiState.ShowLoanTemplate(it) }
                } else {
                    _loanUiState.value = response.body()
                        ?.let { LoanUiState.ShowUpdateLoanTemplate(it) }
                }
            } else {
                _loanUiState.value = LoanUiState.ShowError(R.string.error_fetching_template)
            }
        }
    }

}