# Web Platform

## Module: cmp-web

> Kotlin/JS web application (Experimental)

---

## Status: Experimental

The web platform is currently in experimental status. Some features may not be fully functional.

---

## Build Commands

| Command | Action |
|---------|--------|
| `./gradlew :cmp-web:jsBrowserRun` | Run in browser (dev) |
| `./gradlew :cmp-web:jsBrowserProductionWebpack` | Production build |
| `./gradlew :cmp-web:jsBrowserDevelopmentWebpack` | Development build |

---

## Key Files

| File | Purpose |
|------|---------|
| `cmp-web/build.gradle.kts` | Module configuration |
| `cmp-web/src/jsMain/kotlin/.../Main.kt` | Entry point |
| `cmp-web/src/jsMain/resources/index.html` | HTML template |

---

## Gradle Configuration

```kotlin
kotlin {
    js(IR) {
        browser {
            commonWebpackConfig {
                cssSupport {
                    enabled.set(true)
                }
            }
        }
        binaries.executable()
    }
}
```

---

## Entry Point

```kotlin
fun main() {
    onWasmReady {
        BrowserViewportWindow("Mifos Mobile") {
            App()
        }
    }
}
```

---

## Output Location

| Build Type | Output |
|------------|--------|
| Development | `build/dist/js/developmentExecutable/` |
| Production | `build/dist/js/productionExecutable/` |

---

## Browser Support

| Browser | Status |
|---------|:------:|
| Chrome | ✅ |
| Firefox | ✅ |
| Safari | ⚠️ |
| Edge | ✅ |

---

## Web-Specific Limitations

| Feature | Status | Notes |
|---------|:------:|-------|
| Local Storage | ✅ | Using browser storage |
| Network | ✅ | CORS considerations |
| Biometrics | ❌ | Not available |
| Camera | ⚠️ | Browser permissions |
| Notifications | ⚠️ | Web Push API |

---

## Development Server

When running `jsBrowserRun`:
- Default URL: `http://localhost:8080`
- Hot reload enabled
- Source maps available

---

## Production Deployment

1. Build production bundle:
   ```bash
   ./gradlew :cmp-web:jsBrowserProductionWebpack
   ```

2. Deploy `build/dist/js/productionExecutable/` to web server

3. Configure server for SPA routing

---

## Known Issues

| Issue | Workaround |
|-------|------------|
| Large bundle size | Tree shaking, code splitting |
| CORS errors | Configure server headers |
| WebSocket issues | Use polling fallback |

---

## Related

- [LAYER_STATUS.md](../LAYER_STATUS.md) - Platform overview
- [LAYER_GUIDE.md](../LAYER_GUIDE.md) - Architecture patterns
