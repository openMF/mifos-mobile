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
import org.mifos.mobile.models.guarantor.GuarantorApplicationPayload
import org.mifos.mobile.models.guarantor.GuarantorTemplatePayload
import org.mifos.mobile.repositories.GuarantorRepository
import org.mifos.mobile.ui.enums.GuarantorState
import org.mifos.mobile.utils.GuarantorUiState
import javax.inject.Inject

@HiltViewModel
class AddGuarantorViewModel @Inject constructor(private val guarantorRepositoryImp: GuarantorRepository) :
    ViewModel() {

    private var compositeDisposables: CompositeDisposable = CompositeDisposable()

    private val _guarantorUiState = MutableLiveData<GuarantorUiState>()
    val guarantorUiState: LiveData<GuarantorUiState> get() = _guarantorUiState

    fun getGuarantorTemplate(state: GuarantorState?, loanId: Long?) {
        _guarantorUiState.value = GuarantorUiState.Loading
        guarantorRepositoryImp.getGuarantorTemplate(loanId)?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeWith(object : DisposableObserver<GuarantorTemplatePayload?>() {
                override fun onNext(payload: GuarantorTemplatePayload) {
                    if (state === GuarantorState.CREATE) {
                        _guarantorUiState.value = GuarantorUiState.ShowGuarantorApplication(payload)
                    } else if (state === GuarantorState.UPDATE) {
                        _guarantorUiState.value = GuarantorUiState.ShowGuarantorUpdation(payload)
                    }
                }

                override fun onError(e: Throwable) {
                    _guarantorUiState.value = GuarantorUiState.ShowError(e.message)
                }

                override fun onComplete() {}
            })?.let { compositeDisposables.add(it) }
    }

    fun createGuarantor(loanId: Long?, payload: GuarantorApplicationPayload?) {
        _guarantorUiState.value = GuarantorUiState.Loading
        guarantorRepositoryImp.createGuarantor(loanId, payload)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeWith(object : DisposableObserver<ResponseBody?>() {
                override fun onNext(responseBody: ResponseBody) {
                    _guarantorUiState.value =
                        GuarantorUiState.SubmittedSuccessfully(responseBody.string(), payload)
                }

                override fun onError(e: Throwable) {
                    _guarantorUiState.value = GuarantorUiState.ShowError(e.message)
                }

                override fun onComplete() {}
            })?.let { compositeDisposables.add(it) }
    }

    fun updateGuarantor(
        payload: GuarantorApplicationPayload?,
        loanId: Long?,
        guarantorId: Long?,
    ) {
        _guarantorUiState.value = GuarantorUiState.Loading
        guarantorRepositoryImp.updateGuarantor(payload, loanId, guarantorId)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeWith(object : DisposableObserver<ResponseBody?>() {
                override fun onNext(responseBody: ResponseBody) {
                    _guarantorUiState.value =
                        GuarantorUiState.GuarantorUpdatedSuccessfully(responseBody.string())
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