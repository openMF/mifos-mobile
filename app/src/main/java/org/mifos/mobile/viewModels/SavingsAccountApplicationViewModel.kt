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
import org.mifos.mobile.models.accounts.savings.SavingsAccountApplicationPayload
import org.mifos.mobile.models.accounts.savings.SavingsAccountUpdatePayload
import org.mifos.mobile.models.templates.savings.SavingsAccountTemplate
import org.mifos.mobile.repositories.SavingsAccountRepository
import org.mifos.mobile.ui.enums.SavingsAccountState
import org.mifos.mobile.utils.SavingsAccountUiState
import javax.inject.Inject

@HiltViewModel
class SavingsAccountApplicationViewModel @Inject constructor(private val savingsAccountRepositoryImp : SavingsAccountRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val _savingsAccountApplicationUiState = MutableLiveData<SavingsAccountUiState>()
    val savingsAccountApplicationUiState : LiveData<SavingsAccountUiState> get() = _savingsAccountApplicationUiState

    fun loadSavingsAccountApplicationTemplate(
        clientId: Long?,
        state: SavingsAccountState?,
    ) {
        _savingsAccountApplicationUiState.value = SavingsAccountUiState.Loading
        savingsAccountRepositoryImp.getSavingAccountApplicationTemplate(clientId)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeWith(object : DisposableObserver<SavingsAccountTemplate?>() {
                override fun onNext(template: SavingsAccountTemplate) {
                    if (state === SavingsAccountState.CREATE) {
                        _savingsAccountApplicationUiState.value = SavingsAccountUiState.ShowUserInterfaceSavingAccountApplication(template)
                    } else {
                        _savingsAccountApplicationUiState.value = SavingsAccountUiState.ShowUserInterfaceSavingAccountUpdate(template)
                    }
                }

                override fun onError(e: Throwable) {
                    _savingsAccountApplicationUiState.value = SavingsAccountUiState.ErrorMessage(e)
                }

                override fun onComplete() {}
            })?.let { compositeDisposable.add(it) }
    }

    fun submitSavingsAccountApplication(payload: SavingsAccountApplicationPayload?) {
        _savingsAccountApplicationUiState.value = SavingsAccountUiState.Loading
        savingsAccountRepositoryImp.submitSavingAccountApplication(payload)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeWith(object : DisposableObserver<ResponseBody?>() {
                override fun onNext(responseBody: ResponseBody) {
                    _savingsAccountApplicationUiState.value = SavingsAccountUiState.HideProgress
                }

                override fun onError(e: Throwable) {
                    _savingsAccountApplicationUiState.value = SavingsAccountUiState.ErrorMessage(e)
                }

                override fun onComplete() {
                    _savingsAccountApplicationUiState.value = SavingsAccountUiState.SavingsAccountApplicationSuccess
                }
            })?.let { compositeDisposable.add(it) }
    }

    fun updateSavingsAccount(accountId: Long?, payload: SavingsAccountUpdatePayload?) {
        _savingsAccountApplicationUiState.value = SavingsAccountUiState.Loading
        savingsAccountRepositoryImp.updateSavingsAccount(accountId, payload)
            ?.subscribeOn(Schedulers.newThread())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeWith(object : DisposableObserver<ResponseBody?>() {
                override fun onNext(responseBody: ResponseBody) {
                    _savingsAccountApplicationUiState.value = SavingsAccountUiState.HideProgress
                }

                override fun onError(e: Throwable) {
                    _savingsAccountApplicationUiState.value = SavingsAccountUiState.ErrorMessage(e)
                }

                override fun onComplete() {
                    _savingsAccountApplicationUiState.value = SavingsAccountUiState.SavingsAccountUpdateSuccess
                }
            })?.let { compositeDisposable.add(it) }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}