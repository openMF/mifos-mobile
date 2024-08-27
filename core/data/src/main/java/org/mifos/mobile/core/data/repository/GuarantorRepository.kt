/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.data.repository

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
