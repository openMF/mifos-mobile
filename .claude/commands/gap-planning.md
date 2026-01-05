# Gap Planning Command

Creates step-by-step implementation plans for identified gaps. Runs on O(1) by reading index files.

## Usage

```
/gap-planning                        # Show ALL gaps with ALL plan commands
/gap-planning design                 # Plan all design layer work
/gap-planning design mockup          # Plan mockup generation (10 features)
/gap-planning design spec            # Plan specification updates
/gap-planning server                 # Plan server documentation
/gap-planning client                 # Plan all client layer work
/gap-planning client network         # Plan network services
/gap-planning client data            # Plan repositories
/gap-planning feature                # Plan all feature layer work
/gap-planning feature [name]         # Plan specific feature
/gap-planning platform               # Plan all platform work
/gap-planning platform web           # Plan web stabilization
/gap-planning testing                # Plan all testing work
/gap-planning testing client         # Plan client layer tests
/gap-planning testing feature        # Plan feature layer tests (VM + UI)
/gap-planning testing platform       # Plan E2E + screenshot tests
/gap-planning testing [feature]      # Plan tests for specific feature
/gap-planning [feature-name]         # Plan specific feature (all 5 layers)
```

---

## Comprehensive Output (No Parameters)

When `/gap-planning` is called without parameters, show **ALL gaps with ALL implementation plans**:

```
╔══════════════════════════════════════════════════════════════════════════════╗
║  MIFOS MOBILE - GAP PLANNING (O(1) Lookup)                                   ║
║  All Gaps → All Plans → You Choose                                           ║
╠══════════════════════════════════════════════════════════════════════════════╣

## Current Gaps Overview

| Layer | Gaps | Priority | Status |
|-------|:----:|:--------:|--------|
| Design | 10 mockups | P1 | Ready to plan |
| Server | 0 | - | ✅ Complete |
| Client | 0 | - | ✅ Complete |
| Feature | 0 | - | ✅ Complete |
| Platform | 1 (web) | P2 | Ready to plan |

---

## 📋 ALL AVAILABLE PLANS

### P0 - Critical (Blocks Other Work)

| Gap | Plan Command | Tasks | Effort |
|-----|--------------|:-----:|:------:|
| (none currently) | - | - | - |

### P1 - High Priority (User-Facing)

| # | Gap | Plan Command | Tasks | Effort |
|:-:|-----|--------------|:-----:|:------:|
| 1 | Missing mockups (10 features) | `/gap-planning design mockup` | 30 | L |
| 2 | Missing design-tokens (9 features) | `/gap-planning design mockup` | 18 | M |

**Design Mockup Tasks Preview**:
```
Features needing mockups:
1. accounts      → 3 screens (List, Detail, Transactions)
2. beneficiary   → 4 screens (List, Add, Edit, Detail)
3. dashboard     → 1 screen (Overview)
4. home          → 2 screens (Home, Profile)
5. loan-account  → 4 screens (List, Detail, Schedule, Summary)
6. notification  → 1 screen (List)
7. recent-transaction → 1 screen (List)
8. savings-account → 4 screens (List, Detail, Update, Withdraw)
9. share-account → 2 screens (List, Detail)
10. transfer     → 2 screens (Form, Confirmation)

Run `/gap-planning design mockup` for step-by-step tasks.
```

### P2 - Nice to Have (Polish)

| # | Gap | Plan Command | Tasks | Effort |
|:-:|-----|--------------|:-----:|:------:|
| 1 | Web experimental | `/gap-planning platform web` | 5 | M |

**Web Platform Tasks Preview**:
```
1. Fix Kotlin/JS compilation warnings
2. Add CORS handling for production
3. Implement WebSocket fallback
4. Optimize bundle size
5. Add Safari compatibility fixes

Run `/gap-planning platform web` for step-by-step tasks.
```

### 🧪 Testing (Embedded in Layers)

| # | Gap | Plan Command | Tests | Effort |
|:-:|-----|--------------|:-----:|:------:|
| 1 | ViewModel tests (0/49) | `/gap-planning testing feature` | 200+ | L |
| 2 | UI tests (0/63 screens) | `/gap-planning testing feature` | 150+ | L |
| 3 | E2E tests (0/8 flows) | `/gap-planning testing platform` | 30+ | M |
| 4 | Screenshot tests (0/30) | `/gap-planning testing platform` | 60+ | M |
| 5 | Repository tests (partial) | `/gap-planning testing client` | 50+ | M |

**Testing Priority by Feature**:
```
P0 - Core: auth, home, accounts, transfer
P1 - Accounts: beneficiary, loan, savings
P2 - Supporting: settings, notification, qr, passcode
P3 - Other: guarantor, location, dashboard
```

→ Run `/gap-planning testing [feature]` for per-feature test plan.

---

## 🎯 QUICK START

Pick a plan based on priority:

| Priority | Recommendation | Command |
|:--------:|----------------|---------|
| **P1** | Start with mockups | `/gap-planning design mockup` |
| **P2** | Then web platform | `/gap-planning platform web` |

Or jump directly to implementation:

| Target | Command |
|--------|---------|
| Single feature mockup | `/design [feature-name]` |
| Feature implementation | `/implement [feature-name]` |
| Verify existing | `/verify [feature-name]` |

---

## 🔄 WORKFLOW

```
/gap-analysis           →  See all status (O(1) comprehensive view)
       │
       ▼
/gap-planning           →  See all plans (this view)
       │
       ▼
/gap-planning [target]  →  Get detailed step-by-step tasks
       │
       ▼
/implement [target]     →  Execute the plan
       │
       ▼
/verify [target]        →  Confirm completion
       │
       ▼
/gap-analysis           →  Updated status (loop back)
```

╚══════════════════════════════════════════════════════════════════════════════╝
```

---

## Detailed Plans (With Parameter)

When a specific target is provided, show the **detailed step-by-step plan**.

### Design Mockup Plan (`/gap-planning design mockup`)

```
## Design Mockup Generation Plan

**Target**: 10 features needing mockups
**Effort**: Large (30 tasks across 10 features)
**Tool**: Google Stitch / Figma

### Features & Tasks

| # | Feature | Screens | Tasks | Priority |
|:-:|---------|:-------:|:-----:|:--------:|
| 1 | accounts | 3 | 6 | P1 |
| 2 | beneficiary | 4 | 8 | P1 |
| 3 | dashboard | 1 | 2 | P0 |
| 4 | home | 2 | 4 | P1 |
| 5 | loan-account | 4 | 8 | P1 |
| 6 | notification | 1 | 2 | P2 |
| 7 | recent-transaction | 1 | 2 | P2 |
| 8 | savings-account | 4 | 8 | P1 |
| 9 | share-account | 2 | 4 | P2 |
| 10 | transfer | 2 | 4 | P1 |

### Per-Feature Tasks

For each feature:
1. Read SPEC.md to understand screens
2. Read API.md to understand data
3. Generate PROMPTS_STITCH.md for Google Stitch
4. Generate mockup images
5. Create design-tokens.json
6. Update FIGMA_LINKS.md with URLs

### Execution Commands

| Feature | Command |
|---------|---------|
| dashboard (P0) | `/design dashboard mockup` |
| accounts | `/design accounts mockup` |
| beneficiary | `/design beneficiary mockup` |
| home | `/design home mockup` |
| loan-account | `/design loan-account mockup` |
| savings-account | `/design savings-account mockup` |
| transfer | `/design transfer mockup` |
| notification | `/design notification mockup` |
| recent-transaction | `/design recent-transaction mockup` |
| share-account | `/design share-account mockup` |

### Verification

After each feature:
- [ ] PROMPTS_STITCH.md exists
- [ ] Mockup images generated
- [ ] design-tokens.json created
- [ ] FIGMA_LINKS.md updated
- [ ] MOCKUPS_INDEX.md updated
```

### Platform Web Plan (`/gap-planning platform web`)

```
## Web Platform Stabilization Plan

**Target**: Move web from experimental to stable
**Effort**: Medium (5 tasks)
**Module**: cmp-web

### Current Status

| Issue | Impact | Fix |
|-------|--------|-----|
| Kotlin/JS warnings | Build noise | Suppress/fix |
| CORS in production | API blocked | Server headers |
| WebSocket issues | Real-time fails | Polling fallback |
| Large bundle | Slow load | Tree shaking |
| Safari compat | 15% users | Polyfills |

### Tasks

1. **Fix compilation warnings**
   - File: `cmp-web/build.gradle.kts`
   - Action: Add suppressions or fix warnings

2. **CORS configuration**
   - File: Server config (Fineract)
   - Action: Add Access-Control-Allow-Origin headers

3. **WebSocket fallback**
   - File: `cmp-shared/.../network/`
   - Action: Implement polling when WebSocket fails

4. **Bundle optimization**
   - File: `cmp-web/build.gradle.kts`
   - Action: Enable tree shaking, code splitting

5. **Safari compatibility**
   - File: `cmp-web/src/jsMain/resources/`
   - Action: Add polyfills for missing APIs

### Verification

- [ ] `./gradlew :cmp-web:jsBrowserProductionWebpack` builds clean
- [ ] App loads in Safari
- [ ] API calls work in production
- [ ] Bundle size < 2MB
```

---

## Instructions for Claude

### Step 1: Read O(1) Index Files

Read these files for gap information:

| Need | Index File | Path |
|------|------------|------|
| Design gaps | MOCKUPS_INDEX.md | `design-spec-layer/MOCKUPS_INDEX.md` |
| Feature gaps | MODULES_INDEX.md | `feature-layer/MODULES_INDEX.md` |
| Client gaps | FEATURE_MAP.md | `client-layer/FEATURE_MAP.md` |
| Platform gaps | LAYER_STATUS.md | `platform-layer/LAYER_STATUS.md` |

### Step 2: Identify Gaps

From index files, find items marked ⚠️ or ❌:
- Design: Features missing mockups, design-tokens
- Client: Missing services or repositories
- Feature: Missing screens or ViewModels
- Platform: Experimental or broken builds

### Step 3: Generate Plans

For each gap found:
1. Determine priority (P0/P1/P2)
2. List specific tasks
3. Estimate effort (S/M/L)
4. Provide execution commands
5. Add verification checklist

### Step 4: Output Format

- **No parameters**: Show all gaps + all plan summaries
- **With layer**: Show detailed plan for that layer
- **With feature**: Show detailed plan for that feature

---

## Priority Guidelines

| Priority | Criteria | Examples |
|----------|----------|----------|
| P0 | Critical - blocks other work | Missing feature module |
| P1 | High value - user-facing | v2.0 UI, mockups |
| P2 | Polish - nice to have | Animations, web fixes |

## Effort Guidelines

| Effort | Scope | Tasks |
|--------|-------|:-----:|
| S | Single file change | 1-3 |
| M | Multiple files, one area | 4-10 |
| L | Feature-wide or cross-cutting | 10+ |

---

## Output Rules

1. **Read index files only** - Use O(1) lookup
2. **Show all gaps** - No hidden information
3. **Show all commands** - For every gap
4. **Include effort estimates** - S/M/L
5. **Prioritize** - P0 → P1 → P2
6. **Provide verification** - Checklist for each plan
7. **NO interactive questions** - Show everything, user decides
