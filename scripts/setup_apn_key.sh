#!/bin/bash

# ==============================================================================
# APN (Apple Push Notification) Key Setup Script
# ==============================================================================
# This script helps configure APN keys for Firebase Cloud Messaging on iOS
# ==============================================================================

set -e  # Exit on any error

# Color codes
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
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
    echo -e "${BLUE}$1${NC}"
    echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
    echo
}

# Print banner
print_section "🔔 APN (Apple Push Notification) Key Setup"

# Check if shared_keys.env exists
if [ ! -f "secrets/shared_keys.env" ]; then
    print_error "secrets/shared_keys.env not found"
    print_info "Run the iOS setup wizard first: bash scripts/setup_ios_complete.sh"
    exit 1
fi

# Load existing configuration
source secrets/shared_keys.env

# Introduction
print_info "This script helps you configure Apple Push Notification (APN) keys"
print_info "APN keys are required if your app uses Firebase Cloud Messaging for push notifications"
echo

print_warning "If your app does NOT use push notifications, you can skip this setup"
read -p "Do you want to configure APN keys? [y/N]: " -n 1 -r
echo
if [[ ! $REPLY =~ ^[Yy]$ ]]; then
    print_info "Skipping APN setup"
    exit 0
fi

# Instructions for creating APN key
print_section "📝 Creating APN Key in Apple Developer Portal"

print_info "Follow these steps to create an APN key:"
echo "  1. Go to: https://developer.apple.com/account/resources/authkeys/list"
echo "  2. Click the '+' button"
echo "  3. Name it: 'APNs Auth Key' or similar"
echo "  4. Check the box for 'Apple Push Notifications service (APNs)'"
echo "  5. Click 'Continue'"
echo "  6. Click 'Register'"
echo "  7. Click 'Download' (you can only download once!)"
echo "  8. Note the Key ID (10 characters shown on the screen)"
echo

read -p "Have you created and downloaded an APN key? [y/N]: " -n 1 -r
echo
if [[ ! $REPLY =~ ^[Yy]$ ]]; then
    print_warning "Please create an APN key first, then run this script again"
    exit 0
fi

# Get APN Key ID
print_section "🔑 APN Key Configuration"

read -p "Enter your APN Key ID (10 characters): " APN_KEY_ID
while [[ ! $APN_KEY_ID =~ ^[A-Z0-9]{10}$ ]]; do
    print_error "Invalid Key ID format. Must be 10 characters (letters and numbers)"
    read -p "Enter APN Key ID: " APN_KEY_ID
done
print_success "APN Key ID: $APN_KEY_ID"
echo

# Locate APN key file
print_info "Looking for APN .p8 key file..."

# Find .p8 files
P8_FILES=($(find . -maxdepth 2 -name "AuthKey_*.p8" 2>/dev/null | grep -v "secrets/AuthKey.p8"))

if [ ${#P8_FILES[@]} -eq 0 ]; then
    print_warning "No APN .p8 key files found in current directory"
    read -p "Enter the full path to your APN .p8 key file: " APN_P8_PATH
else
    echo "Found .p8 files:"
    for i in "${!P8_FILES[@]}"; do
        echo "  $((i+1)). ${P8_FILES[$i]}"
    done
    read -p "Select file number (or enter path to another file): " SELECTION

    if [[ $SELECTION =~ ^[0-9]+$ ]] && [ $SELECTION -ge 1 ] && [ $SELECTION -le ${#P8_FILES[@]} ]; then
        APN_P8_PATH="${P8_FILES[$((SELECTION-1))]}"
    else
        APN_P8_PATH="$SELECTION"
    fi
fi

# Validate file exists
while [ ! -f "$APN_P8_PATH" ]; do
    print_error "File not found: $APN_P8_PATH"
    read -p "Enter the full path to your APN .p8 key file: " APN_P8_PATH
done

# Validate it's a valid P8 key
if ! grep -q "BEGIN PRIVATE KEY" "$APN_P8_PATH"; then
    print_error "This doesn't appear to be a valid .p8 key file"
    exit 1
fi

# Copy to secrets directory
cp "$APN_P8_PATH" "secrets/APNAuthKey.p8"
chmod 600 "secrets/APNAuthKey.p8"
print_success "Copied APN key to secrets/APNAuthKey.p8"
echo

# Update shared_keys.env
print_section "💾 Updating Configuration"

# Check if APN configuration already exists
if grep -q "^export APN_KEY_ID=" secrets/shared_keys.env; then
    print_info "Updating existing APN configuration..."
    # Update existing values
    sed -i.bak "s/^export APN_KEY_ID=.*/export APN_KEY_ID=\"$APN_KEY_ID\"/" secrets/shared_keys.env
    sed -i.bak "s|^export APN_KEY_PATH=.*|export APN_KEY_PATH=\"./secrets/APNAuthKey.p8\"|" secrets/shared_keys.env
    sed -i.bak "s/^export APN_TEAM_ID=.*/export APN_TEAM_ID=\"$TEAM_ID\"/" secrets/shared_keys.env
    rm -f secrets/shared_keys.env.bak
else
    print_info "Adding APN configuration..."
    # Add APN configuration block
    cat >> secrets/shared_keys.env << EOF

# ==============================================================================
# APN (Apple Push Notification) Configuration
# ==============================================================================
# For Firebase Cloud Messaging on iOS
export APN_KEY_ID="$APN_KEY_ID"
export APN_KEY_PATH="./secrets/APNAuthKey.p8"
export APN_TEAM_ID="$TEAM_ID"
EOF
fi

print_success "Updated secrets/shared_keys.env with APN configuration"
echo

# Firebase Console Instructions
print_section "🔥 Uploading APN Key to Firebase Console"

print_info "You need to upload the APN key to Firebase Console for each iOS app:"
echo
echo "Steps:"
echo "  1. Go to: https://console.firebase.google.com/"
echo "  2. Select your Firebase project"
echo "  3. Click the gear icon → Project settings"
echo "  4. Go to the 'Cloud Messaging' tab"
echo "  5. Scroll to 'Apple app configuration'"
echo "  6. Under 'APNs authentication key', click 'Upload'"
echo "  7. Upload: secrets/APNAuthKey.p8"
echo "  8. Enter Key ID: $APN_KEY_ID"
echo "  9. Enter Team ID: $TEAM_ID"
echo "  10. Click 'Upload'"
echo

print_warning "You need to do this for EACH iOS app in your Firebase project"
echo

read -p "Press Enter after uploading to Firebase Console..."

# Verification
print_section "✅ APN Setup Complete!"

print_success "APN key configured successfully!"
echo
print_info "Configuration Summary:"
echo "  ✓ APN Key ID: $APN_KEY_ID"
echo "  ✓ APN Team ID: $TEAM_ID"
echo "  ✓ APN Key File: secrets/APNAuthKey.p8"
echo

print_info "Files created/updated:"
echo "  ✓ secrets/APNAuthKey.p8"
echo "  ✓ secrets/shared_keys.env (updated with APN config)"
echo

print_warning "IMPORTANT: Keep secrets/APNAuthKey.p8 secure and NEVER commit to git!"
echo

print_info "Next Steps:"
echo "  1. Verify setup: bash scripts/verify_apn_setup.sh"
echo "  2. Test push notifications in your app"
echo "  3. Check Firebase Console for delivery reports"
echo

print_success "Happy pushing! 🔔"
