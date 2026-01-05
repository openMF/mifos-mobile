/**
 * Quick Actions Component Generator
 * Creates horizontal action button rows
 */

import { Colors, Typography, Spacing, Radius } from '../../design-system/tokens'
import { solidPaint, setAutoLayout, createText, createIconPlaceholder } from '../../utils/helpers'

export interface QuickAction {
  icon: string
  label: string
  color?: string
}

export interface QuickActionsOptions {
  actions: QuickAction[]
  variant?: 'filled' | 'outlined' | 'tonal'
  size?: 'small' | 'medium' | 'large'
}

/**
 * Create a quick actions row
 */
export async function createQuickActions(options: QuickActionsOptions): Promise<FrameNode> {
  const {
    actions,
    variant = 'tonal',
    size = 'medium',
  } = options

  const container = figma.createFrame()
  container.name = 'QuickActions'
  container.layoutSizingHorizontal = 'FILL'
  container.fills = []

  setAutoLayout(container, {
    direction: 'HORIZONTAL',
    gap: Spacing.sm,
    alignment: 'CENTER',
  })

  for (const action of actions) {
    const button = await createQuickActionButton(action, variant, size)
    container.appendChild(button)
  }

  return container
}

/**
 * Create a single quick action button
 */
async function createQuickActionButton(
  action: QuickAction,
  variant: 'filled' | 'outlined' | 'tonal',
  size: 'small' | 'medium' | 'large'
): Promise<FrameNode> {
  const sizeConfig = {
    small: { iconSize: 20, padding: 8, gap: 4, iconBg: 36 },
    medium: { iconSize: 24, padding: 12, gap: 8, iconBg: 48 },
    large: { iconSize: 28, padding: 16, gap: 8, iconBg: 56 },
  }

  const config = sizeConfig[size]

  const button = figma.createFrame()
  button.name = `QuickAction-${action.label}`
  button.layoutSizingHorizontal = 'FILL'
  button.cornerRadius = Radius.lg

  // Variant styling
  switch (variant) {
    case 'filled':
      button.fills = [solidPaint(action.color || Colors.primaryContainer)]
      break
    case 'outlined':
      button.fills = [solidPaint(Colors.surface)]
      button.strokes = [solidPaint(Colors.outlineVariant)]
      button.strokeWeight = 1
      break
    case 'tonal':
      button.fills = [solidPaint(Colors.surfaceContainerHigh)]
      break
  }

  setAutoLayout(button, {
    direction: 'VERTICAL',
    padding: config.padding,
    gap: config.gap,
    alignment: 'CENTER',
    crossAlignment: 'CENTER',
  })

  // Icon background
  const iconBg = figma.createFrame()
  iconBg.name = 'IconBackground'
  iconBg.resize(config.iconBg, config.iconBg)
  iconBg.cornerRadius = config.iconBg / 2
  iconBg.fills = [solidPaint(action.color || Colors.primaryContainer)]

  setAutoLayout(iconBg, {
    direction: 'HORIZONTAL',
    alignment: 'CENTER',
    crossAlignment: 'CENTER',
  })

  const icon = createIconPlaceholder(
    action.icon,
    config.iconSize,
    'transparent',
    Colors.onPrimaryContainer
  )
  iconBg.appendChild(icon)
  button.appendChild(iconBg)

  // Label
  const label = await createText(action.label, {
    ...Typography.labelMedium,
    color: Colors.onSurface,
  })
  label.textAlignHorizontal = 'CENTER'
  button.appendChild(label)

  return button
}

/**
 * Create a Mifos-style service item grid
 */
export async function createServiceItemGrid(services: QuickAction[]): Promise<FrameNode> {
  const grid = figma.createFrame()
  grid.name = 'ServiceGrid'
  grid.layoutSizingHorizontal = 'FILL'
  grid.fills = []

  setAutoLayout(grid, {
    direction: 'HORIZONTAL',
    gap: Spacing.md,
    padding: Spacing.md,
  })

  // Create rows of 4 items each
  const itemsPerRow = 4
  let currentRow: FrameNode | null = null

  for (let i = 0; i < services.length; i++) {
    if (i % itemsPerRow === 0) {
      currentRow = figma.createFrame()
      currentRow.name = `Row-${Math.floor(i / itemsPerRow)}`
      currentRow.layoutSizingHorizontal = 'FILL'
      currentRow.fills = []
      setAutoLayout(currentRow, {
        direction: 'HORIZONTAL',
        gap: Spacing.md,
        alignment: 'SPACE_BETWEEN',
      })
      grid.appendChild(currentRow)
    }

    const service = services[i]
    const item = await createServiceItem(service)
    currentRow?.appendChild(item)
  }

  return grid
}

/**
 * Create a single service item (icon + label)
 */
async function createServiceItem(service: QuickAction): Promise<FrameNode> {
  const item = figma.createFrame()
  item.name = `Service-${service.label}`
  item.resize(72, 80)
  item.fills = []

  setAutoLayout(item, {
    direction: 'VERTICAL',
    gap: 8,
    alignment: 'CENTER',
    crossAlignment: 'CENTER',
  })

  // Icon container
  const iconContainer = figma.createFrame()
  iconContainer.name = 'IconContainer'
  iconContainer.resize(48, 48)
  iconContainer.cornerRadius = 12
  iconContainer.fills = [solidPaint(service.color || Colors.primaryContainer)]

  setAutoLayout(iconContainer, {
    direction: 'HORIZONTAL',
    alignment: 'CENTER',
    crossAlignment: 'CENTER',
  })

  const icon = createIconPlaceholder(
    service.icon,
    24,
    'transparent',
    Colors.onPrimaryContainer
  )
  iconContainer.appendChild(icon)
  item.appendChild(iconContainer)

  // Label
  const label = await createText(service.label, {
    ...Typography.labelSmall,
    color: Colors.onSurface,
  })
  label.textAlignHorizontal = 'CENTER'
  item.appendChild(label)

  return item
}

/**
 * Create a section header with "View All" action
 */
export async function createSectionHeader(
  title: string,
  showViewAll: boolean = true
): Promise<FrameNode> {
  const header = figma.createFrame()
  header.name = `SectionHeader-${title}`
  header.layoutSizingHorizontal = 'FILL'
  header.fills = []

  setAutoLayout(header, {
    direction: 'HORIZONTAL',
    padding: { top: Spacing.md, right: 0, bottom: Spacing.sm, left: 0 },
    alignment: 'SPACE_BETWEEN',
    crossAlignment: 'CENTER',
  })

  const titleText = await createText(title, {
    ...Typography.titleMedium,
    color: Colors.onSurface,
  })
  header.appendChild(titleText)

  if (showViewAll) {
    const viewAll = await createText('View All →', {
      ...Typography.labelMedium,
      color: Colors.primary,
    })
    header.appendChild(viewAll)
  }

  return header
}
