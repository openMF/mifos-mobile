package org.mifos.mobile.repositories

import io.reactivex.Observable
import okhttp3.ResponseBody
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.guarantor.GuarantorApplicationPayload
import org.mifos.mobile.models.guarantor.GuarantorPayload
import org.mifos.mobile.models.guarantor.GuarantorTemplatePayload
import javax.inject.Inject

class GuarantorRepositoryImp @Inject constructor(private val dataManager: DataManager) :
    GuarantorRepository {

    override fun getGuarantorTemplate(loanId: Long?): Observable<GuarantorTemplatePayload?>? {
        return dataManager.getGuarantorTemplate(loanId)
    }

    override fun createGuarantor(
        loanId: Long?,
        payload: GuarantorApplicationPayload?
    ): Observable<ResponseBody?>? {
        return dataManager.createGuarantor(loanId, payload)
    }

    override fun updateGuarantor(
        payload: GuarantorApplicationPayload?,
        loanId: Long?,
        guarantorId: Long?
    ): Observable<ResponseBody?>? {
        return dataManager.updateGuarantor(payload, loanId, guarantorId)
    }

    override fun deleteGuarantor(loanId: Long?, guarantorId: Long?): Observable<ResponseBody?>? {
        return dataManager.deleteGuarantor(loanId, guarantorId)
    }

    override fun getGuarantorList(loanId: Long): Observable<List<GuarantorPayload?>?>? {
        return dataManager.getGuarantorList(loanId)
    }
}