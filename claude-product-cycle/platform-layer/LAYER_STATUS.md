# Platform Layer Status

> **4 platforms** | Android primary | KMP shared code

---

## Platform Matrix

| Platform | Module | Build | Flavors | Status |
|----------|--------|:-----:|:-------:|:------:|
| Android | cmp-android | ✅ | demo, prod | Primary |
| iOS | cmp-ios | ✅ | - | CocoaPods |
| Desktop | cmp-desktop | ✅ | - | JVM |
| Web | cmp-web | ⚠️ | - | Kotlin/JS |

**Legend**: ✅ Working | ⚠️ Experimental | ❌ Not Working

---

## O(1) Platform Lookup

| Need | Read File |
|------|-----------|
| Android build | [platforms/ANDROID.md](platforms/ANDROID.md) |
| iOS setup | [platforms/IOS.md](platforms/IOS.md) |
| Desktop config | [platforms/DESKTOP.md](platforms/DESKTOP.md) |
| Web config | [platforms/WEB.md](platforms/WEB.md) |

---

## Shared Modules

| Module | Purpose | Target |
|--------|---------|--------|
| cmp-shared | KMP shared code | All platforms |
| cmp-navigation | Cross-platform navigation | All platforms |
| core/* | Core business logic | All platforms |
| feature/* | Feature modules | All platforms |

---

## Build Commands Quick Reference

| Platform | Debug | Release |
|----------|-------|---------|
| Android | `./gradlew :cmp-android:assembleDemoDebug` | `./gradlew :cmp-android:assembleProdRelease` |
| Desktop | `./gradlew :cmp-desktop:run` | `./gradlew :cmp-desktop:packageDmg` |
| Web | `./gradlew :cmp-web:jsBrowserRun` | `./gradlew :cmp-web:jsBrowserProductionWebpack` |
| iOS | Xcode Build | Archive & Distribute |

---

## Platform Entry Points

| Platform | Entry Point | Location |
|----------|-------------|----------|
| Android | MainActivity | `cmp-android/src/main/kotlin/.../MainActivity.kt` |
| iOS | iosApp | `cmp-ios/iosApp/` |
| Desktop | Main.kt | `cmp-desktop/src/main/kotlin/.../Main.kt` |
| Web | Main.kt | `cmp-web/src/jsMain/kotlin/.../Main.kt` |

---

## Gradle Module Configuration

| Module | Build File |
|--------|------------|
| cmp-android | `cmp-android/build.gradle.kts` |
| cmp-ios | Uses CocoaPods via `cmp-shared` |
| cmp-desktop | `cmp-desktop/build.gradle.kts` |
| cmp-web | `cmp-web/build.gradle.kts` |
| cmp-shared | `cmp-shared/build.gradle.kts` |
| cmp-navigation | `cmp-navigation/build.gradle.kts` |

---

## CI/CD Status

| Platform | CI Pipeline | Status |
|----------|-------------|--------|
| Android | GitHub Actions | ✅ |
| iOS | - | ⚠️ Manual |
| Desktop | - | ⚠️ Manual |
| Web | - | ⚠️ Manual |

---

## Related Files

- [LAYER_GUIDE.md](LAYER_GUIDE.md) - Platform-specific patterns
- [platforms/](platforms/) - Platform-specific documentation
- [../CLAUDE.md](../../CLAUDE.md) - Project overview

---

## Auto-Update Rules

| Scenario | Action |
|----------|--------|
| Platform status changes | Update Platform Matrix |
| New build variant added | Update Build Commands |
| CI/CD configured | Update CI/CD Status |
