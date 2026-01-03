# Feature Layer Analysis Template

## Feature Layer Gap Analysis

**Location**: `feature/*/`
**Reference**: `claude-product-cycle/feature-layer/LAYER_STATUS.md`
**Last Updated**: {{DATE}}

---

### Implementation Status

| # | Feature | ViewModel | Screen | Navigation | DI | v2.0 UI | Complete |
|:-:|---------|:---------:|:------:|:----------:|:--:|:-------:|:--------:|
{{FEATURE_STATUS_ROWS}}

---

### v2.0 Design Implementation

| Feature | Gradient Cards | Analytics | Animations | Dark Mode | v2.0 Score |
|---------|:--------------:|:---------:|:----------:|:---------:|:----------:|
{{V2_STATUS_ROWS}}

---

### Layer Breakdown

```
FEATURE LAYER BREAKDOWN
├── ViewModels         {{VM_BAR}} {{VM_COUNT}}/17 ({{VM_PCT}}%)
├── Screens            {{SCREEN_BAR}} {{SCREEN_COUNT}}/17 ({{SCREEN_PCT}}%)
├── Navigation         {{NAV_BAR}} {{NAV_COUNT}}/17 ({{NAV_PCT}}%)
├── DI Modules         {{DI_BAR}} {{DI_COUNT}}/17 ({{DI_PCT}}%)
└── v2.0 UI Match      {{V2_BAR}} {{V2_COUNT}}/17 ({{V2_PCT}}%)

OVERALL (excl v2.0)    {{OVERALL_NO_V2_BAR}} {{OVERALL_NO_V2_PCT}}%
OVERALL (incl v2.0)    {{OVERALL_BAR}} {{OVERALL_PCT}}%
```

---

### Missing Features

{{MISSING_FEATURES_TABLE}}

---

### v2.0 Priority Gaps

{{V2_GAPS_TABLE}}

---

**Next**: `/gap-analysis platform` to check platform parity
