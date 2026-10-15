#!/bin/bash

# ==============================================================================
# iOS Deployment Verification Script
# ==============================================================================
# This script performs comprehensive verification of iOS deployment configuration
# ==============================================================================

set -e

# Color codes for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# Script directory and project root
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"
cd "$PROJECT_ROOT"

# Print functions
print_success() {
    echo -e "${GREEN}✓ $1${NC}"
}

print_error() {
    echo -e "${RED}✗ $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠ $1${NC}"
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

# Initialize counters
TOTAL_CHECKS=0
PASSED_CHECKS=0
FAILED_CHECKS=0
WARNING_CHECKS=0

# Check function
check() {
    TOTAL_CHECKS=$((TOTAL_CHECKS + 1))
    if eval "$2"; then
        print_success "$1"
        PASSED_CHECKS=$((PASSED_CHECKS + 1))
        return 0
    else
        print_error "$1"
        FAILED_CHECKS=$((FAILED_CHECKS + 1))
        return 1
    fi
}

# Warning function
warn() {
    TOTAL_CHECKS=$((TOTAL_CHECKS + 1))
    if eval "$2"; then
        print_success "$1"
        PASSED_CHECKS=$((PASSED_CHECKS + 1))
        return 0
    else
        print_warning "$1 (optional)"
        WARNING_CHECKS=$((WARNING_CHECKS + 1))
        return 0
    fi
}

# Print banner
print_section "🔍 iOS Deployment Verification"

# ============================================================================
# 1. Version Handling Verification
# ============================================================================
print_section "1. Version Handling"

check "Version check script exists" \
    "[ -f 'scripts/check_ios_version.sh' ]"

check "version.txt can be generated" \
    "cd '$PROJECT_ROOT' && ./gradlew versionFile > /dev/null 2>&1 && [ -f 'version.txt' ]"

if [ -f "version.txt" ]; then
    FULL_VERSION=$(cat version.txt)
    print_info "Current version: $FULL_VERSION"

    # Verify version format
    if [[ "$FULL_VERSION" =~ ^[0-9]+\.[0-9]+\.[0-9]+ ]]; then
        print_success "Version format is valid"
        PASSED_CHECKS=$((PASSED_CHECKS + 1))
    else
        print_error "Version format is invalid"
        FAILED_CHECKS=$((FAILED_CHECKS + 1))
    fi
    TOTAL_CHECKS=$((TOTAL_CHECKS + 1))
fi

check "Info.plist uses MARKETING_VERSION variable" \
    "grep -q '\$(MARKETING_VERSION)' 'cmp-ios/iosApp/Info.plist'"

check "Info.plist uses CURRENT_PROJECT_VERSION variable" \
    "grep -q '\$(CURRENT_PROJECT_VERSION)' 'cmp-ios/iosApp/Info.plist'"

check "Info.plist has no hardcoded CFBundleShortVersionString" \
    "! grep -A1 'CFBundleShortVersionString' 'cmp-ios/iosApp/Info.plist' | grep -q '<string>[0-9]'"

check "Xcode project has MARKETING_VERSION in Debug config" \
    "grep -q 'MARKETING_VERSION' 'cmp-ios/iosApp.xcodeproj/project.pbxproj'"

check "Xcode project has CURRENT_PROJECT_VERSION in Debug config" \
    "grep -q 'CURRENT_PROJECT_VERSION' 'cmp-ios/iosApp.xcodeproj/project.pbxproj'"

# ============================================================================
# 2. FastFile Configuration
# ============================================================================
print_section "2. FastFile Configuration"

check "FastFile exists" \
    "[ -f 'fastlane/FastFile' ]"

check "Version sanitization helper exists" \
    "grep -q 'get_version_from_gradle' 'fastlane/FastFile'"

check "Version sanitization has sanitize_for_appstore parameter" \
    "grep -q 'sanitize_for_appstore' 'fastlane/FastFile'"

check "Firebase lane exists" \
    "grep -q 'lane :deploy_on_firebase' 'fastlane/FastFile'"

check "TestFlight beta lane exists" \
    "grep -q 'lane :beta' 'fastlane/FastFile'"

check "App Store release lane exists" \
    "grep -q 'lane :release' 'fastlane/FastFile'"

check "Release lane generates release notes" \
    "grep -q 'generateReleaseNote()' 'fastlane/FastFile'"

check "Release lane creates metadata directory" \
    "grep -q 'FileUtils.mkdir_p' 'fastlane/FastFile'"

check "Release lane writes release_notes.txt" \
    "grep -q 'File.write(release_notes_path' 'fastlane/FastFile'"

check "Release lane has copyright parameter" \
    "grep -q 'copyright:' 'fastlane/FastFile'"

check "Deliver action has skip_metadata: false" \
    "grep -q 'skip_metadata: false' 'fastlane/FastFile'"

check "Deliver action has skip_screenshots: true" \
    "grep -q 'skip_screenshots: true' 'fastlane/FastFile'"

# ============================================================================
# 3. Configuration Files
# ============================================================================
print_section "3. Configuration Files"

check "project_config.rb exists" \
    "[ -f 'fastlane-config/project_config.rb' ]"

check "ios_config.rb exists" \
    "[ -f 'fastlane-config/ios_config.rb' ]"

check "project_config.rb defines IOS hash" \
    "grep -q 'IOS = {' 'fastlane-config/project_config.rb'"

check "project_config.rb defines IOS_SHARED hash" \
    "grep -q 'IOS_SHARED = {' 'fastlane-config/project_config.rb'"

check "project_config.rb has app_identifier" \
    "grep -q 'app_identifier:' 'fastlane-config/project_config.rb'"

check "project_config.rb has metadata_path" \
    "grep -q 'metadata_path:' 'fastlane-config/project_config.rb'"

check "skip_app_version_update is false (allows version creation)" \
    "grep -q 'skip_app_version_update: false' 'fastlane-config/project_config.rb'"

# ============================================================================
# 4. Export Compliance
# ============================================================================
print_section "4. Export Compliance"

check "Info.plist has ITSAppUsesNonExemptEncryption key" \
    "grep -q 'ITSAppUsesNonExemptEncryption' 'cmp-ios/iosApp/Info.plist'"

check "ITSAppUsesNonExemptEncryption is set to false" \
    "grep -A1 'ITSAppUsesNonExemptEncryption' 'cmp-ios/iosApp/Info.plist' | grep -q '<false/>'"

check "TestFlight lane has uses_non_exempt_encryption parameter" \
    "grep -q 'uses_non_exempt_encryption:' 'fastlane/FastFile'"

# ============================================================================
# 5. Required Secret Files
# ============================================================================
print_section "5. Required Secret Files"

check "secrets directory exists" \
    "[ -d 'secrets' ]"

warn "shared_keys.env exists" \
    "[ -f 'secrets/shared_keys.env' ]"

warn ".match_password exists" \
    "[ -f 'secrets/.match_password' ]"

warn "match_ci_key (SSH key) exists" \
    "[ -f 'secrets/match_ci_key' ]"

warn "AuthKey.p8 (App Store Connect API) exists" \
    "[ -f 'secrets/AuthKey.p8' ]"

warn "Firebase credentials exist" \
    "[ -f 'secrets/firebaseAppDistributionServiceCredentialsFile.json' ]"

# ============================================================================
# 6. Deployment Scripts
# ============================================================================
print_section "6. Deployment Scripts"

check "deploy_firebase.sh exists" \
    "[ -f 'scripts/deploy_firebase.sh' ]"

check "deploy_testflight.sh exists" \
    "[ -f 'scripts/deploy_testflight.sh' ]"

check "deploy_appstore.sh exists" \
    "[ -f 'scripts/deploy_appstore.sh' ]"

check "check_ios_version.sh exists" \
    "[ -f 'scripts/check_ios_version.sh' ]"

check "deploy_firebase.sh is executable" \
    "[ -x 'scripts/deploy_firebase.sh' ]"

check "deploy_testflight.sh is executable" \
    "[ -x 'scripts/deploy_testflight.sh' ]"

check "deploy_appstore.sh is executable" \
    "[ -x 'scripts/deploy_appstore.sh' ]"

check "check_ios_version.sh is executable" \
    "[ -x 'scripts/check_ios_version.sh' ]"

# ============================================================================
# 7. Documentation
# ============================================================================
print_section "7. Documentation"

check "IOS_DEPLOYMENT.md exists" \
    "[ -f 'docs/IOS_DEPLOYMENT.md' ]"

check "IOS_SETUP.md exists" \
    "[ -f 'docs/IOS_SETUP.md' ]"

check "IOS_DEPLOYMENT_CHECKLIST.md exists" \
    "[ -f 'docs/IOS_DEPLOYMENT_CHECKLIST.md' ]"

check "FASTLANE_CONFIGURATION.md exists" \
    "[ -f 'docs/FASTLANE_CONFIGURATION.md' ]"

# ============================================================================
# 8. Metadata Configuration
# ============================================================================
print_section "8. Metadata Configuration"

check "metadata directory is in .gitignore" \
    "grep -q '^fastlane/metadata' '.gitignore' || grep -q '^fastlane/metadata$' '.gitignore'"

check "screenshots directory is in .gitignore" \
    "grep -q '^fastlane/screenshots' '.gitignore' || grep -q '^fastlane/screenshots$' '.gitignore'"

# ============================================================================
# 9. Ruby & Bundler
# ============================================================================
print_section "9. Ruby & Bundler"

check "Ruby is installed" \
    "command -v ruby > /dev/null 2>&1"

check "Bundler is installed" \
    "command -v bundle > /dev/null 2>&1"

check "Gemfile exists" \
    "[ -f 'Gemfile' ]"

check "Gemfile.lock exists" \
    "[ -f 'Gemfile.lock' ]"

warn "Bundle dependencies are installed" \
    "bundle check > /dev/null 2>&1"

# ============================================================================
# 10. Xcode Configuration
# ============================================================================
print_section "10. Xcode Configuration"

if [[ "$OSTYPE" == "darwin"* ]]; then
    check "Xcode is installed" \
        "command -v xcodebuild > /dev/null 2>&1"

    check "Xcode project exists" \
        "[ -d 'cmp-ios/iosApp.xcodeproj' ]"

    check "Xcode workspace exists" \
        "[ -d 'cmp-ios/iosApp.xcworkspace' ]"
else
    print_warning "Skipping Xcode checks (not running on macOS)"
fi

# ============================================================================
# Summary
# ============================================================================
print_section "📊 Verification Summary"

echo "Total Checks: $TOTAL_CHECKS"
print_success "Passed: $PASSED_CHECKS"
if [ $FAILED_CHECKS -gt 0 ]; then
    print_error "Failed: $FAILED_CHECKS"
fi
if [ $WARNING_CHECKS -gt 0 ]; then
    print_warning "Warnings: $WARNING_CHECKS (optional checks)"
fi
echo

# Calculate pass rate
PASS_RATE=$(awk "BEGIN {printf \"%.1f\", ($PASSED_CHECKS / $TOTAL_CHECKS) * 100}")

if [ $FAILED_CHECKS -eq 0 ]; then
    print_section "✅ All Critical Checks Passed!"
    echo "Pass Rate: ${PASS_RATE}%"
    echo
    print_success "iOS deployment configuration is ready for automatic deployment!"
    echo
    if [ $WARNING_CHECKS -gt 0 ]; then
        print_info "Note: ${WARNING_CHECKS} optional checks failed (secret files)"
        print_info "These are expected if you haven't set up secrets yet"
        print_info "Run: bash scripts/setup_ios_complete.sh"
    fi
    exit 0
else
    print_section "❌ Verification Failed"
    echo "Pass Rate: ${PASS_RATE}%"
    echo
    print_error "${FAILED_CHECKS} critical check(s) failed"
    print_info "Please review the errors above and fix the issues"
    print_info "See docs/IOS_DEPLOYMENT_CHECKLIST.md for detailed troubleshooting"
    exit 1
fi
