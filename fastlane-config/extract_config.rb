#!/usr/bin/env ruby
# ==============================================================================
# iOS Configuration Extraction Script for GitHub Actions
# ==============================================================================
# This script extracts iOS configuration from project_config.rb and outputs
# it as JSON for consumption by GitHub Actions workflows.
#
# Usage:
#   ruby fastlane-config/extract_config.rb
#
# Output:
#   JSON object with iOS configuration values
# ==============================================================================

require 'json'
require_relative 'project_config'

begin
  # Extract iOS configuration from project_config.rb
  config = {
    # App-specific configuration (from IOS)
    app_identifier: FastlaneConfig::ProjectConfig::IOS[:app_identifier],
    firebase_app_id: FastlaneConfig::ProjectConfig::IOS[:firebase][:app_id],
    firebase_groups: FastlaneConfig::ProjectConfig::IOS[:firebase][:groups],
    metadata_path: FastlaneConfig::ProjectConfig::IOS[:metadata_path],
    version_number: FastlaneConfig::ProjectConfig::IOS[:version_number],

    # Shared configuration (from IOS_SHARED)
    team_id: FastlaneConfig::ProjectConfig::IOS_SHARED[:team_id],
    match_git_url: FastlaneConfig::ProjectConfig::IOS_SHARED[:code_signing][:match_git_url],
    match_git_branch: FastlaneConfig::ProjectConfig::IOS_SHARED[:code_signing][:match_git_branch],
    match_type: FastlaneConfig::ProjectConfig::IOS_SHARED[:code_signing][:match_type],

    # Dynamically computed provisioning profiles
    provisioning_profile_adhoc: FastlaneConfig::ProjectConfig::IOS_SHARED[:code_signing][:provisioning_profiles][:adhoc],
    provisioning_profile_appstore: FastlaneConfig::ProjectConfig::IOS_SHARED[:code_signing][:provisioning_profiles][:appstore]
  }

  # Output as pretty-printed JSON
  puts JSON.pretty_generate(config)

  # Exit successfully
  exit 0

rescue => e
  # Error handling
  STDERR.puts "Error extracting iOS configuration:"
  STDERR.puts "  #{e.class}: #{e.message}"
  STDERR.puts
  STDERR.puts "Stack trace:"
  e.backtrace.each { |line| STDERR.puts "  #{line}" }
  STDERR.puts
  STDERR.puts "Please ensure:"
  STDERR.puts "  1. fastlane-config/project_config.rb exists and is valid Ruby"
  STDERR.puts "  2. Both IOS and IOS_SHARED configurations are defined"
  STDERR.puts "  3. All required keys are present in the configuration"

  # Exit with error
  exit 1
end