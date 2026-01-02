# /projectstatus - Project Overview

## Purpose
Display the current state of the Mifos Mobile project, including feature implementation status, available commands, and suggested next steps.

---

## Workflow

```
┌───────────────────────────────────────────────────────────────────┐
│                    /projectstatus WORKFLOW                         │
├───────────────────────────────────────────────────────────────────┤
│                                                                    │
│  STEP 1: READ STATUS FILES                                        │
│  ├─→ claude-product-cycle/design-spec-layer/STATUS.md             │
│  └─→ Individual feature STATUS.md files                           │
│                                                                    │
│  STEP 2: ANALYZE CODEBASE                                         │
│  ├─→ Check feature/ directory for implemented features            │
│  ├─→ Check core/network/services/ for API services                │
│  ├─→ Check core/data/repository/ for repositories                 │
│  └─→ Compare spec vs implementation                               │
│                                                                    │
│  STEP 3: GENERATE DASHBOARD                                       │
│  ├─→ Feature status table                                         │
│  ├─→ Layer completion summary                                     │
│  ├─→ Available commands                                           │
│  └─→ Suggested next steps                                         │
│                                                                    │
└───────────────────────────────────────────────────────────────────┘
```

---

## Output Template

```
╔══════════════════════════════════════════════════════════════════════╗
║  MIFOS MOBILE - PROJECT STATUS                                       ║
╠══════════════════════════════════════════════════════════════════════╣
║                                                                       ║
║  PROJECT: Mifos Mobile (Self-Service Banking App)                    ║
║  TECH STACK: Kotlin Multiplatform + Compose + Fineract API           ║
║  LAST UPDATED: [Date]                                                ║
║                                                                       ║
╠══════════════════════════════════════════════════════════════════════╣
║  FEATURE STATUS                                                       ║
╠══════════════════════════════════════════════════════════════════════╣
║                                                                       ║
║  | Feature           | Status     | Client | Feature | Gaps |        ║
║  |-------------------|------------|--------|---------|------|        ║
║  | Auth              | ✅ Done    | ✅     | ✅      | 0    |        ║
║  | Home              | ✅ Done    | ✅     | ✅      | 0    |        ║
║  | Accounts          | ✅ Done    | ✅     | ✅      | 0    |        ║
║  | Loan Account      | ✅ Done    | ✅     | ✅      | 0    |        ║
║  | Savings Account   | ✅ Done    | ✅     | ✅      | 0    |        ║
║  | Share Account     | ✅ Done    | ✅     | ✅      | 0    |        ║
║  | Beneficiary       | ✅ Done    | ✅     | ✅      | 0    |        ║
║  | Transfer          | ✅ Done    | ✅     | ✅      | 0    |        ║
║  | Recent Transaction| ✅ Done    | ✅     | ✅      | 0    |        ║
║  | Notification      | ✅ Done    | ✅     | ✅      | 0    |        ║
║  | Settings          | ✅ Done    | ✅     | ✅      | 0    |        ║
║  | Passcode          | ✅ Done    | -      | ✅      | 0    |        ║
║  | Guarantor         | ✅ Done    | ✅     | ✅      | 0    |        ║
║  | QR Code           | ✅ Done    | -      | ✅      | 0    |        ║
║  | Location          | ✅ Done    | -      | ✅      | 0    |        ║
║  | Client Charges    | ✅ Done    | ✅     | ✅      | 0    |        ║
║                                                                       ║
╠══════════════════════════════════════════════════════════════════════╣
║  AVAILABLE COMMANDS                                                   ║
╠══════════════════════════════════════════════════════════════════════╣
║                                                                       ║
║  Design:                                                              ║
║    /design [Feature]      → Create/update feature specification      ║
║                                                                       ║
║  Implement:                                                           ║
║    /implement [Feature]   → Full E2E implementation                  ║
║    /client [Feature]      → Network + Data layers                    ║
║    /feature [Feature]     → UI layer (ViewModel + Screen)            ║
║                                                                       ║
║  Verify:                                                              ║
║    /verify [Feature]      → Validate implementation vs spec          ║
║                                                                       ║
╠══════════════════════════════════════════════════════════════════════╣
║  SUGGESTED NEXT STEPS                                                 ║
╠══════════════════════════════════════════════════════════════════════╣
║                                                                       ║
║  1. Review existing features: /verify [Feature]                      ║
║  2. Improve feature: /design [Feature] for enhancements              ║
║  3. Add new feature: /design [NewFeature]                            ║
║                                                                       ║
╚══════════════════════════════════════════════════════════════════════╝
```

---

## Key Files to Read

1. `claude-product-cycle/design-spec-layer/STATUS.md` - Master status tracker
2. `feature/*/` - Feature module directories
3. `core/network/services/` - API services
4. `core/data/repository/` - Repositories

---

## Status Legend

| Status | Meaning |
|--------|---------|
| ✅ Done | Feature complete, all working |
| ⚠️ Needs Update | Has gaps, spec changed, or incomplete |
| 🔄 In Progress | Currently being implemented |
| 📋 Planned | Spec exists, not started |
| 🆕 Not Started | No work done |
