package org.mifos.mobile.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.mifos.mobile.FakeRemoteDataSource
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.guarantor.GuarantorApplicationPayload
import org.mifos.mobile.models.guarantor.GuarantorPayload
import org.mifos.mobile.models.guarantor.GuarantorTemplatePayload
import javax.inject.Inject

class GuarantorRepositoryImp @Inject constructor(private val dataManager: DataManager) :
    GuarantorRepository {

    override fun getGuarantorTemplate(loanId: Long?): Flow<GuarantorTemplatePayload?> {
        return flow {
            emit(dataManager.getGuarantorTemplate(loanId))
        }.catch {
            emit(FakeRemoteDataSource.guarantorTemplatePayload)
        }
    }

    override fun createGuarantor(
        loanId: Long?,
        payload: GuarantorApplicationPayload?
    ): Flow<ResponseBody?> {
        return flow {
            emit(dataManager.createGuarantor(loanId, payload))
        }.catch {
            val responseBody = "Guarantor Added Successfully"
                .toResponseBody("text/plain".toMediaTypeOrNull())
            emit(responseBody)
        }
    }

    override fun updateGuarantor(
        payload: GuarantorApplicationPayload?,
        loanId: Long?,
        guarantorId: Long?
    ): Flow<ResponseBody?> {
        return flow {
            emit(dataManager.updateGuarantor(payload, loanId, guarantorId))
        }.catch {
            val response = "Guarantor Updated Successfully".toResponseBody(
                "plain/text".toMediaTypeOrNull()
            )
            emit(response)
        }
    }

    override fun deleteGuarantor(loanId: Long?, guarantorId: Long?): Flow<ResponseBody?> {
        return flow {
            emit(dataManager.deleteGuarantor(loanId, guarantorId))
        }.catch {
            val response = "Guarantor Deleted Successfully".toResponseBody(
                "plain/text".toMediaTypeOrNull()
            )
            emit(response)
        }
    }

    override fun getGuarantorList(loanId: Long): Flow<List<GuarantorPayload?>?> {
        return flow {
            emit(dataManager.getGuarantorList(loanId))
        }.catch {
            emit(FakeRemoteDataSource.guarantorsList)
        }
    }
}