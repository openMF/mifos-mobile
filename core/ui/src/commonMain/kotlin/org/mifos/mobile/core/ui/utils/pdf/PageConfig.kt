/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.ui.utils.pdf

/**
 * Generic page size enum.
 */
enum class PageSize(val widthMm: Int, val heightMm: Int) {
    A4(210, 297),
    LETTER(216, 279),
}

/**
 * Orientation for the page.
 */
enum class Orientation {
    PORTRAIT,
    LANDSCAPE,
}

/**
 * Page configuration used to instruct platform-specific generators.
 */
data class PageConfig(
    val size: PageSize = PageSize.A4,
    val orientation: Orientation = Orientation.PORTRAIT,
    val marginMm: Int = 8,
)
