/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package template.core.base.security.di

import org.koin.core.module.Module
import org.koin.dsl.module
import template.core.base.security.BiometricAuthenticator
import template.core.base.security.DeepLinkValidator
import template.core.base.security.FailedAttemptTracker
import template.core.base.security.SecureAuthManager
import template.core.base.security.SecureNavHandler
import template.core.base.security.SecureWiper
import template.core.base.security.SecurityConfig
import template.core.base.security.SecurityPolicy
import template.core.base.security.SessionManager
import template.core.base.security.TamperDetector

/**
 * Zero-config security Koin module. Auto-detects build type via
 * platform-specific [template.core.base.security.isReleaseBuild] and
 * registers all security components with sensible defaults.
 *
 * Consumer apps do NOT need to call this — it is auto-included in
 * [cmp.navigation.di.KoinModules.allModules].
 */
val SecurityModule = module {
    includes(platformSecurityModule)

    single { SecurityConfig() }
    single { SecurityPolicy.default() }
    single { TamperDetector() }
    single { SecureWiper() }
    single { BiometricAuthenticator() }
    single { FailedAttemptTracker(get(), get()) }
    single { SessionManager(get()) }
    single { DeepLinkValidator() }
    single { SecureNavHandler(get()) }
    single { SecureAuthManager(get(), get(), get()) }
}

expect val platformSecurityModule: Module
