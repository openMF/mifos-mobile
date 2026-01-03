# Gap Planning: Design → Mockups Sub-Section

## Implementation Plan: Mockup Generation

**Phase**: Phase 2 - Mockup Generation
**Progress**: {{MOCKUPS_COUNT}}/17 features ({{MOCKUPS_PCT}}%)
**Current Focus**: {{CURRENT_FEATURE}}

---

### 🔌 Step 0: Setup AI Design Tools & MCP

Before generating mockups, configure your design tools:

#### Check Current MCP Status
```bash
claude mcp list
```

#### Available AI Design Tools

| Tool | MCP Available | Setup Command |
|------|:-------------:|---------------|
| **Google Stitch** (Recommended) | ✅ | `claude mcp add stitch-ai -- npx -y stitch-ai-mcp` |
| **Figma** | ✅ | `claude mcp add figma -- npx -y figma-mcp --token TOKEN` |
| Uizard | ❌ | Web only: [uizard.io](https://uizard.io/) |
| Visily | ❌ | Web only: [visily.ai](https://www.visily.ai/) |

#### Recommended Setup

**Option A: Full MCP Integration (Best)**
```bash
# 1. Install Google Stitch MCP
claude mcp add stitch-ai -- npx -y stitch-ai-mcp

# 2. Install Figma MCP (get token from figma.com/developers)
claude mcp add figma -- npx -y figma-mcp --token YOUR_TOKEN

# 3. Verify
claude mcp list
```

**Option B: Manual Workflow (No MCP)**
- Use web interfaces directly
- Copy/paste prompts manually
- Google Stitch: [stitch.withgoogle.com](https://stitch.withgoogle.com/)

#### MCP Resources
- Stitch AI MCP: [github.com/StitchAI/stitch-ai-mcp](https://github.com/StitchAI/stitch-ai-mcp)
- Google Stitch Web: [stitch.withgoogle.com](https://stitch.withgoogle.com/)

---

### Task Queue

| # | Feature | Status | Command |
|:-:|---------|:------:|---------|
| 0 | MCP Setup | {{MCP_STATUS}} | See above |
{{TASK_QUEUE_ROWS}}

**Status**: ✅ Done | 🔄 Current | ⏳ Pending

---

### Current Task: {{CURRENT_FEATURE}}

**Execute**:
```
/design {{CURRENT_FEATURE}} mockup
```

**What Happens**:
1. Claude asks you to select AI design tool
2. Reads `features/{{CURRENT_FEATURE}}/MOCKUP.md`
3. Parses screens, components, colors, typography
4. Generates `features/{{CURRENT_FEATURE}}/mockups/PROMPTS.md`
5. Generates `features/{{CURRENT_FEATURE}}/mockups/design-tokens.json`
6. If MCP connected: Offers to send directly to tool
7. Outputs next steps

**User Actions (after generation)**:

| With MCP | Without MCP |
|----------|-------------|
| Claude sends prompt directly | Copy prompt from PROMPTS.md |
| Auto-generates in Stitch | Paste in [stitch.withgoogle.com](https://stitch.withgoogle.com/) |
| Review & export to Figma | Export to Figma manually |
| Update FIGMA_LINKS.md | Update FIGMA_LINKS.md |

---

### Output Files

```
features/{{CURRENT_FEATURE}}/mockups/
├── PROMPTS.md           # AI tool prompts (generated)
├── design-tokens.json   # Structured tokens (generated)
└── FIGMA_LINKS.md       # Figma URLs (user fills)
```

---

### PROMPTS.md Format (Google Stitch)

```markdown
# {{CURRENT_FEATURE}} - AI Mockup Prompts

> **Generated from**: features/{{CURRENT_FEATURE}}/MOCKUP.md
> **AI Tool**: Google Stitch

## Screen 1: [Screen Name]

### Google Stitch Prompt

Create a mobile [screen type] screen with Material Design 3:

**App Context:**
Mifos Mobile - Self-service banking app

**Screen Size:** 393 x 852 pixels

**Header Section:**
- [Details from MOCKUP.md]

**Main Content:**
- [Sections from MOCKUP.md]

**Style Guidelines:**
- Primary Gradient: #667EEA → #764BA2
- Surface: #FFFBFE
- Typography: Inter font family
- Spacing: 16px standard padding
```

---

### design-tokens.json Format

```json
{
  "feature": "{{CURRENT_FEATURE}}",
  "generated": "YYYY-MM-DD",
  "tokens": {
    "colors": {
      "primaryGradientStart": "#667EEA",
      "primaryGradientEnd": "#764BA2",
      "surface": "#FFFBFE",
      "success": "#00D09C",
      "error": "#FF4757"
    },
    "typography": {...},
    "spacing": {...},
    "radius": {...}
  },
  "screens": [...],
  "components": [...]
}
```

---

### After Current Task

1. Run `/gap-analysis design mockup` to see updated status
2. Continue with next feature: `{{NEXT_FEATURE}}`
3. Session can end - progress tracked in `mockups/` directories

---

### Batch Generation (Optional)

To generate all remaining mockups:
```
/design mockup
```

This will iterate through all features without mockups/ directory.

---

### Verification

After all mockups generated:
- [ ] MCP configured (stitch-ai and/or figma)
- [ ] All 17 features have mockups/ directory
- [ ] All PROMPTS.md follow selected tool format
- [ ] All design-tokens.json are valid JSON
- [ ] Ready for Phase 3: Figma Export

---

### Workflow Summary

```
┌─────────────────────────────────────────────────────────────────┐
│                 MOCKUP GENERATION WORKFLOW                       │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  STEP 0: SETUP (One-time)                                       │
│  └─→ claude mcp add stitch-ai -- npx -y stitch-ai-mcp          │
│                                                                  │
│  STEP 1: GENERATE                                               │
│  └─→ /design [feature] mockup                                   │
│      └─→ Select tool → Generate PROMPTS.md + tokens             │
│                                                                  │
│  STEP 2: CREATE DESIGN                                          │
│  └─→ With MCP: Claude sends to Stitch directly                  │
│  └─→ Without: Copy prompt to stitch.withgoogle.com              │
│                                                                  │
│  STEP 3: EXPORT                                                 │
│  └─→ Export to Figma                                            │
│  └─→ Update FIGMA_LINKS.md                                      │
│                                                                  │
│  STEP 4: REPEAT                                                 │
│  └─→ /gap-analysis design mockup → next feature                 │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```
