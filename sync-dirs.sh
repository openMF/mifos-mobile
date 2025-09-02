#!/bin/bash

# sync-dirs.sh
# Script to sync directories and files from upstream repository

# Colors and formatting
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
BOLD='\033[1m'
NC='\033[0m'    # No Color
CHECKMARK='\xE2\x9C\x94'
CROSS='\xE2\x9C\x98'

# Default upstream URL
DEFAULT_UPSTREAM_URL="https://github.com/openMF/kmp-project-template.git"

# Script options
DRY_RUN=false
FORCE=false
LOG_FILE="sync-$(date +%d%m%Y-%H%M%S).log"

# Directories and files to sync
SYNC_DIRS=(
    "cmp-android"
    "cmp-desktop"
    "cmp-ios"
    "cmp-web"
    "cmp-shared"
    "core-base"
    "build-logic"
    "fastlane"
    "scripts"
    "config"
    ".github"
    ".run"
)

SYNC_FILES=(
    "Gemfile"
    "Gemfile.lock"
    "ci-prepush.bat"
    "ci-prepush.sh"
)

# Define exclusions for directories and files
# Format: "path/to/exclude:type"
# type can be 'dir' or 'file'
# Use "root" key for files in the root directory
declare -A EXCLUSIONS=(
    ["cmp-android"]="src/main/res:dir dependencies:dir src/main/ic_launcher-playstore.png:file google-services.json:file"
    ["cmp-web"]="src/jsMain/resources:dir src/wasmJsMain/resources:dir"
    ["cmp-desktop"]="icons:dir"
    ["cmp-ios"]="iosApp/Assets.xcassets:dir"
    ["root"]="secrets.env:file"
)

# Display help information
show_help() {
    echo -e "${BOLD}Usage:${NC} ./sync-dirs.sh [options]"
    echo
    echo -e "${BOLD}Description:${NC}"
    echo "  This script syncs directories and files from an upstream repository."
    echo "  It preserves excluded files and directories as defined in the script."
    echo
    echo -e "${BOLD}Options:${NC}"
    echo "  -h, --help      Display this help message and exit"
    echo "  --dry-run       Show what would be done without making changes"
    echo "  -f, --force     Skip confirmation prompts and proceed automatically"
    echo
    echo -e "${BOLD}Examples:${NC}"
    echo "  ./sync-dirs.sh              # Run with interactive prompts"
    echo "  ./sync-dirs.sh --dry-run    # Test run without changes"
    echo "  ./sync-dirs.sh --force      # Run with no prompts"
}

# Logging function
log_message() {
    echo "[$(date '+%Y-%m-%d %H:%M:%S')] $1" >> "$LOG_FILE"
    echo -e "$1"
}

# Error handling function
handle_error() {
    log_message "${RED}${CROSS} Error: $1${NC}"
    exit 1
}

# Print error message
print_error() {
    log_message "${RED}${CROSS} Error: $1${NC}"
}

# Simple progress indicator function
show_progress() {
    if [ "$DRY_RUN" = false ]; then
        echo -ne "${BLUE}[                    ]${NC}\r"
        echo -ne "${BLUE}[=====               ]${NC}\r"
        sleep 0.1
        echo -ne "${BLUE}[==========          ]${NC}\r"
        sleep 0.1
        echo -ne "${BLUE}[===============     ]${NC}\r"
        sleep 0.1
        echo -ne "${BLUE}[====================]${NC}"
        echo
    fi
}

# Fancy banner
print_banner() {
    echo -e "${BLUE}╔════════════════════════════════════════════╗${NC}"
    echo -e "${BLUE}║${BOLD}        Project Directory Sync Tool         ${NC}${BLUE}║${NC}"
    echo -e "${BLUE}╚════════════════════════════════════════════╝${NC}"
    echo
}

# Print step with color and symbol
print_step() {
    log_message "${GREEN}${CHECKMARK} $1${NC}"
}

# Print warning with color
print_warning() {
    log_message "${YELLOW}⚠ $1${NC}"
}

# Function to generate unique branch name
get_sync_branch_name() {
    local date_suffix=$(date +%Y%m%d-%H%M%S)
    echo "sync/upstream-${date_suffix}"
}

# Print directories and files to be synced
print_items() {
    echo -e "${BLUE}Items to sync:${NC}"
    echo -e "${BOLD}Directories:${NC}"
    for dir in "${SYNC_DIRS[@]}"; do
        echo -e "  ${BOLD}→${NC} $dir"
    done

    echo -e "\n${BOLD}Files:${NC}"
    for file in "${SYNC_FILES[@]}"; do
        echo -e "  ${BOLD}→${NC} $file"
    done
    echo
}

# Function to check if a path is excluded
is_excluded() {
    local check_dir=$1
    local full_path=$2
    local check_type=$3  # 'file' or 'dir'

    # Remove ./ from the beginning of the path if it exists
    full_path="${full_path#./}"

    # Check for root-level exclusions
    if [ -n "${EXCLUSIONS["root"]}" ] && [[ "$check_type" == "file" ]]; then
        local IFS=' '
        read -ra ROOT_EXCLUDE_ITEMS <<< "${EXCLUSIONS["root"]}"

        for item in "${ROOT_EXCLUDE_ITEMS[@]}"; do
            local IFS=':'
            read -ra PARTS <<< "$item"
            local exclude_path="${PARTS[0]}"
            local exclude_type="${PARTS[1]}"

            if [ "$exclude_type" = "$check_type" ] && [ "$full_path" = "$exclude_path" ]; then
                return 0  # Path is excluded
            fi
        done
    fi

    # Check directory-specific exclusions
    for dir in "${!EXCLUSIONS[@]}"; do
        # Skip the root key as we've already checked it
        if [ "$dir" = "root" ]; then
            continue
        fi

        # Check if the path starts with the directory we're looking at
        if [[ "$full_path" == "$dir"* ]]; then
            local IFS=' '
            read -ra EXCLUDE_ITEMS <<< "${EXCLUSIONS[$dir]}"

            for item in "${EXCLUDE_ITEMS[@]}"; do
                local IFS=':'
                read -ra PARTS <<< "$item"
                local exclude_path="$dir/${PARTS[0]}"
                local exclude_type="${PARTS[1]}"

                # Remove any duplicate slashes
                exclude_path=$(echo "$exclude_path" | sed 's#/\+#/#g')
                full_path=$(echo "$full_path" | sed 's#/\+#/#g')

                if [ "$exclude_type" = "$check_type" ] && [ "$full_path" = "$exclude_path" ]; then
                    return 0  # Path is excluded
                fi
            done
        fi
    done
    return 1  # Path is not excluded
}

cleanup_temp_dirs() {
    print_step "Cleaning up temporary directories..."
    find . -type d -name "temp_*" -exec rm -rf {} +
    show_progress
}

# Function to preserve excluded paths
preserve_excluded_paths() {
    local dir=$1
    local destination=$2

    if [ -n "${EXCLUSIONS[$dir]}" ]; then
        local IFS=' '
        read -ra EXCLUDE_ITEMS <<< "${EXCLUSIONS[$dir]}"

        for item in "${EXCLUDE_ITEMS[@]}"; do
            local IFS=':'
            read -ra PARTS <<< "$item"
            local exclude_path="${PARTS[0]}"
            local exclude_type="${PARTS[1]}"
            local full_source_path="$dir/$exclude_path"
            local full_dest_path="$destination/$exclude_path"

            if [ -e "$full_source_path" ]; then
                print_step "Preserving excluded ${exclude_type}: ${BOLD}$exclude_path${NC}"
                mkdir -p "$(dirname "$full_dest_path")"
                cp -r "$full_source_path" "$(dirname "$full_dest_path")"
            fi
        done
    fi
}

# Function to sync directory with exclusions
sync_directory() {
    local dir=$1
    local temp_branch=$2

    if [ -d "$dir" ]; then
        print_step "Syncing ${BOLD}$dir${NC}..."

        if [ "$DRY_RUN" = false ]; then
            # Create temporary directory for original content
            mkdir -p "temp_$dir"

            # Store original directory for excluded items
            if [ -d "$dir" ]; then
                # First handle directory exclusions
                if [ -n "${EXCLUSIONS[$dir]}" ]; then
                    local IFS=' '
                    read -ra EXCLUDE_ITEMS <<< "${EXCLUSIONS[$dir]}"

                    for item in "${EXCLUDE_ITEMS[@]}"; do
                        local IFS=':'
                        read -ra PARTS <<< "$item"
                        local exclude_path="$dir/${PARTS[0]}"
                        local exclude_type="${PARTS[1]}"

                        if [ "$exclude_type" = "dir" ] && [ -e "$exclude_path" ]; then
                            print_step "Preserving excluded directory: ${BOLD}${PARTS[0]}${NC}"
                            mkdir -p "$(dirname "temp_$exclude_path")"
                            cp -r "$exclude_path" "$(dirname "temp_$exclude_path")"
                        elif [ "$exclude_type" = "file" ] && [ -f "$exclude_path" ]; then
                            print_step "Preserving excluded file: ${BOLD}${PARTS[0]}${NC}"
                            mkdir -p "$(dirname "temp_$exclude_path")"
                            cp "$exclude_path" "temp_$exclude_path"
                        fi
                    done
                fi
            fi

            # Checkout from upstream
            git checkout "$temp_branch" -- "$dir" || {
                print_error "Failed to sync $dir"
                rm -rf "temp_$dir"
                return 1
            }

            # Restore excluded files and directories
            if [ -n "${EXCLUSIONS[$dir]}" ]; then
                local IFS=' '
                read -ra EXCLUDE_ITEMS <<< "${EXCLUSIONS[$dir]}"

                for item in "${EXCLUDE_ITEMS[@]}"; do
                    local IFS=':'
                    read -ra PARTS <<< "$item"
                    local exclude_path="$dir/${PARTS[0]}"
                    local exclude_type="${PARTS[1]}"
                    local temp_path="temp_$exclude_path"

                    if [ -e "$temp_path" ]; then
                        print_step "Restoring excluded ${exclude_type}: ${BOLD}${PARTS[0]}${NC}"
                        mkdir -p "$(dirname "$exclude_path")"
                        if [ "$exclude_type" = "dir" ]; then
                            rm -rf "$exclude_path"
                            cp -r "$temp_path" "$(dirname "$exclude_path")"
                        else
                            cp "$temp_path" "$exclude_path"
                        fi
                    fi
                done
            fi
        fi
    else
        print_warning "Directory ${BOLD}$dir${NC} not found. Creating it..."
        if [ "$DRY_RUN" = false ]; then
            mkdir -p "$dir"
            git checkout "$temp_branch" -- "$dir" || {
                handle_error "Failed to sync $dir"
                cleanup_temp_dirs
            }
        fi
    fi
    show_progress
}

# Function to sync individual file with exclusions
sync_file() {
    local file=$1
    local temp_branch=$2

    # Check if file should be excluded (root-level or directory-specific)
    if is_excluded "$(dirname "$file")" "$file" "file"; then
        print_step "Skipping excluded file: ${BOLD}$file${NC}"
        return
    fi

    print_step "Syncing ${BOLD}$file${NC}..."
    if [ "$DRY_RUN" = false ]; then
        if [ -f "$file" ]; then
            # Create directory for excluded files if it doesn't exist
            mkdir -p "temp_files"
            # Store original file if it exists
            cp "$file" "temp_files/$(basename "$file")"
        fi

        if ! git checkout "$temp_branch" -- "$file"; then
            if [ -f "temp_files/$(basename "$file")" ]; then
                # Restore original file if checkout fails
                cp "temp_files/$(basename "$file")" "$file"
            fi
            print_error "Failed to sync $file"
            return 1
        fi
    fi
    show_progress
}

# Function to get default branch name
get_default_branch() {
    local default_branch
    default_branch=$(git remote show origin | grep 'HEAD branch' | cut -d' ' -f5)
    echo "$default_branch"
}

# Function to preserve root-level excluded files
preserve_root_files() {
    if [ -n "${EXCLUSIONS["root"]}" ] && [ "$DRY_RUN" = false ]; then
        print_step "Preserving root-level excluded files..."
        mkdir -p "temp_root"

        local IFS=' '
        read -ra ROOT_EXCLUDE_ITEMS <<< "${EXCLUSIONS["root"]}"

        for item in "${ROOT_EXCLUDE_ITEMS[@]}"; do
            local IFS=':'
            read -ra PARTS <<< "$item"
            local exclude_path="${PARTS[0]}"
            local exclude_type="${PARTS[1]}"

            if [ "$exclude_type" = "file" ] && [ -f "$exclude_path" ]; then
                print_step "Preserving root file: ${BOLD}$exclude_path${NC}"
                cp "$exclude_path" "temp_root/"
            fi
        done
    fi
}

# Function to restore root-level excluded files
restore_root_files() {
    if [ -n "${EXCLUSIONS["root"]}" ] && [ "$DRY_RUN" = false ]; then
        print_step "Restoring root-level excluded files..."

        local IFS=' '
        read -ra ROOT_EXCLUDE_ITEMS <<< "${EXCLUSIONS["root"]}"

        for item in "${ROOT_EXCLUDE_ITEMS[@]}"; do
            local IFS=':'
            read -ra PARTS <<< "$item"
            local exclude_path="${PARTS[0]}"
            local exclude_type="${PARTS[1]}"

            if [ "$exclude_type" = "file" ] && [ -f "temp_root/$(basename "$exclude_path")" ]; then
                print_step "Restoring root file: ${BOLD}$exclude_path${NC}"
                cp "temp_root/$(basename "$exclude_path")" "$exclude_path"
            fi
        done

        rm -rf "temp_root"
    fi
}

# Parse command line arguments
while [[ $# -gt 0 ]]; do
    case $1 in
        -h|--help)
            show_help
            exit 0
            ;;
        --dry-run)
            DRY_RUN=true
            shift
            ;;
        -f|--force)
            FORCE=true
            shift
            ;;
        *)
            handle_error "Unknown option: $1. Use --help for usage information."
            ;;
    esac
done

# Check git configuration
if ! git config user.name > /dev/null || ! git config user.email > /dev/null; then
    handle_error "Git user.name or user.email not configured"
fi

# Main script
clear
print_banner
print_items

# Print configured exclusions
echo -e "${BLUE}Configured Exclusions:${NC}"
OLD_IFS="$IFS"  # Save original IFS
for dir in "${!EXCLUSIONS[@]}"; do
    echo -e "  ${BOLD}${dir}${NC}:"
    IFS=' '
    read -ra EXCLUDE_ITEMS <<< "${EXCLUSIONS[$dir]}"
    for item in "${EXCLUDE_ITEMS[@]}"; do
        IFS=':'
        read -ra PARTS <<< "$item"
        if [ "$dir" = "root" ]; then
            echo -e "    → ${PARTS[0]} (${PARTS[1]}) [root level]"
        else
            echo -e "    → ${PARTS[0]} (${PARTS[1]})"
        fi
    done
done
IFS="$OLD_IFS"  # Restore original IFS
echo

# Check if upstream remote exists
if ! git remote | grep -q '^upstream$'; then
    print_warning "Upstream remote not found."
    if [ "$DRY_RUN" = false ]; then
        echo -e "${YELLOW}Default upstream URL:${NC} ${BOLD}$DEFAULT_UPSTREAM_URL${NC}"
        if [ "$FORCE" = false ]; then
            echo -e "${YELLOW}Press Enter to use default URL or input a different one:${NC}"
            read -r custom_url
        else
            custom_url=""
        fi

        upstream_url=${custom_url:-$DEFAULT_UPSTREAM_URL}
        print_step "Adding upstream remote: ${BOLD}$upstream_url${NC}"
        git remote add upstream "$upstream_url" || handle_error "Failed to add upstream remote"
        show_progress
    fi
fi

# Fetch from upstream
print_step "Fetching from upstream..."
if ! git fetch upstream; then
    handle_error "Failed to fetch from upstream"
fi
show_progress

# Get default branch if dev doesn't exist
DEFAULT_BRANCH=$(get_default_branch)
BASE_BRANCH="dev"
if ! git rev-parse --verify "origin/dev" >/dev/null 2>&1; then
    print_warning "dev branch not found, using default branch: ${BOLD}$DEFAULT_BRANCH${NC}"
    BASE_BRANCH="$DEFAULT_BRANCH"
fi

# Create sync branch
SYNC_BRANCH=$(get_sync_branch_name)
print_step "Creating sync branch: ${BOLD}$SYNC_BRANCH${NC}"

if [ "$DRY_RUN" = false ]; then
    # Create sync branch from base branch
    if ! git checkout -b "$SYNC_BRANCH" "$BASE_BRANCH"; then
        handle_error "Failed to create sync branch"
    fi
    show_progress

    # Create temporary branch for upstream changes
    TEMP_BRANCH="temp-${SYNC_BRANCH}"
    print_step "Creating temporary branch: ${BOLD}$TEMP_BRANCH${NC}"
    if ! git checkout -b "$TEMP_BRANCH" "upstream/$BASE_BRANCH"; then
        handle_error "Failed to create temporary branch"
    fi
    show_progress

    # Switch back to sync branch
    print_step "Switching back to sync branch..."
    if ! git checkout "$SYNC_BRANCH"; then
        handle_error "Failed to switch to sync branch"
    fi
    show_progress

    # Preserve root-level excluded files
    preserve_root_files
fi

# Sync directories
echo -e "\n${BLUE}${BOLD}Syncing directories...${NC}"
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}\n"
for dir in "${SYNC_DIRS[@]}"; do
    sync_directory "$dir" "$TEMP_BRANCH"
done

# Sync files
echo -e "\n${BLUE}${BOLD}Syncing files...${NC}"
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}\n"
for file in "${SYNC_FILES[@]}"; do
    sync_file "$file" "$TEMP_BRANCH"
done

if [ "$DRY_RUN" = false ]; then
    # Restore root-level excluded files
    restore_root_files

    cleanup_temp_dirs
    rm -rf temp_files

    # Cleanup temporary branch
    print_step "Cleaning up temporary branch..."
    git branch -D "$TEMP_BRANCH" || handle_error "Failed to delete temporary branch"
    show_progress

    # Check for changes
    if ! git diff --quiet --exit-code --cached; then
        print_step "Committing changes..."
        git add "${SYNC_DIRS[@]}" "${SYNC_FILES[@]}"
        git commit -m "sync: Update directories and files from upstream

This PR syncs the following items with upstream:
- Directories: ${SYNC_DIRS[*]}
- Files: ${SYNC_FILES[*]}" || handle_error "Failed to commit changes"
        show_progress

        if [ "$FORCE" = false ]; then
            echo -e "\n${YELLOW}${BOLD}Would you like to push the sync branch? (y/n)${NC}"
            read -r response
            if [[ "$response" =~ ^[Yy]$ ]]; then
                print_step "Pushing sync branch..."
                git push -u origin "$SYNC_BRANCH" || handle_error "Failed to push sync branch"
                show_progress
                echo -e "\n${GREEN}${BOLD}✨ Sync branch pushed successfully! ✨${NC}"
                echo -e "${YELLOW}Please create a pull request from branch ${BOLD}$SYNC_BRANCH${NC}${YELLOW} to ${BOLD}$BASE_BRANCH${NC}${YELLOW} in your repository.${NC}\n"
            else
                echo -e "\n${YELLOW}Changes committed but not pushed. You can push later with:${NC}"
                echo -e "${BOLD}git push -u origin $SYNC_BRANCH${NC}"
                echo -e "${YELLOW}Then create a pull request from ${BOLD}$SYNC_BRANCH${NC}${YELLOW} to ${BOLD}$BASE_BRANCH${NC}\n"
            fi
        else
            print_step "Pushing sync branch..."
            git push -u origin "$SYNC_BRANCH" || handle_error "Failed to push sync branch"
            show_progress
            echo -e "\n${GREEN}${BOLD}✨ Sync branch pushed successfully! ✨${NC}"
            echo -e "${YELLOW}Please create a pull request from branch ${BOLD}$SYNC_BRANCH${NC}${YELLOW} to ${BOLD}$BASE_BRANCH${NC}${YELLOW} in your repository.${NC}\n"
        fi
    else
        print_warning "No changes to commit"
        git checkout "$BASE_BRANCH"
        git branch -D "$SYNC_BRANCH"
    fi
else
    echo -e "\n${YELLOW}${BOLD}Dry run completed. No changes were made.${NC}\n"
fi