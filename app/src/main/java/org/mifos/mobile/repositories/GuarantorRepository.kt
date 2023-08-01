package org.mifos.mobile.repositories

import io.reactivex.Observable
import okhttp3.ResponseBody
import org.mifos.mobile.models.guarantor.GuarantorApplicationPayload
import org.mifos.mobile.models.guarantor.GuarantorPayload
import org.mifos.mobile.models.guarantor.GuarantorTemplatePayload

interface GuarantorRepository {

    fun getGuarantorTemplate(loanId: Long?): Observable<GuarantorTemplatePayload?>?

    fun createGuarantor(
        loanId: Long?,
        payload: GuarantorApplicationPayload?,
    ): Observable<ResponseBody?>?

    fun updateGuarantor(
        payload: GuarantorApplicationPayload?,
        loanId: Long?,
        guarantorId: Long?,
    ): Observable<ResponseBody?>?

    fun deleteGuarantor(loanId: Long?, guarantorId: Long?): Observable<ResponseBody?>?

    fun getGuarantorList(loanId: Long): Observable<List<GuarantorPayload?>?>?
}