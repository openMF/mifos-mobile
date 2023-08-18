package org.mifos.mobile.repositories

import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.beneficiary.Beneficiary
import org.mifos.mobile.models.templates.account.AccountOptionsTemplate
import retrofit2.Response
import javax.inject.Inject

class ThirdPartyTransferRepositoryImp @Inject constructor(private val dataManager: DataManager) :
    ThirdPartyTransferRepository {

    override suspend fun thirdPartyTransferTemplate(): Response<AccountOptionsTemplate?>? {
        return dataManager.thirdPartyTransferTemplate()
    }

    override suspend fun beneficiaryList(): Response<List<Beneficiary?>?>? {
        return dataManager.beneficiaryList()
    }


}