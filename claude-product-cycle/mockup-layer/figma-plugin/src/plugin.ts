/**
 * Mifos Mockup Generator - Figma Plugin
 *
 * Generates Material Design 3 mockups for Mifos Mobile app
 * from feature specifications.
 */

import { Colors, Typography, Spacing, Radius, Screens, ComponentSizes } from './design-system/tokens'
import { loadFonts, solidPaint, setAutoLayout, createText } from './utils/helpers'
import {
  createScreen,
  createScrollableContent,
  createTopBar,
  createBottomNav,
  createCard,
  createNetWorthCard,
  createAccountCard,
  createListItem,
  createTransactionItem,
  createQuickActions,
  createSectionHeader,
  createButton,
  createInputField,
} from './generators'

// ============================================================================
// PLUGIN ENTRY POINT
// ============================================================================

figma.showUI(__html__, { visible: false })

// Handle menu commands
figma.on('run', async ({ command }) => {
  try {
    await loadFonts()

    switch (command) {
      // Screen generators
      case 'generate-dashboard':
        await generateDashboardScreen()
        break
      case 'generate-auth':
        await generateAuthScreens()
        break
      case 'generate-accounts':
        await generateAccountsScreen()
        break

      // Component generators
      case 'component-net-worth-card':
        await generateNetWorthCardComponent()
        break
      case 'component-account-card':
        await generateAccountCardComponent()
        break
      case 'component-transaction-item':
        await generateTransactionItemComponent()
        break
      case 'component-quick-actions':
        await generateQuickActionsComponent()
        break
      case 'component-top-bar':
        await generateTopBarComponent()
        break
      case 'component-bottom-nav':
        await generateBottomNavComponent()
        break
      case 'component-input-field':
        await generateInputFieldComponent()
        break
      case 'component-button':
        await generateButtonComponent()
        break

      // Design system generators
      case 'ds-colors':
        await generateColorPalette()
        break
      case 'ds-typography':
        await generateTypographyScale()
        break
      case 'ds-spacing':
        await generateSpacingGrid()
        break

      default:
        figma.notify('Unknown command: ' + command)
    }

    figma.closePlugin()
  } catch (error) {
    figma.notify('Error: ' + (error as Error).message)
    figma.closePlugin()
  }
})

// ============================================================================
// SCREEN GENERATORS
// ============================================================================

async function generateDashboardScreen(): Promise<void> {
  const screen = createScreen({
    name: 'Dashboard',
    hasTopBar: true,
    hasBottomNav: true,
  })

  const content = screen.findChild(n => n.name === 'Content') as FrameNode
  if (!content) return

  // Top bar
  const topBar = await createTopBar({
    title: 'Dashboard',
    showBackButton: false,
    actions: [
      { icon: '🔔', label: 'Notifications' },
      { icon: '⚙️', label: 'Settings' },
    ],
  })
  content.appendChild(topBar)

  // Scrollable content
  const scrollContent = createScrollableContent()

  // Greeting
  const greeting = await createText('Good morning, John', {
    ...Typography.headlineSmall,
    color: Colors.onSurface,
  })
  scrollContent.appendChild(greeting)

  const date = await createText('December 28, 2025', {
    ...Typography.bodyMedium,
    color: Colors.onSurfaceVariant,
  })
  scrollContent.appendChild(date)

  // Net Worth Card
  const netWorthCard = await createNetWorthCard({
    totalNetWorth: '$ 45,750.00',
    monthlyChange: '+$1,250.00',
    savings: '$52,500',
    loans: '-$6,750',
    shares: '$0',
  })
  scrollContent.appendChild(netWorthCard)

  // Quick Actions
  const quickActionsHeader = await createSectionHeader('QUICK ACTIONS', false)
  scrollContent.appendChild(quickActionsHeader)

  const quickActions = await createQuickActions({
    actions: [
      { icon: '💸', label: 'Transfer' },
      { icon: '📥', label: 'Deposit' },
      { icon: '📊', label: 'Invest' },
      { icon: '👥', label: 'Beneficiary' },
    ],
    variant: 'tonal',
  })
  scrollContent.appendChild(quickActions)

  // My Accounts Section
  const accountsHeader = await createSectionHeader('MY ACCOUNTS', true)
  scrollContent.appendChild(accountsHeader)

  const savingsCard = await createAccountCard({
    icon: '💰',
    accountName: 'Primary Savings',
    accountNumber: 'SA-0001234567',
    balance: '$ 35,000.00',
    status: 'Active',
    subtitle: 'Interest Rate: 4.5% p.a.',
  })
  scrollContent.appendChild(savingsCard)

  const emergencyCard = await createAccountCard({
    icon: '💰',
    accountName: 'Emergency Fund',
    accountNumber: 'SA-0001234568',
    balance: '$ 17,500.00',
    status: 'Active',
    subtitle: 'Interest Rate: 3.2% p.a.',
  })
  scrollContent.appendChild(emergencyCard)

  const loanCard = await createAccountCard({
    icon: '🏦',
    accountName: 'Personal Loan',
    accountNumber: 'LA-0009876543',
    balance: '-$ 6,750.00',
    status: 'Active',
    progress: 80,
  })
  scrollContent.appendChild(loanCard)

  // Recent Activity Section
  const activityHeader = await createSectionHeader('RECENT ACTIVITY', true)
  scrollContent.appendChild(activityHeader)

  const todayLabel = await createText('Today', {
    ...Typography.labelMedium,
    color: Colors.onSurfaceVariant,
  })
  scrollContent.appendChild(todayLabel)

  const tx1 = await createTransactionItem({
    type: 'credit',
    description: 'Salary Credit',
    amount: '$4,500.00',
    accountName: 'Primary Savings',
    time: '09:30 AM',
  })
  scrollContent.appendChild(tx1)

  const tx2 = await createTransactionItem({
    type: 'debit',
    description: 'Bill Payment',
    amount: '$150.00',
    accountName: 'Primary Savings',
    time: '08:15 AM',
  })
  scrollContent.appendChild(tx2)

  content.appendChild(scrollContent)

  // Bottom Navigation
  const bottomNav = await createBottomNav({
    items: [
      { icon: '🏠', label: 'Home' },
      { icon: '💳', label: 'Accounts' },
      { icon: '💸', label: 'Transfer' },
      { icon: '📊', label: 'Activity' },
      { icon: '👤', label: 'Profile' },
    ],
    selectedIndex: 0,
  })
  content.appendChild(bottomNav)

  // Position on canvas
  screen.x = figma.viewport.center.x - screen.width / 2
  screen.y = figma.viewport.center.y - screen.height / 2

  figma.currentPage.appendChild(screen)
  figma.currentPage.selection = [screen]
  figma.viewport.scrollAndZoomIntoView([screen])

  figma.notify('Dashboard screen generated!')
}

async function generateAuthScreens(): Promise<void> {
  // Login Screen
  const loginScreen = createScreen({ name: 'Login' })
  const loginContent = loginScreen.findChild(n => n.name === 'Content') as FrameNode

  if (loginContent) {
    const scrollContent = createScrollableContent()

    // Logo placeholder
    const logo = figma.createFrame()
    logo.name = 'Logo'
    logo.resize(120, 120)
    logo.cornerRadius = 60
    logo.fills = [solidPaint(Colors.primaryContainer)]
    scrollContent.appendChild(logo)

    // Title
    const title = await createText('Welcome Back', {
      ...Typography.headlineMedium,
      color: Colors.onSurface,
    })
    scrollContent.appendChild(title)

    const subtitle = await createText('Sign in to continue', {
      ...Typography.bodyLarge,
      color: Colors.onSurfaceVariant,
    })
    scrollContent.appendChild(subtitle)

    // Input fields
    const usernameInput = await createInputField({
      label: 'Username',
      placeholder: 'Enter your username',
      variant: 'outlined',
    })
    scrollContent.appendChild(usernameInput)

    const passwordInput = await createInputField({
      label: 'Password',
      placeholder: 'Enter your password',
      variant: 'outlined',
      trailingIcon: '👁',
    })
    scrollContent.appendChild(passwordInput)

    // Forgot password
    const forgotPassword = await createText('Forgot Password?', {
      ...Typography.labelLarge,
      color: Colors.primary,
    })
    scrollContent.appendChild(forgotPassword)

    // Sign in button
    const signInButton = await createButton({
      label: 'SIGN IN',
      variant: 'filled',
      fullWidth: true,
    })
    scrollContent.appendChild(signInButton)

    // Sign up link
    const signUpText = await createText("Don't have an account? Sign Up", {
      ...Typography.bodyMedium,
      color: Colors.onSurfaceVariant,
    })
    scrollContent.appendChild(signUpText)

    loginContent.appendChild(scrollContent)
  }

  loginScreen.x = figma.viewport.center.x - loginScreen.width / 2
  loginScreen.y = figma.viewport.center.y - loginScreen.height / 2

  figma.currentPage.appendChild(loginScreen)
  figma.currentPage.selection = [loginScreen]
  figma.viewport.scrollAndZoomIntoView([loginScreen])

  figma.notify('Auth screens generated!')
}

async function generateAccountsScreen(): Promise<void> {
  const screen = createScreen({ name: 'Accounts' })
  const content = screen.findChild(n => n.name === 'Content') as FrameNode

  if (content) {
    const topBar = await createTopBar({
      title: 'My Accounts',
      showBackButton: true,
    })
    content.appendChild(topBar)

    const scrollContent = createScrollableContent()

    // Filter tabs (placeholder)
    const tabs = figma.createFrame()
    tabs.name = 'FilterTabs'
    tabs.layoutSizingHorizontal = 'FILL'
    tabs.resize(100, 48)
    tabs.fills = []
    scrollContent.appendChild(tabs)

    // Account cards
    const accounts = [
      { icon: '💰', name: 'Primary Savings', number: 'SA-001', balance: '$35,000.00', status: 'Active' },
      { icon: '💰', name: 'Emergency Fund', number: 'SA-002', balance: '$17,500.00', status: 'Active' },
      { icon: '🏦', name: 'Personal Loan', number: 'LA-001', balance: '-$6,750.00', status: 'Active', progress: 80 },
    ]

    for (const account of accounts) {
      const card = await createAccountCard({
        icon: account.icon,
        accountName: account.name,
        accountNumber: account.number,
        balance: account.balance,
        status: account.status,
        progress: account.progress,
      })
      scrollContent.appendChild(card)
    }

    content.appendChild(scrollContent)

    const bottomNav = await createBottomNav({
      items: [
        { icon: '🏠', label: 'Home' },
        { icon: '💳', label: 'Accounts' },
        { icon: '💸', label: 'Transfer' },
        { icon: '📊', label: 'Activity' },
        { icon: '👤', label: 'Profile' },
      ],
      selectedIndex: 1,
    })
    content.appendChild(bottomNav)
  }

  screen.x = figma.viewport.center.x - screen.width / 2
  screen.y = figma.viewport.center.y - screen.height / 2

  figma.currentPage.appendChild(screen)
  figma.currentPage.selection = [screen]
  figma.viewport.scrollAndZoomIntoView([screen])

  figma.notify('Accounts screen generated!')
}

// ============================================================================
// COMPONENT GENERATORS
// ============================================================================

async function generateNetWorthCardComponent(): Promise<void> {
  const card = await createNetWorthCard({
    totalNetWorth: '$ 45,750.00',
    monthlyChange: '+$1,250.00',
    savings: '$52,500',
    loans: '-$6,750',
    shares: '$0',
  })

  card.x = figma.viewport.center.x - 180
  card.y = figma.viewport.center.y - 100

  figma.currentPage.appendChild(card)
  figma.currentPage.selection = [card]
  figma.viewport.scrollAndZoomIntoView([card])

  figma.notify('Net Worth Card generated!')
}

async function generateAccountCardComponent(): Promise<void> {
  const card = await createAccountCard({
    icon: '💰',
    accountName: 'Primary Savings',
    accountNumber: 'SA-0001234567',
    balance: '$ 35,000.00',
    status: 'Active',
    subtitle: 'Interest Rate: 4.5% p.a.',
  })

  card.x = figma.viewport.center.x - 180
  card.y = figma.viewport.center.y - 50

  figma.currentPage.appendChild(card)
  figma.currentPage.selection = [card]
  figma.viewport.scrollAndZoomIntoView([card])

  figma.notify('Account Card generated!')
}

async function generateTransactionItemComponent(): Promise<void> {
  const item = await createTransactionItem({
    type: 'credit',
    description: 'Salary Credit',
    amount: '$4,500.00',
    accountName: 'Primary Savings',
    time: '09:30 AM',
  })

  item.x = figma.viewport.center.x - 180
  item.y = figma.viewport.center.y - 36

  figma.currentPage.appendChild(item)
  figma.currentPage.selection = [item]
  figma.viewport.scrollAndZoomIntoView([item])

  figma.notify('Transaction Item generated!')
}

async function generateQuickActionsComponent(): Promise<void> {
  const actions = await createQuickActions({
    actions: [
      { icon: '💸', label: 'Transfer' },
      { icon: '📥', label: 'Deposit' },
      { icon: '📊', label: 'Invest' },
      { icon: '👥', label: 'Beneficiary' },
    ],
  })

  actions.x = figma.viewport.center.x - 180
  actions.y = figma.viewport.center.y - 50

  figma.currentPage.appendChild(actions)
  figma.currentPage.selection = [actions]
  figma.viewport.scrollAndZoomIntoView([actions])

  figma.notify('Quick Actions generated!')
}

async function generateTopBarComponent(): Promise<void> {
  const topBar = await createTopBar({
    title: 'Dashboard',
    showBackButton: false,
    actions: [
      { icon: '🔔', label: 'Notifications' },
      { icon: '⚙️', label: 'Settings' },
    ],
  })

  topBar.x = figma.viewport.center.x - 196
  topBar.y = figma.viewport.center.y - 32

  figma.currentPage.appendChild(topBar)
  figma.currentPage.selection = [topBar]
  figma.viewport.scrollAndZoomIntoView([topBar])

  figma.notify('Top Bar generated!')
}

async function generateBottomNavComponent(): Promise<void> {
  const bottomNav = await createBottomNav({
    items: [
      { icon: '🏠', label: 'Home' },
      { icon: '💳', label: 'Accounts' },
      { icon: '💸', label: 'Transfer' },
      { icon: '📊', label: 'Activity' },
      { icon: '👤', label: 'Profile' },
    ],
    selectedIndex: 0,
  })

  bottomNav.x = figma.viewport.center.x - 196
  bottomNav.y = figma.viewport.center.y - 40

  figma.currentPage.appendChild(bottomNav)
  figma.currentPage.selection = [bottomNav]
  figma.viewport.scrollAndZoomIntoView([bottomNav])

  figma.notify('Bottom Navigation generated!')
}

async function generateInputFieldComponent(): Promise<void> {
  const input = await createInputField({
    label: 'Email',
    placeholder: 'Enter your email',
    variant: 'outlined',
  })

  input.x = figma.viewport.center.x - 180
  input.y = figma.viewport.center.y - 28

  figma.currentPage.appendChild(input)
  figma.currentPage.selection = [input]
  figma.viewport.scrollAndZoomIntoView([input])

  figma.notify('Input Field generated!')
}

async function generateButtonComponent(): Promise<void> {
  const container = figma.createFrame()
  container.name = 'ButtonVariants'
  container.fills = []
  setAutoLayout(container, {
    direction: 'VERTICAL',
    gap: 16,
  })

  const variants: Array<'filled' | 'outlined' | 'text' | 'elevated' | 'tonal'> = [
    'filled', 'outlined', 'text', 'elevated', 'tonal'
  ]

  for (const variant of variants) {
    const button = await createButton({
      label: variant.toUpperCase(),
      variant,
    })
    container.appendChild(button)
  }

  container.x = figma.viewport.center.x - 80
  container.y = figma.viewport.center.y - 140

  figma.currentPage.appendChild(container)
  figma.currentPage.selection = [container]
  figma.viewport.scrollAndZoomIntoView([container])

  figma.notify('Button variants generated!')
}

// ============================================================================
// DESIGN SYSTEM GENERATORS
// ============================================================================

async function generateColorPalette(): Promise<void> {
  const palette = figma.createFrame()
  palette.name = 'Color Palette'
  palette.fills = [solidPaint(Colors.surface)]

  setAutoLayout(palette, {
    direction: 'VERTICAL',
    padding: 24,
    gap: 16,
  })

  const title = await createText('Color Palette', {
    ...Typography.headlineMedium,
    color: Colors.onSurface,
  })
  palette.appendChild(title)

  const colorGroups = [
    { name: 'Primary', colors: [Colors.primary, Colors.onPrimary, Colors.primaryContainer, Colors.onPrimaryContainer] },
    { name: 'Secondary', colors: [Colors.secondary, Colors.onSecondary, Colors.secondaryContainer, Colors.onSecondaryContainer] },
    { name: 'Surface', colors: [Colors.surface, Colors.onSurface, Colors.surfaceVariant, Colors.onSurfaceVariant] },
    { name: 'Error', colors: [Colors.error, Colors.onError, Colors.errorContainer, Colors.onErrorContainer] },
  ]

  for (const group of colorGroups) {
    const row = figma.createFrame()
    row.name = group.name
    row.fills = []
    setAutoLayout(row, {
      direction: 'HORIZONTAL',
      gap: 8,
    })

    for (const color of group.colors) {
      const swatch = figma.createFrame()
      swatch.name = color
      swatch.resize(60, 60)
      swatch.cornerRadius = 8
      swatch.fills = [solidPaint(color)]
      swatch.strokes = [solidPaint(Colors.outline)]
      swatch.strokeWeight = 1
      row.appendChild(swatch)
    }

    palette.appendChild(row)
  }

  palette.x = figma.viewport.center.x - palette.width / 2
  palette.y = figma.viewport.center.y - palette.height / 2

  figma.currentPage.appendChild(palette)
  figma.currentPage.selection = [palette]
  figma.viewport.scrollAndZoomIntoView([palette])

  figma.notify('Color Palette generated!')
}

async function generateTypographyScale(): Promise<void> {
  const scale = figma.createFrame()
  scale.name = 'Typography Scale'
  scale.fills = [solidPaint(Colors.surface)]

  setAutoLayout(scale, {
    direction: 'VERTICAL',
    padding: 24,
    gap: 16,
  })

  const styles = [
    { name: 'Display Large', style: Typography.displayLarge },
    { name: 'Headline Medium', style: Typography.headlineMedium },
    { name: 'Title Large', style: Typography.titleLarge },
    { name: 'Body Large', style: Typography.bodyLarge },
    { name: 'Label Medium', style: Typography.labelMedium },
  ]

  for (const item of styles) {
    const text = await createText(`${item.name} - ${item.style.fontSize}px`, {
      ...item.style,
      color: Colors.onSurface,
    })
    scale.appendChild(text)
  }

  scale.x = figma.viewport.center.x - scale.width / 2
  scale.y = figma.viewport.center.y - scale.height / 2

  figma.currentPage.appendChild(scale)
  figma.currentPage.selection = [scale]
  figma.viewport.scrollAndZoomIntoView([scale])

  figma.notify('Typography Scale generated!')
}

async function generateSpacingGrid(): Promise<void> {
  const grid = figma.createFrame()
  grid.name = 'Spacing Grid'
  grid.fills = [solidPaint(Colors.surface)]

  setAutoLayout(grid, {
    direction: 'VERTICAL',
    padding: 24,
    gap: 16,
  })

  const title = await createText('Spacing Scale', {
    ...Typography.headlineMedium,
    color: Colors.onSurface,
  })
  grid.appendChild(title)

  const spacings = [
    { name: 'xs', value: Spacing.xs },
    { name: 'sm', value: Spacing.sm },
    { name: 'md', value: Spacing.md },
    { name: 'lg', value: Spacing.lg },
    { name: 'xl', value: Spacing.xl },
  ]

  for (const spacing of spacings) {
    const row = figma.createFrame()
    row.name = spacing.name
    row.fills = []
    setAutoLayout(row, {
      direction: 'HORIZONTAL',
      gap: 16,
      crossAlignment: 'CENTER',
    })

    const label = await createText(`${spacing.name} (${spacing.value}px)`, {
      ...Typography.bodyMedium,
      color: Colors.onSurface,
    })
    label.resize(100, 20)
    row.appendChild(label)

    const bar = figma.createFrame()
    bar.name = 'Bar'
    bar.resize(spacing.value * 4, 24)
    bar.cornerRadius = 4
    bar.fills = [solidPaint(Colors.primary)]
    row.appendChild(bar)

    grid.appendChild(row)
  }

  grid.x = figma.viewport.center.x - grid.width / 2
  grid.y = figma.viewport.center.y - grid.height / 2

  figma.currentPage.appendChild(grid)
  figma.currentPage.selection = [grid]
  figma.viewport.scrollAndZoomIntoView([grid])

  figma.notify('Spacing Grid generated!')
}
