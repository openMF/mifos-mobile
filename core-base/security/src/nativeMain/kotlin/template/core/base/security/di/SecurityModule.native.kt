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
import template.core.base.security.CertificatePinConfig
import template.core.base.security.FieldEncryptor
import template.core.base.security.SecureKeyProvider
import template.core.base.security.SecureRandom

actual val platformSecurityModule: Module = module {
    single { SecureKeyProvider() }
    single { FieldEncryptor(get()) }
    single { SecureRandom() }
    single { CertificatePinConfig.default() }
}
