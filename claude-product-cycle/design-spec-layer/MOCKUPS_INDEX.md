# Mockups Index - O(1) Lookup

> **Figma**: 7/18 | **Stitch**: 11/18 | **Tokens**: 8/18

---

## Status Matrix

| Feature | FIGMA_LINKS | PROMPTS_FIGMA | PROMPTS_STITCH | design-tokens |
|---------|:-----------:|:-------------:|:--------------:|:-------------:|
| accounts | ❌ | ✅ | ✅ | ❌ |
| auth | ✅ | ✅ | ✅ | ✅ |
| beneficiary | ❌ | ✅ | ✅ | ❌ |
| client-charge | ✅ | ❌ | ❌ | ✅ |
| dashboard | ❌ | ✅ | ✅ | ✅ |
| guarantor | ✅ | ❌ | ❌ | ✅ |
| home | ❌ | ✅ | ✅ | ❌ |
| loan-account | ❌ | ✅ | ✅ | ❌ |
| location | ✅ | ❌ | ❌ | ✅ |
| notification | ❌ | ✅ | ✅ | ❌ |
| passcode | ✅ | ❌ | ❌ | ✅ |
| qr | ✅ | ❌ | ❌ | ✅ |
| recent-transaction | ❌ | ✅ | ✅ | ❌ |
| savings-account | ❌ | ✅ | ✅ | ❌ |
| settings | ✅ | ❌ | ❌ | ✅ |
| share-account | ❌ | ✅ | ✅ | ❌ |
| transfer | ❌ | ✅ | ✅ | ❌ |

**Legend**: ✅ Exists | ❌ Missing

---

## O(1) File Access

| Need | Path |
|------|------|
| Figma Links | `features/[name]/mockups/FIGMA_LINKS.md` |
| Figma Prompts | `features/[name]/mockups/PROMPTS_FIGMA.md` |
| Stitch Prompts | `features/[name]/mockups/PROMPTS_STITCH.md` |
| Design Tokens | `features/[name]/mockups/design-tokens.json` |

---

## Completion Summary

### Complete (All 4 Files)

| Feature | Status |
|---------|--------|
| auth | ✅ All mockup files present |

### Has Figma Links + Tokens (Need Prompts)

| Feature | Has | Needs |
|---------|-----|-------|
| client-charge | FIGMA_LINKS, design-tokens | PROMPTS_FIGMA, PROMPTS_STITCH |
| guarantor | FIGMA_LINKS, design-tokens | PROMPTS_FIGMA, PROMPTS_STITCH |
| location | FIGMA_LINKS, design-tokens | PROMPTS_FIGMA, PROMPTS_STITCH |
| passcode | FIGMA_LINKS, design-tokens | PROMPTS_FIGMA, PROMPTS_STITCH |
| qr | FIGMA_LINKS, design-tokens | PROMPTS_FIGMA, PROMPTS_STITCH |
| settings | FIGMA_LINKS, design-tokens | PROMPTS_FIGMA, PROMPTS_STITCH |

### Has Prompts (Need Figma Links + Tokens)

| Feature | Has | Needs |
|---------|-----|-------|
| accounts | PROMPTS_FIGMA, PROMPTS_STITCH | FIGMA_LINKS, design-tokens |
| beneficiary | PROMPTS_FIGMA, PROMPTS_STITCH | FIGMA_LINKS, design-tokens |
| home | PROMPTS_FIGMA, PROMPTS_STITCH | FIGMA_LINKS, design-tokens |
| loan-account | PROMPTS_FIGMA, PROMPTS_STITCH | FIGMA_LINKS, design-tokens |
| notification | PROMPTS_FIGMA, PROMPTS_STITCH | FIGMA_LINKS, design-tokens |
| recent-transaction | PROMPTS_FIGMA, PROMPTS_STITCH | FIGMA_LINKS, design-tokens |
| savings-account | PROMPTS_FIGMA, PROMPTS_STITCH | FIGMA_LINKS, design-tokens |
| share-account | PROMPTS_FIGMA, PROMPTS_STITCH | FIGMA_LINKS, design-tokens |
| transfer | PROMPTS_FIGMA, PROMPTS_STITCH | FIGMA_LINKS, design-tokens |

### Has Prompts + Tokens (Need Figma Links)

| Feature | Has | Needs |
|---------|-----|-------|
| dashboard | PROMPTS_FIGMA, PROMPTS_STITCH, design-tokens | FIGMA_LINKS |

---

## Gaps by File Type

### Need FIGMA_LINKS.md (11 features)

```
accounts, beneficiary, dashboard, home, loan-account,
notification, recent-transaction, savings-account,
share-account, transfer
```

### Need design-tokens.json (9 features)

```
accounts, beneficiary, home, loan-account, notification,
recent-transaction, savings-account, share-account, transfer
```

### Need PROMPTS_FIGMA.md (6 features)

```
client-charge, guarantor, location, passcode, qr, settings
```

### Need PROMPTS_STITCH.md (6 features)

```
client-charge, guarantor, location, passcode, qr, settings
```

---

## Design Tool Workflows

### Figma-First Workflow

```
1. Create in Figma
2. Add link to FIGMA_LINKS.md
3. Export design-tokens.json
```

### AI-Generation Workflow (Google Stitch)

```
1. Write PROMPTS_STITCH.md
2. Generate mockups
3. Export to Figma
4. Update FIGMA_LINKS.md
```

---

## Related Files

- [FEATURES_INDEX.md](FEATURES_INDEX.md) - Feature overview
- [TOOL_CONFIG.md](TOOL_CONFIG.md) - Tool configuration
- [STATUS.md](STATUS.md) - Layer status

---

## Auto-Update Rules

| Scenario | Action |
|----------|--------|
| Figma link added | Update FIGMA_LINKS column to ✅ |
| Prompts created | Update respective column to ✅ |
| Tokens exported | Update design-tokens column to ✅ |
| All 4 files done | Move to "Complete" section |
