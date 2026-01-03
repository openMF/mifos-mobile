# Design Layer Analysis Template

## Design Layer Gap Analysis

**Location**: `claude-product-cycle/design-spec-layer/`
**Last Updated**: {{DATE}}

---

### Design Completeness

| # | Feature | SPEC.md | MOCKUP.md | API.md | STATUS.md | Complete |
|:-:|---------|:-------:|:---------:|:------:|:---------:|:--------:|
{{DESIGN_STATUS_ROWS}}

---

### Sub-Layer Summary

```
DESIGN LAYER BREAKDOWN
├── SPEC.md      {{SPEC_BAR}} {{SPEC_COUNT}}/17 ({{SPEC_PCT}}%)
├── MOCKUP.md    {{MOCKUP_BAR}} {{MOCKUP_COUNT}}/17 ({{MOCKUP_PCT}}%) {{MOCKUP_VERSION}}
├── API.md       {{API_BAR}} {{API_COUNT}}/17 ({{API_PCT}}%)
├── STATUS.md    {{STATUS_BAR}} {{STATUS_COUNT}}/17 ({{STATUS_PCT}}%)
└── mockups/     {{MOCKUPS_BAR}} {{MOCKUPS_COUNT}}/17 ({{MOCKUPS_PCT}}%) ← Figma prompts

OVERALL          {{OVERALL_BAR}} {{OVERALL_PCT}}%
```

---

### Mockups Sub-Section (Figma Generation)

| # | Feature | MOCKUP.md | mockups/ | PROMPTS.md | design-tokens | Figma URL |
|:-:|---------|:---------:|:--------:|:----------:|:-------------:|:---------:|
{{MOCKUPS_STATUS_ROWS}}

**Legend**:
- `MOCKUP.md` - ASCII design spec (v2.0)
- `mockups/` - Generated AI prompts directory
- `PROMPTS.md` - Google Stitch prompts
- `design-tokens` - JSON tokens file
- `Figma URL` - Exported Figma link

**Next Actions**:
- Run `/gap-planning design` for step-by-step mockup generation plan
- Run `/design [feature] mockup` to generate mockups for specific feature

---

### Shared Components

| File | Location | Status |
|------|----------|:------:|
| COMPONENTS.md | `_shared/COMPONENTS.md` | {{COMPONENTS_STATUS}} |

---

### Mockup Tools

| Component | Location | Status |
|-----------|----------|:------:|
| figma-plugin/ | `mockup-tools/figma-plugin/` | {{FIGMA_STATUS}} |
| templates/ | `mockup-tools/templates/` | {{TEMPLATES_STATUS}} |
| scripts/ | `mockup-tools/scripts/` | {{SCRIPTS_STATUS}} |

---

### Gaps

{{DESIGN_GAPS_TABLE}}

---

**Next**: `/gap-analysis server` to check API availability
