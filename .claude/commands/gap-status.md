# /gap-status - Plan Progress Tracking

## Purpose

Track progress on implementation plans created by `/gap-planning`. Shows current step, completed steps, and what's next.

---

## Usage

```
/gap-status                      # Show all active plans summary
/gap-status [plan-name]          # Show detailed progress for plan
/gap-status design               # Show design layer plans
/gap-status testing              # Show testing layer plans
/gap-status feature [name]       # Show feature-specific plan
/gap-status complete [plan]      # Mark plan as complete
/gap-status pause [plan]         # Pause a plan
/gap-status resume [plan]        # Resume a paused plan
```

---

## Workflow

```
┌─────────────────────────────────────────────────────────────────────────────┐
│  /gap-status WORKFLOW                                                        │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  PHASE 0: O(1) CONTEXT LOADING                                              │
│  ├─→ Read plans/PLANS_INDEX.md        → Get all plans overview              │
│  ├─→ Read plans/active/*.md           → Get active plan details             │
│  └─→ Count completed steps            → Calculate progress                  │
│                                                                              │
│  PHASE 1: DETERMINE OUTPUT                                                  │
│  ├─→ If no args: Show all active plans summary                              │
│  ├─→ If [plan-name]: Show detailed plan progress                            │
│  └─→ If action (complete/pause/resume): Update plan status                  │
│                                                                              │
│  PHASE 2: GENERATE REPORT                                                   │
│  ├─→ Progress bars for each plan                                            │
│  ├─→ Current step highlight                                                 │
│  ├─→ Next steps preview                                                     │
│  └─→ Suggested commands                                                     │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## Output: All Plans Summary (No Args)

```
╔══════════════════════════════════════════════════════════════════════════════╗
║  MIFOS MOBILE - PLAN STATUS                                                  ║
╠══════════════════════════════════════════════════════════════════════════════╣

## 🔄 Active Plans

| # | Plan | Progress | Current Step | Last Updated |
|:-:|------|:--------:|--------------|--------------|
| 1 | design-mockup | [████████░░] 80% (8/10) | Step 9: transfer mockups | 2026-01-05 |
| 2 | testing-auth | [████░░░░░░] 40% (4/10) | Step 5: LoginViewModel tests | 2026-01-05 |
| 3 | feature-dashboard | [██░░░░░░░░] 20% (2/10) | Step 3: Create DashboardViewModel | 2026-01-04 |

## ⏸️ Paused Plans

| # | Plan | Progress | Paused At | Reason |
|:-:|------|:--------:|-----------|--------|
| - | (none) | - | - | - |

## ✅ Recently Completed

| # | Plan | Steps | Completed |
|:-:|------|:-----:|-----------|
| 1 | client-layer | 12/12 | 2026-01-03 |

---

## Commands

| Action | Command |
|--------|---------|
| View plan details | `/gap-status [plan-name]` |
| Continue implementation | `/implement [target]` |
| Mark complete | `/gap-status complete [plan]` |
| Create new plan | `/gap-planning [target]` |

╚══════════════════════════════════════════════════════════════════════════════╝
```

---

## Output: Specific Plan (With Args)

```
╔══════════════════════════════════════════════════════════════════════════════╗
║  PLAN: design-mockup                                                         ║
╠══════════════════════════════════════════════════════════════════════════════╣

**Status**: 🔄 Active
**Progress**: [████████░░] 80% (8/10 steps)
**Created**: 2026-01-03
**Last Updated**: 2026-01-05

---

## Steps

| # | Step | Status | Description |
|:-:|------|:------:|-------------|
| 1 | ✅ | Done | Generate auth mockups |
| 2 | ✅ | Done | Generate home mockups |
| 3 | ✅ | Done | Generate accounts mockups |
| 4 | ✅ | Done | Generate beneficiary mockups |
| 5 | ✅ | Done | Generate loan-account mockups |
| 6 | ✅ | Done | Generate savings-account mockups |
| 7 | ✅ | Done | Generate share-account mockups |
| 8 | ✅ | Done | Generate notification mockups |
| 9 | 🔄 | **Current** | Generate transfer mockups |
| 10 | ⬜ | Pending | Generate recent-transaction mockups |

---

## Current Step Details

### Step 9: Generate transfer mockups

**Target**: `design-spec-layer/features/transfer/mockups/`

**Tasks**:
- [ ] Run `/design transfer mockup`
- [ ] Review generated PROMPTS.md
- [ ] Execute prompts in Google Stitch
- [ ] Save design-tokens.json
- [ ] Update MOCKUPS_INDEX.md

**Expected Files**:
```
features/transfer/mockups/
├── PROMPTS.md
├── PROMPTS_FIGMA.md (if MCP)
├── design-tokens.json
└── screenshots/ (optional)
```

---

## Next Step Preview

### Step 10: Generate recent-transaction mockups

**Target**: `design-spec-layer/features/recent-transaction/mockups/`

Same process as Step 9 but for recent-transaction feature.

---

## Progress Log

| Date | Step | Action |
|------|:----:|--------|
| 2026-01-05 | 8 | Completed notification mockups |
| 2026-01-05 | 9 | Started transfer mockups |
| 2026-01-04 | 5-7 | Completed account mockups |
| 2026-01-03 | 1-4 | Initial mockups complete |

---

## Commands

| Action | Command |
|--------|---------|
| Execute current step | `/design transfer mockup` |
| Mark step complete | Update plan file, re-run `/gap-status` |
| Pause plan | `/gap-status pause design-mockup` |
| Mark plan complete | `/gap-status complete design-mockup` |

╚══════════════════════════════════════════════════════════════════════════════╝
```

---

## Phase 0: O(1) Context Loading

### Files to Read

| File | Purpose | Data Extracted |
|------|---------|----------------|
| `plans/PLANS_INDEX.md` | Plan inventory | activePlans[], completedPlans[] |
| `plans/active/[plan].md` | Plan details | steps[], currentStep, progress |

---

## Plan File Structure

When `/gap-planning` creates a plan, it saves to `plans/active/[name].md`:

```markdown
# Plan: Design Layer - Mockups

**Created**: 2026-01-03
**Status**: 🔄 Active
**Command**: /gap-planning design mockup
**Progress**: 8/10 steps (80%)

---

## Overview

Generate mockups for all features missing UI designs.

---

## Steps

- [x] **Step 1**: Generate auth mockups
  - Run `/design auth mockup`
  - Files: `features/auth/mockups/`
  - Completed: 2026-01-03

- [x] **Step 2**: Generate home mockups
  - Run `/design home mockup`
  - Files: `features/home/mockups/`
  - Completed: 2026-01-04

- [ ] **Step 9**: Generate transfer mockups ← CURRENT
  - Run `/design transfer mockup`
  - Files: `features/transfer/mockups/`

- [ ] **Step 10**: Generate recent-transaction mockups
  - Run `/design recent-transaction mockup`
  - Files: `features/recent-transaction/mockups/`

---

## Progress Log

| Date | Step | Action | Notes |
|------|:----:|--------|-------|
| 2026-01-05 | 8 | ✅ Completed | notification mockups done |
| 2026-01-05 | 9 | 🔄 Started | transfer in progress |
```

---

## Step Status Icons

| Icon | Status | Meaning |
|:----:|--------|---------|
| ✅ | Done | Step completed |
| 🔄 | Current | Currently working on |
| ⬜ | Pending | Not started |
| ⏸️ | Blocked | Waiting on dependency |
| ❌ | Failed | Step failed, needs retry |

---

## Instructions

### When `/gap-status` is called (no args):

1. Read `plans/PLANS_INDEX.md`
2. For each active plan, read `plans/active/[plan].md`
3. Count `[x]` vs `[ ]` checkboxes to calculate progress
4. Display summary table with progress bars

### When `/gap-status [plan]` is called:

1. Read `plans/active/[plan].md`
2. Find current step (first `[ ]` after last `[x]`)
3. Display detailed view with:
   - All steps with status
   - Current step details
   - Next step preview
   - Progress log

### When `/gap-status complete [plan]` is called:

1. Read `plans/active/[plan].md`
2. Update status to "✅ Completed"
3. Move file to `plans/completed/[plan].md`
4. Update `plans/PLANS_INDEX.md`

---

## Integration with Other Commands

| When This Runs | Update Plan |
|----------------|-------------|
| `/gap-planning [target]` | Create new plan file |
| `/implement` checkpoint | Update related plan step |
| `/design [feature] mockup` | Update mockup plan step |
| `/verify [feature]` | Update verification plan |

---

## Progress Bar Reference

```
100% = [██████████]  |  50% = [█████░░░░░]
 90% = [█████████░]  |  40% = [████░░░░░░]
 80% = [████████░░]  |  30% = [███░░░░░░░]
 70% = [███████░░░]  |  20% = [██░░░░░░░░]
 60% = [██████░░░░]  |  10% = [█░░░░░░░░░]
```

---

## Example: Creating and Tracking a Plan

```bash
# 1. Create plan
/gap-planning design mockup
# → Creates plans/active/design-mockup.md with 10 steps

# 2. Check status
/gap-status
# → Shows design-mockup at 0% (0/10)

# 3. Work on step 1
/design auth mockup
# → Completes auth mockups

# 4. Update plan (manual or via command)
# Edit plans/active/design-mockup.md, mark step 1 as [x]

# 5. Check status again
/gap-status design-mockup
# → Shows 10% (1/10), current step is now step 2

# 6. Continue until done
# ...

# 7. Mark complete
/gap-status complete design-mockup
# → Moves to plans/completed/
```
