# ==============================================================================
# Project Configuration - Update these values when setting up a new project
# ==============================================================================
# This is the SINGLE SOURCE OF TRUTH for all project-specific configurations.
# Update these values once, and they will be applied across all fastlane lanes.
# ==============================================================================

module FastlaneConfig
  module ProjectConfig
    # ============================================================================
    # Core Project Information
    # ============================================================================
    PROJECT_NAME = "mifos-mobile"
    ORGANIZATION_NAME = "Mifos Initiative"

    # ============================================================================
    # Android Configuration
    # ============================================================================
    ANDROID = {
      # Package name for Android app
      package_name: "org.mifos.mobile",

      # Play Store credentials file path
      play_store_json_key: "secrets/playStorePublishServiceCredentialsFile.json",

      # Build output paths (relative to project root)
      apk_paths: {
        prod: "cmp-android/build/outputs/apk/prod/release/cmp-android-prod-release.apk",
        demo: "cmp-android/build/outputs/apk/demo/release/cmp-android-demo-release.apk"
      },
      aab_path: "cmp-android/build/outputs/bundle/prodRelease/cmp-android-prod-release.aab",

      # Keystore configuration
      keystore: {
        file: "release_keystore.keystore",
        password: "mifos1234",
        key_alias: "mifos-mobile",
        key_password: "Mifos@123"
      },

      # Firebase App Distribution
      firebase: {
        prod_app_id: "1:728434912738:android:d853a78f14af0c381a1dbb",
        demo_app_id: "1:728434912738:android:7845cce9777d9cf11a1dbb",
        groups: "mifos-mobile-apps"
      }
    }

    # ============================================================================
    # iOS Configuration - App Specific (Change for each app)
    # ============================================================================
    IOS = {
      # Bundle identifier (CHANGE THIS FOR EACH APP)
      app_identifier: "org.mifos.mobile",

      # Firebase App Distribution (CHANGE THIS FOR EACH APP)
      firebase: {
        app_id: "1:728434912738:ios:ee2e0815a6915b351a1dbb",
        groups: "mifos-mobile-apps"
      },

      # Project paths (relative to project root)
      project_path: "cmp-ios/iosApp.xcodeproj",
      workspace_path: "cmp-ios/iosApp.xcworkspace",
      plist_path: "cmp-ios/iosApp/Info.plist",

      # Build configuration
      scheme: "iosApp",
      output_name: "iosApp.ipa",
      output_directory: "cmp-ios/build",

      # App Store metadata paths
      metadata_path: "./fastlane/metadata",
      age_rating_config_path: "./fastlane/age_rating.json",

      # Primary locale — must match App Store Connect primary language
      primary_locale: "en-US",

      # Version configuration (fallback only - actual version read from version.txt)
      # The versionFile gradle task generates version.txt from project.version
      # Fastlane lanes read version.txt to sync iOS version with Android
      version_number: "1.0.0"  # Fallback if version.txt is not available
    }

    # ============================================================================
    # Shared iOS Configuration (SAME FOR ALL APPS - from shared_keys.env)
    # ============================================================================
    IOS_SHARED = {
      # Team and Developer Account (SHARED)
      team_id: ENV['TEAM_ID'] || "L432S2FZP5",

      # CI/CD Configuration
      ci_provider: "circleci", # Options: circleci, travis, jenkins, gitlab_ci, etc.

      # App Store Connect API (SHARED)
      app_store_connect: {
        key_id: ENV['APPSTORE_KEY_ID'] || "ZVQ6W6P822",
        issuer_id: ENV['APPSTORE_ISSUER_ID'] || "7ab9e361-9603-4c3e-b147-be3b0f816099",
        key_filepath: ENV['APPSTORE_KEY_PATH'] || "./secrets/AuthKey.p8"
      },

      # Code Signing & Provisioning (SHARED)
      code_signing: {
        match_type: "adhoc",
        match_git_url: ENV['MATCH_GIT_URL'] || "git@github.com:openMF/ios-provisioning-profile.git",
        match_git_branch: ENV['MATCH_GIT_BRANCH'] || "master",
        match_git_private_key: ENV['MATCH_SSH_KEY_PATH'] || "./secrets/match_ci_key",
        # Provisioning profile names are generated based on app_identifier
        provisioning_profiles: {
          adhoc: "match AdHoc #{IOS[:app_identifier]}",
          appstore: "match AppStore #{IOS[:app_identifier]}"
        }
      },

      # TestFlight Beta Configuration
      testflight: {
        # Beta App Review Information (shown to Apple reviewers)
        beta_app_review_info: {
          contact_email: ENV['TESTFLIGHT_CONTACT_EMAIL'] || "team@mifos.org",
          contact_first_name: ENV['TESTFLIGHT_FIRST_NAME'] || "Mifos",
          contact_last_name: ENV['TESTFLIGHT_LAST_NAME'] || "Initiative",
          contact_phone: ENV['TESTFLIGHT_PHONE'] || "9078675309",
          demo_account_name: ENV['TESTFLIGHT_DEMO_EMAIL'] || "",
          demo_account_password: ENV['TESTFLIGHT_DEMO_PASSWORD'] || "",
          notes: "Thank you for reviewing our app!"
        },

        # Beta App Feedback Configuration
        beta_app_feedback_email: ENV['BETA_FEEDBACK_EMAIL'] || "team@mifos.org",
        beta_app_description: "Mifos - Mobile  Application",

        # Demo account requirement
        demo_account_required: false,

        # Distribution settings
        distribute_external: true, # Distribute to external testers
        notify_external_testers: true, # Notify external testers when build is available

        # Tester Groups (Required when distribute_external is true)
        groups: ENV['TESTFLIGHT_GROUPS']&.split(',') || ["mifos-mobile-apps"],

        # Submission settings
        skip_submission: false, # Submit the build for distribution
        skip_waiting_for_build_processing: true, # Don't wait for full processing (saves CI minutes)
        submit_beta_review: true, # Submit for beta review automatically

        # Build management
        expire_previous_builds: false, # Don't expire previous builds automatically
        reject_build_waiting_for_review: false, # Don't reject builds waiting for review

        # Processing wait settings (if skip_waiting_for_build_processing is false)
        wait_processing_interval: 30, # Check every 30 seconds
        wait_processing_timeout_duration: 3600, # Timeout after 1 hour (3600 seconds)

        # Encryption compliance
        uses_non_exempt_encryption: false, # Set to true if app uses encryption

        # Localized App Information (for TestFlight)
        localized_app_info: {
          "default" => {
            feedback_email: ENV['BETA_FEEDBACK_EMAIL'] || "team@mifos.org",
            marketing_url: ENV['APP_MARKETING_URL'] || "https://mifos.org",
            privacy_policy_url: ENV['APP_PRIVACY_URL'] || "https://mifos.org/privacy",
            description: "Mifos - Mobile Application"
          },
          "en-US" => {
            feedback_email: ENV['BETA_FEEDBACK_EMAIL'] || "team@mifos.org",
            marketing_url: ENV['APP_MARKETING_URL'] || "https://mifos.org",
            privacy_policy_url: ENV['APP_PRIVACY_URL'] || "https://mifos.org/privacy",
            description: "Mifos - Mobile Application"
          }
        }
      },

      # App Store Release Configuration
      appstore: {
        # App Review Information (shown to Apple reviewers during App Store review)
        app_review_information: {
          first_name: ENV['APPSTORE_REVIEW_FIRST_NAME'] || "Mifos",
          last_name: ENV['APPSTORE_REVIEW_LAST_NAME'] || "Initiative",
          phone_number: ENV['APPSTORE_REVIEW_PHONE'] || "9078675309",
          email_address: ENV['APPSTORE_REVIEW_EMAIL'] || "team@mifos.org",
          demo_user: ENV['APPSTORE_DEMO_EMAIL'] || "",
          demo_password: ENV['APPSTORE_DEMO_PASSWORD'] || "",
          notes: "Thank you for reviewing our app!"
        },

        # Submission settings
        submit_for_review: true, # Automatically submit for review after upload
        automatic_release: true, # Automatically release after approval
        phased_release: false, # Phased release over 7 days (set to true if desired)

        # Version and build management
        skip_app_version_update: false, # Let Fastlane create versions in App Store Connect
        reject_if_possible: true, # Reject previous submission if possible

        # Processing settings
        force: true, # Skip HTML file verification
        precheck_include_in_app_purchases: false, # Skip in-app purchase precheck
        run_precheck_before_submit: true, # Run precheck validation before submitting

        # Submission information (compliance, privacy, ads, etc.)
        submission_information: {
          # Advertising Identifier (IDFA) usage
          add_id_info_uses_idfa: false,
          add_id_info_limits_tracking: false,
          add_id_info_serves_ads: false,
          add_id_info_tracks_action: false,
          add_id_info_tracks_install: false,

          # Content rights
          content_rights_has_rights: true,
          content_rights_contains_third_party_content: false,

          # Export compliance (encryption)
          export_compliance_platform: 'ios',
          export_compliance_compliance_required: false,
          export_compliance_encryption_updated: false,
          export_compliance_app_type: nil,
          export_compliance_uses_encryption: false,
          export_compliance_is_exempt: true,
          export_compliance_contains_third_party_cryptography: false,
          export_compliance_contains_proprietary_cryptography: false,
          export_compliance_available_on_french_store: true
        }
      }
    }

    # ============================================================================
    # Shared Configuration (Both Android & iOS)
    # ============================================================================
    SHARED = {
      # Firebase service credentials (used by both platforms)
      firebase_service_credentials: "secrets/firebaseAppDistributionServiceCredentialsFile.json"
    }

    # ============================================================================
    # Helper Methods
    # ============================================================================

    # Get Android package name
    def self.android_package_name
      ANDROID[:package_name]
    end

    # Get iOS bundle identifier
    def self.ios_bundle_identifier
      IOS[:app_identifier]
    end

    # Get Firebase credentials file
    def self.firebase_credentials_file
      SHARED[:firebase_service_credentials]
    end

    # Get merged iOS config (app-specific + shared)
    def self.ios_config
      IOS.merge(IOS_SHARED)
    end

    # Validate that all required files exist
    def self.validate_config
      required_files = [
        SHARED[:firebase_service_credentials],
        IOS_SHARED[:app_store_connect][:key_filepath],
        IOS_SHARED[:code_signing][:match_git_private_key]
      ]

      # Add Android files only if running Android lanes
      if ENV['FASTLANE_PLATFORM_NAME'] == 'android'
        required_files << ANDROID[:play_store_json_key]
      end

      missing_files = required_files.reject { |file| File.exist?(File.join(Dir.pwd, '..', file)) }

      unless missing_files.empty?
        UI.important("⚠️  Warning: The following required files are missing:")
        missing_files.each { |file| UI.important("   - #{file}") }
        UI.important("\nPlease ensure these files are in place before running deployment lanes.")
      end
    end

    # Print configuration summary
    def self.print_config_summary
      UI.header "📋 Configuration Summary"
      UI.message "Project: #{PROJECT_NAME}"
      UI.message "Organization: #{ORGANIZATION_NAME}"
      UI.message ""
      UI.message "iOS Bundle ID: #{IOS[:app_identifier]}"
      UI.message "iOS Team ID: #{IOS_SHARED[:team_id]}"
      UI.message "Firebase iOS App: #{IOS[:firebase][:app_id]}"
      UI.message "Match Repo: #{IOS_SHARED[:code_signing][:match_git_url]}"
      UI.message ""
      UI.message "Android Package: #{ANDROID[:package_name]}"
      UI.message "Firebase Android App (Prod): #{ANDROID[:firebase][:prod_app_id]}"
      UI.message ""
    end
  end
end