
# Fastlane - Deployment Automation

**Last Updated:** 2026-02-13
**Total Lanes:** 12 (7 Android + 5 iOS)
**Configuration:** `fastlane-config/project_config.rb`

[← Back to Main](../CLAUDE.md)

---

## Table of Contents

1. [Overview](#overview)
2. [Android Lanes](#android-lanes)
3. [iOS Lanes](#ios-lanes)
4. [Configuration](#configuration)
5. [Common Tasks](#common-tasks)
6. [Troubleshooting](#troubleshooting)

---

## Overview

Fastlane automates iOS and Android deployment to Firebase, TestFlight, App Store, and Play Store.

**Key Concepts:**
- **Lanes:** Automated deployment workflows
- **Match:** iOS code signing certificate management (uses Git repository)
- **Plugins:** `firebase_app_distribution`, `increment_build_number`
- **Configuration:** Single source of truth in `fastlane-config/project_config.rb`

**Architecture:**
```
GitHub Actions
    ↓
Custom Actions (inflate secrets, call Fastlane)
    ↓
Fastlane Lanes (build, sign, upload)
    ↓
Firebase / Play Store / TestFlight / App Store
```

---

## Android Lanes

### Lane 1: `assembleDebugApks`

**Purpose:** Build debug APKs (no signing)

**Usage:**
```bash
bundle exec fastlane android assembleDebugApks
```

**What it does:**
- Runs: `./gradlew assembleDebug`
- No code signing
- Used for PR checks and local testing

**Output:** Debug APK at `cmp-android/build/outputs/apk/debug/`

---

### Lane 2: `assembleReleaseApks`

**Purpose:** Build release APK with signing

**Usage:**
```bash
bundle exec fastlane android assembleReleaseApks \
  storeFile:release_keystore.keystore \
  storePassword:$KEYSTORE_PASSWORD \
  keyAlias:$KEYSTORE_ALIAS \
  keyPassword:$KEYSTORE_ALIAS_PASSWORD
```

**What it does:**
1. Gets signing config from options or ENV
2. Runs `generateVersion()` to create `version.txt`
3. Calls private lane `buildAndSignApp` with:
    - `taskName: "assemble"`
    - `buildType: "Release"`
    - Signing properties
4. Injects signing config via Gradle properties:
   ```
   -Pandroid.injected.signing.store.file=keystores/release_keystore.keystore
   -Pandroid.injected.signing.store.password=xxx
   -Pandroid.injected.signing.key.alias=xxx
   -Pandroid.injected.signing.key.password=xxx
   ```

**Output:** Signed APK at `cmp-android/build/outputs/apk/prod/release/`

**Line:** 18

---

### Lane 3: `bundleReleaseApks`

**Purpose:** Build release AAB (Android App Bundle) with signing

**Usage:**
```bash
bundle exec fastlane android bundleReleaseApks \
  storeFile:release_keystore.keystore \
  storePassword:$KEYSTORE_PASSWORD \
  keyAlias:$KEYSTORE_ALIAS \
  keyPassword:$KEYSTORE_ALIAS_PASSWORD
```

**What it does:**
- Same as `assembleReleaseApks` but for AAB format
- Used for Play Store uploads

**Output:** AAB at `cmp-android/build/outputs/bundle/prodRelease/`

**Line:** 32

---

### Lane 4: `deployReleaseApkOnFirebase`

**Purpose:** Deploy production APK to Firebase App Distribution

**Usage:**
```bash
bundle exec fastlane android deployReleaseApkOnFirebase \
  storeFile:release_keystore.keystore \
  storePassword:$KEYSTORE_PASSWORD \
  keyAlias:$KEYSTORE_ALIAS \
  keyPassword:$KEYSTORE_ALIAS_PASSWORD \
  groups:mifos-mobile-apps  # ⚠️ IGNORED (known bug)
```

**What it does:**
1. Gets signing config from options
2. Gets Firebase config from `FastlaneConfig.get_firebase_config(:android, :prod)`:
    - `appId`: Firebase Android prod app ID
    - `serviceCredsFile`: `secrets/firebaseAppDistributionServiceCredentialsFile.json`
    - `groups`: Tester groups
3. Generates version for Firebase platform:
    - Increments build number from latest Firebase release
4. Generates release notes from last commit
5. Builds prod APK: `./gradlew assembleProdRelease`
6. Uploads to Firebase App Distribution

**⚠️ CRITICAL BUG:** The `groups` parameter passed from GitHub Actions is **IGNORED**.
- Lane reads groups from `firebase_config[:groups]` (hardcoded from config)
- **Workaround:** Set `ENV['FIREBASE_GROUPS']` in GitHub Actions
- See [BUGS_AND_ISSUES.md](../docs/analysis/BUGS_AND_ISSUES.md#1-firebase-tester-groups-parameter-ignored)

**Output:** Uploaded to Firebase App Distribution

**Line:** 46

---

### Lane 5: `deployDemoApkOnFirebase`

**Purpose:** Deploy demo APK to Firebase App Distribution

**Usage:**
```bash
bundle exec fastlane android deployDemoApkOnFirebase \
  storeFile:release_keystore.keystore \
  storePassword:$KEYSTORE_PASSWORD \
  keyAlias:$KEYSTORE_ALIAS \
  keyPassword:$KEYSTORE_ALIAS_PASSWORD \
  groups:mifos-mobile-apps  # ⚠️ IGNORED (known bug)
```

**What it does:**
- Same as `deployReleaseApkOnFirebase` but for demo variant
- Uses Firebase demo app ID
- Builds: `./gradlew assembleDemoRelease`
- Uploads demo APK to Firebase

**⚠️ Same bug:** `groups` parameter ignored

**Output:** Uploaded to Firebase App Distribution

**Line:** 76

---

### Lane 6: `deployInternal`

**Purpose:** Deploy AAB to Play Store internal track

**Usage:**
```bash
bundle exec fastlane android deployInternal \
  storeFile:release_keystore.keystore \
  storePassword:$KEYSTORE_PASSWORD \
  keyAlias:$KEYSTORE_ALIAS \
  keyPassword:$KEYSTORE_ALIAS_PASSWORD
```

**What it does:**
1. Generates version for Play Store platform:
    - Gets latest version code from production + beta tracks
    - Increments by 1
2. Generates release notes
3. Writes release notes to `metadata/android/en-US/changelogs/default.txt`
4. Builds AAB: `./gradlew bundleProdRelease`
5. Uploads to Play Store internal track:
   ```ruby
   upload_to_play_store(
     track: 'internal',
     aab: build_paths[:prod_aab_path],
     skip_upload_metadata: true,
     skip_upload_images: true,
     skip_upload_screenshots: true
   )
   ```

**Play Store Config:**
- Uses `secrets/playStorePublishServiceCredentialsFile.json`

**Output:** Uploaded to Play Store internal track

**Line:** 108

---

### Lane 7: `promoteToBeta`

**Purpose:** Promote Play Store internal track → beta

**Usage:**
```bash
bundle exec fastlane android promoteToBeta
```

**What it does:**
- Promotes existing internal release to beta track
- No build required
- **Requires:** Internal release must exist

**⚠️ No Validation:** Doesn't check if internal release exists. See [BUGS_AND_ISSUES.md](../docs/analysis/BUGS_AND_ISSUES.md#5-production-promotion-has-no-validation).

**Output:** Promoted to beta track

**Line:** 139

---

### Lane 8: `promote_to_production` (Production Only)

**Purpose:** Promote Play Store beta track → production

**Usage:**
```bash
bundle exec fastlane android promote_to_production
```

**What it does:**
- Promotes existing beta release to production track
- No build required
- **Requires:** Beta release must exist

**⚠️ CRITICAL:**
- Production deployment
- Requires double confirmation
- Never bypass this lane

**⚠️ No Validation:** Doesn't check if beta release exists

**Output:** Promoted to production track

**Line:** 151

---

## iOS Lanes

### Lane 1: `build_ios`

**Purpose:** Build unsigned iOS IPA (debug build)

**Usage:**
```bash
bundle exec fastlane ios build_ios
```

**What it does:**
1. Installs CocoaPods dependencies: `pod install`
2. Builds iOS app without code signing:
   ```ruby
   build_ios_app(
     scheme: ios_config[:scheme],
     workspace: ios_config[:workspace_path],
     skip_codesigning: true,
     skip_archive: true
   )
   ```

**Output:** Unsigned IPA at `cmp-ios/build/iosApp.ipa`

**Line:** 436

---

### Lane 2: `build_signed_ios`

**Purpose:** Build signed iOS IPA (release build)

**Usage:**
```bash
bundle exec fastlane ios build_signed_ios
```

**What it does:**
1. Installs CocoaPods dependencies
2. Sets up CI environment (if running on CI)
3. Loads App Store Connect API key:
    - `key_id`: From ENV or config
    - `issuer_id`: From ENV or config
    - `key_filepath`: `secrets/AuthKey.p8`
4. Fetches certificates with Fastlane Match:
    - `type: "adhoc"` (for Firebase)
    - `readonly: false` (can create new certs if needed)
    - Uses SSH key from `secrets/match_ci_key`
    - Clones certificates from Match Git repository
5. Builds signed IPA:
   ```ruby
   build_ios_app(
     scheme: ios_config[:scheme],
     workspace: ios_config[:workspace_path],
     export_options: {
       provisioningProfiles: {
         app_identifier => provisioning_profile_name
       }
     },
     xcargs: "CODE_SIGN_STYLE=Manual ..."
   )
   ```

**Fastlane Match:**
- Repository: `git@github.com:openMF/ios-provisioning-profile.git`
- Branch: `master`
- SSH Key: `secrets/match_ci_key`

**Output:** Signed IPA at `cmp-ios/build/iosApp.ipa`

**Line:** 456

---

### Lane 3: `deploy_on_firebase`

**Purpose:** Deploy iOS IPA to Firebase App Distribution

**Usage:**
```bash
bundle exec fastlane ios deploy_on_firebase
```

**What it does:**
1. Calls `increment_version`:
    - Gets version from Gradle (full semver - Firebase accepts pre-release)
    - Sets version in Xcode project
    - Gets latest Firebase build number and increments
2. Calls `build_signed_ios` (adhoc provisioning)
3. Generates release notes from last commit
4. Uploads to Firebase App Distribution:
   ```ruby
   firebase_app_distribution(
     app: firebase_config[:appId],
     service_credentials_file: firebase_config[:serviceCredsFile],
     release_notes: releaseNotes,
     groups: firebase_config[:groups]
   )
   ```

**⚠️ CRITICAL BUG:** `groups` parameter from GitHub Actions ignored (same as Android)

**Version Format:**
- Gradle: `2026.1.1-beta.0.9+abc123`
- Firebase accepts: `2026.1.1-beta.0.9` (pre-release allowed)

**Output:** Uploaded to Firebase App Distribution

**Line:** 508

---

### Lane 4: `beta`

**Purpose:** Upload iOS build to TestFlight

**Usage:**
```bash
bundle exec fastlane ios beta
```

**What it does:**
1. Installs CocoaPods
2. Sets up CI environment
3. Loads App Store Connect API key
4. Fetches Match certificates (`type: "appstore"`)
5. Gets **sanitized version** from Gradle:
    - Gradle: `2026.1.1-beta.0.9+abc123`
    - **Sanitized for App Store:** `2026.1.9`
    - Format: `YYYY.M.{commitCount}`
6. Sets version in Xcode project
7. Gets latest TestFlight build number and increments
8. Builds signed IPA (appstore provisioning profile)
9. Generates release notes
10. Uploads to TestFlight with comprehensive metadata:
    ```ruby
    pilot(
      api_key: ...,
      beta_app_review_info: ...,
      beta_app_feedback_email: ...,
      groups: testflight_config[:groups],
      changelog: releaseNotes,
      submit_beta_review: true,
      distribute_external: true,
      notify_external_testers: true
    )
    ```

**Version Sanitization:**
- **Why:** App Store requires `MAJOR.MINOR.PATCH` format (max 3 integers)
- **How:** Extract year, month, commit count from Gradle version
- **Result:** `2026.1.9` (Year.Month.CommitCount)

See [Version Handling Guide](../docs/claude/version-handling.md)

**TestFlight Config:**
- Groups: `["mifos-mobile-apps"]`
- Auto-submit for beta review
- Distribute to external testers
- Notify testers when available

**Output:** Uploaded to TestFlight

**Line:** 532

---

### Lane 5: `release`

**Purpose:** Submit iOS app to App Store for review

**Usage:**
```bash
bundle exec fastlane ios release
```

**What it does:**
1. Installs CocoaPods
2. Sets up CI environment
3. Loads App Store Connect API key
4. Fetches Match certificates (`type: "appstore"`)
5. Gets **sanitized version** from Gradle
6. Gets latest TestFlight build number and increments
7. Updates `Info.plist` with required privacy strings:
   ```ruby
   plist['NSContactsUsageDescription'] = 'This app does not access...'
   plist['NSLocationWhenInUseUsageDescription'] = '...'
   plist['NSBluetoothAlwaysUsageDescription'] = '...'
   ```
8. Builds signed IPA
9. Generates release notes
10. Writes release notes to `fastlane/metadata/en-US/release_notes.txt`
11. Uploads to App Store with `deliver`:
    ```ruby
    deliver(
      api_key: ...,
      copyright: "#{year} Mifos Initiative",
      metadata_path: "./fastlane/metadata",
      skip_metadata: false,  # Upload release notes
      skip_screenshots: true,
      submit_for_review: appstore_config[:submit_for_review],
      automatic_release: true,
      app_review_information: ...,
      submission_information: ...
    )
    ```

**⚠️ CRITICAL:**
- Production App Store deployment
- Requires double confirmation
- Submits for review automatically

**Metadata:**
- **Release Notes:** Generated from conventional commits
- **Copyright:** Auto-updated with current year
- **Other Metadata:** Preserved from App Store Connect (description, screenshots)

**Output:** Submitted to App Store for review

**Line:** 635

---

## Configuration

### File Structure

```
fastlane-config/
├── project_config.rb     # Master configuration (ANDROID, IOS, IOS_SHARED)
├── android_config.rb     # Android-specific helpers
└── ios_config.rb         # iOS-specific helpers

fastlane/
├── Fastfile              # All lanes (755 lines)
├── config/
│   └── config_helpers.rb # Helper methods (get_firebase_config, etc.)
└── metadata/             # App Store metadata
    └── en-US/
        └── release_notes.txt
```

### `project_config.rb` (296 lines)

**Purpose:** Single source of truth for all deployment configuration

**Sections:**
1. **`ProjectConfig::ANDROID`** (App-specific):
    - `package_name`: `cmp.android.app`
    - `play_store_json_key`: Play Store credentials path
    - `apk_paths`: Prod/Demo APK paths
    - `aab_path`: AAB path
    - `keystore`: File, password, alias
    - `firebase`: Prod/Demo app IDs, tester groups

2. **`ProjectConfig::IOS`** (App-specific):
    - `app_identifier`: `org.mifos.kmp.template`
    - `firebase`: App ID, tester groups
    - Project paths (Xcode, workspace, plist)
    - Build configuration (scheme, output paths)
    - Metadata paths

3. **`ProjectConfig::IOS_SHARED`** (Shared across all iOS apps):
    - `team_id`: Apple Developer Team ID
    - `ci_provider`: CircleCI
    - **App Store Connect API:**
        - `key_id`, `issuer_id`, `key_filepath`
    - **Code Signing (Match):**
        - `match_git_url`, `match_git_branch`, `match_git_private_key`
        - Provisioning profile names
    - **TestFlight Config:**
        - Beta review info, feedback email, tester groups
    - **App Store Config:**
        - Review info, submission settings, export compliance

4. **`ProjectConfig::SHARED`** (Both platforms):
    - `firebase_service_credentials`: Shared Firebase credentials path

### Helper Methods

**`FastlaneConfig.get_android_signing_config(options)`:**
- Returns signing config from options → ENV → defaults
- Used by all Android build lanes

**`FastlaneConfig.get_firebase_config(platform, type)`:**
- Platform: `:android` or `:ios`
- Type: `:prod` or `:demo` (Android only)
- Returns: `{ appId:, serviceCredsFile:, groups: }`
- **⚠️ Bug:** Reads groups from ENV/config only, never from lane options

---

## Common Tasks

### Local Android Deployment

```bash
# Setup environment
export KEYSTORE_PASSWORD="xxx"
export KEYSTORE_ALIAS="kmp-project-template"
export KEYSTORE_ALIAS_PASSWORD="xxx"

# Deploy to Firebase (prod)
bundle exec fastlane android deployReleaseApkOnFirebase \
  storeFile:release_keystore.keystore \
  storePassword:$KEYSTORE_PASSWORD \
  keyAlias:$KEYSTORE_ALIAS \
  keyPassword:$KEYSTORE_ALIAS_PASSWORD

# Deploy to Play Store internal
bundle exec fastlane android deployInternal \
  storeFile:release_keystore.keystore \
  storePassword:$KEYSTORE_PASSWORD \
  keyAlias:$KEYSTORE_ALIAS \
  keyPassword:$KEYSTORE_ALIAS_PASSWORD

# Promote internal → beta
bundle exec fastlane android promoteToBeta

# ⚠️ Production promotion (double confirmation required)
bundle exec fastlane android promote_to_production
```

### Local iOS Deployment

```bash
# Setup environment (or use scripts/deploy_*.sh)
export MATCH_PASSWORD="xxx"
export APPSTORE_KEY_ID="ZVQ6W6P822"
export APPSTORE_ISSUER_ID="7ab9e361-9603-4c3e-b147-be3b0f816099"

# Deploy to Firebase
bundle exec fastlane ios deploy_on_firebase

# Deploy to TestFlight
bundle exec fastlane ios beta

# ⚠️ Deploy to App Store (double confirmation required)
bundle exec fastlane ios release
```

### Version Management

```bash
# Generate version file (used by all lanes)
./gradlew versionFile
cat version.txt  # e.g., 2026.1.1-beta.0.9+abc123

# Check iOS version sanitization
./scripts/check_ios_version.sh
```

### Secrets Setup

```bash
# Encode secrets for GitHub Actions
./keystore-manager.sh encode-secrets

# View current secrets
./keystore-manager.sh view

# Add secrets to GitHub repository
./keystore-manager.sh add
```

---

## Troubleshooting

### Android Issues

#### 1. Keystore not found

**Error:** `Keystore file not found at: /path/to/keystores/release_keystore.keystore`

**Cause:** Keystore file missing or wrong path

**Fix:**
```bash
# Generate keystore
./keystore-manager.sh generate

# Or use GitHub Actions to inflate from secret
echo $ORIGINAL_KEYSTORE_FILE | base64 --decode > keystores/release_keystore.keystore
```

---

#### 2. Wrong tester group in Firebase

**Error:** No error, but wrong testers receive build

**Cause:** `groups` parameter ignored (known bug)

**Fix:** Set environment variable before running lane:
```bash
export FIREBASE_GROUPS="my-custom-group"
bundle exec fastlane android deployReleaseApkOnFirebase ...
```

Or in GitHub Actions:
```yaml
env:
  FIREBASE_GROUPS: "my-custom-group"
```

---

#### 3. Version code conflict in Play Store

**Error:** `Version code 123 has already been used`

**Cause:** Version code not incremented

**Fix:**
- Lane automatically increments from latest production + beta
- If manual upload caused conflict, delete draft release in Play Console

---

### iOS Issues

#### 4. Match certificates not found

**Error:** `Could not find a matching code signing identity`

**Cause:** Match repo empty or SSH key not configured

**Fix:**
```bash
# Run iOS setup wizard
./scripts/setup_ios_complete.sh

# Manually verify Match
bundle exec fastlane match adhoc --readonly
bundle exec fastlane match appstore --readonly
```

---

#### 5. Version sanitization error

**Error:** `Invalid version number`

**Cause:** Gradle version format incompatible with App Store

**Fix:**
- Lane automatically sanitizes: `YYYY.M.D-beta.0.9` → `YYYY.M.9`
- Verify with: `./scripts/check_ios_version.sh`
- See [Version Handling Guide](../docs/claude/version-handling.md)

---

#### 6. CocoaPods installation fails

**Error:** `Pod install failed`

**Cause:** CocoaPods dependencies not found or network issue

**Fix:**
```bash
cd cmp-ios
pod repo update
pod install --repo-update
```

---

#### 7. TestFlight upload stuck "Processing"

**Not an error:** TestFlight processing can take 10-60 minutes

**Check status:**
- App Store Connect → TestFlight → Builds
- Email notification when ready

**Lane skips waiting:**
```ruby
skip_waiting_for_build_processing: true  # Saves CI minutes
```

---

#### 8. App Store submission rejected

**Cause:** Missing metadata, privacy policy, or review notes

**Fix:**
1. Check App Store Connect rejection reason
2. Update metadata in `fastlane/metadata/en-US/`
3. Re-run lane: `bundle exec fastlane ios release`

---

### General Issues

#### 9. Lane fails with "Secret not found"

**Cause:** Missing secret file

**Fix:**
```bash
# Check what's missing
ls -la secrets/

# Use keystore-manager to inflate
./keystore-manager.sh view  # See what should be there
```

---

#### 10. Build number conflict

**Cause:** TestFlight/Play Store already has that build number

**Fix:**
- Lanes automatically increment from latest
- If conflict persists, manually increment in Xcode/build.gradle

---

**Need more help?**
- [Deployment Playbook](../docs/claude/deployment-playbook.md)
- [Version Handling Guide](../docs/claude/version-handling.md)
- [Secrets Management Guide](../docs/claude/secrets-management.md)
- [Known Issues](../docs/analysis/BUGS_AND_ISSUES.md)

[← Back to Main](../CLAUDE.md)
