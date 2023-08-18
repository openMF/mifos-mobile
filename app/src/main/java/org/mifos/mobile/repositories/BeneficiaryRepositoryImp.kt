package org.mifos.mobile.repositories

import okhttp3.ResponseBody
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.beneficiary.Beneficiary
import org.mifos.mobile.models.beneficiary.BeneficiaryPayload
import org.mifos.mobile.models.beneficiary.BeneficiaryUpdatePayload
import org.mifos.mobile.models.templates.beneficiary.BeneficiaryTemplate
import retrofit2.Response
import javax.inject.Inject

class BeneficiaryRepositoryImp @Inject constructor(private val dataManager: DataManager) :
    BeneficiaryRepository {

    override suspend fun beneficiaryTemplate(): Response<BeneficiaryTemplate?>? {
        return dataManager.beneficiaryTemplate()
    }

    override suspend fun createBeneficiary(beneficiaryPayload: BeneficiaryPayload?): Response<ResponseBody?>? {
        return dataManager.createBeneficiary(beneficiaryPayload)
    }

    override suspend fun updateBeneficiary(
        beneficiaryId: Long?,
        payload: BeneficiaryUpdatePayload?
    ): Response<ResponseBody?>? {
        return dataManager.updateBeneficiary(beneficiaryId, payload)
    }

    override suspend fun deleteBeneficiary(beneficiaryId: Long?): Response<ResponseBody?>? {
        return dataManager.deleteBeneficiary(beneficiaryId)
    }

    override suspend fun beneficiaryList(): Response<List<Beneficiary?>?>? {
        return dataManager.beneficiaryList()
    }

}