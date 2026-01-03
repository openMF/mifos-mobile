# Server Layer Analysis Template

## Server Layer Gap Analysis

**Base URL**: `https://tt.mifos.community/fineract-provider/api/v1/self/`
**Reference**: `claude-product-cycle/server-layer/FINERACT_API.md`
**Last Updated**: {{DATE}}

---

### API Endpoint Availability

| # | Feature | Endpoints | Documented | Available | Tested | Gap |
|:-:|---------|:---------:|:----------:|:---------:|:------:|-----|
{{SERVER_STATUS_ROWS}}

---

### Endpoint Categories

```
SERVER LAYER BREAKDOWN
├── Authentication  {{AUTH_BAR}} {{AUTH_PCT}}%
├── Clients         {{CLIENTS_BAR}} {{CLIENTS_PCT}}%
├── Accounts        {{ACCOUNTS_BAR}} {{ACCOUNTS_PCT}}%
├── Savings         {{SAVINGS_BAR}} {{SAVINGS_PCT}}%
├── Loans           {{LOANS_BAR}} {{LOANS_PCT}}%
├── Shares          {{SHARES_BAR}} {{SHARES_PCT}}%
├── Beneficiaries   {{BENEFICIARY_BAR}} {{BENEFICIARY_PCT}}%
├── Transfers       {{TRANSFER_BAR}} {{TRANSFER_PCT}}%
├── Transactions    {{TXN_BAR}} {{TXN_PCT}}%
├── Notifications   {{NOTIF_BAR}} {{NOTIF_PCT}}%
├── Guarantors      {{GUARANTOR_BAR}} {{GUARANTOR_PCT}}%
└── Charges         {{CHARGES_BAR}} {{CHARGES_PCT}}%

OVERALL             {{OVERALL_BAR}} {{OVERALL_PCT}}%
```

---

### Known Server Limitations

| Endpoint | Issue | Workaround |
|----------|-------|------------|
{{SERVER_LIMITATIONS}}

---

### Gaps

{{SERVER_GAPS_TABLE}}

---

**Next**: `/gap-analysis client` to check implementation
