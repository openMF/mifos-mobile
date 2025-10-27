/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.location

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

/**
 * Android-specific implementation of the [RenderMap] composable function.
 * This function displays a Google Map centered on the Mifos Initiative headquarters.
 *
 * @param modifier A [Modifier] for this composable.
 */
@Composable
actual fun RenderMap(modifier: Modifier) {
    val headquarterLatLng = LatLng(47.61115, -122.34481)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(headquarterLatLng, 16f)
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
    ) {
        Marker(
            state = MarkerState(position = headquarterLatLng),
            title = "Mifos Initiative",
            snippet = "Mifos Location",
        )
    }
}
