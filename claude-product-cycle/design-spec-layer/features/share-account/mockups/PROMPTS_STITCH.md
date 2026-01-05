# Share Account - Google Stitch Prompts

> **Tool**: [Google Stitch](https://stitch.withgoogle.com)
> **Style**: Material Design 3, Investment Portfolio Fintech
> **Format**: Copy each prompt block directly into Stitch

---

## Design System Reference

```
Primary Gradient: #667EEA -> #764BA2
Secondary Gradient: #11998E -> #38EF7D
Success/Gain: #00D09C
Error/Loss: #FF4757
Warning: #FFB800
Text Primary: #1F2937
Text Secondary: #6B7280
Surface: #FFFFFF
Screen Size: 393 x 852 pixels (Android)
```

---

## Screen 1: Share Portfolio Dashboard

```
Mobile investment portfolio dashboard, Material Design 3, fintech, 393x852px

Top Bar:
- Back arrow, "My Investments" title
- Add and help icons right

Portfolio Hero Card:
- Full width, gradient #667EEA to #764BA2, 24dp radius
- Portfolio icon + "Your Portfolio" header
- "Total Value" label
- "$5,750.00" balance 36sp white ExtraBold
- Gain indicator: up arrow "+$425.00 (+7.9%)" green
- "All Time Gain" subtitle
- Two stat chips: coin "150 Shares", dollar "1,250 Dividends"
- Sparkline chart in background

Portfolio Performance Chart:
- Time selector tabs: 1D, 1W, 1M, 3M, 1Y, ALL
- Line chart 180dp height, gradient fill
- Current price point highlighted
- X-axis: month labels
- Monthly return chips below: +2.5% Jan, +1.8% Feb, +3.2% Mar, +0.4% Apr

Next Dividend Card:
- Success gradient #11998E to #38EF7D
- Calendar icon with MAR 15 date
- "Next Dividend" header
- "Estimated: $156.25" prominent
- Two info chips: "DRIP On Auto-invest", "42 days until payout"

Investment Streak Section:
- Fire icon "18 Month Investor!"
- Monthly investment grid 3 rows
- Green checkmarks for invested months
- Current month highlighted
- Badge: "Diamond Hands 2024"

Share Accounts Section:
- "MY SHARE ACCOUNTS" header, "Active (2)", "View All >"
- Equity Shares Card:
  - Coin icon, SH-0001234567
  - Value $2,500.00, Gain +$175.00 (+7.5%) green
  - Stats: 100 Shares, $25.00/share
  - Actions: [Buy] [Sell] [Details]
- Premium Shares Card:
  - Value $3,250.00, Gain +$250.00 (+8.3%)
  - 50 Shares, $65.00/share

Smart Investment Tip Card:
- Primary gradient
- Sparkle icon "AI Insight"
- "Your dividend yield is 5.2%. Buy 10 more shares to reach Gold Member tier (+0.5% bonus dividend rate)."
- Actions: [Dismiss] [Buy Now]

Quick Actions Row:
- 4 buttons: Buy Shares, Dividend Calc, Price Alerts, Vote

Bottom Navigation:
- Home, Accounts, FAB +, Cards, Profile
```

---

## Screen 2: Share Account Detail

```
Mobile share account detail, Material Design 3, investment fintech, 393x852px

Top Bar:
- Back arrow, "Equity Shares" title
- "SH-0001234567" subtitle, menu icon

Share Hero Card:
- Primary gradient
- Coin icon "EQUITY SHARES", account number
- "Portfolio Value" label
- "$2,500.00" balance 36sp
- Gain badge: up arrow "+$175.00 (+7.5%)"
- Green "ACTIVE" status pill

Share Stats Row:
- Three stat boxes: Coin "100 Shares", Price "$25.00/share", Gain "+7.5%"

Share Price Chart:
- Time selector: 1D, 1W, 1M, 3M, 1Y, ALL
- Line chart with gradient fill
- Current price highlighted
- Purchase price marker with dashed line: "You bought here"
- X-axis: date labels

Dividend Tracker Card:
- Success gradient
- Dollar icon "Your Dividend Earnings"
- "$500.00 Total Received" large
- Grid 2x2: This Year $400, Last Div $100, Yield 4.0% p.a., Frequency Quarterly

DRIP Settings Card:
- "Dividend Reinvestment" header
- Toggle: "DRIP Enabled" [ON]
- Description: "Automatically reinvest dividends to buy more shares"
- "Est. extra shares/year: 4"

Quick Actions Row:
- Three buttons: Buy Shares, Sell Shares, Dividends History

Share Details Grid:
- Product Name: Equity Shares
- Approved Shares: 100
- Pending Shares: 0
- Purchase Price (Avg): $23.25
- Current Price: $25.00
- Currency: USD ($)

Member Benefits Card:
- Star icon "Gold Member (100+ shares)"
- Checklist with checkmarks:
  - +0.5% Bonus Dividend: Applied to all payouts
  - Voting Rights (1 vote): Next AGM March 2025
  - Priority New Offerings: Early access enabled

Actions Menu:
- List with chevrons: Dividend History, Transaction History, Charges, Price Alerts, Generate QR
```

---

## Screen 3: Buy Shares Flow

```
Mobile buy shares - Step 1, Material Design 3, fintech, 393x852px

Top Bar:
- Close X, "Buy Shares" title, "1/3" indicator

Progress Indicator:
- Three steps: [1] Amount, [2] Review, [3] Confirm

Share Selector Card:
- Primary gradient
- "How many shares?" label
- Large "25" number centered with "shares" label
- Slider: 10 to 100

Quick Amounts:
- Four chips: 10, [25] selected, 50, 100

Investment Preview:
- "Investment Breakdown" header
- Shares to Buy: 25
- Price per Share: $25.00
- Subtotal: $625.00
- Transaction Fee: $1.25
- Total Cost: $626.25 bold

Projected Returns Card:
- Primary gradient
- Chart icon "Your Projected Returns"
- "After Purchase:" header
- Two stat boxes: "Total Shares 125", "Est. Annual Dividend $125.00"
- Sparkle text: "You'll earn $3.12/mo in dividends!"

Payment Source:
- "Pay From" label
- Account card: SAV icon, Primary Savings ****4521
- Available: $8,200.00
- "Change Account v" link

Continue Button:
- Full width gradient: "Continue - Review"

---

Step 2: Review

Top Bar:
- Back arrow, "Buy Shares" title, "2/3"

Order Review Card:
- Equity Shares, SH-0001234567
- Order Type: Buy
- Shares: 25
- Price per Share: $25.00
- Subtotal: $625.00
- Transaction Fee: $1.25
- TOTAL: $626.25 highlighted box

After Purchase Card:
- "Your Holdings After" header
- Total Shares: 100 -> 125
- Portfolio Value: $2,500 -> $3,125
- Annual Dividend: $100 -> $125
- Star badge: "Gold Member Maintained"

Streak Bonus Card:
- Fire icon "Streak Continues!"
- "This purchase extends your 19-month investment streak!"

Confirm Button:
- Full width gradient: "Confirm - $626.25"
- Lock icon: "Secured with 256-bit encryption"

---

Step 3: Success

Success Card:
- Animated checkmark + confetti
- "Purchase Successful!"
- "+25 Shares", "$626.25"

Updated Holdings:
- Two stat boxes: Coin "125 Total Shares", Dollar "$125.00 Est. Annual Dividend"
- "Portfolio Value: $3,125.00"

Streak Update:
- Fire icon "19 MONTHS Consecutive Investing"
- Badge: "Diamond Hands Unlocked!"

Action Buttons:
- View Order, Home, Done (gradient)
```

---

## Screen 4: Sell Shares Flow

```
Mobile sell shares, Material Design 3, fintech, 393x852px

Top Bar:
- Close X, "Sell Shares" title, "1/3"

Progress Indicator:
- Three steps: Amount, Review, Confirm

Sell Selector Card:
- Accent gradient #FC466B to #3F5EFB
- "How many to sell?" label
- Large "25" number with "shares" label
- "Available: 100 Max: 100"

Quick Amounts:
- Four chips: 10, [25] selected, 50, ALL

Sale Preview:
- "Sale Breakdown" header
- Shares to Sell: 25
- Current Price: $25.00
- Your Avg Purchase Price: $23.25
- Gross Proceeds: $625.00
- Transaction Fee: $1.25
- Net Proceeds: $623.75

Profit/Loss Card:
- Success gradient (if profit)
- Up arrow "Your Profit"
- "+$42.50" large, "(+7.3%)"
- "Based on avg purchase price"

Impact on Holdings:
- "After This Sale" header
- Remaining Shares: 100 -> 75
- Portfolio Value: $2,500 -> $1,875
- Annual Dividend: $100 -> $75
- Warning: "Below Gold Member (100) -0.5% bonus dividend"

Receive Funds To:
- SAV icon, Primary Savings ****4521
- "Transfer: 1-2 days"

Warning:
- "Sale is final and irreversible"

Confirm Button:
- Accent gradient: "Confirm Sale - $623.75"
```

---

## Screen 5: Dividend History

```
Mobile dividend history, Material Design 3, fintech, 393x852px

Top Bar:
- Back arrow, "Dividend History" title, filter icon

Dividend Summary Card:
- Success gradient
- Dollar icon "Lifetime Dividends"
- "$1,250.00 All Time" large
- Two stat boxes: "This Year $400.00", "Last Year $350.00"

Dividend Growth Chart:
- Bar chart showing yearly growth
- Bars: 2021 ~$100, 2022 ~$200, 2023 ~$350, 2024 ~$400
- "Compound Growth: 18.5% annually"

Year Filter:
- Chips: [All], 2024, 2023, 2022

2024 Dividends Section:
- Q4 Dividend: +$100.00, Dec 01, 2024 [PAID] green badge
- Rate: 4.0% | 100 shares
- Q3 Dividend: +$100.00, Sep 01, 2024 [PAID]
- Q2 Dividend: +$100.00, Jun 01, 2024 [PAID]
- Q1 Dividend: +$100.00, Mar 01, 2024 [PAID]

2023 Dividends Section:
- Q4 Dividend: +$87.50, Dec 01, 2023 [PAID]
- Rate: 3.75% | 80 shares
- Q3 Dividend: +$87.50, Sep 01, 2023 [PAID]
- Load More button
```

---

## Screen 6: Price Alerts

```
Mobile price alerts, Material Design 3, fintech, 393x852px

Top Bar:
- Back arrow, "Price Alerts" title, add icon

Current Price Card:
- "Equity Shares" header
- "Current Price" label
- "$25.00" large
- Gain badge: "+$1.75 (+7.5%) YTD"

Active Alerts Section:
- "ACTIVE ALERTS" header

Alert Card 1:
- Up arrow icon "Price Above $27.00"
- "Current: $25.00 Gap: $2.00"
- Progress bar 92.6% to trigger
- Actions: [Edit] [Delete]

Alert Card 2:
- Down arrow icon "Price Below $22.00" (buy opportunity)
- "Current: $25.00 Gap: $3.00"
- "Not triggered" status
- Actions: [Edit] [Delete]

Create New Alert Card:
- Primary gradient
- Plus icon "Add Price Alert"
- "Get notified when share price reaches your target"
```

---

## Screen 7: Dividend Calculator

```
Mobile dividend calculator, Material Design 3, fintech, 393x852px

Top Bar:
- Back arrow, "Dividend Calculator" title

Current Holdings Card:
- "Equity Shares" header
- Current Holdings: 100 shares
- Annual Dividend: $100.00

Projection Settings:
- Monthly Investment label
- Large "$250" editable
- Slider: $0 to $1000

- Investment Period label
- Chips: 1Y, 3Y, [5Y] selected, 10Y, 20Y

Projection Results Card:
- Primary gradient
- Chart icon "After 5 Years"
- "$18,750.00 Total Value" large
- Grid 2x2:
  - Total Shares: 700
  - Annual Dividend: $700.00
  - Invested: $15,000
  - Dividends: $1,250

Growth Chart:
- Area chart showing compound growth
- Y-axis: $6k to $18k
- X-axis: Now, Y1, Y2, Y3, Y4, Y5
- Legend: Principal (solid), Dividends (stacked)

Start Button:
- Full width gradient: "Start Investing"
```

---

## Screen 8: Member Voting

```
Mobile governance voting, Material Design 3, fintech, 393x852px

Top Bar:
- Back arrow, "Governance & Voting" title

Voting Power Card:
- Primary gradient
- Vote icon "Your Voting Power"
- "1 Vote" large
- "Gold Member (100+ shares)"
- "1 vote per resolution"

Upcoming Votes Section:
- "UPCOMING VOTES" header
- "Annual General Meeting March 15, 2025"

Resolution 1 Card:
- "Increase Dividend Rate" title
- "Proposal: 4.0% -> 4.5%"
- Three vote buttons: FOR, AGAINST, ABSTAIN
- "Voting ends: Mar 10, 2025"

Resolution 2 Card:
- "Elect Board Member" title
- "Candidates: 3"
- [View Candidates >] link
- [Vote Now >] button

Past Votes Section:
- "PAST VOTES" header
- 2024 AGM Votes: 3 resolutions voted [v] [View Results >]
- 2023 AGM Votes: 2 resolutions voted [v] [View Results >]
```

---

## Screen 9: Achievements

```
Mobile share achievements, Material Design 3, gamification, 393x852px

Top Bar:
- Back arrow, "My Achievements" title

Achievement Stats:
- Three stat boxes: "8 Badges", "19 Streak", "$1250 Divs"

Unlocked Badges Section:
- "Unlocked (8)" header
- 4x2 badge grid:
  - Fire Streak Pro, Star Gold Member, Dollar Div King, 100 First 100
  - DRIP DRIP Master, Vote Active Voter, Up Profit Pro, Lock Hold Firm
- Full color with glow effects

Locked Badges Section:
- "Locked (4)" header
- Grayed out: Plat Platinum Member, 500 500 Shares, Year 5 Year Holder, VIP VIP Club
- Progress hint: "Platinum Member: Own 200+"
- Progress bar 50% -> 200

Recent Activity:
- Timeline:
  - Gold Member achieved! Jan 15, 2025
  - 18-month streak reached! Jan 10, 2025
  - Dividend King badge Dec 01, 2024
```

---

## Screen 10: Loading & Empty States

```
Loading State:
Mobile share loading skeleton, Material Design 3, 393x852px

Hero Shimmer:
- Gradient card shape with shimmer
- Balance and gain placeholders

Chart Shimmer:
- Rectangle shimmer for chart area

Account Cards Shimmer:
- Card shapes with icon, text, balance shimmers

Animation: left-to-right sweep, 1000ms
Gradient: #E1E4E8 -> #F8F9FA -> #E1E4E8

---

Empty State:
Mobile share empty state, Material Design 3, 393x852px

Center Content:
- Coin illustration 160dp floating
- "Start Your Investment Journey" title 20sp bold
- "Invest in your cooperative and start earning dividends today!"

Benefits:
- "WHY INVEST?" header
- Checklist:
  - Earn Quarterly Dividends (4-7% annual yield)
  - Own Part of Your Coop (Build long-term wealth)
  - Get Voting Rights (Have your say)
  - Member Benefits (Priority access & bonuses)

CTA:
- Full width gradient: "Apply for Shares"
```

---

## Components

### PortfolioHeroCard
```
Portfolio hero card, Material Design 3:
- Min height: 220dp, padding: 24dp
- Gradient #667EEA to #764BA2, 24dp radius
- Sparkline chart in background 20% opacity
- Gain/loss badge: green for positive, red for negative
- Shadow: 12dp blur, gradient 25%
```

### SharePerformanceChart
```
Share price chart, Material Design 3:
- Height: 180dp
- Line: 3dp stroke, gradient
- Fill: gradient at 20% opacity
- Purchase price: dashed line with label
- Touch: tooltip on drag
- Animation: path draws 1200ms
```

### DividendTrackerCard
```
Dividend tracker card, Material Design 3:
- Success gradient #11998E to #38EF7D
- Stats grid 2x2
- Height: auto, padding: 20dp
- Corner radius: 16dp
```

### InvestmentStreakGrid
```
Investment streak grid, Material Design 3:
- Cell: 32dp x 32dp, spacing: 4dp
- Invested: #00D09C green check
- Current: #FFB800 amber pulse
- Missed: #FF4757 red X
- Future: #E1E4E8 gray
```

### ShareCard
```
Share account card, Material Design 3:
- Min height: 160dp, padding: 16dp
- White surface, 16dp radius
- Gain badge: green/red based on performance
- Quick action buttons row
```

### DRIPToggle
```
DRIP toggle card, Material Design 3:
- Toggle: Material Switch
- Info text: secondary below
- Outlined card style
- ON state: success tint
```

---

## Quick Start

1. Open [stitch.withgoogle.com](https://stitch.withgoogle.com)
2. Create project "Mifos Mobile - Share Account"
3. Copy each screen prompt -> Generate
4. Generate components separately for reuse
5. Export all to Figma when done
