/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package cmp.navigation.authenticated

actual fun getPopRules(): Map<String, Int> = mapOf(
    "cmp.navigation.authenticated.ConfirmDetailsRoute" to 2,
    "cmp.navigation.authenticated.TransferProcessRoute" to 2,
    "cmp.navigation.authenticated.BeneficiaryApplicationConfirmationNavRoute" to 2,
)
