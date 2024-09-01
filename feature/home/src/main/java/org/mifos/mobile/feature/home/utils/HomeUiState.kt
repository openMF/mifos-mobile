/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.home.utils

import android.graphics.Bitmap

internal sealed class HomeUiState {
    data object Loading : HomeUiState()
    data class Error(val errorMessage: Int) : HomeUiState()
    data class Success(val homeState: HomeState) : HomeUiState()
}

internal data class HomeState(
    val username: String? = "",
    val image: Bitmap? = null,
    val loanAmount: Double = 0.0,
    val savingsAmount: Double = 0.0,
)
