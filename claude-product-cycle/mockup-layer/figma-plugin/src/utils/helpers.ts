/**
 * Utility functions for Figma plugin
 */

// ============================================================================
// COLOR UTILITIES
// ============================================================================

/**
 * Convert hex color to Figma RGB (0-1 range)
 */
export function hexToRgb(hex: string): RGB {
  const result = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(hex)
  if (!result) {
    return { r: 0, g: 0, b: 0 }
  }
  return {
    r: parseInt(result[1], 16) / 255,
    g: parseInt(result[2], 16) / 255,
    b: parseInt(result[3], 16) / 255,
  }
}

/**
 * Convert hex color with alpha to Figma RGBA
 */
export function hexToRgba(hex: string, alpha: number = 1): RGBA {
  const rgb = hexToRgb(hex)
  return { ...rgb, a: alpha }
}

/**
 * Create a solid paint from hex color
 */
export function solidPaint(hex: string, opacity: number = 1): SolidPaint {
  return {
    type: 'SOLID',
    color: hexToRgb(hex),
    opacity,
  }
}

/**
 * Create a gradient paint
 */
export function gradientPaint(
  colors: string[],
  positions: number[],
  angle: number = 180
): GradientPaint {
  const radians = (angle * Math.PI) / 180
  const gradientStops: ColorStop[] = colors.map((color, i) => ({
    color: hexToRgba(color),
    position: positions[i] ?? i / (colors.length - 1),
  }))

  return {
    type: 'GRADIENT_LINEAR',
    gradientStops,
    gradientTransform: [
      [Math.cos(radians), Math.sin(radians), 0],
      [-Math.sin(radians), Math.cos(radians), 0],
    ],
  }
}

// ============================================================================
// TEXT UTILITIES
// ============================================================================

/**
 * Load fonts required for the design system
 */
export async function loadFonts(): Promise<void> {
  await Promise.all([
    figma.loadFontAsync({ family: 'Inter', style: 'Regular' }),
    figma.loadFontAsync({ family: 'Inter', style: 'Medium' }),
    figma.loadFontAsync({ family: 'Inter', style: 'Semi Bold' }),
    figma.loadFontAsync({ family: 'Inter', style: 'Bold' }),
  ])
}

/**
 * Create a text node with styling
 */
export async function createText(
  content: string,
  style: {
    fontSize: number
    fontWeight: number
    lineHeight: number
    letterSpacing?: number
    color?: string
  }
): Promise<TextNode> {
  const text = figma.createText()

  // Load appropriate font weight
  let fontStyle = 'Regular'
  if (style.fontWeight >= 700) fontStyle = 'Bold'
  else if (style.fontWeight >= 600) fontStyle = 'Semi Bold'
  else if (style.fontWeight >= 500) fontStyle = 'Medium'

  await figma.loadFontAsync({ family: 'Inter', style: fontStyle })

  text.fontName = { family: 'Inter', style: fontStyle }
  text.characters = content
  text.fontSize = style.fontSize
  text.lineHeight = { value: style.lineHeight, unit: 'PIXELS' }

  if (style.letterSpacing) {
    text.letterSpacing = { value: style.letterSpacing, unit: 'PIXELS' }
  }

  if (style.color) {
    text.fills = [solidPaint(style.color)]
  }

  return text
}

// ============================================================================
// LAYOUT UTILITIES
// ============================================================================

/**
 * Configure auto-layout on a frame
 */
export function setAutoLayout(
  frame: FrameNode,
  options: {
    direction: 'HORIZONTAL' | 'VERTICAL'
    padding?: number | { top?: number; right?: number; bottom?: number; left?: number }
    gap?: number
    alignment?: 'MIN' | 'CENTER' | 'MAX' | 'SPACE_BETWEEN'
    crossAlignment?: 'MIN' | 'CENTER' | 'MAX'
  }
): void {
  frame.layoutMode = options.direction

  if (typeof options.padding === 'number') {
    frame.paddingTop = options.padding
    frame.paddingRight = options.padding
    frame.paddingBottom = options.padding
    frame.paddingLeft = options.padding
  } else if (options.padding) {
    frame.paddingTop = options.padding.top ?? 0
    frame.paddingRight = options.padding.right ?? 0
    frame.paddingBottom = options.padding.bottom ?? 0
    frame.paddingLeft = options.padding.left ?? 0
  }

  if (options.gap !== undefined) {
    frame.itemSpacing = options.gap
  }

  if (options.alignment) {
    frame.primaryAxisAlignItems = options.alignment
  }

  if (options.crossAlignment) {
    frame.counterAxisAlignItems = options.crossAlignment
  }
}

/**
 * Set sizing mode for auto-layout children
 */
export function setSizing(
  node: SceneNode & LayoutMixin,
  options: {
    horizontal?: 'FIXED' | 'HUG' | 'FILL'
    vertical?: 'FIXED' | 'HUG' | 'FILL'
  }
): void {
  if (options.horizontal) {
    node.layoutSizingHorizontal = options.horizontal
  }
  if (options.vertical) {
    node.layoutSizingVertical = options.vertical
  }
}

// ============================================================================
// SHADOW UTILITIES
// ============================================================================

/**
 * Apply elevation shadow to a node
 */
export function applyElevation(
  node: BlendMixin,
  level: 0 | 1 | 2 | 3 | 4 | 5
): void {
  const elevations: Record<number, DropShadowEffect[]> = {
    0: [],
    1: [{
      type: 'DROP_SHADOW',
      color: { r: 0, g: 0, b: 0, a: 0.15 },
      offset: { x: 0, y: 1 },
      radius: 3,
      spread: 1,
      visible: true,
      blendMode: 'NORMAL',
    }],
    2: [{
      type: 'DROP_SHADOW',
      color: { r: 0, g: 0, b: 0, a: 0.15 },
      offset: { x: 0, y: 2 },
      radius: 6,
      spread: 2,
      visible: true,
      blendMode: 'NORMAL',
    }],
    3: [{
      type: 'DROP_SHADOW',
      color: { r: 0, g: 0, b: 0, a: 0.2 },
      offset: { x: 0, y: 4 },
      radius: 8,
      spread: 3,
      visible: true,
      blendMode: 'NORMAL',
    }],
    4: [{
      type: 'DROP_SHADOW',
      color: { r: 0, g: 0, b: 0, a: 0.2 },
      offset: { x: 0, y: 6 },
      radius: 10,
      spread: 4,
      visible: true,
      blendMode: 'NORMAL',
    }],
    5: [{
      type: 'DROP_SHADOW',
      color: { r: 0, g: 0, b: 0, a: 0.25 },
      offset: { x: 0, y: 8 },
      radius: 12,
      spread: 6,
      visible: true,
      blendMode: 'NORMAL',
    }],
  }

  node.effects = elevations[level]
}

// ============================================================================
// ICON UTILITIES
// ============================================================================

/**
 * Create a placeholder icon (circle with letter)
 */
export function createIconPlaceholder(
  letter: string,
  size: number,
  bgColor: string,
  textColor: string
): FrameNode {
  const frame = figma.createFrame()
  frame.name = `Icon-${letter}`
  frame.resize(size, size)
  frame.cornerRadius = size / 2
  frame.fills = [solidPaint(bgColor)]

  // We'll add text in the main plugin after fonts are loaded
  frame.layoutMode = 'HORIZONTAL'
  frame.primaryAxisAlignItems = 'CENTER'
  frame.counterAxisAlignItems = 'CENTER'

  return frame
}

// ============================================================================
// FRAME UTILITIES
// ============================================================================

/**
 * Create a basic frame with common settings
 */
export function createFrame(
  name: string,
  options?: {
    width?: number
    height?: number
    fill?: string
    radius?: number
  }
): FrameNode {
  const frame = figma.createFrame()
  frame.name = name

  if (options?.width && options?.height) {
    frame.resize(options.width, options.height)
  }

  if (options?.fill) {
    frame.fills = [solidPaint(options.fill)]
  } else {
    frame.fills = []
  }

  if (options?.radius) {
    frame.cornerRadius = options.radius
  }

  return frame
}

/**
 * Create a divider line
 */
export function createDivider(
  width: number,
  color: string = '#CAC4D0'
): RectangleNode {
  const divider = figma.createRectangle()
  divider.name = 'Divider'
  divider.resize(width, 1)
  divider.fills = [solidPaint(color)]
  return divider
}
