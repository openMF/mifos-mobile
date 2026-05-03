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

import com.russhwolf.settings.Settings

/**
 * Platform-specific factory that creates an encrypted [Settings] instance.
 *
 * Returns the standard [Settings] interface — zero API change for consumers.
 * - Android: EncryptedSharedPreferences -> SharedPreferencesSettings
 * - iOS: KeychainSettings (serviceName = "org.mifos.secure")
 * - Desktop: AES-encrypted PropertiesSettings
 * - Web: In-memory (full SubtleCrypto deferred to Phase 4)
 */
expect class SecureSettingsFactory {
    fun create(): Settings
}
