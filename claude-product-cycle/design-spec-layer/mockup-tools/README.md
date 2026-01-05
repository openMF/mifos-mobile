# Mockup Layer

> Generate design mockups from feature specifications

## Overview

The Mockup Layer provides tools to generate visual design mockups from SPEC.md files. It supports two approaches:

1. **Figma Plugin** - Programmatically generate designs in Figma
2. **AI Prompts** - Generate prompts for Google Stitch, Uizard, or Visily

## Quick Start

### Option A: Using Figma Plugin

```bash
# 1. Install dependencies
cd figma-plugin
npm install

# 2. Build the plugin
npm run build

# 3. In Figma Desktop:
#    - Plugins → Development → Import plugin from manifest
#    - Select: mockup-layer/figma-plugin/manifest.json

# 4. Run the plugin
#    - Plugins → Development → Mifos Mockup Generator
```

### Option B: Using AI Prompts

```bash
# Generate prompts for a feature
/mockup dashboard prompts

# Copy the generated prompts from:
# features/dashboard/mockups/PROMPTS.md

# Paste into Google Stitch or Uizard
```

## Commands

| Command | Description |
|---------|-------------|
| `/mockup` | Show available features |
| `/mockup [feature]` | Generate Figma plugin code |
| `/mockup [feature] prompts` | Generate AI tool prompts |
| `/mockup [feature] tokens` | Generate design tokens JSON |
| `/mockup sync` | Sync all mockups status |

## Design System

The mockup layer uses Material Design 3 tokens:

### Colors
```typescript
primary: '#6750A4'      // Purple 500
onPrimary: '#FFFFFF'
primaryContainer: '#EADDFF'
secondary: '#625B71'
surface: '#FFFBFE'
surfaceVariant: '#E7E0EC'
error: '#B3261E'
success: '#2E7D32'
```

### Typography
```typescript
displayLarge:   { size: 57, weight: 400 }
headlineMedium: { size: 28, weight: 400 }
titleLarge:     { size: 22, weight: 400 }
bodyLarge:      { size: 16, weight: 400 }
labelMedium:    { size: 12, weight: 500 }
```

### Spacing
```typescript
xs: 4, sm: 8, md: 16, lg: 24, xl: 32
```

## Workflow Integration

```
┌─────────────────────────────────────────────────────────────┐
│  /design dashboard                                          │
│       ↓                                                     │
│  SPEC.md with ASCII mockups                                 │
│       ↓                                                     │
│  /mockup dashboard                                          │
│       ↓                                                     │
│  Figma plugin OR AI prompts generated                       │
│       ↓                                                     │
│  Run in Figma / Paste in AI tool                           │
│       ↓                                                     │
│  Export to Figma (if using AI tool)                        │
│       ↓                                                     │
│  Connect Figma MCP                                          │
│       ↓                                                     │
│  /implement dashboard                                       │
│       ↓                                                     │
│  Compose/SwiftUI code generated                            │
└─────────────────────────────────────────────────────────────┘
```

## File Structure

```
mockup-layer/
├── figma-plugin/           # Figma plugin source
│   ├── src/
│   │   ├── plugin.ts       # Main entry point
│   │   ├── design-system/  # Material Design 3 tokens
│   │   └── generators/     # Component generators
│   └── manifest.json       # Figma plugin manifest
├── scripts/                # Build & generation scripts
└── templates/              # Prompt templates
```

## Component Generators

The Figma plugin includes generators for common components:

| Generator | Description |
|-----------|-------------|
| `createScreen(title)` | Mobile screen frame (390x844) |
| `createTopBar(title, icons)` | App bar with navigation |
| `createCard(title, content)` | Material card component |
| `createButton(label, variant)` | Button (primary/secondary/text) |
| `createListItem(icon, title, subtitle)` | List row |
| `createInputField(label, placeholder)` | Text input |
| `createBottomNav(items)` | Bottom navigation bar |
| `createQuickActions(actions)` | Horizontal action row |

## AI Tool Prompts

Generated prompts are optimized for:

- **Google Stitch** - Best for Material Design
- **Uizard** - Good for quick iterations
- **Visily** - Good Figma integration

Example prompt structure:
```
Create a [screen type] with Material Design 3:

- [Component 1]: [Description]
- [Component 2]: [Description]
...

Style: Clean, modern, Material Design 3
Colors: Purple/blue gradient accents
Platform: Android mobile app
```

## Troubleshooting

### Figma Plugin Issues

1. **Plugin not loading**: Ensure you're using Figma Desktop (not web)
2. **TypeScript errors**: Run `npm run build` to check for errors
3. **Missing fonts**: The plugin uses Inter font by default

### AI Tool Issues

1. **Low quality output**: Try adding more specific details to prompts
2. **Wrong style**: Explicitly mention "Material Design 3" in prompts
3. **Missing components**: Break down complex screens into sections

## Related Documentation

- [Figma Plugin API](https://www.figma.com/plugin-docs/)
- [Material Design 3](https://m3.material.io/)
- [Google Stitch](https://stitch.withgoogle.com/)
