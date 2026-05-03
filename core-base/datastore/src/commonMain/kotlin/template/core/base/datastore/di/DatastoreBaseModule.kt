/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package template.core.base.datastore.di

import com.russhwolf.settings.Settings
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import template.core.base.datastore.SecureSettingsFactory

/**
 * Platform-specific module that provides [SecureSettingsFactory].
 * Android needs Context; other platforms use no-arg constructors.
 */
expect val datastoreBasePlatformModule: Module

/**
 * Provides two [Settings] instances via Koin named qualifiers:
 * - `named("plain")`: Standard unencrypted settings
 * - `named("secure")`: Encrypted settings backed by platform secure storage
 */
val DatastoreBaseModule = module {
    includes(datastoreBasePlatformModule)
    single<Settings>(named("plain")) { Settings() }
    single<Settings>(named("secure")) { get<SecureSettingsFactory>().create() }
}
