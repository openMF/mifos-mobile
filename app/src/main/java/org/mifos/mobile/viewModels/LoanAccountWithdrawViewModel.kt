package org.mifos.mobile.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.mifos.mobile.R
import org.mifos.mobile.models.accounts.loan.LoanWithdraw
import org.mifos.mobile.repositories.LoanRepository
import org.mifos.mobile.utils.LoanUiState
import javax.inject.Inject

@HiltViewModel
class LoanAccountWithdrawViewModel @Inject constructor(private val loanRepositoryImp: LoanRepository) :
    ViewModel() {

    private val compositeDisposables: CompositeDisposable = CompositeDisposable()

    private val _loanUiState = MutableLiveData<LoanUiState>()
    val loanUiState: LiveData<LoanUiState> get() = _loanUiState

    fun withdrawLoanAccount(loanId: Long?, loanWithdraw: LoanWithdraw?) {
        viewModelScope.launch {
            _loanUiState.value = LoanUiState.Loading
            loanRepositoryImp.withdrawLoanAccount(loanId, loanWithdraw)
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.io())
                ?.subscribeWith(object : DisposableObserver<ResponseBody?>() {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                        _loanUiState.value =
                            LoanUiState.ShowError(R.string.error_loan_account_withdraw)
                    }

                    override fun onNext(responseBody: ResponseBody) {
                        _loanUiState.value = LoanUiState.WithdrawSuccess
                    }
                })?.let { compositeDisposables.add(it) }
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposables.clear()
    }
}