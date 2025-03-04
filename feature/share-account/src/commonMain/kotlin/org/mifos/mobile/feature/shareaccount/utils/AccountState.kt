/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.shareaccount.utils

import org.mifos.mobile.core.model.entity.accounts.share.ShareAccount

sealed class AccountState {
    data object Error : AccountState()
    data object Loading : AccountState()
    data object Empty : AccountState()
    data class Success(val filteredShareAccounts: List<ShareAccount>) : AccountState()
}
