#!/bin/bash

# ==============================================================================
# iOS App Store Production Deployment Script
# ==============================================================================
# This script deploys your iOS app to the App Store for production release
# ==============================================================================

set -e  # Exit on any error

# Color codes for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
MAGENTA='\033[0;35m'
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

print_critical() {
    echo -e "${MAGENTA}⚠️  CRITICAL: $1${NC}"
}

print_section() {
    echo
    echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
    echo -e "${BLUE}$1${NC}"
    echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
    echo
}

# Print banner
print_section "🚀 iOS App Store Production Deployment"

# Check if running on macOS
if [[ "$OSTYPE" != "darwin"* ]]; then
    print_error "This script must be run on macOS"
    exit 1
fi

# Validate prerequisites
print_info "Checking prerequisites..."

# Check for Xcode
if ! command -v xcodebuild &> /dev/null; then
    print_error "Xcode is not installed"
    print_info "Install Xcode from the App Store"
    exit 1
fi
print_success "Xcode installed"

# Check for Ruby
if ! command -v ruby &> /dev/null; then
    print_error "Ruby is not installed"
    exit 1
fi
print_success "Ruby installed"

# Check for Bundler
if ! command -v bundle &> /dev/null; then
    print_error "Bundler is not installed"
    print_info "Install with: gem install bundler"
    exit 1
fi
print_success "Bundler installed"

# Validate required files
print_section "📋 Validating Configuration"

REQUIRED_FILES=(
    "secrets/shared_keys.env"
    "secrets/.match_password"
    "secrets/match_ci_key"
    "secrets/AuthKey.p8"
)

MISSING_FILES=()

for file in "${REQUIRED_FILES[@]}"; do
    if [ ! -f "$file" ]; then
        MISSING_FILES+=("$file")
    else
        print_success "Found: $file"
    fi
done

if [ ${#MISSING_FILES[@]} -gt 0 ]; then
    print_error "Missing required files:"
    for file in "${MISSING_FILES[@]}"; do
        echo "  - $file"
    done
    echo
    print_info "Run the iOS setup wizard: bash scripts/setup_ios_complete.sh"
    exit 1
fi

# Load shared configuration
print_info "Loading iOS shared configuration..."
source secrets/shared_keys.env

# Validate App Store Connect API key configuration
if [ -z "$APPSTORE_KEY_ID" ] || [ -z "$APPSTORE_ISSUER_ID" ]; then
    print_error "App Store Connect API credentials not configured"
    print_info "Update secrets/shared_keys.env with APPSTORE_KEY_ID and APPSTORE_ISSUER_ID"
    exit 1
fi

# Load Match password
export MATCH_PASSWORD=$(cat secrets/.match_password)

# Setup SSH for Match
export GIT_SSH_COMMAND="ssh -i secrets/match_ci_key -o IdentitiesOnly=yes"

print_success "Configuration loaded"

# Display configuration summary
print_section "📱 Deployment Configuration"
echo "Team ID: ${TEAM_ID}"
echo "App Store Connect Key ID: ${APPSTORE_KEY_ID}"
echo "Match Repository: ${MATCH_GIT_URL}"
echo "Match Branch: ${MATCH_GIT_BRANCH}"
echo

# Important warnings for App Store submission
print_section "⚠️  IMPORTANT: App Store Submission Checklist"
print_critical "This is a PRODUCTION deployment to the App Store!"
echo
print_warning "Before proceeding, ensure you have:"
echo "  ✓ Tested the app thoroughly on physical devices"
echo "  ✓ Verified all features work as expected"
echo "  ✓ Checked for crashes and bugs"
echo "  ✓ Tested on multiple iOS versions (if supporting multiple)"
echo "  ✓ Verified in-app purchases work (if applicable)"
echo "  ✓ Reviewed app metadata in App Store Connect:"
echo "    - App Description"
echo "    - Screenshots (all required sizes)"
echo "    - Privacy Policy URL"
echo "    - Support URL"
echo "    - Age Rating"
echo "    - Keywords"
echo "  ✓ Prepared release notes for this version"
echo "  ✓ Set up app pricing and availability"
echo

print_section "🔍 App Store Review Process"
print_info "What happens after submission:"
echo "  1. Binary upload to App Store Connect (this script)"
echo "  2. Binary processing by Apple (10-30 minutes)"
echo "  3. Submission for App Store review (automatic)"
echo "  4. 'Waiting for Review' status (can take hours to days)"
echo "  5. 'In Review' status (Apple is reviewing)"
echo "  6. Review decision:"
echo "     ✓ Approved → App goes live automatically (or on release date)"
echo "     ✗ Rejected → Fix issues and resubmit"
echo
print_warning "App Store review typically takes 24-72 hours"
print_warning "Rejections are common - be prepared to fix issues and resubmit"
echo

print_section "⚙️  Deployment Options"
echo "This script will use settings from fastlane-config/project_config.rb:"
echo "  - Submit for Review: ${APPSTORE_SUBMIT_FOR_REVIEW:-true}"
echo "  - Automatic Release: ${APPSTORE_AUTOMATIC_RELEASE:-true}"
echo

print_info "You can override these with command-line options:"
echo "  --submit-for-review=false   Skip auto-submit (manual submit later)"
echo "  --automatic-release=false   Manual release after approval"
echo

# Parse command-line arguments
SUBMIT_FOR_REVIEW=""
AUTOMATIC_RELEASE=""

for arg in "$@"; do
    case $arg in
        --submit-for-review=*)
        SUBMIT_FOR_REVIEW="${arg#*=}"
        ;;
        --automatic-release=*)
        AUTOMATIC_RELEASE="${arg#*=}"
        ;;
        --help)
        echo "Usage: bash scripts/deploy_appstore.sh [OPTIONS]"
        echo
        echo "Options:"
        echo "  --submit-for-review=true|false   Submit for review after upload (default: true)"
        echo "  --automatic-release=true|false   Automatically release after approval (default: true)"
        echo "  --help                           Show this help message"
        exit 0
        ;;
    esac
done

# Final confirmation with double-check
print_section "⚠️  FINAL CONFIRMATION"
print_critical "You are about to deploy to the PRODUCTION App Store!"
print_critical "This action CANNOT be undone!"
echo
print_warning "Are you absolutely sure you want to continue?"
read -p "Type 'YES' (in capital letters) to confirm: " CONFIRM

if [ "$CONFIRM" != "YES" ]; then
    print_info "Deployment cancelled"
    exit 0
fi

# Second confirmation for extra safety
read -p "Final confirmation - deploy to App Store? [y/N]: " -n 1 -r
echo

if [[ ! $REPLY =~ ^[Yy]$ ]]; then
    print_info "Deployment cancelled"
    exit 0
fi

# Install/Update Fastlane dependencies
print_section "📦 Installing Dependencies"
bundle install
print_success "Dependencies installed"

# Build deployment command
FASTLANE_CMD="bundle exec fastlane ios release"

if [ -n "$SUBMIT_FOR_REVIEW" ]; then
    FASTLANE_CMD="$FASTLANE_CMD submit_for_review:$SUBMIT_FOR_REVIEW"
fi

if [ -n "$AUTOMATIC_RELEASE" ]; then
    FASTLANE_CMD="$FASTLANE_CMD automatic_release:$AUTOMATIC_RELEASE"
fi

# Run Fastlane deployment
print_section "🚀 Deploying to App Store"
print_info "This process will:"
echo "  1. Sync code signing certificates with Match"
echo "  2. Increment version and build number"
echo "  3. Update Info.plist with privacy descriptions"
echo "  4. Build signed IPA"
echo "  5. Upload binary to App Store Connect"
echo "  6. Submit for App Store review (if enabled)"
echo

eval $FASTLANE_CMD

# Check if deployment was successful
if [ $? -eq 0 ]; then
    print_section "✅ Deployment Successful!"
    print_success "Your iOS app has been uploaded to App Store Connect"

    if [ "$SUBMIT_FOR_REVIEW" != "false" ]; then
        print_info "Next steps:"
        echo "  1. Monitor review status in App Store Connect"
        echo "  2. Check https://appstoreconnect.apple.com/"
        echo "  3. Go to App Store → iOS App → App Store"
        echo "  4. Watch for status changes:"
        echo "     - Processing → Waiting for Review → In Review → Approved/Rejected"
        echo
        print_warning "Review typically takes 24-72 hours"

        if [ "$AUTOMATIC_RELEASE" != "false" ]; then
            print_warning "App will go live AUTOMATICALLY after approval!"
        else
            print_info "You'll need to manually release the app after approval"
        fi
    else
        print_info "Binary uploaded but NOT submitted for review"
        print_info "Go to App Store Connect to manually submit when ready"
    fi

    echo
    print_info "Useful links:"
    echo "  - App Store Connect: https://appstoreconnect.apple.com/"
    echo "  - App Store Review Guidelines: https://developer.apple.com/app-store/review/guidelines/"
    echo "  - Common Rejection Reasons: https://developer.apple.com/app-store/review/"
else
    print_section "❌ Deployment Failed"
    print_error "Please check the error messages above"
    print_info "Common issues:"
    echo "  - Invalid Match password"
    echo "  - SSH key not added to Match repository"
    echo "  - Invalid App Store Connect API key"
    echo "  - Certificate/provisioning profile issues"
    echo "  - Build number conflicts (already uploaded)"
    echo "  - Missing app metadata in App Store Connect"
    echo "  - Missing required assets (screenshots, app icon, etc.)"
    echo "  - Export compliance information missing"
    exit 1
fi
