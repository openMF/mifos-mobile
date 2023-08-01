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
import org.mifos.mobile.repositories.GuarantorRepository
import org.mifos.mobile.utils.GuarantorUiState
import javax.inject.Inject

@HiltViewModel
class GuarantorDetailViewModel @Inject constructor(private val guarantorRepositoryImp: GuarantorRepository) :
    ViewModel() {

    private var compositeDisposables: CompositeDisposable = CompositeDisposable()

    private val _guarantorUiState = MutableLiveData<GuarantorUiState>()
    val guarantorUiState: LiveData<GuarantorUiState> get() = _guarantorUiState

    fun deleteGuarantor(loanId: Long?, guarantorId: Long?) {
        _guarantorUiState.value = GuarantorUiState.Loading
        guarantorRepositoryImp.deleteGuarantor(loanId, guarantorId)?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeWith(object : DisposableObserver<ResponseBody?>() {
                override fun onNext(responseBody: ResponseBody) {
                    _guarantorUiState.value =
                        GuarantorUiState.GuarantorDeletedSuccessfully(responseBody.string())
                }

                override fun onError(e: Throwable) {
                    _guarantorUiState.value = GuarantorUiState.ShowError(e.message)
                }

                override fun onComplete() {}
            })?.let { compositeDisposables.add(it) }

    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposables.clear()
    }

}