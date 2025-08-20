/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.model.entity.templates.shareProductDetails

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize

@Parcelize
@Serializable
data class AccountingMappings(
    val shareReferenceId: AccountingItem? = null,
    val incomeFromFeeAccountId: AccountingItem? = null,
    val shareEquityId: AccountingItem? = null,
    val shareSuspenseId: AccountingItem? = null,
) : Parcelable
