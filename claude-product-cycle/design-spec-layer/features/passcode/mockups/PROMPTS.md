# Passcode Feature - AI Design Tool Prompts

> **Generated from**: MOCKUP.md v2.0
> **Design Pattern**: Secure Gateway Experience
> **Primary Gradient**: #667EEA (Purple) -> #764BA2 (Deep Purple)
> **Generated**: 2025-01-04

---

## Google Stitch Prompts

### Prompt 1: Biometric Authentication (Primary Entry)

```
Create a Biometric Authentication screen as the primary login for a fintech banking app.

Design specifications:
- Material Design 3 with 2025 security patterns
- Primary gradient: #667EEA to #764BA2
- Biometric-first, passcode-second design

Header Section:
- Gradient background covering top third
- User avatar (96dp) with verified badge
- Welcome message: "Welcome back, Maria"
- Last login: "Last seen: Today, 2:45 PM"

Biometric Prompt Area (Center):
- Large fingerprint/Face ID icon (120dp) with gradient outline
- Pulsing animation ring around icon
- Text: "Touch sensor to unlock" or "Look at camera"
- Subtle glow effect in brand color

Security Status:
- Shield icon with "Secured with biometrics"
- Device name: "iPhone 15 Pro"

Alternative Options:
- "Use Passcode Instead" link
- "Not Maria? Switch Account" link

Footer:
- App logo small
- Version number
- "Forgot passcode?" help link

Animations:
- Fingerprint icon pulses subtly
- Success: Icon transforms to checkmark with confetti
- Failure: Shake animation with red flash

Style: Premium security feel, biometric-first, trust-building
```

### Prompt 2: Passcode Entry Screen

```
Create a Passcode Entry screen for a fintech banking app.

Design specifications:
- Material Design 3 with secure input patterns
- Gradient header with user context

Header Section:
- Gradient background: #667EEA to #764BA2
- User avatar (64dp)
- "Enter your passcode" title
- Subtitle: "4-digit PIN"

Passcode Dots:
- 4 large dots (16dp each) in horizontal row
- Empty: Outlined circle
- Filled: Gradient filled circle
- Dot animation: Scale up on fill
- Spacing: 24dp between dots

Numpad:
- 3x4 grid layout
- Numbers 1-9 in top 3 rows
- Bottom row: Biometric icon, 0, Delete icon
- Each button: 72dp circle
- Number text: 28sp bold
- Subtle press animation: Scale 0.95
- Haptic feedback indicator

Attempt Counter:
- If wrong: "2 attempts remaining" in warning color
- Progress dots showing attempts

Security Gamification:
- Streak counter: "12 successful logins"
- Badge: "Trusted User" achievement icon

Footer Options:
- "Forgot Passcode?" link
- "Use Biometrics" link (if available)

Error State:
- Dots shake horizontally
- Red outline briefly
- Haptic vibration
- Counter updates

Style: Secure, clear feedback, gamified trust elements
```

### Prompt 3: Create Passcode Flow

```
Create a Create Passcode screen for setting up a new PIN in a fintech app.

Design specifications:
- Material Design 3 with onboarding patterns
- Step progress indicator

Progress Header:
- Step indicator: "Step 2 of 3"
- Progress bar: 66% filled with gradient
- Back arrow

Title Section:
- "Create Your Passcode" (24sp bold)
- "Choose a 4-digit PIN you'll remember" (14sp secondary)

Security Tips Card:
- Light card with security tips:
  - "Avoid sequential numbers (1234)"
  - "Don't use birthdates"
  - "Keep it memorable but unique"
- Collapsible/expandable

Passcode Dots:
- 4 empty dots, filling as user types
- Gradient fill animation

Numpad:
- Same 3x4 layout as entry screen
- Numbers 1-9, 0, delete
- No biometric option during creation

Strength Indicator:
- Below dots: "Passcode Strength"
- Bar showing: Weak (red) -> Medium (yellow) -> Strong (green)
- Updates in real-time based on pattern

Continue Button:
- "Continue" - disabled until 4 digits entered
- Gradient filled when active
- Leads to confirmation step

Style: Guided creation, security education, clear progress
```

### Prompt 4: Confirm Passcode Screen

```
Create a Confirm Passcode screen for verifying new PIN in a fintech app.

Design specifications:
- Material Design 3 continuation of create flow
- Confirmation step with matching validation

Progress Header:
- Step indicator: "Step 3 of 3"
- Progress bar: Nearly complete (85%)

Title Section:
- "Confirm Your Passcode" (24sp bold)
- "Re-enter your 4-digit PIN to confirm" (14sp secondary)

Visual Reference:
- Small masked display of what was entered: "●●●●"
- "Previously entered" label

Passcode Dots:
- 4 empty dots for re-entry
- Match indicator when complete:
  - Match: Green checkmark animation
  - Mismatch: Red X with shake

Numpad:
- Standard 3x4 layout
- Numbers only, delete

Match Feedback:
- Success: "Passcodes match!" with checkmark
- Error: "Passcodes don't match. Try again." with reset

Next Steps Card:
- After success, show:
  - "Set up biometrics next?" toggle
  - "Enable Face ID / Touch ID for faster login"

Complete Button:
- "Complete Setup" - appears on match
- Gradient filled
- Leads to success screen

Style: Clear confirmation flow, immediate feedback, next step promotion
```

### Prompt 5: Change Passcode Flow

```
Create a Change Passcode screen for updating existing PIN in a fintech app.

Design specifications:
- Material Design 3 with security update patterns
- Three-step verification flow

Header:
- Back button
- "Change Passcode" title
- Step indicator

Current Passcode Step:
- "Enter Current Passcode" title
- 4 passcode dots
- Numpad with biometric option
- "Forgot current passcode?" link

New Passcode Step (after verification):
- "Create New Passcode" title
- Security tips reminder
- 4 empty dots
- Strength indicator

Confirm Step:
- "Confirm New Passcode"
- Match validation

Security Confirmation:
- After success:
  - "Passcode Updated Successfully"
  - Checkmark animation
  - "All your sessions remain active"
  - "Last changed: Just now"

Session Options:
- "Sign out other devices?" optional action
- List of active sessions if multiple

Style: Secure update flow, session awareness, confirmation
```

---

## Figma MCP Prompts

### Prompt 1: Passcode Entry Frame

```
Create a Figma frame for "Passcode - Entry Screen" mobile screen (375x812px).

Components needed:
1. Status bar (system)

2. Header Component:
   - Gradient background: #667EEA to #764BA2
   - Avatar with verified badge (64dp)
   - Title and subtitle text

3. Passcode Dots Component:
   - 4 circle variants: empty, filled, error
   - Gradient fill for filled state
   - Horizontal layout with 24dp gap
   - Animation state indicators

4. Numpad Component:
   - 3x4 grid
   - Number button (72dp circle):
     - Default, pressed, disabled states
   - Special buttons: biometric, delete icons
   - Consistent typography: 28sp bold

5. Attempt Counter:
   - Warning text variant
   - Attempt dots

6. Footer Links:
   - Text links with tap targets

Design tokens:
- Primary: #667EEA
- Secondary: #764BA2
- Success: #00D09C
- Error: #FF4757
- Surface: #FFFFFF
- Numpad button: #F8F9FA
```

### Prompt 2: Complete Passcode Flow

```
Create a Figma prototype flow for Passcode feature with these frames:

Frame 1: Biometric Entry (default)
- Face ID / Touch ID prompt
- User welcome
- Fallback to passcode link

Frame 2: Passcode Entry
- Gradient header with avatar
- 4 passcode dots
- Numpad grid
- Biometric shortcut

Frame 3: Create Passcode
- Progress indicator
- Security tips
- Empty dots
- Strength meter

Frame 4: Confirm Passcode
- Progress near complete
- Re-entry dots
- Match/mismatch states

Frame 5: Change Passcode
- Current passcode verification
- New passcode creation
- Confirmation step

Frame 6: Success Screen
- Checkmark animation
- Confirmation message
- Continue button

Frame 7: Error States
- Max attempts warning
- Locked account state
- Recovery options

Prototype connections:
- Biometric -> Success or Passcode fallback
- Passcode entry dots fill on numpad tap
- Wrong passcode shows error state
- Create flow progresses through steps

Component variants:
- Passcode dot: empty/filled/error/success
- Numpad button: default/pressed/disabled
- Strength meter: weak/medium/strong
```

---

## Design Tokens

```json
{
  "feature": "passcode",
  "version": "2.0",
  "designPattern": "Secure Gateway Experience",
  "colors": {
    "primary": "#667EEA",
    "secondary": "#764BA2",
    "success": "#00D09C",
    "error": "#FF4757",
    "warning": "#FFB800",
    "surface": "#FFFFFF",
    "background": "#F8F9FA"
  },
  "components": {
    "passcodeDot": {
      "size": 16,
      "gap": 24,
      "emptyBorder": 2,
      "variants": {
        "empty": { "border": "#E1E4E8", "fill": "transparent" },
        "filled": { "border": "gradient", "fill": "gradient" },
        "error": { "border": "#FF4757", "fill": "#FF4757" },
        "success": { "border": "#00D09C", "fill": "#00D09C" }
      }
    },
    "numpadButton": {
      "size": 72,
      "fontSize": 28,
      "background": "#F8F9FA",
      "pressedScale": 0.95
    },
    "biometricIcon": {
      "size": 120,
      "strokeWidth": 3,
      "pulseScale": 1.1
    },
    "strengthMeter": {
      "height": 4,
      "radius": 2,
      "segments": 3,
      "colors": {
        "weak": "#FF4757",
        "medium": "#FFB800",
        "strong": "#00D09C"
      }
    }
  },
  "animation": {
    "dotFill": {
      "duration": 150,
      "scale": 1.2
    },
    "errorShake": {
      "duration": 400,
      "distance": 10
    },
    "biometricPulse": {
      "duration": 2000,
      "type": "infinite"
    },
    "successCheck": {
      "duration": 500,
      "type": "draw-stroke"
    }
  }
}
```

---

## Screen Checklist

| Screen | Stitch Prompt | Figma Prompt | Status |
|--------|---------------|--------------|--------|
| Biometric Entry | 1 | 2 | Ready |
| Passcode Entry | 2 | 1, 2 | Ready |
| Create Passcode | 3 | 2 | Ready |
| Confirm Passcode | 4 | 2 | Ready |
| Change Passcode | 5 | 2 | Ready |
| Success Screen | - | 2 | Pending |
| Error/Locked States | - | 2 | Pending |
