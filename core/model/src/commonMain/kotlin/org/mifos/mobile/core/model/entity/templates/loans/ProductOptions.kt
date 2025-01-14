/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.model.entity.templates.loans

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize

@Serializable
@Parcelize
data class ProductOptions(

    val id: Int? = null,

    val name: String? = null,

    val includeInBorrowerCycle: Boolean? = null,

    val useBorrowerCycle: Boolean? = null,

    @SerialName("isLinkedToFloatingInterestRates")
    val linkedToFloatingInterestRates: Boolean? = null,

    @SerialName("isFloatingInterestRateCalculationAllowed")
    val floatingInterestRateCalculationAllowed: Boolean? = null,

    val allowvaliableInstallments: Boolean? = null,

    @SerialName("isInterestRecalculationEnabled")
    val interestRecalculationEnabled: Boolean? = null,

    val canDefineInstallmentAmount: Boolean? = null,

    val holdGuaranteeFunds: Boolean? = null,

    val accountMovesOutOfNPAOnlyOnArrearsCompletion: Boolean? = null,
) : Parcelable
