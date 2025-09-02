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

# Default environment file path
ENV_FILE="secrets.env"

# Default values
COMMAND="generate"
REPO=""
ENV=""
SECRET_NAME=""

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
    echo "  view     - View all secrets in the secrets.env file as a formatted table"
    echo "  add      - Add secrets to a GitHub repository from secrets.env"
    echo "  list     - List all secrets in a GitHub repository"
    echo "  delete   - Delete a secret from a GitHub repository"
    echo "  delete-all - Delete all secrets from a GitHub repository that are in secrets.env"
    echo "               Use --include-excluded flag to also delete excluded secrets"
    echo "  help     - Show this help message"
    echo ""
    echo "Options:"
    echo "  --repo=username/repo - GitHub repository name"
    echo "  --env=environment    - GitHub environment name"
    echo "  --name=SECRET_NAME   - Secret name (for delete command)"
    echo ""
    echo "Examples:"
    echo "  ./keystore-manager.sh generate"
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
            # macOS
            base64 "$file_path"
        else
            # Linux
            base64 -w 0 "$file_path"
        fi
    else
        echo -e "${RED}Error: File not found: $file_path${NC}"
        return 1
    fi
}

# Function to create/update secrets.env file
update_secrets_env() {
    local original_keystore=$1
    local upload_keystore=$2
    local original_b64=$(encode_base64 "keystores/$original_keystore")
    local upload_b64=$(encode_base64 "keystores/$upload_keystore")

    # Check if secrets.env exists
    if [ -f "$ENV_FILE" ]; then
        echo -e "${BLUE}Updating existing secrets.env file${NC}"

        # Create a temporary file
        local temp_file="secrets.env.tmp"

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
        echo -e "${BLUE}Creating new secrets.env file${NC}"

        # Create a new secrets.env file
        cat > "$ENV_FILE" <<EOL
# GitHub Secrets Environment File
# Format: KEY=VALUE
# Use <<EOF and EOF to denote multiline values
# Run this command to format these secrets dos2unix secrets.env

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

    echo -e "${GREEN}secrets.env file has been updated with base64 encoded keystores${NC}"
}

# Function to update fastlane-config/android_config.rb with keystore information
update_fastlane_config() {
    local keystore_name=$1
    local keystore_password=$2
    local key_alias=$3
    local key_password=$4

    # Path to the fastlane config file
    local config_dir="fastlane-config"
    local config_file="$config_dir/android_config.rb"

    echo -e "${BLUE}Updating fastlane configuration with keystore information...${NC}"

    # Create the fastlane-config directory if it doesn't exist
    if [ ! -d "$config_dir" ]; then
        echo -e "${BLUE}Creating '$config_dir' directory...${NC}"
        mkdir -p "$config_dir"
    fi

    # Check if the config file exists
    if [ -f "$config_file" ]; then
        echo -e "${BLUE}Updating existing $config_file${NC}"

        # Use sed to replace the values directly
        # This keeps the file structure intact while only changing the values
        sed -i.bak \
            -e "s|default_store_file:.*|default_store_file: \"$keystore_name\",|" \
            -e "s|default_store_password:.*|default_store_password: \"$keystore_password\",|" \
            -e "s|default_key_alias:.*|default_key_alias: \"$key_alias\",|" \
            -e "s|default_key_password:.*|default_key_password: \"$key_password\"|" \
            "$config_file"

        # Remove the backup file
        rm -f "$config_file.bak"
    else
        # File doesn't exist, create it with a complete structure
        echo -e "${BLUE}Creating new $config_file${NC}"

        mkdir -p "$config_dir"

        # Create the file with the complete structure
        cat > "$config_file" << EOL
module FastlaneConfig
  module AndroidConfig
    STORE_CONFIG = {
      default_store_file: "$keystore_name",
      default_store_password: "$keystore_password",
      default_key_alias: "$key_alias",
      default_key_password: "$key_password"
    }

    FIREBASE_CONFIG = {
      firebase_prod_app_id: "1:728433984912738:android:3902eb32kjaska3363b0938f1a1dbb",
      firebase_demo_app_id: "1:72843493212738:android:8392hjks3298ak9032skja",
      firebase_service_creds_file: "secrets/firebaseAppDistributionServiceCredentialsFile.json",
      firebase_groups: "mifos-mobile-apps"
    }

    BUILD_PATHS = {
      prod_apk_path: "cmp-android/build/outputs/apk/prod/release/cmp-android-prod-release.apk",
      demo_apk_path: "cmp-android/build/outputs/apk/demo/release/cmp-android-demo-release.apk",
      prod_aab_path: "cmp-android/build/outputs/bundle/prodRelease/cmp-android-prod-release.aab"
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

        # Create a backup of the original file
        cp "$gradle_file" "$gradle_file.bak"

        # Use sed to update the signing configuration
        sed -i \
            -e "s|storeFile = file(System.getenv(\"KEYSTORE_PATH\") ?: \".*\")|storeFile = file(System.getenv(\"KEYSTORE_PATH\") ?: \"../keystores/$keystore_name\")|" \
            -e "s|storePassword = System.getenv(\"KEYSTORE_PASSWORD\") ?: \".*\"|storePassword = System.getenv(\"KEYSTORE_PASSWORD\") ?: \"$keystore_password\"|" \
            -e "s|keyAlias = System.getenv(\"KEYSTORE_ALIAS\") ?: \".*\"|keyAlias = System.getenv(\"KEYSTORE_ALIAS\") ?: \"$key_alias\"|" \
            -e "s|keyPassword = System.getenv(\"KEYSTORE_ALIAS_PASSWORD\") ?: \".*\"|keyPassword = System.getenv(\"KEYSTORE_ALIAS_PASSWORD\") ?: \"$key_password\"|" \
            "$gradle_file"

        # Remove the backup file
        rm -f "$gradle_file.bak"
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

        # Update fastlane-config/android_config.rb with UPLOAD keystore information
        update_fastlane_config "$UPLOAD_KEYSTORE_NAME" "$UPLOAD_KEYSTORE_FILE_PASSWORD" "$UPLOAD_KEYSTORE_ALIAS" "$UPLOAD_KEYSTORE_ALIAS_PASSWORD"

        # Update cmp-android/build.gradle.kts with UPLOAD keystore information
        update_gradle_config "$UPLOAD_KEYSTORE_NAME" "$UPLOAD_KEYSTORE_FILE_PASSWORD" "$UPLOAD_KEYSTORE_ALIAS" "$UPLOAD_KEYSTORE_ALIAS_PASSWORD"
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
        echo -e "${GREEN}secrets.env has been updated with base64 encoded keystores${NC}"
        echo -e "${GREEN}fastlane-config/android_config.rb has been updated with UPLOAD keystore information${NC}"
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

    echo -e "${BLUE}Adding secrets to ${repo} from secrets.env${NC}"
    if [ -n "$env" ]; then
        echo -e "${BLUE}Environment: ${env}${NC}"
    fi

    # Check if secrets.env exists
    if [ ! -f "$ENV_FILE" ]; then
        echo -e "${RED}Error: secrets.env file not found. Please run the 'generate' command first.${NC}"
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
        *)
        # Unknown option
        ;;
    esac
done

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