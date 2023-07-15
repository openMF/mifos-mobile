package org.mifos.mobile.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import org.mifos.mobile.models.accounts.savings.SavingsAccountWithdrawPayload
import org.mifos.mobile.repositories.SavingsAccountRepository
import org.mifos.mobile.utils.SavingsAccountUiState
import javax.inject.Inject

@HiltViewModel
class SavingsAccountWithdrawViewModel @Inject constructor(private val savingsAccountRepositoryImp: SavingsAccountRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val _savingsAccountWithdrawUiState = MutableLiveData<SavingsAccountUiState>()
    val savingsAccountWithdrawUiState : LiveData<SavingsAccountUiState> get() = _savingsAccountWithdrawUiState

    fun submitWithdrawSavingsAccount(
        accountId: String?,
        payload: SavingsAccountWithdrawPayload?,
    ) {
        _savingsAccountWithdrawUiState.value = SavingsAccountUiState.Loading
        savingsAccountRepositoryImp.submitWithdrawSavingsAccount(accountId, payload)
            ?.subscribeOn(Schedulers.newThread())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeWith(object : DisposableObserver<ResponseBody?>() {
                override fun onNext(responseBodyObservable: ResponseBody) {
                    _savingsAccountWithdrawUiState.value =
                        SavingsAccountUiState.SavingsAccountWithdrawSuccess
                }

                override fun onError(e: Throwable) {
                    _savingsAccountWithdrawUiState.value = SavingsAccountUiState.ErrorMessage(e)
                }

                override fun onComplete() {}
            })?.let { compositeDisposable.add(it) }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}