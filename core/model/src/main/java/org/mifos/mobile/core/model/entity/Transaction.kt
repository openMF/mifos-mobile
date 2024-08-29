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

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.mifos.mobile.core.model.entity.client.Type

/**
 * @author Vishwajeet
 * @since 10/8/16.
 */

@Parcelize
data class Transaction(

    var id: Long? = null,

    var officeId: Long? = null,

    var officeName: String? = null,

    var type: Type,

    var date: List<Int> = ArrayList(),

    var currency: Currency? = null,

    var amount: Double? = null,

    var submittedOnDate: List<Int> = ArrayList(),

    var reversed: Boolean? = null,

) : Parcelable
