package org.mifos.mobile.models.accounts

import com.google.firebase.events.Subscriber
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.mifos.mobile.fineract.repository.FineractRepository
import org.mifos.mobile.models.PartyIdentifiers
import org.mifos.mobile.models.client.Identifier
import org.mifos.mobile.utils.Constants
import javax.inject.Inject

class FetchSecondaryIdentifiers @Inject constructor(
        private val fineractRepository: FineractRepository) :
        UseCase<FetchSecondaryIdentifiers.RequestValues, FetchSecondaryIdentifiers.ResponseValue>() {

    override fun executeUseCase(requestValues: RequestValues) {

        fineractRepository.getSecondaryIdentifiers(requestValues.accountExternalId)
                .subscribeOn(Schedulers.io())
                .subscribe(object : Subscriber<PartyIdentifiers>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) = useCaseCallback.onError(e.message)

                    override fun onNext(response: PartyIdentifiers) {
                        response.identifierList?.let {
                            useCaseCallback.onSuccess(ResponseValue(it))
                        } ?: useCaseCallback.onError(Constants.NO_IDENTIFIERS_FOUND)
                    }

                })
    }

    class RequestValues(val accountExternalId: String) : UseCase.RequestValues

    class ResponseValue(val identifierList: List<Identifier>) : UseCase.ResponseValue
}