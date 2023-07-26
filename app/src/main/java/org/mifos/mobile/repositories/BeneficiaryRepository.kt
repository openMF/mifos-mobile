package org.mifos.mobile.repositories

import io.reactivex.Observable
import okhttp3.ResponseBody
import org.mifos.mobile.models.beneficiary.Beneficiary
import org.mifos.mobile.models.beneficiary.BeneficiaryPayload
import org.mifos.mobile.models.beneficiary.BeneficiaryUpdatePayload
import org.mifos.mobile.models.templates.beneficiary.BeneficiaryTemplate

interface BeneficiaryRepository {

    fun beneficiaryTemplate(): Observable<BeneficiaryTemplate?>?

    fun createBeneficiary(beneficiaryPayload: BeneficiaryPayload?): Observable<ResponseBody?>?

    fun updateBeneficiary(
        beneficiaryId: Long?,
        payload: BeneficiaryUpdatePayload?,
    ): Observable<ResponseBody?>?

    fun deleteBeneficiary(beneficiaryId: Long?): Observable<ResponseBody?>?

    fun beneficiaryList(): Observable<List<Beneficiary?>?>?

}