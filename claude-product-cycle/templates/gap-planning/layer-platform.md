# Platform Layer Planning Template

## Implementation Plan: Platform Layer

**Modules**: `cmp-android/`, `cmp-ios/`, `cmp-desktop/`, `cmp-web/`
**Last Updated**: {{DATE}}

---

### Gaps Identified

| # | Platform | Feature | Issue | Priority | Effort |
|---|----------|---------|-------|:--------:|:------:|
{{PLATFORM_GAPS_TABLE}}

---

### Platform Status

| Platform | Issues | Priority Tasks | Effort |
|----------|:------:|----------------|:------:|
| 🤖 Android | {{ANDROID_ISSUES}} | {{ANDROID_TASKS}} | {{ANDROID_EFFORT}} |
| 🍎 iOS | {{IOS_ISSUES}} | {{IOS_TASKS}} | {{IOS_EFFORT}} |
| 🖥️ Desktop | {{DESKTOP_ISSUES}} | {{DESKTOP_TASKS}} | {{DESKTOP_EFFORT}} |
| 🌐 Web | {{WEB_ISSUES}} | {{WEB_TASKS}} | {{WEB_EFFORT}} |

---

### Tasks Overview

| # | Task | Platform | Files | Priority | Effort |
|---|------|----------|-------|:--------:|:------:|
{{TASKS_TABLE}}

---

### Desktop Fixes

{{DESKTOP_TASK_DETAILS}}

**Common Desktop Issues**:

| Issue | Solution |
|-------|----------|
| No camera for QR | Use file picker to select QR image |
| No GPS for location | Remove feature or use IP geolocation |
| Keyboard handling | Add desktop-specific key listeners |

**Code Pattern**:
```kotlin
// Platform-specific implementation
expect fun scanQrCode(): String

// Desktop actual
actual fun scanQrCode(): String {
    // File picker implementation
}
```

---

### Web Fixes

{{WEB_TASK_DETAILS}}

**Common Web Issues**:

| Issue | Solution |
|-------|----------|
| Camera permissions | Use browser MediaDevices API |
| Geolocation | Use browser Geolocation API |
| Bundle size | Implement code splitting |
| Touch gestures | Add mouse event alternatives |

**Code Pattern**:
```kotlin
// Web-specific implementation
@JsExport
fun requestCameraPermission(): Promise<Boolean> {
    // Browser API implementation
}
```

---

### iOS Fixes

{{IOS_TASK_DETAILS}}

**Common iOS Issues**:

| Issue | Solution |
|-------|----------|
| CocoaPods sync | Run `pod install` in cmp-ios/ |
| Permissions | Add to Info.plist |
| Safe area | Use proper insets |

---

### Android Fixes

{{ANDROID_TASK_DETAILS}}

**Common Android Issues**:

| Issue | Solution |
|-------|----------|
| Permissions | Add to AndroidManifest.xml |
| ProGuard | Add keep rules |
| Deep links | Configure intent filters |

---

### Testing Matrix

| Feature | Android | iOS | Desktop | Web |
|---------|:-------:|:---:|:-------:|:---:|
{{TESTING_MATRIX}}

---

### Build Verification

```bash
# Android
./gradlew :cmp-android:assembleDemoDebug

# iOS
cd cmp-ios && pod install && xcodebuild

# Desktop
./gradlew :cmp-desktop:run

# Web
./gradlew :cmp-web:jsBrowserRun
```

---

### After Completion

1. Test on actual devices
2. Update platform-specific documentation
3. Update `PRODUCT_MAP.md`

---

**Start with**: Platform with most issues
