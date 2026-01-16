# Session End Command

Save current work context for the next session.

## Usage

```
/session-end                    # Save context and summarize session
/session-end "brief note"       # Save with custom note
```

## Instructions

### Step 1: Gather Session Information

1. Check git status: `git status`
2. Check recent commits this session: `git log --oneline -5`
3. Review any uncommitted changes

### Step 2: Update CURRENT_WORK.md

**Update** `claude-product-cycle/CURRENT_WORK.md`:

```markdown
# Current Work

**Last Updated**: {{DATE}} {{TIME}}
**Branch**: {{BRANCH}}
**Session Note**: {{USER_NOTE_OR_AUTO_SUMMARY}}

---

## Active Tasks

| # | Task | Feature | Status | Files | Notes |
|---|------|---------|:------:|-------|-------|
{{ACTIVE_TASKS_FROM_SESSION}}

---

## In Progress

### {{FEATURE_NAME}}

**What was done**:
- {{COMPLETED_ITEMS}}

**What's next**:
- {{NEXT_ITEMS}}

**Key files touched**:
- {{FILE_LIST}}

---

## Uncommitted Changes

```
{{GIT_STATUS_OUTPUT}}
```

---

## Resume Instructions

1. Run `/session-start` to load this context
2. Continue with: {{NEXT_ACTION}}
3. Key context: {{IMPORTANT_NOTES}}
```

### Step 3: Prompt for Commit

If there are uncommitted changes:

```markdown
---

### Uncommitted Changes Detected

You have uncommitted changes. Shall I:

1. **Commit now** - Create a WIP commit to save progress
2. **Stash changes** - Save for later without committing
3. **Leave as-is** - Keep changes uncommitted

Recommended: Option 1 with message:
`wip: [feature] - {{AUTO_SUMMARY}}`
```

### Step 4: Output Session Summary

```markdown
## Session Saved

**Duration**: {{APPROXIMATE_WORK}}
**Commits**: {{COMMIT_COUNT}} commits this session
**Files Changed**: {{FILES_CHANGED}}

---

### What We Accomplished

- {{ACCOMPLISHMENT_1}}
- {{ACCOMPLISHMENT_2}}
- {{ACCOMPLISHMENT_3}}

---

### Next Session

Start with: `/session-start`
Continue: {{NEXT_TASK}}

---

**Context saved to**: `claude-product-cycle/CURRENT_WORK.md`
```

## Output Rules

1. Always update CURRENT_WORK.md
2. Capture uncommitted changes
3. Note specific files and line numbers if relevant
4. Be specific about next actions
5. Prompt for commit if changes exist
