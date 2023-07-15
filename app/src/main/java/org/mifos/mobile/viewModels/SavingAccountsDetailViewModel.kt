package org.mifos.mobile.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.mifos.mobile.models.accounts.savings.SavingsWithAssociations
import org.mifos.mobile.repositories.SavingsAccountRepository
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.SavingsAccountUiState
import javax.inject.Inject

@HiltViewModel
class SavingAccountsDetailViewModel @Inject constructor(private val savingsAccountRepositoryImp: SavingsAccountRepository) : ViewModel() {

    private val _savingAccountsDetailUiState = MutableLiveData<SavingsAccountUiState>()
    val savingAccountsDetailUiState : LiveData<SavingsAccountUiState> get() = _savingAccountsDetailUiState
    private val compositeDisposables: CompositeDisposable = CompositeDisposable()

    /**
     * Load details of a particular saving account from the server and notify the view
     * to display it. Notify the view, in case there is any error in fetching
     * the details from server.
     *
     * @param accountId Id of Savings Account
     */
    fun loadSavingsWithAssociations(accountId: Long?) {
        _savingAccountsDetailUiState.value = SavingsAccountUiState.Loading
        savingsAccountRepositoryImp.getSavingsWithAssociations(
            accountId,
            Constants.TRANSACTIONS,
        )
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribeWith(object : DisposableObserver<SavingsWithAssociations?>() {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    _savingAccountsDetailUiState.value = SavingsAccountUiState.Error
                }

                override fun onNext(savingAccount: SavingsWithAssociations) {
                    _savingAccountsDetailUiState.value = SavingsAccountUiState.SuccessLoadingSavingsWithAssociations(savingAccount)
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