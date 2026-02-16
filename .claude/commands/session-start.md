# Session Start Command

Resume context from previous sessions and identify current work.

## Instructions

### Step 1: Read Context Files

Read these files in order:
1. `claude-product-cycle/CURRENT_WORK.md` - Active work from last session
2. `claude-product-cycle/PRODUCT_MAP.md` - Master status tracker
3. Recent git commits: `git log --oneline -10`

### Step 2: Generate Session Summary

```markdown
## Session Resumed

**Last Session**: {{LAST_SESSION_DATE}}
**Branch**: {{CURRENT_BRANCH}}

---

### Active Work (from CURRENT_WORK.md)

| Task | Feature | Status | Next Action |
|------|---------|:------:|-------------|
{{ACTIVE_TASKS}}

---

### Recent Changes

```
{{RECENT_COMMITS}}
```

---

### Quick Status

| Layer | Progress | Gaps |
|-------|:--------:|------|
| Design | {{DESIGN_PCT}}% | {{DESIGN_GAPS}} |
| Client | {{CLIENT_PCT}}% | {{CLIENT_GAPS}} |
| Feature | {{FEATURE_PCT}}% | {{FEATURE_GAPS}} |

---

### Suggested Actions

1. {{CONTINUE_TASK}} - Continue from last session
2. `/gap-analysis` - Full status dashboard
3. `/gap-planning [feature]` - Plan next implementation

**Ready to continue?**
```

## Output Rules

1. Always read CURRENT_WORK.md first
2. Show what was in progress
3. Show recent commits for context
4. Suggest clear next action
5. Be concise - user wants to resume quickly
