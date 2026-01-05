# Feature Layer Planning Template

## Implementation Plan: Feature Layer

**Location**: `feature/*/`
**Reference**: `claude-product-cycle/feature-layer/LAYER_STATUS.md`
**Last Updated**: {{DATE}}

---

### Gaps Identified

| # | Feature | Gap Type | Priority | Effort |
|---|---------|----------|:--------:|:------:|
{{FEATURE_GAPS_TABLE}}

---

### Gap Types

| Type | Description | Template |
|------|-------------|----------|
| Missing | Feature module doesn't exist | `feature-new.md` |
| v2.0 UI | Feature exists, needs UI update | `feature-v2.md` |
| Incomplete | Missing ViewModel/Screen/Nav/DI | `feature-new.md` (partial) |

---

### Priority Order

**P0 - Missing Features**:
{{P0_FEATURES}}

**P1 - v2.0 UI Updates**:
{{P1_FEATURES}}

**P2 - Polish**:
{{P2_FEATURES}}

---

### Tasks Overview

| # | Task | Feature | Files | Priority | Effort |
|---|------|---------|-------|:--------:|:------:|
{{TASKS_TABLE}}

---

### Missing Features Plan

For each missing feature, create:

| Component | File Pattern |
|-----------|--------------|
| Module | `feature/[name]/build.gradle.kts` |
| ViewModel | `feature/[name]/src/.../[Name]ViewModel.kt` |
| Screen | `feature/[name]/src/.../[Name]Screen.kt` |
| Navigation | `feature/[name]/src/.../navigation/[Name]Navigation.kt` |
| DI | `feature/[name]/src/.../di/[Name]Module.kt` |

**Run**: `/gap-planning [feature]` for detailed plan

---

### v2.0 UI Updates Plan

For each v2.0 update, implement:

| Element | Priority | Shared Component |
|---------|:--------:|------------------|
| Gradient hero cards | P1 | `HeroGradientCard.kt` |
| Analytics charts | P1 | `SpendingAnalyticsCard.kt` |
| Quick actions v2.0 | P1 | `QuickActionButton.kt` |
| Micro-animations | P2 | Animation utilities |
| Dark mode polish | P2 | Theme updates |

**Run**: `/gap-planning [feature]` for detailed plan

---

### Shared Components to Create

| Component | Location | Used By |
|-----------|----------|---------|
{{SHARED_COMPONENTS_TABLE}}

---

### Implementation Order

```
1. Create shared components (core/designsystem)
   └── HeroGradientCard, SpendingAnalyticsCard, etc.

2. Implement missing features (P0)
   └── dashboard, etc.

3. Update existing features to v2.0 (P1)
   └── home, accounts, transfer, etc.

4. Polish and animations (P2)
   └── All features
```

---

### Verification

```bash
./gradlew :feature:[name]:build
./gradlew :cmp-android:assembleDemoDebug
# Visual verification against MOCKUP.md
```

After completing:
1. Update `feature-layer/LAYER_STATUS.md`
2. Update each feature's `STATUS.md`
3. Update `PRODUCT_MAP.md`

---

**Start with**: `/gap-planning {{FIRST_FEATURE}}`
