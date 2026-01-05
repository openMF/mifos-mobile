# Savings Account - Google Stitch Prompts

> **Tool**: [Google Stitch](https://stitch.withgoogle.com)
> **Style**: Material Design 3, Gamified Savings Fintech
> **Format**: Copy each prompt block directly into Stitch

---

## Design System Reference

```
Primary Gradient: #667EEA -> #764BA2
Secondary Gradient: #11998E -> #38EF7D (Savings Green)
Success/Credit: #00D09C
Error/Debit: #FF4757
Warning: #FFB800
Text Primary: #1F2937
Text Secondary: #6B7280
Surface: #FFFFFF
Screen Size: 393 x 852 pixels (Android)
```

---

## Screen 1: Savings Dashboard

```
Mobile savings dashboard screen, Material Design 3, gamified fintech app, 393x852px

Top Bar:
- Back arrow, "Savings" title
- Settings and notification icons right

Hero Card (Secondary Gradient):
- Full width, gradient #11998E to #38EF7D, 24dp radius
- "TOTAL SAVINGS" label white 12sp + eye toggle
- "$52,500.00" balance 36sp white ExtraBold centered
- Two stat chips: "+$1,250.00 This Month" green, "$127.50 Interest Earned" gold
- Fire emoji "12-day streak! Save today to keep it going" banner

Quick Deposit Section:
- "QUICK DEPOSIT" header
- 5 pill buttons row: $10, $25, $50, $100, Custom
- Each with + icon

Goals Carousel:
- "YOUR GOALS" header + "See All >" link
- Horizontal scroll cards 140dp width
- Each card: progress ring 80dp with emoji center, percentage inside
- Goal name, amount "$7,200 / $10K", quick add button
- Cards: Vacation Fund 72%, New Car 45%, Emergency 88%

Interest Earnings Section:
- "INTEREST EARNINGS" header + "This Year" dropdown
- "$892.50 total earned" prominent
- Area chart 160dp height, gradient fill
- AI insight card: "At your current rate, you'll earn ~$1,100 by EOY"

Auto-Save Rules Section:
- "AUTO-SAVE RULES" header
- Rule cards with toggle switches
- Round-up Savings [ON]: "Saved $45.30 this month"
- Weekly Transfer [ON]: "$50 every Friday -> Vacation Fund"
- Smart Save [OFF]: AI-powered, "Enable ->"
- "+ Add New Rule" button

Achievements Row:
- "ACHIEVEMENTS" header + "View All >"
- Horizontal badge row: fire 12day, $50K saved, Goal Master, First $1K, Year Saver, Locked ???
- States: EARNED (full color glow), 75% (grayscale + ring), LOCKED (grayscale)

My Accounts Section:
- "MY ACCOUNTS" header with count "3 accounts"
- Account cards white 20dp radius
- Primary Savings: wallet icon 4.5% badge, ****4521, $35,000.00, "+$450 this month"
- Emergency Fund: shield icon 3.2%, $17,500.00, progress bar 88% to goal
- Holiday Savings: pending status, amber badge, "Awaiting Approval"

Bottom Navigation:
- 64dp height: Home, Accounts, FAB Transfer, Profile
```

---

## Screen 2: Savings Account Detail

```
Mobile savings account detail screen, Material Design 3, fintech app, 393x852px

Top Bar:
- Back arrow, "Primary Savings" title, menu icon

Hero Card (Secondary Gradient):
- "Account ****4521" with green Active pill
- "$35,000.00" balance 36sp centered, "Available Balance" label
- Three stat chips: "4.5% APY", "$127.50 Interest", "+$450 This Mo"

Quick Actions Grid:
- 8 action buttons, 2 rows of 4
- Row 1: Transfer, Deposit, History, QR Code
- Row 2: Set Goal, Charges, Lock, Calculator
- Each 56dp icon containers with labels below

Interest Projection Card:
- "INTEREST PROJECTION" header
- Current Balance $35,000.00
- Monthly Deposit +$500.00
- Interest Rate 4.5% APY
- Divider
- In 1 Year: $42,575.00 (+$7,575)
- In 5 Years: $66,890.00 (+$31,890)
- "Calculate Custom Projection" gradient button

Balance History Chart:
- "BALANCE HISTORY" header + "Last 6 Mo" dropdown
- Line chart 160dp, gradient fill below
- X-axis: Jul Aug Sep Oct Nov Dec
- Legend dots: Deposits, Withdrawals, Interest

Account Details Grid:
- Key-value pairs with dividers
- Account Number: 000000004521
- Product: Savings Plus
- Interest Rate: 4.5% per annum
- Interest Posting: Monthly
- Total Deposits: $42,500.00
- Total Withdrawals: $7,500.00
- Interest Earned (YTD): $127.50
- Minimum Balance: $500.00
- Opened On: Jan 15, 2024

Recent Transactions:
- "RECENT TRANSACTIONS" header + "See All >"
- Grouped by date
- Today: Interest Credit +$12.50 green
- Yesterday: Deposit +$500.00, Round-up +$3.45
- Dec 28: Salary Credit +$4,500.00
```

---

## Screen 3: Create Savings Goal

```
Mobile create goal screen, Material Design 3, fintech app, 393x852px

Top Bar:
- Back arrow, "Create Goal" title

Goal Type Selection:
- "What are you saving for?" centered prompt
- "POPULAR GOALS" section
- 8 emoji buttons grid 2x4:
  - Vacation, Car, House, Wedding
  - Emergency, Education, Gadget, Other

Goal Name Input:
- "GOAL NAME" label
- Text input: "Dream Vacation to Bali"

Target Amount Section:
- "TARGET AMOUNT" label
- Large centered "$10,000" editable
- Quick amount chips: $1K, $5K, $10K (selected), $25K, Custom

Target Date:
- "TARGET DATE" label
- Date picker dropdown: "August 2025"

Savings Calculation:
- "TO REACH YOUR GOAL" header
- "You need to save approximately:"
- "$1,250 / month" or "$289 / week"
- AI suggestion card gradient border: "Based on your income, $800/mo is more sustainable. Consider extending to Nov 2025."

Auto-Save Configuration:
- "AUTO-SAVE" section
- Checkbox: "Enable automatic savings"
- Amount input: $250.00
- Frequency chips: Weekly (selected), Bi-weekly, Monthly, Custom
- From Account dropdown: Primary Savings ****4521

Create Button:
- Full width gradient button "Create Goal"
```

---

## Screen 4: Goal Detail View

```
Mobile goal detail screen, Material Design 3, fintech app, 393x852px

Top Bar:
- Back arrow, "Vacation Fund" title, Edit and menu buttons

Hero Progress:
- Large centered progress ring 120dp
- Beach emoji inside, "72%" percentage
- "$7,200 of $10,000" below
- Fire emoji "On track! Keep it up!"

Stats Row:
- "$2,800 left to go" header
- Two info boxes: "Est. Complete: Aug 2025", "Auto-saving: $250/week"

Action Buttons:
- Two buttons: "+ Add Money" gradient, "Withdraw" outlined

Savings Progress Chart:
- "SAVINGS PROGRESS" header
- Area chart showing actual vs projected
- Markers for milestones
- Legend: Actual (solid), Projected (dashed)

Milestones Timeline:
- "MILESTONES" header
- Vertical timeline with checkmarks
- Started Goal - Mar 1, 2025 (checked)
- 25% Complete - Apr 15, 2025 (checked)
- 50% Complete - Jun 1, 2025 (checked)
- 75% Complete - Est. Jan 2025 (half filled, current)
- Goal Complete - Est. Aug 2025 (empty, target emoji)

Savings Activity:
- "SAVINGS ACTIVITY" header + "See All >"
- Transaction list:
- Weekly auto-save +$250.00 Today
- Quick deposit +$100.00 Dec 28
- Weekly auto-save +$250.00 Dec 27
```

---

## Screen 5: Savings Challenges

```
Mobile savings challenges screen, Material Design 3, gamification, 393x852px

Top Bar:
- Back arrow, "Savings Challenges" title

Tab Bar:
- Three tabs: Active (selected), Completed, Available

Active Challenges Section:
- "YOUR ACTIVE CHALLENGES" header

Challenge Card 1 (Highlighted):
- Gradient border, fire emoji
- "NO-SPEND WEEKEND"
- "Don't spend anything this weekend!"
- Progress bar 70%, "1 day 8 hours remaining"
- "Reward: +$25 bonus to savings"

Challenge Card 2:
- "52-WEEK SAVINGS CHALLENGE"
- "Save incrementally each week: $1, $2, $3..."
- "Week 45 of 52", progress bar 87%
- "This week: Save $45"
- "Total saved: $990 / $1,378"
- "Save $45 Now" button

Challenge Card 3:
- Coffee emoji "COFFEE JAR CHALLENGE"
- "Save $5 every time you skip buying coffee"
- Progress bar 35%, "$175 / $500 goal"
- "35 coffees skipped!"
- "+ Log Skipped Coffee" button

Available Challenges Section:
- "AVAILABLE CHALLENGES" header
- 30-Day Savings Streak card with "Start ->" button
- Round-up Master card with "Start ->" button
```

---

## Screen 6: Quick Deposit Flow

```
Mobile deposit screen, Material Design 3, fintech app, 393x852px

Top Bar:
- Back arrow, "Deposit" title

To Account Selector:
- "TO" label
- Account card: wallet icon 4.5% badge, Primary Savings ****4521
- Balance: $35,000.00, dropdown indicator

Amount Input:
- "AMOUNT" label
- Large centered "$500.00" editable
- Quick amount chips: $25, $50, $100, $250, $500 (selected)

From Account Selector:
- "FROM" label
- Account card: bank icon, Checking Account ****7890
- Available: $8,500.00, dropdown indicator

Goal Assignment:
- "ADD TO GOAL (Optional)" label
- Three goal chips: Vacation (selected), Car, Emergency
- Info text: "This deposit counts toward your Vacation Fund goal"

Summary Card:
- Amount: $500.00
- To: Primary Savings ****21
- From: Checking ****7890
- Goal: Vacation Fund

Deposit Button:
- Full width gradient button "Deposit $500.00"
```

---

## Screen 7: Auto-Save Rules

```
Mobile auto-save rules screen, Material Design 3, fintech app, 393x852px

Top Bar:
- Back arrow, "Auto-Save Rules" title, "+ Add Rule" button

Info Banner:
- Lightbulb icon
- "Auto-save helps you save without thinking about it. Set rules and watch your savings grow automatically!"

Your Rules Section:
- "YOUR RULES" header

Rule Card 1 (Highlighted, Gradient border):
- "ROUND-UP SAVINGS" with [ON] toggle
- "Round every purchase to the nearest $1 and save the difference"
- Stats box: "This Month: $45.30 saved from 87 transactions"
- Stats box: "All Time: $523.45 saved from 1,247 transactions"
- Multiplier selector: 1x (selected), 2x, 3x, 5x
- Edit and Pause buttons

Rule Card 2:
- "WEEKLY TRANSFER" with [ON] toggle
- "$50.00 every Friday at 5:00 PM"
- "To: Vacation Fund"
- "Next transfer: Jan 3, 2025"
- "Total transferred: $2,350.00"
- Edit and Pause buttons

Rule Card 3:
- "PAYDAY SAVINGS" with [ON] toggle
- "Save 10% of every deposit over $1,000"
- "To: Emergency Fund"
- "Last triggered: Dec 28 ($450 saved)"
- "Total saved: $2,700.00"

Rule Card 4:
- "SMART SAVE (AI-Powered)" with [OFF] toggle
- "Let AI analyze your spending patterns and automatically save when you can afford it"
- "Typical savings: $100-300/month"
- "Enable ->" button

Total Stats:
- "TOTAL AUTO-SAVED" header
- This Month: $545.30
- This Year: $5,573.45
- All Time: $12,847.90
```

---

## Screen 8: Interest Calculator

```
Mobile savings calculator screen, Material Design 3, fintech app, 393x852px

Top Bar:
- Back arrow, "Savings Calculator" title

Info Text:
- "See how your savings can grow over time"

Starting Amount Input:
- "STARTING AMOUNT" label
- Text input: $35,000

Monthly Contribution:
- "MONTHLY CONTRIBUTION" label
- Text input: $500
- Slider: $0 to $2,000, thumb at $500

Interest Rate:
- "INTEREST RATE" label
- Text input: 4.5% APY

Time Period Selector:
- "TIME PERIOD" label
- Chips: 1 Year, 3 Years, 5 Years (selected), 10 Years, Custom

Projection Results (Gradient Card):
- "YOUR PROJECTION" header
- Large centered "$71,890.50" with "in 5 years"
- Breakdown box:
  - Starting Amount: $35,000.00
  - Total Contributions: $30,000.00 (60 deposits)
  - Interest Earned: $6,890.50
  - Divider
  - Final Balance: $71,890.50

Growth Chart:
- Area chart showing compound growth
- Y-axis: $35K to $80K
- X-axis: Now, Y1, Y2, Y3, Y4, Y5
- Legend: Principal + Contributions (solid), Interest (stacked)

Start Saving Button:
- Full width gradient button "Start Saving $500/month Now"
```

---

## Screen 9: Loading State

```
Mobile savings loading skeleton, Material Design 3, 393x852px

Top Bar: Same as success

Hero Card Shimmer:
- Gradient card shape with shimmer overlay
- Balance placeholder 200dp centered
- Two stat chip placeholders

Quick Deposit Shimmer:
- 5 circular button shimmers

Goals Carousel Shimmer:
- 3 card shapes with progress ring shimmers

Chart Shimmer:
- Rectangle shimmer for chart area

Account Cards Shimmer:
- Shimmer card shapes with icon, text lines, balance

Shimmer animation: left-to-right sweep, 1200ms infinite
Gradient: #E1E4E8 -> #F8F9FA -> #E1E4E8
```

---

## Screen 10: Empty State

```
Mobile savings empty state, Material Design 3, 393x852px

Top Bar: Same as success

Center Content:
- Piggy bank illustration 160dp floating with up-down animation
- "Start Your Savings Journey" title 20sp bold
- "Every great fortune starts with a single deposit. Open your first savings account and watch your money grow!" subtitle centered

Benefits Card:
- "WHY SAVE WITH US?" header
- Checklist items:
  - Up to 4.5% APY on savings
  - No minimum balance required
  - Smart auto-save rules
  - Goal tracking with rewards

CTA:
- Full width gradient button "Open Savings Account"
- "Learn About Savings ->" text link below
```

---

## Components

### Savings Goal Card
```
Goal card component, Material Design 3:
- Width: 140dp, height: 180dp
- Corner radius: 16dp, padding: 12dp
- Progress ring 80dp diameter, 8dp stroke
- Emoji 32dp centered in ring
- Percentage 18sp bold below emoji
- Goal name 14sp SemiBold, max 2 lines
- Amount 12sp secondary color
- Quick add button 32dp height, outlined
```

### Auto-Save Rule Card
```
Auto-save rule card, Material Design 3:
- Full width, auto height
- Padding: 16dp, corner radius: 12dp
- Icon 24dp left, title 16sp SemiBold
- Description 14sp secondary
- Stats 12sp success color
- Toggle switch right aligned
- ON state: secondary gradient border 1dp
- OFF state: standard border #E1E4E8
```

### Achievement Badge
```
Achievement badge component, Material Design 3:
- Size: 64dp x 80dp
- Icon container: 48dp circle
- Emoji: 28dp centered
- Label: 10sp below
- States: EARNED (full color + glow), PROGRESS (grayscale + ring), LOCKED (grayscale + lock)
- Tap reveals requirements
```

### Interest Chart
```
Interest earnings chart, Material Design 3:
- Height: 160dp
- Line: 2dp stroke, secondary gradient
- Fill: gradient at 30% opacity
- Data points: 6dp circles on touch
- Axis labels: 10sp secondary
- Animation: path draws 800ms
```

---

## Quick Start

1. Open [stitch.withgoogle.com](https://stitch.withgoogle.com)
2. Create project "Mifos Mobile - Savings Account"
3. Copy each screen prompt -> Generate
4. Generate components separately for reuse
5. Export all to Figma when done
