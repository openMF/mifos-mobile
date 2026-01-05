# Mockups Directory Structure

> **Purpose**: Store and organize UI mockup images for each feature
> **Last Updated**: 2025-01-04

---

## Directory Structure

```
features/
└── [feature-name]/
    └── mockups/
        ├── PROMPTS.md          # AI design tool prompts
        ├── design-tokens.json  # Structured design tokens
        ├── FIGMA_LINKS.md      # Figma export URLs
        ├── dummy/              # Placeholder images (reference)
        │   ├── 01-screen-name.png
        │   ├── 02-screen-name.png
        │   └── ...
        └── prod/               # Production mockups (actual designs)
            ├── 01-screen-name.png
            ├── 02-screen-name.png
            └── ...
```

---

## Usage

### For Designers

1. **Generate designs** using prompts from `PROMPTS.md`:
   - Google Stitch: [stitch.withgoogle.com](https://stitch.withgoogle.com)
   - Figma: Use Figma AI or manual design

2. **Export designs** as PNG/JPG at:
   - 1x (375px width for mobile)
   - 2x (750px) - optional for high-res
   - 3x (1125px) - optional for high-res

3. **Save to `prod/`** with matching filenames:
   - Replace `dummy/01-login-screen.png`
   - With `prod/01-login-screen.png`

4. **Update `FIGMA_LINKS.md`** with Figma URLs

### For Developers (Claude)

When implementing screens, I will:
1. Check `prod/` first for actual designs
2. Fall back to `dummy/` for placeholder reference
3. Use `design-tokens.json` for colors, spacing, typography
4. Reference screen naming for navigation flow

---

## Naming Convention

```
[order]-[screen-name].png

Examples:
01-login-screen.png
02-registration-screen.png
03-forgot-password.png
```

### Order Prefix
- `01-09`: Main screens
- `10-19`: Secondary screens
- `20-29`: Modals/dialogs
- `30-39`: Empty/error states
- `40-49`: Dark mode variants

---

## Feature Screen Inventory

| Feature | Screens | dummy/ | prod/ |
|---------|:-------:|:------:|:-----:|
| auth | 6 | ✅ | ⏳ |
| home | 5 | ✅ | ⏳ |
| accounts | 4 | ✅ | ⏳ |
| savings-account | 6 | ✅ | ⏳ |
| loan-account | 7 | ✅ | ⏳ |
| share-account | 6 | ✅ | ⏳ |
| beneficiary | 6 | ✅ | ⏳ |
| transfer | 6 | ✅ | ⏳ |
| recent-transaction | 6 | ✅ | ⏳ |
| notification | 5 | ✅ | ⏳ |
| settings | 9 | ✅ | ⏳ |
| passcode | 7 | ✅ | ⏳ |
| guarantor | 8 | ✅ | ⏳ |
| qr | 7 | ✅ | ⏳ |
| location | 8 | ✅ | ⏳ |
| client-charge | 9 | ✅ | ⏳ |
| dashboard | 7 | ✅ | ⏳ |

**Legend**: ✅ Complete | ⏳ Pending | ❌ Missing

---

## Dark Mode Variants

For dark mode, add `-dark` suffix:
```
01-login-screen.png       # Light mode
01-login-screen-dark.png  # Dark mode
```

---

## Image Specifications

| Property | Value |
|----------|-------|
| Format | PNG (preferred) or JPG |
| Width | 375px (1x) / 750px (2x) |
| Color Profile | sRGB |
| Compression | Optimized for web |

---

## Workflow

```
1. Read PROMPTS.md for feature
        ↓
2. Generate in Stitch/Figma
        ↓
3. Export as PNG
        ↓
4. Save to prod/ folder
        ↓
5. Update FIGMA_LINKS.md
        ↓
6. Commit & push
```
