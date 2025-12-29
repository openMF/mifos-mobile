/**
 * Mifos Mobile Design System Tokens
 * Based on Material Design 3
 */

// ============================================================================
// COLOR TOKENS
// ============================================================================

export const Colors = {
  // Primary
  primary: '#6750A4',
  onPrimary: '#FFFFFF',
  primaryContainer: '#EADDFF',
  onPrimaryContainer: '#21005D',

  // Secondary
  secondary: '#625B71',
  onSecondary: '#FFFFFF',
  secondaryContainer: '#E8DEF8',
  onSecondaryContainer: '#1D192B',

  // Tertiary
  tertiary: '#7D5260',
  onTertiary: '#FFFFFF',
  tertiaryContainer: '#FFD8E4',
  onTertiaryContainer: '#31111D',

  // Error
  error: '#B3261E',
  onError: '#FFFFFF',
  errorContainer: '#F9DEDC',
  onErrorContainer: '#410E0B',

  // Success
  success: '#2E7D32',
  onSuccess: '#FFFFFF',
  successContainer: '#C8E6C9',
  onSuccessContainer: '#1B5E20',

  // Warning
  warning: '#F57C00',
  onWarning: '#FFFFFF',
  warningContainer: '#FFE0B2',
  onWarningContainer: '#E65100',

  // Surface
  surface: '#FFFBFE',
  surfaceDim: '#DED8E1',
  surfaceBright: '#FFFBFE',
  surfaceContainerLowest: '#FFFFFF',
  surfaceContainerLow: '#F7F2FA',
  surfaceContainer: '#F3EDF7',
  surfaceContainerHigh: '#ECE6F0',
  surfaceContainerHighest: '#E6E0E9',
  onSurface: '#1C1B1F',
  onSurfaceVariant: '#49454F',

  // Outline
  outline: '#79747E',
  outlineVariant: '#CAC4D0',

  // Background
  background: '#FFFBFE',
  onBackground: '#1C1B1F',

  // Inverse
  inverseSurface: '#313033',
  inverseOnSurface: '#F4EFF4',
  inversePrimary: '#D0BCFF',

  // Scrim
  scrim: '#000000',
  shadow: '#000000',
} as const

// ============================================================================
// TYPOGRAPHY TOKENS
// ============================================================================

export const Typography = {
  displayLarge: {
    fontFamily: 'Inter',
    fontSize: 57,
    fontWeight: 400,
    lineHeight: 64,
    letterSpacing: -0.25,
  },
  displayMedium: {
    fontFamily: 'Inter',
    fontSize: 45,
    fontWeight: 400,
    lineHeight: 52,
    letterSpacing: 0,
  },
  displaySmall: {
    fontFamily: 'Inter',
    fontSize: 36,
    fontWeight: 400,
    lineHeight: 44,
    letterSpacing: 0,
  },
  headlineLarge: {
    fontFamily: 'Inter',
    fontSize: 32,
    fontWeight: 400,
    lineHeight: 40,
    letterSpacing: 0,
  },
  headlineMedium: {
    fontFamily: 'Inter',
    fontSize: 28,
    fontWeight: 400,
    lineHeight: 36,
    letterSpacing: 0,
  },
  headlineSmall: {
    fontFamily: 'Inter',
    fontSize: 24,
    fontWeight: 400,
    lineHeight: 32,
    letterSpacing: 0,
  },
  titleLarge: {
    fontFamily: 'Inter',
    fontSize: 22,
    fontWeight: 400,
    lineHeight: 28,
    letterSpacing: 0,
  },
  titleMedium: {
    fontFamily: 'Inter',
    fontSize: 16,
    fontWeight: 500,
    lineHeight: 24,
    letterSpacing: 0.15,
  },
  titleSmall: {
    fontFamily: 'Inter',
    fontSize: 14,
    fontWeight: 500,
    lineHeight: 20,
    letterSpacing: 0.1,
  },
  bodyLarge: {
    fontFamily: 'Inter',
    fontSize: 16,
    fontWeight: 400,
    lineHeight: 24,
    letterSpacing: 0.5,
  },
  bodyMedium: {
    fontFamily: 'Inter',
    fontSize: 14,
    fontWeight: 400,
    lineHeight: 20,
    letterSpacing: 0.25,
  },
  bodySmall: {
    fontFamily: 'Inter',
    fontSize: 12,
    fontWeight: 400,
    lineHeight: 16,
    letterSpacing: 0.4,
  },
  labelLarge: {
    fontFamily: 'Inter',
    fontSize: 14,
    fontWeight: 500,
    lineHeight: 20,
    letterSpacing: 0.1,
  },
  labelMedium: {
    fontFamily: 'Inter',
    fontSize: 12,
    fontWeight: 500,
    lineHeight: 16,
    letterSpacing: 0.5,
  },
  labelSmall: {
    fontFamily: 'Inter',
    fontSize: 11,
    fontWeight: 500,
    lineHeight: 16,
    letterSpacing: 0.5,
  },
} as const

// ============================================================================
// SPACING TOKENS
// ============================================================================

export const Spacing = {
  none: 0,
  xs: 4,
  sm: 8,
  md: 16,
  lg: 24,
  xl: 32,
  xxl: 48,
  xxxl: 64,
} as const

// ============================================================================
// RADIUS TOKENS
// ============================================================================

export const Radius = {
  none: 0,
  xs: 4,
  sm: 8,
  md: 12,
  lg: 16,
  xl: 28,
  full: 9999,
} as const

// ============================================================================
// ELEVATION TOKENS
// ============================================================================

export const Elevation = {
  level0: {
    shadowColor: Colors.shadow,
    shadowOffsetX: 0,
    shadowOffsetY: 0,
    shadowBlur: 0,
    shadowSpread: 0,
  },
  level1: {
    shadowColor: Colors.shadow,
    shadowOffsetX: 0,
    shadowOffsetY: 1,
    shadowBlur: 3,
    shadowSpread: 1,
  },
  level2: {
    shadowColor: Colors.shadow,
    shadowOffsetX: 0,
    shadowOffsetY: 2,
    shadowBlur: 6,
    shadowSpread: 2,
  },
  level3: {
    shadowColor: Colors.shadow,
    shadowOffsetX: 0,
    shadowOffsetY: 4,
    shadowBlur: 8,
    shadowSpread: 3,
  },
  level4: {
    shadowColor: Colors.shadow,
    shadowOffsetX: 0,
    shadowOffsetY: 6,
    shadowBlur: 10,
    shadowSpread: 4,
  },
  level5: {
    shadowColor: Colors.shadow,
    shadowOffsetX: 0,
    shadowOffsetY: 8,
    shadowBlur: 12,
    shadowSpread: 6,
  },
} as const

// ============================================================================
// SCREEN DIMENSIONS
// ============================================================================

export const Screens = {
  // iPhone 14 Pro
  mobile: { width: 393, height: 852 },
  // iPhone 14 Pro Max
  mobileLarge: { width: 430, height: 932 },
  // Standard tablet
  tablet: { width: 768, height: 1024 },
} as const

// ============================================================================
// ICON SIZES
// ============================================================================

export const IconSizes = {
  xs: 16,
  sm: 20,
  md: 24,
  lg: 32,
  xl: 48,
} as const

// ============================================================================
// COMPONENT SIZES
// ============================================================================

export const ComponentSizes = {
  topBarHeight: 64,
  bottomNavHeight: 80,
  buttonHeight: 48,
  buttonHeightSmall: 36,
  inputHeight: 56,
  listItemHeight: 72,
  listItemHeightSmall: 56,
  cardMinHeight: 100,
  fabSize: 56,
  fabSizeSmall: 40,
  chipHeight: 32,
  avatarSize: 40,
  avatarSizeLarge: 56,
} as const
