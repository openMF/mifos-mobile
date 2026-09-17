/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.designsystem.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import org.mifos.mobile.core.model.MifosBrandTheme
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BrandThemesTest {

    @Test
    fun `ipoteka theme uses official primary brand colors`() {
        val scheme = brandColorScheme(MifosBrandTheme.IPOTEKA, darkTheme = false)

        assertEquals(Color(0xFF52AE30), scheme.primary)
        assertEquals(Color(0xFF006648), scheme.secondary)
    }

    @Test
    fun `every brand has distinct light and dark semantic schemes`() {
        MifosBrandTheme.entries.forEach { brand ->
            val light = brandColorScheme(brand, darkTheme = false)
            val dark = brandColorScheme(brand, darkTheme = true)

            assertTrue(light.background.luminance() > dark.background.luminance())
            assertTrue(light.surface.luminance() > dark.surface.luminance())
            assertTrue(light.primary != dark.primary)
        }

        val lightPrimaries = MifosBrandTheme.entries
            .map { brandColorScheme(it, darkTheme = false).primary }
            .toSet()
        assertEquals(MifosBrandTheme.entries.size, lightPrimaries.size)
    }
}
