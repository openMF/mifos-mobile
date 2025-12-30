# /mockup - Design Mockup Generation

## Purpose
Generate visual design mockups from feature specifications using Figma plugin or AI tool prompts.

---

## Command Variants

```
/mockup                     → Show available features
/mockup [Feature]           → Generate Figma plugin code
/mockup [Feature] prompts   → Generate AI tool prompts
/mockup [Feature] tokens    → Generate design tokens JSON
/mockup sync                → Update mockup status across features
```

---

## Model Recommendation

**This command works well with both Sonnet and Opus**. Sonnet for quick prompt generation, Opus for complex multi-screen mockups.

---

## MCP Connection Check (IMPORTANT)

Before generating mockups, CHECK if Figma MCP is connected. If not, prompt user:

```
┌───────────────────────────────────────────────────────────────────┐
│  ⚠️  FIGMA MCP NOT CONNECTED                                      │
├───────────────────────────────────────────────────────────────────┤
│                                                                    │
│  Figma MCP enables:                                               │
│  • Direct design-to-code workflow                                 │
│  • Reading Figma frames for implementation                        │
│  • Better design accuracy                                         │
│                                                                    │
│  OPTIONS:                                                         │
│                                                                    │
│  [1] Connect Figma MCP now (Recommended)                          │
│      → Run: claude mcp add figma https://mcp.figma.com/mcp        │
│      → Requires Figma account authentication                      │
│                                                                    │
│  [2] Skip - Continue with manual workflow                         │
│      → Generate prompts/plugin code                               │
│      → Manually export to Figma later                             │
│      → Connect MCP before /implement                              │
│                                                                    │
│  [3] Cancel                                                       │
│                                                                    │
└───────────────────────────────────────────────────────────────────┘

Which option do you prefer?
```

### Connection Commands Reference

```bash
# Figma MCP (for design-to-code)
claude mcp add figma https://mcp.figma.com/mcp

# Check connected MCPs
claude mcp list

# Remove MCP
claude mcp remove figma
```

### When to Check MCP

| Command Variant | MCP Required | Check Timing |
|-----------------|--------------|--------------|
| `/mockup [feature]` | Optional | After generation |
| `/mockup [feature] prompts` | Optional | After generation |
| `/mockup [feature] tokens` | No | Skip check |
| Before `/implement` | Yes | Must connect |

---

## Key Files

```
claude-product-cycle/mockup-layer/
├── LAYER_STATUS.md                    # Mockup layer status
├── README.md                          # Usage documentation
├── figma-plugin/                      # Figma plugin source
│   ├── package.json
│   ├── manifest.json
│   └── src/
│       ├── plugin.ts                  # Main plugin entry
│       ├── design-system/             # Material Design 3 tokens
│       │   └── tokens.ts
│       ├── generators/                # Component generators
│       │   └── components/
│       └── utils/
│           └── helpers.ts
├── scripts/                           # Build scripts
└── templates/                         # Prompt templates

claude-product-cycle/design-spec-layer/features/[feature]/
├── SPEC.md                            # Source specification
├── API.md                             # API documentation
└── mockups/                           # Generated mockups (NEW)
    ├── PROMPTS.md                     # AI tool prompts
    ├── figma-generator.ts             # Feature-specific Figma code
    └── design-tokens.json             # Feature design tokens
```

---

## Workflow

```
┌───────────────────────────────────────────────────────────────────┐
│                    /mockup [Feature] WORKFLOW                      │
├───────────────────────────────────────────────────────────────────┤
│                                                                    │
│  STEP 1: READ SPECIFICATION                                       │
│  ├─→ Read features/[feature]/SPEC.md                              │
│  ├─→ Parse ASCII mockups section                                  │
│  ├─→ Extract component definitions                                │
│  └─→ Identify screen layout structure                             │
│                                                                    │
│  STEP 2: ANALYZE COMPONENTS                                       │
│  ├─→ Map ASCII elements to Material Design 3 components           │
│  ├─→ Identify custom components needed                            │
│  ├─→ Determine color/typography tokens                            │
│  └─→ List required generators                                     │
│                                                                    │
│  STEP 3: GENERATE OUTPUT (based on variant)                       │
│  │                                                                 │
│  │  [Default: Figma Plugin]                                       │
│  │  ├─→ Generate TypeScript code using generators                 │
│  │  ├─→ Output to features/[feature]/mockups/figma-generator.ts   │
│  │  └─→ Instructions to run in Figma                              │
│  │                                                                 │
│  │  [prompts variant]                                             │
│  │  ├─→ Generate detailed prompts for AI tools                    │
│  │  ├─→ Output to features/[feature]/mockups/PROMPTS.md           │
│  │  └─→ Include Google Stitch, Uizard, Visily formats             │
│  │                                                                 │
│  │  [tokens variant]                                              │
│  │  ├─→ Extract design tokens from SPEC                           │
│  │  ├─→ Output to features/[feature]/mockups/design-tokens.json   │
│  │  └─→ Include colors, typography, spacing                       │
│  │                                                                 │
│  STEP 4: UPDATE STATUS                                            │
│  ├─→ Update mockup-layer/LAYER_STATUS.md                          │
│  └─→ Add mockup status to feature table                           │
│                                                                    │
│  STEP 5: OUTPUT NEXT STEPS                                        │
│  └─→ Instructions to use generated output                         │
│                                                                    │
└───────────────────────────────────────────────────────────────────┘
```

---

## PROMPTS.md Template

```markdown
# [Feature Name] - AI Mockup Prompts

> Generated from: features/[feature]/SPEC.md
> Generated on: [Date]

---

## Screen 1: [Screen Name]

### Google Stitch / Visily Prompt

```
Create a [screen type] mobile screen with Material Design 3:

**Header:**
- [Component description]

**Main Content:**
- [Section 1]: [Description]
- [Section 2]: [Description]

**Footer:**
- [Component description]

**Style Guidelines:**
- Color scheme: Purple primary (#6750A4), white surface
- Typography: Inter font family
- Spacing: 16px standard padding
- Platform: Android mobile app (393x852)
```

### Uizard Prompt

```
Design a mobile banking app screen showing:
1. [Key element 1]
2. [Key element 2]
3. [Key element 3]

Style: Modern, clean, Material Design 3
Colors: Purple accent on white background
```

---

## Component Prompts

### [Component Name]
```
[Detailed prompt for individual component]
```

---

## Export Instructions

1. Generate design in [AI Tool]
2. Export to Figma (if available)
3. Connect Figma MCP to Claude Code
4. Run /implement [feature] for code generation
```

---

## figma-generator.ts Template

```typescript
/**
 * [Feature Name] - Figma Mockup Generator
 * Generated from: features/[feature]/SPEC.md
 */

import {
  createScreen,
  createTopBar,
  createBottomNav,
  createCard,
  // ... other imports
} from '../../mockup-layer/figma-plugin/src/generators'

export async function generate[Feature]Screen(): Promise<FrameNode> {
  const screen = createScreen({
    name: '[Feature] Screen',
    hasTopBar: true,
    hasBottomNav: true,
  })

  // Add components based on SPEC.md
  // ...

  return screen
}
```

---

## design-tokens.json Template

```json
{
  "feature": "[Feature Name]",
  "generated": "[Date]",
  "tokens": {
    "colors": {
      "primary": "#6750A4",
      "surface": "#FFFBFE",
      "custom": {}
    },
    "typography": {
      "headline": { "size": 28, "weight": 400 },
      "body": { "size": 16, "weight": 400 }
    },
    "spacing": {
      "screen": 16,
      "section": 24,
      "component": 8
    },
    "components": [
      {
        "name": "[Component]",
        "type": "card|list|button|input",
        "properties": {}
      }
    ]
  }
}
```

---

## Output Template

After generating mockup assets, output:

```
┌───────────────────────────────────────────────────────────────────┐
│            MOCKUP GENERATION COMPLETE                              │
├───────────────────────────────────────────────────────────────────┤
│                                                                    │
│  FEATURE: [Feature Name]                                          │
│  SOURCE: features/[feature]/SPEC.md                               │
│                                                                    │
│  ════════════════════════════════════════════════════════════════ │
│                                                                    │
│  GENERATED FILES:                                                 │
│  ✅ features/[feature]/mockups/PROMPTS.md                         │
│  ✅ features/[feature]/mockups/figma-generator.ts                 │
│  ✅ features/[feature]/mockups/design-tokens.json                 │
│                                                                    │
│  ════════════════════════════════════════════════════════════════ │
│                                                                    │
│  OPTION A: FIGMA PLUGIN                                           │
│  1. cd claude-product-cycle/mockup-layer/figma-plugin             │
│  2. npm install && npm run build                                  │
│  3. In Figma: Plugins → Development → Import manifest             │
│  4. Run: Plugins → Mifos Mockup Generator → Generate [Feature]    │
│                                                                    │
│  OPTION B: AI TOOL                                                │
│  1. Open features/[feature]/mockups/PROMPTS.md                    │
│  2. Copy prompt for Google Stitch / Uizard / Visily               │
│  3. Generate and export to Figma                                  │
│                                                                    │
│  ════════════════════════════════════════════════════════════════ │
│                                                                    │
│  AFTER MOCKUP IS IN FIGMA:                                        │
│  1. Connect Figma MCP: claude mcp add figma https://mcp.figma.com │
│  2. Run: /implement [feature]                                     │
│                                                                    │
└───────────────────────────────────────────────────────────────────┘
```

---

## If No Feature Name Provided

Show available features:

```
🎨 FEATURES AVAILABLE FOR MOCKUP GENERATION:

| Feature | SPEC | Mockup Status | Command |
|---------|------|---------------|---------|
| dashboard | ✅ | 📋 Planned | /mockup dashboard |
| auth | ✅ | - | /mockup auth |
| home | ✅ | - | /mockup home |
| accounts | ✅ | - | /mockup accounts |
| ...

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

COMMAND VARIANTS:
  /mockup [feature]           Generate Figma plugin code
  /mockup [feature] prompts   Generate AI tool prompts
  /mockup [feature] tokens    Generate design tokens JSON

Which feature do you want to generate mockups for?
```

---

## Component Mapping Reference

| ASCII Element | Material Design 3 | Generator Function |
|---------------|-------------------|-------------------|
| `[Title]` | Top App Bar | `createTopBar()` |
| `┌───┐` box | Card | `createCard()` |
| `[Button]` | Filled Button | `createButton()` |
| `[___]` | Text Field | `createInputField()` |
| `• Item` | List Item | `createListItem()` |
| `[🏠][📊]` | Bottom Nav | `createBottomNav()` |
| `[Icon] Label` | Quick Action | `createQuickActions()` |

---

## Design System Reference

### Colors (Material Design 3)
```
Primary:     #6750A4
OnPrimary:   #FFFFFF
Surface:     #FFFBFE
OnSurface:   #1C1B1F
Error:       #B3261E
Success:     #2E7D32
```

### Typography
```
Display Large:  57px / 400
Headline Medium: 28px / 400
Title Large:    22px / 400
Body Large:     16px / 400
Label Medium:   12px / 500
```

### Spacing
```
xs: 4px  | sm: 8px  | md: 16px | lg: 24px | xl: 32px
```

### Screen Dimensions
```
Mobile:       393 x 852
Mobile Large: 430 x 932
Tablet:       768 x 1024
```
