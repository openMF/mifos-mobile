module FastlaneConfig
  module IosConfig
    FIREBASE_CONFIG = {
      firebase_app_id: "1:728434912738:ios:ee2e0815a6915b351a1dbb",
      firebase_service_creds_file: "secrets/firebaseAppDistributionServiceCredentialsFile.json",
      firebase_groups: "mifos-mobile-apps"
    }

    BUILD_CONFIG = {
      project_path: "cmp-ios/iosApp.xcodeproj",
      workspace_path: "cmp-ios/iosApp.xcworkspace",
      configuration: "Release",
      podfile_path: "cmp-ios/Podfile",
      plist_path: "cmp-ios/iosApp/Info.plist",
      scheme: "cmp-ios",
      output_name: "iosApp.ipa",
      output_directory: "cmp-ios/build",
      match_git_private_key: "./secrets/match_ci_key",
      target: "iosApp",
      team_id: "L432S2FZP5",
      code_sign_identity: "Apple Distribution",
      match_type: "adhoc",
      app_identifier: "org.mifos.mobile",
      provisioning_profile_name: "match AdHoc org.mifos.mobile",
      git_url: "git@github.com:openMF/ios-provisioning-profile.git",
      git_branch: "mifos-mobile",
      key_id: "7V3ABCDEFG",
      issuer_id: "7ab9e231-9603-4c3e-a147-be3b0f123456",
      key_filepath: "./secrets/Auth_key.p8",
      version_number: "1.0.0",
      metadata_path: "./fastlane/metadata/ios",
      app_rating_config_path: "./fastlane/age_rating.json",
      screenshots_ios_path: "./fastlane/screenshots_ios",
      screenshots_macos_path: "./fastlane/screenshots_macos",
    }
  end
end
