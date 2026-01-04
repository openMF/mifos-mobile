# Accounts - Google Stitch Prompts

> **Tool**: [Google Stitch](https://stitch.withgoogle.com)
> **Style**: Material Design 3, Financial Command Center
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

## Screen 1: Main Accounts View

```
Mobile accounts dashboard screen, Material Design 3, fintech app, 393x852px

Top Bar:
- "My Accounts" title 24sp bold, #1F2937
- Right side: search, quick actions, notification icons
- Filter button right aligned

Tab Bar:
- 5 tabs: All (selected), Savings, Loans, Shares, Goals
- Selected indicator pill below active tab
- Horizontal scroll if needed

Net Worth Hero Card:
- Full width, gradient #667EEA to #764BA2, 24dp radius
- "NET WORTH" label white 12sp + eye toggle
- "$18,450.00" balance 36sp white ExtraBold centered
- Two stat chips: "+$1,250 This Month" green, "-$380 Expenses" red
- Financial Health Score section: circular ring 85/100, "EXCELLENT" label
- "+5 from last month" change indicator

Spending Analytics Section:
- "SPENDING ANALYTICS" header + "This Week" dropdown
- "$1,240 spent" amount, "-12% vs last week" indicator green
- Bar chart 120dp height, 7 bars for Mon-Sun
- Today bar has dotted outline indicator
- Category chips horizontal scroll: Food $320 26%, Transport $180 15%, Shopping $450 36%, Bills $290 23%

AI Insights Section:
- "AI INSIGHTS" header + "See All" link
- Insight cards with gradient border 5% opacity
- Lightbulb icon, suggestion text, action buttons
- [Explore Options] [Dismiss]

Savings Accounts Section:
- "SAVINGS ACCOUNTS" header with count, total
- "7-day streak" flame badge, "+$850 this month"
- Account cards white 20dp radius, swipeable
- Primary Savings: wallet icon gradient bg, ****4521, $8,200.00
- Goal progress bar 65%, "$8,200 / $12,600"
- Gamification stats: fire streak, trophy goals, lightning auto-save

Loan Accounts Section:
- "LOAN ACCOUNTS" header with count, outstanding total
- "Debt-free in 18 months" projection
- Personal Loan card: -$4,500.00 red, 60% progress
- Next payment $350 due Jan 15, [Schedule] [Pay Now] buttons

Share Accounts Section:
- "SHARE ACCOUNTS" header with value
- "+$22.50 dividends this year"
- Community Shares: 15 shares @ $50, 3% APY
- [Buy More] [Sell Shares] buttons

Upcoming Bills Section:
- "DUE THIS WEEK" header + "Manage" link
- Overdue card: red left border 4dp, "OVERDUE" badge pulsing
- Electricity $85, "Was due Dec 28", [PAY NOW] [Snooze] buttons
- Due soon cards: amber indicator, Internet $65, Water $42, Rent $1,200

Bottom Navigation:
- 64dp height, 4 tabs: Home, Accounts (selected), FAB Transfer, Profile
- FAB 56dp gradient, elevated -16dp above nav bar
```

---

## Screen 2: Goals Tab View

```
Mobile goals dashboard screen, Material Design 3, fintech app, 393x852px

Tab Bar:
- Goals tab selected with indicator

Goals Overview Hero:
- Gradient card #667EEA to #764BA2
- "GOALS OVERVIEW" header
- Stats row: 4 Active, 2 Achieved, 1 Paused, $20,600 Total Goal
- Overall progress bar 62%, "$12,500 saved of $20,600 target"

Achievements Section:
- "ACHIEVEMENTS" header + "View All Badges" link
- Badge row horizontal: fire 7day, diamond $5k, target Goal Master, star Super Saver, lock ???
- States: EARNED (full color glow), 55% (grayscale + ring), LOCKED (grayscale)

Active Goals List:
- "ACTIVE GOALS" header + "+ Create Goal" button
- Emergency Fund card: shield emoji, 65% progress ring
- "$8,200 / $12,600", fire "7-day streak", "Est: Mar 2025"
- Auto-save info: "$50/week on Fridays", [Edit] [Pause] buttons

- Bali Trip card: palm emoji, 45% progress
- "$3,500 / $8,000", "Boost available!" callout
- [Boost Now] [Maybe Later] buttons

Achieved Goals Section:
- "ACHIEVED GOALS" header with celebration emoji
- New Laptop: checkmark, "$1,500 saved", "Completed Dec 2024"
- Holiday Shopping: checkmark, "$800 saved", "Completed Nov 2024"
```

---

## Screen 3: Spending Category Drill-Down

```
Mobile category detail screen, Material Design 3, fintech app, 393x852px

Top Bar:
- Back arrow, "Food & Dining" title, Share button
- "This Week" dropdown selector

Hero Stats:
- "$320.45 spent this week" large centered
- Line chart showing daily spending trend
- "15% vs last week" comparison
- "Budget: $400/week" with progress bar 80%

Sub-Categories Section:
- "SUB-CATEGORIES" header
- Restaurants $185 (58%) with progress bar
- Coffee & Cafes $68.50 (21%)
- Groceries $45.95 (14%)
- Fast Food $21.00 (7%)

Recent Transactions:
- "RECENT TRANSACTIONS" header + "See All" link
- Today section:
  - Pizza Palace -$24.50, Restaurants, 2:30 PM, Visa ****4521
  - Starbucks -$6.75, Coffee & Cafes, 9:15 AM
- Yesterday section:
  - Whole Foods -$45.95, Groceries, 6:45 PM
  - McDonald's -$12.50, Fast Food, 12:30 PM

AI Insight Card:
- Lightbulb icon, gradient border
- "You spent 58% on restaurants this week. Cooking at home 2 more days could save ~$50"
- [Set Cooking Goal] [Dismiss] buttons
```

---

## Screen 4: Loading State

```
Mobile accounts loading skeleton, Material Design 3, 393x852px

Tab Bar:
- Shimmer chips for filter tabs

Net Worth Hero Skeleton:
- Gradient background shape
- Shimmer placeholders: 200dp balance, two 80dp stat chips
- Shimmer health score circle

Analytics Skeleton:
- Header shimmer bar
- 7 shimmer bar chart shapes
- 4 category chip shimmers

Account Cards Skeleton:
- White card shapes with:
  - 48dp icon circle shimmer
  - Two text line shimmers (180dp, 120dp)
  - Balance shimmer 80dp right aligned

Shimmer animation: left-to-right sweep, 1200ms infinite
Gradient: #E1E4E8 → #F8F9FA → #E1E4E8
```

---

## Screen 5: Empty State

```
Mobile accounts empty state, Material Design 3, 393x852px

Header: Same as success with tabs visible

Center Content:
- Floating illustration 160dp: briefcase/portfolio icon
- Subtle up-down animation 2s loop
- "Start Your Financial Journey" title 20sp bold
- "Open your first account and take control of your finances" subtitle centered

Suggested Section:
- "SUGGESTED FOR YOU" header
- Savings Account card: bank icon, "4.5% APY", "Min: $0, No fees", [Open Now] button
- Investment Club card: chart icon, "3% dividends", "Min: 1 @ $50", [Learn More] button

CTA:
- "Explore All Account Types →" text link centered
```

---

## Screen 6: Error State

```
Mobile accounts error state, Material Design 3, 393x852px

Header: Same as success

Center Content:
- Cloud with X icon illustration, #FF4757 tint
- "Something Went Wrong" title 20sp bold
- "We couldn't load your accounts. Please check your connection."

Actions:
- "Try Again" gradient button full width
- "Check Network Settings" text link below
```

---

## Screen 7: Offline State

```
Mobile accounts offline banner, Material Design 3, 393x852px

Offline Banner:
- 48dp height, #FFB800 15% background
- Antenna icon, "You're offline" message, [Reconnect] button

Header:
- "My Accounts" title
- "Last updated 2 hours ago" subtitle gray

Content:
- All data at 70% opacity
- "(Cached)" labels on section headers
- Net Worth card with desaturated overlay
- "Data may be outdated" warning

Analytics Section:
- "Unavailable" badge
- Placeholder: "Data requires internet connection"

Account Cards:
- "Read-only mode • Cannot make transactions" banner
- Normal cards but swipe actions disabled
```

---

## Components

### Net Worth Hero Card
```
Financial summary hero card, Material Design 3:
- Width: 361dp, height ~220dp
- Gradient #667EEA to #764BA2, 24dp radius
- "NET WORTH" white 12sp + eye toggle
- Balance 36sp white ExtraBold centered
- Stat chips: semi-transparent white bg
- Financial Health Score: circular ring 80dp, score 36sp center
- Level 3 shadow, gradient glow at 20%
```

### Financial Health Score Ring
```
Circular score indicator, Material Design 3:
- Outer ring: 80dp diameter, 8dp stroke
- Track: white 20% opacity
- Fill: gradient based on score level
- Center: score number 36sp, rating label 12sp
- Animation: ring fills + counter animates on load
- Change indicator below: "+5 from last month"
```

### Spending Analytics Bar Chart
```
Weekly spending bar chart, Material Design 3:
- Height: 120dp
- 7 bars for Mon-Sun
- Bar width: 24dp, spacing: 12dp
- Bar color: primary gradient
- Today: dotted outline indicator
- Touch: tooltip with exact amount
- Animation: bars grow from bottom 400ms staggered
```

### AI Insight Card
```
AI suggestion card, Material Design 3:
- Width: 361dp, auto height
- Gradient border 1dp at 20% opacity
- Background: gradient at 5% opacity
- Lightbulb icon 24dp left
- Message text 14sp
- Action buttons: outlined style
- Swipe to dismiss
```

### Account Card (Enhanced)
```
Enhanced account card, Material Design 3:
- Width: 361dp, height ~140dp auto
- White surface, 20dp radius, 2dp elevation
- Left: 48dp icon container, gradient 15% bg
- Account name 16sp, number 12sp gray
- Balance 24sp bold, change indicator 14sp
- Goal section: emoji, name, progress bar, percentage
- Gamification row: streak, goals, auto-save chips
- Swipe actions: Deposit green, Details blue (left), Pay Now purple, Schedule orange (right for loans)
```

### Achievement Badge
```
Achievement badge component, Material Design 3:
- Size: 64dp × 80dp
- Icon container: 48dp circle
- Icon: 28dp emoji
- Label: 10sp below
- States: EARNED (full color + glow), PROGRESS (grayscale + ring %), LOCKED (grayscale + lock icon)
- Tap: reveals requirements for locked
```

---

## Quick Start

1. Open [stitch.withgoogle.com](https://stitch.withgoogle.com)
2. Create project "Mifos Mobile - Accounts"
3. Copy each screen prompt → Generate
4. Generate components separately for reuse
5. Export all to Figma when done
