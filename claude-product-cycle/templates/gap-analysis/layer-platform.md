# Platform Layer Analysis Template

## Platform Layer Gap Analysis

**Modules**: `cmp-android/`, `cmp-ios/`, `cmp-desktop/`, `cmp-web/`
**Last Updated**: {{DATE}}

---

### Platform Support Matrix

| Feature | Android | iOS | Desktop | Web |
|---------|:-------:|:---:|:-------:|:---:|
{{PLATFORM_MATRIX_ROWS}}

---

### Platform Health

```
PLATFORM LAYER BREAKDOWN
├── Android    {{ANDROID_BAR}} {{ANDROID_PCT}}%  {{ANDROID_STATUS}}
├── iOS        {{IOS_BAR}} {{IOS_PCT}}%  {{IOS_STATUS}}
├── Desktop    {{DESKTOP_BAR}} {{DESKTOP_PCT}}%  {{DESKTOP_STATUS}}
└── Web        {{WEB_BAR}} {{WEB_PCT}}%  {{WEB_STATUS}}

OVERALL        {{OVERALL_BAR}} {{OVERALL_PCT}}%
```

---

### Platform-Specific Issues

#### Desktop
| Issue | Feature | Status | Fix |
|-------|---------|:------:|-----|
{{DESKTOP_ISSUES}}

#### Web (JS/WASM)
| Issue | Feature | Status | Fix |
|-------|---------|:------:|-----|
{{WEB_ISSUES}}

---

### Build Status

| Platform | Build | Tests | Last Verified |
|----------|:-----:|:-----:|---------------|
{{BUILD_STATUS_ROWS}}

---

### Recommendations

{{PLATFORM_RECOMMENDATIONS}}

---

**Next**: `/gap-planning [feature]` to plan improvements
