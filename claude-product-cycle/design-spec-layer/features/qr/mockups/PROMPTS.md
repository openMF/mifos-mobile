# QR Code Feature - AI Design Tool Prompts

> **Generated from**: MOCKUP.md v2.0
> **Design Pattern**: Smart Pay Hub
> **Primary Gradient**: #667EEA (Purple) -> #764BA2 (Deep Purple)
> **Generated**: 2025-01-04

---

## Google Stitch Prompts

### Prompt 1: Smart Pay Hub (Scanner Mode)

```
Create a mobile Smart Pay Hub screen for a fintech banking app's QR feature.

Design specifications:
- Material Design 3 with 2025 fintech patterns
- Primary gradient: #667EEA to #764BA2 (purple-blue)
- Camera-first design with overlay

Header:
- Back button, "Pay" title, "History" action icon

Mode Selector:
- Horizontal tab bar with 3 modes: SCAN (selected), MY QR, REQUEST
- Selected tab: Gradient pill (#667EEA to #764BA2)
- Unselected: Transparent with gray text
- Container: #F0F4F8 pill background
- Height: 48dp, sliding indicator animation

Camera Preview Area:
- Full width minus 32dp margin
- Height: 320dp
- Overlay: Semi-transparent black 60%
- Cutout: 240x240dp clear scanning area
- Corner markers: 4dp gradient strokes at each corner
- Scan line: Animated gradient line moving vertically
- Rounded corners: 24dp

Instruction Text:
- "Point camera at QR code to scan"
- 14sp, #6B7280, centered

Quick Actions:
- Two buttons side by side:
  - Flashlight icon with "Flashlight" label
  - Gallery icon with "From Gallery" label
- Each: 56x56dp icon area + label below
- Background: #F8F9FA, corner radius: 16dp
- Gap: 24dp between buttons

Recent Scans Section:
- Section header: "RECENT SCANS"
- Horizontal scroll of recent contacts:
  - Avatar (48dp circle) with initials
  - Amount badge below: "$50", "$25", "$100"
  - Name truncated below
  - Last item: "+" icon for "New Scan"

Style: Camera-focused, quick actions prominent, social payment history
```

### Prompt 2: My QR Code Screen (Receive Money)

```
Create a My QR Code screen for receiving money in a fintech app.

Design specifications:
- Material Design 3 with premium QR card design
- Gradient QR card as hero element

Header:
- Back button, "Pay" title, "History" action

Mode Selector:
- Same tabs: SCAN, MY QR (selected), REQUEST
- MY QR tab highlighted with gradient

Title:
- "Share to Receive Money" (16sp bold, centered)

QR Card (Premium Design):
- Full-width card with gradient background: #667EEA to #764BA2
- Corner radius: 28dp
- Shadow: 0 12 40 #667EEA@35%
- Padding: 24dp

QR Container inside card:
- White background
- Corner radius: 16dp
- Padding: 16dp
- QR code: 220dp
- QR modules: Black on white

User Info (inside gradient card, below QR):
- Small info card: White @15% opacity, 12dp radius
- Avatar (32dp) + Name + @username

Account Selector:
- Dropdown card below QR card
- Icon: Briefcase
- Label: "Receive Into"
- Account: "Savings Account ****4521"
- Balance: "$8,542.00"
- Chevron dropdown indicator
- Background: #F8F9FA, border: 1dp #E1E4E8

Optional Amount Section:
- Label: "Request Specific Amount (Optional)"
- Large input: "$ 0.00" (32sp bold)
- Quick amount chips: +10, +50, +100, +500
- Chips: Outlined style, 40dp height

Action Buttons:
- Two buttons side by side (48% width each):
  - Share: Gradient filled, share icon
  - Save: Outlined #667EEA, save icon
- Height: 52dp

Style: Premium feel, shareable design, flexible amount request
```

### Prompt 3: Request Money (Split Bill Mode)

```
Create a Request Money screen with split bill functionality for a fintech app.

Design specifications:
- Material Design 3 with social payment patterns
- Multi-person selection for split payments

Header:
- Back button, "Pay" title, "History" action

Mode Selector:
- REQUEST tab selected with gradient

Title:
- "Request Money from Friends" (16sp bold, centered)

Amount Entry:
- Large gradient text amount: "$75.00" (48sp bold)
- Quick add chips: +10, +25, +50, +100

Description Field:
- Label: "What's this for? (Optional)"
- Input with emoji support: "Dinner at The Grill"
- Quick tags row: Food, Fun, Rent, Other (with emojis)

Friends Selection Section:
- Header: "SELECT FRIENDS TO REQUEST FROM"
- Subheader: "3 selected, $25.00 each" (auto-calculated split)

Friend Cards (checkable list):
- Selected friends (3):
  - Checkbox filled with gradient
  - Avatar with initials
  - Name and @username
  - "Recent" or "Frequent" badge
  - Amount per person: "$25.00"
  - Card background: Gradient @10%, gradient border 2dp

- Unselected friend:
  - Empty checkbox
  - Avatar, name, username
  - No amount shown

Card styling:
- Height: 72dp
- Padding: 16dp
- Radius: 16dp

Primary Button:
- "Send Request to 3 Friends" (gradient filled)
- Full width
- Height: 52dp

Style: Social, fun split bill experience, clear per-person amounts
```

### Prompt 4: Scan Success - Payment Preview

```
Create a Payment Preview screen after successful QR scan for a fintech app.

Design specifications:
- Material Design 3 with success confirmation patterns
- Trust indicators and payment details

Success Header (Gradient):
- Background: Success gradient #00D09C to #38EF7D
- Height: 180dp
- Centered check icon (64dp) with draw animation
- Text: "QR Code Verified"

Recipient Card:
- Avatar with verified badge
- Name: "Jane Doe"
- Organization: "Mifos Head Office"
- Trust badge: Green "Verified Account, Safe to pay"

Payment Details Card:
- Background: #F8F9FA
- Corner radius: 16dp
- Rows with dividers:
  - Account Type: Savings Account
  - Account Number: SA-0001234567
  - Office/Branch: Mifos Head Office
- Row height: 48dp each

Amount Section:
- "Requested: $150.00" (28sp bold)
- Amount chips: $50, $100, $150 (selected with gradient border), Custom
- Selected chip: Scale 1.05, gradient border

Security Footer:
- Lock icon + "256-bit encrypted, Instant transfer"
- Subtle gray text

Primary Button:
- "Pay $150.00 Now" with arrow
- Gradient filled
- Full width

Secondary Action:
- "+ Add as Beneficiary" link below button

Style: Trust-focused, clear confirmation, secure payment feel
```

### Prompt 5: Dynamic QR with Timer

```
Create a Dynamic QR screen with expiration timer for a fintech app.

Design specifications:
- Material Design 3 with time-sensitive design
- Security-focused dynamic QR

Header:
- Back button, "Payment Request" title

Main QR Card (Gradient):
- Background: Gradient #667EEA to #764BA2
- QR code in white container
- Amount displayed: "$150.00" (24sp bold, white)

Timer Component:
- Inside gradient card
- Clock icon + "Expires in 4:32"
- Progress bar: Decreasing from left to right
- Bar color: White, track: White @20%
- Warning state (<1 min): Orange color
- Expired state: Gray with "Regenerate" button

Request Details Card:
- White card below
- Rows:
  - From: "John Doe"
  - For: "Dinner at The Grill"
  - Created: "2 minutes ago"

Action Buttons:
- Two buttons side by side:
  - Regenerate: Outlined with refresh icon
  - Share: Gradient filled with share icon

Security Note:
- Shield icon + "Dynamic QR expires for your protection"
- Subtle gray text

Style: Security-focused, time-sensitive urgency, professional
```

---

## Figma MCP Prompts

### Prompt 1: Smart Pay Hub Frame

```
Create a Figma frame for "QR - Smart Pay Hub" mobile screen (375x812px).

Components needed:
1. Status bar (system)
2. Header: Back button, "Pay" title, History icon

3. Mode Selector Component:
   - Auto-layout horizontal
   - Container: Pill shape, #F0F4F8 background
   - 3 tabs: SCAN, MY QR, REQUEST
   - Selected variant: Gradient pill indicator
   - Height: 48dp

4. Camera Preview Component:
   - Dark overlay with clear cutout
   - Corner markers (4 corners) with gradient stroke
   - Scan line animation (horizontal gradient line)
   - Rounded corners: 24dp

5. Quick Action Buttons:
   - Icon + label vertical stack
   - 56dp icon area
   - Background: #F8F9FA

6. Recent Scans Component:
   - Horizontal scroll
   - Avatar circles with amount badges
   - Last item: Add new

Design tokens:
- Primary: #667EEA
- Secondary: #764BA2
- Surface: #FFFFFF
- Overlay: #000000 60%
```

### Prompt 2: Complete QR Flow

```
Create a Figma prototype flow for QR feature with these frames:

Frame 1: Scanner Mode (main)
- Camera preview with overlay
- Mode selector on SCAN
- Quick actions
- Recent scans

Frame 2: My QR Code
- Mode selector on MY QR
- Premium QR card with gradient
- Account selector
- Optional amount input
- Share/Save buttons

Frame 3: Request Money
- Mode selector on REQUEST
- Amount entry
- Description field
- Friend multi-select list
- Send request button

Frame 4: Scan Success
- Success header animation
- Recipient card with trust badge
- Payment details
- Amount selection
- Pay button

Frame 5: Dynamic QR
- Timer countdown
- Gradient QR card
- Request details
- Regenerate/Share buttons

Frame 6: Payment Success
- Confetti animation placeholder
- Receipt details
- Share/Done buttons

Frame 7: QR History
- Stats card
- Transaction list
- Filter options

Prototype connections:
- Mode selector switches between frames 1-3
- Scan success leads to frame 4
- Payment completes to frame 6

Component variants:
- Mode selector: scan/myqr/request active states
- Amount chip: selected/unselected
- Friend card: selected/unselected
```

---

## Design Tokens

```json
{
  "feature": "qr",
  "version": "2.0",
  "designPattern": "Smart Pay Hub",
  "colors": {
    "primary": "#667EEA",
    "secondary": "#764BA2",
    "success": "#00D09C",
    "successSecondary": "#38EF7D",
    "surface": "#FFFFFF",
    "background": "#F8F9FA",
    "overlay": "rgba(0,0,0,0.6)",
    "cameraBackground": "#000000"
  },
  "components": {
    "modeSelector": {
      "height": 48,
      "containerRadius": 24,
      "tabPadding": 16,
      "indicatorRadius": 20
    },
    "cameraPreview": {
      "height": 320,
      "cutoutSize": 240,
      "cornerMarkerLength": 32,
      "cornerMarkerStroke": 4,
      "radius": 24
    },
    "qrCard": {
      "padding": 24,
      "radius": 28,
      "qrSize": 220,
      "qrPadding": 16,
      "qrContainerRadius": 16
    },
    "amountChip": {
      "height": 40,
      "padding": 16,
      "radius": 20,
      "gap": 8
    },
    "friendCard": {
      "height": 72,
      "padding": 16,
      "radius": 16,
      "avatarSize": 40,
      "checkboxSize": 24
    },
    "timer": {
      "progressHeight": 4,
      "warningThreshold": 60
    }
  },
  "animation": {
    "scanLine": {
      "duration": 2000,
      "type": "loop"
    },
    "modeSwitch": {
      "duration": 300,
      "easing": "ease-out"
    },
    "successCheck": {
      "duration": 600,
      "type": "draw-stroke"
    }
  }
}
```

---

## Screen Checklist

| Screen | Stitch Prompt | Figma Prompt | Status |
|--------|---------------|--------------|--------|
| Smart Pay Hub (Scanner) | 1 | 1, 2 | Ready |
| My QR Code | 2 | 2 | Ready |
| Request Money | 3 | 2 | Ready |
| Scan Success | 4 | 2 | Ready |
| Dynamic QR Timer | 5 | 2 | Ready |
| Payment Success | - | 2 | Pending |
| QR History | - | 2 | Pending |
