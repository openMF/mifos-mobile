# Mockup Layer - Status & Memory

> **Layer**: Design Mockup Generation
> **Command**: `/mockup [Feature]`
> **Location**: `claude-product-cycle/mockup-layer/`

---

## Current Status

| Component | Count | Status |
|-----------|-------|--------|
| Figma Plugin | 1 | Setup Complete |
| Design Tokens | 1 | Complete |
| Component Generators | 8 | Complete |
| Scripts | 3 | Complete |
| Skills | 1 | Complete |

---

## Layer Structure

```
mockup-layer/
├── LAYER_STATUS.md           # This file
├── README.md                 # Usage guide
├── figma-plugin/             # Figma plugin project
│   ├── package.json
│   ├── manifest.json
│   ├── tsconfig.json
│   └── src/
│       ├── plugin.ts         # Main plugin entry
│       ├── design-system/    # Design tokens
│       │   ├── index.ts
│       │   ├── tokens.ts
│       │   ├── colors.ts
│       │   ├── typography.ts
│       │   └── spacing.ts
│       ├── generators/       # Component generators
│       │   ├── index.ts
│       │   └── components/
│       │       ├── screen.ts
│       │       ├── top-bar.ts
│       │       ├── card.ts
│       │       ├── button.ts
│       │       ├── list-item.ts
│       │       ├── input-field.ts
│       │       ├── bottom-nav.ts
│       │       └── quick-actions.ts
│       └── utils/
│           ├── helpers.ts
│           └── spec-parser.ts
├── scripts/
│   ├── generate-prompts.ts   # SPEC → AI prompts
│   ├── build-plugin.ts       # Build Figma plugin
│   └── sync-designs.ts       # Sync to Figma
└── templates/
    ├── ai-prompt.md          # Template for AI tools
    └── component.ts          # Template for generators
```

---

## Workflow

```
┌─────────────────────────────────────────────────────────────────┐
│                    /mockup WORKFLOW                              │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  INPUT: /mockup [feature]                                        │
│                                                                  │
│  STEP 1: READ SPEC                                              │
│  ├─→ Read features/[feature]/SPEC.md                            │
│  ├─→ Parse ASCII mockups                                         │
│  └─→ Extract component definitions                               │
│                                                                  │
│  STEP 2: GENERATE OUTPUT (Choose one)                           │
│  ├─→ Option A: Figma plugin code (.ts)                          │
│  ├─→ Option B: AI tool prompts (PROMPTS.md)                     │
│  └─→ Option C: Design tokens (tokens.json)                      │
│                                                                  │
│  STEP 3: OUTPUT TO                                              │
│  └─→ features/[feature]/mockups/                                │
│                                                                  │
│  STEP 4: USER ACTION                                            │
│  ├─→ Run Figma plugin in Figma Desktop                          │
│  └─→ OR paste prompts in Google Stitch/Uizard                   │
│                                                                  │
│  STEP 5: EXPORT & CONNECT                                       │
│  ├─→ Design ready in Figma                                      │
│  └─→ Figma MCP available for /implement                         │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

## Feature Mockup Status

| Feature | Plugin | Prompts | Tokens | Figma File |
|---------|--------|---------|--------|------------|
| dashboard | Planned | Planned | Planned | - |
| auth | - | - | - | - |
| home | - | - | - | - |
| accounts | - | - | - | - |

---

## Design System

### Material Design 3 Tokens

| Category | Tokens | Status |
|----------|--------|--------|
| Colors | Primary, Secondary, Surface, Error | Complete |
| Typography | Display, Headline, Title, Body, Label | Complete |
| Spacing | xs, sm, md, lg, xl | Complete |
| Radius | sm, md, lg, xl | Complete |
| Elevation | level0-5 | Complete |

---

## Component Generators

| Component | Generator | Usage |
|-----------|-----------|-------|
| Screen | `createScreen()` | Base mobile screen frame |
| TopBar | `createTopBar()` | App bar with title/icons |
| Card | `createCard()` | Content card with variants |
| Button | `createButton()` | Primary/secondary/text buttons |
| ListItem | `createListItem()` | List row with icon/text/action |
| InputField | `createInputField()` | Text input with label |
| BottomNav | `createBottomNav()` | Bottom navigation bar |
| QuickActions | `createQuickActions()` | Horizontal action buttons |

---

## Build Commands

```bash
# Install dependencies
cd claude-product-cycle/mockup-layer/figma-plugin
npm install

# Build plugin
npm run build

# Watch mode (development)
npm run watch

# Generate prompts for a feature
npx ts-node ../scripts/generate-prompts.ts dashboard
```

---

## Related Docs

- Design Specs: `claude-product-cycle/design-spec-layer/features/`
- Skill Definition: `claude-product-cycle/commands-layer/skills/mockup.md`
- Figma Plugin API: https://www.figma.com/plugin-docs/

---

## Changelog

| Date | Change |
|------|--------|
| 2025-12-28 | Created mockup-layer with Figma plugin structure |
