/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.model.entity.client

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize
import org.mifos.mobile.core.model.entity.Timeline

@Serializable
@Parcelize
data class Client(
    val id: Int = 0,

    val accountNo: String? = null,

    private val status: Status? = null,

    private val active: Boolean? = null,

    val activationDate: List<Int> = emptyList(),

    val dobDate: List<Int> = emptyList(),

    val firstname: String? = null,

    val middlename: String? = null,

    val lastname: String? = null,

    val displayName: String? = null,

    val fullname: String? = null,

    private val officeId: Int? = null,

    val officeName: String? = null,

    private val staffId: Int? = null,

    private val staffName: String? = null,

    private val timeline: Timeline? = null,

    val imageId: Int = 0,

    val isImagePresent: Boolean = false,

    private val externalId: String? = null,

    val mobileNo: String? = null,

    val clientClassification: ClientClassification? = null,

    val clientType: ClientType? = null,

    val gender: Gender? = null,

    val groups: List<Group> = emptyList(),
) : Parcelable
