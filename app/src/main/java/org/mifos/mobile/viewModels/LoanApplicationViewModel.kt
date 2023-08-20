package org.mifos.mobile.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.mifos.mobile.R
import org.mifos.mobile.repositories.LoanRepository
import org.mifos.mobile.ui.enums.LoanState
import org.mifos.mobile.utils.LoanUiState
import javax.inject.Inject

@HiltViewModel
class LoanApplicationViewModel @Inject constructor(private val loanRepositoryImp: LoanRepository) :
    ViewModel() {

    private val _loanUiState = MutableStateFlow<LoanUiState>(LoanUiState.Loading)
    val loanUiState: StateFlow<LoanUiState> = _loanUiState

    fun loadLoanApplicationTemplate(loanState: LoanState) {
        viewModelScope.launch {
            _loanUiState.value = LoanUiState.Loading
            loanRepositoryImp.template()?.catch {
                _loanUiState.value = LoanUiState.ShowError(R.string.error_fetching_template)
            }?.collect {
                if (loanState === LoanState.CREATE) {
                    _loanUiState.value =
                        it?.let { it1 -> LoanUiState.ShowLoanTemplateByProduct(it1) }!!
                } else {
                    _loanUiState.value = it?.let { it1 ->
                        LoanUiState.ShowUpdateLoanTemplateByProduct(
                            it1
                        )
                    }!!
                }
            }
        }
    }

    fun loadLoanApplicationTemplateByProduct(productId: Int?, loanState: LoanState?) {
        viewModelScope.launch {
            _loanUiState.value = LoanUiState.Loading
            loanRepositoryImp.getLoanTemplateByProduct(productId)?.catch {
                _loanUiState.value = LoanUiState.ShowError(R.string.error_fetching_template)
            }?.collect {
                if (loanState === LoanState.CREATE) {
                    _loanUiState.value = it?.let { it1 -> LoanUiState.ShowLoanTemplate(it1) }!!
                } else {
                    _loanUiState.value = it?.let { it1 -> LoanUiState.ShowUpdateLoanTemplate(it1) }!!
                }
            }
        }
    }
}