package org.mifos.mobile.repositories

import io.reactivex.Observable
import org.mifos.mobile.models.beneficiary.Beneficiary
import org.mifos.mobile.models.templates.account.AccountOptionsTemplate

interface ThirdPartyTransferRepository {

    fun thirdPartyTransferTemplate(): Observable<AccountOptionsTemplate?>?
    fun beneficiaryList(): Observable<List<Beneficiary?>?>?

}