# Transfer/Pay - Google Stitch Prompts

> **Tool**: [Google Stitch](https://stitch.withgoogle.com)
> **Style**: Material Design 3, Modern P2P Payments Fintech
> **Format**: Copy each prompt block directly into Stitch

---

## Design System Reference

```
Primary Gradient: #667EEA -> #764BA2
Secondary Gradient: #11998E -> #38EF7D
Success/Credit: #00D09C
Error/Debit: #FF4757
Warning: #FFB800
Text Primary: #1F2937
Text Secondary: #6B7280
Surface: #FFFFFF
Screen Size: 393 x 852 pixels (Android)
```

---

## Screen 1: Payment Hub (Main Entry)

```
Mobile payment hub, Material Design 3, P2P fintech, 393x852px

Top Bar:
- Back arrow, no title
- "History" text button right

Mode Selector:
- Three pill tabs: Send (selected gradient), Request, Split
- Icons: money send, money request, people split

From Account Card:
- Primary gradient full width
- "From" label, "Change" dropdown
- Bank icon, account name "Primary Savings"
- Account masked "****4521"
- "Available: $8,200.00" with eye toggle

Recent Recipients Section:
- "RECENT" header, "See All >" link
- Horizontal scroll avatars 72dp
- Each: avatar, name, last amount, time ago
- Add new "+" at end

To Section:
- "TO" label
- Search input: "Name, phone, email, or account..."
- "OR" divider
- Three action cards: Contacts (phone), Scan QR (camera), Payment Link (link)

Suggested Section:
- Lightbulb icon "SUGGESTED"
- Smart suggestion card: "You usually send John $50 on Fridays"
- Gradient "Quick Send: $50 to John" button

Pending Request Card:
- "Maria requested $120 for dinner"
- "3 hours ago"
- Two buttons: "Decline" outline, "Pay $120" gradient

Bottom Navigation:
- Home, Accounts, FAB (gradient), Profile
```

---

## Screen 2: Send Money - Recipient Selected

```
Mobile send money recipient selected, Material Design 3, 393x852px

Top Bar:
- Back arrow, "Pay John" title

Recipient Card:
- Centered avatar 80dp
- Name "John Doe"
- Username "@johndoe"

Amount Display:
- Large centered "$ 0" placeholder
- Underline
- "Tap to enter" helper

Quick Amounts:
- "QUICK AMOUNTS" label
- Four chips: $10, $25, $50, $100

History-Based Amounts:
- "BASED ON HISTORY" label
- Two chips: "$50 Your usual amount", "$35 Last payment"

Note Section:
- "WHAT'S IT FOR?" label
- Input: "Add a note..." with emoji button
- Recent notes with John: emoji chips (Pizza, Coffee, Movie, Uber)

Continue Button (disabled):
- Full width gray "Enter an amount"
```

---

## Screen 3: Send Money - Amount Entered

```
Mobile send money with amount, Material Design 3, 393x852px

Top Bar:
- Back arrow, "Pay John" title

Recipient Card:
- Centered avatar 80dp
- Name "John Doe"

Amount Display:
- Large "$ 50.00" gradient underlined
- "Remaining: $8,150.00" helper

Note Input (filled):
- Pizza emoji "Pizza night!" with clear button

Number Keypad:
- 3x4 grid: 1-9, decimal, 0, backspace
- 64dp height buttons

Pay Button:
- Full width gradient
- "Pay $50.00" with money icon
- "SWIPE UP TO PAY" hint below
```

---

## Screen 4: Request Money Mode

```
Mobile request money, Material Design 3, P2P fintech, 393x852px

Top Bar:
- Back arrow, "History" right

Mode Selector:
- Send, Request (selected gradient), Split tabs

Request From Section:
- Search input "Search people..."
- "SELECTED (2)" label
- Chips: "John Doe" with X, "Maria Santos" with X

Amount Section:
- "AMOUNT TO REQUEST (Total)" label
- Large "$100.00" centered
- "Split: $50.00 each (2 people)" helper

Split Options:
- Two radio buttons: "Split Equally", "Custom Split" (selected)

Custom Amounts:
- John Doe: $60.00 editable
- Maria Santos: $40.00 editable
- Divider, "Total: $100.00"

Note Input:
- "Dinner at Italian Place" with pasta emoji

Request Button:
- Gradient "Request $100.00 (2 people)"

Bottom Navigation
```

---

## Screen 5: Split Bill Mode

```
Mobile split bill, Material Design 3, P2P fintech, 393x852px

Top Bar:
- Back arrow, "Split Bill" title

Total Bill Section:
- "TOTAL BILL" label
- Large "$156.50" centered editable
- "Scan receipt to auto-fill" camera button

Splitting With Section:
- "SPLITTING WITH (4 people)" header, "+ Add Person"
- Person cards with sliders:
  - "You (Paying now)": $39.13, 25% bar
  - "John Doe": $39.13, 25%, "Request will be sent"
  - "Maria Santos": $39.12, 25%
  - "Alex Kumar": $39.12, 25%

Split Method:
- "Split Method" label with balance icon
- Four radio chips: Equal (selected), Custom, By Item, % Based

Note Input:
- Pizza emoji "Dinner at Pizza Palace"

Split Button:
- Gradient "Pay $39.13 & Request from 3 people"
```

---

## Screen 6: Schedule Recurring Payment

```
Mobile schedule recurring payment, Material Design 3, 393x852px

Top Bar:
- Back arrow, "Schedule Payment" title

Recipient & Amount Card:
- "TO" section with avatar "John Doe", "@johndoe"
- "AMOUNT" section: "$50.00" large

Frequency Section:
- "SCHEDULE" header
- "Frequency" label
- Four radio chips: Once, Weekly (selected), Monthly, Custom

Day of Week Section:
- "Day of Week" label
- Seven day chips: S M T W T F S
- Friday selected with gradient

Date Range:
- Start Date picker: "Jan 3, 2025"
- End Date picker: "No end date"

Notifications:
- "Notification Preferences" header
- Checkboxes: Notify before payment (checked), Notify if fails (checked), Email receipt (unchecked)

Note Input:
- Pizza emoji "Weekly pizza fund"

Summary Card:
- "$50.00 every Friday starting Jan 3, 2025"
- "Next payment: Jan 3, 2025"

Schedule Button:
- Gradient "Schedule Recurring Payment" with calendar icon
```

---

## Screen 7: Payment Confirmation (Swipe to Pay)

```
Mobile payment confirmation swipe, Material Design 3, 393x852px

Top Bar:
- Close X only

Payment Summary Card:
- Centered large avatar "John Doe"
- "$50.00" prominent
- Pizza emoji "Pizza night!" note

Transaction Details:
- From: Primary Savings ****4521
- To: John Doe @johndoe
- Date: Dec 30, 2025

Swipe to Pay Zone:
- Gradient track with thumb icon
- "SWIPE TO PAY" text with arrows
- Instruction: "Swipe right to confirm payment"
- Security: Lock icon "Secured with Face ID / Touch ID"
```

---

## Screen 8: Success State (Animated)

```
Mobile payment success, Material Design 3, 393x852px

Top Bar:
- "Done" text button right only

Confetti Animation:
- Colorful confetti particles

Success Icon:
- Large green circle
- Animated checkmark drawing in

Success Message:
- "Payment Sent!" 28sp bold

Transaction Card:
- Avatar thumbnail
- "$50.00 -> John Doe"
- Pizza emoji "Pizza night!"

Transaction Details:
- Transaction ID: TXN-2025123001234567
- Date & Time: Dec 30, 2025 • 10:45 AM
- From: Primary Savings ****4521

Action Buttons Row:
- "Share" outline with share icon
- "Receipt" outline with download icon

Primary CTA:
- Gradient "Pay Again" full width
```

---

## Screen 9: Payment History / Activity

```
Mobile payment history, Material Design 3, P2P fintech, 393x852px

Top Bar:
- Back arrow, "Payment History" title
- Filter dropdown

Search Input:
- "Search payments..."

Filter Tabs:
- All (selected), Sent, Received, Pending, Scheduled

TODAY Section:
- Transaction cards:
  - Avatar, "You paid John Doe", "-$50.00", pizza emoji, time, "Complete" green badge
  - Avatar, "Maria paid you", "+$120.00", pasta emoji, time, "Complete"

YESTERDAY Section:
- Avatar, "You requested from Alex", "$35.00", coffee emoji, "Pending" amber
- "Remind" and "Cancel" buttons
- Avatar, "You paid Sarah Lee", "-$200.00", house emoji, "Complete"

SCHEDULED Section:
- Recurring icon, "Weekly to John Doe", "$50.00"
- Pizza emoji, "Every Friday", "Scheduled" badge
- "Next: Jan 3, 2025", Edit and Cancel buttons

Bottom Navigation
```

---

## Screen 10: QR Scan Screen

```
Mobile QR scanner, Material Design 3, 393x852px

Top Bar:
- Back arrow, "Scan to Pay" title
- Flashlight toggle icon

Instruction:
- "Point your camera at a payment QR code"

Camera Viewfinder:
- Full width camera preview
- Scanning frame overlay with animated corners
- Gradient corner brackets

Bottom Actions:
- Three buttons row: "My QR" (camera), "Gallery" (image), "NFC" (contactless)
```

---

## Screen 11: My QR Code (Receive)

```
Mobile my QR code, Material Design 3, 393x852px

Top Bar:
- Back arrow, "My QR Code" title
- Share icon right

Profile Section:
- Centered avatar
- Name "Your Name"
- Username "@username"

QR Code Card:
- Large white card with QR code
- Pattern: standard QR with user data encoded

Amount Input:
- "Set amount to receive" label
- "$0.00" editable input
- Helper: "Anyone who scans can pay you this amount"

Action Buttons:
- "Copy Link" with clipboard icon
- "Save Image" with download icon
```

---

## Components

### ModeTabSelector
```
Mode tab selector, Material Design 3:
- Height: 48dp
- Three pill tabs: Send, Request, Split
- Selected: gradient background, white text
- Unselected: surface, secondary text
- Icons: 20dp before text
- Touch target: full pill
```

### RecentRecipientCard
```
Recent recipient card, Material Design 3:
- Width: 72dp, Height: 96dp
- Avatar: 48dp circle with gradient background
- Name: 12sp Medium, max 8 chars
- Amount: 11sp Regular, secondary
- Time: 10sp Regular, tertiary
- Tap: opens pre-filled payment
```

### AmountInputDisplay
```
Amount input display, Material Design 3:
- Amount: 44sp ExtraBold, primary/error
- Currency: 28sp Regular, 60% opacity
- Underline: 2dp gradient
- Remaining balance: 14sp secondary, live update
```

### QuickAmountChip
```
Quick amount chip, Material Design 3:
- Regular: 40dp height, outlined
- History-based: 48dp, two lines, tinted background
- Selected: gradient fill, white text
- Amount: 16sp Medium
- Label: 12sp Secondary
```

### SwipeToPayButton
```
Swipe to pay button, Material Design 3:
- Height: 64dp, radius: 32dp
- Track: gradient background, shimmer
- Thumb: 48dp white circle, arrow icon
- Threshold: 80% width
- Complete: checkmark morph, haptic
```

### NoteInputWithEmoji
```
Note input with emoji, Material Design 3:
- Height: 48dp, radius: 12dp
- Background: surface variant
- Emoji button: opens picker
- Clear button: visible when filled
- Recent notes: chips below
```

---

## Quick Start

1. Open [stitch.withgoogle.com](https://stitch.withgoogle.com)
2. Create project "Mifos Mobile - Transfer"
3. Copy each screen prompt -> Generate
4. Generate components separately for reuse
5. Export all to Figma when done
