package org.mifos.mobile.presenters

import android.content.Context

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

import okhttp3.ResponseBody

import org.mifos.mobile.R
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.injection.ApplicationContext
import org.mifos.mobile.presenters.base.BasePresenter
import org.mifos.mobile.ui.views.BeneficiaryDetailView

import javax.inject.Inject

/**
 * Created by dilpreet on 16/6/17.
 */
class BeneficiaryDetailPresenter @Inject constructor(private val manager: DataManager, @ApplicationContext context: Context?) :
        BasePresenter<BeneficiaryDetailView?>(context) {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    override fun detachView() {
        super.detachView()
        compositeDisposable.clear()
    }

    /**
     * Used to delete a Beneficiary with given `beneficiaryId` and notifies the view after
     * successful deletion of a beneficiary. And in case of any error during deletion, it notifies
     * the view.
     *
     * @param beneficiaryId Id of Beneficiary which you want to delete.
     */
    fun deleteBeneficiary(beneficiaryId: Long?) {
        checkViewAttached()
        mvpView?.showProgress()
        manager.deleteBeneficiary(beneficiaryId)
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.io())
                ?.subscribeWith(object : DisposableObserver<ResponseBody?>() {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                        mvpView?.hideProgress()
                        mvpView?.showError(context?.getString(R.string.error_deleting_beneficiary))
                    }

                    override fun onNext(responseBody: ResponseBody) {
                        mvpView?.hideProgress()
                        mvpView?.showBeneficiaryDeletedSuccessfully()
                    }
                })?.let { compositeDisposable.add(it) }
    }

}