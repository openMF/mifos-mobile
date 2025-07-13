/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.auth.status

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import mifos_mobile.core.ui.generated.resources.Res
import mifos_mobile.core.ui.generated.resources.ic_icon_error
import mifos_mobile.core.ui.generated.resources.ic_icon_success
import org.mifos.mobile.core.designsystem.component.MifosScaffold
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.ui.component.MifosPoweredCard
import org.mifos.mobile.core.ui.component.MifosStatusComponent
import org.mifos.mobile.feature.auth.otpAuthentication.EventType

@Composable
internal fun StatusScreen(
    eventType: EventType,
    eventDestination: String,
    title: String,
    subtitle: String,
    buttonText: String,
    navigateToDestination: (String) -> Unit,
) {
    MifosScaffold(
        bottomBar = {
            Surface {
                MifosPoweredCard(
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(DesignToken.padding.large),
            verticalArrangement = Arrangement.Center,
        ) {
            MifosStatusComponent(
                icon = if (eventType == EventType.SUCCESS) {
                    Res.drawable.ic_icon_success
                } else {
                    Res.drawable.ic_icon_error
                },
                title = title,
                subTitle = subtitle,
                buttonText = buttonText,
                onClick = { navigateToDestination(eventDestination) },
            )
        }
    }
}
