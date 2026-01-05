# Gap Analysis - Comprehensive Dashboard Template

Replace {{PLACEHOLDERS}} with real data from O(1) index files.

---

```
╔══════════════════════════════════════════════════════════════════════════════╗
║  MIFOS MOBILE - GAP ANALYSIS (O(1) Lookup)                                   ║
║  Last Updated: {{DATE}}                                                       ║
╠══════════════════════════════════════════════════════════════════════════════╣

## 5-Layer Health Overview

┌─────────────────────────────────────────────────────────────────────────────┐
│  Layer          Progress              Implemented    Gaps    Status         │
├─────────────────────────────────────────────────────────────────────────────┤
│  1. Design      {{DESIGN_BAR}} {{DESIGN_PCT}}%      {{DESIGN_DONE}}/17       {{DESIGN_GAPS}}     {{DESIGN_STATUS}}  │
│  2. Server      {{SERVER_BAR}} {{SERVER_PCT}}%      {{SERVER_DONE}}/11       {{SERVER_GAPS}}     {{SERVER_STATUS}}  │
│  3. Client      {{CLIENT_BAR}} {{CLIENT_PCT}}%      {{CLIENT_DONE}}/30       {{CLIENT_GAPS}}     {{CLIENT_STATUS}}  │
│  4. Feature     {{FEATURE_BAR}} {{FEATURE_PCT}}%    {{FEATURE_DONE}}/63      {{FEATURE_GAPS}}    {{FEATURE_STATUS}} │
│  5. Platform    {{PLATFORM_BAR}} {{PLATFORM_PCT}}%  {{PLATFORM_DONE}}/4      {{PLATFORM_GAPS}}   {{PLATFORM_STATUS}}│
├─────────────────────────────────────────────────────────────────────────────┤
│  OVERALL        {{OVERALL_BAR}} {{OVERALL_PCT}}%                                            │
└─────────────────────────────────────────────────────────────────────────────┘

---

## ✅ IMPLEMENTED (What's Complete)

### Design Layer ({{DESIGN_TOTAL}} features)

| # | Feature | SPEC | API | STATUS | Mockups | Figma | Tokens |
|:-:|---------|:----:|:---:|:------:|:-------:|:-----:|:------:|
{{DESIGN_ROWS}}

### Server Layer ({{SERVER_TOTAL}} endpoint categories)

| Category | Endpoints | Status | File |
|----------|:---------:|:------:|------|
{{SERVER_ROWS}}

### Client Layer ({{SERVICE_COUNT}} services, {{REPO_COUNT}} repositories)

| Component | Count | Status |
|-----------|:-----:|:------:|
| Services | {{SERVICE_COUNT}}/{{SERVICE_TOTAL}} | {{SERVICE_STATUS}} |
| Repositories | {{REPO_COUNT}}/{{REPO_TOTAL}} | {{REPO_STATUS}} |
| DI Modules | {{DI_COUNT}}/{{DI_TOTAL}} | {{DI_STATUS}} |

**Services**:
| # | Service | File | Feature |
|:-:|---------|------|---------|
{{SERVICE_ROWS}}

**Repositories**:
| # | Repository | Implementation | Service |
|:-:|------------|----------------|---------|
{{REPO_ROWS}}

### Feature Layer ({{MODULE_COUNT}} modules, {{SCREEN_COUNT}} screens)

| Component | Count | Status |
|-----------|:-----:|:------:|
| Modules | {{MODULE_COUNT}}/{{MODULE_TOTAL}} | {{MODULE_STATUS}} |
| ViewModels | {{VM_COUNT}}/{{VM_TOTAL}} | {{VM_STATUS}} |
| Screens | {{SCREEN_COUNT}}/{{SCREEN_TOTAL}} | {{SCREEN_STATUS}} |
| DI Modules | {{FEATURE_DI_COUNT}}/{{FEATURE_DI_TOTAL}} | {{FEATURE_DI_STATUS}} |

**Modules**:
| # | Module | Path | DI | VMs | Screens |
|:-:|--------|------|:--:|:---:|:-------:|
{{MODULE_ROWS}}

### Platform Layer ({{PLATFORM_COUNT}} platforms)

| Platform | Module | Build | Flavors | Status |
|----------|--------|:-----:|:-------:|:------:|
{{PLATFORM_ROWS}}

---

## ❌ GAPS (What Needs Work)

### P0 - Critical (Blocks Other Work)

| # | Gap | Layer | Impact | Plan Command |
|:-:|-----|-------|--------|--------------|
{{P0_ROWS}}

### P1 - High Priority (User-Facing)

| # | Gap | Layer | Impact | Plan Command |
|:-:|-----|-------|--------|--------------|
{{P1_ROWS}}

### P2 - Nice to Have (Polish)

| # | Gap | Layer | Impact | Plan Command |
|:-:|-----|-------|--------|--------------|
{{P2_ROWS}}

---

## 🛠️ AVAILABLE ACTIONS

### Create Plans (Based on Gaps Found)

| Priority | Gap | Command | Tasks |
|:--------:|-----|---------|:-----:|
{{PLAN_ROWS}}

### Implement Features

| Target | Command | What It Does |
|--------|---------|--------------|
| E2E feature | `/implement [feature]` | Full 5-layer implementation |
| Client only | `/client [feature]` | Network + Data layers |
| UI only | `/feature [feature]` | ViewModel + Screen |
| Design only | `/design [feature]` | Spec + Mockup |

### Verify Implementation

| Target | Command | What It Does |
|--------|---------|--------------|
| Any feature | `/verify [feature]` | Check implementation vs spec |
| All features | `/verify` | Full verification |

---

## 📊 O(1) PERFORMANCE GAINS

| Metric | Before (O(n)) | After (O(1)) | Improvement |
|--------|:-------------:|:------------:|:-----------:|
| Files to scan | 10-50 | 1-2 | **90% fewer** |
| Lines to read | 500-3000 | 60-200 | **80-95% less** |
| Tool calls | 3-5 | 1-2 | **60% fewer** |
| Context usage | High | Low | **~10x efficient** |

### How O(1) Works

```
BEFORE (O(n) - Slow):
┌──────────────────────────────────────────────────────┐
│ Question: "Which features need mockups?"              │
│ → Scan 17 feature dirs                               │
│ → Read 50+ files                                     │
│ → 2000+ lines                                        │
│ → 5+ tool calls                                      │
│ → 30+ seconds                                        │
└──────────────────────────────────────────────────────┘

AFTER (O(1) - Fast):
┌──────────────────────────────────────────────────────┐
│ Question: "Which features need mockups?"              │
│ → Read MOCKUPS_INDEX.md                              │
│ → 1 file, ~100 lines                                 │
│ → 1 tool call                                        │
│ → <3 seconds                                         │
└──────────────────────────────────────────────────────┘
```

---

## 📁 O(1) INDEX FILES

| Layer | Index File | Path | Lines | Use For |
|-------|------------|------|:-----:|---------|
| Feature | MODULES_INDEX.md | `feature-layer/` | ~120 | Find any module |
| Feature | SCREENS_INDEX.md | `feature-layer/` | ~180 | Find any screen |
| Design | FEATURES_INDEX.md | `design-spec-layer/` | ~100 | Check feature status |
| Design | MOCKUPS_INDEX.md | `design-spec-layer/` | ~100 | Check mockup status |
| Client | FEATURE_MAP.md | `client-layer/` | ~150 | Map feature → services |
| Server | API_INDEX.md | `server-layer/` | ~80 | Find any endpoint |
| Platform | LAYER_STATUS.md | `platform-layer/` | ~80 | Platform commands |

---

## 🎯 RECOMMENDED NEXT STEPS

{{NEXT_STEPS}}

---

## 🔄 WORKFLOW CYCLE

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                         O(1) DEVELOPMENT CYCLE                               │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   /gap-analysis  ──────────────────────────────────────────────────────→    │
│        │                                                                     │
│        │  Shows ALL implemented + ALL gaps + ALL commands                   │
│        │                                                                     │
│        ▼                                                                     │
│   YOU DECIDE what to work on                                                │
│        │                                                                     │
│        ├─→ /gap-planning [target]  →  Get step-by-step plan                │
│        │                                                                     │
│        ├─→ /implement [target]     →  Execute implementation               │
│        │                                                                     │
│        ├─→ /verify [target]        →  Confirm it works                     │
│        │                                                                     │
│        └─→ /gap-analysis           →  See updated status (loop)            │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

╚══════════════════════════════════════════════════════════════════════════════╝
```

---

## Template Variables Reference

| Variable | Source | Example |
|----------|--------|---------|
| {{DATE}} | Current date | 2025-01-05 |
| {{DESIGN_BAR}} | Progress bar | [████████░░] |
| {{DESIGN_PCT}} | Percentage | 80 |
| {{DESIGN_DONE}} | Completed count | 14 |
| {{DESIGN_GAPS}} | Gap count | 3 |
| {{DESIGN_STATUS}} | Status text | ⚠️ Mockups |
| {{DESIGN_ROWS}} | Table rows | From FEATURES_INDEX.md |
| {{SERVER_ROWS}} | Table rows | From API_INDEX.md |
| {{SERVICE_ROWS}} | Table rows | From FEATURE_MAP.md |
| {{REPO_ROWS}} | Table rows | From FEATURE_MAP.md |
| {{MODULE_ROWS}} | Table rows | From MODULES_INDEX.md |
| {{PLATFORM_ROWS}} | Table rows | From LAYER_STATUS.md |
| {{P0_ROWS}} | Critical gaps | From gap analysis |
| {{P1_ROWS}} | High priority gaps | From gap analysis |
| {{P2_ROWS}} | Nice to have gaps | From gap analysis |
| {{PLAN_ROWS}} | Planning commands | Generated from gaps |
| {{NEXT_STEPS}} | Recommendations | Generated from priorities |
