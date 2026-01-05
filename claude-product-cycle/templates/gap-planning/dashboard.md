# Gap Planning Dashboard Template

## Mifos Mobile - Implementation Plan

**Last Updated**: {{DATE}}
**Planning Scope**: All 5 Layers

---

### Priority Summary

```
┌─────────────────────────────────────────────────────────────────┐
│  IMPLEMENTATION PRIORITIES                                       │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  P0 - Critical     {{P0_COUNT}} tasks                           │
│  P1 - High Value   {{P1_COUNT}} tasks                           │
│  P2 - Polish       {{P2_COUNT}} tasks                           │
│                                                                  │
│  Total Effort: {{TOTAL_EFFORT}}                                 │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

### Layer-by-Layer Plan

| Layer | Gaps | Priority Tasks | Effort | Command |
|-------|:----:|----------------|:------:|---------|
| Design | {{DESIGN_GAPS}} | {{DESIGN_TASKS}} | {{DESIGN_EFFORT}} | `/gap-planning design` |
| Server | {{SERVER_GAPS}} | {{SERVER_TASKS}} | {{SERVER_EFFORT}} | `/gap-planning server` |
| Client | {{CLIENT_GAPS}} | {{CLIENT_TASKS}} | {{CLIENT_EFFORT}} | `/gap-planning client` |
| Feature | {{FEATURE_GAPS}} | {{FEATURE_TASKS}} | {{FEATURE_EFFORT}} | `/gap-planning feature` |
| Platform | {{PLATFORM_GAPS}} | {{PLATFORM_TASKS}} | {{PLATFORM_EFFORT}} | `/gap-planning platform` |

---

### P0 - Critical Tasks (Do First)

| # | Layer | Task | Feature | Effort |
|---|-------|------|---------|:------:|
{{P0_TASKS_TABLE}}

---

### P1 - High Value Tasks

| # | Layer | Task | Feature | Effort |
|---|-------|------|---------|:------:|
{{P1_TASKS_TABLE}}

---

### P2 - Polish Tasks

| # | Layer | Task | Feature | Effort |
|---|-------|------|---------|:------:|
{{P2_TASKS_TABLE}}

---

### Recommended Order

```
1. {{FIRST_TASK}}
   └── /gap-planning {{FIRST_FEATURE}}

2. {{SECOND_TASK}}
   └── /gap-planning {{SECOND_FEATURE}}

3. {{THIRD_TASK}}
   └── /gap-planning {{THIRD_FEATURE}}
```

---

### Quick Wins (< 1 hour each)

{{QUICK_WINS_LIST}}

---

**Start with**: `/gap-planning {{RECOMMENDED_START}}`
