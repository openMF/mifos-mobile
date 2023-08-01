package org.mifos.mobile.viewModels

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import org.mifos.mobile.R
import org.mifos.mobile.models.beneficiary.Beneficiary
import org.mifos.mobile.models.beneficiary.BeneficiaryPayload
import org.mifos.mobile.models.beneficiary.BeneficiaryUpdatePayload
import org.mifos.mobile.models.templates.beneficiary.BeneficiaryTemplate
import org.mifos.mobile.repositories.BeneficiaryRepository
import org.mifos.mobile.utils.BeneficiaryUiState
import javax.inject.Inject

@HiltViewModel
class BeneficiaryApplicationViewModel @Inject constructor(private val beneficiaryRepositoryImp: BeneficiaryRepository) :
    ViewModel() {

    private val compositeDisposables: CompositeDisposable = CompositeDisposable()

    private val _beneficiaryUiState = MutableLiveData<BeneficiaryUiState>()
    val beneficiaryUiState: LiveData<BeneficiaryUiState> get() = _beneficiaryUiState

    fun loadBeneficiaryTemplate() {
        _beneficiaryUiState.value = BeneficiaryUiState.Loading
        beneficiaryRepositoryImp.beneficiaryTemplate()
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribeWith(object : DisposableObserver<BeneficiaryTemplate?>() {
                override fun onComplete() {
                    _beneficiaryUiState.value = BeneficiaryUiState.SetVisibility(View.VISIBLE)
                }

                override fun onError(e: Throwable) {
                    _beneficiaryUiState.value =
                        BeneficiaryUiState.ShowError(R.string.error_fetching_beneficiary_template)
                }

                override fun onNext(beneficiaryTemplate: BeneficiaryTemplate) {
                    _beneficiaryUiState.value =
                        BeneficiaryUiState.ShowBeneficiaryTemplate(beneficiaryTemplate)
                }
            })?.let { compositeDisposables.add(it) }
    }

    fun createBeneficiary(payload: BeneficiaryPayload?) {
        _beneficiaryUiState.value = BeneficiaryUiState.Loading
        beneficiaryRepositoryImp.createBeneficiary(payload)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribeWith(object : DisposableObserver<ResponseBody?>() {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    _beneficiaryUiState.value =
                        BeneficiaryUiState.ShowError(R.string.error_creating_beneficiary)
                }

                override fun onNext(responseBody: ResponseBody) {
                    _beneficiaryUiState.value = BeneficiaryUiState.CreatedSuccessfully
                }
            })?.let { compositeDisposables.add(it) }

    }

    fun updateBeneficiary(beneficiaryId: Long?, payload: BeneficiaryUpdatePayload?) {
        _beneficiaryUiState.value = BeneficiaryUiState.Loading
        beneficiaryRepositoryImp.updateBeneficiary(beneficiaryId, payload)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribeWith(object : DisposableObserver<ResponseBody?>() {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    _beneficiaryUiState.value =
                        BeneficiaryUiState.ShowError(R.string.error_updating_beneficiary)
                }

                override fun onNext(responseBody: ResponseBody) {
                    _beneficiaryUiState.value = BeneficiaryUiState.UpdatedSuccessfully
                }
            })?.let { compositeDisposables.add(it) }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposables.clear()
    }
}