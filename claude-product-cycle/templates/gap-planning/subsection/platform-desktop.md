# Gap Planning: Platform → Desktop Sub-Section

## Implementation Plan: Desktop Platform

**Module**: `cmp-desktop/`
**Progress**: {{DESKTOP_PCT}}% Complete
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
# Run desktop app
./gradlew :cmp-desktop:run

# Build distribution
./gradlew :cmp-desktop:packageDistributionForCurrentOS

# Run tests
./gradlew :cmp-desktop:test
```

---

### After Completion

1. Run `/gap-analysis platform desktop` to see updated status
2. Test on macOS/Windows/Linux
3. Update PRODUCT_MAP.md

---

### Verification

- [ ] Build compiles without errors
- [ ] All features work on Desktop
- [ ] Window resizing works
- [ ] Keyboard shortcuts work
