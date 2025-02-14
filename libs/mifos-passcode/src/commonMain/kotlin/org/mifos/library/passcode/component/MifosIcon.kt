/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.library.passcode.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mifos_mobile.libs.mifos_passcode.generated.resources.Res
import mifos_mobile.libs.mifos_passcode.generated.resources.lib_mifos_passcode_mifos_logo
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun MifosIcon(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
    ) {
        Image(
            modifier = Modifier.size(180.dp),
            painter = painterResource(Res.drawable.lib_mifos_passcode_mifos_logo),
            contentDescription = null,
        )
    }
}
