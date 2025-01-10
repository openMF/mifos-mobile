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
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize

/**
 * Created by Rajan Maurya on 16/07/16.
 */

@Parcelize
data class LoanPurposeOptions(

    val id: Int? = null,

    val name: String? = null,

    val position: Int? = null,

    val description: String? = null,

    @SerialName("isActive")
    val active: Boolean? = null,

) : Parcelable
