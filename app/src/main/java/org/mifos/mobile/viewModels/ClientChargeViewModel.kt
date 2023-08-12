package org.mifos.mobile.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.mifos.mobile.R
import org.mifos.mobile.models.Charge
import org.mifos.mobile.models.Page
import org.mifos.mobile.repositories.ClientChargeRepository
import org.mifos.mobile.utils.ClientChargeUiState
import javax.inject.Inject

@HiltViewModel
class ClientChargeViewModel @Inject constructor(private val clientChargeRepositoryImp: ClientChargeRepository) :
    ViewModel() {

    private val compositeDisposables: CompositeDisposable = CompositeDisposable()

    private val _clientChargeUiState = MutableLiveData<ClientChargeUiState>()
    val clientChargeUiState get() = _clientChargeUiState

    fun loadClientCharges(clientId: Long) {
        _clientChargeUiState.value = ClientChargeUiState.Loading
        clientChargeRepositoryImp.getClientCharges(clientId)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribeWith(object : DisposableObserver<Page<Charge?>?>() {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    _clientChargeUiState.value =
                        ClientChargeUiState.ShowError(R.string.client_charges)
                }

                override fun onNext(chargePage: Page<Charge?>) {
                    _clientChargeUiState.value =
                        ClientChargeUiState.ShowClientCharges(chargePage.pageItems)
                }
            })?.let {
                compositeDisposables.add(
                    it,
                )
            }
    }

    fun loadLoanAccountCharges(loanId: Long) {
        _clientChargeUiState.value = ClientChargeUiState.Loading
        clientChargeRepositoryImp.getLoanCharges(loanId)?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribeWith(object : DisposableObserver<List<Charge?>?>() {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    _clientChargeUiState.value =
                        ClientChargeUiState.ShowError(R.string.client_charges)
                }

                override fun onNext(chargeList: List<Charge?>) {
                    _clientChargeUiState.value = ClientChargeUiState.ShowClientCharges(chargeList)
                }
            })?.let {
                compositeDisposables.add(
                    it,
                )
            }
    }

    fun loadSavingsAccountCharges(savingsId: Long) {
        _clientChargeUiState.value = ClientChargeUiState.Loading
        clientChargeRepositoryImp.getSavingsCharges(savingsId)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribeWith(object : DisposableObserver<List<Charge?>?>() {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    _clientChargeUiState.value =
                        ClientChargeUiState.ShowError(R.string.client_charges)
                }

                override fun onNext(chargeList: List<Charge?>) {
                    _clientChargeUiState.value = ClientChargeUiState.ShowClientCharges(chargeList)
                }
            })?.let {
                compositeDisposables.add(
                    it,
                )
            }
    }

    fun loadClientLocalCharges() {
        _clientChargeUiState.value = ClientChargeUiState.Loading
        clientChargeRepositoryImp.clientLocalCharges()
            .observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribeWith(object : DisposableObserver<Page<Charge?>?>() {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    _clientChargeUiState.value =
                        ClientChargeUiState.ShowError(R.string.client_charges)
                }

                override fun onNext(chargePage: Page<Charge?>) {
                    _clientChargeUiState.value =
                        ClientChargeUiState.ShowClientCharges(chargePage.pageItems)
                }
            })?.let {
                compositeDisposables.add(
                    it,
                )
            }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposables.clear()
    }

}