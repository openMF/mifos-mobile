/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.model.entity

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize
import org.mifos.mobile.core.model.entity.client.Type

@Serializable
@Parcelize
data class Transaction(

    val id: Long? = null,

    val officeId: Long? = null,

    val officeName: String? = null,

    val type: Type,

    val date: List<Int> = emptyList(),

    val currency: Currency? = null,

    val amount: Double? = null,

    val submittedOnDate: List<Int> = emptyList(),

    val reversed: Boolean? = null,

) : Parcelable
