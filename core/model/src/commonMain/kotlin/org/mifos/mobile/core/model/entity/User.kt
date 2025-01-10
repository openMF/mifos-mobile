/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.model.entity

import kotlinx.serialization.SerialName
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize

/**
 * @author Vishwajeet
 * @since 12/06/16
 */
@Parcelize
data class User(
    val userId: Long = 0,
    @SerialName("authenticated")
    val isAuthenticated: Boolean = false,
    val username: String? = null,
    val officeId: Long = 0,
    val officeName: String? = null,
    val roles: ArrayList<Role> = arrayListOf(),
    val base64EncodedAuthenticationKey: String? = null,
    val permissions: ArrayList<String> = arrayListOf(),
    val shouldRenewPassword: Boolean = false,
    val isTwoFactorAuthenticationRequired: Boolean = false,
) : Parcelable

@Parcelize
data class Role(
    @SerialName("id")
    val id: Long = 0,
    @SerialName("name")
    val name: String? = null,
    @SerialName("description")
    val description: String? = null,
    @SerialName("disabled")
    val disabled: Boolean = false,
) : Parcelable
