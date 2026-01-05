# Location - API Reference

---

## Overview

The Location feature is a **static/local feature** that does not require any backend API calls. All content is hardcoded in the application.

---

## Static Data

### Organization Information

The following data is stored in string resources (`feature/location/src/commonMain/composeResources/values/strings.xml`):

```xml
<resources>
    <string name="mifos_initiative">Mifos Initiative</string>
    <string name="mifos_location">Mifos Initiative , Seattle , Washington 98121</string>
    <string name="map_marker_heading">Seattle</string>
    <string name="map_marker_desc">Home to large tech industry</string>
</resources>
```

### Hardcoded Coordinates

Location coordinates are hardcoded in the Android implementation:

```kotlin
// Mifos Initiative Headquarters
val headquarterLatLng = LatLng(47.61115, -122.34481)

// Map Configuration
val defaultZoomLevel = 16f
```

**Physical Location**: Seattle, Washington 98121, USA

---

## Map Display Logic

### Android Implementation

Uses Google Maps Compose library (`com.google.maps.android:maps-compose`):

```kotlin
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
```

**Dependencies Required**:
- Google Maps SDK for Android
- Google Maps Compose (`com.google.maps.android:maps-compose`)
- Valid Google Maps API key in `AndroidManifest.xml`

### Other Platforms (iOS, Desktop, Web)

Currently implemented as no-op placeholders:

```kotlin
@Composable
actual fun RenderMap(modifier: Modifier) {
    // No-op - platform-specific implementation pending
}
```

**Future Implementation Options**:
- **iOS**: Apple MapKit or Google Maps iOS SDK
- **Desktop**: OpenStreetMap with JXMapViewer or similar
- **Web**: Google Maps JavaScript API or Leaflet.js

---

## API Summary

| Endpoint | Service | Repository | Status |
|----------|---------|------------|--------|
| None | N/A | N/A | Static feature |

---

## External Service Dependencies

### Google Maps (Android Only)

| Service | Purpose | Required |
|---------|---------|----------|
| Google Maps SDK | Render interactive map | Yes (Android) |
| Google Maps Tiles | Map imagery | Yes (requires internet) |

**Configuration**: Requires `MAPS_API_KEY` in `local.properties` or build configuration.

---

## Data Flow

```
+---------------------+
|   String Resources  |
|   (static strings)  |
+----------+----------+
           |
           v
+---------------------+
|   LocationsScreen   |
|   (Composable)      |
+----------+----------+
           |
           v
+---------------------+
|   RenderMap         |
|   (expect/actual)   |
+----------+----------+
           |
    +------+------+
    |             |
    v             v
+-------+   +---------+
|Android|   | Others  |
|Google |   | (no-op) |
|Maps   |   |         |
+-------+   +---------+
```

---

## Notes

- No network calls to Mifos backend are made by this feature
- Map tile loading requires internet connectivity
- Google Maps API key must be configured for Android builds
- Consider implementing map alternatives for non-Android platforms in future releases
