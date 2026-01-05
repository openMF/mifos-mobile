# Client Charges Feature - AI Design Tool Prompts

> **Generated from**: MOCKUP.md v2.0
> **Design Pattern**: Smart Charges Hub
> **Primary Gradient**: #667EEA (Purple) -> #764BA2 (Deep Purple)
> **Generated**: 2025-01-04

---

## Google Stitch Prompts

### Prompt 1: Smart Charges Dashboard

```
Create a Smart Charges Dashboard screen for a fintech banking app.

Design specifications:
- Material Design 3 with 2025 fintech patterns
- Primary gradient: #667EEA to #764BA2
- Gamified payment streak integration

Header:
- Back button, "My Charges" title
- Calendar icon, Settings icon

Outstanding Balance Card (Gradient):
- Background: Gradient #667EEA to #764BA2
- Large amount: "$225.00" (36sp bold white)
- Label: "OUTSTANDING BALANCE"
- Three stat boxes below:
  - Total: $500.00
  - Paid: $250.00 (green indicator)
  - Waived: $25.00 (yellow indicator)

Payment Streak Banner:
- Fire icon with "On-Time Streak: 5 payments"
- Progress bar: Showing 5/10 to next reward
- Text: "Next reward at 10!"
- Flame animation on icon

Quick Actions Row:
- 4 icon buttons in grid:
  - Pay All (credit card icon)
  - Autopay Setup (refresh icon)
  - Calendar View (calendar icon)
  - Insights (chart icon)
- Each: 56dp square, icon + label

Filter Tabs:
- Horizontal scroll: All, Loan, Savings, Share
- Selected tab underlined with gradient

Due Soon Section:
- Section header: "DUE SOON (2)" with "View" link
- Charge cards with urgency indicators

Charge Card (Due Soon):
- Left icon: Document with red status dot
- Title: "Loan Processing Fee"
- Account: "LOAN-001234"
- Due: "Tomorrow" with "$150.00 outstanding"
- Status chips: "PENALTY", "75% paid", "Due in 1 day"
- Action buttons: "Pay $150.00", "Split Payment"
- Background: White, shadow, 20dp radius

Recently Paid Section:
- Section header: "RECENTLY PAID (2)"
- Compact paid charge cards with green checkmark
- "+5 pts earned" badge

Style: Gamified payments, urgency indicators, quick actions
```

### Prompt 2: Due Date Calendar View

```
Create a Payment Calendar screen for viewing charge due dates in a fintech app.

Design specifications:
- Material Design 3 with calendar patterns
- Color-coded due dates

Header:
- Back button, "Payment Calendar" title

Calendar Component:
- Month navigation: "< January 2025 >"
- Week day headers: Mo Tu We Th Fr Sa Su
- Date grid with indicators:
  - Red dot: Overdue/Penalty
  - Yellow dot: Due Soon
  - Green dot: Upcoming
  - Checkmark: Paid
- Amount shown on relevant dates: "$150", "$50"
- Selected date highlighted with gradient circle

Legend:
- Color key: Overdue (red), Due Soon (yellow), Upcoming (green), Paid (check)

Selected Date Section:
- Date header: "JANUARY 15 - DUE"
- Charge cards for that date:
  - Loan Processing Fee, $150.00
  - "Pay Now" button

AI Tip Card:
- Light bulb icon
- "Based on your cash flow, the best day to pay the $150 charge is January 10th when your balance is highest."
- Action button: "Schedule Payment for Jan 10"

Monthly Summary (collapsible):
- Total due this month: $200
- Paid so far: $0
- Upcoming: $200

Style: Visual calendar, smart scheduling, AI recommendations
```

### Prompt 3: Charge Detail Screen

```
Create a Charge Detail screen with comprehensive payment options for a fintech app.

Design specifications:
- Material Design 3 with detailed charge information
- Multiple payment action options

Header (Gradient):
- Background: Gradient #667EEA to #764BA2
- Back button, "Charge Details" title, More menu
- Large icon with document
- Charge name: "LOAN PROCESSING FEE"
- Account: "Loan Account: LOAN-001234"
- Outstanding: "$150.00" (36sp bold white)
- Status chips: "PENALTY", "Due Tomorrow"

Amount Breakdown Card:
- Title: "AMOUNT BREAKDOWN"
- Rows with values:
  - Due Amount: $150.00
  - Amount Paid: $75.00 (green)
  - Amount Waived: $0.00 (yellow)
  - Outstanding: $75.00 (red)
- Payment Progress bar: 50% filled

Charge Details Card:
- Title: "CHARGE DETAILS"
- Info rows:
  - Charge ID: #123
  - Due Date: Jan 15, 2025
  - Calculation Type: Flat Rate
  - Charge Time Type: On Disbursement
  - Currency: USD
  - Is Penalty: Yes (red badge)

Payment History Timeline:
- Title: "PAYMENT HISTORY"
- Timeline entries:
  - Jan 10: Partial Payment, $75.00 paid (green)
  - Dec 28: Charge Created, $150.00 applied

Payment Options Section:
- Title: "PAYMENT OPTIONS"
- Primary button: "Pay Full Amount ($75.00)" (gradient)
- Secondary options:
  - "Split into 2 Payments of $37.50"
  - "Set Up Autopay for Future"
  - "Request Waiver"

Style: Comprehensive info, multiple payment paths, clear history
```

### Prompt 4: Autopay Setup Flow

```
Create an Autopay Setup wizard for charges in a fintech app.

Design specifications:
- Material Design 3 with step wizard
- 3-step setup process

Step 1: Select Charges
Header:
- Back button, "Set Up Autopay" title
- Step indicator: "Step 1 of 3" with 33% progress bar

Intro:
- Refresh icon
- Title: "Set Up Automatic Payments"
- Subtitle: "Never miss a payment. Autopay will automatically pay charges when due."

Charge Type Selection:
- Title: "SELECT CHARGES TO AUTOPAY"
- Checkbox cards:
  - All Future Loan Charges (checked)
    - "Includes: Processing fees, late fees, penalties"
    - "Estimated: ~$50-200/month"
  - All Future Savings Charges (checked)
    - "Includes: Maintenance fees, service charges"
    - "Estimated: ~$25/month"
  - All Future Client Charges (unchecked)
    - "Includes: Annual fees, document fees"
    - "Estimated: ~$100/year"

Continue Button: "Continue to Select Account"

Step 2: Select Account
- Progress: 66%
- Title: "PAY FROM ACCOUNT"
- Account cards (radio selection):
  - Primary Savings ****4521 (selected, recommended)
    - Available: $8,200.00
    - "Recommended - Highest balance"
  - Emergency Fund ****7832
    - Available: $2,500.00

Payment Timing:
- Radio options:
  - On Due Date (Recommended, selected)
  - 3 Days Before Due Date
  - 7 Days Before Due Date

Low Balance Protection:
- Checkbox: "Skip autopay if account balance is below $500"
- Subtitle: "You'll be notified to pay manually"

Continue Button: "Continue to Confirm"

Step 3: Confirmation
- Progress: 100%
- Summary Card:
  - Charges: Loan + Savings
  - Account: Savings ****4521
  - Timing: On Due Date
  - Protection: Skip if < $500

Benefits List:
- Never miss a payment
- Protect your payment streak
- Avoid late fees and penalties
- Earn autopay bonus points

Email Notice:
- "You'll receive email confirmation and reminders 24 hours before each autopay"

Enable Button: "Enable Autopay" (gradient)

Style: Guided setup, clear benefits, protection options
```

### Prompt 5: Split Payment Flow

```
Create a Split Payment screen for breaking charges into installments in a fintech app.

Design specifications:
- Material Design 3 with installment patterns
- Clear payment plan visualization

Header:
- Back button, "Split Payment" title

Charge Context:
- Document icon
- "Loan Processing Fee"
- "Original Amount: $150.00"

Payment Plan Selection:
- Title: "CHOOSE PAYMENT PLAN"

Plan Cards (radio selection):
Plan 1 - 2 Payments (selected):
- "$75.00 x 2"
- Timeline visualization: Jan 15 -> Feb 15
- Amounts shown: $75.00 each
- Badge: "No extra fees, Recommended"
- Gradient border when selected

Plan 2 - 3 Payments:
- "$50.00 x 3"
- Timeline: Jan 15 -> Feb 15 -> Mar 15
- "+$5.00 split fee" warning

Plan 3 - 4 Payments:
- "$37.50 x 4"
- Timeline: Jan 15 - Apr 15 (monthly)
- "+$10.00 split fee"

Payment Summary:
- Charge Amount: $150.00
- Split Fee: $0.00
- Total: $150.00
- First Payment Due: Jan 15, 2025

Account Selector:
- Credit card icon
- "Pay From: Savings ****4521"
- "Change >" link

Confirm Button:
- "Confirm Split Payment" (gradient)

Terms:
- Small text about split payment agreement

Style: Clear plan comparison, timeline visualization, fee transparency
```

### Prompt 6: Payment Success Screen

```
Create a Payment Success screen with streak update for a fintech app.

Design specifications:
- Material Design 3 with celebration patterns
- Gamification integration

Success Header:
- Large checkmark with animated circle
- Confetti animation background
- "Payment Success!" text

Amount Display:
- "$75.00"
- "Loan Processing Fee"

Transaction Details Card:
- Reference: TXN-2025-001234
- Date: Jan 15, 2025
- From Account: Savings ****4521
- Status: Completed (green)

Streak Update Banner (Gradient):
- Fire icon
- "STREAK UPDATED!"
- "Your on-time payment streak is now 6!"
- Progress bar: 6/10 to reward
- "+10 points earned for on-time payment"

Action Buttons:
- Download Receipt
- Share Receipt

Back Button:
- "Back to Charges"

Style: Celebratory, gamified rewards, clear confirmation
```

---

## Figma MCP Prompts

### Prompt 1: Charges Dashboard Frame

```
Create a Figma frame for "Charges - Smart Dashboard" mobile screen (375x812px).

Components needed:
1. Header: Back button, title, calendar and settings icons

2. Outstanding Balance Card:
   - Gradient background
   - Large amount display
   - Three stat boxes row

3. Payment Streak Banner:
   - Fire icon with animation indicator
   - Progress bar
   - Reward text

4. Quick Actions Grid:
   - 4 icon buttons: Pay All, Autopay, Calendar, Insights
   - Auto-layout 2x2 or 1x4

5. Filter Tabs:
   - Horizontal scroll
   - Selected/unselected states

6. Charge Card Component (create variants):
   - Due Soon (urgent styling)
   - Paid (success styling)
   - Overdue (error styling)
   - Status chips
   - Action buttons

7. Section Headers:
   - Title with count badge
   - View link

Design tokens:
- Primary: #667EEA
- Secondary: #764BA2
- Success: #00D09C
- Warning: #FFB800
- Error: #FF4757
```

### Prompt 2: Complete Charges Flow

```
Create a Figma prototype flow for Charges feature with these frames:

Frame 1: Smart Dashboard (main)
- Outstanding balance card
- Streak banner
- Quick actions
- Charge list

Frame 2: Calendar View
- Month calendar with indicators
- Selected date charges
- AI scheduling tip

Frame 3: Charge Detail
- Amount breakdown
- Charge info
- Payment history timeline
- Payment options

Frame 4-6: Autopay Setup Flow
- Step 1: Select charge types
- Step 2: Select account and timing
- Step 3: Confirmation

Frame 7: Split Payment
- Plan selection cards
- Timeline visualization
- Summary

Frame 8: Payment Success
- Celebration animation
- Streak update
- Receipt actions

Frame 9: Charge Insights
- Summary stats
- Charts
- Performance metrics

Prototype connections:
- Dashboard -> Detail via card tap
- Dashboard -> Calendar via quick action
- Pay button -> Success
- Autopay button -> Setup flow

Component variants:
- Charge card: due-soon/paid/overdue
- Status chip: penalty/autopay/due-date
- Plan card: selected/unselected
```

---

## Design Tokens

```json
{
  "feature": "client-charge",
  "version": "2.0",
  "designPattern": "Smart Charges Hub",
  "colors": {
    "primary": "#667EEA",
    "secondary": "#764BA2",
    "success": "#00D09C",
    "warning": "#FFB800",
    "error": "#FF4757",
    "surface": "#FFFFFF",
    "background": "#F8F9FA"
  },
  "status": {
    "paid": { "bg": "#00D09C15", "text": "#00D09C" },
    "pending": { "bg": "#FFB80015", "text": "#FFB800" },
    "overdue": { "bg": "#FF475715", "text": "#FF4757" },
    "penalty": { "bg": "#FF475715", "text": "#FF4757" },
    "autopay": { "bg": "#667EEA15", "text": "#667EEA" }
  },
  "components": {
    "balanceCard": {
      "height": "auto",
      "padding": 20,
      "radius": 24,
      "statBoxWidth": 100
    },
    "streakBanner": {
      "height": 80,
      "progressHeight": 6,
      "fireIconSize": 24
    },
    "chargeCard": {
      "minHeight": 120,
      "padding": 16,
      "radius": 20,
      "iconSize": 48,
      "gap": 12
    },
    "statusChip": {
      "height": 24,
      "padding": 8,
      "radius": 12,
      "fontSize": 11
    },
    "calendarDay": {
      "size": 40,
      "indicatorSize": 8,
      "selectedRadius": 20
    },
    "planCard": {
      "padding": 16,
      "radius": 16,
      "timelineHeight": 4
    },
    "progressBar": {
      "height": 6,
      "radius": 3
    }
  },
  "animation": {
    "streakFlame": {
      "duration": 2000,
      "type": "flicker"
    },
    "confetti": {
      "duration": 2000,
      "particles": 20
    },
    "progressFill": {
      "duration": 600,
      "easing": "decelerate"
    }
  }
}
```

---

## Screen Checklist

| Screen | Stitch Prompt | Figma Prompt | Status |
|--------|---------------|--------------|--------|
| Smart Dashboard | 1 | 1, 2 | Ready |
| Calendar View | 2 | 2 | Ready |
| Charge Detail | 3 | 2 | Ready |
| Autopay Setup | 4 | 2 | Ready |
| Split Payment | 5 | 2 | Ready |
| Payment Success | 6 | 2 | Ready |
| Charge Insights | - | 2 | Pending |
| Waiver Request | - | - | Pending |
| Empty State | - | - | Pending |
