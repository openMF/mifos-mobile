module FastlaneConfig
  module IosConfig
    FIREBASE_CONFIG = {
      firebase_app_id: ProjectConfig::IOS[:firebase][:app_id],
      firebase_service_creds_file: ProjectConfig.firebase_credentials_file,
      firebase_groups: ProjectConfig::IOS[:firebase][:groups]
    }

    BUILD_CONFIG = {
      project_path: ProjectConfig::IOS[:project_path],
      workspace_path: ProjectConfig::IOS[:workspace_path],
      configuration: "Release",
      podfile_path: "cmp-ios/Podfile",
      plist_path: ProjectConfig::IOS[:plist_path],
      scheme: ProjectConfig::IOS[:scheme],
      output_name: ProjectConfig::IOS[:output_name],
      output_directory: ProjectConfig::IOS[:output_directory],
      match_git_private_key: ProjectConfig::IOS_SHARED[:code_signing][:match_git_private_key],
      target: "iosApp",
      team_id: ProjectConfig::IOS_SHARED[:team_id],
      code_sign_identity: "Apple Distribution",
      match_type: ProjectConfig::IOS_SHARED[:code_signing][:match_type],
      app_identifier: ProjectConfig::IOS[:app_identifier],
      provisioning_profile_name: ProjectConfig::IOS_SHARED[:code_signing][:provisioning_profiles][:adhoc],
      git_url: ProjectConfig::IOS_SHARED[:code_signing][:match_git_url],
      git_branch: ProjectConfig::IOS_SHARED[:code_signing][:match_git_branch],
      key_id: ProjectConfig::IOS_SHARED[:app_store_connect][:key_id],
      issuer_id: ProjectConfig::IOS_SHARED[:app_store_connect][:issuer_id],
      key_filepath: ProjectConfig::IOS_SHARED[:app_store_connect][:key_filepath],
      version_number: ProjectConfig::IOS[:version_number],
      metadata_path: ProjectConfig::IOS[:metadata_path],
      app_rating_config_path: ProjectConfig::IOS[:age_rating_config_path],
      screenshots_ios_path: "./fastlane/screenshots_ios",
      screenshots_macos_path: "./fastlane/screenshots_macos",
      primary_locale: ProjectConfig::IOS[:primary_locale],

      # TestFlight Configuration (from IOS_SHARED)
      TESTFLIGHT_CONFIG = ProjectConfig::IOS_SHARED[:testflight]

      # App Store Configuration (from IOS_SHARED)
      APPSTORE_CONFIG = ProjectConfig::IOS_SHARED[:appstore]
    }
  end
end
