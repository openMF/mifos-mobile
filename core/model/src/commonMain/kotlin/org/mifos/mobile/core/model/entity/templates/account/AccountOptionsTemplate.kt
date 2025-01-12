/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.model.entity.templates.account

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize

@Serializable
@Parcelize
data class AccountOptionsTemplate(

    val fromAccountOptions: List<AccountOption> = emptyList(),

    val toAccountOptions: List<AccountOption> = emptyList(),

    val fromAccountTypeOptions: List<AccountType> = emptyList(),

    val toAccountTypeOptions: List<AccountType> = emptyList(),
) : Parcelable
