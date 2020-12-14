package org.mifos.mobile.data

import io.reactivex.Observable
import org.mifos.mobile.api.BaseApiManager
import org.mifos.mobile.models.entity.PartyIdentifiers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FineractRepository @Inject constructor(fineractApiManager: BaseApiManager) {
    private val fineractApiManager: BaseApiManager

    fun getSecondaryIdentifiers(accountExternalId: String): Observable<PartyIdentifiers>? {
        return fineractApiManager.getFineractPaymentHubApi()?.fetchSecondaryIdentifiers(accountExternalId)
    }

    init {
        this.fineractApiManager = fineractApiManager
    }
}