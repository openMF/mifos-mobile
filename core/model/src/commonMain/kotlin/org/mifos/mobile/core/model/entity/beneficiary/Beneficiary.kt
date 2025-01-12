/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.model.entity.beneficiary

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize
import org.mifos.mobile.core.model.entity.templates.account.AccountType

@Serializable
@Parcelize
data class Beneficiary(
    val id: Int? = null,

    val name: String? = null,

    val officeName: String? = null,

    val clientName: String? = null,

    val accountType: AccountType? = null,

    val accountNumber: String? = null,

    val transferLimit: Double? = null,

) : Parcelable
