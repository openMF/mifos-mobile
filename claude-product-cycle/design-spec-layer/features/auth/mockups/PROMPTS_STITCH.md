# Auth - Google Stitch Prompts

> **Tool**: [Google Stitch](https://stitch.withgoogle.com)
> **Style**: Material Design 3, Premium Fintech
> **Format**: Copy each prompt block directly into Stitch

---

## Design System Reference

```
Primary Gradient: #667EEA → #764BA2
Success: #00D09C
Error: #FF4757
Text Primary: #1F2937
Text Secondary: #6B7280
Text Muted: #9CA3AF
Border: #E5E7EB
Screen Size: 393 x 852 pixels (Android)
```

---

## Screen 1: Splash

```
Mobile splash screen, Material Design 3, fintech banking app, 393x852px

Background: Full gradient 45deg from #667EEA to #764BA2

Center:
- White app logo icon, 72dp, subtle glow effect
- "MIFOS" text below, 24sp white, letter-spacing 8dp
- Tagline "Your Financial Freedom", 14sp, white 70% opacity

Bottom: Three white loading dots, 8dp each, pulse animation

Style: Premium, minimalist, no UI chrome
```

---

## Screen 2: Welcome/Onboarding

```
Mobile onboarding screen, Material Design 3, fintech app, 393x852px

Top right: "Skip" link, 14sp, #667EEA

Center:
- Illustration 240x240dp showing money, cards, transfers
- Title "Manage Your Money", 28sp bold, #1F2937
- Subtitle "Track spending, save smarter, and transfer money instantly—all in one secure app", 16sp, #6B7280, centered

Page indicators: 3 dots, active #667EEA filled, inactive #E5E7EB outlined

Primary button: "Get Started", full width, 56dp height, 16dp radius, gradient #667EEA to #764BA2, white text, shadow

Below button: "Already have an account? Sign In" with "Sign In" as #667EEA link

Bottom: Lock icon + "Bank-grade security | 256-bit encryption", 12sp, #9CA3AF
```

---

## Screen 3: Login Hub

```
Mobile login screen, Material Design 3, fintech banking app, 393x852px

Header:
- Mifos logo centered, 56dp, gradient colors
- "Welcome Back", 24sp bold, #1F2937
- "Sign in to continue", 14sp, #6B7280

Phone input section:
- Country picker with flag + dial code dropdown
- Phone number input field
- Container 56dp height, 1dp #E5E7EB border, 12dp radius

Primary button: "Continue with Phone", full width, gradient, disabled until valid input

Divider: "or" with lines

Two side-by-side cards below:
- Left: Face icon + "Use Biometric", outlined, 56dp, 12dp radius
- Right: Email icon + "Use Email", outlined, 56dp, 12dp radius

Bottom:
- "Server: gsoc.mifos.community [Change]", small text
- Lock icon + "256-bit encryption | Your data is secure"
```

---

## Screen 4: OTP Verification

```
Mobile OTP verification screen, Material Design 3, fintech app, 393x852px

Top: Back arrow button, 24dp, #1F2937

Center icon: Phone with checkmark, 80dp container, gradient background

Title section:
- "Verify Your Phone", 24sp bold, #1F2937
- "We sent a 6-digit code to", 14sp, #6B7280
- "+1 (555) 123-4567", 14sp bold, #1F2937

OTP input: 6 individual boxes in row, each 48x56dp, 12dp gap, 2dp #E5E7EB border, 8dp radius
- Filled state: #667EEA 10% background, #667EEA border
- Numbers: 24sp bold centered

Below OTP: "Auto-reading SMS..." with dots, 12sp, #667EEA

Resend section:
- "Didn't receive the code?", 14sp, #6B7280
- "Resend in 0:45" countdown or "Resend Code" link

Bottom: Lock icon + "Code expires in 10 minutes", 12sp, #9CA3AF
```

---

## Screen 5: Email Login

```
Mobile email login screen, Material Design 3, fintech app, 393x852px

Top: Back arrow + Mifos logo centered 48dp

Title: "Sign in with Email", 24sp bold, #1F2937

Form fields:
1. Username/Email input
   - Floating label "Username or Email", 12sp, #6B7280
   - User icon left, 56dp height, 1dp #E5E7EB border, 12dp radius

2. Password input
   - Floating label "Password"
   - Lock icon left, eye toggle right
   - Password dots shown

"Forgot Password?" link, right-aligned, 14sp, #667EEA

Primary button: "Sign In", full width, gradient

Divider: "or" with lines

Two option cards: "Use Biometric" and "Use Phone"

Bottom: Server info + security badge
```

---

## Screen 6: Biometric Setup

```
Mobile biometric setup screen, Material Design 3, fintech app, 393x852px

Top right: "Skip" link

Center:
- Large face/fingerprint illustration, 200x200dp
- Scanning animation effect
- Primary gradient accent colors

Title: "Enable Face ID", 24sp bold, #1F2937

Description: "Use Face ID for quick and secure access to your Mifos account", 16sp, #6B7280, centered, max 280dp width

Primary button: "Enable Face ID", full width, gradient

Secondary: "Maybe Later", text button, 14sp, #6B7280

Bottom: Info icon + "You can enable this anytime in Settings", 12sp, #9CA3AF
```

---

## Screen 7: Server Selection

```
Mobile server selection screen, Material Design 3, fintech app, 393x852px

Top: Back button + "Select Server" title 24sp bold + "Choose your Mifos instance" subtitle 14sp #6B7280

Section "RECOMMENDED" (12sp label):
- Card with gradient border (selected):
  - Globe icon, "Demo Server", "gsoc.mifos.community"
  - Checkmark for selected state

Section "OTHER SERVERS":
- Card: Building icon, "Production Server", "demo.mifos.io"
- Card: Wrench icon, "Sandbox Server", "sandbox.mifos.community"

Section "CUSTOM SERVER":
- Input field with "https://" prefix, 56dp, 12dp radius

Primary button: "Connect", full width, gradient
```

---

## Screen 8: Login Success

```
Mobile success celebration screen, Material Design 3, fintech app, 393x852px

Center:
- Large success animation container, 160dp
- Green checkmark (#00D09C) with confetti particles
- Multi-color confetti bursting outward

Title: "You're All Set!", 28sp bold, #1F2937

Welcome: "Welcome back, Maria!", 16sp, #6B7280

Bottom:
- Linear progress bar filling
- "Setting up your dashboard...", 12sp, #6B7280
```

---

## Components (Generate Separately)

### Primary Button
```
Material Design 3 button component:
- 56dp height, 16dp corner radius
- Gradient background #667EEA to #764BA2
- White text, 16sp semibold, centered
- Shadow: 0 4dp 12dp rgba(102,126,234,0.3)
- States: default, pressed (0.98 scale), disabled (50% opacity)
```

### Input Field
```
Material Design 3 input field:
- 56dp height, 12dp corner radius
- Border: 1dp #E5E7EB default, 2dp #667EEA focus
- Left icon 20dp, #9CA3AF default, #667EEA focus
- Text 16sp #1F2937, placeholder #9CA3AF
- Error state: #FF4757 border and icon
```

### OTP Input
```
Material Design 3 OTP input, 6 boxes:
- Each box 48x56dp, 12dp gap between
- Border 2dp #E5E7EB, 8dp radius
- Filled: #667EEA 10% background, #667EEA border
- Number: 24sp bold centered
```

---

## Quick Start

1. Open [stitch.withgoogle.com](https://stitch.withgoogle.com)
2. Create project "Mifos Mobile - Auth"
3. Copy each prompt block above
4. Generate screen → Adjust → Next screen
5. Export all to Figma when done
