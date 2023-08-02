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
import org.mifos.mobile.models.client.Client
import org.mifos.mobile.models.notification.NotificationRegisterPayload
import org.mifos.mobile.models.notification.NotificationUserDetail
import org.mifos.mobile.presenters.UserDetailsPresenter
import org.mifos.mobile.repositories.HomeRepository
import org.mifos.mobile.repositories.UserDetailRepository
import org.mifos.mobile.utils.ImageUtil
import org.mifos.mobile.utils.UserDetailUiState
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val userDetailRepositoryImp: UserDetailRepository,
    private val homeRepositoryImp: HomeRepository
) : ViewModel() {

    @Inject
    lateinit var preferencesHelper: PreferencesHelper

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    private val _userDetailUiState = MutableLiveData<UserDetailUiState>()
    val userDetailUiState: LiveData<UserDetailUiState> = _userDetailUiState

    val userDetails: Unit
        get() {
            homeRepositoryImp.currentClient()?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.io())
                ?.subscribeWith(object : DisposableObserver<Client?>() {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                        _userDetailUiState.value =
                            UserDetailUiState.ShowError(R.string.error_fetching_client)
                    }

                    override fun onNext(client: Client) {
                        preferencesHelper.officeName = client.officeName
                        _userDetailUiState.value = UserDetailUiState.ShowUserDetails(client)
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
                        _userDetailUiState.postValue(UserDetailUiState.ShowUserImage(null))
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
        _userDetailUiState.postValue(UserDetailUiState.ShowUserImage(decodedBitmap))
    }

    fun registerNotification(token: String) {
        val payload = preferencesHelper.clientId?.let { NotificationRegisterPayload(it, token) }
        userDetailRepositoryImp.registerNotification(payload)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribeWith(object : DisposableObserver<ResponseBody?>() {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    Log.e(UserDetailsPresenter::class.java.simpleName, e.toString())
                    if (e is HttpException && e.code() == 500) {
                        payload?.let { getUserNotificationId(it, token) }
                    }
                }

                override fun onNext(responseBody: ResponseBody) {
                    preferencesHelper.setSentTokenToServer(true)
                    preferencesHelper.saveGcmToken(token)
                }
            })?.let { compositeDisposable.add(it) }
    }

    fun getUserNotificationId(payload: NotificationRegisterPayload, token: String) {
        preferencesHelper.clientId?.let { userDetailRepositoryImp.getUserNotificationId(it) }
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribeWith(object : DisposableObserver<NotificationUserDetail?>() {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    Log.e(UserDetailViewModel::class.java.simpleName, e.toString())
                }

                override fun onNext(userDetail: NotificationUserDetail) {
                    updateRegistrationNotification(userDetail.id.toLong(), payload, token)
                }
            })?.let { compositeDisposable.add(it) }
    }

    fun updateRegistrationNotification(
        id: Long,
        payload: NotificationRegisterPayload,
        token: String,
    ) {
        userDetailRepositoryImp.updateRegisterNotification(id, payload)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribeWith(object : DisposableObserver<ResponseBody?>() {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    Log.e(UserDetailViewModel::class.java.simpleName, e.toString())
                }

                override fun onNext(responseBody: ResponseBody) {
                    preferencesHelper.setSentTokenToServer(true)
                    preferencesHelper.saveGcmToken(token)
                }
            })?.let { compositeDisposable.add(it) }
    }

}