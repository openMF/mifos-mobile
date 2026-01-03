# Gap Analysis Dashboard Template

## Mifos Mobile - Gap Analysis Dashboard

**App Version**: {{APP_VERSION}} | **Last Updated**: {{DATE}}
**Design System**: v2.0 (2025 Fintech Patterns)

---

### 5-Layer Health Overview

```
┌─────────────────────────────────────────────────────────────────┐
│  PRODUCT LIFECYCLE HEALTH                                        │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  1. Design Layer    {{DESIGN_BAR}}  {{DESIGN_PCT}}%              │
│     ├─ SPEC.md      {{SPEC_BAR}}  {{SPEC_PCT}}%                  │
│     ├─ MOCKUP.md    {{MOCKUP_BAR}}  {{MOCKUP_PCT}}%              │
│     └─ API.md       {{API_BAR}}  {{API_PCT}}%                    │
│                                                                  │
│  2. Server Layer    {{SERVER_BAR}}  {{SERVER_PCT}}%              │
│                                                                  │
│  3. Client Layer    {{CLIENT_BAR}}  {{CLIENT_PCT}}%              │
│     ├─ Network      {{NETWORK_BAR}}  {{NETWORK_PCT}}%            │
│     └─ Data         {{DATA_BAR}}  {{DATA_PCT}}%                  │
│                                                                  │
│  4. Feature Layer   {{FEATURE_BAR}}  {{FEATURE_PCT}}%            │
│     ├─ ViewModels   {{VM_BAR}}  {{VM_PCT}}%                      │
│     ├─ Screens      {{SCREEN_BAR}}  {{SCREEN_PCT}}%              │
│     └─ v2.0 Match   {{V2_BAR}}  {{V2_PCT}}%                      │
│                                                                  │
│  5. Platform Layer  {{PLATFORM_BAR}}  {{PLATFORM_PCT}}%          │
│     ├─ Android      {{ANDROID_BAR}}  {{ANDROID_PCT}}%            │
│     ├─ iOS          {{IOS_BAR}}  {{IOS_PCT}}%                    │
│     ├─ Desktop      {{DESKTOP_BAR}}  {{DESKTOP_PCT}}%            │
│     └─ Web          {{WEB_BAR}}  {{WEB_PCT}}%                    │
│                                                                  │
│  OVERALL            {{OVERALL_BAR}}  {{OVERALL_PCT}}%            │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

### Feature Matrix (All Layers)

| # | Feature | Design | Server | Client | Feature | Platform |
|:-:|---------|:------:|:------:|:------:|:-------:|:--------:|
{{FEATURE_MATRIX_ROWS}}

**Legend**: ✅ Complete | ⚠️ Partial | ❌ Missing | `-` N/A

---

### Layer Commands

| Layer | Command | Status |
|-------|---------|--------|
| Design | `/gap-analysis design` | {{DESIGN_STATUS}} |
| Server | `/gap-analysis server` | {{SERVER_STATUS}} |
| Client | `/gap-analysis client` | {{CLIENT_STATUS}} |
| Feature | `/gap-analysis feature` | {{FEATURE_STATUS}} |
| Platform | `/gap-analysis platform` | {{PLATFORM_STATUS}} |

---

### Critical Gaps (P0)

{{P0_GAPS_TABLE}}

### High Priority Gaps (P1)

{{P1_GAPS_TABLE}}

### Quick Wins (P2)

{{P2_GAPS_TABLE}}

---

**Drill down**: `/gap-analysis [layer]` or `/gap-analysis [feature-name]`
