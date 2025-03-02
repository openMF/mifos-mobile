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

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by Rajan Maurya on 10/03/17.
 */

@Parcelize
data class TransferPayload(
    var fromOfficeId: Int? = null,

    var fromClientId: Long? = null,

    var fromAccountType: Int? = null,

    var fromAccountId: Int? = null,

    var toOfficeId: Int? = null,

    var toClientId: Long? = null,

    var toAccountType: Int? = null,

    var toAccountId: Int? = null,

    var transferDate: String? = null,

    var transferAmount: Double? = null,

    var transferDescription: String? = null,

    var dateFormat: String = "dd MMMM yyyy",

    var locale: String = "en",

//    @Transient
    var fromAccountNumber: String? = null,

//    @Transient
    var toAccountNumber: String? = null,

) : Parcelable
