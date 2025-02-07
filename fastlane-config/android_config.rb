module FastlaneConfig
  module AndroidConfig
    STORE_CONFIG = {
      default_store_file: "release_keystore.keystore",
      default_store_password: "mifos1234",
      default_key_alias: "mifos-mobile",
      default_key_password: "mifos1234"
    }

    FIREBASE_CONFIG = {
      firebase_prod_app_id: "1:728434912738:android:d853a78f14af0c381a1dbb",
      firebase_demo_app_id: "1:728434912738:android:7845cce9777d9cf11a1dbb",
      firebase_service_creds_file: "secrets/firebaseAppDistributionServiceCredentialsFile.json",
      firebase_groups: "mifos-mobile-testers"
    }

    BUILD_PATHS = {
      prod_apk_path: "cmp-android/build/outputs/apk/prod/release/cmp-android-prod-release.apk",
      demo_apk_path: "cmp-android/build/outputs/apk/demo/release/cmp-android-demo-release.apk",
      prod_aab_path: "cmp-android/build/outputs/bundle/prodRelease/cmp-android-prod-release.aab"
    }
  end
end
