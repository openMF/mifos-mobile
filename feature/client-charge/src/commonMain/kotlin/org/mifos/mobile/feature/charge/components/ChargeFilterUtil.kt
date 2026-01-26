/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.charge.components

import mifos_mobile.feature.client_charge.generated.resources.Res
import mifos_mobile.feature.client_charge.generated.resources.charges
import mifos_mobile.feature.client_charge.generated.resources.outstanding
import mifos_mobile.feature.client_charge.generated.resources.paid
import mifos_mobile.feature.client_charge.generated.resources.waived
import org.jetbrains.compose.resources.StringResource
import org.mifos.mobile.core.model.entity.Charge

/**
 * Enum class representing different filters that can be applied to Charges.
 */
enum class ChargeFilterUtil(
    val label: StringResource,
    val matchCondition: (Charge) -> Boolean,
) {

    ALL(
        label = Res.string.charges,
        matchCondition = { true },
    ),

    PAID(
        label = Res.string.paid,
        matchCondition = { it.paid },
    ),

    PENDING(
        label = Res.string.outstanding,
        matchCondition = { !it.paid && !it.waived },
    ),

    WAIVED(
        label = Res.string.waived,
        matchCondition = { it.waived },
    ),
}
