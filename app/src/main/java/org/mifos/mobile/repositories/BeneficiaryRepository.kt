package org.mifos.mobile.repositories

import okhttp3.ResponseBody
import org.mifos.mobile.models.beneficiary.Beneficiary
import org.mifos.mobile.models.beneficiary.BeneficiaryPayload
import org.mifos.mobile.models.beneficiary.BeneficiaryUpdatePayload
import org.mifos.mobile.models.templates.beneficiary.BeneficiaryTemplate
import retrofit2.Response

interface BeneficiaryRepository {

    suspend fun beneficiaryTemplate(): Response<BeneficiaryTemplate?>?

    suspend fun createBeneficiary(beneficiaryPayload: BeneficiaryPayload?): Response<ResponseBody?>?

    suspend fun updateBeneficiary(
        beneficiaryId: Long?,
        payload: BeneficiaryUpdatePayload?,
    ): Response<ResponseBody?>?

    suspend fun deleteBeneficiary(beneficiaryId: Long?): Response<ResponseBody?>?

    suspend fun beneficiaryList(): Response<List<Beneficiary?>?>?

}