package org.mifos.mobile.presenters

import android.content.Context
import android.graphics.Bitmap
import android.util.Base64
import android.util.Log

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

import okhttp3.ResponseBody

import org.mifos.mobile.R
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.api.local.PreferencesHelper
import org.mifos.mobile.injection.ActivityContext
import org.mifos.mobile.models.accounts.loan.LoanAccount
import org.mifos.mobile.models.accounts.savings.SavingAccount
import org.mifos.mobile.models.client.Client
import org.mifos.mobile.models.client.ClientAccounts
import org.mifos.mobile.presenters.base.BasePresenter
import org.mifos.mobile.ui.views.HomeOldView
import org.mifos.mobile.utils.ImageUtil.Companion.instance

import java.io.IOException
import javax.inject.Inject

/**
 * Created by dilpreet on 19/6/17.
 */
class HomeOldPresenter @Inject constructor(
        private val dataManager: DataManager, @ActivityContext context: Context,
        private val preferencesHelper: PreferencesHelper
) : BasePresenter<HomeOldView?>(context) {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    override fun attachView(mvpView: HomeOldView?) {
        super.attachView(mvpView)
    }

    override fun detachView() {
        super.detachView()
        compositeDisposable.clear()
    }

    /**
     * Fetches Client account details as [ClientAccounts] from the server and notifies the
     * view to display the [List] of [LoanAccount] and [SavingAccount]. And in
     * case of any error during fetching the required details it notifies the view.
     */
    fun loadClientAccountDetails() {
        checkViewAttached()
        mvpView?.showProgress()
        dataManager.clientAccounts
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.io())
                ?.subscribeWith(object : DisposableObserver<ClientAccounts?>() {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                        mvpView?.hideProgress()
                        mvpView?.showError(context?.getString(R.string.error_fetching_accounts))
                    }

                    override fun onNext(clientAccounts: ClientAccounts) {
                        mvpView?.hideProgress()
                        mvpView?.showLoanAccountDetails(getLoanAccountDetails(clientAccounts
                                .loanAccounts))
                        mvpView?.showSavingAccountDetails(getSavingAccountDetails(clientAccounts
                                .savingsAccounts))
                    }
                })?.let {
                    compositeDisposable.add(it
                    )
                }
    }

    /**
     * Fetches Details about Client from the server as [Client] and notifies the view to
     * display the details. And in case of any error during fetching the required details it
     * notifies the view.
     */
    val userDetails: Unit
        get() {
            checkViewAttached()
            dataManager.currentClient
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribeOn(Schedulers.io())
                    ?.subscribeWith(object : DisposableObserver<Client?>() {
                        override fun onComplete() {}
                        override fun onError(e: Throwable) {
                            mvpView?.showError(context?.getString(R.string.error_fetching_client))
                            mvpView?.hideProgress()
                        }

                        override fun onNext(client: Client) {
                            preferencesHelper.officeName = client.officeName
                            mvpView?.showUserDetails(client)
                        }
                    })?.let {
                        compositeDisposable.add(it
                        )
                    }
        }

    /**
     * Fetches Client image from the server in [Base64] format which is then decoded into a
     * [Bitmap] after which the view notified to display it.
     */
    val userImage: Unit
        get() {
            checkViewAttached()
            setUserProfile(preferencesHelper.userProfileImage)
            dataManager.clientImage
                    ?.observeOn(Schedulers.newThread())
                    ?.subscribeOn(Schedulers.io())
                    ?.subscribeWith(object : DisposableObserver<ResponseBody?>() {
                        override fun onComplete() {}
                        override fun onError(e: Throwable) {
                            mvpView?.showUserImage(null)
                        }

                        override fun onNext(response: ResponseBody) {
                            try {
                                val encodedString = response.string()
                                val pureBase64Encoded = encodedString.substring(encodedString.indexOf(',') + 1)
                                preferencesHelper.userProfileImage = pureBase64Encoded
                                setUserProfile(pureBase64Encoded)
                            } catch (e: IOException) {
                                Log.d("userimage", e.toString())
                            }
                        }
                    })?.let {
                        compositeDisposable.add(it
                        )
                    }
        }

    fun setUserProfile(image: String?) {
        if (image == null) {
            return
        }
        val decodedBytes = Base64.decode(image, Base64.DEFAULT)
        val decodedBitmap = instance?.compressImage(decodedBytes)
        mvpView?.showUserImage(decodedBitmap)
    }

    val unreadNotificationsCount: Unit
        get() {
            compositeDisposable.add(dataManager.unreadNotificationsCount
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.computation())
                    .subscribeWith(object : DisposableObserver<Int?>() {
                        override fun onComplete() {}
                        override fun onError(e: Throwable) {
                            mvpView?.showNotificationCount(0)
                        }

                        override fun onNext(integer: Int) {
                            mvpView?.showNotificationCount(integer)
                        }
                    }))
        }

    /**
     * Returns total Loan balance
     *
     * @param loanAccountList [List] of [LoanAccount] associated with the client
     * @return Returns `totalAmount` which is calculated by adding all [LoanAccount]
     * balance.
     */
    private fun getLoanAccountDetails(loanAccountList: List<LoanAccount>): Double {
        var totalAmount = 0.0
        for ((_, _, _, _, _, _, _, _, _, _, _, _, _, _, loanBalance) in loanAccountList) {
            totalAmount += loanBalance
        }
        return totalAmount
    }

    /**
     * Returns total Savings balance
     *
     * @param savingAccountList [List] of [SavingAccount] associated with the client
     * @return Returns `totalAmount` which is calculated by adding all [SavingAccount]
     * balance.
     */
    private fun getSavingAccountDetails(savingAccountList: List<SavingAccount>?): Double {
        var totalAmount = 0.0
        for ((_, _, _, _, _, accountBalance) in savingAccountList!!) {
            totalAmount += accountBalance
        }
        return totalAmount
    }

}