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

/*
 * Created by saksham on 24/July/2018
 */

import android.os.Parcelable
import kotlinx.android.parcel.RawValue
import kotlinx.parcelize.Parcelize

@Parcelize
data class GuarantorPayload(

    var id: Long? = 0,

    var city: String? = null,

    var lastname: String? = null,

    var guarantorType: @RawValue GuarantorType? = null,

    var firstname: String? = null,

    var joinedDate: List<Int>? = null,

    var loanId: Long? = null,

    var status: Boolean? = true,
) : Parcelable
