# GitHub Actions - CI/CD Infrastructure

**Last Updated:** 2026-02-13
**Reusable Workflows:** `openMF/mifos-x-actionhub@v1.0.8`
**Custom Actions:** 13 total (4 Android, 4 iOS, 2 macOS, 1 Desktop, 1 Web, 1 Static Analysis)

[← Back to Main](../CLAUDE.md)

---

## Table of Contents

1. [Overview](#overview)
2. [Workflows](#workflows)
3. [Custom Actions](#custom-actions)
4. [Secrets](#secrets)
5. [Troubleshooting](#troubleshooting)

---

## Overview

This project uses **reusable workflows** from `openMF/mifos-x-actionhub` repository. All workflows are version-pinned to `@v1.0.8` for stability.

**Architecture:**
```
Local Workflows (.github/workflows/)
    ↓
Reusable Workflows (mifos-x-actionhub/.github/workflows/)
    ↓
Custom Actions (mifos-x-actionhub-*/)
    ↓
Fastlane Lanes (fastlane/Fastfile)
```

---

## Workflows

### 1. `multi-platform-build-and-publish.yml`

**Purpose:** Production deployment across all 5 platforms

**Trigger:**
- Manual dispatch (`workflow_dispatch`)
- Inputs: `release_type` (internal/beta), platform toggles, package names

**Reusable Workflow:**
```yaml
uses: openMF/mifos-x-actionhub/.github/workflows/multi-platform-build-and-publish.yaml@v1.0.8
```

**Jobs (10):**

| # | Job Name | Platform | Custom Action | Purpose |
|---|----------|----------|---------------|---------|
| 1 | `publish_android_on_firebase` | Android | `android-firebase-publish@v1.0.0` | Deploy APK to Firebase |
| 2 | `publish_android_on_playstore` | Android | `publish-android-playstore-beta@v1.0.0` | Deploy AAB to Play Store (internal/beta) |
| 3 | `publish_ios_app_to_firebase` | iOS | `publish-ios-firebase@v1.0.3` | Deploy IPA to Firebase |
| 4 | `publish_ios_app_to_testflight` | iOS | `publish-ios-testflight@v1.0.1` | Upload to TestFlight |
| 5 | `publish_ios_app_to_appstore` | iOS | `publish-ios-appstore@v1.0.1` | Submit to App Store |
| 6 | `publish_macos_app_to_testflight` | macOS | `publish-macos-testflight-kmp@v1.0.0` | macOS TestFlight |
| 7 | `publish_macos_app_to_appstore` | macOS | `publish-macos-appstore-kmp@v1.0.0` | macOS App Store |
| 8 | `publish_desktop_external` | Desktop | `publish-desktop-app-kmp@v1.0.1` | Matrix: Windows/macOS/Linux |
| 9 | `publish_web` | Web | `web-publish-kmp@v1.0.1` | Deploy to GitHub Pages |
| 10 | `github_release` | All | N/A | Create pre-release with artifacts |

**Workflow Inputs:**

```yaml
inputs:
  release_type:           # 'internal' or 'beta'
  target_branch:          # default: 'dev'

  # Package names
  android_package_name:   # e.g., 'cmp-android'
  ios_package_name:       # e.g., 'cmp-ios'
  desktop_package_name:   # e.g., 'cmp-desktop'
  web_package_name:       # e.g., 'cmp-web'

  # Distribution toggles
  distribute_ios_firebase:   # boolean
  distribute_ios_testflight: # boolean
  distribute_ios_appstore:   # boolean

  # Configuration
  use_cocoapods:          # boolean (iOS dependency manager)
  shared_module:          # 'cmp-shared'
  metadata_path:          # './fastlane/metadata'
  xcode-version:          # '15.2'
  java-version:           # '17'
```

**Required Secrets:** 30+ (see [Secrets](#secrets) section)

---

### 2. `pr-check.yml`

**Purpose:** PR validation with static analysis and debug builds

**Trigger:**
- Pull request events (opened, synchronize, reopened)

**Reusable Workflow:**
```yaml
uses: openMF/mifos-x-actionhub/.github/workflows/pr-check.yaml@v1.0.8
```

**Jobs (5):**

| # | Job Name | Custom Action | Purpose |
|---|----------|---------------|---------|
| 1 | `checks` | `static-analysis-check@v1.0.1` | Spotless, Detekt, Dependency Guard |
| 2 | `build_android_app` | `build-android-app@v1.0.2` | Build debug Android APK |
| 3 | `build_desktop_app` | `build-desktop-app-kmp@v1.0.1` | Matrix: ubuntu/macos/windows |
| 4 | `build_web_app` | `build-web-app-kmp@v1.0.1` | Build web app |
| 5 | `build_ios_app` | `build-ios-app@v1.0.3` | Build unsigned iOS IPA (optional) |

**Workflow Inputs:**

```yaml
inputs:
  build_ios:              # boolean (default: true)
  android_package_name:   # e.g., 'cmp-android'
  desktop_package_name:   # e.g., 'cmp-desktop'
  web_package_name:       # e.g., 'cmp-web'
  ios_package_name:       # e.g., 'cmp-ios'
  use_cocoapods:          # boolean
  shared_module:          # 'cmp-shared'
```

**Required Secrets:** None (debug builds only)

---

### 3. `promote-to-production.yml`

**Purpose:** Promote Play Store beta track → production

**Trigger:**
- Manual dispatch
- Release published

**Reusable Workflow:**
```yaml
uses: openMF/mifos-x-actionhub/.github/workflows/promote-to-production.yaml@v1.0.8
```

**Jobs (1):**

| Job Name | Custom Action | Purpose |
|----------|---------------|---------|
| `play_promote_production` | `publish-android-playstore-production@v1.0.0` | Promote beta → production |

**⚠️ Known Issue:** No validation that beta release exists before promotion. See [BUGS_AND_ISSUES.md](../docs/analysis/BUGS_AND_ISSUES.md#5-production-promotion-has-no-validation).

**Required Secrets:** `PLAYSTORECREDS`

---

## Custom Actions

### Android Actions (4)

#### 1. `mifos-x-actionhub-build-android-app@v1.0.2`

**Purpose:** Build Android APK or AAB (debug or release)

**Inputs:**
- `android_package_name` (required): Module name (e.g., `cmp-android`)
- `build_type` (required): `Debug` or `Release`
- `key_store` (optional): Base64-encoded keystore
- `google_services` (optional): Base64-encoded google-services.json
- `key_store_password`, `key_store_alias`, `key_store_alias_password` (optional)
- `java-version` (default: `17`)

**What it does:**
1. Sets up Java 17 (Zulu distribution)
2. Caches Gradle dependencies
3. **Version Generation** (if Release):
   - Tries: `./gradlew versionFile` → reads `version.txt`
   - Fallback: Git-based version (latest tag + commit count)
   - Calculates `VERSION_CODE` from commit count
4. Inflates keystore and google-services.json from base64
5. Runs: `./gradlew :{package_name}:assembleRelease` (or `assembleDebug`)
6. Uploads APK as artifact

**⚠️ Known Issue:** `set +e` swallows versionFile errors. See [BUGS_AND_ISSUES.md](../docs/analysis/BUGS_AND_ISSUES.md#4-version-generation-task-may-fail-silently).

**Output Artifacts:**
- `android-app` (all APKs from `**/build/outputs/apk/**/*.apk`)

---

#### 2. `mifos-x-actionhub-android-firebase-publish@v1.0.0`

**Purpose:** Deploy Android APK to Firebase App Distribution

**Inputs:**
- `android_package_name` (required)
- `release_type` (optional): `prod` (default) or `demo`
- `keystore_file`, `keystore_password`, `keystore_alias`, `keystore_alias_password`
- `google_services` (required): Base64-encoded google-services.json
- `firebase_creds` (required): Base64-encoded Firebase service account JSON
- `tester_groups` (required): Firebase tester group name

**What it does:**
1. Installs Fastlane + plugins (`firebase_app_distribution`, `increment_build_number`)
2. Inflates secrets to:
   - `{package_name}/google-services.json`
   - `keystores/release_keystore.keystore`
   - `secrets/firebaseAppDistributionServiceCredentialsFile.json`
3. Calls Fastlane lane:
   - Prod: `bundle exec fastlane android deployReleaseApkOnFirebase`
   - Demo: `bundle exec fastlane android deployDemoApkOnFirebase`
4. Cleans up secrets

**⚠️ CRITICAL BUG:** The `tester_groups` input is **IGNORED**. Fastlane lane doesn't use it.
- **Workaround:** Set `ENV['FIREBASE_GROUPS']` in workflow environment
- See [BUGS_AND_ISSUES.md](../docs/analysis/BUGS_AND_ISSUES.md#1-firebase-tester-groups-parameter-ignored)

**Output Artifacts:**
- `firebase-app` (all APKs)

---

#### 3. `mifos-x-actionhub-publish-android-on-playstore-beta@v1.0.0`

**Purpose:** Deploy AAB to Play Store (internal track, optionally promote to beta)

**Inputs:**
- `android_package_name` (required)
- `release_type` (required): `internal` or `beta`
- Keystore parameters (same as above)
- `google_services` (required)
- `playstore_creds` (required): Base64-encoded Play Store service account JSON

**What it does:**
1. Inflates secrets to:
   - `{package_name}/google-services.json`
   - `keystores/release_keystore.keystore`
   - `secrets/playStorePublishServiceCredentialsFile.json`
2. Calls Fastlane lane: `bundle exec fastlane android deployInternal`
   - Uploads AAB to internal track
3. If `release_type == 'beta'`: `bundle exec fastlane android promoteToBeta`
   - Promotes internal → beta
4. Cleans up secrets

**Fastlane Lanes Used:**
- `deployInternal` (line 108): Builds AAB, uploads to internal track
- `promoteToBeta` (line 139): Promotes internal → beta

**Output Artifacts:**
- `play-store-app` (AAB from `**/build/outputs/bundle/**/*.aab`)

---

#### 4. `mifos-x-actionhub-publish-android-on-playstore-production@v1.0.0`

**Purpose:** Promote Play Store beta track → production

**Inputs:**
- `playstore_creds` (required): Base64-encoded service account JSON

**What it does:**
1. Installs Fastlane
2. Inflates Play Store credentials to `secrets/playStorePublishServiceCredentialsFile.json`
3. Calls: `bundle exec fastlane android promote_to_production`
4. Cleans up secrets

**⚠️ Known Issue:** No validation that beta release exists. See [BUGS_AND_ISSUES.md](../docs/analysis/BUGS_AND_ISSUES.md#5-production-promotion-has-no-validation).

**Fastlane Lane Used:**
- `promote_to_production` (line 151): Promotes beta → production

**No Output Artifacts**

---

### iOS Actions (4)

#### 5. `mifos-x-actionhub-build-ios-app@v1.0.3`

**Purpose:** Build iOS IPA (debug unsigned or release signed)

**Inputs:**
- `ios_package_name` (required): iOS module name (e.g., `cmp-ios`)
- `build_type` (required): `Debug` or `Release`
- `use_cocoapods` (default: `false`): Install CocoaPods dependencies
- `shared_module` (required): Shared module path
- For Release builds:
  - `appstore_key_id`, `appstore_issuer_id` (App Store Connect API)
  - `appstore_auth_key` (Base64-encoded .p8 file)
  - `match_password`: Fastlane Match passphrase
  - `match_ssh_private_key` (Base64-encoded SSH key for Match repo)

**What it does:**
1. Sets up Ruby + Fastlane
2. Sets up Xcode (default: 15.2)
3. If Release:
   - Writes App Store Connect API key to `secrets/AuthKey.p8`
   - Configures SSH for Fastlane Match: `secrets/match_ci_key` + `~/.ssh/config`
   - Calls: `bundle exec fastlane ios build_signed_ios`
4. If Debug:
   - Calls: `bundle exec fastlane ios build_ios` (no code signing)
5. Cleans up secrets

**Fastlane Lanes Used:**
- `build_ios` (line 436): Debug build, skip codesigning
- `build_signed_ios` (line 456): Release build with Match certificates

**Output Artifacts:**
- `ios-app` (IPA from `**/build/**/*.ipa`)

---

#### 6. `mifos-x-actionhub-publish-ios-on-firebase@v1.0.3`

**Purpose:** Deploy iOS IPA to Firebase App Distribution

**Inputs:**
- `ios_package_name` (required)
- `use_cocoapods` (default: `false`)
- `shared_module` (required)
- App Store Connect API parameters (same as build-ios-app)
- `firebase_creds` (required): Base64-encoded service account JSON
- `tester_groups` (required): Firebase tester group

**What it does:**
1. Installs Fastlane + `firebase_app_distribution`, `increment_build_number` plugins
2. Writes secrets:
   - `secrets/AuthKey.p8`
   - `secrets/match_ci_key`
   - `secrets/firebaseAppDistributionServiceCredentialsFile.json`
   - SSH config for Match
3. Calls: `bundle exec fastlane ios deploy_on_firebase`
   - Auto-increments build number from latest Firebase release
   - Builds signed IPA
   - Uploads to Firebase
4. Cleans up secrets

**⚠️ CRITICAL BUG:** Same as Android - `tester_groups` input is ignored.
- **Workaround:** Set `ENV['FIREBASE_GROUPS']`
- See [BUGS_AND_ISSUES.md](../docs/analysis/BUGS_AND_ISSUES.md#1-firebase-tester-groups-parameter-ignored)

**Fastlane Lane Used:**
- `deploy_on_firebase` (line 508): Increment version, build, upload

**Output Artifacts:**
- `firebase-app-ios` (IPA)

---

#### 7. `mifos-x-actionhub-publish-ios-on-appstore-testflight@v1.0.1`

**Purpose:** Upload iOS build to TestFlight

**Inputs:**
- Same as publish-ios-on-firebase (no Firebase creds needed)

**What it does:**
1. Writes secrets (App Store Connect API key, Match SSH key)
2. Calls: `bundle exec fastlane ios beta`
   - Gets version from Gradle (sanitized for App Store: `YYYY.M.{commitCount}`)
   - Increments build number from latest TestFlight build
   - Builds signed IPA with appstore provisioning profile
   - Uploads to TestFlight with comprehensive metadata
3. Cleans up secrets

**Fastlane Lane Used:**
- `beta` (line 532): Version, build, upload to TestFlight

**Version Sanitization:**
- Gradle: `2026.1.1-beta.0.9+abc123` → App Store: `2026.1.9`
- See [Version Handling Guide](../docs/claude/version-handling.md)

**Output Artifacts:**
- `testflight-app` (IPA)

---

#### 8. `mifos-x-actionhub-publish-ios-on-appstore@v1.0.1`

**Purpose:** Submit iOS app to App Store for review

**Inputs:**
- Same as TestFlight action

**What it does:**
1. Writes secrets
2. Calls: `bundle exec fastlane ios release`
   - Gets sanitized version from Gradle
   - Increments build number from TestFlight
   - Updates Info.plist with privacy strings
   - Builds signed IPA
   - Generates release notes from conventional commits
   - Uploads to App Store with metadata
   - Submits for review
3. Cleans up secrets

**Fastlane Lane Used:**
- `release` (line 635): Version, build, upload, submit for review

**Output Artifacts:**
- `appstore-app` (IPA)

---

### macOS Actions (2)

#### 9. `mifos-x-actionhub-publish-macos-on-appstore-testflight-kmp@v1.0.0`

**Purpose:** Deploy macOS app to TestFlight

**Inputs:**
- `desktop_package_name` (required)
- App Store Connect API parameters
- `mac_signing_certificate` (Base64-encoded .p12)
- `mac_signing_certificate_password`
- `mac_installer_certificate` (Base64-encoded .p12)
- `mac_installer_certificate_password`
- `mac_provisioning_profile_base64` (Base64-encoded .provisionprofile)
- `bundle_identifier` (required)

**What it does:**
1. Creates temporary keychain
2. Imports signing certificates (.p12 files)
3. Writes provisioning profile to `~/Library/MobileDevice/Provisioning Profiles/`
4. Calls: `bundle exec fastlane mac desktop_testflight`
5. Cleans up keychain and secrets

**Note:** macOS uses **manual certificate management** (not Fastlane Match)

---

#### 10. `mifos-x-actionhub-publish-macos-on-appstore-kmp@v1.0.0`

**Purpose:** Deploy macOS app to App Store

**Inputs:**
- Same as macOS TestFlight action

**What it does:**
- Same as TestFlight, but calls: `bundle exec fastlane mac desktop_release`

**Note:** Production macOS deployment

---

### Desktop Action (1)

#### 11. `mifos-x-actionhub-publish-desktop-app-kmp@v1.0.1`

**Purpose:** Build Desktop apps for Windows, macOS, Linux

**Matrix Strategy:**
```yaml
strategy:
  matrix:
    os: [ubuntu-latest, windows-latest, macos-latest]
runs-on: ${{ matrix.os }}
```

**Inputs:**
- `desktop_package_name` (required)
- Windows, macOS, Linux signing parameters (9 total secrets)
- `java-version` (default: `17`)

**What it does:**
1. Sets up Java 17
2. Sets up Gradle
3. Runs: `./gradlew :${{ desktop_package_name }}:packageReleaseDistributionForCurrentOS`
4. Uploads platform-specific artifacts:
   - **Windows:** `*.exe`, `*.msi` (lines 72-91)
   - **macOS:** `*.dmg` (lines 97-107)
   - **Linux:** `*.deb` (lines 102-107)

**Compose Desktop Gradle Task:**
- `packageReleaseDistributionForCurrentOS` (Compose Desktop plugin)

**Output Artifacts:**
- `desktop-app-windows`, `desktop-app-macos`, `desktop-app-linux`

---

### Web Action (1)

#### 12. `mifos-x-actionhub-web-publish-kmp@v1.0.1`

**Purpose:** Deploy web app to GitHub Pages

**Inputs:**
- `web_package_name` (required)
- `java-version` (default: `17`)

**What it does:**
1. Sets up Java 17
2. Runs: `./gradlew :${{ web_package_name }}:jsBrowserDistribution`
3. Deploys to GitHub Pages:
   - Uses `peaceiris/actions-gh-pages@v4`
   - Publishes `build/dist/js/productionExecutable/` to `gh-pages` branch

**Kotlin/JS Gradle Task:**
- `jsBrowserDistribution` (Kotlin/JS plugin)

**Outputs:**
- `page_url`: GitHub Pages URL

**Output Artifacts:**
- `web-app` (JavaScript distribution)

---

### Static Analysis Action (1)

#### 13. `mifos-x-actionhub-static-analysis-check@v1.0.1`

**Purpose:** Run code quality checks

**Inputs:**
- `java-version` (default: `17`)

**What it does:**
1. Sets up Java 17
2. Sets up Gradle
3. Runs checks sequentially:
   ```bash
   ./gradlew check -p build-logic        # Build logic checks
   ./gradlew spotlessCheck                # Code formatting
   ./gradlew detekt                       # Kotlin linting
   ./gradlew dependencyGuard              # Dependency validation
   ```
4. Uploads Detekt reports as artifacts

**Tools:**
- **Spotless:** Enforces code formatting (Kotlin, KTS files)
- **Detekt:** Kotlin static analysis and linting
- **Dependency Guard:** Validates dependency changes

**Output Artifacts:**
- `detekt-reports` (Detekt HTML/XML reports)

---

## Secrets

### Secret Categories

| Category | Count | Secrets |
|----------|-------|---------|
| **Android** | 8 | ORIGINAL_KEYSTORE_FILE, ORIGINAL_KEYSTORE_FILE_PASSWORD, ORIGINAL_KEYSTORE_ALIAS, ORIGINAL_KEYSTORE_ALIAS_PASSWORD, UPLOAD_KEYSTORE_FILE, UPLOAD_KEYSTORE_FILE_PASSWORD, UPLOAD_KEYSTORE_ALIAS, UPLOAD_KEYSTORE_ALIAS_PASSWORD |
| **Firebase** | 3 | FIREBASECREDS, GOOGLESERVICES |
| **Play Store** | 1 | PLAYSTORECREDS |
| **iOS** | 5 | APPSTORE_KEY_ID, APPSTORE_ISSUER_ID, APPSTORE_AUTH_KEY, MATCH_PASSWORD, MATCH_SSH_PRIVATE_KEY |
| **Desktop** | 9 | WINDOWS_SIGNING_KEY, WINDOWS_SIGNING_PASSWORD, WINDOWS_SIGNING_CERTIFICATE, MACOS_SIGNING_KEY, MACOS_SIGNING_PASSWORD, MACOS_SIGNING_CERTIFICATE, LINUX_SIGNING_KEY, LINUX_SIGNING_PASSWORD, LINUX_SIGNING_CERTIFICATE |
| **Shared** | 1 | GITHUB_TOKEN (auto-provided) |

**Total:** 30+ secrets

### File-to-Secret Mapping

Use `keystore-manager.sh` to encode secrets:

| File in `secrets/` | GitHub Secret Name | Used By |
|-------------------|--------------------|---------|
| `firebaseAppDistributionServiceCredentialsFile.json` | `FIREBASECREDS` | Android/iOS Firebase publish |
| `google-services.json` | `GOOGLESERVICES` | Android build/deploy |
| `playStorePublishServiceCredentialsFile.json` | `PLAYSTORECREDS` | Play Store publish/promote |
| `Auth_key.p8` | `APPSTORE_AUTH_KEY` | iOS build/deploy |
| `match_ci_key` | `MATCH_SSH_PRIVATE_KEY` | iOS build/deploy (Match access) |

**Commands:**
```bash
# Encode all secrets for GitHub Actions
./keystore-manager.sh encode-secrets

# Add secrets to GitHub repository (requires gh CLI)
./keystore-manager.sh add

# View current secrets
./keystore-manager.sh view
```

See [Secrets Management Guide](../docs/claude/secrets-management.md) for complete reference.

---

## Troubleshooting

### Common Issues

#### 1. Workflow fails with "Secret not found"

**Cause:** Missing GitHub secret

**Fix:**
```bash
# Check what secrets are configured
gh secret list

# Add missing secret
./keystore-manager.sh add  # Interactive mode
```

---

#### 2. Firebase deployment succeeds but wrong tester group

**Cause:** `tester_groups` input is ignored (known bug)

**Fix:** Set environment variable in workflow:
```yaml
env:
  FIREBASE_GROUPS: "my-tester-group"
```

See [BUGS_AND_ISSUES.md](../docs/analysis/BUGS_AND_ISSUES.md#1-firebase-tester-groups-parameter-ignored)

---

#### 3. iOS build fails with "Match certificates not found"

**Possible causes:**
- Match SSH key not configured
- Match password incorrect
- Match repository empty

**Fix:**
```bash
# Run iOS setup wizard
./scripts/setup_ios_complete.sh

# Verify Match configuration
./scripts/verify_ios_deployment.sh
```

---

#### 4. Version mismatch between platforms

**Cause:** Version generation from Gradle → Firebase → App Store requires sanitization

**Fix:** Fastlane automatically sanitizes versions. See [Version Handling Guide](../docs/claude/version-handling.md).

---

#### 5. Production promotion fails with "No beta release found"

**Cause:** Beta track empty (known issue - no pre-flight validation)

**Fix:**
1. Check Play Console for beta releases
2. Deploy to beta first: `release_type: beta` in multi-platform workflow
3. Wait for beta review to complete
4. Then promote to production

---

#### 6. Desktop build fails on specific OS

**Cause:** Platform-specific signing issues or missing dependencies

**Check:**
- Signing certificates are valid for target platform
- Compose Desktop version supports target OS
- Required native dependencies installed on runner

---

#### 7. Web deployment fails with "Permission denied"

**Cause:** `GITHUB_TOKEN` lacks Pages write permissions

**Fix:**
1. Go to Settings → Actions → General
2. Workflow permissions → "Read and write permissions"
3. Re-run workflow

---

### Debugging Tips

1. **Check Action Logs:**
   - GitHub Actions → Failed workflow → Expand failed step
   - Look for Fastlane errors, Gradle failures, or secret inflation issues

2. **Validate Locally:**
   ```bash
   # Test Fastlane lanes locally
   bundle exec fastlane android deployInternal --verbose
   bundle exec fastlane ios beta --verbose
   ```

3. **Dry Run:**
   - Use `skip_submission: true` in Fastlane lanes to test build without uploading

4. **Secret Validation:**
   ```bash
   # Verify secrets are properly encoded
   ./keystore-manager.sh view

   # Re-encode if corrupted
   ./keystore-manager.sh encode-secrets
   ```

---

**Need more help?**
- [Deployment Playbook](../docs/claude/deployment-playbook.md)
- [Known Issues](../docs/analysis/BUGS_AND_ISSUES.md)
- [GitHub Actions Deep Dive](../docs/claude/github-actions-deep-dive.md)

[← Back to Main](../CLAUDE.md)
