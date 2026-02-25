#!/bin/bash

# Android Keystore Generator and GitHub Secrets Management Script
# This script generates Android keystores and manages GitHub secrets

set -e  # Exit on any error

# Colors for better readability
GREEN='\033[0;32m'
BLUE='\033[0;34m'
RED='\033[0;31m'
YELLOW='\033[0;33m'
CYAN='\033[0;36m'
BOLD='\033[1m'
NC='\033[0m' # No Color

# Print helper functions
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

# Default environment file paths (secrets/ is preferred, root is legacy)
SECRETS_DIR_ENV_FILE="secrets/secrets.env"
ROOT_ENV_FILE="secrets.env"
ENV_FILE=""  # Will be resolved after argument parsing

# Default values
COMMAND="generate"
REPO=""
ENV=""
SECRET_NAME=""
ENV_FILE_OVERRIDE=""  # User-specified --env-file path

# Keys that should not be sent to GitHub
EXCLUDED_GITHUB_KEYS=(
    "COMPANY_NAME"
    "DEPARTMENT"
    "ORGANIZATION"
    "CITY"
    "STATE"
    "COUNTRY"
    "VALIDITY"
    "KEYALG"
    "KEYSIZE"
    "OVERWRITE"
    "ORIGINAL_KEYSTORE_NAME"
    "UPLOAD_KEYSTORE_NAME"
    "CN"
    "OU"
    "O"
    "L"
    "ST"
    "C"
)

  # Global associative array for iOS string secrets
  declare -g -A IOS_STRING_SECRETS

# Function to strip quotes from values
strip_quotes() {
    local value="$1"
    # Remove surrounding double quotes if present
    value="${value#\"}"
    value="${value%\"}"
    # Remove surrounding single quotes if present
    value="${value#\'}"
    value="${value%\'}"
    echo "$value"
}

# Resolve which secrets.env file to use
# Priority: --env-file override > secrets/secrets.env > root secrets.env
resolve_env_file() {
    # If user specified --env-file, use that
    if [[ -n "$ENV_FILE_OVERRIDE" ]]; then
        ENV_FILE="$ENV_FILE_OVERRIDE"
        return 0
    fi

    local secrets_dir_exists=false
    local root_exists=false

    [[ -f "$SECRETS_DIR_ENV_FILE" ]] && secrets_dir_exists=true
    [[ -f "$ROOT_ENV_FILE" ]] && root_exists=true

    # Both exist - ask user to choose
    if [[ "$secrets_dir_exists" = true ]] && [[ "$root_exists" = true ]]; then
        echo -e "${YELLOW}Found secrets.env in two locations:${NC}"
        echo -e "  ${CYAN}[1]${NC} secrets/secrets.env  (recommended)"
        echo -e "  ${CYAN}[2]${NC} secrets.env          (legacy/root)"
        echo ""
        read -r -p "Which file should be used? [1/2] (default: 1): " choice
        case "$choice" in
            2)
                ENV_FILE="$ROOT_ENV_FILE"
                print_info "Using root: $ROOT_ENV_FILE"
                ;;
            *)
                ENV_FILE="$SECRETS_DIR_ENV_FILE"
                print_info "Using secrets dir: $SECRETS_DIR_ENV_FILE"
                ;;
        esac
    elif [[ "$secrets_dir_exists" = true ]]; then
        ENV_FILE="$SECRETS_DIR_ENV_FILE"
    elif [[ "$root_exists" = true ]]; then
        ENV_FILE="$ROOT_ENV_FILE"
    else
        # Neither exists - default to secrets/ (will be created by generate/sync)
        ENV_FILE="$SECRETS_DIR_ENV_FILE"
    fi
}

# Load variables from secrets.env if it exists (simple variables only)
load_env_vars() {
    local env_file="$1"
    local show_message="$2"

    if [ -f "$env_file" ]; then
        if [ "$show_message" = "true" ]; then
            echo -e "${BLUE}Loading configuration from $env_file${NC}"
        fi

        # Only load simple variables (KEY=VALUE format), ignore multiline blocks
        local in_multiline=false
        local multiline_end=""

        while IFS= read -r line; do
            # Skip comments and blank lines
            if [ "$in_multiline" = false ] && [[ -z "$line" || "$line" == \#* ]]; then
                continue
            fi

            # Check if we're entering a multiline block
            if [ "$in_multiline" = false ] && [[ "$line" == *"<<"* ]]; then
                multiline_end=$(echo "$line" | sed 's/.*<<\(.*\)/\1/')
                in_multiline=true
                continue
            fi

            # Check if we're exiting a multiline block
            if [ "$in_multiline" = true ] && [[ "$line" == "$multiline_end" ]]; then
                in_multiline=false
                continue
            fi

            # Skip lines inside multiline blocks
            if [ "$in_multiline" = true ]; then
                continue
            fi

            # Process regular KEY=VALUE pairs
            if [[ "$line" == *"="* ]]; then
                # Extract the variable name
                local key=$(echo "$line" | cut -d '=' -f1 | xargs)
                # Extract the value (anything after the first =)
                local value=$(echo "$line" | cut -d '=' -f2-)
                # Export the variable
                export "$key"="$value"
            fi
        done < "$env_file"
    fi
}

# Function to display help
show_help() {
    echo -e "${BLUE}Android Keystore Generator and GitHub Secrets Management Script${NC}"
    echo ""
    echo "Usage:"
    echo "  ./keystore-manager.sh [COMMAND] [OPTIONS]"
    echo ""
    echo "Commands:"
    echo "  generate - Generate Android keystores and update secrets.env (default)"
    echo "  encode-secrets - Encode files from secrets/ directory and update secrets.env"
    echo "  sync     - Validate secrets.env format and completeness"
    echo "  view     - View all secrets in the secrets.env file as a formatted table"
    echo "  add      - Add secrets to a GitHub repository from secrets.env"
    echo "  list     - List all secrets in a GitHub repository"
    echo "  delete   - Delete a secret from a GitHub repository"
    echo "  delete-all - Delete all secrets from a GitHub repository that are in secrets.env"
    echo "               Use --include-excluded flag to also delete excluded secrets"
    echo "  help     - Show this help message"
    echo ""
    echo "Options:"
    echo "  --repo=username/repo      - GitHub repository name"
    echo "  --env=environment         - GitHub environment name"
    echo "  --name=SECRET_NAME        - Secret name (for delete command)"
    echo "  --env-file=path           - Override secrets.env file path"
    echo "                              (default: secrets/secrets.env, fallback: secrets.env)"
    echo ""
    echo "Examples:"
    echo "  ./keystore-manager.sh generate"
    echo "  ./keystore-manager.sh encode-secrets"
    echo "  ./keystore-manager.sh sync"
    echo "  ./keystore-manager.sh view"
    echo "  ./keystore-manager.sh add --repo=username/repo"
    echo "  ./keystore-manager.sh list --repo=username/repo"
    echo "  ./keystore-manager.sh delete --repo=username/repo --name=SECRET_NAME"
    echo " ./keystore-manager.sh delete-all --repo=username/repo [--env=environment]"
    echo " ./keystore-manager.sh delete-all --repo=username/repo [--env=environment] --include-excluded"

}

# Function to view secrets from secrets.env in a table
view_secrets() {
    if [ ! -f "$ENV_FILE" ]; then
        echo -e "${RED}Error: $ENV_FILE file not found.${NC}"
        exit 1
    fi

    echo -e "${BLUE}Loading configuration from $ENV_FILE${NC}"
    echo -e "${BLUE}Viewing secrets from $ENV_FILE${NC}"
    echo ""

    # Calculate column widths
    KEY_WIDTH=30
    VALUE_WIDTH=50
    TOTAL_WIDTH=$((KEY_WIDTH + VALUE_WIDTH + 5))  # 5 for borders and spacing

    # Function to print horizontal border
    print_border() {
        local char=$1
        local width=$2
        printf "${CYAN}%*s${NC}\n" "$width" | tr " " "$char"
    }

    # Print table header
    print_border "═" $TOTAL_WIDTH
    printf "${CYAN}║${BOLD} %-${KEY_WIDTH}s ${CYAN}║${BOLD} %-${VALUE_WIDTH}s ${CYAN}║${NC}\n" "SECRET KEY" "VALUE"
    print_border "═" $TOTAL_WIDTH

    # Process the file line by line with support for multiline values
    local multiline_mode=false
    local multiline_end=""

    while IFS= read -r line || [ -n "$line" ]; do
        # Skip empty lines and comments when not in multiline mode
        if [ "$multiline_mode" = false ] && [[ -z "$line" || "$line" == \#* ]]; then
            continue
        fi

        # Check if we're exiting a multiline block
        if [ "$multiline_mode" = true ] && [[ "$line" == "$multiline_end" ]]; then
            multiline_mode=false
            continue
        fi

        # Skip content lines inside multiline blocks
        if [ "$multiline_mode" = true ]; then
            continue
        fi

        # Check if this is the start of a multiline value
        if [[ "$line" == *"<<"* ]]; then
            # Extract the key (part before <<)
            local key=$(echo "$line" | cut -d '<' -f1 | xargs)
            # Extract the delimiter (part after <<)
            multiline_end=$(echo "$line" | sed 's/.*<<\(.*\)/\1/')
            multiline_mode=true

            # Print the multiline value immediately
            printf "${CYAN}║${NC} ${YELLOW}%-${KEY_WIDTH}s${NC} ${CYAN}║${NC} ${GREEN}%-${VALUE_WIDTH}s${NC} ${CYAN}║${NC}\n" "$key" "[MULTILINE VALUE]"
        elif [[ "$line" == *"="* ]]; then
            # This is a regular key=value line
            local key=$(echo "$line" | cut -d '=' -f1 | xargs)
            local value=$(echo "$line" | cut -d '=' -f2-)

            # Strip quotes for display
            value=$(strip_quotes "$value")

            # Truncate value if too long
            local display_value=""
            if [ ${#value} -gt $VALUE_WIDTH ]; then
                display_value="${value:0:$((VALUE_WIDTH-5))}..."
            else
                display_value="$value"
            fi

            # Print the regular key-value pair
            printf "${CYAN}║${NC} ${YELLOW}%-${KEY_WIDTH}s${NC} ${CYAN}║${NC} ${GREEN}%-${VALUE_WIDTH}s${NC} ${CYAN}║${NC}\n" "$key" "$display_value"
        fi
    done < "$ENV_FILE"

    # Print table footer
    print_border "═" $TOTAL_WIDTH

    # Help message for multiline values
    echo -e "${BLUE}Note: For multiline values, the content is displayed as [MULTILINE VALUE]${NC}"
}

# Function to check if keytool is available
check_keytool() {
    if ! command -v keytool &> /dev/null; then
        echo -e "${RED}Error: keytool command not found.${NC}"
        echo -e "Please ensure you have Java Development Kit (JDK) installed and that keytool is in your PATH."
        exit 1
    fi
}

# Function to check if gh CLI is available
check_gh_cli() {
    if ! command -v gh &> /dev/null; then
        echo -e "${RED}GitHub CLI (gh) is not installed. Please install it first:${NC}"
        echo -e "https://cli.github.com/manual/installation"
        exit 1
    fi

    # Check if user is authenticated
    if ! gh auth status &> /dev/null; then
        echo -e "${RED}You are not logged in to GitHub CLI. Please run:${NC}"
        echo -e "${BLUE}gh auth login${NC}"
        exit 1
    fi
}

# Function to create keystores directory
create_keystores_dir() {
    if [ ! -d "keystores" ]; then
        echo -e "${BLUE}Creating 'keystores' directory...${NC}"
        mkdir -p keystores
        if [ $? -ne 0 ]; then
            echo -e "${RED}Error: Failed to create 'keystores' directory.${NC}"
            exit 1
        fi
    fi
}

# Function to encode file to base64
encode_base64() {
    local file_path=$1
    if [ -f "$file_path" ]; then
        if [[ "$OSTYPE" == "darwin"* ]]; then
            # macOS requires -i flag for input file
            # Linux accepts positional argument and -w 0 for no wrapping
            base64 -i "$file_path"
        else
            # Linux
            base64 -w 0 "$file_path"
        fi
    else
        echo -e "${RED}Error: File not found: $file_path${NC}"
        return 1
    fi
}

# Function to create secrets directory if it doesn't exist
create_secrets_dir() {
    if [ ! -d "secrets" ]; then
        echo -e "${BLUE}Creating 'secrets' directory...${NC}"
        mkdir -p secrets
        if [ $? -ne 0 ]; then
            echo -e "${RED}Error: Failed to create 'secrets' directory.${NC}"
            exit 1
        fi
    fi
}

# Parse iOS string secrets from shared_keys.env
parse_shared_keys_env() {
    local SHARED_KEYS_FILE="secrets/shared_keys.env"

    # Skip if file doesn't exist (Android-only setup)
    if [[ ! -f "$SHARED_KEYS_FILE" ]]; then
        print_info "shared_keys.env not found - skipping iOS secrets (Android-only project)"
        return 0
    fi

    print_info "Parsing iOS secrets from shared_keys.env..."

    # Read MATCH_PASSWORD from .match_password file if it exists
    local MATCH_PWD=""
    if [[ -f "secrets/.match_password" ]]; then
        MATCH_PWD=$(head -n1 secrets/.match_password 2>/dev/null | tr -d '\n\r')
        print_success "Loaded MATCH_PASSWORD from .match_password file"
    else
        print_warning "secrets/.match_password not found - MATCH_PASSWORD will be empty"
    fi

    # Extract values from shared_keys.env
    # Format: export KEY="value"
    local APPSTORE_KEY_ID=$(grep '^export APPSTORE_KEY_ID=' "$SHARED_KEYS_FILE" 2>/dev/null | cut -d'"' -f2)
    local APPSTORE_ISSUER_ID=$(grep '^export APPSTORE_ISSUER_ID=' "$SHARED_KEYS_FILE" 2>/dev/null | cut -d'"' -f2)
    local NOTARIZATION_TEAM_ID=$(grep '^export TEAM_ID=' "$SHARED_KEYS_FILE" 2>/dev/null | cut -d'"' -f2)
    local NOTARIZATION_APPLE_ID=$(grep '^export NOTARIZATION_APPLE_ID=' "$SHARED_KEYS_FILE" 2>/dev/null | cut -d'"' -f2)
    local NOTARIZATION_PASSWORD=$(grep '^export NOTARIZATION_PASSWORD=' "$SHARED_KEYS_FILE" 2>/dev/null | cut -d'"' -f2)

    # Validate critical values
    if [[ -z "$APPSTORE_KEY_ID" ]]; then
        print_warning "APPSTORE_KEY_ID is empty - App Store Connect API key may not be configured"
    fi
    if [[ -z "$APPSTORE_ISSUER_ID" ]]; then
        print_warning "APPSTORE_ISSUER_ID is empty - App Store Connect API issuer may not be configured"
    fi

    # Populate global associative array (declared at top of script)
    IOS_STRING_SECRETS["APPSTORE_KEY_ID"]="$APPSTORE_KEY_ID"
    IOS_STRING_SECRETS["APPSTORE_ISSUER_ID"]="$APPSTORE_ISSUER_ID"
    IOS_STRING_SECRETS["MATCH_PASSWORD"]="$MATCH_PWD"
    IOS_STRING_SECRETS["NOTARIZATION_APPLE_ID"]="$NOTARIZATION_APPLE_ID"
    IOS_STRING_SECRETS["NOTARIZATION_PASSWORD"]="$NOTARIZATION_PASSWORD"
    IOS_STRING_SECRETS["NOTARIZATION_TEAM_ID"]="$NOTARIZATION_TEAM_ID"

    # Print summary
    local count=0
    for key in "${!IOS_STRING_SECRETS[@]}"; do
        if [[ -n "${IOS_STRING_SECRETS[$key]}" ]]; then
            count=$((count + 1))
        fi
    done

    print_success "Found $count of 6 iOS string secrets"
}

# Global associative array for macOS password secrets
declare -g -A MACOS_PASSWORD_SECRETS

# Parse macOS password secrets from dotfiles in secrets/
parse_macos_password_files() {
    print_info "Parsing macOS password files from secrets/..."

    local count=0

    # Read KEYCHAIN_PASSWORD from .keychain_password file
    if [[ -f "secrets/.keychain_password" ]]; then
        local val
        val=$(head -n1 secrets/.keychain_password 2>/dev/null | tr -d '\n\r')
        if [[ -n "$val" ]]; then
            MACOS_PASSWORD_SECRETS["KEYCHAIN_PASSWORD"]="$val"
            count=$((count + 1))
            print_success "Loaded KEYCHAIN_PASSWORD from .keychain_password file"
        else
            print_warning ".keychain_password file is empty"
        fi
    else
        print_info "secrets/.keychain_password not found - KEYCHAIN_PASSWORD will remain as-is"
    fi

    # Read CERTIFICATES_PASSWORD from .certificates_password file
    if [[ -f "secrets/.certificates_password" ]]; then
        local val
        val=$(head -n1 secrets/.certificates_password 2>/dev/null | tr -d '\n\r')
        if [[ -n "$val" ]]; then
            MACOS_PASSWORD_SECRETS["CERTIFICATES_PASSWORD"]="$val"
            count=$((count + 1))
            print_success "Loaded CERTIFICATES_PASSWORD from .certificates_password file"
        else
            print_warning ".certificates_password file is empty"
        fi
    else
        print_info "secrets/.certificates_password not found - CERTIFICATES_PASSWORD will remain as-is"
    fi

    print_success "Found $count of 2 macOS password secrets"
}

# Update macOS password secrets in secrets.env
# Always ensures KEYCHAIN_PASSWORD and CERTIFICATES_PASSWORD exist in the file.
# Populates from password files if available, otherwise adds empty placeholders.
update_macos_password_secrets() {
    local SECRETS_FILE="$ENV_FILE"

    if [[ ! -f "$SECRETS_FILE" ]]; then
        print_info "No secrets file to update macOS passwords in"
        return 0
    fi

    print_info "Updating macOS password secrets in $SECRETS_FILE..."

    # Keys we must ensure exist
    local required_keys=("KEYCHAIN_PASSWORD" "CERTIFICATES_PASSWORD")

    for key in "${required_keys[@]}"; do
        # Get value from parsed password files (may be empty)
        local value="${MACOS_PASSWORD_SECRETS[$key]:-}"

        if grep -q "^${key}=" "$SECRETS_FILE" 2>/dev/null; then
            # Key exists - update only if we have a non-empty value
            if [[ -n "$value" ]]; then
                local escaped_value
                escaped_value=$(printf '%s\n' "$value" | sed 's/[&/\]/\\&/g')
                sed -i.bak "s|^${key}=.*|${key}=\"${escaped_value}\"|" "$SECRETS_FILE"
                print_success "Updated $key"
            else
                print_info "Preserving existing $key (no password file found)"
            fi
        else
            # Key doesn't exist - add after macOS App Store section header (or end of file)
            local escaped_value=""
            [[ -n "$value" ]] && escaped_value=$(printf '%s\n' "$value" | sed 's/[&/\]/\\&/g')

            local section_line
            section_line=$(grep -n "^# macOS App Store" "$SECRETS_FILE" 2>/dev/null | head -1 | cut -d: -f1)
            if [[ -n "$section_line" ]]; then
                # Find end of comments block after section header
                local insert_line=$((section_line + 1))
                local total_lines
                total_lines=$(wc -l < "$SECRETS_FILE")
                # Skip past comment lines to find insertion point
                while [[ $insert_line -le $total_lines ]]; do
                    local line_content
                    line_content=$(sed -n "${insert_line}p" "$SECRETS_FILE")
                    if [[ "$line_content" != \#* ]] && [[ -n "$line_content" ]]; then
                        break
                    fi
                    insert_line=$((insert_line + 1))
                done
                {
                    head -n $((insert_line - 1)) "$SECRETS_FILE"
                    echo "${key}=\"${escaped_value}\""
                    tail -n +${insert_line} "$SECRETS_FILE"
                } > "${SECRETS_FILE}.tmp" && mv "${SECRETS_FILE}.tmp" "$SECRETS_FILE"
                print_success "Added $key to macOS App Store section"
            else
                echo "${key}=\"${escaped_value}\"" >> "$SECRETS_FILE"
                print_success "Appended $key to end of file"
            fi
        fi
    done

    rm -f "${SECRETS_FILE}.bak"
}

# Function to encode secrets directory files and update secrets.env
encode_secrets_directory_files() {
    echo -e "${BLUE}==================================================================${NC}"
    echo -e "${BLUE}Encoding files from secrets/ directory${NC}"
    echo -e "${BLUE}==================================================================${NC}"

    # Define mapping of file names to secret names
    declare -A FILE_TO_SECRET_MAP
    FILE_TO_SECRET_MAP["firebaseAppDistributionServiceCredentialsFile.json"]="FIREBASECREDS"
    FILE_TO_SECRET_MAP["google-services.json"]="GOOGLESERVICES"
    FILE_TO_SECRET_MAP["playStorePublishServiceCredentialsFile.json"]="PLAYSTORECREDS"
    FILE_TO_SECRET_MAP["AuthKey.p8"]="APPSTORE_AUTH_KEY"
    FILE_TO_SECRET_MAP["match_ci_key"]="MATCH_SSH_PRIVATE_KEY"
    # macOS App Store certificates and provisioning profiles
    FILE_TO_SECRET_MAP["mac_app_distribution.p12"]="MAC_APP_DISTRIBUTION_CERTIFICATE_B64"
    FILE_TO_SECRET_MAP["mac_installer_distribution.p12"]="MAC_INSTALLER_DISTRIBUTION_CERTIFICATE_B64"
    FILE_TO_SECRET_MAP["mac_embedded.provisionprofile"]="MAC_EMBEDDED_PROVISION_B64"
    FILE_TO_SECRET_MAP["mac_runtime.provisionprofile"]="MAC_RUNTIME_PROVISION_B64"

    local secrets_found=0
    local secrets_encoded=0
    declare -A ENCODED_SECRETS

    # Check if secrets directory exists
    if [ ! -d "secrets" ]; then
        echo -e "${YELLOW}No 'secrets' directory found. Skipping secrets encoding.${NC}"
        return 0
    fi

    # Scan secrets directory for known files
    for file_name in "${!FILE_TO_SECRET_MAP[@]}"; do
        local file_path="secrets/$file_name"
        local secret_name="${FILE_TO_SECRET_MAP[$file_name]}"

        if [ -f "$file_path" ]; then
            secrets_found=$((secrets_found + 1))
            echo -e "${BLUE}Found: $file_name${NC}"
            echo -e "${BLUE}Encoding as: $secret_name${NC}"

            local encoded=$(encode_base64 "$file_path")
            if [ $? -eq 0 ]; then
                ENCODED_SECRETS["$secret_name"]="$encoded"
                secrets_encoded=$((secrets_encoded + 1))
                echo -e "${GREEN}✓ Successfully encoded $file_name${NC}"
            else
                echo -e "${RED}✗ Failed to encode $file_name${NC}"
            fi
        fi
    done

    if [ $secrets_found -eq 0 ]; then
        echo -e "${YELLOW}No known secret files found in secrets/ directory${NC}"
        echo -e "${YELLOW}Looking for: firebaseAppDistributionServiceCredentialsFile.json, google-services.json, playStorePublishServiceCredentialsFile.json, AuthKey.p8, match_ci_key${NC}"
        return 0
    fi

    if [ $secrets_encoded -eq 0 ]; then
        echo -e "${RED}Failed to encode any secret files${NC}"
        return 1
    fi

    # Update secrets.env file
    echo -e "${BLUE}Updating $ENV_FILE with encoded files...${NC}"
    update_secrets_env_with_files

    echo -e "${GREEN}Encoded $secrets_encoded out of $secrets_found secret files${NC}"
    return 0
}

# Function to update secrets.env with encoded secret files
update_secrets_env_with_files() {
    if [ ! -f "$ENV_FILE" ]; then
        echo -e "${YELLOW}$ENV_FILE not found. Secret files will not be added.${NC}"
        return 0
    fi

    # Access the ENCODED_SECRETS array from parent scope

    local temp_file="${ENV_FILE}.tmp"
    local in_multiline=false
    local multiline_end=""
    local current_key=""

    # Read existing secrets.env and track which sections exist
    declare -A existing_sections

    while IFS= read -r line || [ -n "$line" ]; do
        if [ "$in_multiline" = false ] && [[ "$line" == *"<<EOF" ]]; then
            current_key=$(echo "$line" | cut -d '<' -f1 | xargs)
            existing_sections["$current_key"]=1
            multiline_end="EOF"
            in_multiline=true
        elif [ "$in_multiline" = true ] && [[ "$line" == "$multiline_end" ]]; then
            in_multiline=false
        fi
    done < "$ENV_FILE"

    # Copy existing file and update/append sections
    cp "$ENV_FILE" "$temp_file"
    in_multiline=false

    # For each encoded secret, update or append
    for secret_name in "${!ENCODED_SECRETS[@]}"; do
        local encoded_value="${ENCODED_SECRETS[$secret_name]}"

        if [ -n "${existing_sections[$secret_name]}" ]; then
            # Update existing section
            echo -e "${BLUE}Updating existing section: $secret_name${NC}"
            local temp_file2="${temp_file}.2"
            local in_target_section=false

            while IFS= read -r line || [ -n "$line" ]; do
                if [[ "$line" == "${secret_name}<<EOF" ]]; then
                    in_target_section=true
                    echo "$line" >> "$temp_file2"
                    echo "$encoded_value" >> "$temp_file2"
                    continue
                fi

                if [ "$in_target_section" = true ] && [[ "$line" == "EOF" ]]; then
                    in_target_section=false
                    echo "$line" >> "$temp_file2"
                    continue
                fi

                if [ "$in_target_section" = false ]; then
                    echo "$line" >> "$temp_file2"
                fi
            done < "$temp_file"

            mv "$temp_file2" "$temp_file"
        else
            # Append new section
            echo -e "${BLUE}Adding new section: $secret_name${NC}"
            echo "" >> "$temp_file"
            echo "${secret_name}<<EOF" >> "$temp_file"
            echo "$encoded_value" >> "$temp_file"
            echo "EOF" >> "$temp_file"
        fi
    done

    # Replace original file
    mv "$temp_file" "$ENV_FILE"
    echo -e "${GREEN}$ENV_FILE updated successfully${NC}"
}

# Update iOS string secrets in secrets.env
update_ios_string_secrets() {
    local SECRETS_FILE="$ENV_FILE"

    # Check if secrets.env exists
    if [[ ! -f "$SECRETS_FILE" ]]; then
        print_warning "$SECRETS_FILE not found. Creating new file..."
        mkdir -p "$(dirname "$SECRETS_FILE")"
        touch "$SECRETS_FILE"
    fi

    # Skip if no iOS secrets extracted
    if [[ ${#IOS_STRING_SECRETS[@]} -eq 0 ]]; then
        print_info "No iOS secrets to update"
        return 0
    fi

    print_info "Updating iOS string secrets in $SECRETS_FILE..."

    # Check if iOS Configuration section exists
    if grep -q "^# iOS Configuration" "$SECRETS_FILE" 2>/dev/null; then
        print_info "iOS section exists - updating individual keys..."
        update_ios_section
    else
        print_info "iOS section doesn't exist - appending new section..."
        append_ios_section
    fi
}

# Helper function to update existing iOS section
update_ios_section() {
    local SECRETS_FILE="$ENV_FILE"

    for key in "${!IOS_STRING_SECRETS[@]}"; do
        local value="${IOS_STRING_SECRETS[$key]}"

        # Check if key exists in file
        if grep -q "^${key}=" "$SECRETS_FILE"; then
            # Update existing key
            if [[ -n "$value" ]]; then
                # Replace with new value (escape special characters)
                local escaped_value=$(printf '%s\n' "$value" | sed 's/[&/\]/\\&/g')
                sed -i.bak "s|^${key}=.*|${key}=\"${escaped_value}\"|" "$SECRETS_FILE"
                print_success "Updated $key"
            else
                # Keep existing value if new value is empty
                print_info "Preserving existing $key (new value empty)"
            fi
        else
            # Key doesn't exist - add it after iOS section header
            local section_line=$(grep -n "^# iOS Configuration" "$SECRETS_FILE" | cut -d: -f1)
            if [[ -n "$section_line" ]]; then
                # Insert after the separator line following the header (portable approach)
                local insert_line=$((section_line + 2))
                local escaped_value=$(printf '%s\n' "$value" | sed 's/[&/\]/\\&/g')
                {
                    head -n $((insert_line - 1)) "$SECRETS_FILE"
                    echo "${key}=\"${escaped_value}\""
                    tail -n +${insert_line} "$SECRETS_FILE"
                } > "${SECRETS_FILE}.tmp" && mv "${SECRETS_FILE}.tmp" "$SECRETS_FILE"
                print_success "Added $key to iOS section"
            fi
        fi
    done

    # Remove backup file
    rm -f "${SECRETS_FILE}.bak"
}

# Helper function to append new iOS section
append_ios_section() {
    local SECRETS_FILE="$ENV_FILE"

    # Append new iOS section
    cat >> "$SECRETS_FILE" << EOF

# ==============================================================================
# iOS Configuration
# ==============================================================================

# App Store Connect API Keys
APPSTORE_KEY_ID="${IOS_STRING_SECRETS[APPSTORE_KEY_ID]}"
APPSTORE_ISSUER_ID="${IOS_STRING_SECRETS[APPSTORE_ISSUER_ID]}"

# Fastlane Match
MATCH_PASSWORD="${IOS_STRING_SECRETS[MATCH_PASSWORD]}"

# macOS Notarization (for Desktop app distribution)
NOTARIZATION_APPLE_ID="${IOS_STRING_SECRETS[NOTARIZATION_APPLE_ID]}"
NOTARIZATION_PASSWORD="${IOS_STRING_SECRETS[NOTARIZATION_PASSWORD]}"
NOTARIZATION_TEAM_ID="${IOS_STRING_SECRETS[NOTARIZATION_TEAM_ID]}"
EOF

    print_success "Appended iOS Configuration section"
}

# Add Desktop signing placeholders to secrets.env
add_desktop_placeholders() {
    local SECRETS_FILE="$ENV_FILE"

    # Check if file exists
    if [[ ! -f "$SECRETS_FILE" ]]; then
        print_error "File $SECRETS_FILE does not exist"
        return 1
    fi

    # Check if Desktop Signing section exists
    if grep -q "^# Desktop Signing" "$SECRETS_FILE" 2>/dev/null; then
        print_info "Desktop Signing section already exists - skipping"
    else
        print_info "Adding Desktop Signing placeholder section..."

        # Append Desktop section
        if ! cat >> "$SECRETS_FILE" << 'EOF'

# ==============================================================================
# Desktop Signing (Optional)
# ==============================================================================
# These are optional for Desktop app distribution outside app stores.
# Populate when setting up code signing for Windows/macOS/Linux desktop apps.

# Windows Signing
WINDOWS_SIGNING_KEY=""
WINDOWS_SIGNING_PASSWORD=""
WINDOWS_SIGNING_CERTIFICATE=""

# macOS Signing (Desktop app, not iOS)
MACOS_SIGNING_KEY=""
MACOS_SIGNING_PASSWORD=""
MACOS_SIGNING_CERTIFICATE=""

# Linux Signing
LINUX_SIGNING_KEY=""
LINUX_SIGNING_PASSWORD=""
LINUX_SIGNING_CERTIFICATE=""
EOF
        then
            print_error "Failed to append Desktop Signing section"
            return 1
        fi

        print_success "Added Desktop Signing placeholder section"
    fi

    # Add macOS App Store section if not present
    if grep -q "^# macOS App Store" "$SECRETS_FILE" 2>/dev/null; then
        print_info "macOS App Store section already exists - skipping"
    else
        print_info "Adding macOS App Store placeholder section..."

        if ! cat >> "$SECRETS_FILE" << 'EOF'

# ==============================================================================
# macOS App Store (Required for macOS TestFlight & App Store deployment)
# ==============================================================================
# Keychain and certificate passwords for CI code signing.
# Place .p12 and .provisionprofile files in secrets/ directory, then run sync.
#
# Password files (read automatically by sync):
#   secrets/.keychain_password              → KEYCHAIN_PASSWORD
#   secrets/.certificates_password          → CERTIFICATES_PASSWORD
#
# Certificate/profile files (base64 encoded by sync):
#   secrets/mac_app_distribution.p12        → MAC_APP_DISTRIBUTION_CERTIFICATE_B64
#   secrets/mac_installer_distribution.p12  → MAC_INSTALLER_DISTRIBUTION_CERTIFICATE_B64
#   secrets/mac_embedded.provisionprofile   → MAC_EMBEDDED_PROVISION_B64
#   secrets/mac_runtime.provisionprofile    → MAC_RUNTIME_PROVISION_B64
EOF
        then
            print_error "Failed to append macOS App Store section"
            return 1
        fi

        print_success "Added macOS App Store placeholder section"
    fi
}

# Validate secrets.env format and completeness
validate_sync_result() {
    local SECRETS_FILE="$ENV_FILE"
    local exit_code=0

    print_info "Validating $SECRETS_FILE..."

    # Check if file exists
    if [[ ! -f "$SECRETS_FILE" ]]; then
        print_error "File $SECRETS_FILE does not exist"
        return 1
    fi

    # Track validation issues
    local format_errors=()
    local missing_secrets=()
    local invalid_base64=()

    # ============================================================================
    # 1. Check file format
    # ============================================================================

    print_info "Checking file format..."

    # Check for GitHub Secrets Environment File header (generated by update_secrets_env)
    if ! grep -q "^# GitHub Secrets Environment File" "$SECRETS_FILE"; then
        format_errors+=("Missing GitHub Secrets Environment File header")
    fi

    # Check for iOS Configuration section header (if iOS project)
    if [[ -f "secrets/shared_keys.env" ]]; then
        if ! grep -q "^# iOS Configuration" "$SECRETS_FILE"; then
            format_errors+=("Missing iOS configuration section header (iOS project detected)")
        fi
    fi

    # Validate heredoc blocks are properly formatted
    local in_heredoc=false
    local heredoc_key=""
    local heredoc_delimiter=""
    local line_num=0

    while IFS= read -r line; do
        line_num=$((line_num + 1))

        # Check for heredoc start
        if [[ "$line" =~ ^([A-Z_]+)\<\<([A-Z]+)$ ]]; then
            if [[ "$in_heredoc" = true ]]; then
                format_errors+=("Line $line_num: Nested heredoc detected (unclosed $heredoc_key)")
            fi
            heredoc_key="${BASH_REMATCH[1]}"
            heredoc_delimiter="${BASH_REMATCH[2]}"
            in_heredoc=true
        # Check for heredoc end
        elif [[ "$in_heredoc" = true ]] && [[ "$line" == "$heredoc_delimiter" ]]; then
            in_heredoc=false
            heredoc_key=""
            heredoc_delimiter=""
        fi
    done < "$SECRETS_FILE"

    # Check if any heredoc was left unclosed
    if [[ "$in_heredoc" = true ]]; then
        format_errors+=("Unclosed heredoc block: $heredoc_key (missing $heredoc_delimiter)")
    fi

    # Check for duplicate keys using process substitution
    local duplicates
    duplicates=$(while IFS= read -r line; do
        # Extract keys from both regular and heredoc formats
        if [[ "$line" =~ ^([A-Z_]+)= ]] || [[ "$line" =~ ^([A-Z_]+)\<\< ]]; then
            echo "${BASH_REMATCH[1]}"
        fi
    done < "$SECRETS_FILE" | sort | uniq -d)

    if [[ -n "$duplicates" ]]; then
        while IFS= read -r dup_key; do
            if [[ -n "$dup_key" ]]; then
                format_errors+=("Duplicate key found: $dup_key")
            fi
        done <<< "$duplicates"
    fi

    # Report format errors
    if [[ ${#format_errors[@]} -gt 0 ]]; then
        print_error "Format validation failed:"
        for error in "${format_errors[@]}"; do
            echo -e "  ${RED}- $error${NC}"
        done
        if [[ $exit_code -eq 0 ]]; then exit_code=1; fi
    else
        print_success "File format is valid"
    fi

    # ============================================================================
    # 2. Check required secrets
    # ============================================================================

    print_info "Checking required secrets..."

    # Define required Android secrets
    local required_android=(
        "KEYSTORE_PASSWORD"
        "KEYALIAS"
        "KEY_PASSWORD"
        "GOOGLESERVICES"
        "PLAYSTORECREDS"
        "FIREBASECREDS"
    )

    # Map alternative key names used in this project
    declare -A key_aliases
    key_aliases["KEYSTORE_PASSWORD"]="ORIGINAL_KEYSTORE_FILE_PASSWORD|UPLOAD_KEYSTORE_FILE_PASSWORD"
    key_aliases["KEYALIAS"]="ORIGINAL_KEYSTORE_ALIAS|UPLOAD_KEYSTORE_ALIAS"
    key_aliases["KEY_PASSWORD"]="ORIGINAL_KEYSTORE_ALIAS_PASSWORD|UPLOAD_KEYSTORE_ALIAS_PASSWORD"

    # Check Android secrets
    for secret in "${required_android[@]}"; do
        local found=false

        # Check direct key name
        if grep -q "^${secret}=" "$SECRETS_FILE" || grep -q "^${secret}<<" "$SECRETS_FILE"; then
            found=true
        # Check alternative names
        elif [[ -n "${key_aliases[$secret]}" ]]; then
            IFS='|' read -ra alternatives <<< "${key_aliases[$secret]}"
            for alt in "${alternatives[@]}"; do
                if grep -q "^${alt}=" "$SECRETS_FILE" || grep -q "^${alt}<<" "$SECRETS_FILE"; then
                    found=true
                    break
                fi
            done
        fi

        if [[ "$found" = false ]]; then
            missing_secrets+=("Android: $secret")
        fi
    done

    # Check iOS secrets if iOS project detected
    if [[ -f "secrets/shared_keys.env" ]]; then
        local required_ios=(
            "APPSTORE_KEY_ID"
            "APPSTORE_ISSUER_ID"
            "APPSTORE_AUTH_KEY"
            "MATCH_PASSWORD"
            "MATCH_SSH_PRIVATE_KEY"
        )

        for secret in "${required_ios[@]}"; do
            if ! grep -q "^${secret}=" "$SECRETS_FILE" && ! grep -q "^${secret}<<" "$SECRETS_FILE"; then
                missing_secrets+=("iOS: $secret")
            fi
        done
    fi

    # Report missing secrets
    if [[ ${#missing_secrets[@]} -gt 0 ]]; then
        print_error "Missing required secrets:"
        for secret in "${missing_secrets[@]}"; do
            echo -e "  ${RED}- $secret${NC}"
        done
        if [[ $exit_code -eq 0 ]]; then exit_code=2; fi
    else
        print_success "All required secrets are present"
    fi

    # ============================================================================
    # 3. Validate base64 encoding
    # ============================================================================

    print_info "Validating base64 encoding for file secrets..."

    # Define file secrets that should be base64 encoded
    local file_secrets=(
        "GOOGLESERVICES"
        "PLAYSTORECREDS"
        "FIREBASECREDS"
        "APPSTORE_AUTH_KEY"
        "MATCH_SSH_PRIVATE_KEY"
        "ORIGINAL_KEYSTORE_FILE"
        "UPLOAD_KEYSTORE_FILE"
        "MAC_APP_DISTRIBUTION_CERTIFICATE_B64"
        "MAC_INSTALLER_DISTRIBUTION_CERTIFICATE_B64"
        "MAC_EMBEDDED_PROVISION_B64"
        "MAC_RUNTIME_PROVISION_B64"
    )

    # Extract and validate base64 values
    for secret in "${file_secrets[@]}"; do
        # Check if secret exists in file
        if ! grep -q "^${secret}<<" "$SECRETS_FILE"; then
            # Skip validation if secret doesn't exist (will be caught by required secrets check)
            continue
        fi

        # Extract the base64 value between heredoc markers
        local value=""
        local in_block=false
        local block_delimiter=""

        while IFS= read -r line; do
            if [[ "$line" =~ ^${secret}\<\<([A-Z]+)$ ]]; then
                in_block=true
                block_delimiter="${BASH_REMATCH[1]}"
                value=""
            elif [[ "$in_block" = true ]] && [[ "$line" == "$block_delimiter" ]]; then
                break
            elif [[ "$in_block" = true ]]; then
                value+="$line"
            fi
        done < "$SECRETS_FILE"

        # Validate base64 encoding
        if [[ -n "$value" ]]; then
            # Try to decode the base64 value (handle macOS vs Linux)
            if [[ "$OSTYPE" == "darwin"* ]]; then
                if ! printf '%s' "$value" | base64 -D > /dev/null 2>&1; then
                    invalid_base64+=("$secret")
                fi
            else
                if ! printf '%s' "$value" | base64 -d > /dev/null 2>&1; then
                    invalid_base64+=("$secret")
                fi
            fi
        fi
    done

    # Report invalid base64
    if [[ ${#invalid_base64[@]} -gt 0 ]]; then
        print_error "Invalid base64 encoding detected:"
        for secret in "${invalid_base64[@]}"; do
            echo -e "  ${RED}- $secret${NC}"
        done
        if [[ $exit_code -eq 0 ]]; then exit_code=3; fi
    else
        print_success "All file secrets have valid base64 encoding"
    fi

    # ============================================================================
    # Final summary
    # ============================================================================

    echo ""
    if [[ $exit_code -eq 0 ]]; then
        print_success "All validations passed"
    else
        local error_summary=""
        if [[ ${#format_errors[@]} -gt 0 ]]; then
            error_summary+="Format errors"
        fi
        if [[ ${#missing_secrets[@]} -gt 0 ]]; then
            [[ -n "$error_summary" ]] && error_summary+=", "
            error_summary+="Missing required secrets"
        fi
        if [[ ${#invalid_base64[@]} -gt 0 ]]; then
            [[ -n "$error_summary" ]] && error_summary+=", "
            error_summary+="Invalid base64 encoding"
        fi
        print_error "Validation failed: $error_summary"
    fi

    return $exit_code
}

# Function to create/update secrets.env file
update_secrets_env() {
    local original_keystore=$1
    local upload_keystore=$2
    local original_b64=$(encode_base64 "keystores/$original_keystore")
    local upload_b64=$(encode_base64 "keystores/$upload_keystore")

    # Check if secrets.env exists
    if [ -f "$ENV_FILE" ]; then
        echo -e "${BLUE}Updating existing $ENV_FILE${NC}"

        # Create a temporary file
        local temp_file="${ENV_FILE}.tmp"

        # Process the file line by line
        local in_original_block=false
        local in_upload_block=false
        local original_found=false
        local upload_found=false

        while IFS= read -r line || [ -n "$line" ]; do
            # Check if we're in the ORIGINAL_KEYSTORE_FILE block
            if [[ "$line" == "ORIGINAL_KEYSTORE_FILE<<EOF" ]]; then
                in_original_block=true
                original_found=true
                echo "$line" >> "$temp_file"
                echo "$original_b64" >> "$temp_file"
                continue
            fi

            # Check if we're in the UPLOAD_KEYSTORE_FILE block
            if [[ "$line" == "UPLOAD_KEYSTORE_FILE<<EOF" ]]; then
                in_upload_block=true
                upload_found=true
                echo "$line" >> "$temp_file"
                echo "$upload_b64" >> "$temp_file"
                continue
            fi

            # Check if we're exiting a block
            if [ "$in_original_block" = true ] && [[ "$line" == "EOF" ]]; then
                in_original_block=false
                echo "$line" >> "$temp_file"
                continue
            fi

            if [ "$in_upload_block" = true ] && [[ "$line" == "EOF" ]]; then
                in_upload_block=false
                echo "$line" >> "$temp_file"
                continue
            fi

            # Skip lines inside blocks as we've already written the new content
            if [ "$in_original_block" = true ] || [ "$in_upload_block" = true ]; then
                continue
            fi

            # Write all other lines as is
            echo "$line" >> "$temp_file"
        done < "$ENV_FILE"

        # Add blocks that weren't found
        if [ "$original_found" = false ]; then
            echo "" >> "$temp_file"
            echo "ORIGINAL_KEYSTORE_FILE<<EOF" >> "$temp_file"
            echo "$original_b64" >> "$temp_file"
            echo "EOF" >> "$temp_file"
        fi

        if [ "$upload_found" = false ]; then
            echo "" >> "$temp_file"
            echo "UPLOAD_KEYSTORE_FILE<<EOF" >> "$temp_file"
            echo "$upload_b64" >> "$temp_file"
            echo "EOF" >> "$temp_file"
        fi

        # Replace the original file
        mv "$temp_file" "$ENV_FILE"
    else
        echo -e "${BLUE}Creating new $ENV_FILE${NC}"

        # Ensure directory exists
        mkdir -p "$(dirname "$ENV_FILE")"
        # Create a new secrets.env file
        cat > "$ENV_FILE" <<EOL
# GitHub Secrets Environment File
# Format: KEY=VALUE
# Use <<EOF and EOF to denote multiline values
# Run this command to format these secrets dos2unix $ENV_FILE

ORIGINAL_KEYSTORE_FILE_PASSWORD=${ORIGINAL_KEYSTORE_FILE_PASSWORD:-Keystore_password}
ORIGINAL_KEYSTORE_ALIAS=${ORIGINAL_KEYSTORE_ALIAS:-Keystore_Alias}
ORIGINAL_KEYSTORE_ALIAS_PASSWORD=${ORIGINAL_KEYSTORE_ALIAS_PASSWORD:-Alias_password}
ORIGINAL_KEYSTORE_FILE<<EOF
$original_b64
EOF

UPLOAD_KEYSTORE_FILE_PASSWORD=${UPLOAD_KEYSTORE_FILE_PASSWORD:-Keystore_password}
UPLOAD_KEYSTORE_ALIAS=${UPLOAD_KEYSTORE_ALIAS:-Keystore_Alias}
UPLOAD_KEYSTORE_ALIAS_PASSWORD=${UPLOAD_KEYSTORE_ALIAS_PASSWORD:-Alias_password}
UPLOAD_KEYSTORE_FILE<<EOF
$upload_b64
EOF
EOL
    fi

    echo -e "${GREEN}$ENV_FILE has been updated with base64 encoded keystores${NC}"
}

# Function to update fastlane-config/project_config.rb with keystore information
update_fastlane_config() {
    local keystore_name=$1
    local keystore_password=$2
    local key_alias=$3
    local key_password=$4

    # Path to the fastlane config file
    local config_dir="fastlane-config"
    local config_file="$config_dir/project_config.rb"

    echo -e "${BLUE}Updating fastlane configuration with keystore information...${NC}"

    # Create the fastlane-config directory if it doesn't exist
    if [ ! -d "$config_dir" ]; then
        echo -e "${BLUE}Creating '$config_dir' directory...${NC}"
        mkdir -p "$config_dir"
    fi

    # Check if the config file exists
    if [ -f "$config_file" ]; then
        echo -e "${BLUE}Updating existing $config_file${NC}"

        # Create a temporary file for the updated content
        local temp_file=$(mktemp)

        # Use awk for cross-platform compatibility (works on both macOS and Linux)
        # This handles the nested keystore config structure in project_config.rb
        awk -v ks_file="$keystore_name" -v ks_pass="$keystore_password" -v k_alias="$key_alias" -v k_pass="$key_password" '
        /keystore:.*\{/,/\}/ {
            if (/file:/) {
                gsub(/file: "[^"]*"/, "file: \"" ks_file "\"")
            }
            if (/password:/ && !/key_password/) {
                gsub(/password: "[^"]*"/, "password: \"" ks_pass "\"")
            }
            if (/key_alias:/) {
                gsub(/key_alias: "[^"]*"/, "key_alias: \"" k_alias "\"")
            }
            if (/key_password:/) {
                gsub(/key_password: "[^"]*"/, "key_password: \"" k_pass "\"")
            }
        }
            { print }
            ' "$config_file" > "$temp_file"

            # Replace the original file with the updated one
            mv "$temp_file" "$config_file"
    else
        # File doesn't exist, create it with a complete structure matching project_config.rb format
        echo -e "${BLUE}Creating new $config_file${NC}"

        mkdir -p "$config_dir"

        # Create the file with the complete structure
        cat > "$config_file" << EOL
# ==============================================================================
# Project Configuration - Update these values when setting up a new project
# ==============================================================================

    module FastlaneConfig
      module ProjectConfig
        PROJECT_NAME = "kmp-project-template"
        ORGANIZATION_NAME = "Devikon Inc."

        ANDROID = {
          package_name: "cmp.android.app",
          play_store_json_key: "secrets/playStorePublishServiceCredentialsFile.json",
          apk_paths: {
            prod: "cmp-android/build/outputs/apk/prod/release/cmp-android-prod-release.apk",
            demo: "cmp-android/build/outputs/apk/demo/release/cmp-android-demo-release.apk"
          },
          aab_path: "cmp-android/build/outputs/bundle/prodRelease/cmp-android-prod-release.aab",
          keystore: {
            file: "$keystore_name",
            password: "$keystore_password",
            key_alias: "$key_alias",
            key_password: "$key_password"
          },
          firebase: {
            prod_app_id: "1:728434912738:android:REPLACE_ME",
            demo_app_id: "1:728434912738:android:REPLACE_ME",
            groups: "cmp-app-testers"
          }
        }

    SHARED = {
          firebase_service_credentials: "secrets/firebaseAppDistributionServiceCredentialsFile.json"
    }
  end
end
EOL
    fi

    echo -e "${GREEN}Fastlane configuration updated successfully${NC}"
}

# Function to update cmp-android/build.gradle.kts with keystore information
update_gradle_config() {
    local keystore_name=$1
    local keystore_password=$2
    local key_alias=$3
    local key_password=$4

    # Path to the Gradle build file
    local gradle_file="cmp-android/build.gradle.kts"

    echo -e "${BLUE}Updating Gradle build file with keystore information...${NC}"

    # Check if the file exists
    if [ -f "$gradle_file" ]; then
        echo -e "${BLUE}Updating existing $gradle_file${NC}"

        # Create a temporary file for the updated content
        local temp_file=$(mktemp)

        # Use awk for cross-platform compatibility (works on both macOS and Linux)
        awk -v ks_name="$keystore_name" -v ks_pass="$keystore_password" -v k_alias="$key_alias" -v k_pass="$key_password" '
        /storeFile = file\(System.getenv\("KEYSTORE_PATH"\)/ {
            gsub(/\?\: "[^"]*"/, "?: \"../keystores/" ks_name "\"")
            print
            next
        }
        /storePassword = System.getenv\("KEYSTORE_PASSWORD"\)/ {
            gsub(/\?\: "[^"]*"/, "?: \"" ks_pass "\"")
            print
            next
        }
        /keyAlias = System.getenv\("KEYSTORE_ALIAS"\)/ {
            gsub(/\?\: "[^"]*"/, "?: \"" k_alias "\"")
            print
            next
        }
        /keyPassword = System.getenv\("KEYSTORE_ALIAS_PASSWORD"\)/ {
            gsub(/\?\: "[^"]*"/, "?: \"" k_pass "\"")
            print
            next
        }
        { print }
        ' "$gradle_file" > "$temp_file"

        # Replace the original file with the updated one
        mv "$temp_file" "$gradle_file"
        echo -e "${GREEN}Gradle build file updated successfully${NC}"
    else
        echo -e "${YELLOW}Gradle file not found: $gradle_file${NC}"
        echo -e "${YELLOW}Skipping Gradle build file update${NC}"
    fi
}

# Function to generate keystore
generate_keystore() {
    local env=$1
    local keystore_name=$2
    local key_alias=$3
    local keystore_password=$4
    local key_password=$5

    # Use common values for other parameters
    local validity=${VALIDITY:-25}
    local keyalg=${KEYALG:-"RSA"}
    local keysize=${KEYSIZE:-2048}
    local dname=${DNAME}
    local overwrite=${OVERWRITE:-false}

    # Path to save the keystore
    local keystore_path="keystores/$keystore_name"

    echo -e "${BLUE}==================================================================${NC}"
    echo -e "${BLUE}Generating $env keystore${NC}"
    echo -e "${BLUE}==================================================================${NC}"

    echo -e "Generating keystore with the following parameters:"
    echo -e "- Environment: $env"
    echo -e "- Keystore Name: $keystore_path"
    echo -e "- Key Alias: $key_alias"
    echo -e "- Validity: $validity years"
    echo -e "- Key Algorithm: $keyalg"
    echo -e "- Key Size: $keysize"

    # Check if the keystore file already exists
    if [ -f "$keystore_path" ]; then
        if [ "$overwrite" = "true" ]; then
            echo -e "${BLUE}Overwriting existing keystore file '$keystore_path'.${NC}"
        else
            echo -e "${BLUE}Keystore file '$keystore_path' already exists and OVERWRITE is not set to 'true'.${NC}"
            echo -e "${BLUE}Using existing keystore.${NC}"
            return 0
        fi
    fi

    # Generate the keystore
    if [ -n "$dname" ]; then
        # If DNAME is provided, use it directly
        echo -e "- Distinguished Name: $dname"
        keytool -genkey -v \
            -keystore "$keystore_path" \
            -alias "$key_alias" \
            -keyalg "$keyalg" \
            -keysize "$keysize" \
            -validity $((validity*365)) \
            -storepass "$keystore_password" \
            -keypass "$key_password" \
            -dname "$dname"
    else
        # If individual DN components are provided, construct the DN using the more descriptive names
        DN_PARTS=()

        # Map more descriptive environment variables to their DN counterparts
        if [ -n "$COMPANY_NAME" ]; then
            local clean_value=$(strip_quotes "$COMPANY_NAME")
            echo -e "- Company Name (CN): $clean_value"
            DN_PARTS+=("CN=$clean_value")
        fi
        if [ -n "$DEPARTMENT" ]; then
            local clean_value=$(strip_quotes "$DEPARTMENT")
            echo -e "- Department (OU): $clean_value"
            DN_PARTS+=("OU=$clean_value")
        fi
        if [ -n "$ORGANIZATION" ]; then
            local clean_value=$(strip_quotes "$ORGANIZATION")
            echo -e "- Organization (O): $clean_value"
            DN_PARTS+=("O=$clean_value")
        fi
        if [ -n "$CITY" ]; then
            local clean_value=$(strip_quotes "$CITY")
            echo -e "- City (L): $clean_value"
            DN_PARTS+=("L=$clean_value")
        fi
        if [ -n "$STATE" ]; then
            local clean_value=$(strip_quotes "$STATE")
            echo -e "- State (ST): $clean_value"
            DN_PARTS+=("ST=$clean_value")
        fi
        if [ -n "$COUNTRY" ]; then
            local clean_value=$(strip_quotes "$COUNTRY")
            echo -e "- Country (C): $clean_value"
            DN_PARTS+=("C=$clean_value")
        fi

        # For backward compatibility, also check the traditional DN variable names
        if [ -z "$COMPANY_NAME" ] && [ -n "$CN" ]; then
            local clean_value=$(strip_quotes "$CN")
            echo -e "- Company Name (CN): $clean_value"
            DN_PARTS+=("CN=$clean_value")
        fi
        if [ -z "$DEPARTMENT" ] && [ -n "$OU" ]; then
            local clean_value=$(strip_quotes "$OU")
            echo -e "- Department (OU): $clean_value"
            DN_PARTS+=("OU=$clean_value")
        fi
        if [ -z "$ORGANIZATION" ] && [ -n "$O" ]; then
            local clean_value=$(strip_quotes "$O")
            echo -e "- Organization (O): $clean_value"
            DN_PARTS+=("O=$clean_value")
        fi
        if [ -z "$CITY" ] && [ -n "$L" ]; then
            local clean_value=$(strip_quotes "$L")
            echo -e "- City (L): $clean_value"
            DN_PARTS+=("L=$clean_value")
        fi
        if [ -z "$STATE" ] && [ -n "$ST" ]; then
            local clean_value=$(strip_quotes "$ST")
            echo -e "- State (ST): $clean_value"
            DN_PARTS+=("ST=$clean_value")
        fi
        if [ -z "$COUNTRY" ] && [ -n "$C" ]; then
            local clean_value=$(strip_quotes "$C")
            echo -e "- Country (C): $clean_value"
            DN_PARTS+=("C=$clean_value")
        fi

        if [ ${#DN_PARTS[@]} -gt 0 ]; then
            # Join the DN parts with commas
            DN=$(IFS=,; echo "${DN_PARTS[*]}")

            keytool -genkey -v \
                -keystore "$keystore_path" \
                -alias "$key_alias" \
                -keyalg "$keyalg" \
                -keysize "$keysize" \
                -validity $((validity*365)) \
                -storepass "$keystore_password" \
                -keypass "$key_password" \
                -dname "$DN"
        else
            # If no DN components are provided, use interactive mode for DN
            echo -e "${BLUE}No Distinguished Name components found in environment file for $env. Using interactive mode for certificate information.${NC}"

            keytool -genkey -v \
                -keystore "$keystore_path" \
                -alias "$key_alias" \
                -keyalg "$keyalg" \
                -keysize "$keysize" \
                -validity $((validity*365)) \
                -storepass "$keystore_password" \
                -keypass "$key_password"
        fi
    fi

    # Check if keystore was successfully created
    if [ $? -eq 0 ] && [ -f "$keystore_path" ]; then
        echo ""
        echo -e "${GREEN}===== $env Keystore created successfully! =====${NC}"
        echo -e "Keystore location: $(realpath "$keystore_path")"
        echo -e "Keystore alias: $key_alias"
        echo ""
        return 0
    else
        echo ""
        echo -e "${RED}Error: Failed to create $env keystore. Please check the error messages above.${NC}"
        return 1
    fi
}

# Function to generate both keystores
generate_keystores() {
    check_keytool
    create_keystores_dir

    # Names for local keystore files (these won't be uploaded to GitHub)
    ORIGINAL_KEYSTORE_NAME=${ORIGINAL_KEYSTORE_NAME:-"original.keystore"}
    UPLOAD_KEYSTORE_NAME=${UPLOAD_KEYSTORE_NAME:-"upload.keystore"}

    # Map GitHub secret names to local keystore variables
    ORIGINAL_KEYSTORE_FILE_PASSWORD=${ORIGINAL_KEYSTORE_FILE_PASSWORD:-"Keystore_password"}
    ORIGINAL_KEYSTORE_ALIAS=${ORIGINAL_KEYSTORE_ALIAS:-"Keystore_Alias"}
    ORIGINAL_KEYSTORE_ALIAS_PASSWORD=${ORIGINAL_KEYSTORE_ALIAS_PASSWORD:-"Alias_password"}

    UPLOAD_KEYSTORE_FILE_PASSWORD=${UPLOAD_KEYSTORE_FILE_PASSWORD:-"Keystore_password"}
    UPLOAD_KEYSTORE_ALIAS=${UPLOAD_KEYSTORE_ALIAS:-"Keystore_Alias"}
    UPLOAD_KEYSTORE_ALIAS_PASSWORD=${UPLOAD_KEYSTORE_ALIAS_PASSWORD:-"Alias_password"}

    # Generate ORIGINAL keystore
    generate_keystore "ORIGINAL" "$ORIGINAL_KEYSTORE_NAME" "$ORIGINAL_KEYSTORE_ALIAS" "$ORIGINAL_KEYSTORE_FILE_PASSWORD" "$ORIGINAL_KEYSTORE_ALIAS_PASSWORD"
    ORIGINAL_RESULT=$?

    # Generate UPLOAD keystore
    generate_keystore "UPLOAD" "$UPLOAD_KEYSTORE_NAME" "$UPLOAD_KEYSTORE_ALIAS" "$UPLOAD_KEYSTORE_FILE_PASSWORD" "$UPLOAD_KEYSTORE_ALIAS_PASSWORD"
    UPLOAD_RESULT=$?

    # Update secrets.env with base64 encoded keystores
    if [ $ORIGINAL_RESULT -eq 0 ] && [ $UPLOAD_RESULT -eq 0 ]; then
        update_secrets_env "$ORIGINAL_KEYSTORE_NAME" "$UPLOAD_KEYSTORE_NAME"

        ## Update fastlane-config/project_config.rb with UPLOAD keystore information
        update_fastlane_config "$UPLOAD_KEYSTORE_NAME" "$UPLOAD_KEYSTORE_FILE_PASSWORD" "$UPLOAD_KEYSTORE_ALIAS" "$UPLOAD_KEYSTORE_ALIAS_PASSWORD"

        # Update cmp-android/build.gradle.kts with UPLOAD keystore information
        update_gradle_config "$UPLOAD_KEYSTORE_NAME" "$UPLOAD_KEYSTORE_FILE_PASSWORD" "$UPLOAD_KEYSTORE_ALIAS" "$UPLOAD_KEYSTORE_ALIAS_PASSWORD"

        # Encode and add files from secrets/ directory
        echo ""
        encode_secrets_directory_files
    fi

    # Summary
    echo ""
    echo -e "${BLUE}==================================================================${NC}"
    echo -e "${BLUE}                          SUMMARY                                  ${NC}"
    echo -e "${BLUE}==================================================================${NC}"

    if [ $ORIGINAL_RESULT -eq 0 ]; then
        echo -e "${GREEN}ORIGINAL keystore: SUCCESS - $(realpath "keystores/$ORIGINAL_KEYSTORE_NAME")${NC}"
    else
        echo -e "${RED}ORIGINAL keystore: FAILED${NC}"
    fi

    if [ $UPLOAD_RESULT -eq 0 ]; then
        echo -e "${GREEN}UPLOAD keystore: SUCCESS - $(realpath "keystores/$UPLOAD_KEYSTORE_NAME")${NC}"
    else
        echo -e "${RED}UPLOAD keystore: FAILED${NC}"
    fi

    echo ""
    echo -e "${BLUE}IMPORTANT: Keep these keystore files and their passwords in a safe place.${NC}"
    echo -e "${BLUE}If you lose them, you will not be able to update your application on the Play Store.${NC}"

    if [ $ORIGINAL_RESULT -eq 0 ] && [ $UPLOAD_RESULT -eq 0 ]; then
        echo -e "${GREEN}$ENV_FILE has been updated with base64 encoded keystores${NC}"
        echo -e "${GREEN}fastlane-config/project_config.rb has been updated with UPLOAD keystore information${NC}"
        echo -e "${GREEN}cmp-android/build.gradle.kts has been updated with UPLOAD keystore information${NC}"
        echo -e "${BLUE}Note: If you have files in secrets/ directory, they have been encoded and added to $ENV_FILE${NC}"
        return 0
    else
        return 1
    fi
}

# Function to check if key should be excluded from GitHub
should_exclude_key() {
    local key=$1
    for excluded_key in "${EXCLUDED_GITHUB_KEYS[@]}"; do
        if [ "$key" = "$excluded_key" ]; then
            return 0  # True, should exclude
        fi
    done
    return 1  # False, should not exclude
}

# Function to add secrets from secrets.env to GitHub
add_secrets_to_github() {
    local repo=$1
    local env=$2

    check_gh_cli

    echo -e "${BLUE}Adding secrets to ${repo} from $ENV_FILE${NC}"
    if [ -n "$env" ]; then
        echo -e "${BLUE}Environment: ${env}${NC}"
    fi

    # Check if secrets.env exists
    if [ ! -f "$ENV_FILE" ]; then
        echo -e "${RED}Error: $ENV_FILE not found. Please run the 'generate' or 'sync' command first.${NC}"
        exit 1
    fi

    # Process the secrets.env file
    process_secrets_file "$repo" "$env"

    echo -e "${GREEN}All secrets have been added to GitHub successfully!${NC}"
}

# Function to process secrets from file
process_secrets_file() {
    local repo=$1
    local env=$2

    echo -e "${BLUE}Processing secrets from $ENV_FILE${NC}"

    # Process the file line by line with support for multiline values
    local current_key=""
    local current_value=""
    local multiline_mode=false
    local multiline_end=""

    while IFS= read -r line || [ -n "$line" ]; do
        # Skip empty lines and comments when not in multiline mode
        if [ "$multiline_mode" = false ] && [[ -z "$line" || "$line" == \#* ]]; then
            continue
        fi

        # Check if we're in multiline mode
        if [ "$multiline_mode" = true ]; then
            # Check if this line is the end marker for multiline
            if [[ "$line" == "$multiline_end" ]]; then
                multiline_mode=false

                # Add secret only if it's not in the excluded list
                if ! should_exclude_key "$current_key"; then
                    echo -e "${BLUE}Adding multiline secret: $current_key${NC}"

                    if [ -n "$env" ]; then
                        echo -n "$current_value" | gh secret set "$current_key" --repo="$repo" --env="$env"
                    else
                        echo -n "$current_value" | gh secret set "$current_key" --repo="$repo"
                    fi
                else
                    echo -e "${YELLOW}Skipping excluded key: $current_key (not sent to GitHub)${NC}"
                fi

                current_key=""
                current_value=""
            else
                # Append this line to the multiline value
                if [ -n "$current_value" ]; then
                    current_value+=$'\n'
                fi
                current_value+="$line"
            fi
        else
            # Check if this is the start of a multiline value using pattern matching
            if echo "$line" | grep -q "<<"; then
                # Extract the key (part before <<)
                current_key=$(echo "$line" | cut -d '<' -f1 | xargs)
                # Extract the delimiter (part after <<)
                multiline_end=$(echo "$line" | sed 's/.*<<\(.*\)/\1/')
                multiline_mode=true
                current_value=""
            elif echo "$line" | grep -q "="; then
                # This is a regular key=value line
                key=$(echo "$line" | cut -d '=' -f1 | xargs)
                value=$(echo "$line" | cut -d '=' -f2-)

                # Strip quotes for the actual value
                value=$(strip_quotes "$value")

                # Add secret only if it's not in the excluded list
                if ! should_exclude_key "$key"; then
                    echo -e "${BLUE}Adding secret: $key${NC}"

                    if [ -n "$env" ]; then
                        echo -n "$value" | gh secret set "$key" --repo="$repo" --env="$env"
                    else
                        echo -n "$value" | gh secret set "$key" --repo="$repo"
                    fi
                else
                    echo -e "${YELLOW}Skipping excluded key: $key (not sent to GitHub)${NC}"
                fi
            fi
        fi
    done < "$ENV_FILE"

    # Check if we're still in multiline mode at the end of the file
    if [ "$multiline_mode" = true ]; then
        echo -e "${RED}Error: Unterminated multiline secret. Missing closing delimiter: $multiline_end${NC}"
        return 1
    fi

    return 0
}

# Function to list secrets
list_secrets() {
    local repo=$1
    local env=$2

    check_gh_cli

    echo -e "${BLUE}Listing secrets for ${repo}${NC}"

    if [ -n "$env" ]; then
        echo -e "${BLUE}Environment: ${env}${NC}"
        gh secret list --repo="$repo" --env="$env"
    else
        gh secret list --repo="$repo"
    fi
}

# Function to delete a secret
delete_secret() {
    local repo=$1
    local name=$2
    local env=$3

    check_gh_cli

    echo -e "${BLUE}Deleting secret ${name} from ${repo}${NC}"

    if [ -n "$env" ]; then
        echo -e "${BLUE}Environment: ${env}${NC}"
        gh secret delete "$name" --repo="$repo" --env="$env"
    else
        gh secret delete "$name" --repo="$repo"
    fi

    echo -e "${GREEN}Secret deleted successfully!${NC}"
}

# Function to delete all secrets in env file from GitHub repository
delete_all_repo_secrets() {
    local repo=$1
    local env=$2
    local include_excluded=${3:-false}  # Default to false if not provided

    check_gh_cli

    echo -e "${BLUE}Deleting all secrets from ${repo} that are in $ENV_FILE${NC}"
    if [ -n "$env" ]; then
        echo -e "${BLUE}Environment: ${env}${NC}"
    fi

    if [ "$include_excluded" = "true" ]; then
        echo -e "${YELLOW}Warning: Including excluded secrets in deletion${NC}"
    fi

    # Check if secrets.env exists
    if [ ! -f "$ENV_FILE" ]; then
        echo -e "${RED}Error: $ENV_FILE file not found.${NC}"
        exit 1
    fi

    # First, get a list of all secrets in the repo
    echo -e "${BLUE}Fetching current secrets from GitHub...${NC}"

    local temp_secrets_list=$(mktemp)
    if [ -n "$env" ]; then
        gh secret list --repo="$repo" --env="$env" > "$temp_secrets_list"
    else
        gh secret list --repo="$repo" > "$temp_secrets_list"
    fi

    # Variables to track progress
    local deleted_count=0
    local skipped_count=0
    local excluded_count=0
    local deleted_secrets=()
    local skipped_secrets=()
    local excluded_secrets=()

    # Process the file line by line to find secrets
    echo -e "${BLUE}Processing secrets from $ENV_FILE...${NC}"
    local multiline_mode=false
    local multiline_end=""

    while IFS= read -r line || [ -n "$line" ]; do
        # Skip empty lines and comments when not in multiline mode
        if [ "$multiline_mode" = false ] && [[ -z "$line" || "$line" == \#* ]]; then
            continue
        fi

        # Check if we're exiting a multiline block
        if [ "$multiline_mode" = true ] && [[ "$line" == "$multiline_end" ]]; then
            multiline_mode=false
            continue
        fi

        # Skip content lines inside multiline blocks
        if [ "$multiline_mode" = true ]; then
            continue
        fi

        # Extract key from regular lines or multiline start
        local key=""
        if [[ "$line" == *"<<"* ]]; then
            # Extract the key (part before <<)
            key=$(echo "$line" | cut -d '<' -f1 | xargs)
            # Extract the delimiter (part after <<)
            multiline_end=$(echo "$line" | sed 's/.*<<\(.*\)/\1/')
            multiline_mode=true
        elif [[ "$line" == *"="* ]]; then
            # This is a regular key=value line
            key=$(echo "$line" | cut -d '=' -f1 | xargs)
        else
            continue
        fi

        # Skip empty keys
        if [ -z "$key" ]; then
            continue
        fi

        # Check if key should be excluded
        local is_excluded=false
        if should_exclude_key "$key"; then
            is_excluded=true
            if [ "$include_excluded" != "true" ]; then
                echo -e "${YELLOW}Skipping excluded key: $key${NC}"
                excluded_count=$((excluded_count + 1))
                excluded_secrets+=("$key")
                continue
            else
                echo -e "${YELLOW}Including excluded key (due to flag): $key${NC}"
            fi
        fi

        # Check if the key exists in the repo
        if grep -q "$key" "$temp_secrets_list"; then
            if [ "$is_excluded" = true ]; then
                echo -e "${YELLOW}Deleting excluded secret: $key${NC}"
            else
                echo -e "${BLUE}Deleting secret: $key${NC}"
            fi

            if [ -n "$env" ]; then
                gh secret delete "$key" --repo="$repo" --env="$env"
            else
                gh secret delete "$key" --repo="$repo"
            fi

            if [ $? -eq 0 ]; then
                if [ "$is_excluded" = true ]; then
                    excluded_count=$((excluded_count + 1))
                    excluded_secrets+=("$key (deleted)")
                else
                    deleted_count=$((deleted_count + 1))
                    deleted_secrets+=("$key")
                fi
            else
                echo -e "${RED}Failed to delete secret: $key${NC}"
                skipped_count=$((skipped_count + 1))
                skipped_secrets+=("$key (error)")
            fi
        else
            echo -e "${YELLOW}Secret not found in repo: $key${NC}"
            skipped_count=$((skipped_count + 1))
            skipped_secrets+=("$key (not found)")
        fi
    done < "$ENV_FILE"

    # Clean up
    rm -f "$temp_secrets_list"

    # Summary
    echo ""
    echo -e "${BLUE}==================================================================${NC}"
    echo -e "${BLUE}                          SUMMARY                                  ${NC}"
    echo -e "${BLUE}==================================================================${NC}"
    echo -e "${GREEN}Deleted $deleted_count secrets${NC}"
    echo -e "${YELLOW}Skipped $skipped_count secrets (not found in repo or errors)${NC}"
    echo -e "${YELLOW}Excluded $excluded_count secrets${NC}"

    if [ ${#deleted_secrets[@]} -gt 0 ]; then
        echo ""
        echo -e "${GREEN}Deleted secrets:${NC}"
        for secret in "${deleted_secrets[@]}"; do
            echo -e "  - $secret"
        done
    fi

    if [ ${#excluded_secrets[@]} -gt 0 ]; then
        echo ""
        echo -e "${YELLOW}Excluded secrets:${NC}"
        for secret in "${excluded_secrets[@]}"; do
            echo -e "  - $secret"
        done
    fi

    echo ""
    echo -e "${GREEN}Secret deletion process completed${NC}"
}

INCLUDE_EXCLUDED="false"  # Default value

# Parse command line arguments
if [ "$1" != "" ]; then
    COMMAND=$1
    shift
fi

for i in "$@"; do
    case $i in
        --repo=*)
        REPO="${i#*=}"
        shift
        ;;
        --env=*)
        ENV="${i#*=}"
        shift
        ;;
        --include-excluded)
        INCLUDE_EXCLUDED="true"
        shift
        ;;
        --name=*)
        SECRET_NAME="${i#*=}"
        shift
        ;;
        --env-file=*)
        ENV_FILE_OVERRIDE="${i#*=}"
        shift
        ;;
        *)
        # Unknown option
        ;;
    esac
done

# Resolve which env file to use
resolve_env_file

# Load variables safely from secrets.env if it exists
# Only show the loading message for the view command
show_message="false"
if [ "$COMMAND" = "view" ]; then
    show_message="true"
fi

if [ -f "$ENV_FILE" ]; then
    load_env_vars "$ENV_FILE" "$show_message"
fi

# Execute the appropriate command
case $COMMAND in
    generate)
        generate_keystores
        ;;
      encode-secrets)
              create_secrets_dir
              encode_secrets_directory_files
              ;;
          sync)
              echo -e "${BLUE}==================================================================${NC}"
              echo -e "${BLUE}              Synchronizing Secrets to $ENV_FILE                 ${NC}"
              echo -e "${BLUE}==================================================================${NC}"
              echo

              # Ensure secrets directory exists
              mkdir -p "$(dirname "$ENV_FILE")"

              # Create backup
              if [[ -f "$ENV_FILE" ]]; then
                  cp "$ENV_FILE" "${ENV_FILE}.backup"
                  print_info "Created backup: ${ENV_FILE}.backup"
              fi

              # Step 1: Parse shared_keys.env (iOS string secrets)
              echo
              print_info "[1/7] Parsing shared_keys.env for iOS string secrets..."
              parse_shared_keys_env

              # Step 2: Parse macOS password files
              echo
              print_info "[2/7] Parsing macOS password files..."
              parse_macos_password_files

              # Step 3: Encode file-based secrets from secrets/ directory
              echo
              print_info "[3/7] Encoding file-based secrets to base64..."
              encode_secrets_directory_files

              # Step 4: Update secrets.env with iOS string secrets
              echo
              print_info "[4/7] Updating $ENV_FILE with iOS string secrets..."
              update_ios_string_secrets

              # Step 5: Add Desktop & macOS App Store placeholders (before populating passwords)
              echo
              print_info "[5/7] Adding Desktop & macOS App Store placeholders..."
              add_desktop_placeholders

              # Step 6: Update macOS password secrets (after placeholders ensure keys exist)
              echo
              print_info "[6/7] Updating $ENV_FILE with macOS password secrets..."
              update_macos_password_secrets

              # Step 7: Validate result
              echo
              print_info "[7/7] Validating $ENV_FILE..."
              echo
              if validate_sync_result; then
                  echo
                  print_success "Secrets synchronized successfully to $ENV_FILE"

                  # Show summary
                  echo
                  print_info "Summary:"
                  total_string=$(grep -cE "^[A-Z_]+=" "$ENV_FILE" 2>/dev/null || echo "0")
                  total_file=$(grep -c "<<EOF" "$ENV_FILE" 2>/dev/null || echo "0")
                  echo "  Total string secrets: $total_string"
                  echo "  Total file secrets:   $total_file"
                  echo
                  print_info "Next steps:"
                  echo "  1. Review $ENV_FILE"
                  echo "  2. Upload to GitHub: ./keystore-manager.sh add --repo=owner/repo"
              else
                  echo
                  print_error "Validation failed - check errors above"
                  print_info "Backup available at: ${ENV_FILE}.backup"
                  exit 1
          fi
          ;;
    view)
        view_secrets
        ;;
    add)
        if [ -z "$REPO" ]; then
            echo -e "${RED}Error: Repository is required.${NC}"
            echo -e "Usage: ./keystore-manager.sh add --repo=username/repo [--env=environment]"
            exit 1
        fi
        add_secrets_to_github "$REPO" "$ENV"
        ;;
    list)
        if [ -z "$REPO" ]; then
            echo -e "${RED}Error: Repository is required.${NC}"
            echo -e "Usage: ./keystore-manager.sh list --repo=username/repo [--env=environment]"
            exit 1
        fi
        list_secrets "$REPO" "$ENV"
        ;;
    delete)
        if [ -z "$REPO" ] || [ -z "$SECRET_NAME" ]; then
            echo -e "${RED}Error: Repository and secret name are required.${NC}"
            echo -e "Usage: ./keystore-manager.sh delete --repo=username/repo --name=SECRET_NAME [--env=environment]"
            exit 1
        fi
        delete_secret "$REPO" "$SECRET_NAME" "$ENV"
        ;;
    delete-all)
        if [ -z "$REPO" ]; then
            echo -e "${RED}Error: Repository is required.${NC}"
            echo -e "Usage: ./keystore-manager.sh delete-all --repo=username/repo [--env=environment] [--include-excluded]"
            exit 1
        fi
        delete_all_repo_secrets "$REPO" "$ENV" "$INCLUDE_EXCLUDED"
        ;;
    help)
        show_help
        ;;
    *)
        echo -e "${RED}Unknown command: $COMMAND${NC}"
        show_help
        exit 1
        ;;
esac