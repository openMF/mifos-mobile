/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.model.entity.payload

import org.mifos.mobile.core.model.enums.AccountType

data class PocketLinkPayload(
    val accountsDetail: List<AccountDetail>,
) {
    data class AccountDetail(
        val accountId: String,
        val accountType: AccountType,
    )
}
