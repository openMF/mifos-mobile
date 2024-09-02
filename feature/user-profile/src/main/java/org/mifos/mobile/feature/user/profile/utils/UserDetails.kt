/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.user.profile.utils

data class UserDetails(
    val userName: String?,
    val accountNumber: String?,
    val activationDate: String?,
    val officeName: String?,
    val clientType: String?,
    val groups: String?,
    val clientClassification: String?,
    val phoneNumber: String?,
    val dob: String?,
    val gender: String?,
)
