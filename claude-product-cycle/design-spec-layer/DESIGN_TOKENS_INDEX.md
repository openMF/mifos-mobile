# Design Tokens Index - O(1) Lookup

> **8 features** with tokens | **2 formats** (Google Stitch, MD3) | **Last Updated**: 2026-01-05

---

## Quick Lookup

| # | Feature | Has Tokens | Format | Colors | Typography | Components | Animations |
|:-:|---------|:----------:|--------|:------:|:----------:|:----------:|:----------:|
| 1 | auth | ✅ | google-stitch | ✅ | ✅ | ✅ | ✅ |
| 2 | dashboard | ✅ | md3 | ✅ | ✅ | ✅ | ❌ |
| 3 | settings | ✅ | md3 | ✅ | ✅ | ❌ | ❌ |
| 4 | guarantor | ✅ | md3 | ✅ | ✅ | ❌ | ❌ |
| 5 | qr | ✅ | md3 | ✅ | ✅ | ❌ | ❌ |
| 6 | passcode | ✅ | md3 | ✅ | ✅ | ❌ | ❌ |
| 7 | location | ✅ | md3 | ✅ | ✅ | ❌ | ❌ |
| 8 | client-charge | ✅ | md3 | ✅ | ✅ | ❌ | ❌ |
| 9 | accounts | ❌ | - | - | - | - | - |
| 10 | beneficiary | ❌ | - | - | - | - | - |
| 11 | home | ❌ | - | - | - | - | - |
| 12 | loan-account | ❌ | - | - | - | - | - |
| 13 | notification | ❌ | - | - | - | - | - |
| 14 | recent-transaction | ❌ | - | - | - | - | - |
| 15 | savings-account | ❌ | - | - | - | - | - |
| 16 | share-account | ❌ | - | - | - | - | - |
| 17 | transfer | ❌ | - | - | - | - | - |

---

## Token Formats

### Google Stitch Format (v2.0)

```json
{
  "feature": "auth",
  "generated": "2026-01-03",
  "tool": "google-stitch",
  "version": "2.0",
  "tokens": {
    "colors": {
      "primary": { "gradient": {...}, "solid": "#667EEA" },
      "surface": { "light": "#FFFFFF", "dark": "#0D1117" },
      "text": { "primary": {...}, "secondary": {...} },
      "semantic": { "success": "#00D09C", "error": "#FF4757" }
    },
    "typography": { "fontFamily": "Inter", "display": {...}, "headline": {...} },
    "spacing": { "xs": "4dp", "sm": "8dp", "md": "12dp", "lg": "16dp" },
    "radius": { "sm": "8dp", "md": "12dp", "lg": "16dp" },
    "shadow": { "button": {...}, "card": {...} }
  },
  "screens": [...],
  "components": [...],
  "animations": {...}
}
```

### MD3 Format (Standard)

```json
{
  "feature": "Dashboard",
  "generated": "2025-12-28",
  "tokens": {
    "colors": { "primary": "#6750A4", "surface": "#FFFBFE", ... },
    "typography": { "displayLarge": {...}, "bodyMedium": {...} },
    "spacing": { "xs": 4, "sm": 8, "md": 16 },
    "radius": { "sm": 8, "md": 12, "lg": 16 }
  },
  "components": [...],
  "screens": [...]
}
```

---

## O(1) Path Pattern

```
design-spec-layer/features/[feature]/mockups/design-tokens.json
```

---

## Token → DesignToken Mapping

| Token JSON | Compose DesignToken | Type |
|------------|---------------------|------|
| `tokens.spacing.xs` | `DesignToken.spacing.extraSmall` | `Dp` |
| `tokens.spacing.sm` | `DesignToken.spacing.small` | `Dp` |
| `tokens.spacing.md` | `DesignToken.spacing.medium` | `Dp` |
| `tokens.spacing.lg` | `DesignToken.spacing.large` | `Dp` |
| `tokens.radius.sm` | `DesignToken.shapes.small` | `Shape` |
| `tokens.radius.md` | `DesignToken.shapes.medium` | `Shape` |
| `tokens.radius.lg` | `DesignToken.shapes.large` | `Shape` |
| `tokens.colors.primary` | `MaterialTheme.colorScheme.primary` | `Color` |
| `tokens.colors.surface` | `MaterialTheme.colorScheme.surface` | `Color` |

---

## Gradient Support

Features with gradients (Google Stitch format):

| Feature | Gradient Type | Colors | Usage |
|---------|---------------|--------|-------|
| auth | primary | `#667EEA → #764BA2` | Buttons, headers |
| auth | secondary | `#11998E → #38EF7D` | Success states |

### Compose Gradient Code

```kotlin
// From design-tokens.json:
// "gradient": { "start": "#667EEA", "end": "#764BA2", "angle": 45 }

val AuthGradient = Brush.linearGradient(
    colors = listOf(
        Color(0xFF667EEA),
        Color(0xFF764BA2)
    ),
    start = Offset(0f, 0f),
    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
)

// Usage in Button:
Button(
    onClick = { },
    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
    modifier = Modifier.background(AuthGradient, shape = DesignToken.shapes.large)
) {
    Text("Login")
}
```

---

## Component Specs

Features with component specifications:

| Feature | Components | Details |
|---------|:----------:|---------|
| auth | 5 | primary-button, text-input, otp-input, auth-card, trust-badge |
| dashboard | 7 | NetWorthCard, QuickActions, AccountCard, TransactionItem, BottomNav, TopBar, SectionHeader |

### Component Spec Example

```json
{
  "id": "primary-button",
  "name": "Primary Button",
  "specs": {
    "height": "56dp",
    "radius": "16dp",
    "background": "gradient",
    "textSize": "16sp",
    "textWeight": "600",
    "textColor": "#FFFFFF",
    "shadow": "button"
  }
}
```

### Generated Compose Code

```kotlin
@Composable
fun AuthPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(56.dp)
            .background(
                brush = AuthGradient,
                shape = RoundedCornerShape(16.dp)
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        shape = RoundedCornerShape(16.dp),
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
        )
    }
}
```

---

## Animation Specs

Features with animation specifications:

| Feature | Animations | Details |
|---------|:----------:|---------|
| auth | 5 | pageTransition, buttonPress, inputFocus, successCelebration, errorShake |

### Animation Spec Example

```json
"animations": {
  "buttonPress": {
    "scale": "0.98",
    "duration": "100ms"
  },
  "errorShake": {
    "translateX": "[-10, 10, -5, 5, 0]",
    "duration": "300ms"
  }
}
```

---

## Usage in /feature Command

### Phase 0: O(1) Context Loading

```kotlin
// Check if design tokens exist
val tokensPath = "design-spec-layer/features/$feature/mockups/design-tokens.json"
val hasTokens = checkInIndex("DESIGN_TOKENS_INDEX.md", feature)
val tokenFormat = getTokenFormat(feature) // "google-stitch" | "md3" | null
```

### Phase 3: Apply Design Tokens

```
IF hasTokens THEN
  1. Read design-tokens.json
  2. Extract colors → Generate feature-specific colors if custom
  3. Extract gradients → Generate Brush definitions
  4. Extract component specs → Apply to generated components
  5. Extract animations → Add animation modifiers
  6. Map spacing/radius → Use DesignToken equivalents
ELSE
  Use default DesignToken values
END
```

---

## Auto-Update Rules

| Scenario | Action |
|----------|--------|
| New tokens generated | Add row to Quick Lookup table |
| `/design mockup` completes | Update Has Tokens column |
| Token format changes | Update Format column |
| Components added | Update Components column |

---

## Related Files

- [FEATURES_INDEX.md](./FEATURES_INDEX.md) - All features
- [MOCKUPS_INDEX.md](./MOCKUPS_INDEX.md) - Mockup status
- `core/designsystem/theme/DesignToken.kt` - Compose design tokens
- `core/designsystem/theme/Color.kt` - Color definitions

---

## Commands

```bash
# Check token status
/gap-analysis design tokens

# Generate tokens for feature
/design [feature] mockup

# Feature with token integration
/feature [feature]   # Auto-applies tokens if available
```
