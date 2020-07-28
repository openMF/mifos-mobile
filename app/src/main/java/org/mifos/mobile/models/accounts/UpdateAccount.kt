package org.mifos.mobile.models.accounts

import com.google.firebase.events.Subscriber
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.Response
import org.mifos.mobile.fineract.repository.FineractRepository
import org.mifos.mobile.models.accounts.savings.SavingsAccountUpdatePayload
import javax.inject.Inject

class UpdateAccount @Inject constructor(private val fineractRepository: FineractRepository) :
        UseCase<UpdateAccount.RequestValues, UpdateAccount.ResponseValue>() {

    override fun executeUseCase(requestValues: RequestValues) {

        fineractRepository.updateSavingsAccount(requestValues.accountId, requestValues.payload)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Subscriber<Response>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        useCaseCallback.onError(e.message)
                    }

                    override fun onNext(t: Response) {

                    }
                })

    }


    class RequestValues(val accountId: Long, val payload: SavingsAccountUpdatePayload)
        : UseCase.RequestValues

    class ResponseValue : UseCase.ResponseValue
}