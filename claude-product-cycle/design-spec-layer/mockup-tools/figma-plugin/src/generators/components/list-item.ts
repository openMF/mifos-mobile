/**
 * List Item Component Generator
 * Creates Material Design 3 list items
 */

import { Colors, Typography, Spacing, ComponentSizes } from '../../design-system/tokens'
import { solidPaint, setAutoLayout, createText, createIconPlaceholder, createDivider } from '../../utils/helpers'

export interface ListItemOptions {
  headline: string
  supportingText?: string
  overline?: string
  leadingIcon?: string
  leadingAvatar?: { initials: string; color?: string }
  leadingImage?: boolean
  trailingText?: string
  trailingIcon?: string
  trailingSwitch?: boolean
  trailingCheckbox?: boolean
  selected?: boolean
  size?: 'one-line' | 'two-line' | 'three-line'
  showDivider?: boolean
}

/**
 * Create a list item
 */
export async function createListItem(options: ListItemOptions): Promise<FrameNode> {
  const {
    headline,
    supportingText,
    overline,
    leadingIcon,
    leadingAvatar,
    leadingImage,
    trailingText,
    trailingIcon,
    trailingSwitch,
    trailingCheckbox,
    selected = false,
    size = supportingText ? 'two-line' : 'one-line',
    showDivider = false,
  } = options

  const heights = {
    'one-line': 56,
    'two-line': 72,
    'three-line': 88,
  }

  const container = figma.createFrame()
  container.name = 'ListItemContainer'
  container.layoutSizingHorizontal = 'FILL'
  container.fills = []
  setAutoLayout(container, {
    direction: 'VERTICAL',
    gap: 0,
  })

  const item = figma.createFrame()
  item.name = `ListItem-${headline}`
  item.layoutSizingHorizontal = 'FILL'
  item.resize(100, heights[size])
  item.fills = selected ? [solidPaint(Colors.secondaryContainer)] : []

  setAutoLayout(item, {
    direction: 'HORIZONTAL',
    padding: { top: 8, right: 16, bottom: 8, left: 16 },
    gap: 16,
    crossAlignment: 'CENTER',
  })

  // Leading element
  if (leadingIcon) {
    const icon = createIconPlaceholder(leadingIcon.charAt(0), 24, 'transparent', Colors.onSurfaceVariant)
    icon.name = 'LeadingIcon'
    item.appendChild(icon)
  } else if (leadingAvatar) {
    const avatar = figma.createFrame()
    avatar.name = 'Avatar'
    avatar.resize(40, 40)
    avatar.cornerRadius = 20
    avatar.fills = [solidPaint(leadingAvatar.color || Colors.primaryContainer)]
    avatar.layoutMode = 'HORIZONTAL'
    avatar.primaryAxisAlignItems = 'CENTER'
    avatar.counterAxisAlignItems = 'CENTER'

    const initials = await createText(leadingAvatar.initials, {
      ...Typography.titleMedium,
      color: Colors.onPrimaryContainer,
    })
    avatar.appendChild(initials)
    item.appendChild(avatar)
  } else if (leadingImage) {
    const image = figma.createFrame()
    image.name = 'LeadingImage'
    image.resize(56, 56)
    image.cornerRadius = 4
    image.fills = [solidPaint(Colors.surfaceContainerHighest)]
    item.appendChild(image)
  }

  // Content
  const content = figma.createFrame()
  content.name = 'Content'
  content.layoutSizingHorizontal = 'FILL'
  content.fills = []
  setAutoLayout(content, {
    direction: 'VERTICAL',
    gap: 0,
    crossAlignment: 'MIN',
  })

  if (overline) {
    const overlineText = await createText(overline, {
      ...Typography.labelSmall,
      color: Colors.onSurfaceVariant,
    })
    content.appendChild(overlineText)
  }

  const headlineText = await createText(headline, {
    ...Typography.bodyLarge,
    color: Colors.onSurface,
  })
  content.appendChild(headlineText)

  if (supportingText) {
    const supportText = await createText(supportingText, {
      ...Typography.bodyMedium,
      color: Colors.onSurfaceVariant,
    })
    if (size === 'three-line') {
      supportText.textAutoResize = 'HEIGHT'
      supportText.layoutSizingHorizontal = 'FILL'
    }
    content.appendChild(supportText)
  }

  item.appendChild(content)

  // Trailing element
  if (trailingText) {
    const trailText = await createText(trailingText, {
      ...Typography.labelSmall,
      color: Colors.onSurfaceVariant,
    })
    item.appendChild(trailText)
  } else if (trailingIcon) {
    const icon = createIconPlaceholder(trailingIcon.charAt(0), 24, 'transparent', Colors.onSurfaceVariant)
    icon.name = 'TrailingIcon'
    item.appendChild(icon)
  } else if (trailingSwitch) {
    const switchNode = createSwitch(false)
    item.appendChild(switchNode)
  } else if (trailingCheckbox) {
    const checkbox = createCheckbox(selected)
    item.appendChild(checkbox)
  }

  container.appendChild(item)

  if (showDivider) {
    const dividerWrapper = figma.createFrame()
    dividerWrapper.name = 'DividerWrapper'
    dividerWrapper.layoutSizingHorizontal = 'FILL'
    dividerWrapper.resize(100, 1)
    dividerWrapper.fills = []
    setAutoLayout(dividerWrapper, {
      direction: 'HORIZONTAL',
      padding: { left: leadingIcon || leadingAvatar || leadingImage ? 72 : 16, right: 0 },
    })

    const divider = createDivider(100, Colors.outlineVariant)
    divider.layoutSizingHorizontal = 'FILL'
    dividerWrapper.appendChild(divider)
    container.appendChild(dividerWrapper)
  }

  return container
}

/**
 * Create a switch component
 */
function createSwitch(checked: boolean): FrameNode {
  const switchFrame = figma.createFrame()
  switchFrame.name = 'Switch'
  switchFrame.resize(52, 32)
  switchFrame.cornerRadius = 16
  switchFrame.fills = [solidPaint(checked ? Colors.primary : Colors.surfaceContainerHighest)]
  switchFrame.strokes = checked ? [] : [solidPaint(Colors.outline)]
  switchFrame.strokeWeight = 2

  // Thumb
  const thumb = figma.createFrame()
  thumb.name = 'Thumb'
  thumb.resize(checked ? 24 : 16, checked ? 24 : 16)
  thumb.cornerRadius = 12
  thumb.fills = [solidPaint(checked ? Colors.onPrimary : Colors.outline)]
  thumb.x = checked ? 24 : 8
  thumb.y = checked ? 4 : 8

  switchFrame.appendChild(thumb)
  return switchFrame
}

/**
 * Create a checkbox component
 */
function createCheckbox(checked: boolean): FrameNode {
  const checkbox = figma.createFrame()
  checkbox.name = 'Checkbox'
  checkbox.resize(24, 24)
  checkbox.cornerRadius = 4

  if (checked) {
    checkbox.fills = [solidPaint(Colors.primary)]
    // Checkmark would be added here with vector
  } else {
    checkbox.fills = []
    checkbox.strokes = [solidPaint(Colors.onSurfaceVariant)]
    checkbox.strokeWeight = 2
  }

  return checkbox
}

/**
 * Create a transaction list item (specific to Mifos)
 */
export async function createTransactionItem(data: {
  type: 'credit' | 'debit'
  description: string
  amount: string
  accountName: string
  time: string
}): Promise<FrameNode> {
  const item = figma.createFrame()
  item.name = `Transaction-${data.description}`
  item.layoutSizingHorizontal = 'FILL'
  item.resize(100, 72)
  item.fills = []

  setAutoLayout(item, {
    direction: 'HORIZONTAL',
    padding: { top: 8, right: 16, bottom: 8, left: 16 },
    gap: 16,
    crossAlignment: 'CENTER',
  })

  // Transaction icon
  const icon = figma.createFrame()
  icon.name = 'TransactionIcon'
  icon.resize(40, 40)
  icon.cornerRadius = 20
  icon.fills = [solidPaint(data.type === 'credit' ? Colors.successContainer : Colors.errorContainer)]
  icon.layoutMode = 'HORIZONTAL'
  icon.primaryAxisAlignItems = 'CENTER'
  icon.counterAxisAlignItems = 'CENTER'

  const arrow = await createText(data.type === 'credit' ? '↓' : '↑', {
    fontSize: 20,
    fontWeight: 500,
    lineHeight: 24,
    color: data.type === 'credit' ? Colors.success : Colors.error,
  })
  icon.appendChild(arrow)
  item.appendChild(icon)

  // Content
  const content = figma.createFrame()
  content.name = 'Content'
  content.layoutSizingHorizontal = 'FILL'
  content.fills = []
  setAutoLayout(content, {
    direction: 'VERTICAL',
    gap: 2,
  })

  const descText = await createText(data.description, {
    ...Typography.bodyLarge,
    color: Colors.onSurface,
  })
  content.appendChild(descText)

  const detailText = await createText(`${data.accountName} • ${data.time}`, {
    ...Typography.bodySmall,
    color: Colors.onSurfaceVariant,
  })
  content.appendChild(detailText)

  item.appendChild(content)

  // Amount
  const amountColor = data.type === 'credit' ? Colors.success : Colors.error
  const prefix = data.type === 'credit' ? '+ ' : '- '
  const amountText = await createText(prefix + data.amount, {
    ...Typography.titleMedium,
    color: amountColor,
  })
  item.appendChild(amountText)

  return item
}
