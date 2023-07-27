package org.mifos.mobile.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.mifos.mobile.models.guarantor.GuarantorPayload
import org.mifos.mobile.repositories.GuarantorRepository
import org.mifos.mobile.utils.GuarantorUiState
import javax.inject.Inject

@HiltViewModel
class GuarantorListViewModel @Inject constructor(private val guarantorRepositoryImp: GuarantorRepository) :
    ViewModel() {

    private var compositeDisposables: CompositeDisposable = CompositeDisposable()

    private val _guarantorUiState = MutableLiveData<GuarantorUiState>()
    val guarantorUiState: LiveData<GuarantorUiState> get() = _guarantorUiState

    fun getGuarantorList(loanId: Long) {
        _guarantorUiState.value = GuarantorUiState.Loading
        guarantorRepositoryImp.getGuarantorList(loanId)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeWith(object : DisposableObserver<List<GuarantorPayload?>?>() {
                override fun onNext(payload: List<GuarantorPayload?>) {
                    _guarantorUiState.value =
                        GuarantorUiState.ShowGuarantorListSuccessfully(payload)
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