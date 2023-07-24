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
import org.mifos.mobile.models.beneficiary.Beneficiary
import org.mifos.mobile.repositories.BeneficiaryRepository
import org.mifos.mobile.utils.BeneficiaryUiState
import javax.inject.Inject

@HiltViewModel
class BeneficiaryListViewModel @Inject constructor(private val beneficiaryRepositoryImp: BeneficiaryRepository) :
    ViewModel() {

    private val compositeDisposables: CompositeDisposable = CompositeDisposable()

    private val _beneficiaryUiState = MutableLiveData<BeneficiaryUiState>()
    val beneficiaryUiState: LiveData<BeneficiaryUiState> get() = _beneficiaryUiState

    fun loadBeneficiaries() {
        _beneficiaryUiState.value = BeneficiaryUiState.Loading
        beneficiaryRepositoryImp.beneficiaryList()
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribeWith(object : DisposableObserver<List<Beneficiary?>?>() {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    _beneficiaryUiState.value = BeneficiaryUiState.ShowError(R.string.beneficiaries)
                }

                override fun onNext(beneficiaries: List<Beneficiary?>) {
                    _beneficiaryUiState.value =
                        BeneficiaryUiState.ShowBeneficiaryList(beneficiaries)
                }
            })?.let { compositeDisposables.add(it) }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposables.clear()
    }
}