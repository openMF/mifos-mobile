fastlane documentation
----

# Installation

Make sure you have the latest version of the Xcode command line tools installed:

```sh
xcode-select --install
```

For _fastlane_ installation instructions, see [Installing _fastlane_](https://docs.fastlane.tools/#installing-fastlane)

# Available Actions

## Android

### android assembleDebugApks

```sh
[bundle exec] fastlane android assembleDebugApks
```

Assemble debug APKs.

### android assembleReleaseApks

```sh
[bundle exec] fastlane android assembleReleaseApks
```

Assemble Release APK

### android bundlePlayStoreRelease

```sh
[bundle exec] fastlane android bundlePlayStoreRelease
```

Bundle Play Store release

### android deploy_on_firebase

```sh
[bundle exec] fastlane android deploy_on_firebase
```

Publish Release Play Store artifacts to Firebase App Distribution

### android deploy_internal

```sh
[bundle exec] fastlane android deploy_internal
```

Deploy internal tracks to Google Play

### android promote_to_beta

```sh
[bundle exec] fastlane android promote_to_beta
```

Promote internal tracks to beta on Google Play

### android promote_to_production

```sh
[bundle exec] fastlane android promote_to_production
```

Promote beta tracks to production on Google Play

### android generateVersion

```sh
[bundle exec] fastlane android generateVersion
```

Generate Play Store Version

### android generateFirebaseVersion

```sh
[bundle exec] fastlane android generateFirebaseVersion
```

Generate Firebase Version

### android generateReleaseNotes

```sh
[bundle exec] fastlane android generateReleaseNotes
```

Generate release notes

### android generateFullReleaseNote

```sh
[bundle exec] fastlane android generateFullReleaseNote
```

Generate release notes from specified tag or latest release tag

----


## iOS

### ios build_ios

```sh
[bundle exec] fastlane ios build_ios
```

Build iOS application

### ios increment_version

```sh
[bundle exec] fastlane ios increment_version
```



### ios deploy_on_firebase

```sh
[bundle exec] fastlane ios deploy_on_firebase
```

Upload iOS application to Firebase App Distribution

### ios generateReleaseNotes

```sh
[bundle exec] fastlane ios generateReleaseNotes
```

Generate release notes

----

This README.md is auto-generated and will be re-generated every time [_fastlane_](https://fastlane.tools) is run.

More information about _fastlane_ can be found on [fastlane.tools](https://fastlane.tools).

The documentation of _fastlane_ can be found on [docs.fastlane.tools](https://docs.fastlane.tools).
