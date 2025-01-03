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
 * Created by michaelsosnick on 12/11/16.
 */

@Parcelize
data class Currency(
    var code: String? = null,
    var name: String? = null,
    var decimalPlaces: Int = 0,
    var displaySymbol: String? = null,
    var nameCode: String? = null,
    var displayLabel: String? = null,
) : Parcelable
