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

import com.google.gson.annotations.SerializedName

/*
 * Created by saksham on 23/July/2018
 */

data class GuarantorApplicationPayload(

    @SerializedName("guarantorTypeId")
    var guarantorTypeId: Long?,

    @SerializedName("firstname")
    var firstName: String?,

    @SerializedName("lastname")
    var lastName: String?,

    @SerializedName("city")
    var city: String? = "",
)
