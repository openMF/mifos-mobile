/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.ui.utils

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
object PresentOrFutureSelectableDates : SelectableDates {
    @ExperimentalMaterial3Api
    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        return utcTimeMillis >= System.currentTimeMillis()
    }

    override fun isSelectableYear(year: Int): Boolean {
        return year >= LocalDate.now().year
    }
}
