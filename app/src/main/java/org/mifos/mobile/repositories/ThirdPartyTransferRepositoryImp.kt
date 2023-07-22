package org.mifos.mobile.repositories

import io.reactivex.Observable
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.beneficiary.Beneficiary
import org.mifos.mobile.models.templates.account.AccountOptionsTemplate
import javax.inject.Inject

class ThirdPartyTransferRepositoryImp @Inject constructor(private val dataManager: DataManager) :
    ThirdPartyTransferRepository {

    override fun thirdPartyTransferTemplate(): Observable<AccountOptionsTemplate?>? {
        return dataManager.thirdPartyTransferTemplate
    }

    override fun beneficiaryList(): Observable<List<Beneficiary?>?>? {
        return dataManager.beneficiaryList
    }


}