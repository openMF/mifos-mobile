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
/design [Feature] mockup    → Generate Figma mockups for feature (NEW)
/design mockup              → Generate Figma mockups for all features (NEW)
```

---

## Mockup Sub-Command

### `/design [Feature] mockup`

Generates Figma-ready mockups from the feature's MOCKUP.md specification.

**Before Running**: Check MCP connections and select AI tool:

#### Step 0: Check MCP & Select Tool

**Check MCP Status**:
```bash
claude mcp list
```

**AI Design Tools Available**:

| Tool | MCP | Best For | Setup Command |
|------|:---:|----------|---------------|
| **Google Stitch** | ✅ | Material Design 3, Android/KMP | `claude mcp add stitch-ai -- npx -y stitch-ai-mcp` |
| **Figma** | ✅ | Team collaboration, custom designs | `claude mcp add figma -- npx -y figma-mcp --token TOKEN` |
| Uizard | ❌ | Quick prototypes | Manual (web only) |
| Visily | ❌ | Component-focused | Manual (web only) |

**Recommended**: Google Stitch (MD3 native, has MCP)

**MCP Resources**:
- Google Stitch MCP: [github.com/StitchAI/stitch-ai-mcp](https://github.com/StitchAI/stitch-ai-mcp)
- Stitch Web: [stitch.withgoogle.com](https://stitch.withgoogle.com/)

#### Tool Selection (Ask User)

When running `/design [feature] mockup`, prompt user to select tool:

```
🎨 Select AI Design Tool:

1. Google Stitch (Recommended) - Material Design 3 native
   MCP: claude mcp add stitch-ai -- npx -y stitch-ai-mcp
   Web: https://stitch.withgoogle.com/

2. Figma + AI - Team collaboration
   MCP: claude mcp add figma -- npx -y figma-mcp --token TOKEN

3. Uizard - Quick prototypes (no MCP)
   Web: https://uizard.io/

4. Visily - Component-focused (no MCP)
   Web: https://www.visily.ai/

Which tool? (1-4, default: 1)
```

#### Workflow

1. Check MCP connection status
2. Ask user to select AI design tool (or use configured default)
3. Read `features/[Feature]/MOCKUP.md` (v2.0 ASCII design)
4. Generate `features/[Feature]/mockups/PROMPTS.md` (tool-specific prompts)
5. Generate `features/[Feature]/mockups/design-tokens.json` (structured tokens)
6. If MCP connected: Offer to send directly to tool
7. Output next steps for user

**Output Files**:
```
features/[Feature]/mockups/
├── PROMPTS.md           # AI tool prompts (format based on selection)
├── design-tokens.json   # Structured design tokens
└── FIGMA_LINKS.md       # Figma URLs (user fills after export)
```

### `/design mockup`

Generates mockups for ALL features that don't have mockups/ directory yet.
Shows progress and allows resuming where left off.

**First Run**: Will ask to select AI tool and configure MCP if not already done.

### Mockup Generation Workflow

```
┌───────────────────────────────────────────────────────────────────┐
│                /design [Feature] mockup WORKFLOW                   │
├───────────────────────────────────────────────────────────────────┤
│                                                                    │
│  STEP 1: READ MOCKUP.md                                           │
│  ├─→ Read features/[feature]/MOCKUP.md (v2.0 ASCII design)       │
│  ├─→ Parse screen layouts, components, colors                     │
│  └─→ Identify all screens and UI elements                         │
│                                                                    │
│  STEP 2: GENERATE PROMPTS.md                                      │
│  ├─→ Create Google Stitch prompts for each screen                │
│  ├─→ Include: colors, typography, spacing, components             │
│  ├─→ Follow Material Design 3 guidelines                          │
│  └─→ Write to features/[feature]/mockups/PROMPTS.md              │
│                                                                    │
│  STEP 3: GENERATE design-tokens.json                              │
│  ├─→ Extract color tokens (primary, surface, error, success)     │
│  ├─→ Extract typography tokens                                    │
│  ├─→ Extract spacing and radius tokens                            │
│  ├─→ List components and screens                                  │
│  └─→ Write to features/[feature]/mockups/design-tokens.json      │
│                                                                    │
│  STEP 4: OUTPUT NEXT STEPS                                        │
│  ├─→ Instructions to use Google Stitch                           │
│  ├─→ How to export to Figma                                       │
│  └─→ Remind to update FIGMA_LINKS.md                             │
│                                                                    │
└───────────────────────────────────────────────────────────────────┘
```

### PROMPTS.md Format (Google Stitch)

```markdown
# [Feature] - AI Mockup Prompts

> **Generated from**: features/[feature]/MOCKUP.md
> **Generated on**: [DATE]
> **AI Tool**: Google Stitch (recommended)

## Screen 1: [Screen Name]

### Google Stitch Prompt

Create a mobile [screen type] screen with Material Design 3:

**App Context:**
Mifos Mobile - Self-service banking app for viewing accounts and transactions.

**Screen Size:** 393 x 852 pixels (iPhone 14 Pro equivalent)

**Header Section:**
- [Component details from MOCKUP.md]

**Main Content:**
- [Section details from MOCKUP.md]

**Style Guidelines:**
- Primary Gradient: #667EEA → #764BA2
- Surface: #FFFBFE
- Typography: Inter font family
- Spacing: 16px standard padding
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
