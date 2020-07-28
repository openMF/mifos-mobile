package org.mifos.mobile.models.accounts

import com.google.firebase.events.Subscriber
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import org.mifos.mobile.fineract.repository.FineractRepository
import org.mifos.mobile.models.RegistrationEntity
import javax.inject.Inject

class RegisterSecondaryIdentifier @Inject constructor(
        private val paymentHubRepository: FineractRepository) :
        UseCase<RegisterSecondaryIdentifier.RequestValues, RegisterSecondaryIdentifier.ResponseValue>() {

    override fun executeUseCase(requestValues: RequestValues) {

        FineractRepository.registerSecondaryIdentifier(requestValues.entity)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Subscriber<ResponseBody>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) = useCaseCallback.onError(e.message)


                    override fun onNext(response: ResponseBody) =
                            useCaseCallback.onSuccess(ResponseValue())

                })
    }

    class RequestValues(val entity: RegistrationEntity) : UseCase.RequestValues

    class ResponseValue : UseCase.ResponseValue
}