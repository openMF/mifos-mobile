# /design - Feature Specification (O(1) Enhanced)

## Purpose
Create or update feature specifications (SPEC.md + API.md) that define what to build and how to build it.

---

## Command Variants

```
/design                         # Show feature list with status (O(1)
/design [Feature]               # Full spec review/create
/design [Feature] add [section] # Add specific section
/design [Feature] improve       # Suggest improvements
/design [Feature] mockup        # Generate Figma mockups for feature
/design mockup                  # Generate Figma mockups for all features
```

---

## O(1) Workflow

```
+-------------------------------------------------------------------------+
|                    /design WORKFLOW (O(1) ENHANCED)                      |
+-------------------------------------------------------------------------+
|                                                                          |
|  PHASE 0: O(1) CONTEXT LOADING (~300 lines total)                       |
|  +--> Read FEATURES_INDEX.md       --> Feature exists? SPEC/API status? |
|  +--> Read MOCKUPS_INDEX.md        --> Mockup status (4 file types)     |
|  +--> Read API_INDEX.md            --> All endpoints for reference      |
|  +--> O(1) path: features/[name]/  --> Direct file access               |
|                                                                          |
|  PHASE 1: FEATURE STATUS (From Index)                                   |
|  +--> Check if feature exists in FEATURES_INDEX                         |
|  +--> Get SPEC/API/STATUS/Mockups status from index                     |
|  +--> Determine: Create new vs Update existing                          |
|                                                                          |
|  PHASE 2: GATHER CONTEXT (O(1) Paths)                                   |
|  +--> Read features/[feature]/SPEC.md (if exists)                       |
|  +--> Read features/[feature]/API.md (if exists)                        |
|  +--> Read features/[feature]/STATUS.md (if exists)                     |
|  +--> Lookup API endpoints from API_INDEX.md                            |
|                                                                          |
|  PHASE 3: ANALYZE & UPDATE                                              |
|  +--> Compare current spec vs requirements                              |
|  +--> Identify gaps, outdated sections                                  |
|  +--> Update/create spec files                                          |
|                                                                          |
|  PHASE 4: INDEX UPDATE (Mandatory)                                      |
|  +--> Update FEATURES_INDEX.md (if new feature)                         |
|  +--> Update STATUS.md (layer status)                                   |
|  +--> Update feature STATUS.md                                          |
|                                                                          |
+-------------------------------------------------------------------------+
```

---

## Phase 0: O(1) Context Loading

### Index Files to Read

| File | Purpose | Lines |
|------|---------|:-----:|
| `design-spec-layer/FEATURES_INDEX.md` | All features + SPEC/API status | ~120 |
| `design-spec-layer/MOCKUPS_INDEX.md` | Mockup completion matrix | ~150 |
| `server-layer/API_INDEX.md` | All API endpoints | ~400 |

### O(1) Path Pattern

```
features/[name]/SPEC.md       # Specification
features/[name]/API.md        # API requirements
features/[name]/STATUS.md     # Feature status
features/[name]/MOCKUP.md     # v2.0 ASCII mockup
features/[name]/mockups/      # Generated mockup files
```

---

## If No Feature Name Provided

Read from FEATURES_INDEX.md and show:

```
+========================================================================+
|  DESIGN LAYER - FEATURE STATUS (O(1) Lookup)                           |
+========================================================================+

| # | Feature | SPEC | API | STATUS | Mockups | Command |
|:-:|---------|:----:|:---:|:------:|:-------:|---------|
| 1 | accounts | [s] | [a] | [st] | [m] | /design accounts |
| 2 | auth | [s] | [a] | [st] | [m] | /design auth |
| ... (all from FEATURES_INDEX.md)

Legend: [s]=SPEC [a]=API [st]=STATUS [m]=Mockups

**Design Progress**: {complete}/{total} features ({percentage}%)

+------------------------------------------------------------------------+
|  QUICK ACTIONS                                                          |
+------------------------------------------------------------------------+
| Create/Update Spec | /design [feature]                                  |
| Generate Mockups   | /design [feature] mockup                           |
| All Mockups        | /design mockup                                     |
| Improve Feature    | /design [feature] improve                          |
+------------------------------------------------------------------------+
```

---

## Mockup Sub-Command (O(1) Enhanced)

### `/design [Feature] mockup`

```
+-------------------------------------------------------------------------+
|                /design [Feature] mockup WORKFLOW                         |
+-------------------------------------------------------------------------+
|                                                                          |
|  PHASE 0: O(1) STATUS CHECK                                             |
|  +--> Read MOCKUPS_INDEX.md                                             |
|  +--> Check feature row: FIGMA | PROMPTS_FIGMA | PROMPTS_STITCH | tokens|
|  +--> Identify: What exists? What's missing?                            |
|                                                                          |
|  PHASE 1: MCP & TOOL CHECK                                              |
|  +--> Check MCP: claude mcp list                                        |
|  +--> If stitch-ai configured: Use Google Stitch                        |
|  +--> If figma configured: Use Figma MCP                                |
|  +--> Otherwise: Ask user to select tool                                |
|                                                                          |
|  PHASE 2: READ MOCKUP.md                                                |
|  +--> Read features/[feature]/MOCKUP.md (v2.0 ASCII design)             |
|  +--> Parse screen layouts, components, colors                          |
|  +--> Identify all screens and UI elements                              |
|                                                                          |
|  PHASE 3: GENERATE OUTPUTS                                              |
|  +--> If missing: Generate PROMPTS_FIGMA.md                             |
|  +--> If missing: Generate PROMPTS_STITCH.md                            |
|  +--> If missing: Generate design-tokens.json                           |
|  +--> Skip files that already exist (from MOCKUPS_INDEX)                |
|                                                                          |
|  PHASE 4: INDEX UPDATE                                                  |
|  +--> Update MOCKUPS_INDEX.md with new status                           |
|  +--> Update FEATURES_INDEX.md Mockups column                           |
|                                                                          |
+-------------------------------------------------------------------------+
```

### `/design mockup` (All Features)

Uses O(1) lookup from MOCKUPS_INDEX.md to identify all gaps:

```
+-------------------------------------------------------------------------+
|  MOCKUP GENERATION STATUS (from MOCKUPS_INDEX.md)                        |
+-------------------------------------------------------------------------+

| Feature | FIGMA | PROMPTS_F | PROMPTS_S | Tokens | Status |
|---------|:-----:|:---------:|:---------:|:------:|--------|
| auth | [x] | [x] | [x] | [x] | Complete |
| dashboard | [ ] | [x] | [x] | [x] | Need FIGMA |
| accounts | [ ] | [x] | [x] | [ ] | Need FIGMA, tokens |
| ... (from MOCKUPS_INDEX)

**Summary**:
- Complete: {n} features
- Need FIGMA_LINKS: {n} features
- Need Prompts: {n} features
- Need Tokens: {n} features

**Next Step**: Generate missing files for [first-incomplete-feature]
```

---

## Tool Selection

### Check MCP First

```bash
claude mcp list
```

### AI Design Tools

| Tool | MCP | Best For | Setup |
|------|:---:|----------|-------|
| **Google Stitch** | YES | Material Design 3, Android/KMP | `claude mcp add stitch-ai -- npx -y stitch-ai-mcp` |
| **Figma** | YES | Team collaboration | `claude mcp add figma -- npx -y figma-mcp --token TOKEN` |
| Uizard | NO | Quick prototypes | Manual (web) |
| Visily | NO | Component-focused | Manual (web) |

**Recommended**: Google Stitch (MD3 native, has MCP)

### Tool Selection Prompt (If Not Configured)

```
Select AI Design Tool:

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

---

## Output Files Structure

```
features/[Feature]/mockups/
+-- PROMPTS_FIGMA.md           # Figma-specific prompts
+-- PROMPTS_STITCH.md          # Google Stitch prompts
+-- design-tokens.json         # Structured design tokens
+-- FIGMA_LINKS.md             # Figma URLs (user fills after export)
```

---

## PROMPTS_STITCH.md Format

```markdown
# [Feature] - Google Stitch Prompts

> **Generated from**: features/[feature]/MOCKUP.md
> **Generated on**: [DATE]
> **AI Tool**: Google Stitch

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
- Primary Gradient: #667EEA -> #764BA2
- Surface: #FFFBFE
- Typography: Inter font family
- Spacing: 16px standard padding
```

---

## Main Workflow: `/design [Feature]`

```
+-------------------------------------------------------------------------+
|                    /design [Feature] WORKFLOW                            |
+-------------------------------------------------------------------------+
|                                                                          |
|  PHASE 0: O(1) CONTEXT LOADING                                          |
|  +--> Read FEATURES_INDEX.md --> Feature exists? Status?                |
|  +--> Read MOCKUPS_INDEX.md --> Mockup status                           |
|  +--> Read API_INDEX.md --> Related endpoints                           |
|                                                                          |
|  PHASE 1: DETERMINE ACTION                                              |
|  +--> If feature NOT in index: Create new feature                       |
|  +--> If SPEC missing: Create SPEC.md                                   |
|  +--> If API missing: Create API.md                                     |
|  +--> If exists: Update/improve existing                                |
|                                                                          |
|  PHASE 2: GATHER CONTEXT (O(1) Paths)                                   |
|  +--> Read features/[feature]/SPEC.md                                   |
|  +--> Read features/[feature]/API.md                                    |
|  +--> Read features/[feature]/STATUS.md                                 |
|  +--> Lookup endpoints from API_INDEX.md                                |
|  +--> Read actual code: feature/[feature]/ (if exists)                  |
|                                                                          |
|  PHASE 3: ANALYZE                                                       |
|  +--> Compare current spec vs implementation                            |
|  +--> Identify gaps, outdated sections                                  |
|  +--> Check API availability in API_INDEX                               |
|  +--> Report findings to user                                           |
|                                                                          |
|  PHASE 4: UPDATE FILES                                                  |
|  +--> Update/create SPEC.md with ASCII mockups                          |
|  +--> Update/create API.md with endpoints                               |
|  +--> Update feature STATUS.md                                          |
|                                                                          |
|  PHASE 5: INDEX UPDATE (Mandatory)                                      |
|  +--> Update FEATURES_INDEX.md (status columns)                         |
|  +--> Update design-spec-layer/STATUS.md                                |
|                                                                          |
|  PHASE 6: OUTPUT SUMMARY                                                |
|  +--> Implementation requirements                                       |
|  +--> Next command suggestion                                           |
|                                                                          |
+-------------------------------------------------------------------------+
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

+-------------------------------------------+
|  <- Back          [Title]            :    |  <- TopBar
+-------------------------------------------+
|                                           |
|  +-----------------------------------+    |
|  |     Section 1                     |    |
|  +-----------------------------------+    |
|                                           |
+-------------------------------------------+

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

---

## 5. API Requirements

| Endpoint | Method | Purpose | Status |
|----------|--------|---------|--------|
| /self/[path] | GET | [Description] | Exists |

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
Headers:
  Authorization: Basic {token}
  Fineract-Platform-TenantId: {tenant}

**Response**:
{
    "field": "value"
}

**Kotlin DTO**:
@Serializable
data class [Name]Dto(
    @SerialName("field") val field: String,
)

**Status**: Implemented / Missing

---

## API Summary

| Endpoint | Service | Repository | Status |
|----------|---------|------------|--------|
| /self/[path] | [Name]Service | [Name]Repository | Done |
```

---

## Output Template

After completing design, output:

```
+=========================================================================+
|            IMPLEMENTATION REQUIREMENTS                                   |
|            Ready for /implement in Sonnet session                        |
+=========================================================================+
|                                                                          |
|  FEATURE: [Feature Name]                                                |
|  SPEC UPDATED: features/[feature]/SPEC.md                               |
|                                                                          |
|  ================================================================       |
|                                                                          |
|  CLIENT WORK NEEDED:                                                    |
|  [ ] Network: [DTO/Service changes]                                     |
|  [ ] Data: [Repository changes]                                         |
|                                                                          |
|  FEATURE WORK NEEDED:                                                   |
|  [ ] ViewModel: [changes]                                               |
|  [ ] Screen: [changes]                                                  |
|  [ ] Components: [new components]                                       |
|                                                                          |
|  ================================================================       |
|                                                                          |
|  INDEXES UPDATED:                                                       |
|  [x] FEATURES_INDEX.md - Status updated                                 |
|  [x] design-spec-layer/STATUS.md - Layer status                         |
|  [x] features/[feature]/STATUS.md - Feature status                      |
|                                                                          |
|  ================================================================       |
|                                                                          |
|  NEXT STEP:                                                             |
|  Run:  /implement [Feature]                                             |
|                                                                          |
+=========================================================================+
```

---

## Feature Reference (From FEATURES_INDEX.md)

| # | Feature | Design Dir | Feature Dir |
|:-:|---------|------------|-------------|
| 1 | accounts | features/accounts/ | feature/account/ |
| 2 | auth | features/auth/ | feature/auth/ |
| 3 | beneficiary | features/beneficiary/ | feature/beneficiary/ |
| 4 | client-charge | features/client-charge/ | feature/user-profile/ |
| 5 | dashboard | features/dashboard/ | feature/dashboard/ |
| 6 | guarantor | features/guarantor/ | feature/guarantor/ |
| 7 | home | features/home/ | feature/home/ |
| 8 | loan-account | features/loan-account/ | feature/loan-account/ |
| 9 | location | features/location/ | feature/location/ |
| 10 | notification | features/notification/ | feature/notification/ |
| 11 | passcode | features/passcode/ | libs/mifos-passcode/ |
| 12 | qr | features/qr/ | feature/qr-code/ |
| 13 | recent-transaction | features/recent-transaction/ | feature/recent-transaction/ |
| 14 | savings-account | features/savings-account/ | feature/savings-account/ |
| 15 | settings | features/settings/ | feature/settings/ |
| 16 | share-account | features/share-account/ | feature/share-account/ |
| 17 | transfer | features/transfer/ | feature/transfer-process/ |

---

## Error Handling

### Feature Not Found

```
+-------------------------------------------------------------------------+
|  ERROR: Feature '[name]' not found                                       |
+-------------------------------------------------------------------------+
|                                                                          |
|  The feature '[name]' does not exist in FEATURES_INDEX.md               |
|                                                                          |
|  OPTIONS:                                                               |
|  1. Create new feature: /design [name]                                  |
|  2. Check available features: /design                                   |
|  3. Similar features: [suggestions based on name]                       |
|                                                                          |
+-------------------------------------------------------------------------+
```

### Invalid Sub-command

```
+-------------------------------------------------------------------------+
|  ERROR: Invalid sub-command '[sub]'                                      |
+-------------------------------------------------------------------------+
|                                                                          |
|  Valid sub-commands:                                                    |
|  - mockup    : Generate mockup prompts                                  |
|  - improve   : Suggest improvements                                     |
|  - add [x]   : Add specific section                                     |
|                                                                          |
+-------------------------------------------------------------------------+
```

---

## Model Recommendation

**This command is optimized for Opus** for complex architectural decisions and comprehensive specification writing.

---

## Related Commands

| Command | Purpose |
|---------|---------|
| `/gap-analysis design` | See design layer gaps |
| `/gap-analysis design mockup` | See mockup gaps specifically |
| `/implement [feature]` | Implement the designed feature |
| `/verify [feature]` | Verify implementation vs spec |

---

## Key Files

```
claude-product-cycle/design-spec-layer/
+-- FEATURES_INDEX.md             # O(1) feature lookup
+-- MOCKUPS_INDEX.md              # O(1) mockup status
+-- STATUS.md                     # Layer status
+-- features/[feature]/
    +-- SPEC.md                   # What to build (UI, flows)
    +-- API.md                    # APIs needed
    +-- STATUS.md                 # Feature implementation status
    +-- MOCKUP.md                 # v2.0 ASCII mockup
    +-- mockups/                  # Generated mockup files
```
