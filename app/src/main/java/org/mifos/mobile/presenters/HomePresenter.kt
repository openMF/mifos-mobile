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
import org.mifos.mobile.presenters.base.BasePresenter
import org.mifos.mobile.ui.views.HomeView
import org.mifos.mobile.utils.ImageUtil

import java.io.IOException
import javax.inject.Inject

/**
 * Created by dilpreet on 19/6/17.
 */
class HomePresenter @Inject constructor(private val dataManager: DataManager, @ApplicationContext context: Context?) : BasePresenter<HomeView?>(context) {
    private val compositeDisposable: CompositeDisposable? = CompositeDisposable()

    @JvmField
    @set:Inject
    var preferencesHelper: PreferencesHelper? = null
    override fun attachView(mvpView: HomeView?) {
        super.attachView(mvpView)
    }

    override fun detachView() {
        super.detachView()
        compositeDisposable?.clear()
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
                            mvpView?.hideProgress()
                            preferencesHelper?.officeName = client.officeName
                            preferencesHelper?.clientName = client.displayName
                            mvpView?.showUserDetails(preferencesHelper?.clientName)
                        }
                    })?.let {
                        compositeDisposable?.add(it
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
            dataManager.clientImage
                    ?.observeOn(Schedulers.newThread())
                    ?.subscribeOn(Schedulers.io())
                    ?.subscribeWith(object : DisposableObserver<ResponseBody?>() {
                        override fun onComplete() {}
                        override fun onError(e: Throwable) {
                            mvpView?.showUserImageNotFound()
                        }

                        override fun onNext(response: ResponseBody) {
                            try {
                                val encodedString = response.string()
                                val pureBase64Encoded = encodedString.substring(encodedString.indexOf(',') + 1)
                                val decodedBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT)
                                val decodedBitmap = ImageUtil.instance?.compressImage(decodedBytes, 256f, 256f)
                                mvpView?.showUserImage(decodedBitmap)
                            } catch (e: IOException) {
                                Log.d("userimage", e.toString())
                            }
                        }
                    })?.let {
                        compositeDisposable?.add(it
                        )
                    }
        }
    val unreadNotificationsCount: Unit
        get() {
            compositeDisposable?.add(dataManager.unreadNotificationsCount
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.computation())
                    .subscribeWith(object : DisposableObserver<Int?>() {
                        override fun onComplete() {}
                        override fun onError(e: Throwable) {}
                        override fun onNext(integer: Int) {
                            mvpView?.showNotificationCount(integer)
                        }
                    }))
        }

}