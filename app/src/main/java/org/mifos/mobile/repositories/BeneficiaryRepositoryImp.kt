package org.mifos.mobile.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.beneficiary.Beneficiary
import org.mifos.mobile.models.beneficiary.BeneficiaryPayload
import org.mifos.mobile.models.beneficiary.BeneficiaryUpdatePayload
import org.mifos.mobile.models.templates.beneficiary.BeneficiaryTemplate
import javax.inject.Inject

class BeneficiaryRepositoryImp @Inject constructor(private val dataManager: DataManager) :
    BeneficiaryRepository {

    override suspend fun beneficiaryTemplate(): Flow<BeneficiaryTemplate> {
        return flow {
            emit(dataManager.beneficiaryTemplate())
        }
    }

    override suspend fun createBeneficiary(beneficiaryPayload: BeneficiaryPayload?): Flow<ResponseBody> {
        return flow {
            emit(dataManager.createBeneficiary(beneficiaryPayload))
        }
    }

    override suspend fun updateBeneficiary(
        beneficiaryId: Long?,
        payload: BeneficiaryUpdatePayload?
    ): Flow<ResponseBody> {
        return flow {
            emit(dataManager.updateBeneficiary(beneficiaryId, payload))
        }
    }

    override suspend fun deleteBeneficiary(beneficiaryId: Long?): Flow<ResponseBody> {
        return flow {
            emit(dataManager.deleteBeneficiary(beneficiaryId))
        }
    }

    override suspend fun beneficiaryList(): Flow<List<Beneficiary>> {
        return flow {
            emit(dataManager.beneficiaryList())
        }
    }

}