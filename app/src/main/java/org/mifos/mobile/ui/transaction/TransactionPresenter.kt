package org.mifos.mobile.ui.transaction

import android.content.Context
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.mifos.mobile.data.DataManagerTransaction
import org.mifos.mobile.injection.ApplicationContext
import org.mifos.mobile.models.entity.Transaction
import org.mifos.mobile.models.entity.TransactionInfo
import org.mifos.mobile.presenters.base.BasePresenter
import org.mifos.mobile.utils.Constants
import javax.inject.Inject

class TransactionPresenter @Inject
constructor(@ApplicationContext context: Context?, dataManagerTransaction: DataManagerTransaction?) :
        BasePresenter<TransactionContract.View>(context), TransactionContract.Presenter {
    var compositeDisposable: CompositeDisposable = CompositeDisposable()
    private var dataManagerTransaction: DataManagerTransaction? = dataManagerTransaction

    override fun attachView(mvpView: TransactionContract.View) {
        super.attachView(mvpView)
    }

    override fun detachView() {
        super.detachView()
        compositeDisposable.clear()
    }

    override fun makeTransaction(transaction: Transaction) {

        checkViewAttached()

        dataManagerTransaction?.transaction(transaction)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableObserver<TransactionInfo>() {
                    override fun onComplete() {}

                    override fun onNext(transactionInfo: TransactionInfo) {
                        mvpView?.showTransactionSuccessfully()
                    }

                    override fun onError(e: Throwable) {
                        mvpView?.showTransactionUnSuccessfully()
                    }
                }
                )?.let { compositeDisposable.add(it) }
    }
}