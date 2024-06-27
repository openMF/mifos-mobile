package org.mifos.mobile.core.data.repositories

import org.mifos.mobile.core.network.DataManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import org.mifos.mobile.core.model.entity.guarantor.GuarantorApplicationPayload
import org.mifos.mobile.core.model.entity.guarantor.GuarantorPayload
import org.mifos.mobile.core.model.entity.guarantor.GuarantorTemplatePayload
import javax.inject.Inject

class GuarantorRepositoryImp @Inject constructor(private val dataManager: DataManager) :
   GuarantorRepository {

    override fun getGuarantorTemplate(loanId: Long?): Flow<GuarantorTemplatePayload?> {
        return flow {
            emit(dataManager.getGuarantorTemplate(loanId))
        }
    }

    override fun createGuarantor(
        loanId: Long?,
        payload: GuarantorApplicationPayload?
    ): Flow<ResponseBody?> {
        return flow {
            emit(dataManager.createGuarantor(loanId, payload))
        }
    }

    override fun updateGuarantor(
        payload: GuarantorApplicationPayload?,
        loanId: Long?,
        guarantorId: Long?
    ): Flow<ResponseBody?> {
        return flow {
            emit(dataManager.updateGuarantor(payload, loanId, guarantorId))
        }
    }

    override fun deleteGuarantor(loanId: Long?, guarantorId: Long?): Flow<ResponseBody?> {
        return flow {
            emit(dataManager.deleteGuarantor(loanId, guarantorId))
        }
    }

    override fun getGuarantorList(loanId: Long): Flow<List<GuarantorPayload?>?> {
        return flow {
            emit(dataManager.getGuarantorList(loanId))
        }
    }
}