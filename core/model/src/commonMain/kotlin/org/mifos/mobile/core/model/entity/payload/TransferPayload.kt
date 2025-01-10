/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.model.entity.payload

import kotlinx.serialization.Transient
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize

/**
 * Created by Rajan Maurya on 10/03/17.
 */

@Parcelize
data class TransferPayload(
    val fromOfficeId: Int? = null,

    val fromClientId: Long? = null,

    val fromAccountType: Int? = null,

    val fromAccountId: Int? = null,

    val toOfficeId: Int? = null,

    val toClientId: Long? = null,

    val toAccountType: Int? = null,

    val toAccountId: Int? = null,

    val transferDate: String? = null,

    val transferAmount: Double? = null,

    val transferDescription: String? = null,

    val dateFormat: String = "dd MMMM yyyy",

    val locale: String = "en",

    @Transient
    val fromAccountNumber: String? = null,

    @Transient
    val toAccountNumber: String? = null,

) : Parcelable
