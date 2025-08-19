package org.mifos.mobile.core.network.services

import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query
import kotlinx.coroutines.flow.Flow
import org.mifos.mobile.core.model.entity.Page
import org.mifos.mobile.core.model.entity.templates.shareProduct.ShareDetails
import org.mifos.mobile.core.model.entity.templates.shares.ShareProduct
import org.mifos.mobile.core.network.utils.ApiEndPoints


interface ShareAccountService {

    @GET("${ApiEndPoints.PRODUCTS}/"+ ApiEndPoints.SHARE)
    fun getShareProducts(
        @Query("clientId") clientId: Long?,
    ): Flow<Page<ShareProduct>>

    @GET("${ApiEndPoints.PRODUCTS}/"+ ApiEndPoints.SHARE + "/{productId}")
    fun getShareProductById(
        @Path("productId") productId: Long,
        @Query("clientId") clientId: String?,
    ): Flow<ShareDetails>

//    @POST(ApiEndPoints.SHARE_ACCOUNTS)
//    suspend fun submitShareApplication(
//        @Path("savingsId") savingsId: Long,
//        @Body payload: SavingsAccountWithdrawPayload?,
//    ): HttpResponse
}
