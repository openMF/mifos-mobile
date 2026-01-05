# Design Layer - Testing Status

> Testing specifications for design layer validation

---

## Overview

The design layer defines **what** should be tested. Each feature specification includes acceptance criteria that translate directly to test cases.

---

## Testing Scope

| Component | Test Type | Purpose |
|-----------|-----------|---------|
| SPEC.md | Contract Tests | Verify implementation matches specification |
| API.md | API Contract Tests | Verify API usage matches documentation |
| Mockups | Screenshot Tests | Visual regression testing |
| design-tokens.json | Theme Tests | Verify design tokens applied correctly |

---

## Per-Feature Testing Requirements

### Test Coverage Matrix

| # | Feature | Contract | API | Screenshot | Status |
|:-:|---------|:--------:|:---:|:----------:|:------:|
| 1 | auth | ⬜ | ⬜ | ⬜ | Not Started |
| 2 | home | ⬜ | ⬜ | ⬜ | Not Started |
| 3 | accounts | ⬜ | ⬜ | ⬜ | Not Started |
| 4 | savings-account | ⬜ | ⬜ | ⬜ | Not Started |
| 5 | loan-account | ⬜ | ⬜ | ⬜ | Not Started |
| 6 | share-account | ⬜ | ⬜ | ⬜ | Not Started |
| 7 | beneficiary | ⬜ | ⬜ | ⬜ | Not Started |
| 8 | transfer | ⬜ | ⬜ | ⬜ | Not Started |
| 9 | recent-transaction | ⬜ | ⬜ | ⬜ | Not Started |
| 10 | notification | ⬜ | ⬜ | ⬜ | Not Started |
| 11 | settings | ⬜ | ⬜ | ⬜ | Not Started |
| 12 | passcode | ⬜ | - | ⬜ | Not Started |
| 13 | guarantor | ⬜ | ⬜ | ⬜ | Not Started |
| 14 | qr | ⬜ | - | ⬜ | Not Started |
| 15 | location | ⬜ | - | ⬜ | Not Started |
| 16 | client-charge | ⬜ | ⬜ | ⬜ | Not Started |
| 17 | dashboard | ⬜ | ⬜ | ⬜ | Not Started |

**Legend**: ✅ Complete | ⬜ Not Started | - N/A

---

## Testing Specification Template

Each feature's SPEC.md should include a `## Test Scenarios` section:

```markdown
## Test Scenarios

### Loading State
- [ ] Shows loading indicator when data is being fetched
- [ ] Disables user interaction during loading

### Success State
- [ ] Displays all required data fields
- [ ] Data matches API response
- [ ] Navigation works correctly

### Error State
- [ ] Shows error message for network failures
- [ ] Retry button is visible and functional
- [ ] Error message is user-friendly

### Empty State
- [ ] Shows appropriate message when no data
- [ ] Optional: Call-to-action for creating data

### Validation
- [ ] Required fields show error when empty
- [ ] Format validation (email, phone, etc.)
- [ ] Business rules validation
```

---

## Screenshot Test Baseline

For mockups to become screenshot test baselines:

| Step | Action | Output |
|:----:|--------|--------|
| 1 | Generate mockups (Google Stitch/Figma) | PNG/SVG files |
| 2 | Export to `mockups/` folder | Light + Dark variants |
| 3 | Configure Roborazzi | Golden images |
| 4 | Run screenshot tests | Compare against baseline |

---

## Implementation Priority

| Priority | Features | Reason |
|:--------:|----------|--------|
| P0 | auth, home | Core user flow |
| P1 | accounts, transfer, beneficiary | Primary functionality |
| P2 | loan-account, savings-account | Account management |
| P3 | Others | Supporting features |

---

## Commands

```bash
# Validate feature spec has test scenarios
/verify [feature] testing

# Generate test cases from spec
/gap-planning [feature] testing
```

---

## Related Files

- [FEATURES_INDEX.md](./FEATURES_INDEX.md) - Feature status
- [MOCKUPS_INDEX.md](./MOCKUPS_INDEX.md) - Mockup status
- Each feature's `SPEC.md` - Detailed specifications
