package org.mifos.mobile.repositories

import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import org.mifos.mobile.models.beneficiary.Beneficiary
import org.mifos.mobile.models.beneficiary.BeneficiaryPayload
import org.mifos.mobile.models.beneficiary.BeneficiaryUpdatePayload
import org.mifos.mobile.models.templates.beneficiary.BeneficiaryTemplate

interface BeneficiaryRepository {

    suspend fun beneficiaryTemplate(): Flow<BeneficiaryTemplate>

    suspend fun createBeneficiary(beneficiaryPayload: BeneficiaryPayload?): Flow<ResponseBody>

    suspend fun updateBeneficiary(
        beneficiaryId: Long?,
        payload: BeneficiaryUpdatePayload?,
    ): Flow<ResponseBody>

    suspend fun deleteBeneficiary(beneficiaryId: Long?): Flow<ResponseBody>

    suspend fun beneficiaryList(): Flow<List<Beneficiary>>

}