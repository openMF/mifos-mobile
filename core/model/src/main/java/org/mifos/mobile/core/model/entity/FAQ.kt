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

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by dilpreet on 12/8/17.
 */

@Parcelize
data class FAQ @JvmOverloads constructor(
    var question: String? = null,
    var answer: String? = null,
    var isSelected: Boolean = false,
) : Parcelable
