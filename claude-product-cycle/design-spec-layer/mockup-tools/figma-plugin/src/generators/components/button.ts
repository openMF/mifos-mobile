/**
 * Button Component Generator
 * Creates Material Design 3 buttons
 */

import { Colors, Typography, Spacing, Radius, ComponentSizes } from '../../design-system/tokens'
import { solidPaint, setAutoLayout, createText, applyElevation, createIconPlaceholder } from '../../utils/helpers'

export type ButtonVariant = 'filled' | 'outlined' | 'text' | 'elevated' | 'tonal'
export type ButtonSize = 'small' | 'medium' | 'large'

export interface ButtonOptions {
  label: string
  variant?: ButtonVariant
  size?: ButtonSize
  icon?: string
  iconPosition?: 'left' | 'right'
  fullWidth?: boolean
  disabled?: boolean
}

/**
 * Create a button
 */
export async function createButton(options: ButtonOptions): Promise<FrameNode> {
  const {
    label,
    variant = 'filled',
    size = 'medium',
    icon,
    iconPosition = 'left',
    fullWidth = false,
    disabled = false,
  } = options

  const button = figma.createFrame()
  button.name = `Button-${variant}-${label}`

  // Size configuration
  const sizeConfig = {
    small: { height: 36, padding: 16, iconSize: 18, typography: Typography.labelMedium },
    medium: { height: 48, padding: 24, iconSize: 20, typography: Typography.labelLarge },
    large: { height: 56, padding: 32, iconSize: 24, typography: Typography.labelLarge },
  }

  const config = sizeConfig[size]
  button.resize(100, config.height)
  button.cornerRadius = config.height / 2

  if (fullWidth) {
    button.layoutSizingHorizontal = 'FILL'
  }

  setAutoLayout(button, {
    direction: 'HORIZONTAL',
    padding: { top: 0, right: config.padding, bottom: 0, left: config.padding },
    gap: 8,
    alignment: 'CENTER',
    crossAlignment: 'CENTER',
  })

  // Variant styling
  let textColor: string
  let bgColor: string | null = null
  let strokeColor: string | null = null

  switch (variant) {
    case 'filled':
      bgColor = disabled ? Colors.onSurface : Colors.primary
      textColor = disabled ? Colors.surface : Colors.onPrimary
      button.opacity = disabled ? 0.12 : 1
      break
    case 'outlined':
      strokeColor = disabled ? Colors.onSurface : Colors.outline
      textColor = disabled ? Colors.onSurface : Colors.primary
      button.opacity = disabled ? 0.12 : 1
      break
    case 'text':
      textColor = disabled ? Colors.onSurface : Colors.primary
      button.opacity = disabled ? 0.38 : 1
      break
    case 'elevated':
      bgColor = Colors.surfaceContainerLow
      textColor = disabled ? Colors.onSurface : Colors.primary
      if (!disabled) applyElevation(button, 1)
      button.opacity = disabled ? 0.12 : 1
      break
    case 'tonal':
      bgColor = disabled ? Colors.onSurface : Colors.secondaryContainer
      textColor = disabled ? Colors.surface : Colors.onSecondaryContainer
      button.opacity = disabled ? 0.12 : 1
      break
    default:
      textColor = Colors.onPrimary
  }

  if (bgColor) {
    button.fills = [solidPaint(bgColor)]
  } else {
    button.fills = []
  }

  if (strokeColor) {
    button.strokes = [solidPaint(strokeColor)]
    button.strokeWeight = 1
  }

  // Icon (left)
  if (icon && iconPosition === 'left') {
    const iconNode = createIconPlaceholder(icon.charAt(0), config.iconSize, 'transparent', textColor)
    button.appendChild(iconNode)
  }

  // Label
  const labelText = await createText(label, {
    ...config.typography,
    color: textColor,
  })
  button.appendChild(labelText)

  // Icon (right)
  if (icon && iconPosition === 'right') {
    const iconNode = createIconPlaceholder(icon.charAt(0), config.iconSize, 'transparent', textColor)
    button.appendChild(iconNode)
  }

  return button
}

/**
 * Create a FAB (Floating Action Button)
 */
export async function createFab(options: {
  icon: string
  label?: string
  variant?: 'primary' | 'secondary' | 'tertiary' | 'surface'
  size?: 'small' | 'medium' | 'large'
}): Promise<FrameNode> {
  const {
    icon,
    label,
    variant = 'primary',
    size = 'medium',
  } = options

  const fab = figma.createFrame()
  fab.name = label ? 'ExtendedFAB' : 'FAB'

  const sizeConfig = {
    small: { size: 40, iconSize: 24, radius: 12 },
    medium: { size: 56, iconSize: 24, radius: 16 },
    large: { size: 96, iconSize: 36, radius: 28 },
  }

  const config = sizeConfig[size]

  // Colors based on variant
  const variantColors = {
    primary: { bg: Colors.primaryContainer, fg: Colors.onPrimaryContainer },
    secondary: { bg: Colors.secondaryContainer, fg: Colors.onSecondaryContainer },
    tertiary: { bg: Colors.tertiaryContainer, fg: Colors.onTertiaryContainer },
    surface: { bg: Colors.surfaceContainerHigh, fg: Colors.primary },
  }

  const colors = variantColors[variant]

  if (label) {
    // Extended FAB
    fab.resize(100, config.size)
    fab.cornerRadius = config.radius
    fab.fills = [solidPaint(colors.bg)]
    applyElevation(fab, 3)

    setAutoLayout(fab, {
      direction: 'HORIZONTAL',
      padding: { top: 16, right: 20, bottom: 16, left: 16 },
      gap: 12,
      crossAlignment: 'CENTER',
    })

    const iconNode = createIconPlaceholder(icon.charAt(0), config.iconSize, 'transparent', colors.fg)
    fab.appendChild(iconNode)

    const labelText = await createText(label, {
      ...Typography.labelLarge,
      color: colors.fg,
    })
    fab.appendChild(labelText)
  } else {
    // Regular FAB
    fab.resize(config.size, config.size)
    fab.cornerRadius = config.radius
    fab.fills = [solidPaint(colors.bg)]
    applyElevation(fab, 3)

    setAutoLayout(fab, {
      direction: 'HORIZONTAL',
      alignment: 'CENTER',
      crossAlignment: 'CENTER',
    })

    const iconNode = createIconPlaceholder(icon.charAt(0), config.iconSize, 'transparent', colors.fg)
    fab.appendChild(iconNode)
  }

  return fab
}

/**
 * Create an icon button
 */
export function createIconButton(options: {
  icon: string
  variant?: 'standard' | 'filled' | 'tonal' | 'outlined'
  selected?: boolean
  disabled?: boolean
}): FrameNode {
  const {
    icon,
    variant = 'standard',
    selected = false,
    disabled = false,
  } = options

  const button = figma.createFrame()
  button.name = `IconButton-${icon}`
  button.resize(48, 48)
  button.cornerRadius = 24

  setAutoLayout(button, {
    direction: 'HORIZONTAL',
    alignment: 'CENTER',
    crossAlignment: 'CENTER',
  })

  let bgColor: string | null = null
  let iconColor: string

  switch (variant) {
    case 'filled':
      bgColor = selected ? Colors.primary : Colors.surfaceContainerHighest
      iconColor = selected ? Colors.onPrimary : Colors.primary
      break
    case 'tonal':
      bgColor = selected ? Colors.secondaryContainer : Colors.surfaceContainerHighest
      iconColor = selected ? Colors.onSecondaryContainer : Colors.onSurfaceVariant
      break
    case 'outlined':
      button.strokes = [solidPaint(Colors.outline)]
      button.strokeWeight = 1
      iconColor = selected ? Colors.inverseSurface : Colors.onSurfaceVariant
      if (selected) bgColor = Colors.inverseSurface
      break
    default: // standard
      iconColor = selected ? Colors.primary : Colors.onSurfaceVariant
      break
  }

  if (bgColor) {
    button.fills = [solidPaint(bgColor)]
  } else {
    button.fills = []
  }

  if (disabled) {
    button.opacity = 0.38
  }

  const iconNode = createIconPlaceholder(icon.charAt(0), 24, 'transparent', iconColor)
  button.appendChild(iconNode)

  return button
}
