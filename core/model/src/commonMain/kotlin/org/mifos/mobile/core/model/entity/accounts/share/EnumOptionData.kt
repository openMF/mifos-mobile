/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.model.entity.accounts.share

import kotlinx.serialization.Serializable

/**
 * A generic data model representing an enumerated option returned by the Share Account API.
 * This class is used to map dropdown options, status fields, or types (like share status,
 * lock-in period type, etc.)
 */
@Serializable
data class EnumOptionData(
    val id: Long? = null,
    val code: String? = null,
    val value: String? = null,
)
