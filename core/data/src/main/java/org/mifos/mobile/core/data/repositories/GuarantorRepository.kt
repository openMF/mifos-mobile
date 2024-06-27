package org.mifos.mobile.core.data.repositories

import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import org.mifos.mobile.core.model.entity.guarantor.GuarantorApplicationPayload
import org.mifos.mobile.core.model.entity.guarantor.GuarantorPayload
import org.mifos.mobile.core.model.entity.guarantor.GuarantorTemplatePayload

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