# Server Layer - Testing Status

> API contract and mock server testing documentation

---

## Overview

The server layer documents the Fineract API. Testing ensures our client-side code correctly consumes these endpoints.

---

## Testing Scope

| Component | Test Type | Purpose |
|-----------|-----------|---------|
| API Endpoints | Contract Tests | Verify request/response format |
| Error Responses | Error Handling Tests | Verify error parsing |
| Mock Responses | Fixture Tests | Test data for offline testing |

---

## API Contract Testing

### Endpoint Categories

| # | Category | Endpoints | Mock Responses | Status |
|:-:|----------|:---------:|:--------------:|:------:|
| 1 | AUTH | 4 | ⬜ | Not Started |
| 2 | CLIENT | 5 | ⬜ | Not Started |
| 3 | SAVINGS | 8 | ⬜ | Not Started |
| 4 | LOANS | 10 | ⬜ | Not Started |
| 5 | SHARES | 4 | ⬜ | Not Started |
| 6 | BENEFICIARY | 4 | ⬜ | Not Started |
| 7 | TRANSFER | 3 | ⬜ | Not Started |
| 8 | CHARGES | 3 | ⬜ | Not Started |
| 9 | NOTIFICATION | 2 | ⬜ | Not Started |
| 10 | USER | 3 | ⬜ | Not Started |
| 11 | GUARANTOR | 4 | ⬜ | Not Started |

**Legend**: ✅ Complete | ⬜ Not Started

---

## Mock Response Files

Location: `core/testing/src/commonMain/resources/api/`

```
api/
├── auth/
│   ├── login_success.json
│   ├── login_error.json
│   └── register_success.json
├── client/
│   ├── client_details.json
│   ├── client_accounts.json
│   └── client_image.json
├── savings/
│   ├── savings_list.json
│   ├── savings_detail.json
│   ├── savings_transactions.json
│   └── savings_template.json
├── loans/
│   ├── loan_list.json
│   ├── loan_detail.json
│   ├── loan_schedule.json
│   └── loan_transactions.json
├── beneficiary/
│   ├── beneficiary_list.json
│   ├── beneficiary_template.json
│   └── beneficiary_detail.json
├── transfer/
│   ├── transfer_template.json
│   └── transfer_success.json
└── common/
    ├── error_400.json
    ├── error_401.json
    ├── error_403.json
    └── error_500.json
```

---

## Mock Server Setup

For integration testing, configure Ktor mock engine:

```kotlin
// Location: core/testing/src/commonMain/.../MockApiEngine.kt

class MockApiEngine {
    fun create(): HttpClientEngine = MockEngine { request ->
        when {
            request.url.encodedPath.contains("/authentication") -> {
                respondFromFile("api/auth/login_success.json")
            }
            request.url.encodedPath.contains("/clients") -> {
                respondFromFile("api/client/client_details.json")
            }
            // ... more routes
        }
    }
}
```

---

## Error Response Testing

| Error Code | Scenario | Mock File |
|:----------:|----------|-----------|
| 400 | Bad Request | `error_400.json` |
| 401 | Unauthorized | `error_401.json` |
| 403 | Forbidden | `error_403.json` |
| 404 | Not Found | `error_404.json` |
| 500 | Server Error | `error_500.json` |

---

## Implementation Plan

### Phase 1: Mock Responses
1. Create JSON fixtures for all endpoints
2. Organize by feature category
3. Include success and error variants

### Phase 2: Mock Engine
1. Configure Ktor MockEngine
2. Map routes to fixtures
3. Handle query parameters

### Phase 3: Contract Tests
1. Verify request headers
2. Verify request body format
3. Verify response parsing

---

## Commands

```bash
# Check API documentation coverage
/gap-analysis server

# Plan mock response creation
/gap-planning server testing
```

---

## Related Files

- [API_INDEX.md](./API_INDEX.md) - All endpoints
- [API_REFERENCE.md](./API_REFERENCE.md) - Detailed API docs
- [ERROR_HANDLING.md](./ERROR_HANDLING.md) - Error patterns
