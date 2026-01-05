# Server Layer Planning Template

## Implementation Plan: Server Layer

**Base URL**: `https://tt.mifos.community/fineract-provider/api/v1/self/`
**Reference**: `claude-product-cycle/server-layer/FINERACT_API.md`
**Last Updated**: {{DATE}}

---

### Gaps Identified

| # | Feature | Gap Type | Priority | Effort |
|---|---------|----------|:--------:|:------:|
{{SERVER_GAPS_TABLE}}

---

### Gap Types

| Type | Description | Action |
|------|-------------|--------|
| Undocumented | Endpoint exists but not in API.md | Document in feature's API.md |
| Unavailable | Endpoint doesn't exist on server | Check Fineract version / workaround |
| Untested | Endpoint not verified | Test and document results |

---

### Tasks Overview

| # | Task | Files | Priority | Effort |
|---|------|-------|:--------:|:------:|
{{TASKS_TABLE}}

---

### Task Details

{{TASK_DETAILS}}

---

### Documentation Tasks

For undocumented endpoints:

**Update** `design-spec-layer/features/[feature]/API.md`:
```markdown
### [METHOD] /self/[endpoint]

**Purpose**: Description

**Request**:
```json
{
  "param": "value"
}
```

**Response**:
```json
{
  "field": "value"
}
```

**Status**: ✅ Available | ⚠️ Limited | ❌ Unavailable
```

---

### Testing Tasks

For untested endpoints:

1. Use demo credentials: `gsoc.mifos.community` / `maria` / `password`
2. Test each endpoint with Postman/curl
3. Document response schemas
4. Note any limitations

**Test Script**:
```bash
curl -X GET "https://tt.mifos.community/fineract-provider/api/v1/self/[endpoint]" \
  -H "Authorization: Basic [base64]" \
  -H "Fineract-Platform-TenantId: default"
```

---

### Known Limitations

| Endpoint | Issue | Workaround |
|----------|-------|------------|
{{LIMITATIONS_TABLE}}

---

### Verification

After completing tasks:
1. Update `server-layer/FINERACT_API.md`
2. Update feature's `API.md`
3. Update `PRODUCT_MAP.md`

---

**Ready?** Document endpoints in `/design [feature]`
