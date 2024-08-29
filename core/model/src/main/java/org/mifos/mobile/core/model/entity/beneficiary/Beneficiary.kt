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

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.mifos.mobile.core.model.entity.templates.account.AccountType

@Parcelize
data class Beneficiary(
    var id: Int? = null,

    var name: String? = null,

    var officeName: String? = null,

    var clientName: String? = null,

    var accountType: AccountType? = null,

    var accountNumber: String? = null,

    var transferLimit: Double? = null,

) : Parcelable
