package org.mifos.mobile.repositories

import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import org.mifos.mobile.models.guarantor.GuarantorApplicationPayload
import org.mifos.mobile.models.guarantor.GuarantorPayload
import org.mifos.mobile.models.guarantor.GuarantorTemplatePayload

interface GuarantorRepository {

    fun getGuarantorTemplate(loanId: Long?): Flow<GuarantorTemplatePayload?>

    fun createGuarantor(
        loanId: Long?,
        payload: GuarantorApplicationPayload?,
    ): Flow<ResponseBody?>

    fun updateGuarantor(
        payload: GuarantorApplicationPayload?,
        loanId: Long?,
        guarantorId: Long?,
    ): Flow<ResponseBody?>

    fun deleteGuarantor(loanId: Long?, guarantorId: Long?): Flow<ResponseBody?>

    fun getGuarantorList(loanId: Long): Flow<List<GuarantorPayload?>?>
}