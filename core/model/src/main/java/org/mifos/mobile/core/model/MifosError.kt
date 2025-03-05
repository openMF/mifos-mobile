/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.mifos.mobile.core.model.entity.mifoserror.Errors

@Parcelize
data class MifosError(
    var developerMessage: String = "",

    var httpStatusCode: String = "",

    var defaultUserMessage: String = "",

    var userMessageGlobalisationCode: String? = null,

    var errors: List<Errors> = ArrayList(),
) : Parcelable
