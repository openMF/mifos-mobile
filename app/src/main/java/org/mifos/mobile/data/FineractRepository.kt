package org.mifos.mobile.data

import android.database.Observable
import org.mifos.mobile.api.BaseApiManager
import org.mifos.mobile.models.entity.PartyIdentifiers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FineractRepository @Inject constructor(fineractApiManager: BaseApiManager) {
    private val fineractApiManager: BaseApiManager
    // for Payment-Hub related API calls
    fun getSecondaryIdentifiers(accountExternalId: String): Observable<PartyIdentifiers> {
        return fineractApiManager.getFineractPaymentHubApi()
                .fetchSecondaryIdentifiers(accountExternalId)
    }

    init {
        this.fineractApiManager = fineractApiManager
    }
}