# Auth - Figma First Draft Prompts

> **Tool**: Figma First Draft (Shift+I in Figma)
> **Style**: Premium Fintech, Modern Banking App
> **Format**: Natural language prompts optimized for Figma AI

---

## How to Use

1. Open Figma → Create new design file
2. Press `Shift + I` or click "Actions" → "First Draft"
3. Copy each prompt below
4. Generate → Iterate → Refine

---

## Screen 1: Splash Screen

```
Design a premium fintech app splash screen for "Mifos Mobile" banking app.

Use a beautiful purple-blue gradient background going diagonally. Place a white minimalist logo in the center with a subtle glow effect. Below the logo, show "MIFOS" in white uppercase letters with wide letter spacing. Add a tagline "Your Financial Freedom" in lighter white below.

At the bottom, show three small loading dots that would animate. Keep it clean, premium, and trustworthy like Revolut or Monzo splash screens. No navigation bars or buttons - just the brand moment.
```

---

## Screen 2: Welcome/Onboarding

```
Design a mobile onboarding welcome screen for a banking app.

At the top right, add a "Skip" text link in purple. In the center, create space for a friendly illustration showing money management - cards, coins, or transfer arrows.

Below the illustration, add a bold headline "Manage Your Money" and a subtitle explaining "Track spending, save smarter, and transfer money instantly—all in one secure app."

Show three page indicator dots (first one active in purple). Add a large "Get Started" button with a purple gradient background. Below it, show "Already have an account? Sign In" with Sign In as a clickable link.

At the very bottom, add a small trust badge showing "Bank-grade security • 256-bit encryption" with a lock icon.
```

---

## Screen 3: Login Hub

```
Design a modern login screen for a fintech app with multiple sign-in options.

Show the Mifos logo at the top, followed by "Welcome Back" as the main heading and "Sign in to continue" as a subtitle.

Create a phone number input field with a country code picker (showing a flag and +1). The input should have a clean outlined style.

Add a prominent "Continue with Phone" button with purple gradient.

Below, show a divider with "or" text.

Then display two side-by-side option cards: one for "Use Biometric" with a face icon, and one for "Use Email" with an email icon. Both should be outlined cards.

At the bottom, show server info "Server: gsoc.mifos.community" with a Change link, and a security badge about encryption.
```

---

## Screen 4: OTP Verification

```
Design an OTP code verification screen for a banking app.

Add a back arrow at the top left. Show a phone icon with a checkmark in a circular purple gradient container.

Display "Verify Your Phone" as the heading. Below it show "We sent a 6-digit code to" and the phone number "+1 (555) 123-4567" in bold.

Create 6 individual square input boxes for the OTP code, arranged in a row with small gaps between them. Some boxes should show numbers to indicate the filled state.

Show "Auto-reading SMS..." text in purple below the inputs.

Add a resend section: "Didn't receive the code?" with a "Resend in 0:45" countdown timer.

At the bottom, show "Code expires in 10 minutes" with a lock icon.
```

---

## Screen 5: Email/Password Login

```
Design an email and password login screen for a banking app.

Add a back arrow and the Mifos logo at the top. Show "Sign in with Email" as the heading.

Create two input fields:
1. Username or Email field with a user icon on the left
2. Password field with a lock icon on the left and an eye icon on the right to toggle visibility

Add a "Forgot Password?" link aligned to the right below the password field.

Include a "Sign In" button with purple gradient.

Show a divider with "or" text.

Add two alternative login options as outlined cards: "Use Biometric" and "Use Phone".

At the bottom, show server information and a security badge.
```

---

## Screen 6: Biometric Setup

```
Design a biometric authentication setup screen for a banking app.

Add a "Skip" link at the top right.

In the center, show a large illustration of Face ID or fingerprint scanning - make it look modern with scanning lines and purple accent colors.

Display "Enable Face ID" as the heading and explain "Use Face ID for quick and secure access to your Mifos account" as a centered paragraph below.

Add a primary "Enable Face ID" button with purple gradient.

Below it, add a "Maybe Later" text button in gray.

At the bottom, show a helpful note: "You can enable this anytime in Settings" with an info icon.
```

---

## Screen 7: Server Selection

```
Design a server selection screen for a banking app that connects to different backend servers.

Add a back arrow and "Select Server" as the title with "Choose your Mifos instance" as subtitle.

Create three sections:

RECOMMENDED section:
- A selected card with gradient border showing "Demo Server" and "gsoc.mifos.community" with a globe icon and checkmark

OTHER SERVERS section:
- Card for "Production Server" at "demo.mifos.io" with a building icon
- Card for "Sandbox Server" at "sandbox.mifos.community" with a wrench icon

CUSTOM SERVER section:
- An input field with "https://" placeholder for entering a custom URL

Add a "Connect" button with purple gradient at the bottom.
```

---

## Screen 8: Login Success

```
Design a celebration/success screen shown after successful login to a banking app.

In the center, show a large green checkmark icon with confetti particles bursting around it - make it feel celebratory and delightful.

Display "You're All Set!" as a bold heading.

Below, show a personalized welcome message "Welcome back, Maria!" with a wave emoji.

At the bottom, add a progress bar that's filling up with text "Setting up your dashboard..." below it. This indicates the app is loading the home screen.

Keep it joyful but professional - like a premium banking app celebrating a successful login.
```

---

## Component Prompts

### Primary Button
```
Design a primary action button for a fintech app. Make it 56 pixels tall with rounded corners (16px radius). Use a purple gradient background from #667EEA to #764BA2. White text, medium weight, centered. Add a subtle shadow for depth. Show default, pressed, and disabled states.
```

### Text Input Field
```
Design a text input field for a banking app. 56 pixels tall with 12px rounded corners. Light gray border that turns purple when focused. Include a left icon area and right icon area (for things like visibility toggle). Show default, focused, filled, and error states.
```

### OTP Input
```
Design a 6-digit OTP code input for a banking app. Create 6 individual square boxes arranged horizontally with small gaps. Each box should have rounded corners and a light border. When filled, the box should have a light purple background. Numbers should be large and bold. Show empty, focused, and filled states.
```

---

## Tips for Figma First Draft

1. **Iterate**: First generation is a starting point - refine with follow-up prompts
2. **Be specific**: Add details about colors, spacing, and interactions
3. **Reference styles**: Mention "like Revolut" or "Monzo-style" for context
4. **Component mode**: Generate components separately, then assemble screens
5. **Variants**: Ask for multiple states (default, hover, pressed, disabled)

---

## After Generation

1. Review and adjust generated designs
2. Ensure consistency across all 8 screens
3. Create a component library from repeated elements
4. Export frames and update `FIGMA_LINKS.md`
