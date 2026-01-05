# Gap Analysis Command

Comprehensive analysis showing ALL implemented items and ALL gaps across the 5-layer lifecycle. Runs on O(1) by reading index files.

## Usage

```
/gap-analysis                        # FULL comprehensive view (recommended)
/gap-analysis design                 # Design layer only
/gap-analysis design mockup          # Design → Mockup sub-section
/gap-analysis server                 # Server layer only
/gap-analysis client                 # Client layer only
/gap-analysis feature                # Feature layer only
/gap-analysis feature [name]         # Specific feature only
/gap-analysis platform               # Platform layer only
/gap-analysis testing                # Testing status (all layers)
/gap-analysis testing [layer]        # Testing for specific layer
/gap-analysis [feature-name]         # Single feature (all 5 layers)
```

## Comprehensive Output (No Parameters)

When `/gap-analysis` is called without parameters, show the **FULL comprehensive view**:

```
╔══════════════════════════════════════════════════════════════════════════════╗
║  MIFOS MOBILE - GAP ANALYSIS (O(1) Lookup)                                   ║
╠══════════════════════════════════════════════════════════════════════════════╣

## 5-Layer Health Overview

┌─────────────────────────────────────────────────────────────────────────────┐
│  Layer          Progress              Implemented    Gaps    Status         │
├─────────────────────────────────────────────────────────────────────────────┤
│  1. Design      [████████░░] 80%      14/17          3       ⚠️ Mockups     │
│  2. Server      [██████████] 100%     11/11          0       ✅ Complete    │
│  3. Client      [██████████] 100%     30/30          0       ✅ Complete    │
│  4. Feature     [██████████] 100%     63/63          0       ✅ Complete    │
│  5. Platform    [█████████░] 95%      4/4            1       ⚠️ Web exp.    │
├─────────────────────────────────────────────────────────────────────────────┤
│  OVERALL        [█████████░] 95%                                            │
└─────────────────────────────────────────────────────────────────────────────┘

---

## ✅ IMPLEMENTED (What's Complete)

### Design Layer (17 features)
| Feature | SPEC | API | STATUS | Mockups |
|---------|:----:|:---:|:------:|:-------:|
| auth | ✅ | ✅ | ✅ | ✅ |
| home | ✅ | ✅ | ✅ | ⚠️ |
| accounts | ✅ | ✅ | ✅ | ⚠️ |
[... all 17 features ...]

### Server Layer (11 endpoint categories)
| Category | Endpoints | Status |
|----------|:---------:|:------:|
| AUTH | 4 | ✅ |
| CLIENT | 5 | ✅ |
| SAVINGS | 8 | ✅ |
[... all 11 categories ...]

### Client Layer (13 services, 17 repositories)
| Component | Count | Status |
|-----------|:-----:|:------:|
| Services | 13/13 | ✅ |
| Repositories | 17/17 | ✅ |
| DI Modules | 2/2 | ✅ |

### Feature Layer (23 modules, 63 screens)
| Component | Count | Status |
|-----------|:-----:|:------:|
| Modules | 23/23 | ✅ |
| ViewModels | 49/49 | ✅ |
| Screens | 63/63 | ✅ |
| DI Modules | 21/21 | ✅ |

### Platform Layer (4 platforms)
| Platform | Build | Status |
|----------|:-----:|:------:|
| Android | ✅ | Primary |
| iOS | ✅ | CocoaPods |
| Desktop | ✅ | JVM |
| Web | ⚠️ | Experimental |

---

## ❌ GAPS (What Needs Work)

### P0 - Critical (Blocks Other Work)
| Gap | Layer | Impact | Plan Command |
|-----|-------|--------|--------------|
| (none currently) | - | - | - |

### P1 - High Priority (User-Facing)
| Gap | Layer | Impact | Plan Command |
|-----|-------|--------|--------------|
| Missing mockups (10) | Design | v2.0 UI blocked | `/gap-planning design mockup` |
| Missing design-tokens (9) | Design | Theme consistency | `/gap-planning design mockup` |

### P2 - Nice to Have (Polish)
| Gap | Layer | Impact | Plan Command |
|-----|-------|--------|--------------|
| Web experimental | Platform | Limited browser support | `/gap-planning platform web` |

---

## 🧪 TESTING STATUS

| Layer | Unit | UI | Integration | Screenshot | Status |
|-------|:----:|:--:|:-----------:|:----------:|:------:|
| Client (Repo) | 14 | - | - | - | ⚠️ Partial |
| Feature (VM) | 0 | 0 | 0 | 0 | ⬜ Not Started |
| Platform (E2E) | - | - | 0 | 0 | ⬜ Not Started |

**Testing Gaps** (P1):
| Gap | Impact | Plan Command |
|-----|--------|--------------|
| ViewModel tests (0/49) | No regression safety | `/gap-planning testing feature` |
| UI tests (0/63) | No UI verification | `/gap-planning testing feature` |
| E2E tests (0/8) | No flow coverage | `/gap-planning testing platform` |
| Screenshot tests (0/30) | No visual regression | `/gap-planning testing platform` |

→ For detailed testing status: `/gap-analysis testing`

---

## 🛠️ AVAILABLE ACTIONS

### Create Plans
| Gap | Command | What It Does |
|-----|---------|--------------|
| All mockups | `/gap-planning design mockup` | Generate mockups for 10 features |
| Specific feature | `/gap-planning [feature]` | Plan single feature improvements |
| Web platform | `/gap-planning platform web` | Stabilize web build |

### Implement
| Target | Command | What It Does |
|--------|---------|--------------|
| E2E feature | `/implement [feature]` | Full implementation |
| Client only | `/client [feature]` | Network + Data layers |
| UI only | `/feature [feature]` | ViewModel + Screen |

### Verify
| Target | Command | What It Does |
|--------|---------|--------------|
| Any feature | `/verify [feature]` | Check implementation vs spec |

### Testing
| Target | Command | What It Does |
|--------|---------|--------------|
| Run tests | `/verify-tests [feature]` | Run tests for feature |
| Test status | `/gap-analysis testing` | See testing coverage |
| Plan tests | `/gap-planning testing [layer]` | Plan test implementation |

---

## 📊 O(1) PERFORMANCE

| Metric | Before | After | Improvement |
|--------|:------:|:-----:|:-----------:|
| Files to scan | 10-50 | 1-2 | **90% fewer** |
| Lines to read | 500-3000 | 60-200 | **80-95% less** |
| Tool calls | 3-5 | 1-2 | **60% fewer** |

---

## 📁 O(1) INDEX FILES

| Layer | Index File | Lines | Use For |
|-------|------------|:-----:|---------|
| Feature | `MODULES_INDEX.md` | ~120 | Find any module |
| Feature | `SCREENS_INDEX.md` | ~180 | Find any screen |
| Design | `FEATURES_INDEX.md` | ~100 | Check feature status |
| Design | `MOCKUPS_INDEX.md` | ~100 | Check mockup status |
| Client | `FEATURE_MAP.md` | ~150 | Map feature → services |
| Server | `API_INDEX.md` | ~80 | Find any endpoint |
| Platform | `LAYER_STATUS.md` | ~80 | Platform commands |

---

## 🎯 RECOMMENDED NEXT STEPS

Based on current gaps:

1. **Design Mockups** (P1) - 10 features need mockups
   → `/gap-planning design mockup`

2. **Web Platform** (P2) - Experimental status
   → `/gap-planning platform web`

3. **Verify Features** - Ensure all features match spec
   → `/verify [feature-name]`

╚══════════════════════════════════════════════════════════════════════════════╝
```

---

## 5-Layer Lifecycle

```
┌─────────────────────────────────────────────────────────────────┐
│  1. Design   → spec | mockup | api | status                     │
│  2. Server   → endpoints | availability                         │
│  3. Client   → network | data | model                           │
│  4. Feature  → viewmodel | screen | navigation | di             │
│  5. Platform → android | ios | desktop | web                    │
└─────────────────────────────────────────────────────────────────┘
```

---

## Instructions

### Step 1: Read O(1) Index Files

Read these files for instant status (DO NOT scan directories):

| Layer | Index File | Path |
|-------|------------|------|
| Feature | MODULES_INDEX.md | `claude-product-cycle/feature-layer/MODULES_INDEX.md` |
| Feature | SCREENS_INDEX.md | `claude-product-cycle/feature-layer/SCREENS_INDEX.md` |
| Design | FEATURES_INDEX.md | `claude-product-cycle/design-spec-layer/FEATURES_INDEX.md` |
| Design | MOCKUPS_INDEX.md | `claude-product-cycle/design-spec-layer/MOCKUPS_INDEX.md` |
| Client | FEATURE_MAP.md | `claude-product-cycle/client-layer/FEATURE_MAP.md` |
| Server | API_INDEX.md | `claude-product-cycle/server-layer/API_INDEX.md` |
| Platform | LAYER_STATUS.md | `claude-product-cycle/platform-layer/LAYER_STATUS.md` |
| Testing | TESTING_STATUS.md | `claude-product-cycle/*/TESTING_STATUS.md` (per layer) |

### Step 2: Calculate Progress

From index files, calculate:
- **Design**: Count ✅ in FEATURES_INDEX.md + MOCKUPS_INDEX.md
- **Server**: All 11 categories = 100%
- **Client**: Count services + repositories in FEATURE_MAP.md
- **Feature**: Count modules + screens in MODULES_INDEX.md + SCREENS_INDEX.md
- **Platform**: Count working platforms in LAYER_STATUS.md

### Step 3: Identify Gaps

From index files, find items marked ⚠️ or ❌:
- Missing mockups → `MOCKUPS_INDEX.md`
- Missing services → `FEATURE_MAP.md`
- Missing screens → `SCREENS_INDEX.md`
- Platform issues → `LAYER_STATUS.md`

### Step 4: Generate Output

Fill the comprehensive template with:
1. Real percentages from index files
2. All implemented items (✅)
3. All gaps (⚠️/❌) with `/gap-planning` commands
4. Recommended next steps based on priorities

---

## Layer-Specific Parameters

When a layer parameter is provided, show detailed view for that layer:

| Parameter | Shows |
|-----------|-------|
| `design` | All 17 features: SPEC, API, STATUS, Mockups status |
| `design mockup` | Mockup-specific: Figma links, Stitch prompts, design-tokens |
| `design spec` | Specification status for all features |
| `server` | All 11 endpoint categories with endpoint counts |
| `client` | All services (13) and repositories (17) |
| `client network` | Network services only |
| `client data` | Repositories only |
| `feature` | All 23 modules with screens, ViewModels, DI |
| `feature [name]` | Single feature: all layers |
| `platform` | All 4 platforms with build commands |
| `platform [name]` | Single platform details |
| `testing` | Testing coverage across all layers |
| `testing [layer]` | Testing for specific layer (design/client/feature/platform) |

---

## Feature Reference

| # | Feature | Design Dir | Feature Dir |
|:-:|---------|------------|-------------|
| 1 | auth | features/auth/ | feature/auth/ |
| 2 | home | features/home/ | feature/home/ |
| 3 | accounts | features/accounts/ | feature/account/ |
| 4 | savings-account | features/savings-account/ | feature/savings-account/ |
| 5 | loan-account | features/loan-account/ | feature/loan-account/ |
| 6 | share-account | features/share-account/ | feature/share-account/ |
| 7 | beneficiary | features/beneficiary/ | feature/beneficiary/ |
| 8 | transfer | features/transfer/ | feature/transfer-process/ |
| 9 | recent-transaction | features/recent-transaction/ | feature/recent-transaction/ |
| 10 | notification | features/notification/ | feature/notification/ |
| 11 | settings | features/settings/ | feature/settings/ |
| 12 | passcode | features/passcode/ | libs/mifos-passcode/ |
| 13 | guarantor | features/guarantor/ | feature/guarantor/ |
| 14 | qr | features/qr/ | feature/qr-code/ |
| 15 | location | features/location/ | feature/location/ |
| 16 | client-charge | features/client-charge/ | feature/user-profile/ |
| 17 | dashboard | features/dashboard/ | feature/dashboard/ |

---

## Output Rules

1. **Read index files only** - Never scan directories when index files exist
2. **Show everything** - All implemented + all gaps in one view
3. **Include all `/gap-planning` commands** - For every gap found
4. **Use progress bars** - Visual at-a-glance status
5. **Prioritize gaps** - P0 → P1 → P2
6. **Show recommended next steps** - Based on current gaps
7. **NO interactive questions** - Comprehensive view, user decides

---

## Progress Bar Reference

```
100% = [██████████]  |  50% = [█████░░░░░]
 95% = [█████████▌]  |  40% = [████░░░░░░]
 90% = [█████████░]  |  30% = [███░░░░░░░]
 80% = [████████░░]  |  20% = [██░░░░░░░░]
 70% = [███████░░░]  |  10% = [█░░░░░░░░░]
 60% = [██████░░░░]  |   0% = [░░░░░░░░░░]
```

**Status Icons**: ✅ Complete | ⚠️ Partial | ❌ Missing | `-` N/A
