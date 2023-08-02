package org.mifos.mobile.repositories

import io.reactivex.Observable
import okhttp3.ResponseBody
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.beneficiary.Beneficiary
import org.mifos.mobile.models.beneficiary.BeneficiaryPayload
import org.mifos.mobile.models.beneficiary.BeneficiaryUpdatePayload
import org.mifos.mobile.models.templates.beneficiary.BeneficiaryTemplate
import javax.inject.Inject

class BeneficiaryRepositoryImp @Inject constructor(private val dataManager: DataManager) :
    BeneficiaryRepository {

    override fun beneficiaryTemplate(): Observable<BeneficiaryTemplate?>? {
        return dataManager.beneficiaryTemplate
    }

    override fun createBeneficiary(beneficiaryPayload: BeneficiaryPayload?): Observable<ResponseBody?>? {
        return dataManager.createBeneficiary(beneficiaryPayload)
    }

    override fun updateBeneficiary(
        beneficiaryId: Long?,
        payload: BeneficiaryUpdatePayload?
    ): Observable<ResponseBody?>? {
        return dataManager.updateBeneficiary(beneficiaryId, payload)
    }

    override fun deleteBeneficiary(beneficiaryId: Long?): Observable<ResponseBody?>? {
        return dataManager.deleteBeneficiary(beneficiaryId)
    }

    override fun beneficiaryList(): Observable<List<Beneficiary?>?>? {
        return dataManager.beneficiaryList
    }

}