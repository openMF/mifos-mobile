/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.data.mapper.guarantor

import org.mifos.mobile.core.model.entity.guarantor.GuarantorTemplatePayload
import org.mifos.mobile.core.model.entity.guarantor.GuarantorType
import org.mifos.mobile.core.network.dto.common.TypeResponseDto
import org.mifos.mobile.core.network.dto.guarantor.GuarantorTemplateResponseDto

fun GuarantorTemplateResponseDto.toModel(): GuarantorTemplatePayload =
    GuarantorTemplatePayload(
        guarantorType = guarantorType?.toModel(),
        guarantorTypeOptions = guarantorTypeOptions
            ?.map { it.toModel() }
            ?.let { ArrayList(it) },
    )

fun TypeResponseDto.toModel(): GuarantorType =
    GuarantorType(
        id = id?.toLong(),
        value = value,
        code = code,
    )
