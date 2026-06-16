/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package template.core.base.security

/**
 * Desktop release build detection via JVM system property.
 *
 * Desktop release packaging should set `-Dapp.release=true`.
 * Defaults to `false` (debug mode) when the property is absent.
 */
actual fun isReleaseBuild(): Boolean =
    System.getProperty("app.release")?.toBoolean() ?: false
