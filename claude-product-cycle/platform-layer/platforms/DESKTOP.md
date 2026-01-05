# Desktop Platform

## Module: cmp-desktop

> JVM-based desktop application using Compose for Desktop

---

## Build Commands

| Command | Action |
|---------|--------|
| `./gradlew :cmp-desktop:run` | Run desktop app |
| `./gradlew :cmp-desktop:packageDmg` | Package macOS DMG |
| `./gradlew :cmp-desktop:packageMsi` | Package Windows MSI |
| `./gradlew :cmp-desktop:packageDeb` | Package Linux DEB |
| `./gradlew :cmp-desktop:packageRpm` | Package Linux RPM |

---

## Key Files

| File | Purpose |
|------|---------|
| `cmp-desktop/build.gradle.kts` | Module configuration |
| `cmp-desktop/src/main/kotlin/.../Main.kt` | Entry point |
| `cmp-desktop/src/main/resources/` | Desktop resources |

---

## Gradle Configuration

```kotlin
compose.desktop {
    application {
        mainClass = "org.mifos.mobile.MainKt"

        nativeDistributions {
            targetFormats(
                TargetFormat.Dmg,
                TargetFormat.Msi,
                TargetFormat.Deb
            )
            packageName = "Mifos Mobile"
            packageVersion = "1.0.0"
        }
    }
}
```

---

## Entry Point

```kotlin
fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Mifos Mobile"
    ) {
        App()
    }
}
```

---

## Platform Support

| OS | Target Format | Status |
|----|---------------|:------:|
| macOS | DMG | ✅ |
| Windows | MSI | ✅ |
| Linux | DEB/RPM | ✅ |

---

## Desktop-Specific Features

| Feature | Status | Notes |
|---------|:------:|-------|
| Window Management | ✅ | Resize, minimize, maximize |
| System Tray | ⚠️ | Optional |
| Keyboard Shortcuts | ✅ | Standard shortcuts |
| File System Access | ✅ | Full access |

---

## Requirements

| Requirement | Version |
|-------------|---------|
| JVM | 17+ |
| Compose Desktop | 1.5+ |

---

## Development Notes

- Uses Compose for Desktop (Multiplatform)
- Shares UI code with Android/iOS
- Platform-specific code in `jvmMain/`

---

## Related

- [LAYER_STATUS.md](../LAYER_STATUS.md) - Platform overview
- [LAYER_GUIDE.md](../LAYER_GUIDE.md) - Architecture patterns
