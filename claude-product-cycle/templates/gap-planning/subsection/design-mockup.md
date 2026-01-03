# Gap Planning: Design → Mockups Sub-Section

## Implementation Plan: Mockup Generation

**Phase**: Phase 2 - Mockup Generation
**Progress**: {{MOCKUPS_COUNT}}/17 features ({{MOCKUPS_PCT}}%)
**Current Focus**: {{CURRENT_FEATURE}}

---

### Task Queue

| # | Feature | Status | Command |
|:-:|---------|:------:|---------|
{{TASK_QUEUE_ROWS}}

**Status**: ✅ Done | 🔄 Current | ⏳ Pending

---

### Current Task: {{CURRENT_FEATURE}}

**Execute**:
```
/design {{CURRENT_FEATURE}} mockup
```

**Steps**:
1. Read `features/{{CURRENT_FEATURE}}/MOCKUP.md`
2. Parse screens, components, colors, typography
3. Generate `features/{{CURRENT_FEATURE}}/mockups/PROMPTS.md`
4. Generate `features/{{CURRENT_FEATURE}}/mockups/design-tokens.json`
5. Output Google Stitch instructions

**User Actions (after generation)**:
1. Copy prompt to [Google Stitch](https://stitch.withgoogle.com)
2. Generate design
3. Export to Figma
4. Update `features/{{CURRENT_FEATURE}}/mockups/FIGMA_LINKS.md`

---

### Output Files

```
features/{{CURRENT_FEATURE}}/mockups/
├── PROMPTS.md           # Google Stitch prompts (generated)
├── design-tokens.json   # Structured tokens (generated)
└── FIGMA_LINKS.md       # Figma URLs (user fills)
```

---

### PROMPTS.md Format

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
- [ ] All 17 features have mockups/ directory
- [ ] All PROMPTS.md follow Google Stitch format
- [ ] All design-tokens.json are valid JSON
- [ ] Ready for Phase 3: Figma Export
