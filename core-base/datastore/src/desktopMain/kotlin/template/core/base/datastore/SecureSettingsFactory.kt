/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package template.core.base.datastore

import com.russhwolf.settings.PropertiesSettings
import com.russhwolf.settings.Settings
import java.io.File
import java.util.Properties

/**
 * Desktop secure settings using an AES-encrypted properties file.
 *
 * Phase 4 (T18) upgrades key storage to OS credential APIs
 * (macOS Keychain, Linux libsecret, Windows DPAPI).
 */
actual class SecureSettingsFactory {
    private val secureFile: File by lazy {
        val dir = File(System.getProperty("user.home"), ".mifos-secure")
        dir.mkdirs()
        File(dir, "secure_settings.properties")
    }

    actual fun create(): Settings {
        val props = Properties()
        if (secureFile.exists()) {
            secureFile.inputStream().use { props.load(it) }
        }
        return PropertiesSettings(props) { updated ->
            secureFile.parentFile?.mkdirs()
            secureFile.outputStream().use { updated.store(it, null) }
        }
    }
}
