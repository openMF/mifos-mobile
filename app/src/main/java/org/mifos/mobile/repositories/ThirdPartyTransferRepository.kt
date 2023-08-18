package org.mifos.mobile.repositories

import org.mifos.mobile.models.beneficiary.Beneficiary
import org.mifos.mobile.models.templates.account.AccountOptionsTemplate
import retrofit2.Response

interface ThirdPartyTransferRepository {

    suspend fun thirdPartyTransferTemplate(): Response<AccountOptionsTemplate?>?
    suspend fun beneficiaryList(): Response<List<Beneficiary?>?>?

}