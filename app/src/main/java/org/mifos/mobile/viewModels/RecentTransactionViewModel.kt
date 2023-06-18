package org.mifos.mobile.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.mifos.mobile.R
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.Page
import org.mifos.mobile.models.Transaction
import org.mifos.mobile.utils.TransactionLoadingState
import javax.inject.Inject

class RecentTransactionViewModel @Inject constructor(
    private val dataManager: DataManager?
) : ViewModel() {

    private val compositeDisposables: CompositeDisposable = CompositeDisposable()
    private val limit = 50
    private var loadmore = false

    val transactionLoadingStateLiveData: MutableLiveData<TransactionLoadingState> =
        MutableLiveData()

    override fun onCleared() {
        super.onCleared()
        compositeDisposables.clear()
    }

    /**
     * Used to call function `loadRecentTransactions(int offset, int limit)` which is used for
     * fetching RecentTransaction from server.
     *
     * @param loadmore Set `false` if calling First time and `true` if you need to fetch
     * more [Transaction]
     * @param offset   Set `0` if calling first time or set length of `totalItemsCount`
     * if
     * you need to fetch more [Transaction]
     */
    fun loadRecentTransactions(loadmore: Boolean, offset: Int) {
        this.loadmore = loadmore
        loadRecentTransactions(offset, limit)
    }

    /**
     * Used to load List of [Transaction] from server depending upon the `offset` and
     * the max `limit` and notifies the view to display it. And in case of any
     * error during fetching the required details it notifies the view.
     *
     * @param offset Starting position for fetching the list of [Transaction]
     * @param limit  Maximum size of List of [Transaction] which is fetched from server
     */
    private fun loadRecentTransactions(offset: Int, limit: Int) {
        transactionLoadingStateLiveData.postValue(TransactionLoadingState.ShowProgress)
        dataManager?.getRecentTransactions(offset, limit)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribeWith(object : DisposableObserver<Page<Transaction?>?>() {
                override fun onComplete() {}

                override fun onError(e: Throwable) {
                    transactionLoadingStateLiveData.postValue(TransactionLoadingState.HideProgress)
                    transactionLoadingStateLiveData.postValue(
                        TransactionLoadingState.ErrorFetchingTransactions(
                            R.string.recent_transactions
                        )
                    )
                }

                override fun onNext(transactions: Page<Transaction?>) {
                    transactionLoadingStateLiveData.postValue(TransactionLoadingState.HideProgress)
                    if (transactions.totalFilteredRecords == 0) {
                        transactionLoadingStateLiveData.postValue(TransactionLoadingState.EmptyTransaction)
                    } else if (loadmore && transactions.pageItems.isNotEmpty()) {
                        transactionLoadingStateLiveData.postValue(
                            TransactionLoadingState.LoadMoreTransactions(transactions.pageItems as List<Transaction>)
                        )
                    } else if (transactions.pageItems.isNotEmpty()) {
                        transactionLoadingStateLiveData.postValue(
                            TransactionLoadingState.RecentTransactions(transactions.pageItems as List<Transaction>)
                        )
                    }
                }
            })?.let {
                compositeDisposables.add(it)
            }
    }
}

