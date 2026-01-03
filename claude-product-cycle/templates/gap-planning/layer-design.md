# Design Layer Planning Template

## Implementation Plan: Design Layer

**Location**: `claude-product-cycle/design-spec-layer/`
**Last Updated**: {{DATE}}

---

### Phase Overview

| Phase | Description | Status | Progress |
|-------|-------------|:------:|:--------:|
| Phase 1 | Specifications (SPEC.md, API.md, MOCKUP.md) | ✅ Done | 100% |
| Phase 2 | Mockup Generation (mockups/) | 🔄 In Progress | {{MOCKUPS_PCT}}% |
| Phase 3 | Figma Export | ⏳ Pending | 0% |

---

### Phase 1: Specifications (DONE)

- [x] All 17 features have SPEC.md ✅
- [x] All 17 features have API.md ✅
- [x] All 17 features have MOCKUP.md v2.0 ✅
- [x] All 17 features have STATUS.md ✅

---

### Phase 2: Mockup Generation (IN PROGRESS)

Generate Google Stitch prompts from MOCKUP.md for each feature:

{{MOCKUP_GENERATION_TASKS}}

---

### Current Task: {{CURRENT_MOCKUP_FEATURE}}

**Execute**: `/design {{CURRENT_MOCKUP_FEATURE}} mockup`

**Steps**:
1. Read `features/{{CURRENT_MOCKUP_FEATURE}}/MOCKUP.md`
2. Generate `features/{{CURRENT_MOCKUP_FEATURE}}/mockups/PROMPTS.md` (Google Stitch format)
3. Generate `features/{{CURRENT_MOCKUP_FEATURE}}/mockups/design-tokens.json`
4. User: Copy prompt to Google Stitch
5. User: Generate design and export to Figma
6. User: Update `features/{{CURRENT_MOCKUP_FEATURE}}/mockups/FIGMA_LINKS.md` with URL

---

### Phase 3: Figma Export (PENDING)

After all mockups generated, export to Figma:
1. Connect Figma MCP: `claude mcp add figma`
2. Run `/implement [feature]` to generate code from Figma

---

### After Completion

1. Run `/gap-analysis design` to see updated status
2. Continue with `/gap-planning design` for next feature
3. Session can end - progress tracked in `mockups/` directories
4. Resume with `/session-start` to continue where you left off

---

### Legacy Gaps (if any)

| # | Feature | Missing | Priority | Effort |
|---|---------|---------|:--------:|:------:|
{{DESIGN_GAPS_TABLE}}

---

### Task Details

{{TASK_DETAILS}}

---

### SPEC.md Template

For missing SPEC.md files, use this structure:

```markdown
# [Feature] Specification

## Overview
Brief description of the feature.

## User Stories
- As a user, I want to...

## Acceptance Criteria
- [ ] Criteria 1
- [ ] Criteria 2

## Dependencies
- List dependencies

## Out of Scope
- What's not included
```

**Location**: `design-spec-layer/features/[feature]/SPEC.md`

---

### MOCKUP.md Template

For missing MOCKUP.md files, follow v2.0 design patterns:

```markdown
# [Feature] Mockup v2.0

## Screen Layout
[ASCII mockup]

## Components
- Component specs with dimensions

## Colors
- Primary Gradient: #667EEA → #764BA2

## Animations
- Entry/exit animations
```

**Location**: `design-spec-layer/features/[feature]/MOCKUP.md`

---

### API.md Template

For missing API.md files:

```markdown
# [Feature] API

## Endpoints

### GET /self/[endpoint]
- Request: params
- Response: schema

### POST /self/[endpoint]
- Request: body schema
- Response: schema
```

**Location**: `design-spec-layer/features/[feature]/API.md`

---

### Verification

After creating files:
1. Review against existing features for consistency
2. Update `design-spec-layer/STATUS.md`
3. Update `PRODUCT_MAP.md`

---

**Ready?** Run `/design [feature]` to create specs
