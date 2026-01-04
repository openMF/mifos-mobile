# Home Dashboard - Google Stitch Prompts

> **Tool**: [Google Stitch](https://stitch.withgoogle.com)
> **Style**: Material Design 3, Premium Fintech
> **Format**: Copy each prompt block directly into Stitch

---

## Design System Reference

```
Primary Gradient: #667EEA → #764BA2
Secondary Gradient: #11998E → #38EF7D
Success/Credit: #00D09C
Error/Debit: #FF4757
Warning: #FFB800
Text Primary: #1F2937
Text Secondary: #6B7280
Surface: #FFFFFF
Screen Size: 393 x 852 pixels (Android)
```

---

## Screen 1: Home Dashboard Success

```
Mobile home dashboard screen, Material Design 3, fintech banking app, 393x852px

Top Bar:
- "Good Morning" greeting, 18sp bold, #1F2937
- "Maria" name below, 24sp bold
- Right side: notification bell with badge (3), avatar circle

Hero Balance Card:
- Full width, gradient #667EEA to #764BA2, 24dp radius
- "Total Balance" label white 70%, eye toggle icon
- "$12,450.00" balance 36sp white ExtraBold centered
- Two stat chips below: "+$2,450 This Month" green, "-$890 Expenses" red
- Semi-transparent white backgrounds on chips

Spending Analytics Section:
- "This Week" header with date range
- "$847.50" amount 24sp, "↓ 12%" change indicator green
- Line chart 80dp height, gradient fill 20% opacity
- X-axis: M T W T F S S labels
- Category chips horizontal scroll: Shopping $342, Food $215, Transport $145

Quick Actions Row:
- 5 circular buttons: Send, Request, QR, Cards, Freeze
- 56dp icon circles, #EADDFF background
- Icons 24dp, #667EEA color
- Labels 12sp below each

Send Again Section:
- "Send Again" header + "See All" link
- Horizontal scroll avatars: JD, SK, MR, AS, + New
- Avatar 48dp circles, name labels below
- Hash-based colors per user

My Accounts Section:
- "My Accounts" header + "View All" link
- Account cards with icon, name, balance, progress
- Primary Savings: wallet icon, $8,200, 82% progress bar
- Home Loan: bank icon, -$45,200 red, 28% progress, "Due: Jan 15"

Upcoming Bills Section:
- "Due This Week" header + "Manage" link
- Bill cards with urgency indicator left border 4dp
- Electricity: red border, "$142.50", "Due Tomorrow", Pay Now + Schedule buttons
- Rent: purple border, "$1,500", "Due Dec 31"

Savings Goals:
- Horizontal scroll goal cards 280dp width
- Progress ring 60% with emoji, "Vacation Fund", "$1,200 / $2,000"
- Fire emoji + "15 day streak" gamification

Recent Activity:
- "Recent Activity" header + "View All"
- Transaction items: icon circle, description, time, amount
- Amazon Prime: shopping icon pink, -$14.99 red
- Salary Deposit: briefcase teal, +$3,500.00 green

AI Assistant Card:
- Gradient border, robot icon
- "Need help with anything?" prompt
- Suggestion chips: "How much did I spend?", "Transfer to John"

Bottom Navigation:
- 64dp height, 4 tabs: Home (selected), Accounts, Transfer FAB, Profile
- FAB 56dp centered, gradient background, elevated -16dp
```

---

## Screen 2: Loading State

```
Mobile home screen loading skeleton, Material Design 3, 393x852px

Header:
- "Good Morning" text visible
- Name placeholder shimmer bar 120dp width

Hero Card Skeleton:
- Gradient card shape with shimmer
- Balance placeholder 200dp width centered
- Two stat chip placeholders below

Quick Actions:
- 5 circular shimmer placeholders 56dp each

Account Cards:
- Shimmer card shapes with icon circle placeholder
- Text lines shimmer 180dp, 120dp widths
- Balance shimmer 80dp right aligned

Shimmer animation: left-to-right sweep, 1000ms infinite
Gradient: #E1E4E8 → #F8F9FA → #E1E4E8
```

---

## Screen 3: Empty State

```
Mobile home empty state screen, Material Design 3, 393x852px

Header:
- Same greeting as success state
- Notification icon without badge

Hero Card:
- Same gradient
- "$0.00" balance centered
- Income $0.00, Expenses $0.00 chips

Quick Actions: Same as success

Center Content:
- Illustration 160dp, chart/document icon
- "No Accounts Yet" title 20sp bold
- "Start your financial journey by opening your first account" subtitle centered

CTA Button:
- "+ Open New Account" gradient button full width
- "Learn More" text link below
```

---

## Screen 4: Error State

```
Mobile home error state screen, Material Design 3, 393x852px

Header: Same greeting

Center Content:
- Warning triangle illustration 120dp, #FF4757 tint
- "Something Went Wrong" title 20sp bold
- "We couldn't load your account information. Please check your connection and try again."

Actions:
- "Try Again" gradient button full width
- "Contact Support" text link below
```

---

## Screen 5: Offline Banner

```
Mobile home with offline banner, Material Design 3, 393x852px

Offline Banner:
- 48dp height, #FFB800 15% background
- Antenna icon, "You're offline. Some features unavailable."

Content:
- All data at 70% opacity
- "Last sync: 2 min ago" indicator
- Pull-to-refresh: "Trying to reconnect..."
```

---

## Components

### Hero Balance Card
```
Financial summary card component, Material Design 3:
- Width: 361dp, height 200dp
- Gradient #667EEA to #764BA2, 24dp radius
- "Total Balance" white 12sp + eye toggle icon
- Balance 36sp white ExtraBold centered
- Income/Expense chips: semi-transparent white bg
- Income arrow up green, Expense arrow down red
- Shadow: 16dp blur, primary at 25%
```

### Quick Action Button
```
Quick action circular button, Material Design 3:
- Container 56dp square, 16dp radius
- Background: Surface + 8% Primary
- Icon 24dp centered, #667EEA
- Label below 12sp #1F2937
- Pressed: scale 0.95, ripple
- Haptic: light impact
```

### Account Card
```
Bank account card component, Material Design 3:
- Width 361dp, height 110dp auto
- White bg, 16dp radius, 2dp elevation
- Left: 44dp icon container, gradient 12% opacity
- Center: account name 16sp, number 12sp gray
- Right: balance 18sp, trend indicator
- Loan variant: progress bar 6dp below content
```

### Spending Chart
```
Weekly spending chart component, Material Design 3:
- Height 80dp, full width
- Line 2dp primary gradient
- Fill gradient 20% opacity below
- Day labels 10sp gray
- Touch: tooltip with amount
```

### Transaction Item
```
Transaction list item, Material Design 3:
- Height 72dp
- Left: 40dp category circle
  - Credit: #C8E6C9 bg, down arrow #2E7D32
  - Debit: #F9DEDC bg, up arrow #B3261E
- Center: description 16sp, merchant+time 12sp gray
- Right: amount colored (green/red)
```

---

## Quick Start

1. Open [stitch.withgoogle.com](https://stitch.withgoogle.com)
2. Create project "Mifos Mobile - Home Dashboard"
3. Copy each prompt block → Generate
4. Generate components separately for reuse
5. Export all to Figma when done
