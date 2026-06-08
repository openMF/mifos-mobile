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
 * Platform-specific cryptographically secure random number generator.
 *
 * - Android/Desktop: java.security.SecureRandom
 * - iOS: SecRandomCopyBytes
 * - Web: crypto.getRandomValues
 */
expect class SecureRandom {
    /** Generates [size] cryptographically random bytes. */
    fun nextBytes(size: Int): ByteArray
}
