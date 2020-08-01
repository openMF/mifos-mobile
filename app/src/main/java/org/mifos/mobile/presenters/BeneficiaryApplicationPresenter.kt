package org.mifos.mobile.presenters

import android.content.Context
import android.view.View

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

import okhttp3.ResponseBody

import org.mifos.mobile.R
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.injection.ApplicationContext
import org.mifos.mobile.models.beneficiary.BeneficiaryPayload
import org.mifos.mobile.models.beneficiary.BeneficiaryUpdatePayload
import org.mifos.mobile.models.templates.beneficiary.BeneficiaryTemplate
import org.mifos.mobile.presenters.base.BasePresenter
import org.mifos.mobile.ui.views.BeneficiaryApplicationView

import javax.inject.Inject

/**
 * Created by dilpreet on 16/6/17.
 */
class BeneficiaryApplicationPresenter @Inject constructor(
        private val dataManager: DataManager?,
        @ApplicationContext context: Context?
) : BasePresenter<BeneficiaryApplicationView?>(context) {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    override fun detachView() {
        super.detachView()
        compositeDisposable.clear()
    }

    /**
     * Loads BeneficiaryTemplate from the server and notifies the view to display it. And in case of
     * any error during fetching the required details it notifies the view.
     */
    fun loadBeneficiaryTemplate() {
        checkViewAttached()
        mvpView?.setVisibility(View.GONE)
        mvpView?.showProgress()
        dataManager?.beneficiaryTemplate
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.io())
                ?.subscribeWith(object : DisposableObserver<BeneficiaryTemplate?>() {
                    override fun onComplete() {
                        mvpView?.setVisibility(View.VISIBLE)
                    }

                    override fun onError(e: Throwable) {
                        mvpView?.hideProgress()
                        mvpView?.showError(context
                                ?.getString(R.string.error_fetching_beneficiary_template))
                    }

                    override fun onNext(beneficiaryTemplate: BeneficiaryTemplate) {
                        mvpView?.hideProgress()
                        mvpView?.showBeneficiaryTemplate(beneficiaryTemplate)
                    }
                })?.let { compositeDisposable.add(it) }
    }

    /**
     * Used to create a Beneficiary and notifies the view after successful creation of Beneficiary.
     * And in case of any error during creation, it notifies the view.
     *
     * @param payload [BeneficiaryPayload] used for creating a Beneficiary.
     */
    fun createBeneficiary(payload: BeneficiaryPayload?) {
        checkViewAttached()
        mvpView?.showProgress()
        dataManager?.createBeneficiary(payload)
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.io())
                ?.subscribeWith(object : DisposableObserver<ResponseBody?>() {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                        mvpView?.hideProgress()
                        mvpView?.showError(context
                                ?.getString(R.string.error_creating_beneficiary))
                    }

                    override fun onNext(responseBody: ResponseBody) {
                        mvpView?.hideProgress()
                        mvpView?.showBeneficiaryCreatedSuccessfully()
                    }
                })?.let { compositeDisposable.add(it) }
    }

    /**
     * Update a Beneficiary with provided `beneficiaryId` and notifies the view after
     * successful updation of Beneficiary. And in case of any error during updation, it notifies the
     * view.
     *
     * @param beneficiaryId Id of Beneficiary which you want to update
     * @param payload       [BeneficiaryPayload] used for updation a Beneficiary.
     */
    fun updateBeneficiary(beneficiaryId: Long?, payload: BeneficiaryUpdatePayload?) {
        checkViewAttached()
        mvpView?.showProgress()
        dataManager?.updateBeneficiary(beneficiaryId, payload)
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.io())
                ?.subscribeWith(object : DisposableObserver<ResponseBody?>() {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                        mvpView?.hideProgress()
                        mvpView?.showError(context
                                ?.getString(R.string.error_updating_beneficiary))
                    }

                    override fun onNext(responseBody: ResponseBody) {
                        mvpView?.hideProgress()
                        mvpView?.showBeneficiaryUpdatedSuccessfully()
                    }
                })?.let { compositeDisposable.add(it) }
    }

}