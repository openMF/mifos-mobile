/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package cmp.navigation.authenticated

import org.mifos.mobile.feature.beneficiary.beneficiaryApplicationConfirmation.BeneficiaryApplicationConfirmationNavRoute
import org.mifos.mobile.feature.loan.application.confirmDetails.ConfirmDetailsRoute
import org.mifos.mobile.feature.transfer.process.transferProcess.TransferProcessRoute

actual fun getPopRules(): Map<String, Int> = mapOf(
    ConfirmDetailsRoute::class.qualifiedName.orEmpty() to 2,
    TransferProcessRoute::class.qualifiedName.orEmpty() to 2,
    BeneficiaryApplicationConfirmationNavRoute::class.qualifiedName.orEmpty() to 2,
)
