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

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.mifos.mobile.core.model.MifosBrandTheme
import template.core.base.designsystem.theme.KptElevationImpl
import template.core.base.designsystem.theme.KptShapesImpl

/**
 * Semantic color schemes for the selectable visual identities.
 *
 * Ipoteka colors follow the public Ipoteka Bank OTP Group brand guide. Aurora and Graphite are
 * product-neutral themes. Screens must consume Material/Kpt semantic tokens instead of these
 * palette values directly.
 */
fun brandColorScheme(
    brand: MifosBrandTheme,
    darkTheme: Boolean,
): ColorScheme = when (brand) {
    MifosBrandTheme.IPOTEKA -> if (darkTheme) ipotekaDarkScheme else ipotekaLightScheme
    MifosBrandTheme.AURORA -> if (darkTheme) auroraDarkScheme else auroraLightScheme
    MifosBrandTheme.GRAPHITE -> if (darkTheme) graphiteDarkScheme else graphiteLightScheme
}

internal fun brandShapes(brand: MifosBrandTheme): KptShapesImpl = when (brand) {
    MifosBrandTheme.IPOTEKA -> KptShapesImpl(
        small = RoundedCornerShape(10.dp),
        medium = RoundedCornerShape(16.dp),
        large = RoundedCornerShape(22.dp),
        extraLarge = RoundedCornerShape(28.dp),
    )
    MifosBrandTheme.AURORA -> KptShapesImpl(
        small = RoundedCornerShape(12.dp),
        medium = RoundedCornerShape(20.dp),
        large = RoundedCornerShape(28.dp),
        extraLarge = RoundedCornerShape(36.dp),
    )
    MifosBrandTheme.GRAPHITE -> KptShapesImpl(
        small = RoundedCornerShape(8.dp),
        medium = RoundedCornerShape(12.dp),
        large = RoundedCornerShape(18.dp),
        extraLarge = RoundedCornerShape(24.dp),
    )
}

internal fun brandElevation(brand: MifosBrandTheme): KptElevationImpl = when (brand) {
    MifosBrandTheme.IPOTEKA -> KptElevationImpl(level1 = 1.dp, level2 = 2.dp, level3 = 4.dp)
    MifosBrandTheme.AURORA -> KptElevationImpl(level1 = 2.dp, level2 = 4.dp, level3 = 8.dp)
    MifosBrandTheme.GRAPHITE -> KptElevationImpl(level1 = 0.dp, level2 = 1.dp, level3 = 2.dp)
}

private val ipotekaLightScheme = lightScheme.copy(
    primary = Color(0xFF52AE30),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDDF3D5),
    onPrimaryContainer = Color(0xFF123A08),
    secondary = Color(0xFF006648),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFC4ECD9),
    onSecondaryContainer = Color(0xFF003828),
    tertiary = Color(0xFF0F87D2),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFD5ECFA),
    onTertiaryContainer = Color(0xFF00344F),
    background = Color(0xFFF6F7FA),
    onBackground = Color(0xFF151617),
    surface = Color.White,
    onSurface = Color(0xFF151617),
    surfaceVariant = Color(0xFFECEFF6),
    onSurfaceVariant = Color(0xFF555B64),
    outline = Color(0xFF7D828B),
    outlineVariant = Color(0xFFD0D8E8),
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Color(0xFFF6F7FA),
    surfaceContainer = Color(0xFFECEFF6),
    surfaceContainerHigh = Color(0xFFE3E8F1),
    surfaceContainerHighest = Color(0xFFD0D8E8),
    inversePrimary = Color(0xFF8ED873),
)

private val ipotekaDarkScheme = darkScheme.copy(
    primary = Color(0xFF8ED873),
    onPrimary = Color(0xFF103A05),
    primaryContainer = Color(0xFF285F19),
    onPrimaryContainer = Color(0xFFDDF3D5),
    secondary = Color(0xFF72D6B2),
    onSecondary = Color(0xFF003828),
    secondaryContainer = Color(0xFF00513A),
    onSecondaryContainer = Color(0xFFC4ECD9),
    tertiary = Color(0xFF79C7F5),
    onTertiary = Color(0xFF00344F),
    tertiaryContainer = Color(0xFF075F91),
    onTertiaryContainer = Color(0xFFD5ECFA),
    background = Color(0xFF101512),
    onBackground = Color(0xFFE9EEE9),
    surface = Color(0xFF151B17),
    onSurface = Color(0xFFE9EEE9),
    surfaceVariant = Color(0xFF273029),
    onSurfaceVariant = Color(0xFFC1C9C1),
    outline = Color(0xFF89938A),
    outlineVariant = Color(0xFF3B463D),
    surfaceContainerLowest = Color(0xFF0B100D),
    surfaceContainerLow = Color(0xFF151B17),
    surfaceContainer = Color(0xFF1A211C),
    surfaceContainerHigh = Color(0xFF222A24),
    surfaceContainerHighest = Color(0xFF2B342D),
    inversePrimary = Color(0xFF52AE30),
)

private val auroraLightScheme = lightScheme.copy(
    primary = Color(0xFF5B5CE2),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE4E3FF),
    onPrimaryContainer = Color(0xFF24246D),
    secondary = Color(0xFF008E9B),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFC5F1F3),
    onSecondaryContainer = Color(0xFF003F45),
    tertiary = Color(0xFFE4578C),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFD9E5),
    onTertiaryContainer = Color(0xFF641B39),
    background = Color(0xFFF8F7FC),
    onBackground = Color(0xFF1B1B23),
    surface = Color(0xFFFFFBFF),
    onSurface = Color(0xFF1B1B23),
    surfaceVariant = Color(0xFFECEAF4),
    onSurfaceVariant = Color(0xFF5C5A67),
    outline = Color(0xFF797784),
    outlineVariant = Color(0xFFD0CEDA),
    surfaceContainerLow = Color(0xFFF5F2FA),
    surfaceContainer = Color(0xFFEFECF5),
    surfaceContainerHigh = Color(0xFFE9E6EF),
    surfaceContainerHighest = Color(0xFFE3E0E9),
    inversePrimary = Color(0xFFC4C2FF),
)

private val auroraDarkScheme = darkScheme.copy(
    primary = Color(0xFFC4C2FF),
    onPrimary = Color(0xFF292877),
    primaryContainer = Color(0xFF4141A9),
    onPrimaryContainer = Color(0xFFE4E3FF),
    secondary = Color(0xFF75DDE4),
    onSecondary = Color(0xFF003F45),
    secondaryContainer = Color(0xFF006971),
    onSecondaryContainer = Color(0xFFC5F1F3),
    tertiary = Color(0xFFFFB0C9),
    onTertiary = Color(0xFF641B39),
    tertiaryContainer = Color(0xFF91385C),
    onTertiaryContainer = Color(0xFFFFD9E5),
    background = Color(0xFF12121A),
    onBackground = Color(0xFFE7E4ED),
    surface = Color(0xFF181821),
    onSurface = Color(0xFFE7E4ED),
    surfaceVariant = Color(0xFF2B2A35),
    onSurfaceVariant = Color(0xFFC9C6D2),
    outline = Color(0xFF928F9D),
    outlineVariant = Color(0xFF45434F),
    surfaceContainerLow = Color(0xFF1C1C25),
    surfaceContainer = Color(0xFF202029),
    surfaceContainerHigh = Color(0xFF2A2933),
    surfaceContainerHighest = Color(0xFF35343E),
    inversePrimary = Color(0xFF5B5CE2),
)

private val graphiteLightScheme = lightScheme.copy(
    primary = Color(0xFF176B5B),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFBCEBDD),
    onPrimaryContainer = Color(0xFF073C33),
    secondary = Color(0xFF485769),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFDCE5F1),
    onSecondaryContainer = Color(0xFF253444),
    tertiary = Color(0xFFB44B12),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFDBC9),
    onTertiaryContainer = Color(0xFF572000),
    background = Color(0xFFF4F6F7),
    onBackground = Color(0xFF171A1C),
    surface = Color(0xFFFCFCFD),
    onSurface = Color(0xFF171A1C),
    surfaceVariant = Color(0xFFE4E8EA),
    onSurfaceVariant = Color(0xFF52585C),
    outline = Color(0xFF737A7E),
    outlineVariant = Color(0xFFC5CBCE),
    surfaceContainerLow = Color(0xFFF0F2F3),
    surfaceContainer = Color(0xFFEAECED),
    surfaceContainerHigh = Color(0xFFE3E6E7),
    surfaceContainerHighest = Color(0xFFDDE0E1),
    inversePrimary = Color(0xFF83D5C1),
)

private val graphiteDarkScheme = darkScheme.copy(
    primary = Color(0xFF83D5C1),
    onPrimary = Color(0xFF00382F),
    primaryContainer = Color(0xFF075445),
    onPrimaryContainer = Color(0xFFBCEBDD),
    secondary = Color(0xFFBCC8D8),
    onSecondary = Color(0xFF263544),
    secondaryContainer = Color(0xFF3C4B5C),
    onSecondaryContainer = Color(0xFFDCE5F1),
    tertiary = Color(0xFFFFB690),
    onTertiary = Color(0xFF572000),
    tertiaryContainer = Color(0xFF813407),
    onTertiaryContainer = Color(0xFFFFDBC9),
    background = Color(0xFF0E1113),
    onBackground = Color(0xFFE2E5E7),
    surface = Color(0xFF14181A),
    onSurface = Color(0xFFE2E5E7),
    surfaceVariant = Color(0xFF262C2F),
    onSurfaceVariant = Color(0xFFC3C8CB),
    outline = Color(0xFF8D9498),
    outlineVariant = Color(0xFF3F4649),
    surfaceContainerLowest = Color(0xFF090C0E),
    surfaceContainerLow = Color(0xFF161A1C),
    surfaceContainer = Color(0xFF1B1F21),
    surfaceContainerHigh = Color(0xFF252A2C),
    surfaceContainerHighest = Color(0xFF303537),
    inversePrimary = Color(0xFF176B5B),
)
