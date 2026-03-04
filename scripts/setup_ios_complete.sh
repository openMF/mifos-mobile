#!/bin/bash

# ==============================================================================
# Complete iOS Deployment Setup Wizard
# ==============================================================================
# This script sets up everything needed for iOS deployment:
# - Shared iOS configuration (Team ID, API keys, Match repo)
# - Code signing certificates via Fastlane Match
# - SSH keys for Match repository access
# ==============================================================================

set -e  # Exit on any error

# Color codes
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
MAGENTA='\033[0;35m'
BOLD='\033[1m'
NC='\033[0m' # No Color

# Script directory
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
    echo -e "${BLUE}${BOLD}$1${NC}"
    echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
    echo
}

print_step() {
    echo -e "${MAGENTA}${BOLD}▶ Step $1: $2${NC}"
}

# Print banner
clear
echo -e "${BLUE}${BOLD}"
cat << "EOF"
╔═══════════════════════════════════════════════════════════════╗
║                                                               ║
║   🚀 iOS Deployment Setup Wizard                             ║
║                                                               ║
║   This wizard will guide you through setting up iOS          ║
║   deployment infrastructure for your project                 ║
║                                                               ║
╚═══════════════════════════════════════════════════════════════╝
EOF
echo -e "${NC}"

# Check prerequisites
print_section "1️⃣  Checking Prerequisites"

# Check macOS
if [[ "$OSTYPE" != "darwin"* ]]; then
    print_error "This script must be run on macOS"
    exit 1
fi
print_success "Running on macOS"

# Check Xcode
if ! command -v xcodebuild &> /dev/null; then
    print_error "Xcode is not installed"
    print_info "Install Xcode from the App Store"
    exit 1
fi
XCODE_VERSION=$(xcodebuild -version | head -n 1)
print_success "$XCODE_VERSION installed"

# Check Ruby
if ! command -v ruby &> /dev/null; then
    print_error "Ruby is not installed"
    exit 1
fi
RUBY_VERSION=$(ruby -v | awk '{print $2}')
print_success "Ruby $RUBY_VERSION installed"

# Check Bundler
if ! command -v bundle &> /dev/null; then
    print_warning "Bundler not installed, installing..."
    gem install bundler
fi
print_success "Bundler installed"

# Check Git
if ! command -v git &> /dev/null; then
    print_error "Git is not installed"
    exit 1
fi
print_success "Git installed"

# Check GitHub CLI (optional but helpful)
if ! command -v gh &> /dev/null; then
    print_warning "GitHub CLI (gh) not installed"
    print_info "Recommended for easier Match repository setup"
    print_info "Install: brew install gh"
else
    print_success "GitHub CLI installed"
fi

# Create secrets directory
print_section "2️⃣  Setting Up Secrets Directory"

if [ ! -d "secrets" ]; then
    mkdir -p secrets
    print_success "Created secrets/ directory"
else
    print_success "secrets/ directory exists"
fi

# Check if shared_keys.env already exists
if [ -f "secrets/shared_keys.env" ]; then
    print_warning "secrets/shared_keys.env already exists"
    read -p "Do you want to overwrite it? [y/N]: " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        print_info "Using existing secrets/shared_keys.env"
        print_info "You can edit it manually or delete it and run this script again"
        exit 0
    fi
fi

# Collect iOS configuration
print_section "3️⃣  Apple Developer Account Information"

print_info "You'll need information from your Apple Developer Account"
print_info "Log in to: https://developer.apple.com/account"
echo

# Team ID
print_step "3.1" "Team ID"
echo "Find your Team ID at: https://developer.apple.com/account -> Membership"
read -p "Enter your Apple Team ID (10 characters): " TEAM_ID
while [[ ! $TEAM_ID =~ ^[A-Z0-9]{10}$ ]]; do
    print_error "Invalid Team ID format. Must be 10 characters (letters and numbers)"
    read -p "Enter your Apple Team ID: " TEAM_ID
done
print_success "Team ID: $TEAM_ID"
echo

# App Store Connect API
print_section "4️⃣  App Store Connect API Configuration"
print_info "You need to create an API key for Fastlane to access App Store Connect"
echo
print_info "Steps to create API key:"
echo "  1. Go to: https://appstoreconnect.apple.com/access/api"
echo "  2. Click the '+' button to create a new key"
echo "  3. Name it: 'Fastlane Deploy Key'"
echo "  4. Select role: 'App Manager' or 'Admin'"
echo "  5. Click 'Generate'"
echo "  6. Download the .p8 key file (you can only do this once!)"
echo "  7. Note the Key ID (10 characters)"
echo "  8. Note the Issuer ID (UUID format)"
echo

read -p "Have you created an API key? [y/N]: " -n 1 -r
echo
if [[ ! $REPLY =~ ^[Yy]$ ]]; then
    print_warning "Please create an API key first, then run this script again"
    exit 0
fi

# API Key ID
print_step "4.1" "App Store Connect Key ID"
read -p "Enter your App Store Connect Key ID (10 characters): " APPSTORE_KEY_ID
while [[ ! $APPSTORE_KEY_ID =~ ^[A-Z0-9]{10}$ ]]; do
    print_error "Invalid Key ID format. Must be 10 characters"
    read -p "Enter Key ID: " APPSTORE_KEY_ID
done
print_success "Key ID: $APPSTORE_KEY_ID"

# Issuer ID
print_step "4.2" "App Store Connect Issuer ID"
read -p "Enter your Issuer ID (UUID format): " APPSTORE_ISSUER_ID
while [[ ! $APPSTORE_ISSUER_ID =~ ^[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}$ ]]; do
    print_error "Invalid Issuer ID format. Must be UUID format (xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx)"
    read -p "Enter Issuer ID: " APPSTORE_ISSUER_ID
done
print_success "Issuer ID: $APPSTORE_ISSUER_ID"

# API Key file
print_step "4.3" "App Store Connect .p8 Key File"
print_info "Looking for .p8 key file in current directory..."

# Find .p8 files
P8_FILES=($(find . -maxdepth 2 -name "AuthKey_*.p8" -o -name "*.p8" 2>/dev/null))

if [ ${#P8_FILES[@]} -eq 0 ]; then
    print_warning "No .p8 key files found"
    read -p "Enter the full path to your .p8 key file: " P8_PATH
else
    echo "Found .p8 files:"
    for i in "${!P8_FILES[@]}"; do
        echo "  $((i+1)). ${P8_FILES[$i]}"
    done
    read -p "Select file number (or enter path to another file): " SELECTION

    if [[ $SELECTION =~ ^[0-9]+$ ]] && [ $SELECTION -ge 1 ] && [ $SELECTION -le ${#P8_FILES[@]} ]; then
        P8_PATH="${P8_FILES[$((SELECTION-1))]}"
    else
        P8_PATH="$SELECTION"
    fi
fi

# Validate file exists
while [ ! -f "$P8_PATH" ]; do
    print_error "File not found: $P8_PATH"
    read -p "Enter the full path to your .p8 key file: " P8_PATH
done

# Copy to secrets directory
cp "$P8_PATH" "secrets/AuthKey.p8"
chmod 600 "secrets/AuthKey.p8"
print_success "Copied .p8 key to secrets/AuthKey.p8"
echo

# Fastlane Match Configuration
print_section "5️⃣  Fastlane Match Configuration"
print_info "Fastlane Match stores your iOS certificates and provisioning profiles"
print_info "in a private Git repository for sharing across team and CI/CD"
echo

print_info "You need a private Git repository for Match. Options:"
echo "  1. Create new private repo on GitHub/GitLab/Bitbucket"
echo "  2. Use existing repository"
echo

# Match repository URL
print_step "5.1" "Match Repository URL"
read -p "Enter Match repository URL (git@github.com:org/repo.git): " MATCH_GIT_URL
while [[ ! $MATCH_GIT_URL =~ ^git@.*\.git$ ]]; do
    print_error "Invalid Git URL format. Must start with 'git@' and end with '.git'"
    read -p "Enter Match repository URL: " MATCH_GIT_URL
done
print_success "Match Repository: $MATCH_GIT_URL"

# Match branch
print_step "5.2" "Match Repository Branch"
read -p "Enter Match repository branch [main]: " MATCH_GIT_BRANCH
MATCH_GIT_BRANCH=${MATCH_GIT_BRANCH:-main}
print_success "Match Branch: $MATCH_GIT_BRANCH"

# SSH Key for Match
print_step "5.3" "SSH Key for Match Repository"

if [ -f "secrets/match_ci_key" ]; then
    print_warning "SSH key already exists: secrets/match_ci_key"
    read -p "Do you want to generate a new one? [y/N]: " -n 1 -r
    echo
    GENERATE_NEW=$REPLY
else
    GENERATE_NEW="y"
fi

if [[ $GENERATE_NEW =~ ^[Yy]$ ]]; then
    print_info "Generating new SSH key pair for Match..."
    ssh-keygen -t ed25519 -C "fastlane-match-$TEAM_ID" -f "secrets/match_ci_key" -N ""
    chmod 600 "secrets/match_ci_key"
    chmod 644 "secrets/match_ci_key.pub"
    print_success "Generated SSH key: secrets/match_ci_key"
fi

print_info "Public key (add this as deploy key to Match repository):"
echo
cat "secrets/match_ci_key.pub"
echo
print_warning "IMPORTANT: Add the above public key as a deploy key to your Match repository"
print_info "Steps:"
echo "  1. Go to your Match repository on GitHub/GitLab"
echo "  2. Go to Settings → Deploy Keys"
echo "  3. Click 'Add deploy key'"
echo "  4. Title: 'Fastlane Match CI'"
echo "  5. Paste the public key above"
echo "  6. Check 'Allow write access' (Match needs to push certificates)"
echo "  7. Click 'Add key'"
echo

read -p "Press Enter after adding the deploy key..."

# Test SSH connection
print_info "Testing SSH connection to Match repository..."
ssh -i secrets/match_ci_key -o StrictHostKeyChecking=no -T git@github.com 2>&1 | grep -q "successfully authenticated" && print_success "SSH connection successful" || print_warning "Could not verify SSH connection"
echo

# Match password
print_step "5.4" "Match Encryption Password"
print_info "Match encrypts your certificates with a password"
print_warning "IMPORTANT: Store this password securely! You'll need it on all machines and CI/CD"
echo

if [ -f "secrets/.match_password" ]; then
    print_warning "Match password file already exists"
    read -p "Do you want to generate a new password? [y/N]: " -n 1 -r
    echo
    GENERATE_NEW_PWD=$REPLY
else
    GENERATE_NEW_PWD="y"
fi

if [[ $GENERATE_NEW_PWD =~ ^[Yy]$ ]]; then
    # Generate secure random password
    MATCH_PASSWORD=$(openssl rand -base64 32)
    echo "$MATCH_PASSWORD" > "secrets/.match_password"
    chmod 600 "secrets/.match_password"
    print_success "Generated Match password (stored in secrets/.match_password)"
    echo
    print_warning "Match Password: $MATCH_PASSWORD"
    print_warning "Save this password in your password manager!"
else
    MATCH_PASSWORD=$(cat "secrets/.match_password")
    print_success "Using existing Match password"
fi
echo

# TestFlight Beta Review Contact Information
print_section "6️⃣  TestFlight Beta Review Contact Information"
print_info "This information is shown to Apple reviewers during beta review"
echo

read -p "TestFlight Contact Email: " TESTFLIGHT_CONTACT_EMAIL
TESTFLIGHT_CONTACT_EMAIL=${TESTFLIGHT_CONTACT_EMAIL:-team@example.com}

read -p "TestFlight Contact First Name: " TESTFLIGHT_FIRST_NAME
TESTFLIGHT_FIRST_NAME=${TESTFLIGHT_FIRST_NAME:-Team}

read -p "TestFlight Contact Last Name: " TESTFLIGHT_LAST_NAME
TESTFLIGHT_LAST_NAME=${TESTFLIGHT_LAST_NAME:-Name}

read -p "TestFlight Contact Phone (with country code): " TESTFLIGHT_PHONE
TESTFLIGHT_PHONE=${TESTFLIGHT_PHONE:-+1234567890}

read -p "Beta Feedback Email: " BETA_FEEDBACK_EMAIL
BETA_FEEDBACK_EMAIL=${BETA_FEEDBACK_EMAIL:-$TESTFLIGHT_CONTACT_EMAIL}

read -p "TestFlight Tester Groups (comma-separated): " TESTFLIGHT_GROUPS
TESTFLIGHT_GROUPS=${TESTFLIGHT_GROUPS:-internal-testers}

print_success "TestFlight configuration collected"
echo

# App Store Review Contact Information
print_section "7️⃣  App Store Review Contact Information"
print_info "This information is shown to Apple reviewers during App Store review"
echo

read -p "App Store Review First Name [$TESTFLIGHT_FIRST_NAME]: " APPSTORE_REVIEW_FIRST_NAME
APPSTORE_REVIEW_FIRST_NAME=${APPSTORE_REVIEW_FIRST_NAME:-$TESTFLIGHT_FIRST_NAME}

read -p "App Store Review Last Name [$TESTFLIGHT_LAST_NAME]: " APPSTORE_REVIEW_LAST_NAME
APPSTORE_REVIEW_LAST_NAME=${APPSTORE_REVIEW_LAST_NAME:-$TESTFLIGHT_LAST_NAME}

read -p "App Store Review Phone [$TESTFLIGHT_PHONE]: " APPSTORE_REVIEW_PHONE
APPSTORE_REVIEW_PHONE=${APPSTORE_REVIEW_PHONE:-$TESTFLIGHT_PHONE}

read -p "App Store Review Email [$TESTFLIGHT_CONTACT_EMAIL]: " APPSTORE_REVIEW_EMAIL
APPSTORE_REVIEW_EMAIL=${APPSTORE_REVIEW_EMAIL:-$TESTFLIGHT_CONTACT_EMAIL}

print_success "App Store review configuration collected"
echo

# Write shared_keys.env
print_section "8️⃣  Generating Configuration File"

cat > secrets/shared_keys.env << EOF
# ==============================================================================
# Shared iOS Keys - Generated by setup_ios_complete.sh
# ==============================================================================
# These keys are SHARED across all your iOS apps.
# Load this file before running deployment scripts: source secrets/shared_keys.env
# ==============================================================================

# Apple Developer Team ID
export TEAM_ID="$TEAM_ID"

# App Store Connect API (Shared across all apps)
export APPSTORE_KEY_ID="$APPSTORE_KEY_ID"
export APPSTORE_ISSUER_ID="$APPSTORE_ISSUER_ID"
export APPSTORE_KEY_PATH="./secrets/AuthKey.p8"

# Fastlane Match Configuration (Shared certificate repository)
export MATCH_GIT_URL="$MATCH_GIT_URL"
export MATCH_GIT_BRANCH="$MATCH_GIT_BRANCH"
export MATCH_SSH_KEY_PATH="./secrets/match_ci_key"

# Match password is stored in: secrets/.match_password
# Load it with: export MATCH_PASSWORD=\$(cat secrets/.match_password)

# TestFlight Beta Review Configuration
export TESTFLIGHT_CONTACT_EMAIL="$TESTFLIGHT_CONTACT_EMAIL"
export TESTFLIGHT_FIRST_NAME="$TESTFLIGHT_FIRST_NAME"
export TESTFLIGHT_LAST_NAME="$TESTFLIGHT_LAST_NAME"
export TESTFLIGHT_PHONE="$TESTFLIGHT_PHONE"
export TESTFLIGHT_DEMO_EMAIL=""
export TESTFLIGHT_DEMO_PASSWORD=""

# Beta Feedback Configuration
export BETA_FEEDBACK_EMAIL="$BETA_FEEDBACK_EMAIL"

# TestFlight Tester Groups (comma-separated)
export TESTFLIGHT_GROUPS="$TESTFLIGHT_GROUPS"

# App Store Review Configuration
export APPSTORE_REVIEW_FIRST_NAME="$APPSTORE_REVIEW_FIRST_NAME"
export APPSTORE_REVIEW_LAST_NAME="$APPSTORE_REVIEW_LAST_NAME"
export APPSTORE_REVIEW_PHONE="$APPSTORE_REVIEW_PHONE"
export APPSTORE_REVIEW_EMAIL="$APPSTORE_REVIEW_EMAIL"
export APPSTORE_DEMO_EMAIL=""
export APPSTORE_DEMO_PASSWORD=""

# App Marketing URLs (customize per app or use shared defaults)
export APP_MARKETING_URL="https://example.com"
export APP_PRIVACY_URL="https://example.com/privacy"
export APP_SUPPORT_URL="https://example.com/support"

# Firebase Configuration (same file for all apps)
export FIREBASE_SERVICE_CREDS="./secrets/firebaseAppDistributionServiceCredentialsFile.json"

# ==============================================================================
# Per-App Configuration (DON'T SET HERE - managed by fastlane-config)
# ==============================================================================
# These are set in fastlane-config/project_config.rb:
# - APP_IDENTIFIER (iOS Bundle ID)
# - FIREBASE_IOS_APP_ID
# The customizer.sh script updates these when creating a new project.
EOF

chmod 600 secrets/shared_keys.env
print_success "Created secrets/shared_keys.env"
echo

# Auto-sync iOS secrets to secrets.env for GitHub Actions
print_info "Synchronizing iOS secrets to secrets.env for GitHub Actions..."
if bash ./keystore-manager.sh sync; then
    print_success "iOS secrets synchronized to secrets.env"
else
    print_warning "Sync encountered issues - you may need to run manually:"
    echo "  ./keystore-manager.sh sync"
fi
echo

# Initialize Match
print_section "9️⃣  Initializing Fastlane Match"
print_info "This will sync your code signing certificates from the Match repository"
print_info "If this is the first time, Match will create new certificates"
echo

# Load configuration
source secrets/shared_keys.env
export MATCH_PASSWORD

# Install Fastlane
print_info "Installing Fastlane dependencies..."
bundle install

# Run Match for adhoc
print_info "Syncing adhoc certificates..."
bundle exec fastlane ios sync_certificates match_type:adhoc || print_warning "Match sync encountered issues (this is normal for first run)"

# Run Match for appstore
print_info "Syncing appstore certificates..."
bundle exec fastlane ios sync_certificates match_type:appstore || print_warning "Match sync encountered issues (this is normal for first run)"

echo

# Summary
print_section "✅ Setup Complete!"

print_success "iOS deployment infrastructure is configured!"
echo
print_info "Configuration Summary:"
echo "  ✓ Team ID: $TEAM_ID"
echo "  ✓ App Store Connect API configured"
echo "  ✓ Match repository: $MATCH_GIT_URL"
echo "  ✓ SSH key generated and configured"
echo "  ✓ Match password generated"
echo "  ✓ TestFlight & App Store review info configured"
echo

print_info "Configuration files created:"
echo "  ✓ secrets/shared_keys.env"
echo "  ✓ secrets/AuthKey.p8"
echo "  ✓ secrets/match_ci_key"
echo "  ✓ secrets/.match_password"
echo

print_warning "IMPORTANT: Keep these files secure and NEVER commit them to git!"
print_info "They are already in .gitignore"
echo

print_info "Next Steps:"
echo "  1. Test deployment to Firebase:"
echo "     bash scripts/deploy_firebase.sh"
echo
echo "  2. Test deployment to TestFlight:"
echo "     bash scripts/deploy_testflight.sh"
echo
echo "  3. Deploy to App Store (when ready):"
echo "     bash scripts/deploy_appstore.sh"
echo

print_info "Optional: Setup APN for push notifications"
echo "  bash scripts/setup_apn_key.sh"
echo

print_success "Happy deploying! 🚀"
