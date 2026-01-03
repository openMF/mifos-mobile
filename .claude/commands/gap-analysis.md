# Gap Analysis Command

Brief entry point to analyze implementation status. Shows where work is needed across the 5-layer lifecycle.

## Usage

```
/gap-analysis                        # Brief overview (all layers summary)
/gap-analysis design                 # Design layer status
/gap-analysis design mockup          # Design → Mockup sub-section only
/gap-analysis design spec            # Design → Spec sub-section only
/gap-analysis server                 # Server layer status
/gap-analysis client                 # Client layer status
/gap-analysis client network         # Client → Network sub-section
/gap-analysis client data            # Client → Data sub-section
/gap-analysis feature                # Feature layer status
/gap-analysis feature [name]         # Feature → specific feature
/gap-analysis platform               # Platform layer status
/gap-analysis platform android       # Platform → Android only
/gap-analysis [feature-name]         # Specific feature (all 5 layers)
```

## 5-Layer Lifecycle with Sub-Sections

```
1. Design   → spec | mockup | api | status
2. Server   → endpoints | availability
3. Client   → network | data | model
4. Feature  → viewmodel | screen | navigation | di
5. Platform → android | ios | desktop | web
```

## Brief Overview Output (No Parameters)

When `/gap-analysis` is called without parameters, show a **brief summary**:

```
## Gap Analysis - Quick Overview

| Layer | Progress | Gaps | Next Action |
|-------|:--------:|:----:|-------------|
| Design | 85% | mockups/ (16) | /gap-analysis design mockup |
| Server | 100% | - | - |
| Client | 95% | 1 service | /gap-analysis client |
| Feature | 94% | dashboard | /gap-analysis feature |
| Platform | 90% | web fixes | /gap-analysis platform |

**Next Step**: Run `/gap-analysis [layer]` for details, or `/gap-planning [layer]` to plan.
```

## Instructions

### Step 1: Determine Output Type

**Layer Parameters**:
| Parameter | Template | Action |
|-----------|----------|--------|
| (none) | Brief summary | Quick overview of all layers |
| `design` | `layer-design.md` | Full design layer status |
| `server` | `layer-server.md` | Server layer status |
| `client` | `layer-client.md` | Client layer status |
| `feature` | `layer-feature.md` | Feature layer status |
| `platform` | `layer-platform.md` | Platform layer status |
| `[name]` | `feature-detail.md` | Specific feature (all layers) |

**Sub-Section Parameters** (layer + sub-section):
| Parameters | Template | Action |
|------------|----------|--------|
| `design mockup` | `subsection/design-mockup.md` | Mockup generation status |
| `design spec` | `subsection/design-spec.md` | Specification status |
| `design api` | `subsection/design-api.md` | API documentation status |
| `client network` | `subsection/client-network.md` | Network services status |
| `client data` | `subsection/client-data.md` | Repository status |
| `feature [name]` | `subsection/feature-single.md` | Single feature status |
| `platform android` | `subsection/platform-android.md` | Android-only status |
| `platform ios` | `subsection/platform-ios.md` | iOS-only status |
| `platform desktop` | `subsection/platform-desktop.md` | Desktop-only status |
| `platform web` | `subsection/platform-web.md` | Web-only status |

### Step 2: Read Status Files

| Layer | Files to Read |
|-------|---------------|
| Design | `design-spec-layer/STATUS.md`, check each `features/*/` folder |
| Server | `server-layer/FINERACT_API.md` |
| Client | `client-layer/LAYER_STATUS.md`, check `core/network/services/` |
| Feature | `feature-layer/LAYER_STATUS.md`, check `feature/*/` folders |
| Platform | Check `cmp-android/`, `cmp-ios/`, `cmp-desktop/`, `cmp-web/` |

### Step 3: Calculate Percentages

For each layer, count actual files:
- Design: Count SPEC.md, MOCKUP.md, API.md, STATUS.md per feature
- Client: Count *Service.kt in `core/network/services/`
- Feature: Count *ViewModel.kt, *Screen.kt in `feature/*/`
- Calculate: `exists / expected * 100`

### Step 4: Fill Template

Read template from `claude-product-cycle/templates/gap-analysis/` and replace placeholders with real data.

**Progress Bar Reference**:
```
100% = [██████████]  |  50% = [█████░░░░░]
 90% = [█████████░]  |  40% = [████░░░░░░]
 80% = [████████░░]  |  30% = [███░░░░░░░]
 70% = [███████░░░]  |  20% = [██░░░░░░░░]
 60% = [██████░░░░]  |  10% = [█░░░░░░░░░]
```

**Status Icons**: ✅ Complete | ⚠️ Partial | ❌ Missing | `-` N/A

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

## Output Rules

1. Read actual files - don't assume
2. Calculate real percentages
3. Use progress bars for visibility
4. List specific gaps with file paths
5. Suggest next command (gap-planning or implement)
