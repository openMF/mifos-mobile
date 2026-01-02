# /design - Feature Specification

## Purpose
Create or update feature specifications (SPEC.md + API.md) that define what to build and how to build it.

---

## Command Variants

```
/design                     → Show feature list
/design [Feature]           → Full spec review/create
/design [Feature] add [section] → Add specific section
/design [Feature] improve   → Suggest improvements
```

---

## Model Recommendation

**This command is optimized for Opus** for complex architectural decisions and comprehensive specification writing.

---

## Key Files

```
claude-product-cycle/design-spec-layer/
├── STATUS.md                         # All features status
├── _shared/
│   ├── PATTERNS.md                   # Implementation patterns
│   └── API_REFERENCE.md              # Fineract API reference
└── features/[feature]/
    ├── SPEC.md                       # What to build (UI, flows)
    ├── API.md                        # APIs needed
    └── STATUS.md                     # Feature implementation status
```

---

## Workflow

```
┌───────────────────────────────────────────────────────────────────┐
│                    /design [Feature] WORKFLOW                      │
├───────────────────────────────────────────────────────────────────┤
│                                                                    │
│  STEP 1: GATHER CONTEXT                                           │
│  ├─→ Read claude-product-cycle/design-spec-layer/STATUS.md        │
│  ├─→ Read features/[feature]/SPEC.md (if exists)                  │
│  ├─→ Read features/[feature]/API.md (if exists)                   │
│  ├─→ Read actual code in feature/[feature]/                       │
│  └─→ Read server-layer/FINERACT_API.md                            │
│                                                                    │
│  STEP 2: ANALYZE                                                  │
│  ├─→ Compare current spec vs implementation                       │
│  ├─→ Identify gaps, outdated sections, missing features           │
│  ├─→ Research best practices for similar apps                     │
│  └─→ Report findings to user                                      │
│                                                                    │
│  STEP 3: UPDATE SPEC.md                                           │
│  ├─→ Update/add sections with ASCII mockups                       │
│  ├─→ Define state model                                           │
│  ├─→ Define user actions                                          │
│  └─→ Add changelog entry                                          │
│                                                                    │
│  STEP 4: UPDATE API.md                                            │
│  ├─→ List all required endpoints                                  │
│  ├─→ Define request/response structures                           │
│  └─→ Note any missing endpoints                                   │
│                                                                    │
│  STEP 5: CROSS-UPDATE (MANDATORY)                                 │
│  ├─→ features/[feature]/STATUS.md                                 │
│  └─→ claude-product-cycle/design-spec-layer/STATUS.md             │
│                                                                    │
│  STEP 6: GENERATE IMPLEMENTATION SUMMARY                          │
│  └─→ Output clear requirements for /implement                     │
│                                                                    │
└───────────────────────────────────────────────────────────────────┘
```

---

## SPEC.md Template

```markdown
# [Feature Name] - Feature Specification

> **Purpose**: [One-line description]
> **User Value**: [Why users need this]
> **Last Updated**: [Date]

---

## 1. Overview

### 1.1 Feature Summary
[2-3 sentences describing the feature]

### 1.2 User Stories
- As a user, I want to [action] so that [benefit]

---

## 2. Screen Layout

### 2.1 ASCII Mockup

```
┌─────────────────────────────────────────┐
│  ← Back          [Title]            ⋮   │  ← TopBar
├─────────────────────────────────────────┤
│                                         │
│  ┌─────────────────────────────────┐   │
│  │     Section 1                    │   │
│  └─────────────────────────────────┘   │
│                                         │
└─────────────────────────────────────────┘
```

### 2.2 Sections Table

| # | Section | Description | API | Priority |
|---|---------|-------------|-----|----------|
| 1 | [Name] | [What it shows] | [Endpoint] | P0 |

---

## 3. User Interactions

| Action | Trigger | Result | API Call |
|--------|---------|--------|----------|
| Tap item | Click | Navigate | - |
| Pull refresh | Swipe down | Reload data | [Endpoint] |

---

## 4. State Model

```kotlin
@Immutable
data class [Feature]State(
    val isLoading: Boolean = false,
    val data: List<Item> = emptyList(),
    val error: String? = null,
)

sealed interface [Feature]ScreenState {
    data object Loading : [Feature]ScreenState
    data object Success : [Feature]ScreenState
    data class Error(val message: StringResource) : [Feature]ScreenState
}
```

---

## 5. API Requirements

| Endpoint | Method | Purpose | Status |
|----------|--------|---------|--------|
| /self/[path] | GET | [Description] | ✅ Exists |

---

## 6. Edge Cases & Error Handling

| Scenario | Behavior | UI Feedback |
|----------|----------|-------------|
| No internet | Show cached | Toast |
| Empty results | Show empty state | Illustration |
| API error | Retry logic | Snackbar |

---

## Changelog

| Date | Change |
|------|--------|
| [date] | Initial spec |
```

---

## API.md Template

```markdown
# [Feature Name] - API Reference

## Endpoints Required

### [Endpoint Name]

**Endpoint**: `GET /self/[path]`

**Description**: [What this endpoint does]

**Request**:
```
Headers:
  Authorization: Basic {token}
  Fineract-Platform-TenantId: {tenant}
```

**Response**:
```json
{
    "field": "value"
}
```

**Kotlin DTO**:
```kotlin
@Serializable
data class [Name]Dto(
    @SerialName("field") val field: String,
)
```

**Status**: ✅ Implemented / ❌ Missing

---

## API Summary

| Endpoint | Service | Repository | Status |
|----------|---------|------------|--------|
| /self/[path] | [Name]Service | [Name]Repository | ✅ |
```

---

## Output Template

After completing design, output:

```
┌───────────────────────────────────────────────────────────────────┐
│            IMPLEMENTATION REQUIREMENTS                             │
│            Ready for /implement in Sonnet session                  │
├───────────────────────────────────────────────────────────────────┤
│                                                                    │
│  FEATURE: [Feature Name]                                          │
│  SPEC UPDATED: features/[feature]/SPEC.md                         │
│                                                                    │
│  ════════════════════════════════════════════════════════════════ │
│                                                                    │
│  CLIENT WORK NEEDED:                                              │
│  [ ] Network: [DTO/Service changes]                               │
│  [ ] Data: [Repository changes]                                   │
│                                                                    │
│  FEATURE WORK NEEDED:                                             │
│  [ ] ViewModel: [changes]                                         │
│  [ ] Screen: [changes]                                            │
│  [ ] Components: [new components]                                 │
│                                                                    │
│  ════════════════════════════════════════════════════════════════ │
│                                                                    │
│  NEXT STEP:                                                       │
│  Run:  /implement [Feature]                                       │
│                                                                    │
└───────────────────────────────────────────────────────────────────┘
```

---

## If No Feature Name Provided

Show feature list:

```
📋 FEATURES AVAILABLE FOR DESIGN:

| Feature | Status | Last Updated | Command |
|---------|--------|--------------|---------|
| auth | ✅ Done | - | /design auth |
| home | ✅ Done | - | /design home |
| accounts | ✅ Done | - | /design accounts |
| loan-account | ✅ Done | - | /design loan-account |
| savings-account | ✅ Done | - | /design savings-account |
| share-account | ✅ Done | - | /design share-account |
| beneficiary | ✅ Done | - | /design beneficiary |
| transfer | ✅ Done | - | /design transfer |
| recent-transaction | ✅ Done | - | /design recent-transaction |
| notification | ✅ Done | - | /design notification |
| settings | ✅ Done | - | /design settings |
| passcode | ✅ Done | - | /design passcode |
| guarantor | ✅ Done | - | /design guarantor |
| qr | ✅ Done | - | /design qr |
| location | ✅ Done | - | /design location |
| client-charge | ✅ Done | - | /design client-charge |

Which feature do you want to design?
```
