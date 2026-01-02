# Claude Product Cycle - Commands Reference

## Quick Reference

```
┌─────────────────────────────────────────────────────────────────────┐
│  DESIGN PHASE                                                        │
├─────────────────────────────────────────────────────────────────────┤
│  /design [Feature]        → Create SPEC.md + API.md (Opus)          │
├─────────────────────────────────────────────────────────────────────┤
│  IMPLEMENT PHASE                                                     │
├─────────────────────────────────────────────────────────────────────┤
│  /implement [Feature]     → Full E2E implementation (Sonnet)        │
│                                                                      │
│  OR use layer commands independently:                                │
│  /client [Feature]        → Network + Data layers                   │
│  /feature [Feature]       → UI layer (ViewModel + Screen)           │
├─────────────────────────────────────────────────────────────────────┤
│  VERIFY PHASE                                                        │
├─────────────────────────────────────────────────────────────────────┤
│  /verify [Feature]        → Validate implementation vs spec         │
├─────────────────────────────────────────────────────────────────────┤
│  UTILITIES                                                           │
├─────────────────────────────────────────────────────────────────────┤
│  /projectstatus           → Project overview                        │
└─────────────────────────────────────────────────────────────────────┘
```

---

## Command Details

### `/projectstatus`
Shows the current state of all features, implementation progress, and suggested next steps.

**Usage**: `/projectstatus`

---

### `/design [Feature]`
Creates or updates the feature specification (SPEC.md + API.md). Best used with Opus for complex architectural decisions.

**Usage**:
```
/design                     → Show feature list
/design Home                → Full spec review/create for Home
/design Accounts add filter → Add specific section
```

**Output**: Creates/updates in `claude-product-cycle/design-spec-layer/features/[feature]/`

---

### `/implement [Feature]`
Full end-to-end implementation including client layer (Network + Data) and feature layer (UI).

**Usage**:
```
/implement                  → Show feature status list
/implement Home             → Full implementation
/implement Home --quick     → Skip validations
```

**Pipeline**:
1. Git: Create feature branch
2. Validate: Check dependencies
3. Client: Network + Data layers
4. Feature: ViewModel + Screen
5. Build & Test
6. Lint & Format
7. Commit

---

### `/client [Feature]`
Implements only the client layer (Network + Data + Domain).

**Usage**: `/client Home`

**Creates**:
- DTOs in `core/network/model/`
- Services in `core/network/services/`
- Repository in `core/data/repository/`
- DI module registrations

---

### `/feature [Feature]`
Implements only the feature/UI layer.

**Usage**: `/feature Home`

**Creates**:
- ViewModel in `feature/[name]/`
- Screen composable
- Components
- Navigation
- DI module

---

### `/verify [Feature]`
Validates that the implementation matches the specification.

**Usage**: `/verify Home`

**Checks**:
- SPEC.md vs actual code
- All user actions implemented
- All API calls present
- DI registration complete
- Navigation configured

---

## Feature List

| Feature | Command |
|---------|---------|
| Authentication | `/design auth` |
| Home Dashboard | `/design home` |
| Accounts | `/design accounts` |
| Loan Account | `/design loan-account` |
| Savings Account | `/design savings-account` |
| Share Account | `/design share-account` |
| Beneficiary | `/design beneficiary` |
| Transfer | `/design transfer` |
| Recent Transactions | `/design recent-transaction` |
| Notifications | `/design notification` |
| Settings | `/design settings` |
| Passcode | `/design passcode` |
| Guarantor | `/design guarantor` |
| QR Code | `/design qr` |
| Location | `/design location` |
| Client Charges | `/design client-charge` |
