/**
 * Screen Frame Generator
 * Creates base mobile screen frames
 */

import { Colors, Screens, ComponentSizes } from '../../design-system/tokens'
import { solidPaint, setAutoLayout } from '../../utils/helpers'

export interface ScreenOptions {
  name: string
  size?: 'mobile' | 'mobileLarge' | 'tablet'
  backgroundColor?: string
  hasTopBar?: boolean
  hasBottomNav?: boolean
  statusBarStyle?: 'light' | 'dark'
}

/**
 * Create a mobile screen frame
 */
export function createScreen(options: ScreenOptions): FrameNode {
  const {
    name,
    size = 'mobile',
    backgroundColor = Colors.surface,
    hasTopBar = true,
    hasBottomNav = true,
    statusBarStyle = 'dark',
  } = options

  const dimensions = Screens[size]
  const screen = figma.createFrame()

  screen.name = name
  screen.resize(dimensions.width, dimensions.height)
  screen.fills = [solidPaint(backgroundColor)]

  // Set up auto-layout
  setAutoLayout(screen, {
    direction: 'VERTICAL',
    padding: 0,
    gap: 0,
  })

  // Create status bar
  const statusBar = createStatusBar(dimensions.width, statusBarStyle)
  screen.appendChild(statusBar)

  // Create content area
  const contentArea = figma.createFrame()
  contentArea.name = 'Content'
  contentArea.layoutSizingHorizontal = 'FILL'
  contentArea.layoutSizingVertical = 'FILL'
  contentArea.fills = []

  setAutoLayout(contentArea, {
    direction: 'VERTICAL',
    padding: 0,
    gap: 0,
  })

  screen.appendChild(contentArea)

  return screen
}

/**
 * Create status bar
 */
function createStatusBar(width: number, style: 'light' | 'dark'): FrameNode {
  const statusBar = figma.createFrame()
  statusBar.name = 'StatusBar'
  statusBar.resize(width, 54)
  statusBar.fills = []
  statusBar.layoutSizingHorizontal = 'FILL'

  setAutoLayout(statusBar, {
    direction: 'HORIZONTAL',
    padding: { top: 14, right: 24, bottom: 10, left: 24 },
    alignment: 'SPACE_BETWEEN',
    crossAlignment: 'CENTER',
  })

  const textColor = style === 'dark' ? Colors.onSurface : Colors.onPrimary

  // Time placeholder
  const time = figma.createFrame()
  time.name = 'Time'
  time.resize(54, 21)
  time.fills = []
  statusBar.appendChild(time)

  // Icons placeholder (right side)
  const icons = figma.createFrame()
  icons.name = 'StatusIcons'
  icons.resize(77, 17)
  icons.fills = []
  statusBar.appendChild(icons)

  return statusBar
}

/**
 * Create scrollable content wrapper
 */
export function createScrollableContent(name: string = 'ScrollContent'): FrameNode {
  const scroll = figma.createFrame()
  scroll.name = name
  scroll.fills = []
  scroll.layoutSizingHorizontal = 'FILL'
  scroll.layoutSizingVertical = 'FILL'
  scroll.clipsContent = true

  setAutoLayout(scroll, {
    direction: 'VERTICAL',
    padding: { top: 0, right: 16, bottom: 16, left: 16 },
    gap: 16,
  })

  return scroll
}
