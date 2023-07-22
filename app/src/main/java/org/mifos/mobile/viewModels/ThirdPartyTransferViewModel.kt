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
import org.mifos.mobile.R
import org.mifos.mobile.models.AccountOptionAndBeneficiary
import org.mifos.mobile.models.beneficiary.Beneficiary
import org.mifos.mobile.models.beneficiary.BeneficiaryDetail
import org.mifos.mobile.models.payload.AccountDetail
import org.mifos.mobile.models.templates.account.AccountOption
import org.mifos.mobile.repositories.ThirdPartyTransferRepository
import org.mifos.mobile.utils.ThirdPartyTransferUiState
import javax.inject.Inject

@HiltViewModel
class ThirdPartyTransferViewModel @Inject constructor(private val thirdPartyTransferRepositoryImp: ThirdPartyTransferRepository) :
    ViewModel() {

    private val compositeDisposables: CompositeDisposable = CompositeDisposable()

    private val _thirdPartyTransferUiState = MutableLiveData<ThirdPartyTransferUiState>()
    val thirdPartyTransferUiState: LiveData<ThirdPartyTransferUiState> get() = _thirdPartyTransferUiState

    fun loadTransferTemplate() {
        _thirdPartyTransferUiState.value = ThirdPartyTransferUiState.Loading
        compositeDisposables.add(
            Observable.zip(
                thirdPartyTransferRepositoryImp.thirdPartyTransferTemplate(),
                thirdPartyTransferRepositoryImp.beneficiaryList()
            ) { templateResult, beneficiaryListResult ->
                AccountOptionAndBeneficiary(
                    templateResult,
                    beneficiaryListResult,
                )
            }.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(object : DisposableObserver<AccountOptionAndBeneficiary?>() {
                    override fun onComplete() {}

                    override fun onError(e: Throwable) {
                        _thirdPartyTransferUiState.value =
                            ThirdPartyTransferUiState.Error(R.string.error_fetching_third_party_transfer_template)
                    }

                    override fun onNext(accountOptionAndBeneficiary: AccountOptionAndBeneficiary) {
                        _thirdPartyTransferUiState.value =
                            ThirdPartyTransferUiState.ShowThirdPartyTransferTemplate(
                                accountOptionAndBeneficiary.accountOptionsTemplate
                            )
                        _thirdPartyTransferUiState.value =
                            ThirdPartyTransferUiState.ShowBeneficiaryList(
                                accountOptionAndBeneficiary.beneficiaryList
                            )
                    }
                }),
        )
    }

    /**
     * Retrieving [List] of `accountNumbers` from [List] of [AccountOption]
     *
     * @param accountOptions [List] of [AccountOption]
     * @return Returns [List] containing `accountNumbers`
     */
    fun getAccountNumbersFromAccountOptions(
        accountOptions: List<AccountOption>?,
        accountTypeLoanString: String?
    ): List<AccountDetail> {
        val accountNumber: MutableList<AccountDetail> = ArrayList()
        Observable.fromIterable(accountOptions)
            .filter { (_, _, accountType) -> accountType?.code != accountTypeLoanString }
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
     * Retrieving [List] of `accountNumbers` from [List] of [Beneficiary]
     *
     * @param beneficiaries [List] of [Beneficiary]
     * @return Returns [List] containing `accountNumbers`
     */
    fun getAccountNumbersFromBeneficiaries(beneficiaries: List<Beneficiary?>?): List<BeneficiaryDetail> {
        val accountNumbers: MutableList<BeneficiaryDetail> = ArrayList()
        Observable.fromIterable(beneficiaries)
            .flatMap { (_, name, _, _, _, accountNumber) ->
                Observable.just(
                    BeneficiaryDetail(
                        accountNumber,
                        name,
                    ),
                )
            }
            .subscribe { beneficiaryDetail -> accountNumbers.add(beneficiaryDetail) }
        return accountNumbers
    }

    /**
     * Searches for a [AccountOption] with provided `accountNo` from [List] of
     * [AccountOption] and returns it.
     *
     * @param accountOptions [List] of [AccountOption]
     * @param accountNo      Account Number which needs to searched in [List] of
     * [AccountOption]
     * @return Returns [AccountOption] which has Account Number same as the provided
     * `accountNo` in function parameter.
     */
    fun searchAccount(accountOptions: List<AccountOption>?, accountNo: String?): AccountOption {
        val account = arrayOf(AccountOption())
        Observable.fromIterable(accountOptions)
            .filter { (_, accountNo1) -> accountNo1 == accountNo }
            .subscribe { accountOption -> account[0] = accountOption }
        return account[0]
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposables.clear()
    }

}