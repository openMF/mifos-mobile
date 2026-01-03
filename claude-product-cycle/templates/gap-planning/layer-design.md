# Design Layer Planning Template

## Implementation Plan: Design Layer

**Location**: `claude-product-cycle/design-spec-layer/`
**Last Updated**: {{DATE}}

---

### Gaps Identified

| # | Feature | Missing | Priority | Effort |
|---|---------|---------|:--------:|:------:|
{{DESIGN_GAPS_TABLE}}

---

### Tasks Overview

| # | Task | Files | Priority | Effort |
|---|------|-------|:--------:|:------:|
{{TASKS_TABLE}}

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
