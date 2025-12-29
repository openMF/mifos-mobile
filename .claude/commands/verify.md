# /verify - Implementation Verification

## Purpose
Validate that the implementation matches the specification. Identify gaps between SPEC.md and actual code.

---

## Workflow

```
┌───────────────────────────────────────────────────────────────────┐
│                    /verify [Feature] WORKFLOW                      │
├───────────────────────────────────────────────────────────────────┤
│                                                                    │
│  PHASE 1: READ SPEC                                               │
│  ├─→ Read features/[feature]/SPEC.md                              │
│  ├─→ Extract all UI sections                                      │
│  ├─→ Extract all user actions                                     │
│  ├─→ Extract state model                                          │
│  └─→ Extract API requirements                                     │
│                                                                    │
│  PHASE 2: CHECK ACTUAL CODE                                       │
│  ├─→ Read feature/[name]/*ViewModel.kt                            │
│  ├─→ Read feature/[name]/*Screen.kt                               │
│  ├─→ Read feature/[name]/components/*.kt                          │
│  ├─→ Read core/network/services/*Service.kt                       │
│  └─→ Read core/data/repository/*Repository.kt                     │
│                                                                    │
│  PHASE 3: COMPARE SPEC VS CODE                                    │
│  ├─→ All sections from spec implemented?                          │
│  ├─→ All user actions handled?                                    │
│  ├─→ State model matches?                                         │
│  ├─→ All API calls present?                                       │
│  └─→ DI registration complete?                                    │
│                                                                    │
│  PHASE 4: CHECK LAYER INTEGRITY                                   │
│  ├─→ Network → Data → Feature flow correct?                       │
│  ├─→ No layer violations?                                         │
│  └─→ Navigation configured?                                       │
│                                                                    │
│  PHASE 5: GENERATE REPORT                                         │
│  ├─→ List all gaps found                                          │
│  ├─→ List suggestions for improvement                             │
│  └─→ Output: Gap report or "✅ Feature verified"                  │
│                                                                    │
└───────────────────────────────────────────────────────────────────┘
```

---

## Verification Checklist

### UI Sections
- [ ] All sections from SPEC.md ASCII mockup present in Screen
- [ ] Loading state handled
- [ ] Error state handled
- [ ] Empty state handled (if applicable)

### User Actions
- [ ] All actions from SPEC.md handled in ViewModel
- [ ] Actions trigger correct events/state changes
- [ ] Navigation works correctly

### State Model
- [ ] State class matches SPEC.md definition
- [ ] All required fields present
- [ ] Correct default values

### API Integration
- [ ] All required endpoints called
- [ ] Error handling for API failures
- [ ] Loading states during API calls

### DI Registration
- [ ] ViewModel registered in module
- [ ] Repository registered in module
- [ ] Service registered in module

### Navigation
- [ ] Route defined
- [ ] Screen registered in nav graph
- [ ] Navigation parameters correct

---

## Output Templates

### All Good:

```
╔══════════════════════════════════════════════════════════════════════╗
║  ✅ VERIFICATION COMPLETE - [Feature]                                 ║
╠══════════════════════════════════════════════════════════════════════╣
║                                                                       ║
║  UI SECTIONS:         ✅ All 5 sections implemented                   ║
║  USER ACTIONS:        ✅ All 8 actions handled                        ║
║  STATE MODEL:         ✅ Matches specification                        ║
║  API INTEGRATION:     ✅ All 3 endpoints called                       ║
║  DI REGISTRATION:     ✅ Complete                                     ║
║  NAVIGATION:          ✅ Configured                                   ║
║                                                                       ║
║  RESULT: Feature fully implements specification                       ║
║                                                                       ║
╚══════════════════════════════════════════════════════════════════════╝
```

### Gaps Found:

```
╔══════════════════════════════════════════════════════════════════════╗
║  ⚠️  VERIFICATION COMPLETE - GAPS FOUND                               ║
╠══════════════════════════════════════════════════════════════════════╣
║                                                                       ║
║  FEATURE: [Feature]                                                   ║
║  SPEC: claude-product-cycle/design-spec-layer/features/[feature]/SPEC.md║
║                                                                       ║
╠══════════════════════════════════════════════════════════════════════╣
║  GAPS IDENTIFIED                                                      ║
╠══════════════════════════════════════════════════════════════════════╣
║                                                                       ║
║  UI SECTIONS (2 gaps):                                                ║
║  ├─ ❌ Empty state not implemented                                    ║
║  └─ ❌ Pull-to-refresh missing                                        ║
║                                                                       ║
║  USER ACTIONS (1 gap):                                                ║
║  └─ ❌ Filter action not handled                                      ║
║                                                                       ║
║  API INTEGRATION (1 gap):                                             ║
║  └─ ❌ /self/endpoint not called                                      ║
║                                                                       ║
║  TOTAL GAPS: 4                                                        ║
║                                                                       ║
╠══════════════════════════════════════════════════════════════════════╣
║  SUGGESTED FIXES                                                      ║
╠══════════════════════════════════════════════════════════════════════╣
║                                                                       ║
║  1. Add EmptyContent composable in Screen                            ║
║  2. Add SwipeRefresh wrapper in Screen                               ║
║  3. Add FilterAction and handleFilter() in ViewModel                 ║
║  4. Add endpoint call in Repository                                   ║
║                                                                       ║
╠══════════════════════════════════════════════════════════════════════╣
║  NEXT STEP                                                            ║
║                                                                       ║
║  Run: /implement [Feature]                                           ║
║  Or fix gaps manually and run: /verify [Feature]                     ║
║                                                                       ║
╚══════════════════════════════════════════════════════════════════════╝
```

---

## Key Files to Compare

| Spec File | Code Files |
|-----------|------------|
| features/[feature]/SPEC.md | feature/[name]/*ViewModel.kt |
| | feature/[name]/*Screen.kt |
| | feature/[name]/components/*.kt |
| features/[feature]/API.md | core/network/services/*Service.kt |
| | core/data/repository/*Repository.kt |

---

## Status Update

After verification, update:
1. `features/[feature]/STATUS.md` - Feature status
2. `claude-product-cycle/design-spec-layer/STATUS.md` - Main tracker
