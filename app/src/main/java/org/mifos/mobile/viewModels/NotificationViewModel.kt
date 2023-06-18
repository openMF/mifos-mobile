package org.mifos.mobile.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.notification.MifosNotification
import javax.inject.Inject

class NotificationViewModel @Inject constructor(private val dataManager : DataManager?): ViewModel() {
    private val compositeDisposables: CompositeDisposable = CompositeDisposable()

    private var loadNotificationsResultSuccess = MutableLiveData<Boolean>()
    val readLoadNotificationsResultSuccess : LiveData<Boolean> get() = loadNotificationsResultSuccess

    private var loadedNotifications : List<MifosNotification?>? = null
    val readLoadedNotifications : List<MifosNotification?> get() = loadedNotifications!!

    fun loadNotifications() {
        dataManager?.notifications
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribeWith(object : DisposableObserver<List<MifosNotification?>?>() {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    loadNotificationsResultSuccess.value = false
                }
                override fun onNext(notificationModels: List<MifosNotification?>) {
                    loadedNotifications = notificationModels
                    loadNotificationsResultSuccess.value = true
                }
            })?.let { compositeDisposables.add(it) }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposables.clear()
    }
}
