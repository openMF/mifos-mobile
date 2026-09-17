/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.model

/**
 * Stable visual identity selected independently from the light/dark mode.
 *
 * Screens consume semantic theme tokens and must never branch on these values.
 */
enum class MifosBrandTheme {
    IPOTEKA,
    AURORA,
    GRAPHITE,
}
