package org.mifos.mobile.repositories

import okhttp3.ResponseBody
import org.mifos.mobile.models.guarantor.GuarantorApplicationPayload
import org.mifos.mobile.models.guarantor.GuarantorPayload
import org.mifos.mobile.models.guarantor.GuarantorTemplatePayload
import retrofit2.Response

interface GuarantorRepository {

    suspend fun getGuarantorTemplate(loanId: Long?): Response<GuarantorTemplatePayload?>?

    suspend fun createGuarantor(
        loanId: Long?,
        payload: GuarantorApplicationPayload?,
    ): Response<ResponseBody?>?

    suspend fun updateGuarantor(
        payload: GuarantorApplicationPayload?,
        loanId: Long?,
        guarantorId: Long?,
    ): Response<ResponseBody?>?

    suspend fun deleteGuarantor(loanId: Long?, guarantorId: Long?): Response<ResponseBody?>?

    suspend fun getGuarantorList(loanId: Long): Response<List<GuarantorPayload?>?>?
}