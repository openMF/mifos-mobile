package org.mifos.mobile.presenters

import android.content.Context

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

import org.mifos.mobile.R
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.injection.ActivityContext
import org.mifos.mobile.models.notification.MifosNotification
import org.mifos.mobile.presenters.base.BasePresenter
import org.mifos.mobile.ui.views.NotificationView

import javax.inject.Inject

/**
 * Created by dilpreet on 14/9/17.
 */
class NotificationPresenter @Inject constructor(private val manager: DataManager, @ActivityContext context: Context) :
        BasePresenter<NotificationView?>(context) {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    override fun attachView(mvpView: NotificationView?) {
        super.attachView(mvpView)
    }

    override fun detachView() {
        super.detachView()
        compositeDisposable.clear()
    }

    fun loadNotifications() {
        checkViewAttached()
        mvpView?.showProgress()
        compositeDisposable.add(manager.notifications
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(object : DisposableObserver<List<MifosNotification?>?>() {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                        mvpView?.hideProgress()
                        mvpView?.showError(context
                                ?.getString(R.string.notification))
                    }

                    override fun onNext(notificationModels: List<MifosNotification?>) {
                        mvpView?.hideProgress()
                        mvpView?.showNotifications(notificationModels)
                    }
                }))
    }

}