# Gap Planning: Feature → {{FEATURE_NAME}}

## Implementation Plan: {{FEATURE_NAME}}

**Location**: `feature/{{FEATURE_DIR}}/`
**Progress**: {{FEATURE_PCT}}% Complete
**Gap Type**: {{GAP_TYPE}}

---

### Current State vs Target

| Component | Current | Target | Gap |
|-----------|:-------:|:------:|-----|
| ViewModel | {{VM_CURRENT}} | ✅ | {{VM_GAP}} |
| Screen | {{SCREEN_CURRENT}} | ✅ v2.0 | {{SCREEN_GAP}} |
| Navigation | {{NAV_CURRENT}} | ✅ | {{NAV_GAP}} |
| DI Module | {{DI_CURRENT}} | ✅ | {{DI_GAP}} |

---

### Implementation Tasks

| # | Task | Type | Priority | Effort |
|---|------|------|:--------:|:------:|
{{TASKS_TABLE}}

---

### Task 1: {{TASK_1_TITLE}}

**Goal**: {{TASK_1_GOAL}}

**Files**:
- {{TASK_1_FILES}}

**Code Sketch**:
```kotlin
{{TASK_1_CODE}}
```

**Verify**:
- {{TASK_1_VERIFY}}

---

### Task 2: {{TASK_2_TITLE}}

**Goal**: {{TASK_2_GOAL}}

**Files**:
- {{TASK_2_FILES}}

**Code Sketch**:
```kotlin
{{TASK_2_CODE}}
```

**Verify**:
- {{TASK_2_VERIFY}}

---

### v2.0 Design Implementation

From `features/{{FEATURE_NAME}}/MOCKUP.md`:

| Design Element | Requirement | Implementation |
|----------------|-------------|----------------|
{{V2_DESIGN_TABLE}}

---

### After Completion

**Step 1: Verify Implementation**
```bash
./gradlew :feature:{{FEATURE_DIR}}:test
./gradlew :cmp-android:assembleDemoDebug
# Run app and verify v2.0 design
```

**Step 2: Update Documentation**
- Update `features/{{FEATURE_NAME}}/STATUS.md`
- Update `claude-product-cycle/PRODUCT_MAP.md`

**Step 3: Commit**
```bash
git add -A
git commit -m "feat({{FEATURE_DIR}}): Implement v2.0 design"
```

---

### Platform Notes

| Platform | Notes |
|----------|-------|
| Android | {{ANDROID_NOTES}} |
| iOS | {{IOS_NOTES}} |
| Desktop | {{DESKTOP_NOTES}} |
| Web | {{WEB_NOTES}} |
