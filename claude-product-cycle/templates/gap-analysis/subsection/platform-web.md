# Gap Analysis: Platform → Web Sub-Section

## Web Platform Status

**Module**: `cmp-web/`
**Progress**: {{WEB_PCT}}% Complete

---

### Status Overview

```
WEB PLATFORM (JS/WASM)
{{WEB_BAR}} {{WEB_PCT}}%

├── Build      {{BUILD_STATUS}}
├── Features   {{FEATURES_COUNT}}/17 working
├── JS Target  {{JS_STATUS}}
└── WASM       {{WASM_STATUS}}
```

---

### Feature Support

| # | Feature | Status | Notes |
|:-:|---------|:------:|-------|
{{FEATURE_STATUS_ROWS}}

---

### Build Targets

| Target | Status | Notes |
|--------|:------:|-------|
| Kotlin/JS | {{JS_STATUS}} | Browser support |
| WASM | {{WASM_STATUS}} | Experimental |

---

### Browser Compatibility

| Browser | Status | Notes |
|---------|:------:|-------|
| Chrome | {{CHROME_STATUS}} | {{CHROME_NOTES}} |
| Firefox | {{FIREFOX_STATUS}} | {{FIREFOX_NOTES}} |
| Safari | {{SAFARI_STATUS}} | {{SAFARI_NOTES}} |
| Edge | {{EDGE_STATUS}} | {{EDGE_NOTES}} |

---

### Platform-Specific Issues

{{PLATFORM_ISSUES}}

---

### Next Action

{{NEXT_ACTION}}

Run `/gap-planning platform web` for step-by-step plan.
