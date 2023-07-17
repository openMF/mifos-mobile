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
import org.mifos.mobile.models.Page
import org.mifos.mobile.models.Transaction
import org.mifos.mobile.repositories.RecentTransactionRepository
import org.mifos.mobile.utils.RecentTransactionUiState
import javax.inject.Inject

@HiltViewModel
class RecentTransactionViewModel @Inject constructor(private val recentTransactionRepositoryImp: RecentTransactionRepository) :
    ViewModel() {

    private val compositeDisposables: CompositeDisposable = CompositeDisposable()
    private val limit = 50
    private var loadmore = false

    private val _recentTransactionUiState = MutableLiveData<RecentTransactionUiState>()
    val recentTransactionUiState: LiveData<RecentTransactionUiState> = _recentTransactionUiState

    fun loadRecentTransactions(loadmore: Boolean, offset: Int) {
        this.loadmore = loadmore
        loadRecentTransactions(offset, limit)
    }

    private fun loadRecentTransactions(offset: Int, limit: Int) {
        _recentTransactionUiState.value = RecentTransactionUiState.Loading
        recentTransactionRepositoryImp.recentTransactions(offset, limit)
            ?.observeOn(  AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribeWith(object : DisposableObserver<Page<Transaction?>?>() {
                override fun onNext(transactions: Page<Transaction?>) {
                    if (transactions.totalFilteredRecords == 0) {
                        _recentTransactionUiState.value = RecentTransactionUiState.EmptyTransaction
                    } else if (loadmore && transactions.pageItems.isNotEmpty()) {
                       _recentTransactionUiState.value = RecentTransactionUiState.LoadMoreRecentTransactions(transactions.pageItems)
                    } else if (transactions.pageItems.isNotEmpty()) {
                        _recentTransactionUiState.value = RecentTransactionUiState.RecentTransactions(transactions.pageItems)
                    }
                }

                override fun onError(e: Throwable) {
                     _recentTransactionUiState.value = RecentTransactionUiState.Error(R.string.recent_transactions)
                }

                override fun onComplete() {}
            }).let {
                if (it != null) {
                    compositeDisposables.add(
                        it,
                    )
                }
            }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposables.clear()
    }
}