/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.model.entity.accounts.savings

import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize

@Parcelize
data class SavingsAccountApplicationPayload(

    val submittedOnDate: String? = null,

    val clientId: Int? = null,

    val productId: Int? = null,

    val locale: String = "en",

    val dateFormat: String = "dd MMMM yyyy",

) : Parcelable
