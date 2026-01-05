# Features Index - O(1) Lookup

> **18 features** | All have SPEC + API + STATUS

---

## Quick Lookup

| # | Feature | Dir | SPEC | API | STATUS | Mockups |
|:-:|---------|-----|:----:|:---:|:------:|:-------:|
| 1 | accounts | features/accounts/ | ✅ | ✅ | ✅ | ⚠️ |
| 2 | auth | features/auth/ | ✅ | ✅ | ✅ | ✅ |
| 3 | beneficiary | features/beneficiary/ | ✅ | ✅ | ✅ | ⚠️ |
| 4 | client-charge | features/client-charge/ | ✅ | ✅ | ✅ | ⚠️ |
| 5 | dashboard | features/dashboard/ | ✅ | ✅ | ✅ | ⚠️ |
| 6 | guarantor | features/guarantor/ | ✅ | ✅ | ✅ | ⚠️ |
| 7 | home | features/home/ | ✅ | ✅ | ✅ | ⚠️ |
| 8 | loan-account | features/loan-account/ | ✅ | ✅ | ✅ | ⚠️ |
| 9 | location | features/location/ | ✅ | ✅ | ✅ | ⚠️ |
| 10 | notification | features/notification/ | ✅ | ✅ | ✅ | ⚠️ |
| 11 | passcode | features/passcode/ | ✅ | ✅ | ✅ | ⚠️ |
| 12 | qr | features/qr/ | ✅ | ✅ | ✅ | ⚠️ |
| 13 | recent-transaction | features/recent-transaction/ | ✅ | ✅ | ✅ | ⚠️ |
| 14 | savings-account | features/savings-account/ | ✅ | ✅ | ✅ | ⚠️ |
| 15 | settings | features/settings/ | ✅ | ✅ | ✅ | ⚠️ |
| 16 | share-account | features/share-account/ | ✅ | ✅ | ✅ | ⚠️ |
| 17 | transfer | features/transfer/ | ✅ | ✅ | ✅ | ⚠️ |

**Legend**: ✅ Complete | ⚠️ Partial | ❌ Missing

---

## O(1) File Access

| Need | Path |
|------|------|
| Specification | `features/[name]/SPEC.md` |
| API Endpoints | `features/[name]/API.md` |
| Feature Status | `features/[name]/STATUS.md` |
| Mockups | `features/[name]/mockups/` |
| Design Tokens | `features/[name]/mockups/design-tokens.json` |

---

## Feature Categories

### Authentication & Security (3)

| Feature | Purpose |
|---------|---------|
| auth | Login, Registration, Password recovery |
| passcode | Biometric/PIN security |
| settings | Password change, security settings |

### Account Management (4)

| Feature | Purpose |
|---------|---------|
| accounts | Account overview, all account types |
| savings-account | Savings account details, operations |
| loan-account | Loan details, repayment schedule |
| share-account | Share account details |

### Transactions (3)

| Feature | Purpose |
|---------|---------|
| beneficiary | Third-party transfer beneficiaries |
| transfer | Fund transfers (self & TPT) |
| recent-transaction | Transaction history |

### Information & Utilities (5)

| Feature | Purpose |
|---------|---------|
| home | Dashboard, quick actions |
| notification | Push notifications |
| qr | QR code generation/scanning |
| location | Branch locator |
| client-charge | Client charges/fees |

### Supporting Features (2)

| Feature | Purpose |
|---------|---------|
| guarantor | Loan guarantor management |
| dashboard | Main navigation hub |

---

## Design Progress Summary

| Status | Count | Features |
|--------|:-----:|----------|
| Complete (all mockups) | 1 | auth |
| Partial (some mockups) | 16 | All others |
| Not Started | 0 | - |

---

## Related Files

- [MOCKUPS_INDEX.md](MOCKUPS_INDEX.md) - Mockup completion status
- [STATUS.md](STATUS.md) - Layer-wide status tracker
- [TOOL_CONFIG.md](TOOL_CONFIG.md) - Design tool configuration

---

## Auto-Update Rules

| Scenario | Action |
|----------|--------|
| New feature added | Add row to Quick Lookup |
| SPEC.md created | Update SPEC column to ✅ |
| API.md created | Update API column to ✅ |
| Mockups complete | Update Mockups column to ✅ |
