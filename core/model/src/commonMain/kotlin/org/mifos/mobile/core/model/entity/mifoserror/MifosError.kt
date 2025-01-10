/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.model.entity.mifoserror

import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize

@Parcelize
data class MifosError(
    val developerMessage: String? = null,
    val httpStatusCode: String? = null,
    val defaultUserMessage: String? = null,
    val userMessageGlobalisationCode: String? = null,
    val errors: List<Errors> = emptyList(),
) : Parcelable
