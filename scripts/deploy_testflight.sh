#!/bin/bash

# ==============================================================================
# iOS TestFlight Deployment Script
# ==============================================================================
# This script deploys your iOS app to TestFlight for beta testing
# ==============================================================================

set -e  # Exit on any error

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

# Print banner
print_section "🚀 iOS TestFlight Beta Deployment"

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
echo "TestFlight Groups: ${TESTFLIGHT_GROUPS:-mifos-mobile-apps}"
echo

# Important warnings
print_section "⚠️  Important Information"
print_warning "TestFlight Deployment Process:"
echo "  1. Build will be uploaded to App Store Connect"
echo "  2. Build will be submitted for beta review by Apple"
echo "  3. Review typically takes 24-48 hours"
echo "  4. Once approved, testers will be notified automatically"
echo

print_warning "Beta Review Information:"
echo "  - Contact Email: ${TESTFLIGHT_CONTACT_EMAIL:-team@mifos.org}"
echo "  - Contact Name: ${TESTFLIGHT_FIRST_NAME:-Mifos} ${TESTFLIGHT_LAST_NAME:-Initiative}"
echo "  - Contact Phone: ${TESTFLIGHT_PHONE:-+1234567890}"
echo

# Confirmation prompt
print_warning "This will build and deploy your iOS app to TestFlight"
read -p "Do you want to continue? [y/N]: " -n 1 -r
echo

if [[ ! $REPLY =~ ^[Yy]$ ]]; then
    print_info "Deployment cancelled"
    exit 0
fi

# Install/Update Fastlane dependencies
print_section "📦 Installing Dependencies"
bundle install
print_success "Dependencies installed"

# Run Fastlane deployment
print_section "🚀 Deploying to TestFlight"
print_info "This process will:"
echo "  1. Sync code signing certificates with Match"
echo "  2. Increment version and build number"
echo "  3. Build signed IPA"
echo "  4. Upload to App Store Connect"
echo "  5. Submit for beta review"
echo

bundle exec fastlane ios beta

# Check if deployment was successful
if [ $? -eq 0 ]; then
    print_section "✅ Deployment Successful!"
    print_success "Your iOS app has been uploaded to TestFlight"
    print_info "Next steps:"
    echo "  1. Monitor beta review status in App Store Connect"
    echo "  2. Check https://appstoreconnect.apple.com/"
    echo "  3. Go to TestFlight → iOS → Builds"
    echo "  4. Wait for 'Ready to Submit' → 'Waiting for Review' → 'In Review' → 'Approved'"
    echo "  5. Once approved, testers will receive notifications"
    echo
    print_info "Beta review typically takes 24-48 hours"
else
    print_section "❌ Deployment Failed"
    print_error "Please check the error messages above"
    print_info "Common issues:"
    echo "  - Invalid Match password"
    echo "  - SSH key not added to Match repository"
    echo "  - Invalid App Store Connect API key"
    echo "  - Certificate/provisioning profile issues"
    echo "  - Build number conflicts (already uploaded)"
    echo "  - Missing beta review information"
    exit 1
fi
