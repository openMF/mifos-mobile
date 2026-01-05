# Gap Analysis: Client → Network Sub-Section

## Network Services Status

**Location**: `core/network/services/`
**Progress**: {{NETWORK_PCT}}% Complete

---

### Status Overview

```
NETWORK SERVICES
{{NETWORK_BAR}} {{SERVICE_COUNT}}/{{TOTAL_SERVICES}} services ({{NETWORK_PCT}}%)
```

---

### Service Status

| # | Service | File | Endpoints | Status |
|:-:|---------|------|:---------:|:------:|
{{SERVICE_STATUS_ROWS}}

**Legend**: ✅ Complete | ⚠️ Partial | ❌ Missing

---

### Service Details

{{SERVICE_DETAILS}}

---

### Missing Services

{{MISSING_SERVICES_LIST}}

---

### Next Action

{{NEXT_ACTION}}

Run `/gap-planning client network` for step-by-step plan.
