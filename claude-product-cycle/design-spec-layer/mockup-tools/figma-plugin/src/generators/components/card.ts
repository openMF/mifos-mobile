/**
 * Card Component Generator
 * Creates Material Design 3 cards
 */

import { Colors, Typography, Spacing, Radius } from '../../design-system/tokens'
import { solidPaint, setAutoLayout, createText, applyElevation } from '../../utils/helpers'

export type CardVariant = 'elevated' | 'filled' | 'outlined'

export interface CardOptions {
  name?: string
  variant?: CardVariant
  width?: number | 'fill'
  padding?: number
  radius?: number
}

/**
 * Create a base card
 */
export function createCard(options: CardOptions = {}): FrameNode {
  const {
    name = 'Card',
    variant = 'elevated',
    width = 'fill',
    padding = Spacing.md,
    radius = Radius.md,
  } = options

  const card = figma.createFrame()
  card.name = name
  card.cornerRadius = radius

  if (width === 'fill') {
    card.layoutSizingHorizontal = 'FILL'
  } else {
    card.resize(width, 100)
  }

  setAutoLayout(card, {
    direction: 'VERTICAL',
    padding,
    gap: Spacing.sm,
  })

  // Apply variant styling
  switch (variant) {
    case 'elevated':
      card.fills = [solidPaint(Colors.surfaceContainerLow)]
      applyElevation(card, 1)
      break
    case 'filled':
      card.fills = [solidPaint(Colors.surfaceContainerHighest)]
      break
    case 'outlined':
      card.fills = [solidPaint(Colors.surface)]
      card.strokes = [solidPaint(Colors.outlineVariant)]
      card.strokeWeight = 1
      break
  }

  return card
}

/**
 * Create a Net Worth card (specific to Mifos Dashboard)
 */
export async function createNetWorthCard(data: {
  totalNetWorth: string
  monthlyChange: string
  savings: string
  loans: string
  shares: string
}): Promise<FrameNode> {
  const card = figma.createFrame()
  card.name = 'NetWorthCard'
  card.layoutSizingHorizontal = 'FILL'
  card.cornerRadius = Radius.xl
  card.fills = [solidPaint(Colors.primary)]

  setAutoLayout(card, {
    direction: 'VERTICAL',
    padding: { top: 20, right: 24, bottom: 20, left: 24 },
    gap: Spacing.md,
  })

  // Header row with title and eye icon
  const header = figma.createFrame()
  header.name = 'Header'
  header.layoutSizingHorizontal = 'FILL'
  header.fills = []
  setAutoLayout(header, {
    direction: 'HORIZONTAL',
    alignment: 'SPACE_BETWEEN',
    crossAlignment: 'CENTER',
  })

  const titleText = await createText('TOTAL NET WORTH', {
    ...Typography.labelMedium,
    color: Colors.onPrimary,
  })
  header.appendChild(titleText)

  // Eye icon placeholder
  const eyeIcon = figma.createFrame()
  eyeIcon.name = 'VisibilityToggle'
  eyeIcon.resize(24, 24)
  eyeIcon.fills = []
  header.appendChild(eyeIcon)

  card.appendChild(header)

  // Amount
  const amountText = await createText(data.totalNetWorth, {
    ...Typography.displayMedium,
    color: Colors.onPrimary,
  })
  amountText.name = 'Amount'
  card.appendChild(amountText)

  // Monthly change
  const changeText = await createText(`↑ ${data.monthlyChange} this month`, {
    ...Typography.bodyMedium,
    color: Colors.onPrimary,
  })
  changeText.name = 'MonthlyChange'
  changeText.opacity = 0.8
  card.appendChild(changeText)

  // Breakdown row
  const breakdown = figma.createFrame()
  breakdown.name = 'Breakdown'
  breakdown.layoutSizingHorizontal = 'FILL'
  breakdown.fills = []
  setAutoLayout(breakdown, {
    direction: 'HORIZONTAL',
    gap: Spacing.xl,
    padding: { top: Spacing.sm, right: 0, bottom: 0, left: 0 },
  })

  const categories = [
    { label: 'Savings', value: data.savings },
    { label: 'Loans', value: data.loans },
    { label: 'Shares', value: data.shares },
  ]

  for (const cat of categories) {
    const item = figma.createFrame()
    item.name = cat.label
    item.fills = []
    setAutoLayout(item, {
      direction: 'VERTICAL',
      gap: 2,
    })

    const label = await createText(cat.label, {
      ...Typography.labelSmall,
      color: Colors.onPrimary,
    })
    label.opacity = 0.7
    item.appendChild(label)

    const value = await createText(cat.value, {
      ...Typography.titleMedium,
      color: Colors.onPrimary,
    })
    item.appendChild(value)

    breakdown.appendChild(item)
  }

  card.appendChild(breakdown)

  return card
}

/**
 * Create an Account card
 */
export async function createAccountCard(data: {
  icon: string
  accountName: string
  accountNumber: string
  balance: string
  status: string
  subtitle?: string
  progress?: number
}): Promise<FrameNode> {
  const card = createCard({ name: 'AccountCard', variant: 'elevated' })

  // Main content row
  const content = figma.createFrame()
  content.name = 'Content'
  content.layoutSizingHorizontal = 'FILL'
  content.fills = []
  setAutoLayout(content, {
    direction: 'HORIZONTAL',
    gap: Spacing.md,
    crossAlignment: 'CENTER',
  })

  // Icon
  const icon = figma.createFrame()
  icon.name = 'Icon'
  icon.resize(40, 40)
  icon.cornerRadius = 20
  icon.fills = [solidPaint(Colors.primaryContainer)]
  icon.layoutMode = 'HORIZONTAL'
  icon.primaryAxisAlignItems = 'CENTER'
  icon.counterAxisAlignItems = 'CENTER'
  content.appendChild(icon)

  // Info section
  const info = figma.createFrame()
  info.name = 'Info'
  info.fills = []
  info.layoutSizingHorizontal = 'FILL'
  setAutoLayout(info, {
    direction: 'VERTICAL',
    gap: 2,
  })

  const nameText = await createText(data.accountName, {
    ...Typography.titleMedium,
    color: Colors.onSurface,
  })
  info.appendChild(nameText)

  const detailText = await createText(`${data.accountNumber}  •  ${data.status}`, {
    ...Typography.bodySmall,
    color: Colors.onSurfaceVariant,
  })
  info.appendChild(detailText)

  if (data.subtitle) {
    const subtitleText = await createText(data.subtitle, {
      ...Typography.bodySmall,
      color: Colors.onSurfaceVariant,
    })
    info.appendChild(subtitleText)
  }

  content.appendChild(info)

  // Balance
  const balanceText = await createText(data.balance, {
    ...Typography.titleMedium,
    color: Colors.onSurface,
  })
  content.appendChild(balanceText)

  card.appendChild(content)

  // Progress bar (for loans)
  if (data.progress !== undefined) {
    const progressContainer = figma.createFrame()
    progressContainer.name = 'ProgressContainer'
    progressContainer.layoutSizingHorizontal = 'FILL'
    progressContainer.fills = []
    setAutoLayout(progressContainer, {
      direction: 'VERTICAL',
      gap: 4,
      padding: { top: Spacing.sm, right: 0, bottom: 0, left: 0 },
    })

    // Progress bar background
    const progressBg = figma.createFrame()
    progressBg.name = 'ProgressBackground'
    progressBg.layoutSizingHorizontal = 'FILL'
    progressBg.resize(100, 8)
    progressBg.cornerRadius = 4
    progressBg.fills = [solidPaint(Colors.surfaceContainerHighest)]

    // Progress bar fill
    const progressFill = figma.createFrame()
    progressFill.name = 'ProgressFill'
    progressFill.resize(data.progress, 8)
    progressFill.cornerRadius = 4
    progressFill.fills = [solidPaint(Colors.primary)]
    progressBg.appendChild(progressFill)

    progressContainer.appendChild(progressBg)

    const progressText = await createText(`${data.progress}% paid`, {
      ...Typography.labelSmall,
      color: Colors.onSurfaceVariant,
    })
    progressContainer.appendChild(progressText)

    card.appendChild(progressContainer)
  }

  return card
}
