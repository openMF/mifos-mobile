# Plans Index - O(1) Lookup

> Track implementation plans created by `/gap-planning`

**Last Updated**: 2026-01-05

---

## Quick Overview

| Status | Count | Description |
|:------:|:-----:|-------------|
| 🔄 Active | 0 | Plans in progress |
| ✅ Completed | 0 | Finished plans |
| ⏸️ Paused | 0 | Plans on hold |

---

## Active Plans

| # | Plan | Target | Progress | Current Step | Created |
|:-:|------|--------|:--------:|--------------|---------|
| - | (none) | - | - | - | - |

---

## Completed Plans

| # | Plan | Target | Steps | Completed |
|:-:|------|--------|:-----:|-----------|
| - | (none) | - | - | - |

---

## Plan File Format

Each plan file in `active/` or `completed/` follows this format:

```markdown
# Plan: [Target Name]

**Created**: YYYY-MM-DD
**Status**: 🔄 Active | ✅ Completed | ⏸️ Paused
**Command**: /gap-planning [args]
**Progress**: X/Y steps (Z%)

---

## Steps

- [ ] **Step 1**: Description
  - Sub-task 1
  - Sub-task 2
  - Files: `path/to/file.kt`

- [ ] **Step 2**: Description
  - Sub-task 1
  - Files: `path/to/file.kt`

- [x] **Step 3**: Description (COMPLETED)
  - ✅ Sub-task 1
  - ✅ Sub-task 2

---

## Progress Log

| Date | Step | Action | Outcome |
|------|------|--------|---------|
| YYYY-MM-DD | 1 | Started | In progress |
| YYYY-MM-DD | 1 | Completed | Files created |
```

---

## O(1) Path Pattern

```
plans/active/[target]-[type].md     # Active plan
plans/completed/[target]-[type].md  # Completed plan
```

Examples:
- `plans/active/design-mockup.md`
- `plans/active/testing-auth.md`
- `plans/active/feature-beneficiary.md`
- `plans/completed/client-layer.md`

---

## Commands

```bash
# Check status of all plans
/gap-status                    # Shows this index + active plans

# Check status of specific plan
/gap-status design mockup      # Shows design-mockup.md progress

# Create new plan
/gap-planning [target]         # Creates plan in plans/active/

# Mark plan complete
/gap-status complete [plan]    # Moves to plans/completed/
```

---

## Auto-Update Rules

| Trigger | Action |
|---------|--------|
| `/gap-planning [target]` | Create plan file, add to Active Plans |
| `/gap-status complete [plan]` | Move to Completed Plans |
| Step completed | Update progress in plan file |
| `/implement` checkpoint | Update related plan progress |
