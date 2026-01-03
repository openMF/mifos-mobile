# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Quick Context (Start Here)

**Current Focus**: v2.0 UI Redesign (2025 Fintech Patterns)

### Session Workflow (Never Lose Context)

```bash
# START of session
/session-start                # Load context from previous session

# DURING session - use these commands
/gap-analysis                 # What's done vs what's needed (5 layers)
/gap-analysis [layer]         # Layer-specific (design|server|client|feature|platform)
/gap-planning [feature]       # Plan specific improvements
/implement [feature]          # Execute implementation
/verify [feature]             # Confirm implementation

# END of session
/session-end                  # Save context for next session
```

### Key Context Files

| File | Purpose |
|------|---------|
| `claude-product-cycle/CURRENT_WORK.md` | Active work, next actions |
| `claude-product-cycle/PRODUCT_MAP.md` | Master status tracker |
| `design-spec-layer/features/*/STATUS.md` | Per-feature status |

### 5-Layer Lifecycle

```
Design → Server → Client → Feature → Platform
```

## Project Overview

Mifos Mobile is a Kotlin Multiplatform (KMP) application for the MifosX Self-Service platform, enabling end-users to view/transact on their accounts and loans. It targets Android, iOS, Desktop (JVM), and Web (Kotlin/JS + WASM).

## Build Commands

```bash
# Build the project
./gradlew build

# Run all pre-push checks (recommended before creating PR)
./ci-prepush.sh

# Individual checks
./gradlew check -p build-logic                     # Verify build-logic configuration
./gradlew spotlessApply --no-configuration-cache   # Apply code formatting
./gradlew dependencyGuardBaseline                  # Generate dependency-guard baseline
./gradlew detekt                                   # Run static analysis

# Run tests
./gradlew testDebug                                # Run debug unit tests
./gradlew :core:data:test                          # Run tests for a specific module

# Lint checks
./gradlew :cmp-android:lintRelease                 # Run lint on Android app

# Android builds
./gradlew :cmp-android:assembleDemoDebug           # Build demo debug APK
./gradlew :cmp-android:assembleProdRelease         # Build production release APK
```

## Architecture

### Module Structure

**Platform Entry Points:**
- `cmp-android/` - Android application module
- `cmp-ios/` - iOS application (uses CocoaPods via `cmp-shared`)
- `cmp-desktop/` - Desktop (JVM) application
- `cmp-web/` - Web application (Kotlin/JS)
- `cmp-shared/` - Shared KMP module compiled for all platforms
- `cmp-navigation/` - Cross-platform navigation with Compose Navigation

**Core Modules (`core/`):**
- `data/` - Repository implementations, connects network to UI
- `network/` - Ktorfit-based API services and HTTP client
- `model/` - Domain models shared across features
- `datastore/` - Local data persistence (DataStore)
- `database/` - Room database (KMP)
- `ui/` - Shared UI components
- `designsystem/` - Design tokens, theme, common composables
- `common/` - Shared utilities
- `qrcode/` - QR code generation/scanning

**Core Base Modules (`core-base/`):** Platform-abstracted implementations shared across the template system.

**Feature Modules (`feature/`):** Each feature is a separate KMP module containing screens, ViewModels, and navigation. Features include: auth, home, accounts, loan-account, savings-account, beneficiary, transfer-process, etc.

**Library Modules (`libs/`):** Internal libraries like country-code-picker, mifos-passcode, material3-navigation.

### Key Patterns

**Dependency Injection:** Koin for all platforms. Each module defines a Koin module in its `di/` package.

**Navigation:** Uses Jetbrains Compose Navigation. Navigation graphs defined in `cmp-navigation/`:
- `ROOT_GRAPH` → `AUTH_GRAPH` → `PASSCODE_GRAPH` → `MAIN_GRAPH`

**Network Layer:** Ktorfit (Retrofit-like for Ktor) with services in `core/network/services/`. Base URL: `https://tt.mifos.community/fineract-provider/api/v1/self/`

**State Management:** ViewModels with StateFlow/SharedFlow. Features use `ScreenState<T>` pattern for loading/success/error states.

### Convention Plugins

Custom Gradle plugins in `build-logic/convention/` standardize module configuration:
- `mifos.android.application` - Android app configuration
- `org.convention.cmp.feature` - KMP feature module (applies Compose, Koin, core dependencies)
- `org.convention.kmp.library` - KMP library module
- `mifos.kmp.room` - Room database setup for KMP
- `mifos.spotless.plugin`, `mifos.detekt.plugin` - Code quality

### Build Flavors

Android has two product flavors:
- `demo` - Development/testing
- `prod` - Production

## Development Notes

- JDK 21 required (see `build-logic/convention/build.gradle.kts`)
- Pull requests target the `development` branch
- Commit message format: `<type>(<scope>): <subject>` (feat, fix, docs, refactor, test, chore)
- Demo credentials: Instance `gsoc.mifos.community`, Username `maria`, Password `password`
