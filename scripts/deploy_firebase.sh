#!/bin/bash

# ==============================================================================
# iOS Firebase App Distribution Deployment Script
# ==============================================================================
# This script deploys your iOS app to Firebase App Distribution for testing
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
print_section "🚀 iOS Firebase App Distribution Deployment"

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
    "secrets/firebaseAppDistributionServiceCredentialsFile.json"
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

# Load Match password
export MATCH_PASSWORD=$(cat secrets/.match_password)

# Setup SSH for Match
export GIT_SSH_COMMAND="ssh -i secrets/match_ci_key -o IdentitiesOnly=yes"

print_success "Configuration loaded"

# Display configuration summary
print_section "📱 Deployment Configuration"
echo "Team ID: ${TEAM_ID}"
echo "Match Repository: ${MATCH_GIT_URL}"
echo "Match Branch: ${MATCH_GIT_BRANCH}"
echo

# Confirmation prompt
print_warning "This will build and deploy your iOS app to Firebase App Distribution"
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
print_section "🚀 Deploying to Firebase"
bundle exec fastlane ios deploy_on_firebase

# Check if deployment was successful
if [ $? -eq 0 ]; then
    print_section "✅ Deployment Successful!"
    print_success "Your iOS app has been deployed to Firebase App Distribution"
    print_info "Testers will receive a notification with download instructions"
else
    print_section "❌ Deployment Failed"
    print_error "Please check the error messages above"
    print_info "Common issues:"
    echo "  - Invalid Match password"
    echo "  - SSH key not added to Match repository"
    echo "  - Invalid Firebase credentials"
    echo "  - Certificate/provisioning profile issues"
    exit 1
fi
