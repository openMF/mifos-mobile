module FastlaneConfig
  module AndroidConfig
    STORE_CONFIG = {
      default_store_file: ProjectConfig::ANDROID[:keystore][:file],
      default_store_password: ProjectConfig::ANDROID[:keystore][:password],
      default_key_alias: ProjectConfig::ANDROID[:keystore][:key_alias],
      default_key_password: ProjectConfig::ANDROID[:keystore][:key_password]
    }

    FIREBASE_CONFIG = {
      firebase_prod_app_id: ProjectConfig::ANDROID[:firebase][:prod_app_id],
      firebase_demo_app_id: ProjectConfig::ANDROID[:firebase][:demo_app_id],
      firebase_service_creds_file: ProjectConfig.firebase_credentials_file,
      firebase_groups: ProjectConfig::ANDROID[:firebase][:groups]
    }

    BUILD_PATHS = {
      prod_apk_path: ProjectConfig::ANDROID[:apk_paths][:prod],
      demo_apk_path: ProjectConfig::ANDROID[:apk_paths][:demo],
      prod_aab_path: ProjectConfig::ANDROID[:aab_path]
    }
  end
end
