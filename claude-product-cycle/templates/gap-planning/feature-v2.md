# v2.0 UI Upgrade Planning Template

## Implementation Plan: {{FEATURE_NAME}}

**Gap Type**: v2.0 UI not implemented
**Current**: Feature works but UI is outdated
**Target**: Update UI to match MOCKUP.md v2.0 design

---

### v2.0 Elements to Implement

| # | Element | Current | Target | Priority | Effort |
|---|---------|:-------:|:------:|:--------:|:------:|
{{V2_ELEMENTS_TABLE}}

---

### Tasks Overview

| # | Task | Files | Priority | Effort |
|---|------|-------|:--------:|:------:|
{{TASKS_TABLE}}

---

### Design Reference

**MOCKUP Location**: `design-spec-layer/features/{{FEATURE}}/MOCKUP.md`

**Key v2.0 Specs**:
- Primary Gradient: `#667EEA → #764BA2`
- Card Radius: `24.dp`
- Spacing: `xs=4, sm=8, md=12, lg=16, xl=20, xxl=24`

---

### Task Details

{{TASK_DETAILS}}

---

### Shared Components to Use/Create

| Component | Location | Status |
|-----------|----------|:------:|
{{SHARED_COMPONENTS}}

---

### Verification

```bash
./gradlew :feature:{{FEATURE}}:build
./gradlew :cmp-android:assembleDemoDebug
# Visual verification against MOCKUP.md
# Test dark mode appearance
# Verify animations
```

---

### After Completion

1. Update `design-spec-layer/features/{{FEATURE}}/STATUS.md`:
   - Mark v2.0 UI as ✅
2. Update `PRODUCT_MAP.md`:
   - Update feature row status

---

**Ready?** Run `/feature {{FEATURE}}`
