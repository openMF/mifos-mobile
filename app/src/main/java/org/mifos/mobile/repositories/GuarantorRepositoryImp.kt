package org.mifos.mobile.repositories

import io.reactivex.Observable
import okhttp3.ResponseBody
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.guarantor.GuarantorApplicationPayload
import org.mifos.mobile.models.guarantor.GuarantorPayload
import org.mifos.mobile.models.guarantor.GuarantorTemplatePayload
import retrofit2.Response
import javax.inject.Inject

class GuarantorRepositoryImp @Inject constructor(private val dataManager: DataManager) :
    GuarantorRepository {

    override suspend fun getGuarantorTemplate(loanId: Long?): Response<GuarantorTemplatePayload?>? {
        return dataManager.getGuarantorTemplate(loanId)
    }

    override suspend fun createGuarantor(
        loanId: Long?,
        payload: GuarantorApplicationPayload?
    ): Response<ResponseBody?>? {
        return dataManager.createGuarantor(loanId, payload)
    }

    override suspend fun updateGuarantor(
        payload: GuarantorApplicationPayload?,
        loanId: Long?,
        guarantorId: Long?
    ): Response<ResponseBody?>? {
        return dataManager.updateGuarantor(payload, loanId, guarantorId)
    }

    override suspend fun deleteGuarantor(loanId: Long?, guarantorId: Long?): Response<ResponseBody?>? {
        return dataManager.deleteGuarantor(loanId, guarantorId)
    }

    override suspend fun getGuarantorList(loanId: Long): Response<List<GuarantorPayload?>?>? {
        return dataManager.getGuarantorList(loanId)
    }
}