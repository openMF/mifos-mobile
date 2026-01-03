# Feature Detail Analysis Template

## Gap Analysis: {{FEATURE_NAME}}

**Last Updated**: {{DATE}}

---

### 5-Layer Status

| Layer | Status | Score | Details |
|-------|:------:|:-----:|---------|
| 1. Design | {{DESIGN_ICON}} | {{DESIGN_PCT}}% | {{DESIGN_DETAILS}} |
| 2. Server | {{SERVER_ICON}} | {{SERVER_PCT}}% | {{SERVER_DETAILS}} |
| 3. Client | {{CLIENT_ICON}} | {{CLIENT_PCT}}% | {{CLIENT_DETAILS}} |
| 4. Feature | {{FEATURE_ICON}} | {{FEATURE_PCT}}% | {{FEATURE_DETAILS}} |
| 5. Platform | {{PLATFORM_ICON}} | {{PLATFORM_PCT}}% | {{PLATFORM_DETAILS}} |

---

### Layer 1: Design

| File | Status | Location |
|------|:------:|----------|
| SPEC.md | {{SPEC_STATUS}} | `design-spec-layer/features/{{FEATURE}}/SPEC.md` |
| MOCKUP.md | {{MOCKUP_STATUS}} | `design-spec-layer/features/{{FEATURE}}/MOCKUP.md` |
| API.md | {{API_STATUS}} | `design-spec-layer/features/{{FEATURE}}/API.md` |
| STATUS.md | {{STATUS_STATUS}} | `design-spec-layer/features/{{FEATURE}}/STATUS.md` |

---

### Layer 2: Server

| Endpoint | Method | Available | Tested |
|----------|--------|:---------:|:------:|
{{SERVER_ENDPOINTS}}

---

### Layer 3: Client

| Component | Status | File |
|-----------|:------:|------|
| Service | {{SERVICE_STATUS}} | {{SERVICE_PATH}} |
| Repository | {{REPO_STATUS}} | {{REPO_PATH}} |
| Models | {{MODEL_STATUS}} | {{MODEL_PATH}} |

---

### Layer 4: Feature

| Component | Status | File |
|-----------|:------:|------|
| ViewModel | {{VM_STATUS}} | {{VM_PATH}} |
| Screen | {{SCREEN_STATUS}} | {{SCREEN_PATH}} |
| Navigation | {{NAV_STATUS}} | {{NAV_PATH}} |
| DI | {{DI_STATUS}} | {{DI_PATH}} |

**v2.0 Implementation**:
| Element | Status | Gap |
|---------|:------:|-----|
{{V2_ELEMENTS}}

---

### Layer 5: Platform

| Platform | Status | Notes |
|----------|:------:|-------|
| Android | {{ANDROID_STATUS}} | {{ANDROID_NOTES}} |
| iOS | {{IOS_STATUS}} | {{IOS_NOTES}} |
| Desktop | {{DESKTOP_STATUS}} | {{DESKTOP_NOTES}} |
| Web | {{WEB_STATUS}} | {{WEB_NOTES}} |

---

### Gaps Summary

| # | Layer | Gap | Priority | Effort |
|---|-------|-----|:--------:|:------:|
{{GAPS_SUMMARY}}

---

### Recommended Actions

1. `/gap-planning {{FEATURE}}` - Get detailed implementation plan
2. `/feature {{FEATURE}}` - Update UI layer
3. `/verify {{FEATURE}}` - Confirm after changes
