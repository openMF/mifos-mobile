# Gap Analysis: Design → Mockups Sub-Section

## Mockups Generation Status

**Phase**: Phase 2 - Mockup Generation
**Progress**: {{MOCKUPS_COUNT}}/17 features ({{MOCKUPS_PCT}}%)

---

### Status Overview

```
MOCKUP GENERATION PROGRESS
{{MOCKUPS_PROGRESS_BAR}} {{MOCKUPS_PCT}}%

├── PROMPTS.md     {{PROMPTS_COUNT}}/17 generated
├── design-tokens  {{TOKENS_COUNT}}/17 generated
└── Figma URLs     {{FIGMA_COUNT}}/17 captured
```

---

### Feature Status

| # | Feature | MOCKUP.md | mockups/ | PROMPTS.md | design-tokens | Figma |
|:-:|---------|:---------:|:--------:|:----------:|:-------------:|:-----:|
| 1 | auth | v2.0 ✅ | {{AUTH_MOCKUPS}} | {{AUTH_PROMPTS}} | {{AUTH_TOKENS}} | {{AUTH_FIGMA}} |
| 2 | home | v2.0 ✅ | {{HOME_MOCKUPS}} | {{HOME_PROMPTS}} | {{HOME_TOKENS}} | {{HOME_FIGMA}} |
| 3 | accounts | v2.0 ✅ | {{ACCOUNTS_MOCKUPS}} | {{ACCOUNTS_PROMPTS}} | {{ACCOUNTS_TOKENS}} | {{ACCOUNTS_FIGMA}} |
| 4 | savings-account | v2.0 ✅ | {{SAVINGS_MOCKUPS}} | {{SAVINGS_PROMPTS}} | {{SAVINGS_TOKENS}} | {{SAVINGS_FIGMA}} |
| 5 | loan-account | v2.0 ✅ | {{LOAN_MOCKUPS}} | {{LOAN_PROMPTS}} | {{LOAN_TOKENS}} | {{LOAN_FIGMA}} |
| 6 | share-account | v2.0 ✅ | {{SHARE_MOCKUPS}} | {{SHARE_PROMPTS}} | {{SHARE_TOKENS}} | {{SHARE_FIGMA}} |
| 7 | beneficiary | v2.0 ✅ | {{BENEFICIARY_MOCKUPS}} | {{BENEFICIARY_PROMPTS}} | {{BENEFICIARY_TOKENS}} | {{BENEFICIARY_FIGMA}} |
| 8 | transfer | v2.0 ✅ | {{TRANSFER_MOCKUPS}} | {{TRANSFER_PROMPTS}} | {{TRANSFER_TOKENS}} | {{TRANSFER_FIGMA}} |
| 9 | recent-transaction | v2.0 ✅ | {{RECENT_MOCKUPS}} | {{RECENT_PROMPTS}} | {{RECENT_TOKENS}} | {{RECENT_FIGMA}} |
| 10 | notification | v2.0 ✅ | {{NOTIFICATION_MOCKUPS}} | {{NOTIFICATION_PROMPTS}} | {{NOTIFICATION_TOKENS}} | {{NOTIFICATION_FIGMA}} |
| 11 | settings | v2.0 ✅ | {{SETTINGS_MOCKUPS}} | {{SETTINGS_PROMPTS}} | {{SETTINGS_TOKENS}} | {{SETTINGS_FIGMA}} |
| 12 | passcode | v2.0 ✅ | {{PASSCODE_MOCKUPS}} | {{PASSCODE_PROMPTS}} | {{PASSCODE_TOKENS}} | {{PASSCODE_FIGMA}} |
| 13 | guarantor | v2.0 ✅ | {{GUARANTOR_MOCKUPS}} | {{GUARANTOR_PROMPTS}} | {{GUARANTOR_TOKENS}} | {{GUARANTOR_FIGMA}} |
| 14 | qr | v2.0 ✅ | {{QR_MOCKUPS}} | {{QR_PROMPTS}} | {{QR_TOKENS}} | {{QR_FIGMA}} |
| 15 | location | v2.0 ✅ | {{LOCATION_MOCKUPS}} | {{LOCATION_PROMPTS}} | {{LOCATION_TOKENS}} | {{LOCATION_FIGMA}} |
| 16 | client-charge | v2.0 ✅ | {{CLIENT_MOCKUPS}} | {{CLIENT_PROMPTS}} | {{CLIENT_TOKENS}} | {{CLIENT_FIGMA}} |
| 17 | dashboard | v2.0 ✅ | {{DASHBOARD_MOCKUPS}} | {{DASHBOARD_PROMPTS}} | {{DASHBOARD_TOKENS}} | {{DASHBOARD_FIGMA}} |

**Legend**: ✅ Complete | ❌ Missing | ⏳ Pending

---

### Pending Features ({{PENDING_COUNT}})

{{PENDING_FEATURES_LIST}}

---

### Next Action

**Execute**: `/design {{NEXT_FEATURE}} mockup`

Or run `/gap-planning design mockup` for step-by-step plan.

---

### Workflow Reference

```
MOCKUP.md (ASCII v2.0)
       ↓
/design [feature] mockup
       ↓
mockups/PROMPTS.md + design-tokens.json
       ↓
User: Google Stitch → Figma
       ↓
User: Update FIGMA_LINKS.md
```
