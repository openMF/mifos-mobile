# Notifications - Google Stitch Prompts

> **Tool**: [Google Stitch](https://stitch.withgoogle.com)
> **Style**: Material Design 3, Smart Notification Intelligence Center
> **Format**: Copy each prompt block directly into Stitch

---

## Design System Reference

```
Primary Gradient: #667EEA -> #764BA2
Secondary Gradient: #11998E -> #38EF7D
Success/Credit: #00D09C
Error/Debit: #FF4757
Warning/Urgent: #FFB800
Text Primary: #1F2937
Text Secondary: #6B7280
Surface: #FFFFFF
Screen Size: 393 x 852 pixels (Android)
```

---

## Screen 1: Smart Inbox (Main View)

```
Mobile smart notification inbox, Material Design 3, fintech, 393x852px

Top Bar:
- Back arrow, "Notifications" title
- Bell icon with badge, settings icon

Focus Mode Toggle:
- Moon icon "Focus Mode" with [OFF] switch
- "Pause non-urgent notifications" subtitle

Category Tabs:
- Priority (3) selected gradient, All (12), Actions (2), Offers (4)

Priority Banner:
- Primary gradient card
- Target icon "3 items need your attention"
- "Tap to view priority notifications"

Section Header:
- "TODAY" with "Mark all read" link

Notification Cards:

Card 1 - Urgent:
- Warning icon with red URGENT badge
- "Loan Payment Due Soon" title
- "Your payment of $250.00 is due in 3 days"
- Gradient "Pay Now - $250" button
- Secondary buttons: "Set Reminder", "View Loan"

Card 2 - Deposit:
- Money icon with green DEPOSIT badge
- "Payment Received!" title
- "You received $1,500.00 from ABC Corp"
- "→ Savings Account ****4521"
- Buttons: "View Account", "Say Thanks"

Card 3 - Security:
- Lock icon with amber REVIEW badge
- "Security Alert" title
- "New login from iPhone 15 Pro in NYC"
- Buttons: "That's Me", "Not Me" (red)

Section Header:
- "YESTERDAY · 5 notifications"

Grouped Notification Cards:
- "Transaction Updates (3)" with chevron
- "Special Offers (2)" with chevron

Insights Card:
- Chart icon "View your notification insights →"
- "You check notifications 8x/day on average"
```

---

## Screen 2: Priority View (Urgent Items)

```
Mobile priority inbox, Material Design 3, fintech, 393x852px

Top Bar:
- Back arrow, "Priority Inbox" title
- Bell, settings icons

Category Tabs:
- Priority (3) selected

Priority Banner:
- Gradient background
- Lightning icon "Priority items need your attention"
- "These won't auto-dismiss"

Section Header:
- "REQUIRES ACTION"

Urgent Card:
- Red URGENT badge
- Warning icon "Loan Payment Due Soon"
- Progress bar: 75% countdown
- "Due in 3 days", "Amount: $250.00"
- "Late fee if unpaid: $25.00" warning
- Gradient "Pay Now - $250.00" button
- Secondary: "Remind Tomorrow", "Schedule Payment"

Review Card:
- Amber REVIEW NEEDED badge
- Lock icon "New Device Login"
- "iPhone 15 Pro · New York, NY"
- "Today at 2:34 PM"
- Buttons: "That's Me" checkmark, "Report Suspicious" red

Action Card:
- Orange ACTION SUGGESTED badge
- Clipboard icon "Complete Your Profile"
- "Add phone number for 2FA security"
- "Progress: 80% complete"
- "Complete Now" button

Achievement Teaser:
- Sparkle icon
- "Clear all priority items to unlock"
- "Inbox Zero achievement badge!"
```

---

## Screen 3: Notification Detail (Expanded View)

```
Mobile notification detail, Material Design 3, fintech, 393x852px

Top Bar:
- Back arrow, menu icon right

Hero Card:
- Primary gradient background
- Large money icon with "SUCCESS" badge
- "Payment Received!" title
- "+$1,500.00" large amount
- "Today at 10:34 AM"

Description:
- "You received a payment from ABC Corporation."
- "The funds have been deposited into your account."

Transaction Details Card:
- Key-value pairs:
  - From: ABC Corporation
  - To: Savings Account ****4521
  - Amount: $1,500.00
  - Reference: SALARY-DEC-2025
  - Transaction ID: TXN-789456123

AI Insights Card:
- "AI INSIGHTS" header
- Lightbulb: "This is your regular monthly salary deposit"
- Chart: "Your income this month: $1,500 (same as last)"
- Target: "Suggested: Move $300 to Emergency Fund"
- "(You typically save 20% of salary)"

Quick Actions:
- Gradient "View Account" button
- Row: "Quick Transfer", "Download PDF"
- Row: "Share Receipt", "Add Category"

Notification Preference:
- Bell icon "Get notified for future payments from ABC Corp?"
- Options: [Always] [Ask me] [Never]
```

---

## Screen 4: Focus Mode Active

```
Mobile focus mode active, Material Design 3, 393x852px

Top Bar:
- Back arrow, "Focus Mode" title, settings icon

Hero Card:
- Primary gradient background
- Large moon icon with "FOCUS" badge
- "Focus Mode Active" title
- "Only urgent notifications will come through"

Countdown Timer:
- Large centered "2:45:30" countdown
- "remaining" label
- "Ends at 6:00 PM"

During Focus Mode Section:
- "DURING FOCUS MODE" header
- Checklist with states:
  - Green check: Security alerts - Will notify
  - Green check: Urgent payment reminders - Will notify
  - Green check: Large transactions (>$500) - Will notify
  - Red X: Marketing & promotions - Silenced
  - Red X: Account updates - Silenced
  - Red X: Weekly digests - Silenced

Paused Notifications:
- "PAUSED NOTIFICATIONS" header
- "3 notifications are waiting"
- List: "Special Offers (2)", "Weekly Summary (1)"
- "These will appear when Focus Mode ends"

Action Buttons:
- "End Focus Mode Early" outline button
- "Extend by 1 Hour" outline button
```

---

## Screen 5: Daily Digest

```
Mobile daily digest, Material Design 3, fintech, 393x852px

Top Bar:
- Back arrow, "Daily Digest" title
- Date "Dec 30, 2025"

Hero Card:
- Gradient background
- Calendar icon "Your Day at a Glance"

Money In & Out Section:
- "MONEY IN & OUT" header
- Two stat cards side by side:
  - Money In: "+$1,650.00", "2 transactions", green
  - Money Out: "-$342.50", "5 transactions", red
- "Net: +$1,307.50" with sparkle

Key Highlights:
- "KEY HIGHLIGHTS" header
- Salary: money icon, "$1,500.00 from ABC Corporation"
- Payment Due: warning icon, "$250.00 due in 3 days" with buttons
- Security: lock icon, "New device login confirmed as you"

Spending by Category:
- "SPENDING BY CATEGORY" header
- Horizontal bar chart:
  - Shopping: longest bar, $189.99
  - Food & Dining: medium bar, $87.50
  - Transportation: short bar, $45.00
  - Subscriptions: shortest bar, $20.01

Smart Insights:
- "SMART INSIGHTS" header
- Lightbulb: "You spent 15% less on food today"
- Target: "Suggested: Set aside $150 for emergency fund"

View All Button:
- "View All 12 Notifications" outline

Digest Settings:
- Clock icon "Digest delivered daily at 8:00 PM [Change]"
```

---

## Screen 6: Notification Analytics

```
Mobile notification analytics, Material Design 3, fintech, 393x852px

Top Bar:
- Back arrow, "Notification Insights" title, settings

Health Score Card:
- "Your Notification Health"
- Large circular progress: 92% HEALTHY
- "You're in control of your alerts!"

Weekly Activity Chart:
- "THIS WEEK" header
- Bar chart Mon-Sat with varying heights
- "Total: 84 notifications · Avg: 12/day"

Engagement Stats:
- "ENGAGEMENT STATS" header
- Key-value list:
  - Received: 84
  - Opened: 72 (86%)
  - Acted On: 28 (33%)
  - Dismissed: 12 (14%)
  - Avg Response: 4.2 min

Notification Breakdown:
- "NOTIFICATION BREAKDOWN" header
- Pie/pyramid chart:
  - Transactions: 45%
  - Alerts: 25%
  - Security: 18%
  - Promos: 12%

Achievements:
- "ACHIEVEMENTS" header
- Trophy: "Inbox Zero Champion" - Earned 3x
- Fire: "Quick Responder" - 98% progress
- Target: "Action Taker" - 28 actions

Optimization Tip:
- Lightbulb "Enable daily digest to reduce interruptions by 40%"
- "Enable Daily Digest" button
```

---

## Screen 7: Smart Settings

```
Mobile notification settings, Material Design 3, fintech, 393x852px

Top Bar:
- Back arrow, "Notification Settings" title

Smart Learning Banner:
- Gradient background
- Brain icon "Smart Learning [ENABLED]"
- "AI learns your preferences over time"

Priority Settings:
- "PRIORITY SETTINGS" header
- High Priority (Always notify):
  - Check: Security alerts
  - Check: Payment due reminders
  - Check: Large transactions (>$500)
  - Check: Failed transactions
- Medium Priority (Silent during focus):
  - Check: Incoming payments
  - Check: Transfer confirmations
- Low Priority (Digest only):
  - Circle: Marketing promotions
  - Circle: Feature announcements

Delivery Preferences:
- "DELIVERY PREFERENCES" header
- Push Notifications: [ON] with description
- Email Notifications: [ON] with description
- SMS Alerts: [OFF] with description

Smart Features:
- "SMART FEATURES" header
- Smart Grouping: [ON]
- Daily Digest: [ON] at 8:00 PM [Change]
- Quiet Hours: [ON] 10:00 PM - 7:00 AM
- AI Suggestions: [ON]

Notification Channels:
- "NOTIFICATION CHANNELS" header
- List with toggles and chevrons:
  - Transactions [ON]
  - Security [ON]
  - Loan & Payment Reminders [ON]
  - Offers & Promotions [OFF]
  - Account Updates [ON]
  - Savings Goals [ON]

Reset Button:
- "Reset to Default Settings" text button
```

---

## Screen 8: Grouped Notifications

```
Mobile grouped notifications, Material Design 3, fintech, 393x852px

Top Bar:
- Back arrow, "Transaction Updates" title
- Date "12/30"

Summary Card:
- Package icon "5 transactions today"
- "Net: -$342.50"

Batch Actions:
- [Select All] [Mark All Read]

Transaction Cards:

Amazon:
- Cart icon, "Amazon", "11:23 AM"
- "Electronics purchase"
- "-$189.99"
- "Shopping · Credit Card ****8765"

Netflix:
- Movie icon, "Netflix", "8:00 AM"
- "Monthly subscription"
- "-$15.99"
- "Subscriptions · Auto-payment"
- Recurring badge

Uber:
- Car icon, "Uber", "7:45 AM"
- "Morning commute"
- "-$24.50"
- "Transportation · Debit Card ****4521"

Starbucks:
- Coffee icon, "Starbucks", "7:15 AM"
- "Coffee & breakfast"
- "-$12.50"
- "Food & Dining · Apple Pay"

Spotify:
- Music icon, "Spotify", "12:00 AM"
- "Premium subscription"
- "-$9.99"
- "Subscriptions · Auto-payment"
- Recurring badge

Subscription Insight:
- Lightbulb "You have 2 subscriptions in this group"
- "Total: $25.98/month"
- "Review Subscriptions" button

Batch Action Buttons:
- "Delete Selected", "Archive Group"
```

---

## Screen 9: Empty State (Achievement Unlocked)

```
Mobile notifications empty state, Material Design 3, 393x852px

Top Bar:
- Back arrow, "Notifications" title
- Bell, settings icons

Category Tabs:
- All showing (0) for each category

Achievement Card:
- Centered trophy icon with sparkles
- "INBOX ZERO ACHIEVED!"
- "All Caught Up!"
- "You've cleared all notifications."
- "Take a moment to celebrate!" with party emoji

Streak Widget:
- Fire icon "Current Streak: 5 days"
- "You've reached inbox zero 5 days in a row!"
- Week grid: Mon-Fri checked, Sat-Sun empty

Info Text:
- "We'll notify you when something important happens with your accounts."

Action Buttons:
- "View Notification Insights" gradient
- "Manage Settings" outline
```

---

## Screen 10: Loading State

```
Mobile notifications loading, Material Design 3, 393x852px

Top Bar:
- Back arrow, "Notifications" title
- Bell, settings icons

Focus Mode Skeleton:
- Shimmer rectangle for toggle card

Tab Skeleton:
- Four shimmer pill shapes

Notification Skeletons:
- Three notification card skeletons:
  - 48dp icon placeholder
  - Two-line title shimmer
  - Three-line content shimmer
  - Two button placeholders

Shimmer Animation:
- Gradient sweep: #F3F4F6 → #FFFFFF → #F3F4F6
- Duration: 1500ms, infinite loop
```

---

## Components

### NotificationCard
```
Notification card component, Material Design 3:
- Height: variable, min 100dp
- Icon: 48dp with priority color ring
- Badge: URGENT (red), DEPOSIT (green), REVIEW (amber)
- Title: 16sp Medium
- Body: 14sp Regular
- Actions: gradient primary, outline secondary
- Swipe actions: archive, delete
```

### PriorityBadge
```
Priority badge component, Material Design 3:
- Height: 24dp pill shape
- URGENT: red background, white text
- REVIEW: amber background, dark text
- DEPOSIT: green background, white text
- ACTION: orange background, white text
```

### FocusModeToggle
```
Focus mode toggle, Material Design 3:
- Height: 64dp card
- Moon icon left
- "Focus Mode" title
- "Pause non-urgent" subtitle
- Toggle switch right
- Active: gradient border glow
```

### DigestSummaryCard
```
Daily digest summary, Material Design 3:
- Two-column layout
- Money In: green icon, amount, count
- Money Out: red icon, amount, count
- Net: centered, sparkle if positive
- Gradient divider between sections
```

### NotificationInsightCard
```
Notification insight card, Material Design 3:
- Height: 56dp
- Icon: chart, lightbulb, or target
- Insight text: 14sp
- Optional action link
- Subtle gradient background
```

---

## Quick Start

1. Open [stitch.withgoogle.com](https://stitch.withgoogle.com)
2. Create project "Mifos Mobile - Notifications"
3. Copy each screen prompt -> Generate
4. Generate components separately for reuse
5. Export all to Figma when done
