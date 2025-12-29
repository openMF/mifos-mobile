/**
 * Input Field Component Generator
 * Creates Material Design 3 text fields
 */

import { Colors, Typography, Spacing, Radius, ComponentSizes } from '../../design-system/tokens'
import { solidPaint, setAutoLayout, createText, createIconPlaceholder } from '../../utils/helpers'

export type InputVariant = 'filled' | 'outlined'
export type InputState = 'default' | 'focused' | 'error' | 'disabled'

export interface InputFieldOptions {
  label: string
  placeholder?: string
  value?: string
  variant?: InputVariant
  state?: InputState
  leadingIcon?: string
  trailingIcon?: string
  supportingText?: string
  errorText?: string
  prefix?: string
  suffix?: string
  characterCount?: { current: number; max: number }
}

/**
 * Create a text input field
 */
export async function createInputField(options: InputFieldOptions): Promise<FrameNode> {
  const {
    label,
    placeholder = '',
    value = '',
    variant = 'outlined',
    state = 'default',
    leadingIcon,
    trailingIcon,
    supportingText,
    errorText,
    prefix,
    suffix,
    characterCount,
  } = options

  const container = figma.createFrame()
  container.name = `InputField-${label}`
  container.layoutSizingHorizontal = 'FILL'
  container.fills = []

  setAutoLayout(container, {
    direction: 'VERTICAL',
    gap: 4,
  })

  // Input container
  const inputContainer = figma.createFrame()
  inputContainer.name = 'InputContainer'
  inputContainer.layoutSizingHorizontal = 'FILL'
  inputContainer.resize(100, ComponentSizes.inputHeight)

  const isError = state === 'error'
  const isFocused = state === 'focused'
  const isDisabled = state === 'disabled'

  // Styling based on variant and state
  if (variant === 'filled') {
    inputContainer.fills = [solidPaint(
      isDisabled ? Colors.onSurface : Colors.surfaceContainerHighest
    )]
    inputContainer.opacity = isDisabled ? 0.04 : 1
    inputContainer.cornerRadius = 4
    // Bottom border
    const bottomBorder = figma.createFrame()
    bottomBorder.name = 'BottomBorder'
    bottomBorder.layoutSizingHorizontal = 'FILL'
    bottomBorder.resize(100, isFocused ? 2 : 1)
    bottomBorder.fills = [solidPaint(
      isError ? Colors.error : isFocused ? Colors.primary : Colors.onSurfaceVariant
    )]
    bottomBorder.y = ComponentSizes.inputHeight - (isFocused ? 2 : 1)
    inputContainer.appendChild(bottomBorder)
  } else {
    // Outlined
    inputContainer.fills = []
    inputContainer.strokes = [solidPaint(
      isError ? Colors.error : isFocused ? Colors.primary : Colors.outline
    )]
    inputContainer.strokeWeight = isFocused ? 2 : 1
    inputContainer.cornerRadius = 4
  }

  setAutoLayout(inputContainer, {
    direction: 'HORIZONTAL',
    padding: { top: 8, right: 16, bottom: 8, left: leadingIcon ? 12 : 16 },
    gap: 16,
    crossAlignment: 'CENTER',
  })

  // Leading icon
  if (leadingIcon) {
    const icon = createIconPlaceholder(
      leadingIcon.charAt(0),
      24,
      'transparent',
      isDisabled ? Colors.onSurface : Colors.onSurfaceVariant
    )
    if (isDisabled) icon.opacity = 0.38
    inputContainer.appendChild(icon)
  }

  // Text content area
  const textArea = figma.createFrame()
  textArea.name = 'TextArea'
  textArea.layoutSizingHorizontal = 'FILL'
  textArea.fills = []

  setAutoLayout(textArea, {
    direction: 'VERTICAL',
    gap: 0,
  })

  // Label (floating when has value or focused)
  const hasContent = value.length > 0 || isFocused
  const labelText = await createText(label, {
    ...(hasContent ? Typography.bodySmall : Typography.bodyLarge),
    color: isError ? Colors.error :
           isFocused ? Colors.primary :
           isDisabled ? Colors.onSurface :
           Colors.onSurfaceVariant,
  })
  if (isDisabled) labelText.opacity = 0.38
  textArea.appendChild(labelText)

  // Value or placeholder
  if (hasContent && value) {
    const valueRow = figma.createFrame()
    valueRow.name = 'ValueRow'
    valueRow.layoutSizingHorizontal = 'FILL'
    valueRow.fills = []
    setAutoLayout(valueRow, {
      direction: 'HORIZONTAL',
      gap: 0,
    })

    if (prefix) {
      const prefixText = await createText(prefix, {
        ...Typography.bodyLarge,
        color: Colors.onSurfaceVariant,
      })
      valueRow.appendChild(prefixText)
    }

    const valueText = await createText(value, {
      ...Typography.bodyLarge,
      color: isDisabled ? Colors.onSurface : Colors.onSurface,
    })
    valueText.layoutSizingHorizontal = 'FILL'
    if (isDisabled) valueText.opacity = 0.38
    valueRow.appendChild(valueText)

    if (suffix) {
      const suffixText = await createText(suffix, {
        ...Typography.bodyLarge,
        color: Colors.onSurfaceVariant,
      })
      valueRow.appendChild(suffixText)
    }

    textArea.appendChild(valueRow)
  } else if (!hasContent && placeholder) {
    const placeholderText = await createText(placeholder, {
      ...Typography.bodyLarge,
      color: Colors.onSurfaceVariant,
    })
    placeholderText.opacity = 0.6
    textArea.appendChild(placeholderText)
  }

  inputContainer.appendChild(textArea)

  // Trailing icon
  if (trailingIcon) {
    const icon = createIconPlaceholder(
      trailingIcon.charAt(0),
      24,
      'transparent',
      isError ? Colors.error :
      isDisabled ? Colors.onSurface :
      Colors.onSurfaceVariant
    )
    if (isDisabled) icon.opacity = 0.38
    inputContainer.appendChild(icon)
  }

  container.appendChild(inputContainer)

  // Supporting/error text and character count
  if (supportingText || errorText || characterCount) {
    const bottomRow = figma.createFrame()
    bottomRow.name = 'BottomRow'
    bottomRow.layoutSizingHorizontal = 'FILL'
    bottomRow.fills = []

    setAutoLayout(bottomRow, {
      direction: 'HORIZONTAL',
      padding: { top: 0, right: 16, bottom: 0, left: 16 },
      alignment: 'SPACE_BETWEEN',
    })

    const supportText = errorText || supportingText
    if (supportText) {
      const text = await createText(supportText, {
        ...Typography.bodySmall,
        color: isError ? Colors.error : Colors.onSurfaceVariant,
      })
      bottomRow.appendChild(text)
    }

    if (characterCount) {
      const countText = await createText(`${characterCount.current}/${characterCount.max}`, {
        ...Typography.bodySmall,
        color: Colors.onSurfaceVariant,
      })
      bottomRow.appendChild(countText)
    }

    container.appendChild(bottomRow)
  }

  return container
}

/**
 * Create a search bar
 */
export async function createSearchBar(options: {
  placeholder?: string
  value?: string
  showLeadingIcon?: boolean
  showTrailingIcon?: boolean
}): Promise<FrameNode> {
  const {
    placeholder = 'Search',
    value = '',
    showLeadingIcon = true,
    showTrailingIcon = false,
  } = options

  const searchBar = figma.createFrame()
  searchBar.name = 'SearchBar'
  searchBar.layoutSizingHorizontal = 'FILL'
  searchBar.resize(100, 56)
  searchBar.cornerRadius = 28
  searchBar.fills = [solidPaint(Colors.surfaceContainerHigh)]

  setAutoLayout(searchBar, {
    direction: 'HORIZONTAL',
    padding: { top: 8, right: 16, bottom: 8, left: 16 },
    gap: 16,
    crossAlignment: 'CENTER',
  })

  // Leading icon (search)
  if (showLeadingIcon) {
    const searchIcon = createIconPlaceholder('🔍', 24, 'transparent', Colors.onSurface)
    searchBar.appendChild(searchIcon)
  }

  // Text
  const textContent = value || placeholder
  const textColor = value ? Colors.onSurface : Colors.onSurfaceVariant
  const text = await createText(textContent, {
    ...Typography.bodyLarge,
    color: textColor,
  })
  text.layoutSizingHorizontal = 'FILL'
  searchBar.appendChild(text)

  // Trailing icon (clear or avatar)
  if (showTrailingIcon || value) {
    const clearIcon = createIconPlaceholder('✕', 24, 'transparent', Colors.onSurfaceVariant)
    searchBar.appendChild(clearIcon)
  }

  return searchBar
}

/**
 * Create a dropdown/select field
 */
export async function createDropdown(options: {
  label: string
  value?: string
  placeholder?: string
  variant?: InputVariant
  state?: InputState
}): Promise<FrameNode> {
  return createInputField({
    ...options,
    trailingIcon: '▼',
  })
}
