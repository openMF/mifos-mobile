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
import com.russhwolf.settings.StorageSettings

/**
 * Web secure settings using localStorage.
 *
 * Web storage is inherently less secure than native — XSS risk accepted.
 * Full SubtleCrypto wrapper deferred to Phase 4 (T18).
 */
actual class SecureSettingsFactory {
    actual fun create(): Settings {
        return StorageSettings()
    }
}
