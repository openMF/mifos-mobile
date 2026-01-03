/**
 * Bottom Navigation Component Generator
 * Creates Material Design 3 bottom navigation bar
 */

import { Colors, Typography, Spacing, ComponentSizes } from '../../design-system/tokens'
import { solidPaint, setAutoLayout, createText, createIconPlaceholder } from '../../utils/helpers'

export interface NavItem {
  icon: string
  label: string
  badge?: number | boolean
}

export interface BottomNavOptions {
  items: NavItem[]
  selectedIndex?: number
}

/**
 * Create a bottom navigation bar
 */
export async function createBottomNav(options: BottomNavOptions): Promise<FrameNode> {
  const { items, selectedIndex = 0 } = options

  const nav = figma.createFrame()
  nav.name = 'BottomNavigation'
  nav.layoutSizingHorizontal = 'FILL'
  nav.resize(393, ComponentSizes.bottomNavHeight)
  nav.fills = [solidPaint(Colors.surfaceContainer)]

  setAutoLayout(nav, {
    direction: 'HORIZONTAL',
    padding: { top: 12, right: 0, bottom: 16, left: 0 },
    alignment: 'SPACE_BETWEEN',
    crossAlignment: 'CENTER',
  })

  for (let i = 0; i < items.length; i++) {
    const item = items[i]
    const isSelected = i === selectedIndex
    const navItem = await createNavItem(item, isSelected)
    nav.appendChild(navItem)
  }

  return nav
}

/**
 * Create a single navigation item
 */
async function createNavItem(item: NavItem, selected: boolean): Promise<FrameNode> {
  const navItem = figma.createFrame()
  navItem.name = `NavItem-${item.label}`
  navItem.layoutSizingHorizontal = 'FILL'
  navItem.fills = []

  setAutoLayout(navItem, {
    direction: 'VERTICAL',
    gap: 4,
    alignment: 'CENTER',
    crossAlignment: 'CENTER',
  })

  // Indicator + Icon container
  const iconContainer = figma.createFrame()
  iconContainer.name = 'IconContainer'
  iconContainer.resize(64, 32)
  iconContainer.fills = []

  // Indicator (pill shape behind icon when selected)
  if (selected) {
    const indicator = figma.createFrame()
    indicator.name = 'Indicator'
    indicator.resize(64, 32)
    indicator.cornerRadius = 16
    indicator.fills = [solidPaint(Colors.secondaryContainer)]
    indicator.x = 0
    indicator.y = 0
    iconContainer.appendChild(indicator)
  }

  // Icon
  const icon = createIconPlaceholder(
    item.icon.charAt(0).toUpperCase(),
    24,
    'transparent',
    selected ? Colors.onSecondaryContainer : Colors.onSurfaceVariant
  )
  icon.x = 20
  icon.y = 4
  iconContainer.appendChild(icon)

  // Badge
  if (item.badge !== undefined && item.badge !== false) {
    const badge = createBadge(typeof item.badge === 'number' ? item.badge : undefined)
    badge.x = 36
    badge.y = 0
    iconContainer.appendChild(badge)
  }

  navItem.appendChild(iconContainer)

  // Label
  const label = await createText(item.label, {
    ...Typography.labelMedium,
    color: selected ? Colors.onSurface : Colors.onSurfaceVariant,
  })
  navItem.appendChild(label)

  return navItem
}

/**
 * Create a badge
 */
function createBadge(count?: number): FrameNode {
  const badge = figma.createFrame()
  badge.name = 'Badge'

  if (count !== undefined && count > 0) {
    // Large badge with number
    badge.resize(count > 9 ? 24 : 16, 16)
    badge.cornerRadius = 8
    badge.fills = [solidPaint(Colors.error)]

    setAutoLayout(badge, {
      direction: 'HORIZONTAL',
      padding: { top: 0, right: 4, bottom: 0, left: 4 },
      alignment: 'CENTER',
      crossAlignment: 'CENTER',
    })

    // Badge count would be added after font loading
  } else {
    // Small dot badge
    badge.resize(6, 6)
    badge.cornerRadius = 3
    badge.fills = [solidPaint(Colors.error)]
  }

  return badge
}

/**
 * Create a navigation rail (for tablets/desktop)
 */
export async function createNavigationRail(options: {
  items: NavItem[]
  selectedIndex?: number
  showFab?: boolean
  fabIcon?: string
}): Promise<FrameNode> {
  const { items, selectedIndex = 0, showFab = false, fabIcon = '+' } = options

  const rail = figma.createFrame()
  rail.name = 'NavigationRail'
  rail.resize(80, 600)
  rail.fills = [solidPaint(Colors.surface)]

  setAutoLayout(rail, {
    direction: 'VERTICAL',
    padding: { top: 12, right: 0, bottom: 12, left: 0 },
    gap: 12,
    crossAlignment: 'CENTER',
  })

  // Menu button placeholder
  const menuButton = figma.createFrame()
  menuButton.name = 'MenuButton'
  menuButton.resize(48, 48)
  menuButton.cornerRadius = 24
  menuButton.fills = []
  menuButton.layoutMode = 'HORIZONTAL'
  menuButton.primaryAxisAlignItems = 'CENTER'
  menuButton.counterAxisAlignItems = 'CENTER'

  const menuIcon = createIconPlaceholder('≡', 24, 'transparent', Colors.onSurfaceVariant)
  menuButton.appendChild(menuIcon)
  rail.appendChild(menuButton)

  // FAB
  if (showFab) {
    const fab = figma.createFrame()
    fab.name = 'FAB'
    fab.resize(56, 56)
    fab.cornerRadius = 16
    fab.fills = [solidPaint(Colors.primaryContainer)]
    fab.layoutMode = 'HORIZONTAL'
    fab.primaryAxisAlignItems = 'CENTER'
    fab.counterAxisAlignItems = 'CENTER'

    const fabIconNode = createIconPlaceholder(fabIcon, 24, 'transparent', Colors.onPrimaryContainer)
    fab.appendChild(fabIconNode)
    rail.appendChild(fab)
  }

  // Spacer
  const spacer = figma.createFrame()
  spacer.name = 'Spacer'
  spacer.resize(1, 12)
  spacer.fills = []
  rail.appendChild(spacer)

  // Navigation items
  for (let i = 0; i < items.length; i++) {
    const item = items[i]
    const isSelected = i === selectedIndex

    const railItem = figma.createFrame()
    railItem.name = `RailItem-${item.label}`
    railItem.resize(56, 56)
    railItem.fills = []

    setAutoLayout(railItem, {
      direction: 'VERTICAL',
      gap: 4,
      alignment: 'CENTER',
      crossAlignment: 'CENTER',
    })

    // Icon with indicator
    const iconWrapper = figma.createFrame()
    iconWrapper.name = 'IconWrapper'
    iconWrapper.resize(56, 32)
    iconWrapper.fills = []

    if (isSelected) {
      const indicator = figma.createFrame()
      indicator.name = 'Indicator'
      indicator.resize(56, 32)
      indicator.cornerRadius = 16
      indicator.fills = [solidPaint(Colors.secondaryContainer)]
      iconWrapper.appendChild(indicator)
    }

    const icon = createIconPlaceholder(
      item.icon.charAt(0).toUpperCase(),
      24,
      'transparent',
      isSelected ? Colors.onSecondaryContainer : Colors.onSurfaceVariant
    )
    icon.x = 16
    icon.y = 4
    iconWrapper.appendChild(icon)

    railItem.appendChild(iconWrapper)

    // Label
    const label = await createText(item.label, {
      ...Typography.labelMedium,
      color: isSelected ? Colors.onSurface : Colors.onSurfaceVariant,
    })
    railItem.appendChild(label)

    rail.appendChild(railItem)
  }

  return rail
}
