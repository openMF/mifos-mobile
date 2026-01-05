# Feature Modules Index - O(1) Lookup

> **23 modules** | **63 screens** | **49 ViewModels** | **21 DI modules**

---

## Quick Lookup

| # | Module | Path | DI | VMs | Screens |
|:-:|--------|------|:--:|:---:|:-------:|
| 1 | accounts | feature/accounts | ✅ | 3 | 3 |
| 2 | auth | feature/auth | ✅ | 5 | 6 |
| 3 | beneficiary | feature/beneficiary | ✅ | 4 | 4 |
| 4 | client-charge | feature/client-charge | ✅ | 2 | 2 |
| 5 | guarantor | feature/guarantor | ✅ | 3 | 3 |
| 6 | home | feature/home | ✅ | 1 | 1 |
| 7 | loan-account | feature/loan-account | ✅ | 4 | 4 |
| 8 | loan-application | feature/loan-application | ✅ | 4 | 3 |
| 9 | location | feature/location | ❌ | 0 | 1 |
| 10 | notification | feature/notification | ✅ | 1 | 1 |
| 11 | onboarding-language | feature/onboarding-language | ✅ | 1 | 1 |
| 12 | passcode | feature/passcode | ✅ | 2 | 2 |
| 13 | qr | feature/qr | ✅ | 3 | 3 |
| 14 | recent-transaction | feature/recent-transaction | ✅ | 1 | 1 |
| 15 | savings-account | feature/savings-account | ✅ | 3 | 4 |
| 16 | savings-application | feature/savings-application | ✅ | 2 | 2 |
| 17 | settings | feature/settings | ✅ | 5 | 9 |
| 18 | share-account | feature/share-account | ✅ | 2 | 2 |
| 19 | share-application | feature/share-application | ✅ | 2 | 2 |
| 20 | status | feature/status | ✅ | 1 | 0 |
| 21 | third-party-transfer | feature/third-party-transfer | ✅ | 1 | 1 |
| 22 | transfer-process | feature/transfer-process | ✅ | 2 | 2 |
| 23 | user-profile | feature/user-profile | ✅ | 1 | 1 |

---

## Base Path Pattern

```
feature/[module]/src/commonMain/kotlin/org/mifos/mobile/feature/[package]/
```

---

## O(1) File Access

| Need | Path Pattern |
|------|--------------|
| Screen | `feature/[module]/.../ui/[Name]Screen.kt` |
| ViewModel | `feature/[module]/.../viewmodel/[Name]ViewModel.kt` |
| DI Module | `feature/[module]/.../di/[Name]Module.kt` |
| Navigation | `feature/[module]/.../navigation/[Name]Navigation.kt` |

---

## Module Details

### High-Complexity Modules (5+ screens/VMs)

| Module | Screens | ViewModels | Key Features |
|--------|:-------:|:----------:|--------------|
| auth | 6 | 5 | Login, Registration, OTP, Password |
| settings | 9 | 5 | Theme, Language, Password, About |
| loan-account | 4 | 4 | Details, Repayment, Summary |
| beneficiary | 4 | 4 | List, Add, Edit, Delete |

### Standard Modules (2-4 screens/VMs)

| Module | Screens | ViewModels | Key Features |
|--------|:-------:|:----------:|--------------|
| accounts | 3 | 3 | List, Transactions, Details |
| guarantor | 3 | 3 | List, Add, Details |
| loan-application | 3 | 4 | Apply, Confirm, Upload |
| passcode | 2 | 2 | Set, Verify |
| qr | 3 | 3 | Display, Read, Import |
| savings-account | 4 | 3 | Details, Update, Withdraw |
| savings-application | 2 | 2 | Apply, Fill |
| share-account | 2 | 2 | List, Details |
| share-application | 2 | 2 | Apply, Fill |
| transfer-process | 2 | 2 | Make, Process |

### Simple Modules (1 screen/VM)

| Module | Screens | ViewModels | Key Features |
|--------|:-------:|:----------:|--------------|
| home | 1 | 1 | Dashboard |
| notification | 1 | 1 | List |
| recent-transaction | 1 | 1 | History |
| location | 1 | 0 | Branch Map |
| onboarding-language | 1 | 1 | Language Selection |
| third-party-transfer | 1 | 1 | TPT |
| user-profile | 1 | 1 | Profile |
| client-charge | 2 | 2 | List, Details |
| status | 0 | 1 | Status tracking |

---

## Related Files

- [SCREENS_INDEX.md](SCREENS_INDEX.md) - All 63 screens with ViewModels
- [LAYER_STATUS.md](LAYER_STATUS.md) - Implementation status
- [LAYER_GUIDE.md](LAYER_GUIDE.md) - Architecture patterns

---

## Auto-Update Rules

| Scenario | Action |
|----------|--------|
| New module added | Add row to Quick Lookup table |
| Screen added to module | Update Screens count |
| ViewModel added | Update VMs count |
| DI module added | Update DI column |
