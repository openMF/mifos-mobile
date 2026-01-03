# Auth - AI Mockup Prompts

> **Generated from**: features/auth/MOCKUP.md
> **Generated on**: 2026-01-03
> **AI Tool**: Google Stitch (Material Design 3)
> **Design Style**: Premium Fintech (Revolut + Monzo inspired)

---

## Screen 1: Splash Screen

### Google Stitch Prompt

Create a mobile splash screen for a premium fintech banking app with Material Design 3:

**App Context:**
Mifos Mobile - Self-service banking app for managing accounts, loans, and transfers.

**Screen Size:** 393 x 852 pixels (Android standard)

**Background:**
- Full screen gradient at 45° angle
- Start color: #667EEA (Purple-Blue)
- End color: #764BA2 (Deep Purple)

**Center Content:**
- App logo: 72dp white icon, centered
- Logo has subtle glow effect (20dp blur, white 15% opacity)
- Below logo: "MIFOS" text, 24sp, white, letter-spacing 8dp
- Tagline: "Your Financial Freedom", 14sp, white 70% opacity

**Bottom:**
- Three loading dots, sequential pulse animation
- White color, 8dp diameter each

**Style Guidelines:**
- Premium, modern fintech aesthetic
- Smooth gradient transition
- Minimalist, trust-building design
- No visible UI chrome

---

## Screen 2: Welcome/Onboarding Screen

### Google Stitch Prompt

Create a mobile onboarding welcome screen for a fintech app with Material Design 3:

**App Context:**
Mifos Mobile - First-time user onboarding with value propositions.

**Screen Size:** 393 x 852 pixels

**Top Right:**
- "Skip" text link, 14sp, color #667EEA

**Center:**
- Large illustration area: 240dp x 240dp
- Animated illustration showing money/cards/transfers
- Use fintech iconography (credit cards, dollar signs, arrows)

**Below Illustration:**
- Title: "Manage Your Money", 28sp, Bold, #1F2937
- Subtitle: "Track spending, save smarter, and transfer money instantly—all in one secure app.", 16sp, #6B7280, center-aligned, line-height 24dp

**Page Indicators:**
- Three dots horizontally centered
- Active dot: #667EEA (filled)
- Inactive dots: #E5E7EB (outlined)

**Primary Button:**
- "Get Started" button, full width with 16dp horizontal margin
- Height: 56dp, Corner radius: 16dp
- Background: Gradient #667EEA → #764BA2
- Text: 16sp, SemiBold, White
- Shadow: 0 4dp 12dp rgba(102,126,234,0.3)

**Below Button:**
- "Already have an account? Sign In", 14sp
- "Sign In" is a link in #667EEA

**Bottom:**
- Trust badge: "🔒 Bank-grade security • 256-bit encryption"
- 12sp, #9CA3AF, centered

---

## Screen 3: Login Hub Screen

### Google Stitch Prompt

Create a mobile login screen for a fintech banking app with Material Design 3:

**App Context:**
Mifos Mobile - Login screen with multiple authentication options.

**Screen Size:** 393 x 852 pixels

**Header:**
- Mifos logo centered, 56dp, primary gradient colors
- Title: "Welcome Back", 24sp, Bold, #1F2937
- Subtitle: "Sign in to continue", 14sp, #6B7280

**Phone Input Section:**
- Container with country picker and phone input
- Country picker: Flag + dial code (e.g., 🇺🇸 +1), dropdown arrow
- Phone input: "Enter phone number" placeholder
- Container height: 56dp, border: 1dp #E5E7EB, radius: 12dp
- Focus state: border 2dp #667EEA with shadow

**Primary Button:**
- "Continue with Phone", full width
- Same styling as onboarding button
- Disabled state until valid phone entered

**Divider:**
- "─────── or ───────" with "or" text

**Alternative Auth Cards:**
- Two side-by-side cards
- Left: "Use Biometric" with face icon
- Right: "Use Email" with email icon
- Each: 56dp height, outlined, 12dp radius

**Bottom Section:**
- Server info: "Server: gsoc.mifos.community [Change]"
- Trust badge: "🔒 256-bit encryption • Your data is secure"

---

## Screen 4: OTP Verification Screen

### Google Stitch Prompt

Create a mobile OTP verification screen for a fintech app with Material Design 3:

**App Context:**
Mifos Mobile - 6-digit SMS code verification after phone login.

**Screen Size:** 393 x 852 pixels

**Top:**
- Back arrow button, 24dp, #1F2937

**Center Icon:**
- Phone with checkmark illustration
- 80dp container with primary gradient background
- Animated scanning effect

**Title Section:**
- "Verify Your Phone", 24sp, Bold, #1F2937
- "We sent a 6-digit code to", 14sp, #6B7280
- "+1 (555) 123-4567", 14sp, Bold, #1F2937

**OTP Input:**
- 6 individual input boxes in a row
- Each box: 48dp x 56dp, gap: 12dp between
- Border: 2dp #E5E7EB, radius: 8dp
- Filled state: background #667EEA 10%, border #667EEA
- Numbers: 24sp, Bold, center-aligned

**Auto-read indicator:**
- "Auto-reading SMS..." with animated dots
- 12sp, #667EEA

**Resend Section:**
- "Didn't receive the code?", 14sp, #6B7280
- "Resend in 0:45" countdown, 14sp, #667EEA (disabled)
- After timer: "Resend Code" link, underlined

**Bottom:**
- "🔒 Code expires in 10 minutes", 12sp, #9CA3AF

---

## Screen 5: Email/Password Login Screen

### Google Stitch Prompt

Create a mobile email login screen for a fintech app with Material Design 3:

**App Context:**
Mifos Mobile - Alternative login for username/password users.

**Screen Size:** 393 x 852 pixels

**Top:**
- Back arrow button
- Mifos logo centered, smaller (48dp)

**Title:**
- "Sign in with Email", 24sp, Bold, #1F2937

**Form Fields:**

**Username/Email Input:**
- Label: "Username or Email", 12sp, #6B7280, floating
- Input: 56dp height, user icon (👤) left, 16sp text
- Border: 1dp #E5E7EB, radius: 12dp
- Focus: 2dp #667EEA border, shadow

**Password Input:**
- Label: "Password", 12sp, #6B7280
- Input: 56dp height, lock icon (🔒) left, visibility toggle (👁) right
- Password dots: ••••••••••••

**Forgot Password:**
- "Forgot Password?" link, right-aligned, 14sp, #667EEA

**Primary Button:**
- "Sign In", full width, gradient background

**Divider:**
- "─────── or ───────"

**Alternative Auth:**
- Two option cards: "Use Biometric" and "Use Phone"

**Bottom:**
- Server info row
- Security badge

---

## Screen 6: Biometric Setup Screen

### Google Stitch Prompt

Create a mobile biometric enrollment screen for a fintech app with Material Design 3:

**App Context:**
Mifos Mobile - Face ID/Fingerprint setup for quick login.

**Screen Size:** 393 x 852 pixels

**Top Right:**
- "Skip" text link

**Center Illustration:**
- Large face/fingerprint illustration, 200dp x 200dp
- Face ID variant: Face outline with scanning animation
- Primary gradient accent colors
- Animated scanning line effect

**Title:**
- "Enable Face ID" (or "Enable Fingerprint"), 24sp, Bold, #1F2937

**Description:**
- "Use Face ID for quick and secure access to your Mifos account."
- 16sp, #6B7280, center-aligned, max-width 280dp

**Primary Button:**
- "Enable Face ID", full width, gradient

**Secondary Button:**
- "Maybe Later", text button, 14sp, #6B7280

**Bottom Info:**
- "ℹ️ You can enable this anytime in Settings"
- 12sp, #9CA3AF

---

## Screen 7: Server Selection Screen

### Google Stitch Prompt

Create a mobile server selection screen for a fintech app with Material Design 3:

**App Context:**
Mifos Mobile - Allow users to switch between different Mifos instances.

**Screen Size:** 393 x 852 pixels

**Top:**
- Back button
- Title: "Select Server", 24sp, Bold, #1F2937
- Subtitle: "Choose your Mifos instance", 14sp, #6B7280

**Recommended Section:**
- Section label: "RECOMMENDED", 12sp, #6B7280
- Card with gradient border (selected state):
  - Globe icon, "Demo Server", "gsoc.mifos.community"
  - Checkmark indicator for selected

**Other Servers Section:**
- Section label: "OTHER SERVERS"
- Two outlined cards:
  - Building icon, "Production Server", "demo.mifos.io"
  - Wrench icon, "Sandbox Server", "sandbox.mifos.community"

**Custom Server Section:**
- Section label: "CUSTOM SERVER"
- Input field: "https://" with placeholder
- 56dp height, 12dp radius

**Primary Button:**
- "Connect", full width, gradient

---

## Screen 8: Login Success Screen

### Google Stitch Prompt

Create a mobile success/celebration screen for a fintech app with Material Design 3:

**App Context:**
Mifos Mobile - Celebration moment after successful login.

**Screen Size:** 393 x 852 pixels

**Center:**
- Large success animation, 160dp container
- Green checkmark (#00D09C) with confetti particles
- Confetti: multi-color small shapes bursting outward

**Title:**
- "You're All Set!", 28sp, Bold, #1F2937

**Welcome Message:**
- "Welcome back, Maria! 👋", 16sp, #6B7280

**Progress Indicator:**
- Linear progress bar at bottom
- "Setting up your dashboard...", 12sp, #6B7280
- Progress bar fills over 2.5 seconds

**Animation Notes:**
- Checkmark draws in with stroke animation
- Confetti bursts from center
- Auto-navigates to home after progress completes

---

## Component Prompts

### Primary Button Component

Create a Material Design 3 primary button for fintech app:
- Height: 56dp, Corner radius: 16dp
- Background: Linear gradient #667EEA → #764BA2
- Text: 16sp, SemiBold, White, centered
- Shadow: 0 4dp 12dp rgba(102,126,234,0.3)
- Press state: scale to 0.98, reduce shadow
- Disabled: 50% opacity, no shadow

### Input Field Component

Create a Material Design 3 input field for fintech app:
- Height: 56dp, Corner radius: 12dp
- Border: 1dp #E5E7EB (default), 2dp #667EEA (focus)
- Left icon: 20dp, #9CA3AF (default), #667EEA (focus)
- Text: 16sp, #1F2937
- Placeholder: 16sp, #9CA3AF
- Focus shadow: 0 0 8dp rgba(102,126,234,0.2)
- Error: border #FF4757, icon #FF4757

### OTP Input Component

Create a Material Design 3 OTP input for fintech app:
- 6 boxes in a row, each 48dp x 56dp
- Gap: 12dp between boxes
- Border: 2dp #E5E7EB, radius: 8dp
- Filled: background #667EEA 10%, border #667EEA
- Focus: border #667EEA with glow
- Number: 24sp, Bold, centered

---

## Export Instructions

### Using Google Stitch

1. Go to [stitch.withgoogle.com](https://stitch.withgoogle.com/)
2. Sign in with Google account
3. Create new project "Mifos Mobile - Auth"
4. For each screen:
   - Copy the prompt above
   - Paste into Stitch
   - Generate design
   - Adjust as needed
5. Export all screens to Figma

### Export to Figma

1. In Stitch, click "Export to Figma"
2. Select your Figma team/project
3. Organize in pages:
   - Page 1: Splash
   - Page 2: Welcome
   - Page 3: Login Hub
   - Page 4: OTP
   - Page 5: Email Login
   - Page 6: Biometric Setup
   - Page 7: Server Selection
   - Page 8: Success

### After Export

Update `features/auth/mockups/FIGMA_LINKS.md` with:
- Figma file URL
- Individual frame URLs for each screen
