/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.data.repositoryImpl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import org.mifos.mobile.core.data.repository.GuarantorRepository
import org.mifos.mobile.core.model.entity.guarantor.GuarantorApplicationPayload
import org.mifos.mobile.core.model.entity.guarantor.GuarantorPayload
import org.mifos.mobile.core.model.entity.guarantor.GuarantorTemplatePayload
import org.mifos.mobile.core.network.DataManager
import javax.inject.Inject

class GuarantorRepositoryImp @Inject constructor(
    private val dataManager: DataManager,
) : GuarantorRepository {

    override fun getGuarantorTemplate(loanId: Long?): Flow<GuarantorTemplatePayload?> {
        return flow {
            emit(dataManager.getGuarantorTemplate(loanId))
        }
    }

    override fun createGuarantor(
        loanId: Long?,
        payload: GuarantorApplicationPayload?,
    ): Flow<ResponseBody?> {
        return flow {
            emit(dataManager.createGuarantor(loanId, payload))
        }
    }

    override fun updateGuarantor(
        payload: GuarantorApplicationPayload?,
        loanId: Long?,
        guarantorId: Long?,
    ): Flow<ResponseBody?> {
        return flow {
            emit(dataManager.updateGuarantor(payload, loanId, guarantorId))
        }
    }

    override fun deleteGuarantor(loanId: Long?, guarantorId: Long?): Flow<ResponseBody?> {
        return flow {
            emit(dataManager.deleteGuarantor(loanId, guarantorId))
        }
    }

    override fun getGuarantorList(loanId: Long): Flow<List<GuarantorPayload?>?> {
        return flow {
            emit(dataManager.getGuarantorList(loanId))
        }
    }
}
