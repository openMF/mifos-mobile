#!/bin/bash

# ==============================================================================
# iOS Version Check Script
# ==============================================================================
# This script displays current iOS version configuration
# ==============================================================================

set -e

# Color codes
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

print_success() {
    echo -e "${GREEN}✓ $1${NC}"
}

print_info() {
    echo -e "${CYAN}ℹ $1${NC}"
}

print_section() {
    echo
    echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
    echo -e "${BLUE}$1${NC}"
    echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
    echo
}

# Navigate to project root
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"
cd "$PROJECT_ROOT"

print_section "📱 iOS Version Configuration Check"

# Check version.txt (Gradle-generated)
if [ -f "version.txt" ]; then
    VERSION_TXT=$(cat version.txt)

    # Extract App Store version: Year.Month.CommitCount
    # From: 2026.1.1-beta.0.9 → To: 2026.1.9
    BASE_VERSION=$(echo "$VERSION_TXT" | cut -d'-' -f1 | cut -d'+' -f1)
    YEAR=$(echo "$BASE_VERSION" | cut -d'.' -f1)
    MONTH=$(echo "$BASE_VERSION" | cut -d'.' -f2)

    # Extract commit count from pre-release identifier
    if echo "$VERSION_TXT" | grep -q '\-'; then
        PRE_RELEASE=$(echo "$VERSION_TXT" | cut -d'-' -f2 | cut -d'+' -f1)
        COMMIT_COUNT=$(echo "$PRE_RELEASE" | rev | cut -d'.' -f1 | rev)
    else
        COMMIT_COUNT="0"
    fi

    APPSTORE_VERSION="${YEAR}.${MONTH}.${COMMIT_COUNT}"

    print_success "Gradle version.txt: $VERSION_TXT"
    if [ "$VERSION_TXT" != "$APPSTORE_VERSION" ]; then
        print_info "App Store sanitized version: $APPSTORE_VERSION"
        echo "  (Format: Year.Month.CommitCount - commit count extracted from pre-release)"
    fi
else
    echo "⚠️  version.txt not found. Run: ./gradlew versionFile"
fi
echo

# Check Xcode project settings
print_info "Xcode Project Settings:"
MARKETING_VERSION=$(xcodebuild -project cmp-ios/iosApp.xcodeproj -showBuildSettings 2>/dev/null | grep "MARKETING_VERSION" | awk '{print $3}' | head -1)
CURRENT_PROJECT_VERSION=$(xcodebuild -project cmp-ios/iosApp.xcodeproj -showBuildSettings 2>/dev/null | grep "CURRENT_PROJECT_VERSION" | awk '{print $3}' | head -1)

if [ -n "$MARKETING_VERSION" ]; then
    print_success "MARKETING_VERSION: $MARKETING_VERSION"
else
    echo "⚠️  MARKETING_VERSION not found"
fi

if [ -n "$CURRENT_PROJECT_VERSION" ]; then
    print_success "CURRENT_PROJECT_VERSION: $CURRENT_PROJECT_VERSION"
else
    echo "⚠️  CURRENT_PROJECT_VERSION not found"
fi
echo

# Check Info.plist
print_info "Info.plist Configuration:"
SHORT_VERSION=$(plutil -p cmp-ios/iosApp/Info.plist 2>/dev/null | grep CFBundleShortVersionString | awk -F'"' '{print $4}')
BUNDLE_VERSION=$(plutil -p cmp-ios/iosApp/Info.plist 2>/dev/null | grep "\"CFBundleVersion\"" | awk -F'"' '{print $4}')

echo "  CFBundleShortVersionString: $SHORT_VERSION"
echo "  CFBundleVersion: $BUNDLE_VERSION"

if [[ "$SHORT_VERSION" == "\$(MARKETING_VERSION)" ]]; then
    print_success "Using dynamic versioning (MARKETING_VERSION)"
else
    echo "  ⚠️  Warning: Info.plist has hardcoded version"
fi

if [[ "$BUNDLE_VERSION" == "\$(CURRENT_PROJECT_VERSION)" ]]; then
    print_success "Using dynamic build number (CURRENT_PROJECT_VERSION)"
else
    echo "  ⚠️  Warning: Info.plist has hardcoded build number"
fi
echo

# Summary
print_section "📊 Summary"
echo "When you deploy iOS:"
echo "  1. Fastlane runs: gradle(tasks: [\"versionFile\"])"
echo "  2. Reads version from: version.txt"
if [ -n "$VERSION_TXT" ] && [ -n "$APPSTORE_VERSION" ]; then
    echo "     • Full version: $VERSION_TXT"
    if [ "$VERSION_TXT" != "$APPSTORE_VERSION" ]; then
        echo "     • TestFlight/App Store version: $APPSTORE_VERSION (sanitized)"
    fi
else
    echo "     • Version: (version.txt not found)"
fi
echo "  3. Updates Xcode MARKETING_VERSION accordingly"
echo "  4. Auto-increments CURRENT_PROJECT_VERSION from TestFlight/Firebase"
echo "  5. Info.plist uses: \$(MARKETING_VERSION) and \$(CURRENT_PROJECT_VERSION)"
echo
echo "Note: Firebase accepts full semantic versions with pre-release identifiers"
echo "      TestFlight/App Store require sanitized versions (MAJOR.MINOR.PATCH only)"
echo

print_info "To update version for next release:"
echo "  1. Update version in Gradle (where project.version is defined)"
echo "  2. Run: ./gradlew versionFile"
echo "  3. Deploy: bash scripts/deploy_testflight.sh"
echo
