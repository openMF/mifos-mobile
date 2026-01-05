# Gap Planning: Platform → iOS Sub-Section

## Implementation Plan: iOS Platform

**Module**: `cmp-ios/`
**Progress**: {{IOS_PCT}}% Complete
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
# Install CocoaPods
cd cmp-ios && pod install

# Build shared framework
./gradlew :cmp-shared:linkDebugFrameworkIosArm64

# Open Xcode
open cmp-ios/iosApp.xcworkspace
```

---

### After Completion

1. Run `/gap-analysis platform ios` to see updated status
2. Test on iOS simulator/device
3. Update PRODUCT_MAP.md

---

### Verification

- [ ] Build compiles in Xcode
- [ ] All features work on iOS
- [ ] No warnings
- [ ] Tests pass
