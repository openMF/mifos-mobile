package org.mifos.mobile.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.mifos.mobile.models.payload.AccountDetail
import org.mifos.mobile.models.templates.account.AccountOption
import org.mifos.mobile.models.templates.account.AccountOptionsTemplate
import org.mifos.mobile.repositories.SavingsAccountRepository
import org.mifos.mobile.utils.SavingsAccountUiState
import javax.inject.Inject

@HiltViewModel
class SavingsMakeTransferViewModel @Inject constructor(private val savingsAccountRepositoryImp: SavingsAccountRepository) :
    ViewModel() {

    private val compositeDisposables = CompositeDisposable()
    private val _savingsMakeTransferUiState = MutableLiveData<SavingsAccountUiState>()
    val savingsMakeTransferUiState : LiveData<SavingsAccountUiState> get() = _savingsMakeTransferUiState

    /**
     * Fetches [AccountOptionsTemplate] from server and notifies the view to display it. And
     * in case of any error during fetching the required details it notifies the view.
     */
    fun loanAccountTransferTemplate() {
        _savingsMakeTransferUiState.value = SavingsAccountUiState.Loading
        savingsAccountRepositoryImp.loanAccountTransferTemplate()
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribeWith(object : DisposableObserver<AccountOptionsTemplate?>() {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    _savingsMakeTransferUiState.value = SavingsAccountUiState.ErrorMessage(e)
                }

                override fun onNext(accountOptionsTemplate: AccountOptionsTemplate) {
                    _savingsMakeTransferUiState.value = SavingsAccountUiState.ShowSavingsAccountTemplate(accountOptionsTemplate)

                }
            })?.let {
                compositeDisposables.add(
                    it,
                )
            }
    }

    /**
     * Retrieving [List] of `accountNo` from [List] of [AccountOption]
     *
     * @param accountOptions [List] of [AccountOption]
     * @return Returns [List] containing `accountNo`
     */
    fun getAccountNumbers(
        accountOptions: List<AccountOption>?,
        isTypePayFrom: Boolean,
        accountTypeLoanString : String?
    ): List<AccountDetail> {
        val accountNumber: MutableList<AccountDetail> = ArrayList()
        Observable.fromIterable(accountOptions)
            .filter { (_, _, accountType) -> !(accountType?.code == accountTypeLoanString && isTypePayFrom) }
            .flatMap { (_, accountNo, accountType) ->
                Observable.just(
                    AccountDetail(
                        accountNo!!,
                        accountType?.value!!,
                    ),
                )
            }
            .subscribe { accountDetail -> accountNumber.add(accountDetail) }
        return accountNumber
    }

    /**
     * Searches for a [AccountOption] with provided `accountId` from [List] of
     * [AccountOption] and returns it.
     *
     * @param accountOptions [List] of [AccountOption]
     * @param accountId      AccountId which needs to searched in [List] of [                       ]
     * @return Returns [AccountOption] which has accountId same as the provided
     * `accountId` in function parameter.
     */
    fun searchAccount(accountOptions: List<AccountOption>?, accountId: Long?): AccountOption {
        val accountOption = arrayOf(AccountOption())
        Observable.fromIterable(accountOptions)
            .filter { (accountId1) -> accountId == accountId1?.toLong() }
            .subscribe { account -> accountOption[0] = account }
        return accountOption[0]
    }

}