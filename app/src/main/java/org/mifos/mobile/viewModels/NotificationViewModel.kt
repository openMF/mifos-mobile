package org.mifos.mobile.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.mifos.mobile.utils.NotificationUiState
import org.mifos.mobile.models.notification.MifosNotification
import org.mifos.mobile.repositories.NotificationRepository
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(private val notificationRepositoryImp: NotificationRepository): ViewModel() {
    private val compositeDisposables: CompositeDisposable = CompositeDisposable()

    private val _notificationUiState = MutableLiveData<NotificationUiState>()
    val notificationUiState : LiveData<NotificationUiState> get() = _notificationUiState

    fun loadNotifications() {
        _notificationUiState.value = NotificationUiState.Loading
        notificationRepositoryImp.loadNotifications()
            .observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribeWith(object : DisposableObserver<List<MifosNotification?>?>() {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    _notificationUiState.value = NotificationUiState.Error
                }
                override fun onNext(notificationModels: List<MifosNotification?>) {
                    _notificationUiState.value = NotificationUiState.LoadNotificationsSuccessful(notificationModels)
                }
            })?.let { compositeDisposables.add(it) }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposables.clear()
    }
}
