# Passcode - UI Mockups v2.0

> **Design Pattern**: Secure Gateway Experience
> **Design Style**: 2025 Fintech (Biometric-First + Security Gamification)
> **Gradient Theme**: Primary Purple-Blue (#667EEA → #764BA2)
> **Version**: 2.0 - Major redesign with 2025 security patterns
> **Last Updated**: 2025-12-30

---

## Design Philosophy

The Passcode feature transforms from a simple PIN entry to a **Secure Gateway Experience** - a trust-building security layer that makes users feel protected while maintaining seamless access. Key innovations:

1. **Biometric-First** - Biometrics are primary, PIN is fallback
2. **Security Health Visualization** - Gamified security score
3. **Trust Indicators** - Visual feedback on authentication strength
4. **Adaptive Security** - Context-aware authentication levels
5. **Panic Mode** - Emergency protection feature

---

## Table of Contents

1. [Screen States](#screen-states)
2. [Component Breakdown](#component-breakdown)
3. [Interactions & Animations](#interactions--animations)
4. [Accessibility](#accessibility)
5. [Dark Mode Variant](#dark-mode-variant)

---

## Screen States

### 1. Secure Gateway - Biometric First (Default)

Modern apps prioritize biometric authentication. PIN is the fallback.

```
+----------------------------------------------------------+
|                                                          |
|  [Status Bar - System Time, Battery, Signal]             |
|                                                          |
+----------------------------------------------------------+
|                                                          |
|                                                          |
|                     ┌──────────────┐                     |
|                     │              │                     |
|                     │   [Avatar]   │   User Photo        |
|                     │     84dp     │   or Initials       |
|                     │              │   Gradient ring     |
|                     │   Trust      │   pulsing slowly    |
|                     │   Ring       │   #00D09C           |
|                     │              │                     |
|                     └──────────────┘                     |
|                                                          |
|                      Welcome back,                       |
|                        Maria                             |
|                      ───────────                         |
|                      24sp / Bold / #1F2937               |
|                                                          |
|                                                          |
|   ┌────────────────────────────────────────────────┐    |
|   │░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░│    |
|   │░░                                            ░░│    |
|   │░░     ┌────────────────────────────────┐     ░░│    |
|   │░░     │                                │     ░░│    |
|   │░░     │      [  Fingerprint Icon  ]    │     ░░│    |
|   │░░     │             64dp               │     ░░│    |
|   │░░     │                                │     ░░│    |
|   │░░     │       Touch to Unlock          │     ░░│    |
|   │░░     │                                │     ░░│    |
|   │░░     └────────────────────────────────┘     ░░│    |
|   │░░                                            ░░│    |
|   │░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░│    |
|   └────────────────────────────────────────────────┘    |
|                                                          |
|   Biometric Button:                                      |
|   Background: Gradient #667EEA → #764BA2                 |
|   Size: Full width - 32dp margin                         |
|   Height: 120dp                                          |
|   Corner: 24dp                                           |
|   Shadow: 0 8 32 #667EEA@30%                             |
|   Glow pulse animation                                   |
|                                                          |
|                                                          |
|                   Use Passcode Instead                   |
|                   ────────────────────                   |
|                   14sp / Medium / #667EEA                |
|                                                          |
|                                                          |
|   ┌────────────────────────────────────────────────┐    |
|   │                                                │    |
|   │  🛡  Security Score: 92/100   [Excellent]     │    |
|   │      ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓░░  Last login: 2h   │    |
|   │                                                │    |
|   └────────────────────────────────────────────────┘    |
|                                                          |
|   Security Chip:                                         |
|   Background: #00D09C@10%                                |
|   Border: 1dp #00D09C@30%                                |
|   Text: #00D09C                                          |
|   Corner: 12dp                                           |
|                                                          |
+----------------------------------------------------------+
```

**Visual Specifications:**

| Element | Light Mode | Dark Mode |
|---------|------------|-----------|
| Background | #F8F9FA | #0D1117 |
| Avatar Ring | Gradient #667EEA → #764BA2 | Same |
| Trust Ring | #00D09C (pulsing) | #00D09C |
| Name Text | #1F2937 | #F0F6FC |
| Biometric Card | Gradient #667EEA → #764BA2 | Same |
| Biometric Icon | #FFFFFF | #FFFFFF |
| Link Text | #667EEA | #A78BFA |
| Security Score BG | #00D09C@10% | #00D09C@15% |
| Security Score Text | #00D09C | #00D09C |

---

### 2. Face ID Variant (iOS)

```
+----------------------------------------------------------+
|                                                          |
|  [Status Bar]                                            |
|                                                          |
+----------------------------------------------------------+
|                                                          |
|                                                          |
|                     ┌──────────────┐                     |
|                     │              │                     |
|                     │   [Avatar]   │                     |
|                     │   + Trust    │                     |
|                     │     Ring     │                     |
|                     │              │                     |
|                     └──────────────┘                     |
|                                                          |
|                      Welcome back,                       |
|                        Maria                             |
|                                                          |
|                                                          |
|   ┌────────────────────────────────────────────────┐    |
|   │░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░│    |
|   │░░                                            ░░│    |
|   │░░     ┌────────────────────────────────┐     ░░│    |
|   │░░     │                                │     ░░│    |
|   │░░     │         ┌──────────┐          │     ░░│    |
|   │░░     │         │   Face   │          │     ░░│    |
|   │░░     │         │    ID    │          │     ░░│    |
|   │░░     │         │   Icon   │          │     ░░│    |
|   │░░     │         │   64dp   │          │     ░░│    |
|   │░░     │         └──────────┘          │     ░░│    |
|   │░░     │                                │     ░░│    |
|   │░░     │       Look to Unlock          │     ░░│    |
|   │░░     │                                │     ░░│    |
|   │░░     └────────────────────────────────┘     ░░│    |
|   │░░                                            ░░│    |
|   │░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░│    |
|   └────────────────────────────────────────────────┘    |
|                                                          |
|                                                          |
|                   Use Passcode Instead                   |
|                                                          |
|                                                          |
|   ┌────────────────────────────────────────────────┐    |
|   │  🛡  Security: Excellent   Streak: 47 days ✨  │    |
|   └────────────────────────────────────────────────┘    |
|                                                          |
+----------------------------------------------------------+
```

---

### 3. Enter Passcode (Fallback Mode)

PIN entry when biometric fails or user chooses passcode.

```
+----------------------------------------------------------+
|                                                          |
|  [Status Bar]                                            |
|                                                          |
+----------------------------------------------------------+
|                                                          |
|                                                          |
|                     ┌──────────────┐                     |
|                     │              │                     |
|                     │  [Security   │   Gradient BG       |
|                     │   Shield]    │   #667EEA→#764BA2   |
|                     │              │                     |
|                     │    + Key     │   Subtle glow       |
|                     │     Icon     │   animation         |
|                     │              │                     |
|                     │     80dp     │                     |
|                     │              │                     |
|                     └──────────────┘                     |
|                                                          |
|                                                          |
|                   Enter Passcode                         |
|                   ──────────────                         |
|                   24sp / Bold                            |
|                                                          |
|            Your 4-digit PIN to access your               |
|                       account                            |
|            ────────────────────────────────              |
|            14sp / Regular / Secondary                    |
|                                                          |
|                                                          |
|            ┌───┐   ┌───┐   ┌───┐   ┌───┐                |
|            │   │   │   │   │   │   │   │                |
|            │ ○ │   │ ○ │   │ ○ │   │ ○ │                |
|            │   │   │   │   │   │   │   │                |
|            └───┘   └───┘   └───┘   └───┘                |
|                                                          |
|            PIN Dots: 16dp diameter                       |
|            Gap: 20dp                                     |
|            Border: 2dp #667EEA                           |
|                                                          |
|                                                          |
|        ┌────────────────────────────────────┐           |
|        │  [Fingerprint]  Try Biometric      │           |
|        │                 Instead            │           |
|        └────────────────────────────────────┘           |
|                                                          |
|        Quick Biometric Chip:                             |
|        Background: #667EEA@10%                           |
|        Border: 1dp #667EEA@30%                           |
|        Height: 40dp                                      |
|        Corner: 20dp                                      |
|                                                          |
+----------------------------------------------------------+
|                                                          |
|      NUMERIC KEYPAD                                      |
|                                                          |
|   ┌────────────┐  ┌────────────┐  ┌────────────┐        |
|   │            │  │            │  │            │        |
|   │     1      │  │     2      │  │     3      │        |
|   │            │  │    ABC     │  │    DEF     │        |
|   └────────────┘  └────────────┘  └────────────┘        |
|                                                          |
|   ┌────────────┐  ┌────────────┐  ┌────────────┐        |
|   │            │  │            │  │            │        |
|   │     4      │  │     5      │  │     6      │        |
|   │    GHI     │  │    JKL     │  │    MNO     │        |
|   └────────────┘  └────────────┘  └────────────┘        |
|                                                          |
|   ┌────────────┐  ┌────────────┐  ┌────────────┐        |
|   │            │  │            │  │            │        |
|   │     7      │  │     8      │  │     9      │        |
|   │   PQRS     │  │    TUV     │  │   WXYZ     │        |
|   └────────────┘  └────────────┘  └────────────┘        |
|                                                          |
|   ┌────────────┐  ┌────────────┐  ┌────────────┐        |
|   │            │  │            │  │            │        |
|   │   [Bio]    │  │     0      │  │    [⌫]    │        |
|   │   Icon     │  │            │  │ Backspace  │        |
|   └────────────┘  └────────────┘  └────────────┘        |
|                                                          |
|   Key Size: 72dp x 72dp                                  |
|   Key BG: #F8F9FA                                        |
|   Key Pressed: #667EEA@15%                               |
|   Number: 28sp Bold                                      |
|   Letters: 10sp Medium #9CA3AF                           |
|                                                          |
+----------------------------------------------------------+
|                                                          |
|              Forgot Passcode?                            |
|              ────────────────                            |
|              14sp / #667EEA / Underline                  |
|                                                          |
+----------------------------------------------------------+
```

---

### 4. Passcode Entry - Partial Input

Shows progressive PIN entry with satisfying fill animations.

```
+----------------------------------------------------------+
|                                                          |
|  [Status Bar]                                            |
|                                                          |
+----------------------------------------------------------+
|                                                          |
|                     ┌──────────────┐                     |
|                     │  [Security   │                     |
|                     │   Shield]    │                     |
|                     └──────────────┘                     |
|                                                          |
|                   Enter Passcode                         |
|                                                          |
|                                                          |
|            ┌───┐   ┌───┐   ┌───┐   ┌───┐                |
|            │   │   │   │   │   │   │   │                |
|            │ ● │   │ ● │   │ ● │   │ ○ │                |
|            │///│   │///│   │///│   │   │                |
|            └───┘   └───┘   └───┘   └───┘                |
|                                                          |
|            FILLED   FILLED  FILLED  EMPTY                |
|            Scale    Scale   Scale   Waiting              |
|            Anim     Anim    Anim                         |
|                                                          |
|            [Each fill: Scale 0→1.2→1.0]                  |
|            [Haptic: Light impact per digit]              |
|            [Gradient fill: #667EEA → #764BA2]            |
|                                                          |
|                                                          |
|        ┌────────────────────────────────────┐           |
|        │  [Fingerprint]  Try Biometric      │           |
|        └────────────────────────────────────┘           |
|                                                          |
+----------------------------------------------------------+
|                    [Numeric Keypad]                      |
+----------------------------------------------------------+
```

**Dot Animation Sequence:**
```
Frame 1 (0ms):    ○  ○  ○  ○     ← All empty
Frame 2 (100ms):  ●  ○  ○  ○     ← 1st fills with bounce
Frame 3 (200ms):  ●  ●  ○  ○     ← 2nd fills with bounce
Frame 4 (300ms):  ●  ●  ●  ○     ← 3rd fills with bounce
Frame 5 (400ms):  ●  ●  ●  ●     ← 4th fills, triggers validation
Frame 6 (600ms):  [Success/Error animation]
```

---

### 5. Biometric Scanning State

System biometric prompt with custom branded overlay.

```
+----------------------------------------------------------+
|                                                          |
|  [Dimmed Background - 60% Black Overlay]                 |
|                                                          |
|                                                          |
|                                                          |
|   ╔════════════════════════════════════════════════════╗|
|   ║                                                    ║|
|   ║              ┌─────────────────────┐              ║|
|   ║              │                     │              ║|
|   ║              │    [Mifos Logo]     │              ║|
|   ║              │                     │              ║|
|   ║              └─────────────────────┘              ║|
|   ║                                                    ║|
|   ║           ┌───────────────────────────┐          ║|
|   ║           │                           │          ║|
|   ║           │     ╭─────────────────╮   │          ║|
|   ║           │     │                 │   │          ║|
|   ║           │     │  [Fingerprint]  │   │   Scan   ║|
|   ║           │     │      Icon       │   │   Ring   ║|
|   ║           │     │      64dp       │   │   Pulse  ║|
|   ║           │     │                 │   │   Anim   ║|
|   ║           │     │   SCANNING...   │   │          ║|
|   ║           │     │                 │   │          ║|
|   ║           │     ╰─────────────────╯   │          ║|
|   ║           │                           │          ║|
|   ║           └───────────────────────────┘          ║|
|   ║                                                    ║|
|   ║                 Sign in to                        ║|
|   ║               Mifos Mobile                        ║|
|   ║                                                    ║|
|   ║       Confirm fingerprint to continue             ║|
|   ║                                                    ║|
|   ║                                                    ║|
|   ║   ┌────────────────────────────────────────────┐ ║|
|   ║   │                                            │ ║|
|   ║   │              Use Passcode                  │ ║|
|   ║   │                                            │ ║|
|   ║   └────────────────────────────────────────────┘ ║|
|   ║                                                    ║|
|   ║   ┌────────────────────────────────────────────┐ ║|
|   ║   │                                            │ ║|
|   ║   │                 Cancel                     │ ║|
|   ║   │                                            │ ║|
|   ║   └────────────────────────────────────────────┘ ║|
|   ║                                                    ║|
|   ╚════════════════════════════════════════════════════╝|
|                                                          |
+----------------------------------------------------------+
```

---

### 6. Create Passcode - Onboarding (Step 1/2)

First-time passcode setup with security tips.

```
+----------------------------------------------------------+
|                                                          |
|  [← Back]                              Step 1 of 2       |
|                                                          |
+----------------------------------------------------------+
|                                                          |
|   ╔════════════════════════════════════════════════════╗|
|   ║                  PROGRESS BAR                       ║|
|   ║                                                     ║|
|   ║   [████████████████████░░░░░░░░░░░░░░░░░░░░░░░░]   ║|
|   ║           50% - Creating PIN                        ║|
|   ║                                                     ║|
|   ╚════════════════════════════════════════════════════╝|
|                                                          |
|                                                          |
|         ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░          |
|         ░                                    ░          |
|         ░     ┌──────────────────────┐      ░          |
|         ░     │░░░░░░░░░░░░░░░░░░░░░░│      ░          |
|         ░     │░░                  ░░│      ░          |
|         ░     │░░    [+  Key]     ░░│      ░   Create  |
|         ░     │░░     Icon        ░░│      ░   Mode    |
|         ░     │░░                  ░░│      ░   Icon   |
|         ░     │░░░░░░░░░░░░░░░░░░░░░░│      ░          |
|         ░     └──────────────────────┘      ░          |
|         ░                                    ░          |
|         ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░          |
|                                                          |
|                                                          |
|                  Create Your PIN                         |
|                  ───────────────                         |
|                  24sp / Bold                             |
|                                                          |
|          Choose a memorable 4-digit code to              |
|                protect your account                      |
|          ────────────────────────────────────            |
|          14sp / Regular / Secondary                      |
|                                                          |
|                                                          |
|            ┌───┐   ┌───┐   ┌───┐   ┌───┐                |
|            │   │   │   │   │   │   │   │                |
|            │ ○ │   │ ○ │   │ ○ │   │ ○ │                |
|            │   │   │   │   │   │   │   │                |
|            └───┘   └───┘   └───┘   └───┘                |
|                                                          |
|                                                          |
|   ┌────────────────────────────────────────────────┐    |
|   │                                                │    |
|   │  💡 Tip: Avoid obvious patterns like 1234     │    |
|   │                                                │    |
|   └────────────────────────────────────────────────┘    |
|                                                          |
|   Security Tip Card:                                     |
|   Background: #FFB800@10%                                |
|   Border: 1dp #FFB800@30%                                |
|   Text: #B8860B                                          |
|   Corner: 12dp                                           |
|                                                          |
+----------------------------------------------------------+
|                    [Numeric Keypad]                      |
+----------------------------------------------------------+
```

---

### 7. Confirm Passcode (Step 2/2)

```
+----------------------------------------------------------+
|                                                          |
|  [← Back]                              Step 2 of 2       |
|                                                          |
+----------------------------------------------------------+
|                                                          |
|   ╔════════════════════════════════════════════════════╗|
|   ║                  PROGRESS BAR                       ║|
|   ║                                                     ║|
|   ║   [████████████████████████████████████████░░░░░]   ║|
|   ║           90% - Confirming PIN                      ║|
|   ║                                                     ║|
|   ╚════════════════════════════════════════════════════╝|
|                                                          |
|                                                          |
|         ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░          |
|         ░                                    ░          |
|         ░     ┌──────────────────────┐      ░          |
|         ░     │░░░░░░░░░░░░░░░░░░░░░░│      ░          |
|         ░     │░░                  ░░│      ░          |
|         ░     │░░     [✓ Check]   ░░│      ░  Confirm  |
|         ░     │░░       Icon      ░░│      ░  Mode     |
|         ░     │░░                  ░░│      ░  Icon    |
|         ░     │░░░░░░░░░░░░░░░░░░░░░░│      ░          |
|         ░     └──────────────────────┘      ░          |
|         ░                                    ░          |
|         ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░          |
|                                                          |
|                                                          |
|                 Confirm Your PIN                         |
|                 ────────────────                         |
|                 24sp / Bold                              |
|                                                          |
|          Re-enter your 4-digit code to confirm           |
|          ─────────────────────────────────────           |
|          14sp / Regular / Secondary                      |
|                                                          |
|                                                          |
|            ┌───┐   ┌───┐   ┌───┐   ┌───┐                |
|            │   │   │   │   │   │   │   │                |
|            │ ● │   │ ● │   │ ○ │   │ ○ │                |
|            │///│   │///│   │   │   │   │                |
|            └───┘   └───┘   └───┘   └───┘                |
|                                                          |
|                                                          |
|   ┌────────────────────────────────────────────────┐    |
|   │                                                │    |
|   │  🔒 Your PIN will be stored securely           │    |
|   │                                                │    |
|   └────────────────────────────────────────────────┘    |
|                                                          |
+----------------------------------------------------------+
|                    [Numeric Keypad]                      |
+----------------------------------------------------------+
```

---

### 8. PIN Mismatch Error (Confirm Step)

```
+----------------------------------------------------------+
|                                                          |
|  [← Back]                              Step 2 of 2       |
|                                                          |
+----------------------------------------------------------+
|                                                          |
|   ╔════════════════════════════════════════════════════╗|
|   ║   [████████████████████████████████████░░░░░░░░░]   ║|
|   ║           75% - PINs don't match                    ║|
|   ╚════════════════════════════════════════════════════╝|
|                                                          |
|                                                          |
|         ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░          |
|         ░                                    ░          |
|         ░     ┌──────────────────────┐      ░          |
|         ░     │XXXXXXXXXXXXXXXXXXXXXX│      ░          |
|         ░     │XX                  XX│      ░  Error   |
|         ░     │XX     [✗ X]       XX│      ░  State   |
|         ░     │XX       Icon      XX│      ░          |
|         ░     │XX                  XX│      ░  Red     |
|         ░     │XXXXXXXXXXXXXXXXXXXXXX│      ░  Tint    |
|         ░     └──────────────────────┘      ░          |
|         ░                                    ░          |
|         ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░          |
|                                                          |
|                                                          |
|                  PINs Don't Match                        |
|                  ────────────────                        |
|                  24sp / Bold / #FF4757                   |
|                                                          |
|          Please re-enter your PIN to confirm             |
|          ────────────────────────────────────            |
|          14sp / Regular / Secondary                      |
|                                                          |
|                                                          |
|       << SHAKE ANIMATION - Dots shake left/right >>      |
|                                                          |
|            ┌───┐   ┌───┐   ┌───┐   ┌───┐                |
|            │XXX│   │XXX│   │XXX│   │XXX│                |
|            │ ✗ │   │ ✗ │   │ ✗ │   │ ✗ │                |
|            │XXX│   │XXX│   │XXX│   │XXX│                |
|            └───┘   └───┘   └───┘   └───┘                |
|                                                          |
|            Error State:                                  |
|            Border: 2dp #FF4757                           |
|            Fill: #FF4757@30%                             |
|            Shake: translateX(-8, 8, -8, 8, 0)           |
|            Duration: 400ms                               |
|            Haptic: Error                                 |
|                                                          |
|                                                          |
|   ┌────────────────────────────────────────────────┐    |
|   │                                                │    |
|   │  ⚠️ Cleared - Please try again                  │    |
|   │                                                │    |
|   └────────────────────────────────────────────────┘    |
|                                                          |
+----------------------------------------------------------+
|                    [Numeric Keypad]                      |
+----------------------------------------------------------+
```

---

### 9. Wrong Passcode Entry (Unlock Screen)

```
+----------------------------------------------------------+
|                                                          |
|  [Status Bar]                                            |
|                                                          |
+----------------------------------------------------------+
|                                                          |
|                     ┌──────────────┐                     |
|                     │  [Security   │   Red pulse         |
|                     │   Shield]    │   overlay           |
|                     │   WARNING    │   #FF4757@20%       |
|                     └──────────────┘                     |
|                                                          |
|                                                          |
|                   Wrong Passcode                         |
|                   ──────────────                         |
|                   24sp / Bold / #FF4757                  |
|                                                          |
|                                                          |
|       << SHAKE ANIMATION - 3 cycles, 100ms each >>       |
|                                                          |
|            ┌───┐   ┌───┐   ┌───┐   ┌───┐                |
|            │XXX│   │XXX│   │XXX│   │XXX│                |
|            │ ✗ │   │ ✗ │   │ ✗ │   │ ✗ │   Error       |
|            │XXX│   │XXX│   │XXX│   │XXX│   dots        |
|            └───┘   └───┘   └───┘   └───┘                |
|                                                          |
|                                                          |
|   ╔════════════════════════════════════════════════════╗|
|   ║                                                    ║|
|   ║   ⚠️  Incorrect passcode                           ║|
|   ║                                                    ║|
|   ║   3 attempts remaining                             ║|
|   ║   ───────────────────────────                      ║|
|   ║   [●●●○○]  Attempts indicator                      ║|
|   ║                                                    ║|
|   ╚════════════════════════════════════════════════════╝|
|                                                          |
|   Error Banner:                                          |
|   Background: #FF4757@10%                                |
|   Border: 1dp #FF4757@30%                                |
|   Icon: Warning #FF4757                                  |
|   Text: #FF4757                                          |
|   Corner: 12dp                                           |
|                                                          |
|                                                          |
|        ┌────────────────────────────────────┐           |
|        │  [Fingerprint]  Try Biometric      │           |
|        └────────────────────────────────────┘           |
|                                                          |
|                                                          |
|              Forgot Passcode?                            |
|                                                          |
+----------------------------------------------------------+
|                    [Numeric Keypad]                      |
+----------------------------------------------------------+
```

**Attempts Warning Levels:**
```
5 attempts: No warning
4 attempts: Yellow warning chip appears
3 attempts: Orange warning, "Be careful"
2 attempts: Red warning, "Last 2 attempts"
1 attempt:  Red alert, "Final attempt before lockout"
0 attempts: Account locked screen
```

---

### 10. Account Locked

Lockout screen with countdown and recovery options.

```
+----------------------------------------------------------+
|                                                          |
|  [Status Bar]                                            |
|                                                          |
+----------------------------------------------------------+
|                                                          |
|                                                          |
|                                                          |
|         ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░          |
|         ░                                    ░          |
|         ░     ┌──────────────────────┐      ░          |
|         ░     │XXXXXXXXXXXXXXXXXXXXXX│      ░          |
|         ░     │XX                  XX│      ░          |
|         ░     │XX    [LOCKED]     XX│      ░   Red     |
|         ░     │XX     ICON        XX│      ░   Pulse   |
|         ░     │XX    80x80dp      XX│      ░   Anim    |
|         ░     │XX                  XX│      ░          |
|         ░     │XXXXXXXXXXXXXXXXXXXXXX│      ░          |
|         ░     └──────────────────────┘      ░          |
|         ░                                    ░          |
|         ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░          |
|                                                          |
|                                                          |
|                   Account Locked                         |
|                   ──────────────                         |
|                   28sp / Bold / #FF4757                  |
|                                                          |
|                                                          |
|       Too many incorrect attempts. Your account          |
|         has been temporarily locked for safety.          |
|       ───────────────────────────────────────            |
|       14sp / Regular / #6B7280                           |
|                                                          |
|                                                          |
|         ╔════════════════════════════════════╗           |
|         ║                                    ║           |
|         ║     ┌────────────────────────┐    ║           |
|         ║     │                        │    ║  Circular |
|         ║     │        4:59            │    ║  Progress |
|         ║     │                        │    ║  Ring     |
|         ║     │     [Progress Ring]    │    ║           |
|         ║     │                        │    ║  Gradient |
|         ║     │     Try again in       │    ║  Stroke   |
|         ║     │                        │    ║           |
|         ║     └────────────────────────┘    ║           |
|         ║                                    ║           |
|         ╚════════════════════════════════════╝           |
|                                                          |
|         Countdown Card:                                  |
|         Background: #1F2937                              |
|         Timer: 36sp / Bold / Gradient text               |
|         Circular progress: #667EEA → #764BA2             |
|         Corner: 20dp                                     |
|                                                          |
|                                                          |
|   ┌────────────────────────────────────────────────┐    |
|   │░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░│    |
|   │░░                                            ░░│    |
|   │░░              Reset via Email               ░░│    |
|   │░░                                            ░░│    |
|   │░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░│    |
|   └────────────────────────────────────────────────┘    |
|                                                          |
|   Outline Button:                                        |
|   Border: 2dp #667EEA                                    |
|   Text: #667EEA                                          |
|   Height: 52dp                                           |
|   Corner: 26dp (pill)                                    |
|                                                          |
|                                                          |
|                   Contact Support                        |
|                   ───────────────                        |
|                   14sp / #6B7280                         |
|                                                          |
+----------------------------------------------------------+
```

**Lockout Escalation:**
| Failed Attempts | Lockout Duration |
|-----------------|------------------|
| 5 | 30 seconds |
| 7 | 5 minutes |
| 10 | 30 minutes |
| 15 | 24 hours + email reset required |

---

### 11. Passcode Setup Success

Celebration screen with security score reveal.

```
+----------------------------------------------------------+
|                                                          |
|  [Status Bar]                                            |
|                                                          |
+----------------------------------------------------------+
|                                                          |
|                                                          |
|                                                          |
|         ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░          |
|         ░                                    ░          |
|         ░     ┌──────────────────────┐      ░          |
|         ░     │░░░░░░░░░░░░░░░░░░░░░░│      ░  Success |
|         ░     │░░                  ░░│      ░  Green   |
|         ░     │░░    [ ✓ CHECK ]  ░░│      ░  Gradient|
|         ░     │░░      ICON       ░░│      ░          |
|         ░     │░░     80x80       ░░│      ░  #00D09C |
|         ░     │░░                  ░░│      ░  →       |
|         ░     │░░  Draw Animation ░░│      ░  #38EF7D |
|         ░     │░░                  ░░│      ░          |
|         ░     │░░░░░░░░░░░░░░░░░░░░░░│      ░          |
|         ░     └──────────────────────┘      ░          |
|         ░                                    ░          |
|         ░   [CONFETTI PARTICLES BURST]       ░          |
|         ░                                    ░          |
|         ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░          |
|                                                          |
|                                                          |
|                 Passcode Created! 🎉                     |
|                 ─────────────────────                    |
|                 28sp / Bold / #00D09C                    |
|                                                          |
|                                                          |
|          Your account is now protected with              |
|            secure passcode authentication                |
|          ─────────────────────────────────               |
|          14sp / Regular / #6B7280                        |
|                                                          |
|                                                          |
|   ╔════════════════════════════════════════════════════╗|
|   ║                                                    ║|
|   ║       🛡️  Security Score Unlocked!                 ║|
|   ║                                                    ║|
|   ║              ┌────────────────┐                   ║|
|   ║              │                │                   ║  Score
|   ║              │       75       │                   ║  Reveal
|   ║              │      /100      │                   ║  Anim
|   ║              │                │                   ║|
|   ║              │   [GOOD]       │                   ║|
|   ║              │                │                   ║|
|   ║              └────────────────┘                   ║|
|   ║                                                    ║|
|   ║   +25 pts for PIN  •  Enable biometrics for +25   ║|
|   ║                                                    ║|
|   ╚════════════════════════════════════════════════════╝|
|                                                          |
|                                                          |
|   ┌────────────────────────────────────────────────┐    |
|   │░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░│    |
|   │░░                                            ░░│    |
|   │░░              Continue                      ░░│    |
|   │░░                                            ░░│    |
|   │░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░│    |
|   └────────────────────────────────────────────────┘    |
|                                                          |
|   Primary Button:                                        |
|   Background: Gradient #667EEA → #764BA2                 |
|   Text: White 16sp Bold                                  |
|   Height: 56dp                                           |
|   Corner: 28dp (pill)                                    |
|   Shadow: 0 8 24 #667EEA@30%                             |
|                                                          |
|                                                          |
|              Enable Face ID / Touch ID                   |
|              ─────────────────────────                   |
|              14sp / #667EEA                              |
|                                                          |
+----------------------------------------------------------+
```

---

### 12. Change Passcode Flow

3-step flow for changing existing passcode.

```
+----------------------------------------------------------+
|                                                          |
|  [← Back]              Change Passcode                   |
|                                                          |
+----------------------------------------------------------+
|                                                          |
|   ╔════════════════════════════════════════════════════╗|
|   ║                  STEP INDICATOR                     ║|
|   ║                                                     ║|
|   ║    [ ● ]━━━━━━[ ○ ]━━━━━━[ ○ ]                     ║|
|   ║   Current      New       Confirm                    ║|
|   ║   (Active)                                          ║|
|   ║                                                     ║|
|   ╚════════════════════════════════════════════════════╝|
|                                                          |
|                                                          |
|                     ┌──────────────┐                     |
|                     │  [Security   │                     |
|                     │   Shield]    │                     |
|                     │   + Key      │                     |
|                     └──────────────┘                     |
|                                                          |
|                                                          |
|              Enter Current Passcode                      |
|              ──────────────────────                      |
|              24sp / Bold                                 |
|                                                          |
|         Verify your identity before changing             |
|         ─────────────────────────────────────            |
|         14sp / Regular / Secondary                       |
|                                                          |
|                                                          |
|            ┌───┐   ┌───┐   ┌───┐   ┌───┐                |
|            │   │   │   │   │   │   │   │                |
|            │ ○ │   │ ○ │   │ ○ │   │ ○ │                |
|            │   │   │   │   │   │   │   │                |
|            └───┘   └───┘   └───┘   └───┘                |
|                                                          |
|                                                          |
|   ┌────────────────────────────────────────────────┐    |
|   │                                                │    |
|   │  🔒 Last changed: 47 days ago                  │    |
|   │                                                │    |
|   └────────────────────────────────────────────────┘    |
|                                                          |
+----------------------------------------------------------+
|                    [Numeric Keypad]                      |
+----------------------------------------------------------+
```

**Step 2 - Enter New:**
```
   ║    [ ✓ ]━━━━━━[ ● ]━━━━━━[ ○ ]                     ║
   ║   Current      New       Confirm                    ║
   ║   (Done)     (Active)                               ║

            Enter New Passcode
      Choose a new 4-digit code
```

**Step 3 - Confirm New:**
```
   ║    [ ✓ ]━━━━━━[ ✓ ]━━━━━━[ ● ]                     ║
   ║   Current      New       Confirm                    ║
   ║   (Done)      (Done)    (Active)                    ║

           Confirm New Passcode
      Re-enter to confirm your new PIN
```

---

### 13. Panic Mode (2025 Security Feature)

Quick access emergency feature - 5 rapid taps on shield.

```
+----------------------------------------------------------+
|                                                          |
|  [Status Bar - RED TINT]                    🆘 PANIC     |
|                                                          |
+----------------------------------------------------------+
|                                                          |
|                                                          |
|         ╔════════════════════════════════════╗           |
|         ║                                    ║           |
|         ║    ⚠️  PANIC MODE ACTIVATED        ║           |
|         ║                                    ║           |
|         ╚════════════════════════════════════╝           |
|                                                          |
|                                                          |
|                     ┌──────────────┐                     |
|                     │XXXXXXXXXXXXXX│   Emergency         |
|                     │XX          XX│   Shield            |
|                     │XX  [ SOS ] XX│   Red gradient      |
|                     │XX          XX│   #FF4757→#C0392B   |
|                     │XXXXXXXXXXXXXX│                     |
|                     └──────────────┘                     |
|                                                          |
|                                                          |
|   ┌────────────────────────────────────────────────┐    |
|   │                                                │    |
|   │   🚨 Emergency Actions                         │    |
|   │   ────────────────────────────────────────    │    |
|   │                                                │    |
|   │   ┌──────────────────────────────────────┐   │    |
|   │   │  🔒  Lock All Accounts               │   │    |
|   │   │      Freeze transactions instantly   │   │    |
|   │   └──────────────────────────────────────┘   │    |
|   │                                                │    |
|   │   ┌──────────────────────────────────────┐   │    |
|   │   │  📧  Alert Emergency Contact         │   │    |
|   │   │      Notify trusted person           │   │    |
|   │   └──────────────────────────────────────┘   │    |
|   │                                                │    |
|   │   ┌──────────────────────────────────────┐   │    |
|   │   │  📞  Call Support Hotline            │   │    |
|   │   │      Connect to 24/7 support         │   │    |
|   │   └──────────────────────────────────────┘   │    |
|   │                                                │    |
|   │   ┌──────────────────────────────────────┐   │    |
|   │   │  📍  Share Location                  │   │    |
|   │   │      Send to emergency contact       │   │    |
|   │   └──────────────────────────────────────┘   │    |
|   │                                                │    |
|   └────────────────────────────────────────────────┘    |
|                                                          |
|                                                          |
|                    Cancel Panic Mode                     |
|                    ────────────────                      |
|                    14sp / #FFFFFF                        |
|                                                          |
+----------------------------------------------------------+
```

---

## Component Breakdown

### Biometric Gateway Card

```
┌─────────────────────────────────────────────────────────────┐
│  BIOMETRIC GATEWAY CARD                                      │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  DEFAULT STATE                                               │
│  ═══════════════════════════════════════════════════════    │
│  ┌───────────────────────────────────────────────────────┐  │
│  │                                                       │  │
│  │   ┌───────────────────────────────────────────────┐  │  │
│  │   │░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░│  │  │
│  │   │░░                                          ░░│  │  │
│  │   │░░         [ Fingerprint Icon ]             ░░│  │  │
│  │   │░░              64 x 64dp                   ░░│  │  │
│  │   │░░                                          ░░│  │  │
│  │   │░░           Touch to Unlock                ░░│  │  │
│  │   │░░              16sp / White                ░░│  │  │
│  │   │░░                                          ░░│  │  │
│  │   │░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░│  │  │
│  │   └───────────────────────────────────────────────┘  │  │
│  │                                                       │  │
│  │   Dimensions: Full width - 32dp margin               │  │
│  │   Height: 120dp                                       │  │
│  │   Background: Gradient #667EEA → #764BA2              │  │
│  │   Corner Radius: 24dp                                 │  │
│  │   Shadow: 0 8 32 #667EEA@30%                          │  │
│  │   Glow Animation: Subtle pulse 3s infinite            │  │
│  │                                                       │  │
│  └───────────────────────────────────────────────────────┘  │
│                                                              │
│  PRESSED STATE                                               │
│  ┌───────────────────────────────────────────────────────┐  │
│  │   Scale: 0.98                                         │  │
│  │   Brightness: +5%                                     │  │
│  │   Haptic: Light impact                                │  │
│  └───────────────────────────────────────────────────────┘  │
│                                                              │
│  SCANNING STATE                                              │
│  ┌───────────────────────────────────────────────────────┐  │
│  │   Fingerprint ring: Rotating gradient stroke          │  │
│  │   Label: "Scanning..." with fade animation            │  │
│  │   Haptic: Continuous light vibration                  │  │
│  └───────────────────────────────────────────────────────┘  │
│                                                              │
│  Props:                                                      │
│  ─────────────────────────────────────────────────────────  │
│  • biometricType: BiometricType (Fingerprint | FaceID)       │
│  • isScanning: Boolean                                       │
│  • onClick: () -> Unit                                       │
│  • onScanComplete: (success: Boolean) -> Unit                │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

### PIN Dot Indicator (Enhanced)

```
┌─────────────────────────────────────────────────────────────┐
│  PIN DOT INDICATOR COMPONENT                                 │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  EMPTY STATE                                                 │
│  ┌───────────────────────────────────────────────┐          │
│  │         ┌─────┐                               │          │
│  │         │     │   Border: 2dp #667EEA         │          │
│  │         │  ○  │   Fill: Transparent           │          │
│  │         │     │   Size: 18dp x 18dp           │          │
│  │         └─────┘   Corner: 50% (Circle)        │          │
│  │                   Glow: None                  │          │
│  └───────────────────────────────────────────────┘          │
│                                                              │
│  FILLED STATE                                                │
│  ┌───────────────────────────────────────────────┐          │
│  │         ┌─────┐                               │          │
│  │         │░░░░░│   Border: None                │          │
│  │         │░ ● ░│   Fill: Gradient #667EEA →    │          │
│  │         │░░░░░│         #764BA2               │          │
│  │         └─────┘   Size: 18dp (scales to 22dp) │          │
│  │                   Animation: Spring bounce     │          │
│  │                   Glow: #667EEA@40%           │          │
│  └───────────────────────────────────────────────┘          │
│                                                              │
│  ERROR STATE                                                 │
│  ┌───────────────────────────────────────────────┐          │
│  │         ┌─────┐                               │          │
│  │         │XXXXX│   Border: 2dp #FF4757         │          │
│  │         │X ✗ X│   Fill: #FF4757@30%           │          │
│  │         │XXXXX│   Animation: Shake X ±8dp     │          │
│  │         └─────┘   Haptic: Error               │          │
│  └───────────────────────────────────────────────┘          │
│                                                              │
│  VISIBLE STATE (Show PIN toggle)                             │
│  ┌───────────────────────────────────────────────┐          │
│  │         ┌─────┐                               │          │
│  │         │     │   Shows actual digit          │          │
│  │         │  5  │   Font: 16sp Bold #667EEA     │          │
│  │         │     │   Background: #667EEA@10%     │          │
│  │         └─────┘                               │          │
│  └───────────────────────────────────────────────┘          │
│                                                              │
│  Composable:                                                 │
│  ─────────────────────────────────────────────────────────  │
│  @Composable                                                 │
│  fun PinDotIndicator(                                        │
│      modifier: Modifier = Modifier,                          │
│      length: Int = 4,                                        │
│      filledCount: Int = 0,                                   │
│      isError: Boolean = false,                               │
│      isVisible: Boolean = false,                             │
│      visibleDigits: List<Char> = emptyList()                 │
│  )                                                           │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

### Numeric Keypad (Premium)

```
┌─────────────────────────────────────────────────────────────┐
│  NUMERIC KEYPAD COMPONENT                                    │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  Layout: 4x3 Grid                                            │
│  Gap: 16dp horizontal, 12dp vertical                         │
│  Padding: 24dp horizontal, 16dp vertical                     │
│                                                              │
│  DIGIT KEY                                                   │
│  ┌───────────────────────────────────────────────┐          │
│  │      ┌─────────────────────────┐             │          │
│  │      │                         │             │          │
│  │      │           2             │  Number:    │          │
│  │      │                         │  28sp Bold  │          │
│  │      │          ABC            │  #1F2937    │          │
│  │      │                         │             │          │
│  │      │   Size: 76dp x 76dp     │  Letters:   │          │
│  │      │   Corner: 50% (Circle)  │  10sp Med   │          │
│  │      │   BG: #F0F4F8           │  #9CA3AF    │          │
│  │      │                         │             │          │
│  │      └─────────────────────────┘             │          │
│  └───────────────────────────────────────────────┘          │
│                                                              │
│  PRESSED STATE                                               │
│  ┌───────────────────────────────────────────────┐          │
│  │      ┌─────────────────────────┐             │          │
│  │      │░░░░░░░░░░░░░░░░░░░░░░░░░│   BG:       │          │
│  │      │░░░░░░░░░░░░░░░░░░░░░░░░░│   #667EEA   │          │
│  │      │░░░░░░░░ 2 ░░░░░░░░░░░░░│   @20%      │          │
│  │      │░░░░░░░░░░░░░░░░░░░░░░░░░│             │          │
│  │      │░░░░░░░ ABC ░░░░░░░░░░░░│   Scale:    │          │
│  │      │░░░░░░░░░░░░░░░░░░░░░░░░░│   0.92      │          │
│  │      └─────────────────────────┘             │          │
│  │                                               │          │
│  │      Duration: 80ms                           │          │
│  │      Haptic: Light impact                     │          │
│  └───────────────────────────────────────────────┘          │
│                                                              │
│  BIOMETRIC KEY (Bottom-Left)                                 │
│  ┌───────────────────────────────────────────────┐          │
│  │      ┌─────────────────────────┐             │          │
│  │      │                         │   Icon:     │          │
│  │      │    [Fingerprint] or     │   28dp      │          │
│  │      │    [Face ID Icon]       │   #667EEA   │          │
│  │      │                         │             │          │
│  │      │   76dp x 76dp           │   BG:       │          │
│  │      │   BG: #667EEA@10%       │   Gradient  │          │
│  │      │   Border: 1dp #667EEA@30%│   @10%     │          │
│  │      └─────────────────────────┘             │          │
│  └───────────────────────────────────────────────┘          │
│                                                              │
│  BACKSPACE KEY (Bottom-Right)                                │
│  ┌───────────────────────────────────────────────┐          │
│  │      ┌─────────────────────────┐             │          │
│  │      │                         │   Icon:     │          │
│  │      │       [⌫ Delete]        │   24dp      │          │
│  │      │                         │   #6B7280   │          │
│  │      │   76dp x 76dp           │             │          │
│  │      │   BG: Transparent       │   Long      │          │
│  │      │                         │   Press:    │          │
│  │      │                         │   Clear all │          │
│  │      └─────────────────────────┘             │          │
│  └───────────────────────────────────────────────┘          │
│                                                              │
│  Composable:                                                 │
│  ─────────────────────────────────────────────────────────  │
│  @Composable                                                 │
│  fun NumericKeypad(                                          │
│      modifier: Modifier = Modifier,                          │
│      onDigitClick: (Char) -> Unit,                           │
│      onBackspaceClick: () -> Unit,                           │
│      onBackspaceLongClick: () -> Unit,                       │
│      onBiometricClick: () -> Unit,                           │
│      biometricType: BiometricType?,                          │
│      isBiometricEnabled: Boolean = true                      │
│  )                                                           │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

### Security Score Chip

```
┌─────────────────────────────────────────────────────────────┐
│  SECURITY SCORE CHIP                                         │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  EXCELLENT (90-100)                                          │
│  ┌───────────────────────────────────────────────────────┐  │
│  │  🛡  Security: 92/100   [Excellent]   Streak: 47d ✨  │  │
│  │                                                       │  │
│  │  BG: #00D09C@10%   Border: 1dp #00D09C@30%           │  │
│  │  Icon: Shield #00D09C   Text: #00D09C                │  │
│  │  Height: 44dp   Corner: 22dp (pill)                  │  │
│  │  Padding: 12dp horizontal                             │  │
│  └───────────────────────────────────────────────────────┘  │
│                                                              │
│  GOOD (70-89)                                                │
│  ┌───────────────────────────────────────────────────────┐  │
│  │  🛡  Security: 75/100   [Good]                        │  │
│  │                                                       │  │
│  │  BG: #667EEA@10%   Border: 1dp #667EEA@30%           │  │
│  │  Icon: Shield #667EEA   Text: #667EEA                │  │
│  └───────────────────────────────────────────────────────┘  │
│                                                              │
│  NEEDS WORK (50-69)                                          │
│  ┌───────────────────────────────────────────────────────┐  │
│  │  ⚠️  Security: 55/100   [Needs Work]   Improve →      │  │
│  │                                                       │  │
│  │  BG: #FFB800@10%   Border: 1dp #FFB800@30%           │  │
│  │  Icon: Warning #FFB800   Text: #B8860B               │  │
│  └───────────────────────────────────────────────────────┘  │
│                                                              │
│  AT RISK (0-49)                                              │
│  ┌───────────────────────────────────────────────────────┐  │
│  │  🔓  Security: 35/100   [At Risk]   Fix Now →         │  │
│  │                                                       │  │
│  │  BG: #FF4757@10%   Border: 1dp #FF4757@30%           │  │
│  │  Icon: Unlock #FF4757   Text: #FF4757                │  │
│  └───────────────────────────────────────────────────────┘  │
│                                                              │
│  Score Calculation:                                          │
│  ─────────────────────────────────────────────────────────  │
│  • PIN set: +25 points                                       │
│  • Biometric enabled: +25 points                             │
│  • Email verified: +15 points                                │
│  • Phone verified: +15 points                                │
│  • No failed attempts (7 days): +10 points                   │
│  • Account age bonus: +10 points                             │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

---

## Interactions & Animations

### Animation Specifications

```
┌─────────────────────────────────────────────────────────────┐
│  PASSCODE ANIMATIONS (Premium 2025)                          │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  1. BIOMETRIC CARD GLOW                                      │
│  ═══════════════════════════════════════════════════════    │
│  Type: Continuous ambient animation                          │
│  Duration: 3000ms per cycle                                  │
│  Easing: ease-in-out                                         │
│                                                              │
│  Keyframes:                                                  │
│  ┌────────────────────────────────────────────────────┐     │
│  │  0%    { box-shadow: 0 8 32 #667EEA@30% }         │     │
│  │  50%   { box-shadow: 0 12 48 #764BA2@40% }        │     │
│  │  100%  { box-shadow: 0 8 32 #667EEA@30% }         │     │
│  └────────────────────────────────────────────────────┘     │
│                                                              │
│                                                              │
│  2. DOT FILL ANIMATION (Spring Bounce)                       │
│  ═══════════════════════════════════════════════════════    │
│  Trigger: User taps digit                                    │
│  Duration: 200ms                                             │
│  Easing: Spring (damping=0.6, stiffness=500)                 │
│                                                              │
│  Sequence:                                                   │
│  ┌────────────────────────────────────────────────────┐     │
│  │                                                    │     │
│  │   T=0ms    T=80ms     T=150ms    T=200ms          │     │
│  │                                                    │     │
│  │   ┌───┐    ┌───┐      ┌────┐     ┌───┐           │     │
│  │   │   │    │ ● │      │ ●● │     │ ● │           │     │
│  │   │ ○ │ →  │///│  →   │////│  →  │///│           │     │
│  │   │   │    │ ● │      │ ●● │     │ ● │           │     │
│  │   └───┘    └───┘      └────┘     └───┘           │     │
│  │   Empty    Fill       Overshoot  Settle          │     │
│  │   18dp     18dp       22dp       18dp            │     │
│  │                                                    │     │
│  └────────────────────────────────────────────────────┘     │
│                                                              │
│  Additional Effects:                                         │
│  • Gradient fill animates from bottom to top                 │
│  • Glow appears with 100ms fade-in                           │
│  • Haptic: Light impact (10ms)                               │
│                                                              │
│                                                              │
│  3. SHAKE ERROR ANIMATION                                    │
│  ═══════════════════════════════════════════════════════    │
│  Trigger: Wrong passcode / PIN mismatch                      │
│  Duration: 400ms                                             │
│  Easing: cubic-bezier(0.36, 0.07, 0.19, 0.97)               │
│                                                              │
│  Keyframes:                                                  │
│  ┌────────────────────────────────────────────────────┐     │
│  │  0%     { transform: translateX(0) }              │     │
│  │  10%    { transform: translateX(-8dp) }           │     │
│  │  20%    { transform: translateX(8dp) }            │     │
│  │  30%    { transform: translateX(-8dp) }           │     │
│  │  40%    { transform: translateX(8dp) }            │     │
│  │  50%    { transform: translateX(-4dp) }           │     │
│  │  60%    { transform: translateX(4dp) }            │     │
│  │  70%    { transform: translateX(-2dp) }           │     │
│  │  80%    { transform: translateX(2dp) }            │     │
│  │  100%   { transform: translateX(0) }              │     │
│  └────────────────────────────────────────────────────┘     │
│                                                              │
│  Concurrent animations:                                      │
│  • Dots turn red: 0ms                                        │
│  • Error glow: 0ms-400ms                                     │
│  • Haptic: Heavy error (150ms duration)                      │
│                                                              │
│                                                              │
│  4. SUCCESS UNLOCK ANIMATION                                 │
│  ═══════════════════════════════════════════════════════    │
│  Trigger: Correct passcode entered                           │
│  Duration: 600ms                                             │
│                                                              │
│  Sequence:                                                   │
│  ┌────────────────────────────────────────────────────┐     │
│  │                                                    │     │
│  │   T=0ms:      All 4 dots filled                   │     │
│  │   T=50ms:     Dots merge to center                │     │
│  │   T=200ms:    Green pulse radiates outward        │     │
│  │   T=400ms:    Shield icon transforms to checkmark │     │
│  │   T=600ms:    Screen fades to home                │     │
│  │                                                    │     │
│  │   [●]  [●]  [●]  [●]                             │     │
│  │      \   |   /                                    │     │
│  │       [●●●●]  → [ ✓ ] → [Navigate]               │     │
│  │                                                    │     │
│  └────────────────────────────────────────────────────┘     │
│                                                              │
│  Haptic: Success pattern (100ms-50ms-100ms)                  │
│                                                              │
│                                                              │
│  5. KEY PRESS RIPPLE                                         │
│  ═══════════════════════════════════════════════════════    │
│  Trigger: Touch down on key                                  │
│  Duration: 150ms                                             │
│                                                              │
│  ┌────────────────────────────────────────────────────┐     │
│  │                                                    │     │
│  │   IDLE           PRESSED          RELEASED        │     │
│  │                                                    │     │
│  │   ┌─────┐        ┌─────┐          ┌─────┐        │     │
│  │   │     │        │░░░░░│          │     │        │     │
│  │   │  2  │   →    │░ 2 ░│    →     │  2  │        │     │
│  │   │ ABC │        │░ABC░│          │ ABC │        │     │
│  │   └─────┘        └─────┘          └─────┘        │     │
│  │                                                    │     │
│  │   Scale: 1.0     Scale: 0.92      Scale: 1.0     │     │
│  │   BG: #F0F4F8    BG: #667EEA@20%  BG: #F0F4F8    │     │
│  │                                                    │     │
│  │   Ripple origin: Touch point                      │     │
│  │   Ripple color: #667EEA@30%                       │     │
│  │                                                    │     │
│  └────────────────────────────────────────────────────┘     │
│                                                              │
│                                                              │
│  6. BIOMETRIC SCANNING PULSE                                 │
│  ═══════════════════════════════════════════════════════    │
│  Trigger: Biometric prompt active                            │
│  Duration: Continuous                                        │
│                                                              │
│  ┌────────────────────────────────────────────────────┐     │
│  │                                                    │     │
│  │         ┌───────────────────┐                     │     │
│  │         │   ╭─────────────╮ │   Ring 1: Scale     │     │
│  │         │  /               \│   1.0 → 1.3 → 1.0   │     │
│  │         │ │  [Fingerprint]  │   Opacity: 1→0→1    │     │
│  │         │  \               /│   Duration: 1500ms  │     │
│  │         │   ╰─────────────╯ │                     │     │
│  │         │ ╭─────────────────╮│   Ring 2: Offset   │     │
│  │         │ ╰─────────────────╯│   by 750ms         │     │
│  │         └───────────────────┘                     │     │
│  │                                                    │     │
│  └────────────────────────────────────────────────────┘     │
│                                                              │
│                                                              │
│  7. LOCKOUT COUNTDOWN                                        │
│  ═══════════════════════════════════════════════════════    │
│  Trigger: Account locked                                     │
│  Duration: Per second tick                                   │
│                                                              │
│  ┌────────────────────────────────────────────────────┐     │
│  │                                                    │     │
│  │   Circular progress ring decreases each second    │     │
│  │   Stroke: Gradient #667EEA → #764BA2              │     │
│  │   Width: 6dp                                       │     │
│  │   Size: 120dp diameter                             │     │
│  │                                                    │     │
│  │   Number transition:                               │     │
│  │   • Old number: translateY -20dp, opacity 0       │     │
│  │   • New number: translateY 0, opacity 1           │     │
│  │   Duration: 200ms per tick                         │     │
│  │                                                    │     │
│  └────────────────────────────────────────────────────┘     │
│                                                              │
│                                                              │
│  8. AVATAR TRUST RING                                        │
│  ═══════════════════════════════════════════════════════    │
│  Trigger: Biometric-first screen appears                     │
│  Duration: Continuous                                        │
│                                                              │
│  ┌────────────────────────────────────────────────────┐     │
│  │                                                    │     │
│  │   Outer ring: Gradient #667EEA → #764BA2          │     │
│  │   Inner ring: #00D09C (trust indicator)           │     │
│  │                                                    │     │
│  │   Animation:                                       │     │
│  │   • Gradient rotation: 360deg per 8s              │     │
│  │   • Trust ring pulse: opacity 0.8→1.0→0.8 per 2s │     │
│  │                                                    │     │
│  └────────────────────────────────────────────────────┘     │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

---

## Accessibility

### Screen Reader Support

```
┌─────────────────────────────────────────────────────────────┐
│  ACCESSIBILITY SPECIFICATIONS                                │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  CONTENT DESCRIPTIONS                                        │
│  ═══════════════════════════════════════════════════════    │
│                                                              │
│  Biometric Gateway Card:                                     │
│  ─────────────────────────────────────────────────────────  │
│  "Touch to unlock with fingerprint. Double tap to activate" │
│  "Look at device to unlock with Face ID"                    │
│                                                              │
│  Security Score Chip:                                        │
│  ─────────────────────────────────────────────────────────  │
│  "Security score: 92 out of 100. Excellent.                 │
│   47 day streak of secure logins"                           │
│                                                              │
│  Avatar with Trust Ring:                                     │
│  ─────────────────────────────────────────────────────────  │
│  "Profile picture for Maria. Account verified and secure"   │
│                                                              │
│  PIN Dots (Empty):                                           │
│  ─────────────────────────────────────────────────────────  │
│  "Passcode entry. 0 of 4 digits entered"                    │
│                                                              │
│  PIN Dots (Partial):                                         │
│  ─────────────────────────────────────────────────────────  │
│  "Passcode entry. 3 of 4 digits entered"                    │
│                                                              │
│  PIN Dots (Error):                                           │
│  ─────────────────────────────────────────────────────────  │
│  "Error. Wrong passcode. 3 attempts remaining"              │
│                                                              │
│  Digit Key (2):                                              │
│  ─────────────────────────────────────────────────────────  │
│  "2, A B C"                                                  │
│                                                              │
│  Backspace Key:                                              │
│  ─────────────────────────────────────────────────────────  │
│  "Delete. Double tap to delete last digit.                  │
│   Long press to clear all"                                   │
│                                                              │
│  Lockout Screen:                                             │
│  ─────────────────────────────────────────────────────────  │
│  "Account temporarily locked. Too many incorrect attempts.  │
│   Try again in 4 minutes 59 seconds"                        │
│                                                              │
│                                                              │
│  FOCUS ORDER                                                 │
│  ═══════════════════════════════════════════════════════    │
│                                                              │
│  Biometric-First Screen:                                     │
│  ┌────────────────────────────────────────────────────┐     │
│  │  1. Avatar + Username ("Welcome back, Maria")      │     │
│  │  2. Biometric Gateway Card (primary action)        │     │
│  │  3. "Use Passcode Instead" link                    │     │
│  │  4. Security Score Chip                            │     │
│  └────────────────────────────────────────────────────┘     │
│                                                              │
│  Passcode Entry Screen:                                      │
│  ┌────────────────────────────────────────────────────┐     │
│  │  1. Shield Icon (decorative, skipped)              │     │
│  │  2. Title ("Enter Passcode")                       │     │
│  │  3. Subtitle                                       │     │
│  │  4. PIN Dots (announces filled count)              │     │
│  │  5. "Try Biometric" chip                           │     │
│  │  6. Keypad - Row 1: 1, 2, 3                       │     │
│  │  7. Keypad - Row 2: 4, 5, 6                       │     │
│  │  8. Keypad - Row 3: 7, 8, 9                       │     │
│  │  9. Keypad - Row 4: Bio, 0, Backspace             │     │
│  │  10. Forgot Passcode link                         │     │
│  └────────────────────────────────────────────────────┘     │
│                                                              │
│                                                              │
│  MINIMUM TOUCH TARGETS                                       │
│  ═══════════════════════════════════════════════════════    │
│                                                              │
│  All interactive elements: Minimum 48dp x 48dp               │
│  Keypad keys: 76dp x 76dp (exceeds minimum)                  │
│  Biometric card: Full width x 120dp                          │
│  Chip buttons: Height 44dp                                   │
│                                                              │
│                                                              │
│  HAPTIC FEEDBACK MAP                                         │
│  ═══════════════════════════════════════════════════════    │
│                                                              │
│  ┌────────────────────────────────────────────────────┐     │
│  │  Action              │ iOS              │ Android  │     │
│  ├──────────────────────┼──────────────────┼──────────┤     │
│  │  Digit entered       │ Light impact     │ TICK     │     │
│  │  Backspace pressed   │ Light impact     │ TICK     │     │
│  │  Wrong passcode      │ Error (heavy)    │ REJECT   │     │
│  │  Passcode accepted   │ Success          │ CONFIRM  │     │
│  │  Account locked      │ Error (3x heavy) │ HEAVY    │     │
│  │  Biometric started   │ Light impact     │ TICK     │     │
│  │  Biometric success   │ Success          │ CONFIRM  │     │
│  │  Biometric failed    │ Error            │ REJECT   │     │
│  └────────────────────────────────────────────────────┘     │
│                                                              │
│                                                              │
│  REDUCED MOTION SUPPORT                                      │
│  ═══════════════════════════════════════════════════════    │
│                                                              │
│  When "Reduce Motion" is enabled:                            │
│  ─────────────────────────────────────────────────────────  │
│  • Shake animation → Red color flash only                    │
│  • Spring bounce → Instant fill                              │
│  • Glow pulse → Static glow                                  │
│  • Success merge → Instant transition                        │
│  • Avatar ring rotation → Static gradient                    │
│  • Countdown ring → Step changes                             │
│                                                              │
│                                                              │
│  HIGH CONTRAST MODE                                          │
│  ═══════════════════════════════════════════════════════    │
│                                                              │
│  • Dot borders: 3dp instead of 2dp                           │
│  • Key borders: Add 1dp #1F2937 border                       │
│  • Gradients: Replaced with solid primary color              │
│  • Text: Enforced WCAG AAA contrast (7:1 minimum)            │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

---

## Dark Mode Variant

### Biometric-First - Dark Mode

```
+----------------------------------------------------------+
|                                                          |
|  [Status Bar - Light Text]            Background: #0D1117|
|                                                          |
+----------------------------------------------------------+
|                                                          |
|                                                          |
|                     ┌──────────────┐                     |
|                     │              │   Avatar with       |
|                     │   [Avatar]   │   gradient ring     |
|                     │              │   preserved         |
|                     │   Trust Ring │                     |
|                     │   #00D09C    │   Glow: more        |
|                     │              │   prominent         |
|                     └──────────────┘                     |
|                                                          |
|                      Welcome back,                       |
|                        Maria                             |
|                      ───────────                         |
|                      #F0F6FC (Light text)                |
|                                                          |
|                                                          |
|   ┌────────────────────────────────────────────────┐    |
|   │░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░│    |
|   │░░                                            ░░│    |
|   │░░     Gradient preserved: #667EEA → #764BA2  ░░│    |
|   │░░                                            ░░│    |
|   │░░         [ Fingerprint Icon ]               ░░│    |
|   │░░              #FFFFFF                       ░░│    |
|   │░░                                            ░░│    |
|   │░░           Touch to Unlock                  ░░│    |
|   │░░                                            ░░│    |
|   │░░     Enhanced glow: #764BA2@40%             ░░│    |
|   │░░                                            ░░│    |
|   │░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░│    |
|   └────────────────────────────────────────────────┘    |
|                                                          |
|                                                          |
|                   Use Passcode Instead                   |
|                   ────────────────────                   |
|                   #A78BFA (Lighter purple)               |
|                                                          |
|                                                          |
|   ┌────────────────────────────────────────────────┐    |
|   │  🛡  Security: 92/100   [Excellent]   47d ✨   │    |
|   │                                                │    |
|   │  BG: #00D09C@15%   Text: #00D09C               │    |
|   └────────────────────────────────────────────────┘    |
|                                                          |
+----------------------------------------------------------+
```

### Passcode Entry - Dark Mode

```
+----------------------------------------------------------+
|                                                          |
|  [Status Bar - Light Text]            Background: #0D1117|
|                                                          |
+----------------------------------------------------------+
|                                                          |
|                     ┌──────────────┐                     |
|                     │░░░░░░░░░░░░░│   Same gradient     |
|                     │░░ [Shield] ░│   #667EEA→#764BA2   |
|                     │░░ + Key   ░░│                     |
|                     │░░░░░░░░░░░░░│   Enhanced glow:    |
|                     └──────────────┘   #764BA2@35%      |
|                                                          |
|                                                          |
|                   Enter Passcode                         |
|                   ──────────────                         |
|                   #F0F6FC                                |
|                                                          |
|            Your 4-digit PIN to access                    |
|            ───────────────────────────                   |
|            #8B949E                                       |
|                                                          |
|                                                          |
|            ┌───┐   ┌───┐   ┌───┐   ┌───┐                |
|            │   │   │   │   │   │   │   │                |
|            │ ○ │   │ ○ │   │ ○ │   │ ○ │                |
|            │   │   │   │   │   │   │   │                |
|            └───┘   └───┘   └───┘   └───┘                |
|                                                          |
|            Border: #A78BFA                               |
|            Fill (entered): Gradient                      |
|                                                          |
|                                                          |
|        ┌────────────────────────────────────┐           |
|        │  [Fingerprint]  Try Biometric      │           |
|        │  BG: #A78BFA@15%                   │           |
|        └────────────────────────────────────┘           |
|                                                          |
+----------------------------------------------------------+
|                                                          |
|      NUMERIC KEYPAD (Dark Mode)                          |
|      Background: #161B22                                 |
|                                                          |
|   ┌────────────┐  ┌────────────┐  ┌────────────┐        |
|   │            │  │            │  │            │        |
|   │     1      │  │     2      │  │     3      │        |
|   │            │  │    ABC     │  │    DEF     │        |
|   └────────────┘  └────────────┘  └────────────┘        |
|                                                          |
|   Key BG: #21262D                                        |
|   Key Pressed: #A78BFA@25%                               |
|   Number: #F0F6FC                                        |
|   Letters: #6E7681                                       |
|                                                          |
|   ┌────────────┐  ┌────────────┐  ┌────────────┐        |
|   │     4      │  │     5      │  │     6      │        |
|   └────────────┘  └────────────┘  └────────────┘        |
|                                                          |
|   ┌────────────┐  ┌────────────┐  ┌────────────┐        |
|   │     7      │  │     8      │  │     9      │        |
|   └────────────┘  └────────────┘  └────────────┘        |
|                                                          |
|   ┌────────────┐  ┌────────────┐  ┌────────────┐        |
|   │   [Bio]    │  │     0      │  │    [⌫]    │        |
|   │  #A78BFA   │  │            │  │  #8B949E   │        |
|   └────────────┘  └────────────┘  └────────────┘        |
|                                                          |
+----------------------------------------------------------+
|                                                          |
|              Forgot Passcode?                            |
|              #A78BFA                                     |
|                                                          |
+----------------------------------------------------------+
```

### Color Token Mapping

```
┌─────────────────────────────────────────────────────────────┐
│  DARK MODE COLOR MAPPING                                     │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  Element              │ Light Mode    │ Dark Mode    │   │
│  ├──────────────────────────────────────────────────────┤   │
│  │  Screen Background    │ #F8F9FA       │ #0D1117      │   │
│  │  Keypad Background    │ #FFFFFF       │ #161B22      │   │
│  │  Key Background       │ #F0F4F8       │ #21262D      │   │
│  │  Key Pressed BG       │ #667EEA@20%   │ #A78BFA@25%  │   │
│  │  Title Text           │ #1F2937       │ #F0F6FC      │   │
│  │  Subtitle Text        │ #6B7280       │ #8B949E      │   │
│  │  Tertiary Text        │ #9CA3AF       │ #6E7681      │   │
│  │  Key Numbers          │ #1F2937       │ #F0F6FC      │   │
│  │  Key Letters          │ #9CA3AF       │ #6E7681      │   │
│  │  Dot Border (empty)   │ #667EEA       │ #A78BFA      │   │
│  │  Dot Fill (filled)    │ Gradient      │ Gradient     │   │
│  │  Link Color           │ #667EEA       │ #A78BFA      │   │
│  │  Error Color          │ #FF4757       │ #FF6B7A      │   │
│  │  Success Color        │ #00D09C       │ #00D09C      │   │
│  │  Biometric Icon       │ #667EEA       │ #A78BFA      │   │
│  │  Card Glow            │ #667EEA@30%   │ #764BA2@40%  │   │
│  │  Primary Gradient     │ #667EEA→      │ Same         │   │
│  │                       │ #764BA2       │ (preserved)  │   │
│  └──────────────────────────────────────────────────────┘   │
│                                                              │
│                                                              │
│  SPECIAL DARK MODE CONSIDERATIONS                            │
│  ═══════════════════════════════════════════════════════    │
│                                                              │
│  Gradient Preservation:                                      │
│  ─────────────────────────────────────────────────────────  │
│  Primary gradient (#667EEA → #764BA2) remains identical     │
│  in dark mode to maintain brand identity.                    │
│                                                              │
│  Enhanced Glow Effects:                                      │
│  ─────────────────────────────────────────────────────────  │
│  • Biometric card glow: 40% opacity (vs 30% light)          │
│  • Shield glow: 35% opacity (vs 25% light)                  │
│  • Dot fill glow: 50% opacity (vs 40% light)                │
│                                                              │
│  Softer Accent Colors:                                       │
│  ─────────────────────────────────────────────────────────  │
│  • Primary accent shifts to #A78BFA (lighter purple)        │
│  • Error shifts to #FF6B7A (softer red)                     │
│  • Improves contrast and reduces eye strain                  │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

---

## Implementation Notes

### State Management

```kotlin
data class PasscodeScreenState(
    // Mode
    val mode: PasscodeMode = PasscodeMode.BIOMETRIC_FIRST,
    val step: PasscodeStep = PasscodeStep.CURRENT,

    // User
    val userName: String = "",
    val userAvatar: String? = null,

    // Entry
    val enteredDigits: String = "",
    val firstPasscode: String? = null, // For create/confirm flow

    // Biometric
    val biometricType: BiometricType? = null,
    val isBiometricEnabled: Boolean = true,
    val isBiometricScanning: Boolean = false,

    // Error
    val isError: Boolean = false,
    val errorMessage: String? = null,
    val attemptsRemaining: Int = 5,

    // Lockout
    val isLocked: Boolean = false,
    val lockoutEndTime: Instant? = null,
    val lockoutDuration: Duration = Duration.ZERO,

    // Security
    val securityScore: Int = 0,
    val securityStreak: Int = 0,
    val lastLoginTime: Instant? = null,

    // UI
    val isPasscodeVisible: Boolean = false,
    val isLoading: Boolean = false
)

enum class PasscodeMode {
    BIOMETRIC_FIRST,   // Default unlock with biometric as primary
    ENTER_PIN,         // PIN entry (fallback from biometric)
    CREATE_NEW,        // First-time setup
    CONFIRM_NEW,       // Confirm during setup
    CHANGE_CURRENT,    // Enter current (change flow)
    CHANGE_NEW,        // Enter new (change flow)
    CHANGE_CONFIRM     // Confirm new (change flow)
}

enum class PasscodeStep {
    CURRENT,    // Enter current passcode
    NEW,        // Enter new passcode
    CONFIRM     // Confirm passcode
}

enum class BiometricType {
    FINGERPRINT,
    FACE_ID,
    IRIS
}

sealed interface PasscodeEvent {
    data class OnDigitEntered(val digit: Char) : PasscodeEvent
    data object OnBackspacePressed : PasscodeEvent
    data object OnBackspaceLongPressed : PasscodeEvent
    data object OnBiometricPressed : PasscodeEvent
    data class OnBiometricResult(val success: Boolean) : PasscodeEvent
    data object OnForgotPasscodePressed : PasscodeEvent
    data object OnToggleVisibility : PasscodeEvent
    data object OnUsePinInstead : PasscodeEvent
    data object OnSkipPressed : PasscodeEvent
    data object OnPanicModeTriggered : PasscodeEvent
}

sealed interface PasscodeSideEffect {
    data object NavigateToHome : PasscodeSideEffect
    data object NavigateToForgotPasscode : PasscodeSideEffect
    data object ShowBiometricPrompt : PasscodeSideEffect
    data class TriggerHaptic(val type: HapticType) : PasscodeSideEffect
    data object PlaySuccessAnimation : PasscodeSideEffect
    data object PlayErrorAnimation : PasscodeSideEffect
    data object ShowPanicMode : PasscodeSideEffect
}

enum class HapticType {
    LIGHT_IMPACT,
    ERROR,
    SUCCESS,
    HEAVY
}
```

### Required Composables

```kotlin
// Main Screen
@Composable
fun PasscodeScreen(
    state: PasscodeScreenState,
    onEvent: (PasscodeEvent) -> Unit
)

// Biometric-First Mode
@Composable
fun BiometricGatewayScreen(
    userName: String,
    userAvatar: String?,
    biometricType: BiometricType,
    securityScore: Int,
    securityStreak: Int,
    onBiometricClick: () -> Unit,
    onUsePinClick: () -> Unit
)

// PIN Entry Mode
@Composable
fun PinEntryScreen(
    shieldState: ShieldState,
    title: String,
    subtitle: String,
    filledCount: Int,
    isError: Boolean,
    errorMessage: String?,
    attemptsRemaining: Int?,
    onBiometricClick: () -> Unit,
    onForgotClick: () -> Unit
)

// Components
@Composable
fun BiometricGatewayCard(
    biometricType: BiometricType,
    isScanning: Boolean,
    onClick: () -> Unit
)

@Composable
fun UserAvatarWithTrustRing(
    userName: String,
    avatarUrl: String?,
    isVerified: Boolean
)

@Composable
fun SecurityScoreChip(
    score: Int,
    streak: Int?,
    onClick: (() -> Unit)?
)

@Composable
fun PinDotIndicator(
    length: Int = 4,
    filledCount: Int,
    isError: Boolean,
    isVisible: Boolean,
    visibleDigits: List<Char>
)

@Composable
fun NumericKeypad(
    onDigitClick: (Char) -> Unit,
    onBackspaceClick: () -> Unit,
    onBackspaceLongClick: () -> Unit,
    biometricType: BiometricType?,
    onBiometricClick: () -> Unit,
    isBiometricEnabled: Boolean
)

@Composable
fun SecurityShield(
    state: ShieldState,
    size: Dp = 80.dp
)

@Composable
fun LockoutCountdown(
    remainingTime: Duration,
    onResetViaEmail: () -> Unit,
    onContactSupport: () -> Unit
)

@Composable
fun StepIndicator(
    currentStep: Int,
    totalSteps: Int,
    stepLabels: List<String>
)

@Composable
fun PanicModeScreen(
    onLockAccounts: () -> Unit,
    onAlertContact: () -> Unit,
    onCallSupport: () -> Unit,
    onShareLocation: () -> Unit,
    onCancel: () -> Unit
)

enum class ShieldState {
    DEFAULT,    // Purple gradient with key
    CREATE,     // Purple with plus
    CONFIRM,    // Purple with check
    ERROR,      // Red tint with X
    LOCKED,     // Red gradient with lock
    SUCCESS     // Green gradient with check
}
```

---

## Changelog

| Date | Version | Changes |
|------|---------|---------|
| 2025-12-30 | 2.0 | Major redesign: Biometric-first pattern, security score gamification, panic mode, trust ring visualization, enhanced animations |
| 2025-12-30 | 1.0 | Initial mockup with basic vibrant design |

---

## Data Binding Reference

> **For `/implement` command**: Maps UI components to API.md sections.
> **Source of Truth**: See `API.md` for complete endpoint details.

### Passcode Feature - Fully Client-Side

See `API.md → No Backend API Required` - Passcode has NO Fineract APIs.

### Client-Only Features

| Feature | Storage | Notes |
|---------|---------|-------|
| Passcode Hash | DataStore (encrypted) | See `API.md → Local Storage Mechanism` |
| Biometric Setup | Platform Keystore | See `API.md → Platform-Specific Storage` |
| Failed Attempts | DataStore | Lockout tracking |
| Security Score | DataStore | Gamification layer |

### Storage Operations

See `API.md → Storage Operations` for:
- Save passcode hash
- Verify passcode
- Enable/disable biometric
- Reset passcode

### Integration Points

| Action | Result |
|--------|--------|
| Successful auth | Navigate to main app |
| Failed auth | Increment counter, show error |
| Max failures | Lock app, require recovery |
| Biometric success | Decrypt stored credentials |
