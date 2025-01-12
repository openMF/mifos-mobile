/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.model.entity.guarantor

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize
import org.mifos.mobile.core.model.RawValue

@Serializable
@Parcelize
data class GuarantorPayload(

    val id: Long? = 0,

    val city: String? = null,

    val lastname: String? = null,

    val guarantorType: @RawValue GuarantorType? = null,

    val firstname: String? = null,

    val joinedDate: List<Int>? = null,

    val loanId: Long? = null,

    val status: Boolean? = true,
) : Parcelable
