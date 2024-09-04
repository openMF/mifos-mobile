/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.library.passcode.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

internal val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = LatoFonts,
        fontWeight = FontWeight.Black,
        fontSize = 34.sp,
    ),
)

internal val PasscodeKeyButtonStyle = TextStyle(
    fontFamily = LatoFonts,
    fontWeight = FontWeight.Bold,
    fontSize = 24.sp,
)

internal val skipButtonStyle = TextStyle(
    color = blueTint,
    fontSize = 20.sp,
    fontFamily = LatoFonts,
)

internal val forgotButtonStyle = TextStyle(
    color = blueTint,
    fontSize = 14.sp,
    fontFamily = LatoFonts,
)
