# Gap Analysis: Feature → {{FEATURE_NAME}}

## {{FEATURE_NAME}} Feature Status

**Location**: `feature/{{FEATURE_DIR}}/`
**Progress**: {{FEATURE_PCT}}% Complete

---

### Layer Status

| Layer | Component | Status | File |
|-------|-----------|:------:|------|
| Design | SPEC.md | {{SPEC_STATUS}} | features/{{FEATURE_NAME}}/SPEC.md |
| Design | API.md | {{API_STATUS}} | features/{{FEATURE_NAME}}/API.md |
| Design | MOCKUP.md | {{MOCKUP_STATUS}} | features/{{FEATURE_NAME}}/MOCKUP.md |
| Design | mockups/ | {{MOCKUPS_STATUS}} | features/{{FEATURE_NAME}}/mockups/ |
| Client | Service | {{SERVICE_STATUS}} | core/network/services/{{SERVICE_FILE}} |
| Client | Repository | {{REPO_STATUS}} | core/data/repository/{{REPO_FILE}} |
| Feature | ViewModel | {{VM_STATUS}} | feature/{{FEATURE_DIR}}/viewmodel/{{VM_FILE}} |
| Feature | Screen | {{SCREEN_STATUS}} | feature/{{FEATURE_DIR}}/{{SCREEN_FILE}} |
| Feature | Navigation | {{NAV_STATUS}} | feature/{{FEATURE_DIR}}/navigation/ |
| Feature | DI Module | {{DI_STATUS}} | feature/{{FEATURE_DIR}}/di/ |

---

### v2.0 Design vs Implementation

| MOCKUP v2.0 Element | Current | Gap |
|---------------------|:-------:|-----|
{{V2_COMPARISON_ROWS}}

---

### Platform Status

| Platform | Status | Notes |
|----------|:------:|-------|
| Android | {{ANDROID_STATUS}} | {{ANDROID_NOTES}} |
| iOS | {{IOS_STATUS}} | {{IOS_NOTES}} |
| Desktop | {{DESKTOP_STATUS}} | {{DESKTOP_NOTES}} |
| Web | {{WEB_STATUS}} | {{WEB_NOTES}} |

---

### Implementation Gaps

| # | Gap | Type | Priority | Effort |
|---|-----|------|:--------:|:------:|
{{GAPS_TABLE}}

---

### Next Action

{{NEXT_ACTION}}

Run `/gap-planning feature {{FEATURE_NAME}}` for step-by-step plan.
