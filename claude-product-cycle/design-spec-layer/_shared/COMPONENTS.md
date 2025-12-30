# Mifos Mobile - Design System & Component Library

> **Version**: 2.0 (Revolut-Style Vibrant Design)
> **Last Updated**: 2025-12-29
> **Design Philosophy**: Modern Fintech with Trust, Clarity, Speed

---

## 1. Color System

### 1.1 Primary Palette

```
┌─────────────────────────────────────────────────────────────────┐
│                        COLOR TOKENS                              │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  PRIMARY GRADIENTS                                               │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐          │
│  │   #667EEA    │  │   #11998E    │  │   #FC466B    │          │
│  │      ↓       │  │      ↓       │  │      ↓       │          │
│  │   #764BA2    │  │   #38EF7D    │  │   #3F5EFB    │          │
│  │  Purple-Blue │  │  Teal-Green  │  │  Pink-Blue   │          │
│  │   (Primary)  │  │ (Secondary)  │  │   (Accent)   │          │
│  └──────────────┘  └──────────────┘  └──────────────┘          │
│                                                                  │
│  SEMANTIC COLORS                                                 │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐          │
│  │   #00D09C    │  │   #FFB800    │  │   #FF4757    │          │
│  │   Success    │  │   Warning    │  │    Error     │          │
│  │  (Teal/Mint) │  │   (Amber)    │  │  (Coral Red) │          │
│  └──────────────┘  └──────────────┘  └──────────────┘          │
│                                                                  │
│  BACKGROUNDS                                                     │
│  ┌──────────────────────────────────────────────────────┐       │
│  │  Light Mode          │  Dark Mode                    │       │
│  │  ─────────────       │  ─────────────                │       │
│  │  Background: #F8F9FA │  Background: #0D1117          │       │
│  │  Surface:    #FFFFFF │  Surface:    #1A1F2E          │       │
│  │  Card:       #FFFFFF │  Card:       #21262D          │       │
│  │  Divider:    #E1E4E8 │  Divider:    #30363D          │       │
│  └──────────────────────────────────────────────────────┘       │
│                                                                  │
│  TEXT COLORS                                                     │
│  ┌──────────────────────────────────────────────────────┐       │
│  │  Light Mode          │  Dark Mode                    │       │
│  │  ─────────────       │  ─────────────                │       │
│  │  Primary:   #1F2937  │  Primary:   #F0F6FC           │       │
│  │  Secondary: #6B7280  │  Secondary: #8B949E           │       │
│  │  Tertiary:  #9CA3AF  │  Tertiary:  #6E7681           │       │
│  │  Disabled:  #D1D5DB  │  Disabled:  #484F58           │       │
│  └──────────────────────────────────────────────────────┘       │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### 1.2 Usage Guidelines

| Context | Light Mode | Dark Mode | Gradient |
|---------|------------|-----------|----------|
| Balance Cards | #FFFFFF | #1A1F2E | Primary Gradient background |
| Income Indicator | #00D09C | #00D09C | - |
| Expense Indicator | #FF4757 | #FF4757 | - |
| CTA Buttons | - | - | Primary Gradient |
| Quick Actions | #667EEA | #667EEA | - |
| Links | #667EEA | #764BA2 | - |

---

## 2. Typography

### 2.1 Type Scale

```
┌─────────────────────────────────────────────────────────────────┐
│                      TYPOGRAPHY SCALE                            │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  DISPLAY                                                         │
│  ════════════════════════════════════════════════════════════   │
│  Display Large    48sp / Bold / -0.5 letter spacing             │
│  Display Medium   40sp / Bold / -0.5 letter spacing             │
│  Display Small    32sp / Bold / 0 letter spacing                │
│                                                                  │
│  AMOUNT (Financial Values)                                       │
│  ════════════════════════════════════════════════════════════   │
│  Amount Large     36sp / ExtraBold / -0.5 letter spacing        │
│  Amount Medium    28sp / Bold / 0 letter spacing                │
│  Amount Small     20sp / SemiBold / 0 letter spacing            │
│                                                                  │
│  HEADLINES                                                       │
│  ════════════════════════════════════════════════════════════   │
│  Headline Large   24sp / Bold / 0 letter spacing                │
│  Headline Medium  20sp / SemiBold / 0.15 letter spacing         │
│  Headline Small   18sp / Medium / 0.15 letter spacing           │
│                                                                  │
│  BODY                                                            │
│  ════════════════════════════════════════════════════════════   │
│  Body Large       16sp / Regular / 0.5 letter spacing           │
│  Body Medium      14sp / Regular / 0.25 letter spacing          │
│  Body Small       12sp / Regular / 0.4 letter spacing           │
│                                                                  │
│  LABELS                                                          │
│  ════════════════════════════════════════════════════════════   │
│  Label Large      14sp / Medium / 0.1 letter spacing            │
│  Label Medium     12sp / Medium / 0.5 letter spacing            │
│  Label Small      10sp / Medium / 0.5 letter spacing            │
│                                                                  │
│  FONT FAMILY: Inter (Primary) / System Default (Fallback)       │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

## 3. Spacing & Layout

### 3.1 Spacing Scale

```
┌─────────────────────────────────────────────────────────────────┐
│                       SPACING TOKENS                             │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  Base Unit: 4dp                                                  │
│                                                                  │
│  ┌────────────────────────────────────────────────────────────┐ │
│  │  Token          │  Value   │  Usage                        │ │
│  ├────────────────────────────────────────────────────────────┤ │
│  │  space-0        │  0dp     │  No spacing                   │ │
│  │  space-1        │  4dp     │  Tight spacing (icons)        │ │
│  │  space-2        │  8dp     │  Small gaps                   │ │
│  │  space-3        │  12dp    │  Default element spacing      │ │
│  │  space-4        │  16dp    │  Card padding                 │ │
│  │  space-5        │  20dp    │  Section spacing              │ │
│  │  space-6        │  24dp    │  Large section gaps           │ │
│  │  space-8        │  32dp    │  Screen padding horizontal    │ │
│  │  space-10       │  40dp    │  Major section breaks         │ │
│  │  space-12       │  48dp    │  Hero card padding            │ │
│  │  space-16       │  64dp    │  Bottom nav height            │ │
│  └────────────────────────────────────────────────────────────┘ │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### 3.2 Layout Grid

```
┌─────────────────────────────────────────────────────────────────┐
│                        LAYOUT GRID                               │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  Screen Margins: 16dp (compact) / 24dp (expanded)                │
│  Content Width: 100% - 32dp (16dp each side)                     │
│  Card Margins: 16dp horizontal                                   │
│  Card Padding: 16dp internal                                     │
│  Card Gap: 12dp between cards                                    │
│                                                                  │
│  ┌────────────────────────────────────────────────────────────┐ │
│  │  16dp │◀───────── Content Area ─────────▶│ 16dp           │ │
│  │  ├────┼──────────────────────────────────┼────┤           │ │
│  │  │    │  ┌────────────────────────────┐  │    │           │ │
│  │  │    │  │         CARD 1             │  │    │           │ │
│  │  │    │  │      (16dp padding)        │  │    │           │ │
│  │  │    │  └────────────────────────────┘  │    │           │ │
│  │  │    │            12dp gap               │    │           │ │
│  │  │    │  ┌────────────────────────────┐  │    │           │ │
│  │  │    │  │         CARD 2             │  │    │           │ │
│  │  │    │  └────────────────────────────┘  │    │           │ │
│  │  │    │                                  │    │           │ │
│  └────────────────────────────────────────────────────────────┘ │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

## 4. Components

### 4.1 HeroBalanceCard

The main balance display at the top of dashboard screens.

```
┌─────────────────────────────────────────────────────────────────┐
│  HERO BALANCE CARD                                               │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░│  │
│  │░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░│  │
│  │░░  💰 Total Balance                           👁 ░░░░░░│  │
│  │░░                                                    ░░░░░│  │
│  │░░  $12,450.00                                       ░░░░░│  │
│  │░░                                                    ░░░░░│  │
│  │░░  ┌───────────┐  ┌───────────┐                    ░░░░░│  │
│  │░░  │  ▲ +$450  │  │  ▼ -$120  │                    ░░░░░│  │
│  │░░  │  Income   │  │  Expense  │                    ░░░░░│  │
│  │░░  └───────────┘  └───────────┘                    ░░░░░│  │
│  │░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░│  │
│  └───────────────────────────────────────────────────────────┘  │
│                                                                  │
│  Props:                                                          │
│  ─────────────────────────────────────────────────────────────  │
│  • balance: String           - Main balance amount               │
│  • currency: String          - Currency symbol ($, €, etc.)      │
│  • income: String            - Monthly income                    │
│  • expense: String           - Monthly expense                   │
│  • isAmountVisible: Boolean  - Toggle balance visibility         │
│  • onVisibilityToggle: ()    - Eye icon callback                 │
│  • gradientColors: List      - Background gradient               │
│                                                                  │
│  Dimensions:                                                     │
│  ─────────────────────────────────────────────────────────────  │
│  • Height: 180dp                                                 │
│  • Corner Radius: 24dp                                           │
│  • Padding: 24dp                                                 │
│  • Gradient: Primary (Purple-Blue diagonal)                      │
│  • Shadow: 8dp blur, 4dp offset, 15% opacity                     │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### 4.2 QuickActionBar

Horizontal scrollable quick action buttons.

```
┌─────────────────────────────────────────────────────────────────┐
│  QUICK ACTION BAR                                                │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │                                                          │   │
│  │   ┌─────┐   ┌─────┐   ┌─────┐   ┌─────┐   ┌─────┐      │   │
│  │   │ 💸  │   │ 📥  │   │ 📱  │   │ 📊  │   │ 🎯  │      │   │
│  │   │     │   │     │   │     │   │     │   │     │      │   │
│  │   └─────┘   └─────┘   └─────┘   └─────┘   └─────┘      │   │
│  │    Send    Request    QR Code  Analytics   Goals        │   │
│  │                                                          │   │
│  └──────────────────────────────────────────────────────────┘   │
│                                                                  │
│  Props:                                                          │
│  ─────────────────────────────────────────────────────────────  │
│  • actions: List<QuickAction>                                    │
│    - icon: ImageVector                                           │
│    - label: String                                               │
│    - onClick: () -> Unit                                         │
│    - badge: String? (optional notification count)                │
│                                                                  │
│  Dimensions:                                                     │
│  ─────────────────────────────────────────────────────────────  │
│  • Icon Container: 56dp x 56dp                                   │
│  • Icon Size: 24dp                                               │
│  • Corner Radius: 16dp                                           │
│  • Gap Between Items: 16dp                                       │
│  • Label: 12sp, Medium weight                                    │
│  • Background: Surface color with 8% primary tint                │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### 4.3 AccountCard

Compact account summary card with progress indicator.

```
┌─────────────────────────────────────────────────────────────────┐
│  ACCOUNT CARD                                                    │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │                                                           │  │
│  │   ┌───┐                                                   │  │
│  │   │ 💵│  Primary Savings              ● Active     ▶     │  │
│  │   └───┘  ****4521                                         │  │
│  │                                                           │  │
│  │          $8,200.00                   ↗ +$200/mo           │  │
│  │                                                           │  │
│  │   ████████████████░░░░░░░░░░░░░░░  65% to goal           │  │
│  │                                                           │  │
│  └───────────────────────────────────────────────────────────┘  │
│                                                                  │
│  Variants:                                                       │
│  ─────────────────────────────────────────────────────────────  │
│  • SAVINGS  - Green icon background, positive balance           │
│  • LOAN     - Blue icon background, negative balance, due date  │
│  • SHARE    - Purple icon background, share count               │
│                                                                  │
│  Props:                                                          │
│  ─────────────────────────────────────────────────────────────  │
│  • accountType: AccountType                                      │
│  • accountName: String                                           │
│  • accountNumber: String (masked ****XXXX)                       │
│  • balance: String                                               │
│  • status: AccountStatus (Active, Dormant, Closed)               │
│  • monthlyChange: String? (optional +/- indicator)               │
│  • progress: Float? (0.0-1.0 for goal progress)                  │
│  • progressLabel: String? ("65% to goal")                        │
│  • onClick: () -> Unit                                           │
│                                                                  │
│  Dimensions:                                                     │
│  ─────────────────────────────────────────────────────────────  │
│  • Height: ~100dp (auto)                                         │
│  • Icon Container: 44dp x 44dp                                   │
│  • Corner Radius: 16dp                                           │
│  • Padding: 16dp                                                 │
│  • Progress Bar Height: 6dp                                      │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### 4.4 TransactionItem

Modern transaction list item with category icon.

```
┌─────────────────────────────────────────────────────────────────┐
│  TRANSACTION ITEM                                                │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │                                                           │  │
│  │   ┌───┐                                                   │  │
│  │   │ 🛒│  Amazon Prime                        -$14.99     │  │
│  │   └───┘  Shopping • Dec 15, 2:34 PM                      │  │
│  │                                                           │  │
│  └───────────────────────────────────────────────────────────┘  │
│                                                                  │
│  Amount Styling:                                                 │
│  ─────────────────────────────────────────────────────────────  │
│  • Positive (Income): #00D09C, prefix "+"                        │
│  • Negative (Expense): #FF4757, prefix "-"                       │
│  • Pending: #6B7280, italic                                      │
│                                                                  │
│  Props:                                                          │
│  ─────────────────────────────────────────────────────────────  │
│  • icon: ImageVector or Emoji                                    │
│  • title: String                                                 │
│  • subtitle: String (category • timestamp)                       │
│  • amount: String                                                │
│  • isPositive: Boolean                                           │
│  • isPending: Boolean                                            │
│  • onClick: () -> Unit                                           │
│                                                                  │
│  Dimensions:                                                     │
│  ─────────────────────────────────────────────────────────────  │
│  • Height: 64dp                                                  │
│  • Icon Container: 40dp x 40dp                                   │
│  • Icon Background: Category color at 12% opacity                │
│  • Corner Radius: 12dp                                           │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### 4.5 InsightCard

AI-powered financial insight display.

```
┌─────────────────────────────────────────────────────────────────┐
│  INSIGHT CARD                                                    │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░│  │
│  │░░                                                      ░░│  │
│  │░░  💡 Weekly Insight                                   ░░│  │
│  │░░                                                      ░░│  │
│  │░░  📊 You spent 23% less on dining this week!          ░░│  │
│  │░░     compared to last week. Keep it up! 🎉            ░░│  │
│  │░░                                                      ░░│  │
│  │░░     🍔 Dining: $45 → $35                            ░░│  │
│  │░░                                                      ░░│  │
│  │░░  ┌────────────┐  ┌────────────┐                     ░░│  │
│  │░░  │View Details│  │  Dismiss   │                     ░░│  │
│  │░░  └────────────┘  └────────────┘                     ░░│  │
│  │░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░│  │
│  └───────────────────────────────────────────────────────────┘  │
│                                                                  │
│  Variants:                                                       │
│  ─────────────────────────────────────────────────────────────  │
│  • POSITIVE - Green gradient, celebration emoji                  │
│  • WARNING  - Amber gradient, caution emoji                      │
│  • INFO     - Blue gradient, info emoji                          │
│                                                                  │
│  Props:                                                          │
│  ─────────────────────────────────────────────────────────────  │
│  • type: InsightType (POSITIVE, WARNING, INFO)                   │
│  • title: String                                                 │
│  • message: String                                               │
│  • detail: String? (optional comparison data)                    │
│  • onViewDetails: () -> Unit                                     │
│  • onDismiss: () -> Unit                                         │
│  • isDismissible: Boolean                                        │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### 4.6 SavingsGoalCard

Gamified savings goal with progress ring.

```
┌─────────────────────────────────────────────────────────────────┐
│  SAVINGS GOAL CARD                                               │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │                                                           │  │
│  │   ┌─────────┐                                             │  │
│  │   │  ╭───╮  │  🏖 Vacation Fund                          │  │
│  │   │  │60%│  │  $1,200 / $2,000                           │  │
│  │   │  ╰───╯  │                                             │  │
│  │   └─────────┘  🔥 15 day streak                          │  │
│  │                                                           │  │
│  │   ████████████████░░░░░░░░░░░░░░░                        │  │
│  │                                                           │  │
│  │   ┌──────────────┐  ┌──────────────┐                     │  │
│  │   │   Add $50    │  │ View Details │                     │  │
│  │   └──────────────┘  └──────────────┘                     │  │
│  │                                                           │  │
│  └───────────────────────────────────────────────────────────┘  │
│                                                                  │
│  Props:                                                          │
│  ─────────────────────────────────────────────────────────────  │
│  • emoji: String                                                 │
│  • goalName: String                                              │
│  • currentAmount: String                                         │
│  • targetAmount: String                                          │
│  • progress: Float (0.0-1.0)                                     │
│  • streak: Int? (days)                                           │
│  • quickAddAmounts: List<Int> (e.g., [10, 25, 50, 100])          │
│  • onQuickAdd: (amount: Int) -> Unit                             │
│  • onViewDetails: () -> Unit                                     │
│                                                                  │
│  Progress Ring:                                                  │
│  ─────────────────────────────────────────────────────────────  │
│  • Size: 64dp x 64dp                                             │
│  • Stroke Width: 6dp                                             │
│  • Track Color: Surface variant                                  │
│  • Progress Color: Secondary Gradient                            │
│  • Center: Percentage text (Bold, 16sp)                          │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### 4.7 BillReminderCard

Upcoming bill payment reminder.

```
┌─────────────────────────────────────────────────────────────────┐
│  BILL REMINDER CARD                                              │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │                                                           │  │
│  │   ┌───┐                                                   │  │
│  │   │ ⚡│  Electricity Bill                  Due Dec 15    │  │
│  │   └───┘  Pacific Gas & Electric                          │  │
│  │                                                           │  │
│  │          $85.00                          ⚠️ 3 days       │  │
│  │                                                           │  │
│  │   ┌─────────────┐  ┌─────────────────┐                   │  │
│  │   │   Pay Now   │  │  Remind Later   │                   │  │
│  │   └─────────────┘  └─────────────────┘                   │  │
│  │                                                           │  │
│  └───────────────────────────────────────────────────────────┘  │
│                                                                  │
│  Urgency Indicators:                                             │
│  ─────────────────────────────────────────────────────────────  │
│  • Overdue:  Red background tint, "⛔ Overdue"                   │
│  • 1-3 days: Amber background tint, "⚠️ X days"                 │
│  • 4-7 days: Default surface, "📅 X days"                        │
│  • 7+ days:  Default surface, date only                          │
│                                                                  │
│  Props:                                                          │
│  ─────────────────────────────────────────────────────────────  │
│  • icon: ImageVector or Emoji                                    │
│  • billName: String                                              │
│  • payee: String                                                 │
│  • amount: String                                                │
│  • dueDate: LocalDate                                            │
│  • isOverdue: Boolean                                            │
│  • onPayNow: () -> Unit                                          │
│  • onRemindLater: () -> Unit                                     │
│  • onDismiss: () -> Unit                                         │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### 4.8 RecentRecipientChip

Avatar-based recent transfer recipient.

```
┌─────────────────────────────────────────────────────────────────┐
│  RECENT RECIPIENT CHIP                                           │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  ┌─────────────────────────────────────────────────────────┐    │
│  │                                                         │    │
│  │  ┌────┐  ┌────┐  ┌────┐  ┌────┐  ┌────┐              │    │
│  │  │ JD │  │ MS │  │ AK │  │ RK │  │ +  │              │    │
│  │  └────┘  └────┘  └────┘  └────┘  └────┘              │    │
│  │  John    Maria   Alex    Raj     Add                  │    │
│  │                                                         │    │
│  └─────────────────────────────────────────────────────────┘    │
│                                                                  │
│  Props:                                                          │
│  ─────────────────────────────────────────────────────────────  │
│  • initials: String (2 characters)                               │
│  • name: String (display name, max 6 chars)                      │
│  • avatarUrl: String? (optional profile image)                   │
│  • backgroundColor: Color (random from preset palette)           │
│  • onClick: () -> Unit                                           │
│                                                                  │
│  Dimensions:                                                     │
│  ─────────────────────────────────────────────────────────────  │
│  • Avatar Size: 48dp x 48dp                                      │
│  • Corner Radius: 50% (circle)                                   │
│  • Label: 12sp, max 6 characters with ellipsis                   │
│  • Horizontal Spacing: 16dp                                      │
│                                                                  │
│  Add Button Variant:                                             │
│  ─────────────────────────────────────────────────────────────  │
│  • Dashed border instead of solid                                │
│  • "+" icon in center                                            │
│  • "Add" label below                                             │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### 4.9 AmountInputField

Large amount input with quick-add chips.

```
┌─────────────────────────────────────────────────────────────────┐
│  AMOUNT INPUT FIELD                                              │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │                                                           │  │
│  │                    $ 150.00                               │  │
│  │                    ═════════                              │  │
│  │                                                           │  │
│  │   ┌──────┐  ┌──────┐  ┌──────┐  ┌──────┐               │  │
│  │   │ +10  │  │ +50  │  │ +100 │  │ +500 │               │  │
│  │   └──────┘  └──────┘  └──────┘  └──────┘               │  │
│  │                                                           │  │
│  │   Available: $8,200.00                                    │  │
│  │                                                           │  │
│  └───────────────────────────────────────────────────────────┘  │
│                                                                  │
│  Props:                                                          │
│  ─────────────────────────────────────────────────────────────  │
│  • value: String                                                 │
│  • onValueChange: (String) -> Unit                               │
│  • currency: String ("$", "€", etc.)                             │
│  • availableBalance: String                                      │
│  • quickAmounts: List<Int> (e.g., [10, 50, 100, 500])            │
│  • onQuickAmountClick: (Int) -> Unit                             │
│  • error: String? (e.g., "Exceeds available balance")            │
│                                                                  │
│  Styling:                                                        │
│  ─────────────────────────────────────────────────────────────  │
│  • Amount Font: 36sp, ExtraBold                                  │
│  • Currency: 24sp, Medium                                        │
│  • Quick Amount Chips: Outlined, 12sp                            │
│  • Available Label: 14sp, Secondary color                        │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### 4.10 TrustBadge

Security and encryption indicator.

```
┌─────────────────────────────────────────────────────────────────┐
│  TRUST BADGE                                                     │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  Inline Variant:                                                 │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │  🔒 256-bit encryption • Instant transfer                 │  │
│  └───────────────────────────────────────────────────────────┘  │
│                                                                  │
│  Compact Variant:                                                │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │  🔒 Secured                                               │  │
│  └───────────────────────────────────────────────────────────┘  │
│                                                                  │
│  Last Login Variant:                                             │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │  Last login: Today 9:41 AM • iPhone 15 Pro                │  │
│  └───────────────────────────────────────────────────────────┘  │
│                                                                  │
│  Props:                                                          │
│  ─────────────────────────────────────────────────────────────  │
│  • variant: TrustBadgeVariant (INLINE, COMPACT, LAST_LOGIN)      │
│  • message: String                                               │
│  • icon: ImageVector (default: Lock)                             │
│  • timestamp: String? (for LAST_LOGIN)                           │
│  • device: String? (for LAST_LOGIN)                              │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### 4.11 FilterChipRow

Horizontal filter selection chips.

```
┌─────────────────────────────────────────────────────────────────┐
│  FILTER CHIP ROW                                                 │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │                                                           │  │
│  │  ┌───────┐  ┌─────────┐  ┌───────┐  ┌───────┐           │  │
│  │  │ All ✓ │  │ Savings │  │ Loans │  │ Share │           │  │
│  │  └───────┘  └─────────┘  └───────┘  └───────┘           │  │
│  │   SELECTED     DEFAULT     DEFAULT    DEFAULT             │  │
│  │                                                           │  │
│  └───────────────────────────────────────────────────────────┘  │
│                                                                  │
│  States:                                                         │
│  ─────────────────────────────────────────────────────────────  │
│  • Default:  Outlined border, transparent background             │
│  • Selected: Filled with Primary color, white text, checkmark    │
│  • Disabled: 40% opacity                                         │
│                                                                  │
│  Props:                                                          │
│  ─────────────────────────────────────────────────────────────  │
│  • filters: List<FilterOption>                                   │
│    - label: String                                               │
│    - isSelected: Boolean                                         │
│    - count: Int? (optional badge)                                │
│  • onFilterSelected: (index: Int) -> Unit                        │
│  • singleSelect: Boolean (true) or multiSelect: Boolean          │
│                                                                  │
│  Dimensions:                                                     │
│  ─────────────────────────────────────────────────────────────  │
│  • Height: 36dp                                                  │
│  • Corner Radius: 18dp (pill shape)                              │
│  • Horizontal Padding: 16dp                                      │
│  • Gap: 8dp                                                      │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

## 5. Navigation

### 5.1 Bottom Navigation Bar

```
┌─────────────────────────────────────────────────────────────────┐
│  BOTTOM NAVIGATION BAR (4-Tab)                                   │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │                                                           │  │
│  │   ┌─────┐    ┌─────┐    ┌─────────┐    ┌─────┐          │  │
│  │   │  ⌂  │    │  ☰  │    │   💸    │    │  👤  │          │  │
│  │   │     │    │     │    │  ═════  │    │     │          │  │
│  │   └─────┘    └─────┘    │   FAB   │    └─────┘          │  │
│  │    Home     Accounts    └─────────┘    Profile           │  │
│  │   ACTIVE    INACTIVE      CENTER      INACTIVE           │  │
│  │                                                           │  │
│  └───────────────────────────────────────────────────────────┘  │
│                                                                  │
│  Specifications:                                                 │
│  ─────────────────────────────────────────────────────────────  │
│  • Height: 64dp                                                  │
│  • Background: Surface with elevation (4dp)                      │
│  • Active Icon: Primary color, filled variant                    │
│  • Inactive Icon: Secondary text color, outlined variant         │
│  • Label: 12sp, shown on active tab only                         │
│  • FAB: 56dp, elevated, gradient background, glow effect         │
│  • Safe Area: Bottom padding for gesture navigation              │
│                                                                  │
│  FAB Design:                                                     │
│  ─────────────────────────────────────────────────────────────  │
│  • Size: 56dp x 56dp                                             │
│  • Elevation: 8dp                                                │
│  • Y-Offset: -16dp (floats above bar)                            │
│  • Background: Primary Gradient                                  │
│  • Shadow: 16dp blur, primary color at 30%                       │
│  • Icon: Send Money (💸), 28dp, white                            │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

## 6. Animations & Micro-interactions

### 6.1 Animation Tokens

```
┌─────────────────────────────────────────────────────────────────┐
│  ANIMATION SPECIFICATIONS                                        │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  Durations:                                                      │
│  ─────────────────────────────────────────────────────────────  │
│  • Instant:     100ms  (micro-interactions)                      │
│  • Fast:        200ms  (button presses, toggles)                 │
│  • Normal:      300ms  (screen transitions)                      │
│  • Slow:        500ms  (complex animations)                      │
│  • Very Slow:   800ms  (celebrations, onboarding)                │
│                                                                  │
│  Easings:                                                        │
│  ─────────────────────────────────────────────────────────────  │
│  • Standard:    cubic-bezier(0.4, 0.0, 0.2, 1)                   │
│  • Decelerate:  cubic-bezier(0.0, 0.0, 0.2, 1)                   │
│  • Accelerate:  cubic-bezier(0.4, 0.0, 1.0, 1)                   │
│  • Spring:      spring(dampingRatio=0.7, stiffness=400)          │
│                                                                  │
│  Key Animations:                                                 │
│  ─────────────────────────────────────────────────────────────  │
│  • Balance reveal:      Fade + Scale (0.9 → 1.0), 300ms          │
│  • Card press:          Scale (1.0 → 0.98), 100ms                │
│  • Pull-to-refresh:     Rotate loading icon, continuous          │
│  • Transaction success: Checkmark draw + confetti, 500ms         │
│  • Amount input:        Haptic feedback on each digit            │
│  • Goal progress:       Circular fill animation, 800ms           │
│  • Streak badge:        Bounce + glow pulse, 300ms               │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

## 7. Accessibility

### 7.1 Guidelines

```
┌─────────────────────────────────────────────────────────────────┐
│  ACCESSIBILITY REQUIREMENTS                                      │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  Touch Targets:                                                  │
│  ─────────────────────────────────────────────────────────────  │
│  • Minimum size: 48dp x 48dp                                     │
│  • Recommended: 56dp x 56dp for primary actions                  │
│  • Spacing between targets: 8dp minimum                          │
│                                                                  │
│  Color Contrast:                                                 │
│  ─────────────────────────────────────────────────────────────  │
│  • Text on background: 4.5:1 minimum (AA)                        │
│  • Large text: 3:1 minimum                                       │
│  • Non-text elements: 3:1 minimum                                │
│  • Focus indicators: 3:1 against adjacent colors                 │
│                                                                  │
│  Screen Reader Support:                                          │
│  ─────────────────────────────────────────────────────────────  │
│  • All interactive elements: contentDescription                  │
│  • Balance amounts: Read as "Balance: 12,450 dollars"            │
│  • Status indicators: Read status (Active, Pending)              │
│  • Progress: Read as "60 percent complete"                       │
│                                                                  │
│  Motion Preferences:                                             │
│  ─────────────────────────────────────────────────────────────  │
│  • Honor "Reduce Motion" system setting                          │
│  • Provide instant transitions when motion reduced               │
│  • Keep essential feedback animations                            │
│                                                                  │
│  Font Scaling:                                                   │
│  ─────────────────────────────────────────────────────────────  │
│  • Support up to 200% text scaling                               │
│  • Use flexible layouts that don't break                         │
│  • Scroll when content overflows                                 │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

## 8. Dark Mode

### 8.1 Theme Switching

```
┌─────────────────────────────────────────────────────────────────┐
│  DARK MODE SPECIFICATIONS                                        │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  Automatic Elements:                                             │
│  ─────────────────────────────────────────────────────────────  │
│  • Background: #F8F9FA → #0D1117                                 │
│  • Surface/Cards: #FFFFFF → #1A1F2E                              │
│  • Primary Text: #1F2937 → #F0F6FC                               │
│  • Secondary Text: #6B7280 → #8B949E                             │
│  • Dividers: #E1E4E8 → #30363D                                   │
│                                                                  │
│  Preserved Colors (Same in both modes):                          │
│  ─────────────────────────────────────────────────────────────  │
│  • Success: #00D09C                                              │
│  • Error: #FF4757                                                │
│  • Warning: #FFB800                                              │
│  • Primary Gradient: #667EEA → #764BA2                           │
│                                                                  │
│  Adjusted Elements:                                              │
│  ─────────────────────────────────────────────────────────────  │
│  • Card shadows: Removed in dark mode                            │
│  • Gradient intensity: Slightly reduced (85% opacity)            │
│  • Icon backgrounds: Increased contrast                          │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

## Changelog

| Date | Version | Change |
|------|---------|--------|
| 2025-12-29 | 2.0 | Complete redesign with Revolut-style vibrant design system |
