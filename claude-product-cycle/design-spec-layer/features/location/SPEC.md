# Location - Feature Specification

> **Purpose**: Display Mifos Initiative headquarters location with interactive map
> **User Value**: Helps users find and visit Mifos Initiative office
> **Last Updated**: 2025-12-29

---

## 1. Overview

### 1.1 Feature Summary
The Location screen displays the Mifos Initiative headquarters information including the organization name, address, and an interactive map view. On Android, it renders a Google Maps component with a marker at the headquarters location. On other platforms (iOS, Desktop, Web), the map component is a no-op placeholder pending platform-specific implementation.

### 1.2 User Stories
- As a user, I want to see where Mifos Initiative is located so I can visit the office
- As a user, I want to view the headquarters on a map so I can get directions
- As a user, I want to see the full address so I can navigate to the location

---

## 2. Screen Layout

### 2.1 ASCII Mockup

```
+-------------------------------------------+
|                                           |
|  Mifos Initiative                         |  <- Organization name (bodyLarge)
|                                           |
|  Mifos Initiative, Seattle,               |  <- Full address (bodyMedium)
|  Washington 98121                         |
|                                           |
|  +-------------------------------------+  |
|  |                                     |  |
|  |                                     |  |
|  |         GOOGLE MAP VIEW             |  |
|  |                                     |  |
|  |              [ * ]                  |  |  <- Marker at headquarters
|  |         "Mifos Initiative"          |  |
|  |         "Mifos Location"            |  |
|  |                                     |  |
|  |                                     |  |
|  |                                     |  |
|  |                                     |  |
|  +-------------------------------------+  |
|                                           |
+-------------------------------------------+
```

### 2.2 Sections Table

| # | Section | Description | API | Priority |
|---|---------|-------------|-----|----------|
| 1 | Organization Name | "Mifos Initiative" heading | None (static) | P0 |
| 2 | Address | Full address text | None (static) | P0 |
| 3 | Map View | Interactive map with marker | None (local) | P0 |

---

## 3. User Interactions

| Action | Trigger | Result | API Call |
|--------|---------|--------|----------|
| View map | Screen load | Display map centered on HQ | None |
| Tap marker | Click marker on map | Show info window with title/snippet | None |
| Pan map | Drag gesture | Move map view | None |
| Zoom map | Pinch gesture | Zoom in/out | None |

---

## 4. State Model

This feature is stateless. It displays static content with no dynamic data loading.

```kotlin
// No ViewModel required - pure composable with static data
// Location coordinates are hardcoded constants

// Hardcoded location data:
val headquarterLatLng = LatLng(47.61115, -122.34481)  // Seattle, WA
val zoomLevel = 16f
val markerTitle = "Mifos Initiative"
val markerSnippet = "Mifos Location"
```

---

## 5. API Requirements

| Endpoint | Method | Purpose | Status |
|----------|--------|---------|--------|
| None | - | Static feature, no API calls | N/A |

This is a fully static/local feature. All data is hardcoded:
- Organization name from string resources
- Address from string resources
- Map coordinates hardcoded in Android implementation

---

## 6. Edge Cases & Error Handling

| Scenario | Behavior | UI Feedback |
|----------|----------|-------------|
| No Google Play Services (Android) | Map may not render | System error dialog |
| No internet (map tiles) | Cached tiles or blank map | Map shows cached data |
| Non-Android platform | Map component is no-op | Empty space where map would be |
| Map API key missing | Map shows error overlay | Google Maps error message |

---

## 7. Components

| Component | Props | Reusable? |
|-----------|-------|-----------|
| LocationsScreen | modifier | No (feature-specific) |
| RenderMap | modifier | Yes (expect/actual pattern) |
| GoogleMap | cameraPositionState, content | Yes (from maps-compose) |
| Marker | state, title, snippet | Yes (from maps-compose) |

---

## 8. Platform Support

| Platform | Map Implementation | Status |
|----------|-------------------|--------|
| Android | Google Maps Compose | Implemented |
| iOS | No-op (placeholder) | Not implemented |
| Desktop | No-op (placeholder) | Not implemented |
| Web (JS) | No-op (placeholder) | Not implemented |
| Web (WASM) | No-op (placeholder) | Not implemented |

---

## 9. Navigation

### Routes
- Base Route: `locations_base_route`
- Screen Route: `locations_screen_route`

### Navigation Functions
```kotlin
fun NavController.navigateToLocationsScreen()
fun NavGraphBuilder.locationsNavGraph()
fun NavGraphBuilder.locationsScreenRoute()
```

---

## Changelog

| Date | Change |
|------|--------|
| 2025-12-29 | Initial specification created based on implementation analysis |
