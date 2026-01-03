# Gap Planning: Design → Specifications Sub-Section

## Implementation Plan: Specifications

**Phase**: Phase 1 - Specifications
**Progress**: {{SPEC_PCT}}% Complete
**Current Focus**: {{CURRENT_FOCUS}}

---

### Task Queue

| # | Feature | SPEC | API | MOCKUP | STATUS | Action |
|:-:|---------|:----:|:---:|:------:|:------:|--------|
{{TASK_QUEUE_ROWS}}

---

### Current Task: {{CURRENT_TASK}}

**Execute**:
```
/design {{CURRENT_FEATURE}}
```

**Steps**:
1. Read existing files in `features/{{CURRENT_FEATURE}}/`
2. Check implementation in `feature/{{FEATURE_DIR}}/`
3. Create/update missing documents
4. Update STATUS.md

---

### Document Templates

#### SPEC.md Template
```markdown
# {{FEATURE_NAME}} Specification

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

#### API.md Template
```markdown
# {{FEATURE_NAME}} API

## Endpoints

### GET /self/[endpoint]
- Request: params
- Response: schema

### POST /self/[endpoint]
- Request: body schema
- Response: schema
```

#### MOCKUP.md Template
```markdown
# {{FEATURE_NAME}} Mockup v2.0

## Screen Layout
[ASCII mockup]

## Components
- Component specs with dimensions

## Colors
- Primary Gradient: #667EEA → #764BA2

## Animations
- Entry/exit animations
```

---

### After Completion

1. Run `/gap-analysis design spec` to see updated status
2. Continue with next feature
3. Update `design-spec-layer/STATUS.md`

---

### Verification

After all specs complete:
- [ ] All 17 features have SPEC.md
- [ ] All 17 features have API.md
- [ ] All 17 features have MOCKUP.md v2.0
- [ ] All 17 features have STATUS.md
- [ ] Ready for Phase 2: Mockup Generation
