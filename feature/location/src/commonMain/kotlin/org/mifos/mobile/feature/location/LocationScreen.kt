/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.location

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mifos_mobile.feature.location.generated.resources.Res
import mifos_mobile.feature.location.generated.resources.mifos_initiative
import mifos_mobile.feature.location.generated.resources.mifos_location
import org.jetbrains.compose.resources.stringResource

/**
 * A composable function that displays the location of the Mifos Initiative.
 * It includes the name, address, and a map view showing the location.
 *
 * @param modifier A [Modifier] for this composable.
 */
@Composable
internal fun LocationsScreen(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = stringResource(Res.string.mifos_initiative),
            style = MaterialTheme.typography.bodyLarge,
        )
        Text(
            text = stringResource(Res.string.mifos_location),
            style = MaterialTheme.typography.bodyMedium,
        )
        RenderMap(modifier = Modifier.fillMaxSize())
    }
}

/**
 * An expect composable function for rendering a map. Each platform is expected
 * to provide its own implementation.
 *
 * @param modifier A [Modifier] for this composable.
 */
@Composable
expect fun RenderMap(modifier: Modifier = Modifier)
