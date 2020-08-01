package org.mifos.mobile.presenters

import android.content.Context

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

import org.mifos.mobile.R
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.injection.ApplicationContext
import org.mifos.mobile.models.AccountOptionAndBeneficiary
import org.mifos.mobile.models.beneficiary.Beneficiary
import org.mifos.mobile.models.beneficiary.BeneficiaryDetail
import org.mifos.mobile.models.payload.AccountDetail
import org.mifos.mobile.models.templates.account.AccountOption
import org.mifos.mobile.models.templates.account.AccountOptionsTemplate
import org.mifos.mobile.presenters.base.BasePresenter
import org.mifos.mobile.ui.views.ThirdPartyTransferView

import java.util.*
import javax.inject.Inject

/**
 * Created by dilpreet on 21/6/17.
 */
class ThirdPartyTransferPresenter @Inject constructor(
        private val dataManager: DataManager?,
        @ApplicationContext context: Context?
) : BasePresenter<ThirdPartyTransferView?>(context) {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    override fun attachView(mvpView: ThirdPartyTransferView?) {
        super.attachView(mvpView)
    }

    override fun detachView() {
        super.detachView()
        compositeDisposable.clear()
    }

    /**
     * Fetches [AccountOptionsTemplate] and [List] of [Beneficiary] from server
     * and notifies the view to display them. And in case of any error during fetching the required
     * details it notifies the view.
     */
    fun loadTransferTemplate() {
        checkViewAttached()
        mvpView?.showProgress()
        compositeDisposable.add(Observable.zip(dataManager?.thirdPartyTransferTemplate,
                dataManager?.beneficiaryList,
                BiFunction<AccountOptionsTemplate?, List<Beneficiary?>?, AccountOptionAndBeneficiary> { accountOptionsTemplate, beneficiaries ->
                    AccountOptionAndBeneficiary(accountOptionsTemplate,
                            beneficiaries)
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(object : DisposableObserver<AccountOptionAndBeneficiary?>() {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                        mvpView?.hideProgress()
                        mvpView?.showError(context?.getString(
                                R.string.error_fetching_third_party_transfer_template))
                    }

                    override fun onNext(accountOptionAndBeneficiary: AccountOptionAndBeneficiary) {
                        mvpView?.hideProgress()
                        mvpView?.showThirdPartyTransferTemplate(accountOptionAndBeneficiary.accountOptionsTemplate)
                        mvpView?.showBeneficiaryList(accountOptionAndBeneficiary.beneficiaryList)
                    }
                }))
    }

    /**
     * Retrieving [List] of `accountNumbers` from [List] of [AccountOption]
     *
     * @param accountOptions [List] of [AccountOption]
     * @return Returns [List] containing `accountNumbers`
     */
    fun getAccountNumbersFromAccountOptions(accountOptions: List<AccountOption>?): List<AccountDetail> {
        val accountNumber: MutableList<AccountDetail> = ArrayList()
        Observable.fromIterable(accountOptions)
                .filter { (_, _, accountType) -> accountType?.code != context?.getString(R.string.account_type_loan) }
                .flatMap { (_, accountNo, accountType) ->
                    Observable.just(AccountDetail(accountNo!!,
                            accountType?.value!!))
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
                    Observable.just(BeneficiaryDetail(accountNumber,
                            name))
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

}