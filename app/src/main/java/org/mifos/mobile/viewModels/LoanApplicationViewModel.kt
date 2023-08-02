package org.mifos.mobile.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.mifos.mobile.R
import org.mifos.mobile.models.templates.loans.LoanTemplate
import org.mifos.mobile.repositories.LoanRepository
import org.mifos.mobile.ui.enums.LoanState
import org.mifos.mobile.utils.LoanUiState
import javax.inject.Inject

@HiltViewModel
class LoanApplicationViewModel @Inject constructor(private val loanRepositoryImp: LoanRepository) :
    ViewModel() {

    private val compositeDisposables: CompositeDisposable = CompositeDisposable()

    private val _loanUiState = MutableLiveData<LoanUiState>()
    val loanUiState: LiveData<LoanUiState> get() = _loanUiState

    fun loadLoanApplicationTemplate(loanState: LoanState) {
        _loanUiState.value = LoanUiState.Loading
        loanRepositoryImp.template()?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribeWith(object : DisposableObserver<LoanTemplate?>() {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    _loanUiState.value = LoanUiState.ShowError(R.string.error_fetching_template)
                }

                override fun onNext(loanTemplate: LoanTemplate) {
                    if (loanState === LoanState.CREATE) {
                        _loanUiState.value = LoanUiState.ShowLoanTemplateByProduct(loanTemplate)
                    } else {
                        _loanUiState.value = LoanUiState.ShowUpdateLoanTemplateByProduct(loanTemplate)
                    }
                }
            })?.let {
                compositeDisposables.add(
                    it,
                )
            }
    }

    fun loadLoanApplicationTemplateByProduct(productId: Int?, loanState: LoanState?) {
        _loanUiState.value = LoanUiState.Loading
        loanRepositoryImp.getLoanTemplateByProduct(productId)?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribeWith(object : DisposableObserver<LoanTemplate?>() {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    _loanUiState.value = LoanUiState.ShowError(R.string.error_fetching_template)
                }

                override fun onNext(loanTemplate: LoanTemplate) {
                    if (loanState === LoanState.CREATE) {
                        _loanUiState.value = LoanUiState.ShowLoanTemplate(loanTemplate)
                    } else {
                        _loanUiState.value = LoanUiState.ShowUpdateLoanTemplate(loanTemplate)
                    }
                }
            })?.let {
                compositeDisposables.add(
                    it,
                )
            }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposables.clear()
    }
}