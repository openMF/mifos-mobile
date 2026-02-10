# iOS Build Workflows

This document describes the GitHub Actions workflows for building and distributing the Zero Rate iOS app.

## 📋 Available Workflows

### 1. **iOS Build** (`ios-build.yml`)
Basic iOS build workflow for development and testing.

**Triggers:**
- Manual dispatch via GitHub Actions UI
- Push to `development` or `main` branches (iOS-related files)
- Pull requests to `development` or `main` branches

**Options:**
- **Build Configuration**: Debug or Release
- **Create IPA**: Optionally create an IPA archive

**Usage:**
1. Go to GitHub Actions tab
2. Select "iOS Build" workflow
3. Click "Run workflow"
4. Choose configuration and options
5. Click "Run workflow"

**Output:**
- Build logs and status
- IPA file (if requested) available in Artifacts

---

### 2. **iOS Release Build** (`ios-release.yml`)
Production-ready iOS build with code signing and distribution.

**Triggers:**
- Manual dispatch only (for controlled releases)

**Distribution Methods:**
- **Firebase**: Distribute to Firebase App Distribution (for testers)
- **TestFlight**: Upload to TestFlight for beta testing
- **App Store**: Submit to App Store for review

**Required Secrets:**
See "Required Secrets" section below.

**Usage:**
1. Go to GitHub Actions tab
2. Select "iOS Release Build" workflow
3. Click "Run workflow"
4. Choose distribution method
5. Add release notes
6. Click "Run workflow"

---

### 3. **Multi-Platform Build** (`multi-platform-build-and-publish.yml`)
Comprehensive workflow that builds Android, iOS, Desktop, and Web.

**iOS Options:**
- `distribute_ios_firebase`: Distribute via Firebase
- `distribute_ios_testflight`: Distribute via TestFlight
- `distribute_ios_appstore`: Submit to App Store

---

## 🔑 Required Secrets

Configure these secrets in **Settings > Secrets and variables > Actions**:

### Code Signing (for TestFlight/App Store)
```
KEYCHAIN_PASSWORD              # Keychain password for CI
CERTIFICATES_PASSWORD          # Certificate password
MATCH_PASSWORD                 # Match encryption password
MATCH_SSH_PRIVATE_KEY         # SSH key for match repository
```

### Apple Developer
```
NOTARIZATION_APPLE_ID         # Apple ID email
NOTARIZATION_PASSWORD         # App-specific password
NOTARIZATION_TEAM_ID          # Apple Developer Team ID
APPSTORE_KEY_ID               # App Store Connect API Key ID
APPSTORE_ISSUER_ID            # App Store Connect Issuer ID
APPSTORE_AUTH_KEY             # App Store Connect API Key (base64)
```

### Firebase (for Firebase distribution)
```
FIREBASE_IOS_APP_ID           # Firebase iOS App ID
FIREBASE_TOKEN                # Firebase CI token
```

---

## 🚀 Quick Start Guide

### Development Builds (No signing required)

1. **Trigger the iOS Build workflow:**
   ```
   Actions > iOS Build > Run workflow
   ```

2. **Select options:**
   - Configuration: Debug
   - Create IPA: Yes

3. **Download the IPA:**
   - Wait for workflow to complete
   - Go to workflow run page
   - Download artifact: `Zero-Rate-iOS-<run-number>`

### Release Builds (Requires code signing)

1. **Set up code signing:**
   - Configure all required secrets (see above)
   - Ensure certificates are valid

2. **Trigger the iOS Release Build workflow:**
   ```
   Actions > iOS Release Build > Run workflow
   ```

3. **Choose distribution:**
   - Firebase: For internal/beta testers
   - TestFlight: For public beta testing
   - App Store: For production release

---

## 🔧 Local Development

### Prerequisites
- macOS with Xcode 16.1+
- CocoaPods installed
- JDK 21

### Build locally

```bash
# 1. Build shared KMP framework
./gradlew :cmp-shared:assembleXCFramework

# 2. Install CocoaPods dependencies
cd cmp-ios
pod install

# 3. Open in Xcode
open iosApp.xcworkspace

# 4. Build and run in Xcode or via command line
xcodebuild \
  -workspace iosApp.xcworkspace \
  -scheme iosApp \
  -configuration Debug \
  -sdk iphonesimulator \
  build
```

---

## 📦 Build Output

### Debug Builds
- Used for development and testing
- Not code-signed
- Cannot be installed on physical devices
- Available as artifacts

### Release Builds
- Production-ready builds
- Code-signed with distribution certificates
- Can be distributed via:
  - **Firebase**: Internal testing
  - **TestFlight**: Beta testing (up to 10,000 testers)
  - **App Store**: Public release

---

## 🐛 Troubleshooting

### Build fails: "No profiles for 'org.mifos.mobile' were found"
- **Solution**: Configure code signing secrets or use Debug configuration

### CocoaPods installation fails
- **Solution**: Check Podfile and run `pod install` locally to verify

### Shared framework not found
- **Solution**: Ensure `./gradlew :cmp-shared:assembleXCFramework` runs successfully

### Xcode version mismatch
- **Solution**: Update `xcode-version` in workflow file to match your setup

---

## 📚 Additional Resources

- [Apple Developer Documentation](https://developer.apple.com/documentation/)
- [Fastlane Documentation](https://docs.fastlane.tools/)
- [Firebase App Distribution](https://firebase.google.com/docs/app-distribution)
- [TestFlight Beta Testing](https://developer.apple.com/testflight/)

---

## 🔄 Workflow Diagram

```
┌─────────────────┐
│  Push/PR/Manual │
└────────┬────────┘
         │
         ▼
┌────────────────────┐
│  Checkout Code     │
└────────┬───────────┘
         │
         ▼
┌────────────────────┐
│  Setup Environment │
│  - JDK 21          │
│  - Xcode 16.1      │
│  - CocoaPods       │
└────────┬───────────┘
         │
         ▼
┌────────────────────┐
│  Build KMP Shared  │
│  Framework         │
└────────┬───────────┘
         │
         ▼
┌────────────────────┐
│  Install Pods      │
└────────┬───────────┘
         │
         ▼
┌────────────────────┐
│  Build iOS App     │
└────────┬───────────┘
         │
         ▼
┌────────────────────┐
│  Create IPA        │
│  (if requested)    │
└────────┬───────────┘
         │
         ▼
┌────────────────────┐
│  Distribute        │
│  - Artifacts       │
│  - Firebase        │
│  - TestFlight      │
│  - App Store       │
└────────────────────┘
```

---

## 📝 Notes

- **Zero Rate Branding**: All workflows build the Zero Rate branded app
- **Bundle ID**: `org.mifos.mobile` (update in Config.xcconfig to change)
- **Team ID**: `L432S2FZP5` (set in Config.xcconfig)
- **Minimum iOS**: 16.0 (set in Podfile)

---

For questions or issues, please open an issue in the repository.
