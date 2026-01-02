/**
 * Top Bar / App Bar Generator
 * Creates Material Design 3 top app bars
 */

import { Colors, Typography, Spacing, ComponentSizes, IconSizes } from '../../design-system/tokens'
import { solidPaint, setAutoLayout, createText, createIconPlaceholder } from '../../utils/helpers'

export interface TopBarOptions {
  title: string
  variant?: 'small' | 'medium' | 'large'
  showBackButton?: boolean
  backgroundColor?: string
  actions?: Array<{ icon: string; label: string }>
}

/**
 * Create a top app bar
 */
export async function createTopBar(options: TopBarOptions): Promise<FrameNode> {
  const {
    title,
    variant = 'small',
    showBackButton = false,
    backgroundColor = Colors.surface,
    actions = [],
  } = options

  const topBar = figma.createFrame()
  topBar.name = 'TopBar'
  topBar.layoutSizingHorizontal = 'FILL'
  topBar.fills = [solidPaint(backgroundColor)]

  if (variant === 'small') {
    topBar.resize(393, ComponentSizes.topBarHeight)

    setAutoLayout(topBar, {
      direction: 'HORIZONTAL',
      padding: { top: 8, right: 4, bottom: 8, left: 4 },
      gap: 4,
      crossAlignment: 'CENTER',
    })

    // Navigation icon (back button or menu)
    if (showBackButton) {
      const backIcon = createIconPlaceholder('←', IconSizes.md, 'transparent', Colors.onSurface)
      backIcon.name = 'BackButton'
      const wrapper = figma.createFrame()
      wrapper.name = 'IconButton'
      wrapper.resize(48, 48)
      wrapper.fills = []
      wrapper.layoutMode = 'HORIZONTAL'
      wrapper.primaryAxisAlignItems = 'CENTER'
      wrapper.counterAxisAlignItems = 'CENTER'
      wrapper.appendChild(backIcon)
      topBar.appendChild(wrapper)
    }

    // Title
    const titleText = await createText(title, {
      ...Typography.titleLarge,
      color: Colors.onSurface,
    })
    titleText.name = 'Title'
    titleText.layoutSizingHorizontal = 'FILL'
    if (showBackButton) {
      titleText.textAlignHorizontal = 'LEFT'
    } else {
      // Add leading padding if no back button
      const spacer = figma.createFrame()
      spacer.name = 'Spacer'
      spacer.resize(16, 1)
      spacer.fills = []
      topBar.insertChild(0, spacer)
    }
    topBar.appendChild(titleText)

    // Action icons
    for (const action of actions) {
      const actionIcon = createIconPlaceholder(
        action.icon.charAt(0).toUpperCase(),
        IconSizes.md,
        'transparent',
        Colors.onSurfaceVariant
      )
      const wrapper = figma.createFrame()
      wrapper.name = `Action-${action.label}`
      wrapper.resize(48, 48)
      wrapper.fills = []
      wrapper.layoutMode = 'HORIZONTAL'
      wrapper.primaryAxisAlignItems = 'CENTER'
      wrapper.counterAxisAlignItems = 'CENTER'
      wrapper.appendChild(actionIcon)
      topBar.appendChild(wrapper)
    }
  } else if (variant === 'medium' || variant === 'large') {
    const height = variant === 'medium' ? 112 : 152
    topBar.resize(393, height)

    setAutoLayout(topBar, {
      direction: 'VERTICAL',
      padding: 0,
      gap: 0,
    })

    // Top row with navigation and actions
    const topRow = figma.createFrame()
    topRow.name = 'TopRow'
    topRow.layoutSizingHorizontal = 'FILL'
    topRow.resize(393, 64)
    topRow.fills = []

    setAutoLayout(topRow, {
      direction: 'HORIZONTAL',
      padding: { top: 8, right: 4, bottom: 8, left: 4 },
      gap: 4,
      crossAlignment: 'CENTER',
    })

    if (showBackButton) {
      const backIcon = createIconPlaceholder('←', IconSizes.md, 'transparent', Colors.onSurface)
      const wrapper = figma.createFrame()
      wrapper.name = 'BackButton'
      wrapper.resize(48, 48)
      wrapper.fills = []
      wrapper.layoutMode = 'HORIZONTAL'
      wrapper.primaryAxisAlignItems = 'CENTER'
      wrapper.counterAxisAlignItems = 'CENTER'
      wrapper.appendChild(backIcon)
      topRow.appendChild(wrapper)
    }

    // Spacer
    const spacer = figma.createFrame()
    spacer.name = 'Spacer'
    spacer.fills = []
    spacer.layoutSizingHorizontal = 'FILL'
    spacer.resize(100, 1)
    topRow.appendChild(spacer)

    // Actions
    for (const action of actions) {
      const actionIcon = createIconPlaceholder(
        action.icon.charAt(0).toUpperCase(),
        IconSizes.md,
        'transparent',
        Colors.onSurfaceVariant
      )
      const wrapper = figma.createFrame()
      wrapper.name = `Action-${action.label}`
      wrapper.resize(48, 48)
      wrapper.fills = []
      wrapper.layoutMode = 'HORIZONTAL'
      wrapper.primaryAxisAlignItems = 'CENTER'
      wrapper.counterAxisAlignItems = 'CENTER'
      wrapper.appendChild(actionIcon)
      topRow.appendChild(wrapper)
    }

    topBar.appendChild(topRow)

    // Title row
    const titleRow = figma.createFrame()
    titleRow.name = 'TitleRow'
    titleRow.layoutSizingHorizontal = 'FILL'
    titleRow.layoutSizingVertical = 'FILL'
    titleRow.fills = []

    setAutoLayout(titleRow, {
      direction: 'HORIZONTAL',
      padding: { top: 0, right: 16, bottom: 28, left: 16 },
      crossAlignment: 'MAX',
    })

    const typography = variant === 'medium' ? Typography.headlineSmall : Typography.headlineMedium
    const titleText = await createText(title, {
      ...typography,
      color: Colors.onSurface,
    })
    titleText.name = 'Title'
    titleRow.appendChild(titleText)

    topBar.appendChild(titleRow)
  }

  return topBar
}
