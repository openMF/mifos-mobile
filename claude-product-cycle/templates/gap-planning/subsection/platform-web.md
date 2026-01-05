# Gap Planning: Platform → Web Sub-Section

## Implementation Plan: Web Platform

**Module**: `cmp-web/`
**Progress**: {{WEB_PCT}}% Complete
**Current Focus**: {{CURRENT_FOCUS}}

---

### Task Queue

| # | Task | Status | Priority | Effort |
|:-:|------|:------:|:--------:|:------:|
{{TASK_QUEUE_ROWS}}

---

### Current Task: {{CURRENT_TASK}}

**Goal**: {{TASK_GOAL}}

**Files**:
- {{TASK_FILES}}

**Steps**:
1. {{STEP_1}}
2. {{STEP_2}}
3. {{STEP_3}}

---

### Build Commands

```bash
# Run web app (JS)
./gradlew :cmp-web:jsBrowserDevelopmentRun

# Build production (JS)
./gradlew :cmp-web:jsBrowserProductionWebpack

# Run WASM (experimental)
./gradlew :cmp-web:wasmJsBrowserDevelopmentRun
```

---

### After Completion

1. Run `/gap-analysis platform web` to see updated status
2. Test in Chrome/Firefox/Safari
3. Update PRODUCT_MAP.md

---

### Verification

- [ ] Build compiles without errors
- [ ] All features work in browser
- [ ] Responsive design works
- [ ] No console errors
