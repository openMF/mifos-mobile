module FastlaneConfig
  module IosConfig
    FIREBASE_CONFIG = {
      firebase_app_id: "1:728434912738:ios:shjhsa78392shja",
      firebase_service_creds_file: "secrets/firebaseAppDistributionServiceCredentialsFile.json",
      firebase_groups: "kmp-project-template-testers"
    }

    BUILD_CONFIG = {
      project_path: "cmp-ios/iosApp.xcodeproj",
      scheme: "iosApp",
      output_directory: "cmp-ios/build"
    }
  end
end