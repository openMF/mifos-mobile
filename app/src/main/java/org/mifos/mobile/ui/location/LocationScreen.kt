package org.mifos.mobile.ui.location

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import kotlin.coroutines.coroutineContext

@Composable
fun LocationsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.mifos_initiative),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = stringResource(id = R.string.mifos_location),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        val headquarterLatLng = LatLng(47.61115, -122.34481)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(headquarterLatLng, 16f)
        }

        GoogleMap(
            modifier = Modifier.fillMaxSize(), cameraPositionState = cameraPositionState
        ) {
            Marker(
                state = MarkerState(position = headquarterLatLng),
                title = stringResource(id = R.string.mifos_initiative),
                snippet = stringResource(id = R.string.mifos_location)
            )
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun PreviewLocationsScreen() {
    MifosMobileTheme {
        LocationsScreen()
    }
}