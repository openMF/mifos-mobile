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
import org.mifos.mobile.injection.ApplicationContext
import org.mifos.mobile.models.client.Client
import org.mifos.mobile.models.notification.NotificationRegisterPayload
import org.mifos.mobile.models.notification.NotificationUserDetail
import org.mifos.mobile.presenters.base.BasePresenter
import org.mifos.mobile.ui.views.UserDetailsView
import org.mifos.mobile.utils.ImageUtil.Companion.instance

import retrofit2.HttpException

import java.io.IOException

import javax.inject.Inject

/**
 * Created by naman on 07/04/17.
 */
class UserDetailsPresenter @Inject constructor(
        @ApplicationContext context: Context, private val dataManager: DataManager,
        private val preferencesHelper: PreferencesHelper
) : BasePresenter<UserDetailsView?>(context) {

    private val compositeDisposables: CompositeDisposable = CompositeDisposable()
    override fun attachView(mvpView: UserDetailsView?) {
        super.attachView(mvpView)
    }

    override fun detachView() {
        super.detachView()
        compositeDisposables.clear()
    }

    /**
     * Fetches Details about Client from the server as [Client] and notifies the view to
     * display the details. And in case of any error during fetching the required details it
     * notifies the view.
     */
    val userDetails: Unit
        get() {
            checkViewAttached()
            mvpView?.showProgress()
            dataManager.currentClient
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribeOn(Schedulers.io())
                    ?.subscribeWith(object : DisposableObserver<Client?>() {
                        override fun onComplete() {}
                        override fun onError(e: Throwable) {
                            mvpView?.hideProgress()
                            mvpView?.showError(context?.getString(R.string.error_fetching_client))
                        }

                        override fun onNext(client: Client) {
                            mvpView?.hideProgress()
                            mvpView?.showUserDetails(client)
                        }
                    })?.let {
                        compositeDisposables.add(it
                        )
                    }
        }//removing 'data:image/jpg;base64' from the response
    //the response is of the form of 'data:image/jpg;base64, .....'
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

                                //removing 'data:image/jpg;base64' from the response
                                //the response is of the form of 'data:image/jpg;base64, .....'
                                val pureBase64Encoded = encodedString.substring(encodedString.indexOf(',') + 1)
                                preferencesHelper.userProfileImage = pureBase64Encoded
                                setUserProfile(pureBase64Encoded)
                            } catch (e: IOException) {
                                Log.e("userimage", e.message)
                            }
                        }
                    })?.let {
                        compositeDisposables.add(it
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

    fun registerNotification(token: String) {
        checkViewAttached()
        val payload = NotificationRegisterPayload(preferencesHelper.clientId!!, token)
        dataManager.registerNotification(payload)
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.io())
                ?.subscribeWith(object : DisposableObserver<ResponseBody?>() {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                        Log.e(UserDetailsPresenter::class.java.simpleName, e.toString())
                        if (e is HttpException && e.code() == 500) {
                            getUserNotificationId(payload, token)
                        }
                    }

                    override fun onNext(responseBody: ResponseBody) {
                        preferencesHelper.setSentTokenToServer(true)
                        preferencesHelper.saveGcmToken(token)
                    }
                })?.let { compositeDisposables.add(it) }
    }

    private fun getUserNotificationId(payload: NotificationRegisterPayload, token: String) {
        checkViewAttached()
        dataManager.getUserNotificationId(preferencesHelper.clientId!!)
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.io())
                ?.subscribeWith(object : DisposableObserver<NotificationUserDetail?>() {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                        Log.e(UserDetailsPresenter::class.java.simpleName, e.toString())
                    }

                    override fun onNext(userDetail: NotificationUserDetail) {
                        updateRegistrationNotification(userDetail.id.toLong(), payload, token)
                    }
                })?.let { compositeDisposables.add(it) }
    }

    private fun updateRegistrationNotification(
            id: Long, payload: NotificationRegisterPayload,
            token: String
    ) {
        checkViewAttached()
        dataManager.updateRegisterNotification(id, payload)
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.io())
                ?.subscribeWith(object : DisposableObserver<ResponseBody?>() {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                        Log.e(UserDetailsPresenter::class.java.simpleName, e.toString())
                    }

                    override fun onNext(responseBody: ResponseBody) {
                        preferencesHelper.setSentTokenToServer(true)
                        preferencesHelper.saveGcmToken(token)
                    }
                })?.let { compositeDisposables.add(it) }
    }

}