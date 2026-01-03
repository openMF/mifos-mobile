# Gap Analysis Command

Analyze implementation status across the 5-layer product lifecycle.

## Usage

```
/gap-analysis                    # Full dashboard
/gap-analysis design             # Design layer (SPEC + MOCKUP + API)
/gap-analysis server             # Server layer (Fineract endpoints)
/gap-analysis client             # Client layer (Network + Data)
/gap-analysis feature            # Feature layer (ViewModel + Screen)
/gap-analysis platform           # Platform layer (Android, iOS, Desktop, Web)
/gap-analysis [feature-name]     # Specific feature (all 5 layers)
```

## 5-Layer Lifecycle

```
1. Design   → SPEC.md + MOCKUP.md + API.md
2. Server   → Fineract API endpoints
3. Client   → Network services + Repositories
4. Feature  → ViewModel + Screen + Navigation
5. Platform → Android, iOS, Desktop, Web
```

## Instructions

### Step 1: Determine Output Type

| Parameter | Template | Action |
|-----------|----------|--------|
| (none) | `templates/gap-analysis/dashboard.md` | Full dashboard |
| `design` | `templates/gap-analysis/layer-design.md` | Design layer |
| `server` | `templates/gap-analysis/layer-server.md` | Server layer |
| `client` | `templates/gap-analysis/layer-client.md` | Client layer |
| `feature` | `templates/gap-analysis/layer-feature.md` | Feature layer |
| `platform` | `templates/gap-analysis/layer-platform.md` | Platform layer |
| `[name]` | `templates/gap-analysis/feature-detail.md` | Feature detail |

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
