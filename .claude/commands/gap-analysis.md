# Gap Analysis Command

Analyze implementation status and identify gaps between v2.0 design specifications and current implementation across all layers.

## Usage

```
/gap-analysis              # Show full dashboard with all features
/gap-analysis [Feature]    # Detailed analysis for specific feature
```

## Instructions

### Step 1: Read Current Status

**ALWAYS** read these files first:
1. `claude-product-cycle/PRODUCT_MAP.md` - Master status tracker
2. `claude-product-cycle/design-spec-layer/features/[Feature]/STATUS.md` - Feature status

### Step 2: Show Enhanced Dashboard

When `/gap-analysis` is run without parameters, show this comprehensive dashboard:

```markdown
## Mifos Mobile - Gap Analysis Dashboard

**App Version**: vX.X.X | **Last Updated**: YYYY-MM-DD
**Design System**: v2.0 (2025 Fintech Patterns)

---

### Feature Matrix

| # | Feature | Design | Mockup | Client | Feature | Platform Gap |
|:-:|---------|:------:|:------:|:------:|:-------:|--------------|
| 1 | **auth** | SPEC | v2.0 | Network/Data | VM+Screen | [Gap or None] |
| 2 | **home** | SPEC | v2.0 | Network/Data | VM+Screen | [Gap or None] |
| ... | ... | ... | ... | ... | ... | ... |
| A | **All Features** | - | - | - | - | Full analysis |

**Legend**:
- Design: SPEC.md + API.md complete
- Mockup: ✅ v2.0 = Redesigned | ❌ = Needs update
- Client: ✅ = Network + Data layers | ⚠️ = Partial | ❌ = Missing
- Feature: ✅ = ViewModel + Screen | ⚠️ = Partial | ❌ = Missing

---

### Overall Health Metrics

```
┌─────────────────────────────────────────────────────────────────┐
│  PROJECT HEALTH                                                  │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  Design Layer     ██████████████████████████████████████  100%  │
│    - SPEC.md      ██████████████████████████████████████  100%  │
│    - API.md       ██████████████████████████████████████  100%  │
│    - MOCKUP v2.0  ██████████████████████████████████████  100%  │
│    - STATUS.md    ██████████████████████████████████████  100%  │
│                                                                  │
│  Client Layer     ██████████████████████████████████████   95%  │
│    - Network      ██████████████████████████████████████  100%  │
│    - Data         ██████████████████████████████████████   95%  │
│                                                                  │
│  Feature Layer    ██████████████████████████████████░░░░   90%  │
│    - ViewModels   ██████████████████████████████████████   95%  │
│    - Screens      ████████████████████████████████░░░░░░   85%  │
│    - v2.0 Match   ██████████░░░░░░░░░░░░░░░░░░░░░░░░░░░░   30%  │
│                                                                  │
│  OVERALL          ██████████████████████████████░░░░░░░░   75%  │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

Use █ for filled (each █ = ~2.8%), ░ for empty. Round to nearest block.

---

### Platform Support

| Platform | Auth | Home | Accounts | Transfer | Overall | Production Ready |
|----------|:----:|:----:|:--------:|:--------:|:-------:|:----------------:|
| **🤖 Android** | ✅ | ✅ | ✅ | ✅ | **100%** | ✅ |
| **🍎 iOS** | ✅ | ✅ | ✅ | ✅ | **100%** | ✅ |
| **🖥️ Desktop** | ✅ | ✅ | ✅ | ✅ | **100%** | ⚠️ |
| **🌐 Web** | ⚠️ | ⚠️ | ⚠️ | ⚠️ | **80%** | ⚠️ |

---

### v2.0 Design vs Implementation Gaps

| Feature | Mockup v2.0 | Current UI | Gap Summary |
|---------|-------------|------------|-------------|
| auth | Biometric-first, gradients | Basic form | Major redesign |
| home | AI assistant, analytics | Simple list | Major redesign |
| accounts | Portfolio view, rings | Tab list | Medium redesign |
| ... | ... | ... | ... |

---

### Critical Gaps (P0)

| Feature | Gap | Layer | Impact | Effort |
|---------|-----|-------|--------|--------|
| dashboard | Feature layer not implemented | Feature | High | L |

If none: `| *None* | All critical gaps resolved | - | - | - |`

---

### High Priority Gaps (P1) - v2.0 Design Alignment

| Feature | Gap | Layer | Impact | Effort |
|---------|-----|-------|--------|--------|
| home | Missing spending analytics chart | Feature | High | M |
| home | Missing AI assistant entry | Feature | Medium | M |
| accounts | Missing portfolio allocation ring | Feature | High | M |
| transfer | Missing quick amount shortcuts | Feature | Medium | S |

---

### Quick Wins (P2)

| Feature | Gap | Layer | Impact | Effort |
|---------|-----|-------|--------|--------|
| all | Add gradient hero cards | Feature | Medium | S |
| all | Add haptic feedback | Feature | Low | S |
| all | Update color tokens | Design | Low | S |

---

### Feature Completeness by Category

```
AUTHENTICATION
├── auth            [██████████] 100% ✅ All platforms
└── passcode        [██████████] 100% ✅ All platforms

ACCOUNTS & DASHBOARD
├── home            [██████████] 100% ⚠️ v2.0 design gap
├── accounts        [██████████] 100% ⚠️ v2.0 design gap
├── dashboard       [██████░░░░]  60% ❌ Feature layer missing
├── savings-account [██████████] 100% ⚠️ v2.0 design gap
├── loan-account    [██████████] 100% ⚠️ v2.0 design gap
└── share-account   [██████████] 100% ⚠️ v2.0 design gap

TRANSFERS
├── transfer        [██████████] 100% ⚠️ v2.0 design gap
└── beneficiary     [██████████] 100% ⚠️ v2.0 design gap

PROFILE & SETTINGS
├── settings        [██████████] 100% ⚠️ v2.0 design gap
├── client-charge   [██████████] 100% ⚠️ v2.0 design gap
├── recent-txn      [██████████] 100% ⚠️ v2.0 design gap
├── notification    [██████████] 100% ⚠️ v2.0 design gap
├── guarantor       [██████████] 100% ⚠️ v2.0 design gap
├── location        [██████████] 100% ⚠️ v2.0 design gap
└── qr              [██████████] 100% ⚠️ v2.0 design gap
```

---

### Recommended Actions

**For v2.0 Design Implementation**:
1. Prioritize hero card gradient updates (quick visual win)
2. Implement spending analytics chart (high user value)
3. Add AI assistant entry point (differentiator)
4. Implement gamification elements (streaks, badges)

**For Dashboard Feature**:
1. Run `/implement dashboard` to create feature layer
2. Aggregate existing client layer services
3. Build unified portfolio view

**For Platform Parity**:
1. Test Web/WASM builds thoroughly
2. Verify Desktop touch interactions
3. iOS-specific optimizations

---

**Select a feature number (1-17) or 'A' for detailed analysis:**
```

### Step 3: Handle Feature Selection

After user selects a specific feature (e.g., "1" for auth or "home"):

1. Read `claude-product-cycle/design-spec-layer/features/{FEATURE}/MOCKUP.md`
2. Read `claude-product-cycle/design-spec-layer/features/{FEATURE}/STATUS.md`
3. Read `claude-product-cycle/design-spec-layer/features/{FEATURE}/SPEC.md`
4. Check actual implementation in `feature/{FEATURE}/`
5. Output detailed feature analysis:

```markdown
## Gap Analysis: [Feature Name]

**Design Status**: SPEC ✅ | API ✅ | MOCKUP v2.0 ✅ | STATUS ✅
**Implementation Status**: Client ✅ | Feature ⚠️ (v2.0 gap)
**Last Updated**: YYYY-MM-DD

---

### Layer Status

| Layer | Component | Status | File Location |
|-------|-----------|:------:|---------------|
| Design | SPEC.md | ✅ | claude-product-cycle/.../SPEC.md |
| Design | API.md | ✅ | claude-product-cycle/.../API.md |
| Design | MOCKUP v2.0 | ✅ | claude-product-cycle/.../MOCKUP.md |
| Client | Network Service | ✅ | core/network/services/...Service.kt |
| Client | Repository | ✅ | core/data/repository/...Repository.kt |
| Feature | ViewModel | ✅ | feature/.../viewmodel/...ViewModel.kt |
| Feature | Screen | ⚠️ | feature/.../...Screen.kt |
| Feature | Navigation | ✅ | feature/.../navigation/...Navigation.kt |
| Feature | DI Module | ✅ | feature/.../di/...Module.kt |

---

### v2.0 MOCKUP vs Current Implementation

| MOCKUP v2.0 Feature | Current Status | Gap |
|---------------------|:--------------:|-----|
| Hero gradient card (#667EEA → #764BA2) | ❌ | Not implemented |
| Spending analytics chart | ❌ | Not implemented |
| AI assistant entry point | ❌ | Not implemented |
| Gamification (streaks) | ❌ | Not implemented |
| Quick action bar | ⚠️ | Partial - missing freeze |
| Recent recipients carousel | ❌ | Not implemented |
| Micro-animations | ❌ | Not implemented |
| Dark mode support | ⚠️ | Basic only |

---

### Platform Status

| Platform | Status | Notes |
|----------|:------:|-------|
| 🤖 Android | ✅ 100% | Full support |
| 🍎 iOS | ✅ 100% | Full support |
| 🖥️ Desktop | ✅ 95% | Minor touch issues |
| 🌐 Web | ⚠️ 80% | Some components need fixes |

---

### Implementation Gaps

| # | Gap | Type | Priority | Effort |
|---|-----|------|:--------:|:------:|
| 1 | Hero gradient card | UI | P1 | S |
| 2 | Spending analytics chart | UI+Data | P1 | M |
| 3 | AI assistant entry | UI | P2 | S |
| 4 | Gamification badges | UI+Data | P2 | M |
| 5 | Micro-animations | UI | P2 | S |

---

### Files to Update

| File | Changes Needed |
|------|----------------|
| `feature/.../Screen.kt` | Implement v2.0 design |
| `feature/.../ViewModel.kt` | Add analytics/gamification state |
| `core/designsystem/theme/...` | Add gradient definitions |

---

### Testing Status

| Test Type | Coverage | Status |
|-----------|:--------:|:------:|
| Unit Tests | 80% | ⚠️ |
| UI Tests | 20% | ❌ |
| Integration | 60% | ⚠️ |

---

### Recommended Actions (Priority Order)

1. **[P0]** Fix any broken functionality first
2. **[P1]** Implement hero gradient cards (visual quick win)
3. **[P1]** Add spending analytics (high user value)
4. **[P2]** Implement micro-animations
5. **[P2]** Add gamification elements

---

**Ready to implement?**
- `/gap-planning [Feature]` - Plan detailed implementation
- `/implement [Feature]` - Full E2E implementation
- `/feature [Feature]` - Update UI layer only
```

### Step 4: Handle "A" (All Features) Selection

If user selects "A", provide the full dashboard plus:

```markdown
---

## Detailed Feature Summary

| # | Feature | Design | Mockup | Client | Feature | v2.0 Gap |
|---|---------|:------:|:------:|:------:|:-------:|----------|
| 1 | auth | ✅ | v2.0 | ✅ | ✅ | UI redesign |
| 2 | home | ✅ | v2.0 | ✅ | ✅ | UI redesign + analytics |
| 3 | accounts | ✅ | v2.0 | ✅ | ✅ | UI redesign |
| ... | ... | ... | ... | ... | ... | ... |
| 17 | dashboard | ✅ | v2.0 | ✅ | ❌ | Feature layer missing |

---

## Library Excellence Metrics

| Category | Current | Target | Action |
|----------|:-------:|:------:|--------|
| Design Specs | 100% | 100% | Complete |
| MOCKUP v2.0 | 100% | 100% | Complete |
| Client Layer | 95% | 100% | Minor gaps |
| Feature Layer | 90% | 100% | Dashboard missing |
| v2.0 UI Match | 0% | 100% | Major effort |
| Test Coverage | 60% | 90% | Add tests |
| Platform Parity | 90% | 100% | Web fixes |

---

## Roadmap Priorities

### Immediate (High Impact, Low Effort)
| Task | Feature | Impact | Effort |
|------|---------|--------|--------|
| Add gradient hero cards | all | High | S |
| Update color tokens | designsystem | Medium | S |
| Add haptic feedback | all | Low | S |

### Short-term (v2.0 Core Features)
| Task | Feature | Impact | Effort |
|------|---------|--------|--------|
| Spending analytics chart | home | High | M |
| Portfolio allocation ring | accounts | High | M |
| Implement dashboard feature | dashboard | High | L |
| Quick amount shortcuts | transfer | Medium | S |

### Long-term (Polish & Enhancement)
| Task | Feature | Impact | Effort |
|------|---------|--------|--------|
| AI assistant integration | home | High | L |
| Full gamification system | all | Medium | L |
| Micro-animations suite | all | Medium | M |
| Web platform fixes | all | Medium | M |

---

**Which feature would you like to work on?**
- Enter feature number (1-17) for detailed analysis
- `/gap-planning [Feature]` to plan implementation
- `/implement [Feature]` to start full implementation
```

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
| 17 | dashboard | features/dashboard/ | (NOT YET CREATED) |

## Output Rules

1. **Always read PRODUCT_MAP.md first** - Source of truth for all status
2. **Use visual progress bars** - Makes status instantly scannable
3. **Include platform columns** - Critical for KMP application
4. **Compare v2.0 MOCKUP vs current** - Key gap identification
5. **Group by priority** - P0 > P1 > P2
6. **Be specific** - File paths, not vague descriptions
7. **Be actionable** - Every gap has a clear fix
8. **Estimate effort** - S (<1hr), M (1-4hr), L (>4hr)

## Platform Icons

- 🤖 Android
- 🍎 iOS
- 🖥️ Desktop (JVM)
- 🌐 Web (JS/WASM)

## Status Icons

- ✅ Full implementation
- ⚠️ Partial/needs update
- ❌ Not implemented

## Progress Bar Reference

```
100% = [██████████]
 90% = [█████████░]
 80% = [████████░░]
 70% = [███████░░░]
 60% = [██████░░░░]
 50% = [█████░░░░░]
 40% = [████░░░░░░]
 30% = [███░░░░░░░]
 20% = [██░░░░░░░░]
 10% = [█░░░░░░░░░]
  0% = [░░░░░░░░░░]
```

## Workflow

```
/gap-analysis              →  View dashboard with all features
        ↓
Select feature (1-17)      →  Detailed feature analysis
        ↓
/gap-planning [Feature]    →  Plan implementation
        ↓
/implement [Feature]       →  Full E2E implementation
        ↓
/verify [Feature]          →  Confirm implementation matches spec
```
