package org.mifos.mobile.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import org.mifos.mobile.repositories.TransferRepository
import org.mifos.mobile.ui.enums.TransferType
import org.mifos.mobile.utils.TransferUiState
import javax.inject.Inject

@HiltViewModel
class TransferProcessViewModel @Inject constructor(private val transferRepositoryImp: TransferRepository) :
    ViewModel() {

    private val compositeDisposables: CompositeDisposable = CompositeDisposable()

    private val _transferUiState = MutableLiveData<TransferUiState>()
    val transferUiState: LiveData<TransferUiState> get() = _transferUiState

    fun makeTransfer(
        fromOfficeId: Int?,
        fromClientId: Long?,
        fromAccountType: Int?,
        fromAccountId: Int?,
        toOfficeId: Int?,
        toClientId: Long?,
        toAccountType: Int?,
        toAccountId: Int?,
        transferDate: String?,
        transferAmount: Double?,
        transferDescription: String?,
        dateFormat: String = "dd MMMM yyyy",
        locale: String = "en",
        fromAccountNumber: String?,
        toAccountNumber: String?,
        transferType: TransferType?
    ) {
        _transferUiState.value = TransferUiState.Loading
        transferRepositoryImp.makeTransfer(
            fromOfficeId,
            fromClientId,
            fromAccountType,
            fromAccountId,
            toOfficeId,
            toClientId,
            toAccountType,
            toAccountId,
            transferDate,
            transferAmount,
            transferDescription,
            dateFormat,
            locale,
            fromAccountNumber,
            toAccountNumber,
            transferType
        )?.observeOn(AndroidSchedulers.mainThread())?.subscribeOn(Schedulers.io())
            ?.subscribeWith(object : DisposableObserver<ResponseBody?>() {
                override fun onNext(responseBody: ResponseBody) {
                    _transferUiState.value = TransferUiState.TransferSuccess
                }

                override fun onError(e: Throwable) {
                    _transferUiState.value = TransferUiState.Error(e)
                }

                override fun onComplete() {}

            }).let { it?.let { it1 -> compositeDisposables.add(it1) } }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposables.clear()
    }
}