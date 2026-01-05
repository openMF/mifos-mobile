# Android Platform

## Module: cmp-android

> Primary platform target for Mifos Mobile

---

## Build Flavors

| Flavor | API Base | Use Case |
|--------|----------|----------|
| demo | tt.mifos.community | Development/Testing |
| prod | Configurable | Production |

---

## Build Commands

| Command | Output |
|---------|--------|
| `./gradlew :cmp-android:assembleDemoDebug` | Demo debug APK |
| `./gradlew :cmp-android:assembleDemoRelease` | Demo release APK |
| `./gradlew :cmp-android:assembleProdDebug` | Prod debug APK |
| `./gradlew :cmp-android:assembleProdRelease` | Production release APK |
| `./gradlew :cmp-android:lintRelease` | Lint checks |
| `./gradlew :cmp-android:testDebug` | Run unit tests |

---

## Key Files

| File | Purpose |
|------|---------|
| `cmp-android/build.gradle.kts` | Module configuration |
| `cmp-android/src/main/AndroidManifest.xml` | App manifest |
| `cmp-android/src/main/kotlin/.../MainActivity.kt` | Entry point |
| `cmp-android/src/main/kotlin/.../MifosApplication.kt` | Application class |

---

## Gradle Configuration

```kotlin
android {
    namespace = "org.mifos.mobile"
    compileSdk = 34

    defaultConfig {
        applicationId = "org.mifos.mobile"
        minSdk = 24
        targetSdk = 34
    }

    productFlavors {
        create("demo") { ... }
        create("prod") { ... }
    }
}
```

---

## Signing Configuration

### Debug
- Auto-generated debug keystore
- Location: `~/.android/debug.keystore`

### Release
- Requires `keystore.properties` file
- Keys: `storeFile`, `storePassword`, `keyAlias`, `keyPassword`

---

## Dependencies

| Category | Key Dependencies |
|----------|------------------|
| Compose | Compose BOM, Material3 |
| DI | Koin Android |
| Network | Ktor Android |
| Storage | DataStore, Room |

---

## Android-Specific Features

| Feature | Implementation |
|---------|----------------|
| Biometrics | AndroidX Biometric |
| Push Notifications | Firebase Cloud Messaging |
| Deep Links | Intent Filters |
| Splash Screen | SplashScreen API |

---

## ProGuard/R8

- Rules in `proguard-rules.pro`
- Keep rules for serialization
- Ktor client rules

---

## Related

- [LAYER_STATUS.md](../LAYER_STATUS.md) - Platform overview
- [LAYER_GUIDE.md](../LAYER_GUIDE.md) - Architecture patterns
