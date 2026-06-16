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
 * Configuration for TLS certificate pinning per hostname.
 *
 * Consumer apps must configure pins for their API domains.
 * The template provides the infrastructure; [default] returns an empty
 * config (no pinning) so the template compiles without domain-specific pins.
 *
 * @param pins Map of hostname patterns to SHA-256 pin hashes.
 */
data class CertificatePinConfig(
    val pins: Map<String, List<String>> = emptyMap(),
) {
    companion object {
        /** No-op default — consumer apps override with their domain pins. */
        fun default(): CertificatePinConfig = CertificatePinConfig()
    }
}
