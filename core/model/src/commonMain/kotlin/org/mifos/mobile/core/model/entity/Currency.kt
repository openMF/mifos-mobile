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

import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize

/**
 * Created by michaelsosnick on 12/11/16.
 */

@Parcelize
data class Currency(
    val code: String? = null,
    val name: String? = null,
    val decimalPlaces: Int = 0,
    val displaySymbol: String? = null,
    val nameCode: String? = null,
    val displayLabel: String? = null,
) : Parcelable
