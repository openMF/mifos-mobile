package org.mifos.mobile.viewModels

import android.util.Base64
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import org.mifos.mobile.R
import org.mifos.mobile.api.local.PreferencesHelper
import org.mifos.mobile.models.accounts.loan.LoanAccount
import org.mifos.mobile.models.accounts.savings.SavingAccount
import org.mifos.mobile.models.client.Client
import org.mifos.mobile.models.client.ClientAccounts
import org.mifos.mobile.repositories.HomeRepository
import org.mifos.mobile.utils.HomeUiState
import org.mifos.mobile.utils.ImageUtil
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val homeRepositoryImp: HomeRepository) :
    ViewModel() {

    @Inject
    lateinit var preferencesHelper: PreferencesHelper

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    private val _homeUiState = MutableLiveData<HomeUiState>()
    val homeUiState: LiveData<HomeUiState> = _homeUiState

    fun loadClientAccountDetails() {
        _homeUiState.value = HomeUiState.Loading
        homeRepositoryImp.clientAccounts()?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribeWith(object : DisposableObserver<ClientAccounts?>() {
                override fun onNext(clientAccounts: ClientAccounts) {
                    _homeUiState.value = HomeUiState.ClientAccountDetails(
                        getLoanAccountDetails(clientAccounts.loanAccounts),
                        getSavingAccountDetails(clientAccounts.savingsAccounts)
                    )
                }

                override fun onError(e: Throwable) {
                    _homeUiState.value = HomeUiState.Error(R.string.error_fetching_accounts)
                }

                override fun onComplete() {}

            })?.let { compositeDisposable.add(it) }
    }

    val userDetails: Unit
        get() {
            homeRepositoryImp.currentClient()?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.io())
                ?.subscribeWith(object : DisposableObserver<Client?>() {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                        _homeUiState.value = HomeUiState.Error(R.string.error_fetching_client)
                    }

                    override fun onNext(client: Client) {
                        preferencesHelper.officeName = client.officeName
                        _homeUiState.value = HomeUiState.UserDetails(client)
                    }
                })?.let {
                    compositeDisposable.add(
                        it,
                    )
                }

        }

    val userImage: Unit
        get() {
            setUserProfile(preferencesHelper.userProfileImage)
            homeRepositoryImp.clientImage()?.observeOn(Schedulers.newThread())
                ?.subscribeOn(Schedulers.io())
                ?.subscribeWith(object : DisposableObserver<ResponseBody?>() {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                        _homeUiState.postValue(HomeUiState.UserImage(null))
                    }

                    override fun onNext(response: ResponseBody) {
                        try {
                            val encodedString = response.string()
                            val pureBase64Encoded =
                                encodedString.substring(encodedString.indexOf(',') + 1)
                            preferencesHelper.userProfileImage = pureBase64Encoded
                            setUserProfile(pureBase64Encoded)
                        } catch (e: IOException) {
                            Log.d("userimage", e.toString())
                        }
                    }
                })?.let {
                    compositeDisposable.add(
                        it,
                    )
                }
        }

    fun setUserProfile(image: String?) {
        if (image == null) {
            return
        }
        val decodedBytes = Base64.decode(image, Base64.DEFAULT)
        val decodedBitmap = ImageUtil.instance?.compressImage(decodedBytes)
        _homeUiState.postValue(HomeUiState.UserImage(decodedBitmap))
    }

    val unreadNotificationsCount: Unit
        get() {
            homeRepositoryImp.unreadNotificationsCount().observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.computation())
                ?.subscribeWith(object : DisposableObserver<Int?>() {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                        _homeUiState.value = HomeUiState.UnreadNotificationsCount(0)
                    }

                    override fun onNext(integer: Int) {
                        _homeUiState.value = HomeUiState.UnreadNotificationsCount(integer)
                    }
                })?.let { compositeDisposable.add(it) }
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

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}