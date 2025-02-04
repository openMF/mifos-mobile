require_relative '../../fastlane-config/android_config'
require_relative '../../fastlane-config/ios_config'

module FastlaneConfig
  # Move methods directly into FastlaneConfig module instead of nested Helpers module
  def self.get_android_signing_config(options = {})
    {
      storeFile: options[:store_file] || ENV['ANDROID_STORE_FILE'] || AndroidConfig::STORE_CONFIG[:default_store_file],
      storePassword: options[:store_password] || ENV['ANDROID_STORE_PASSWORD'] || AndroidConfig::STORE_CONFIG[:default_store_password],
      keyAlias: options[:key_alias] || ENV['ANDROID_KEY_ALIAS'] || AndroidConfig::STORE_CONFIG[:default_key_alias],
      keyPassword: options[:key_password] || ENV['ANDROID_KEY_PASSWORD'] || AndroidConfig::STORE_CONFIG[:default_key_password]
    }
  end

  def self.get_firebase_config(platform, type = :prod)
    case platform
    when :android
      app_id = type == :prod ? AndroidConfig::FIREBASE_CONFIG[:firebase_prod_app_id] : AndroidConfig::FIREBASE_CONFIG[:firebase_demo_app_id]
      {
        appId: ENV['FIREBASE_ANDROID_APP_ID'] || app_id,
        serviceCredsFile: ENV['FIREBASE_SERVICE_CREDS_FILE'] || AndroidConfig::FIREBASE_CONFIG[:firebase_service_creds_file],
        groups: ENV['FIREBASE_GROUPS'] || AndroidConfig::FIREBASE_CONFIG[:firebase_groups]
      }
    when :ios
      {
        appId: ENV['FIREBASE_IOS_APP_ID'] || IosConfig::FIREBASE_CONFIG[:firebase_app_id],
        serviceCredsFile: ENV['FIREBASE_SERVICE_CREDS_FILE'] || IosConfig::FIREBASE_CONFIG[:firebase_service_creds_file],
        groups: ENV['FIREBASE_GROUPS'] || IosConfig::FIREBASE_CONFIG[:firebase_groups]
      }
    end
  end
end