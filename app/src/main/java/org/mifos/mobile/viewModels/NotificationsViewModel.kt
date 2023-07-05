package org.mifos.mobile.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.mifos.mobile.R
import org.mifos.mobile.models.notification.MifosNotification
import org.mifos.mobile.repositories.NotificationRepository
import org.mifos.mobile.utils.NotificationsUiState
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(private val notificationRepositoryImp: NotificationRepository) :
    ViewModel() {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val _notificationUiState = MutableLiveData<NotificationsUiState>()
    val notificationUiState: LiveData<NotificationsUiState> get() = _notificationUiState


    fun loadNotifications() {
        _notificationUiState.value = NotificationsUiState.Loading
        notificationRepositoryImp.loadNotifications().observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribeWith(object : DisposableObserver<List<MifosNotification?>?>() {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    _notificationUiState.value = NotificationsUiState.Error(
                        R.string.notification
                    )
                }

                override fun onNext(notificationModels: List<MifosNotification?>) {
                    _notificationUiState.value = NotificationsUiState.Success(notificationModels)
                }
            })?.let { compositeDisposable.add(it) }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}