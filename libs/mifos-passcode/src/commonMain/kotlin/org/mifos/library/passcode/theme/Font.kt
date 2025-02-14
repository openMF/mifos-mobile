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
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import mifos_mobile.libs.mifos_passcode.generated.resources.Res
import mifos_mobile.libs.mifos_passcode.generated.resources.lib_mifos_passcode_lato_black
import mifos_mobile.libs.mifos_passcode.generated.resources.lib_mifos_passcode_lato_bold
import mifos_mobile.libs.mifos_passcode.generated.resources.lib_mifos_passcode_lato_regular
import org.jetbrains.compose.resources.Font

@Composable
fun getFontFamily() = FontFamily(
    Font(Res.font.lib_mifos_passcode_lato_regular, FontWeight.Normal, FontStyle.Normal),
    Font(Res.font.lib_mifos_passcode_lato_bold, FontWeight.Bold, FontStyle.Normal),
    Font(Res.font.lib_mifos_passcode_lato_black, FontWeight.Black, FontStyle.Normal),
)

@Composable
fun getTypography(): Typography {
    return Typography(
        displayLarge = TextStyle(
            fontFamily = getFontFamily(),
            fontWeight = FontWeight.Black,
            fontSize = 34.sp,
        ),
    )
}
