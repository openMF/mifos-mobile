# Settings Feature - AI Design Tool Prompts

> **Generated from**: MOCKUP.md v2.0
> **Design Pattern**: Personal Command Center
> **Primary Gradient**: #667EEA (Purple) -> #764BA2 (Deep Purple)
> **Generated**: 2025-01-04

---

## Google Stitch Prompts

### Prompt 1: Command Center Hub (Main Settings)

```
Create a mobile settings screen called "Command Center Hub" for a fintech banking app.

Design specifications:
- Material Design 3 with 2025 fintech patterns
- Primary gradient: #667EEA to #764BA2 (purple-blue)
- Background: #F8F9FA

Header Section:
- Gradient card with user profile: avatar with initials, name, email, verified badge
- Profile completion progress bar showing 96% with "Add phone number to reach 100%" CTA
- Avatar shows 96% completion ring

Quick Actions Row:
- 4 icon buttons in horizontal row: Lock App, Dark Mode, Mute Alerts, Export Data
- Each button: 56dp square, #F8F9FA background, 16dp corners

Security Health Card:
- Security score visualization: "88/100 GOOD" with progress bar
- Checklist items with status icons: Passcode enabled (green check), Biometrics active (green check), Password warning (orange), 2FA not enabled (red X)
- "Improve Security" CTA button

Settings List:
- List items with leading icons and chevrons:
  - Profile & Account (person icon)
  - Security Center (lock icon)
  - Appearance (palette icon)
  - Notifications (bell icon)
  - Connected Services (link icon)
  - Privacy & Data (chart icon)
  - Help & Support (help icon)
  - About (info icon)
- Each row: 72dp height, 16dp padding, subtle dividers

Footer:
- Sign Out button (outlined, red text)
- Version text: "v2.0.0 (Build 2025.12.30)"

Style: Clean, professional, trust-focused fintech aesthetic
```

### Prompt 2: Profile Dashboard

```
Create a Profile Dashboard screen for a fintech banking app.

Design specifications:
- Material Design 3 with 2025 patterns
- Primary gradient header: #667EEA to #764BA2

Header Section:
- Large gradient header with centered avatar (96dp)
- Avatar with "Change Photo" camera icon overlay
- User name "John Doe" with verified badge
- "Member since Dec 2023" subtitle

Profile Completion Card:
- 96% completion with animated progress bar
- Checklist: Email verified, Identity verified, Address added (all green checks)
- One incomplete: "Add phone number (+4%)" with arrow

Achievements Section:
- Horizontal scrollable badges: Early Adopter, Power User, 1 Year Member (locked at 11mo), Secure Account
- Each badge: 80dp square, icon, title, checkmark or lock
- "View All 8 Achievements" link

Personal Information Form:
- Editable fields with icons:
  - Full Name (editable)
  - Email (locked, cannot change)
  - Phone Number (not added, plus icon to add)
  - Address (editable)
  - Account Number (locked, read-only)
- Each field: 72dp height, label above value

Danger Zone:
- Delete Account option with warning text
- Red text, caution styling

Style: Trust-building, progress-focused, gamified elements
```

### Prompt 3: Security Center

```
Create a Security Center screen for a fintech banking app.

Design specifications:
- Material Design 3 with security-focused design
- Primary gradient: #667EEA to #764BA2 for header

Security Score Header:
- Large circular score display: "88" with "GOOD" label
- Gradient background card
- Text: "Your Security Score" and "Add 2FA to reach 100"

Quick Security Actions:
- Two large buttons side by side: "Lock App Now", "Freeze Accounts"
- Icon + text, outlined style

Authentication Section:
- List items with toggles and chevrons:
  - Passcode: "4-digit PIN enabled, Last changed: 30 days ago"
  - Biometrics: Toggle ON, "Face ID / Touch ID enabled"
  - Password: Warning icon, "Consider updating, Last changed: 90 days ago"
  - Two-Factor Authentication: "+12 pts" badge, "Not enabled, Recommended"

Session & Devices Section:
- Auto-Lock Timeout: "After 5 minutes"
- Active Sessions: "2 devices connected" with device names
- Privacy Screen: Toggle OFF, "Hide balances in app switcher"

Emergency Section:
- Red-tinted options:
  - Sign Out All Devices
  - Report Compromised Account
- Warning styling with immediate action indicators

Colors: Green for enabled (#00D09C), Orange for warnings (#FFB800), Red for critical (#FF4757)
```

### Prompt 4: Appearance Settings

```
Create an Appearance Settings screen for a fintech banking app.

Design specifications:
- Material Design 3 with theme customization
- Support for light/dark/system themes

Theme Selection:
- Three theme cards in horizontal layout:
  - Light: Sun icon, light background preview
  - Dark: Moon icon, dark background preview (selected with gradient border)
  - System: Sun/Moon combo icon
- Radio selection indicator below each

Live Preview Card:
- Mini app preview showing how theme looks
- Balance card mockup, transaction list preview
- Animated transition between themes

Scheduled Theme:
- Auto Dark Mode toggle with "Switch to dark mode at sunset"
- Custom Schedule option: "Dark: 6:00 PM - 7:00 AM"

Display Settings:
- Text Size: Slider from A- to A+ with "Medium" label
- Bold Text: Toggle switch
- Reduce Motion: Toggle with "Minimize animations" subtitle
- Increase Contrast: Toggle

Language & Region:
- App Language: "English (US)" with chevron
- Currency Format: "USD ($)" with chevron
- Date Format: "MM/DD/YYYY" with chevron

Style: Clean preferences layout, immediate visual feedback, accessibility-conscious
```

### Prompt 5: Privacy & Data Center

```
Create a Privacy & Data Center screen for a fintech banking app.

Design specifications:
- Material Design 3 with privacy-focused design
- Trust-building visual elements

Header Card:
- Gradient card with lock icon
- "Your Privacy Matters" headline
- "Control how your data is used and shared" subtitle

Your Data Section:
- Download Your Data: "Get a copy of all your information, Export as PDF, CSV, or JSON"
- Request Data Transfer: "Move your data to another provider"
- Data Storage: "Where your data is stored, EU Region, Encrypted"

Sharing Preferences:
- Toggle list items:
  - Analytics & Improvements: ON, "Help us improve the app, No personal data shared"
  - Personalized Offers: OFF
  - Marketing Communications: OFF
  - Third-Party Sharing: OFF

App Permissions:
- Permission status list:
  - Camera: "Granted" (green)
  - Location: "Denied" (red)
  - Notifications: "Granted" (green)
  - Biometrics: "Granted" (green)
- Chevrons to open system settings

Legal Section:
- Links: Privacy Policy, Cookie Policy, Terms of Service
- Simple list with chevrons

Style: Transparency-focused, user control emphasis, GDPR-compliant design
```

---

## Figma MCP Prompts

### Prompt 1: Command Center Hub Frame

```
Create a Figma frame for "Settings - Command Center Hub" mobile screen (375x812px).

Components needed:
1. Status bar (system)
2. Header: "Settings" title with gear icon

3. Profile Card Component:
   - Auto-layout vertical, 16dp padding
   - Gradient fill: #667EEA to #764BA2
   - Avatar (48dp) with completion ring overlay
   - Name, email, verified badge
   - Progress bar component with percentage

4. Quick Actions Component:
   - Auto-layout horizontal, gap 12dp
   - 4 icon button variants: Lock, Dark Mode, Mute, Export
   - Each: 56x56dp, surface color, 16dp radius

5. Security Health Card:
   - Score display with circular progress
   - Status checklist with colored icons
   - CTA button

6. Settings List Component:
   - Repeating list item: icon, label, subtitle, chevron
   - 72dp height per item
   - Divider between items

7. Sign Out Button:
   - Outlined style, error color
   - Full width

Design tokens to use:
- Colors: Primary (#667EEA), Secondary (#764BA2), Surface (#FFFFFF), Background (#F8F9FA)
- Typography: Title (20sp bold), Body (16sp), Caption (12sp)
- Spacing: 16dp standard, 24dp section gaps
- Radius: 16dp cards, 24dp buttons
```

### Prompt 2: Complete Settings Flow

```
Create a Figma prototype flow for Settings feature with these frames:

Frame 1: Command Center Hub (main)
- Profile card with completion progress
- Quick actions grid
- Security health card
- Settings list

Frame 2: Profile Dashboard
- Large avatar header
- Completion tracking
- Achievement badges
- Personal info form

Frame 3: Security Center
- Security score circle
- Authentication options
- Device sessions
- Emergency actions

Frame 4: Appearance Settings
- Theme picker with preview
- Display options
- Language settings

Frame 5: Privacy & Data
- Data export options
- Sharing toggles
- Permissions list

Prototype connections:
- Main -> each sub-screen via list item tap
- Back button returns to main
- Toggles show state changes

Component library:
- Create reusable toggle, list item, card, button components
- Define color styles and text styles
- Use auto-layout for responsive behavior
```

---

## Design Tokens

```json
{
  "feature": "settings",
  "version": "2.0",
  "colors": {
    "primary": "#667EEA",
    "secondary": "#764BA2",
    "surface": "#FFFFFF",
    "background": "#F8F9FA",
    "success": "#00D09C",
    "warning": "#FFB800",
    "error": "#FF4757",
    "textPrimary": "#1F2937",
    "textSecondary": "#6B7280"
  },
  "gradients": {
    "primary": "linear-gradient(135deg, #667EEA 0%, #764BA2 100%)",
    "security": "linear-gradient(135deg, #00D09C 0%, #38EF7D 100%)"
  },
  "typography": {
    "title": { "size": 20, "weight": "bold" },
    "subtitle": { "size": 16, "weight": "medium" },
    "body": { "size": 14, "weight": "regular" },
    "caption": { "size": 12, "weight": "regular" }
  },
  "spacing": {
    "xs": 4,
    "sm": 8,
    "md": 16,
    "lg": 24,
    "xl": 32
  },
  "radius": {
    "sm": 8,
    "md": 16,
    "lg": 24,
    "pill": 100
  },
  "elevation": {
    "card": "0 2px 8px rgba(0,0,0,0.08)",
    "modal": "0 8px 24px rgba(0,0,0,0.16)"
  }
}
```

---

## Screen Checklist

| Screen | Stitch Prompt | Figma Prompt | Status |
|--------|---------------|--------------|--------|
| Command Center Hub | 1 | 1, 2 | Ready |
| Profile Dashboard | 2 | 2 | Ready |
| Security Center | 3 | 2 | Ready |
| Appearance Settings | 4 | 2 | Ready |
| Privacy & Data Center | 5 | 2 | Ready |
| Notifications Hub | - | - | Pending |
| Help & Support | - | - | Pending |
| About | - | - | Pending |
