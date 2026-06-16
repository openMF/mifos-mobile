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
 * Central configuration for all security behavior. Controls debug/release gates
 * and configurable policy thresholds.
 *
 * Created per-platform in each app module and passed to [securityModule] as a
 * function parameter to avoid Koin init-order issues.
 *
 * @param isReleaseBuild Single boolean controlling all conditional hardening.
 * @param maxFailedAttempts Number of failed passcode attempts before data wipe.
 * @param sessionTimeoutMinutes Inactivity timeout before app locks.
 * @param clipboardWipeSeconds Delay before clipboard auto-clears in release.
 */
data class SecurityConfig(
    val isReleaseBuild: Boolean = isReleaseBuild(),
    val maxFailedAttempts: Int = 10,
    val sessionTimeoutMinutes: Int = 30,
    val clipboardWipeSeconds: Int = 60,
)
