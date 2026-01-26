/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.data.mapper.templates

import org.mifos.mobile.core.model.entity.templates.beneficiary.AccountTypeOption
import org.mifos.mobile.core.model.entity.templates.beneficiary.BeneficiaryTemplate
import org.mifos.mobile.core.network.dto.common.TypeResponseDto
import org.mifos.mobile.core.network.dto.templates.beneficiary.BeneficiaryTemplateDto

fun BeneficiaryTemplateDto.toModel(): BeneficiaryTemplate =
    BeneficiaryTemplate(
        accountTypeOptions = accountTypeOptions?.map { it.toAccountTypeOption() },
    )

fun TypeResponseDto.toAccountTypeOption(): AccountTypeOption =
    AccountTypeOption(
        id = id,
        code = code,
        value = value,
    )
