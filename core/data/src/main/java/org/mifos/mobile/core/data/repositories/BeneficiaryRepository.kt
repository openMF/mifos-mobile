package org.mifos.mobile.core.data.repositories

import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.model.entity.beneficiary.BeneficiaryPayload
import org.mifos.mobile.core.model.entity.beneficiary.BeneficiaryUpdatePayload
import org.mifos.mobile.core.model.entity.templates.beneficiary.BeneficiaryTemplate

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