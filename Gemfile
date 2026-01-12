source "https://rubygems.org"

ruby '3.3.6'

# Add compatibility gems for Ruby 3.3+
gem "abbrev"
gem "base64"
gem "mutex_m"
gem "bigdecimal"

gem "fastlane"

plugins_path = File.join(File.dirname(__FILE__), 'fastlane', 'Pluginfile')
eval_gemfile(plugins_path) if File.exist?(plugins_path)
