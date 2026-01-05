# Platform Layer Guide

> Platform-specific patterns and configurations for Mifos Mobile KMP

---

## Architecture Overview

```
┌─────────────────────────────────────────────────────────────────┐
│  Platform Layer (cmp-android, cmp-ios, cmp-desktop, cmp-web)   │
├─────────────────────────────────────────────────────────────────┤
│  Shared Layer (cmp-shared, cmp-navigation)                      │
├─────────────────────────────────────────────────────────────────┤
│  Feature Layer (feature/*)                                       │
├─────────────────────────────────────────────────────────────────┤
│  Core Layer (core/*)                                             │
└─────────────────────────────────────────────────────────────────┘
```

---

## Kotlin Multiplatform Targets

| Target | Platform | Kotlin Target |
|--------|----------|---------------|
| Android | Android 7.0+ | `android()` |
| iOS | iOS 14+ | `iosArm64()`, `iosSimulatorArm64()` |
| Desktop | JVM 17+ | `jvm()` |
| Web | Modern browsers | `js(IR)` |

---

## Platform-Specific Code

### Source Sets Structure

```
src/
├── commonMain/       # Shared code (all platforms)
├── commonTest/       # Shared tests
├── androidMain/      # Android-specific
├── iosMain/          # iOS-specific
├── jvmMain/          # Desktop-specific
└── jsMain/           # Web-specific
```

### Expect/Actual Pattern

```kotlin
// commonMain - expect declaration
expect fun getPlatformName(): String

// androidMain - actual implementation
actual fun getPlatformName(): String = "Android"

// iosMain - actual implementation
actual fun getPlatformName(): String = "iOS"
```

---

## Dependency Injection

### Koin Setup Per Platform

**Android** (`cmp-android`):
```kotlin
class MifosApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MifosApplication)
            modules(appModule)
        }
    }
}
```

**iOS** (`cmp-ios`):
```swift
KoinKt.doInitKoin()
```

**Desktop/Web**:
```kotlin
fun main() {
    startKoin {
        modules(appModule)
    }
}
```

---

## Navigation

### Compose Navigation (Cross-Platform)

```kotlin
// cmp-navigation/
NavHost(
    navController = navController,
    startDestination = ROOT_GRAPH,
) {
    authGraph(navController)
    passcodeGraph(navController)
    mainGraph(navController)
}
```

### Navigation Graphs

| Graph | Content |
|-------|---------|
| ROOT_GRAPH | Entry point, splash |
| AUTH_GRAPH | Login, Registration |
| PASSCODE_GRAPH | Passcode setup/verify |
| MAIN_GRAPH | Main app content |

---

## Platform Capabilities

| Capability | Android | iOS | Desktop | Web |
|------------|:-------:|:---:|:-------:|:---:|
| Biometrics | ✅ | ✅ | ❌ | ❌ |
| Push Notifications | ✅ | ✅ | ❌ | ⚠️ |
| Camera (QR) | ✅ | ✅ | ⚠️ | ⚠️ |
| Location | ✅ | ✅ | ❌ | ⚠️ |
| Local Storage | ✅ | ✅ | ✅ | ✅ |
| Deep Links | ✅ | ✅ | ❌ | ✅ |

---

## Build Variants

### Android Flavors

| Flavor | Purpose | Base URL |
|--------|---------|----------|
| demo | Development | tt.mifos.community |
| prod | Production | Configurable |

### Build Types

| Type | Debug | Minify | Signing |
|------|:-----:|:------:|---------|
| debug | ✅ | ❌ | Debug key |
| release | ❌ | ✅ | Release key |

---

## Testing

### Platform Testing Strategy

| Test Type | Location | Runner |
|-----------|----------|--------|
| Unit Tests | `commonTest/` | JUnit/Kotest |
| Android Tests | `androidTest/` | Android Instrumented |
| iOS Tests | `iosTest/` | XCTest |
| UI Tests | Platform-specific | Compose Test |

---

## Resources

### Platform Resources Location

| Platform | Resources |
|----------|-----------|
| Android | `cmp-android/src/main/res/` |
| iOS | `cmp-ios/iosApp/Assets.xcassets/` |
| Desktop | `cmp-desktop/src/main/resources/` |
| Web | `cmp-web/src/jsMain/resources/` |

### Shared Resources (Compose Resources)

```
core/designsystem/src/commonMain/composeResources/
├── drawable/
├── values/
└── font/
```

---

## Related Files

- [LAYER_STATUS.md](LAYER_STATUS.md) - Platform status
- [platforms/ANDROID.md](platforms/ANDROID.md) - Android details
- [platforms/IOS.md](platforms/IOS.md) - iOS details
- [platforms/DESKTOP.md](platforms/DESKTOP.md) - Desktop details
- [platforms/WEB.md](platforms/WEB.md) - Web details
