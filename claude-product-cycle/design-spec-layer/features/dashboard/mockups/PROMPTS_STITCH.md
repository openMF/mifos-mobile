# Dashboard - Google Stitch Prompts

> **Tool**: [Google Stitch](https://stitch.withgoogle.com)
> **Style**: Material Design 3, Modern Banking
> **Format**: Copy each prompt block directly into Stitch

---

## Design System Reference

```
Primary: #6750A4
Primary Container: #EADDFF
Surface: #FFFBFE
On Surface: #1C1B1F
On Surface Variant: #49454F
Error/Debit: #B3261E
Success/Credit: #2E7D32
Screen Size: 393 x 852 pixels (Android)
Font: Inter
```

---

## Screen 1: Main Dashboard

```
Mobile banking dashboard screen, Material Design 3, fintech app, 393x852px

Header:
- "Good morning, John" greeting, 24px bold, #1C1B1F
- "December 28, 2025" date below, 14px, #49454F
- Right side: notification bell + settings gear icons

Hero Card (Net Worth):
- Full width, purple gradient #6750A4, 28px corner radius
- "TOTAL NET WORTH" label white 12px + eye toggle icon
- Large balance "$45,750.00" white 45px centered
- "+$1,250.00 this month" with up arrow, white 80%
- Divider line white 20%
- Three columns below: Savings $52,500 | Loans -$6,750 | Shares $0

Quick Actions:
- "QUICK ACTIONS" section label
- 4 circular buttons row: Transfer, Deposit, Invest, Beneficiary
- Each: 48px icon in #EADDFF circle, label below

My Accounts Section:
- "MY ACCOUNTS" header + "View All" link right

Account Card 1:
- 40px purple circle with wallet icon
- "Primary Savings" title, "SA-0001234567 • Active" subtitle
- "$35,000.00" balance right, "Interest Rate: 4.5% p.a."
- White card, 12px radius, subtle shadow

Account Card 2:
- Same layout, "Emergency Fund", "$17,500.00"

Account Card 3 (Loan):
- Bank icon, "Personal Loan"
- "-$6,750.00" red text
- Progress bar 80% filled purple on gray
- "Due: Jan 15"

Recent Activity:
- "RECENT ACTIVITY" header + "View All" link
- "Today" date label

Transaction 1:
- Green circle down arrow, "Salary Credit"
- "Primary Savings • 09:30 AM" gray
- "+$4,500.00" green right

Transaction 2:
- Red circle up arrow, "Bill Payment"
- "Primary Savings • 08:15 AM" gray
- "-$150.00" red right

Bottom Navigation:
- 80px height, 5 tabs: Home (selected), Accounts, Transfer, Activity, Profile
- Selected: pill indicator behind icon, primary color
```

---

## Screen 2: Account Detail

```
Savings account detail screen, Material Design 3, banking app, 393x852px

Top Bar:
- Back arrow left
- "Primary Savings" title 22px
- 3-dot menu right
- Surface color background

Balance Hero:
- Large centered "$35,000.00" 36px
- "Available Balance" label below
- "SA-0001234567" account number gray
- "Status: Active • Since: Jan 2023"

Action Buttons Row:
- 4 equal outlined buttons with icons above labels:
- Transfer | Withdraw | Statement | QR Pay
- 8px gap between

Account Details Card:
- "ACCOUNT DETAILS" header
- Two-column key-value pairs:
  - Product Name: Savings Plus
  - Interest Rate: 4.5% per annum
  - Total Deposits: $42,500.00
  - Total Withdrawals: $7,500.00
  - Total Interest: $1,250.00
  - Minimum Balance: $500.00
- Elevated card 12px radius

Transactions Section:
- "TRANSACTIONS" header + Filter dropdown right
- List items with dividers:
  - Dec 28: Salary Credit +$4,500.00 green
  - Dec 27: Transfer to John -$500.00 red
  - Dec 26: Interest Posting +$45.50 green
  - Dec 25: ATM Withdrawal -$200.00 red
  - Dec 24: Online Purchase -$89.99 red

Charges Section:
- "CHARGES" header
- Monthly Maintenance | Due: Jan 1 | $5.00
- Annual Fee | Paid: Dec 1 | $25.00 checkmark (gray bg)
```

---

## Screen 3: Transfer Flow

```
Money transfer screen, Material Design 3, banking app, 393x852px

Top Bar:
- Back arrow left
- "Transfer Money" title
- Surface background

From Account Selector:
- "FROM ACCOUNT" label 12px gray
- Dropdown showing:
  - Wallet icon, "Primary Savings"
  - "Available: $35,000.00" green
- Chevron down right
- Outlined container 4px radius

Transfer Type Toggle:
- "TO" label
- Three radio options row:
  - My Account | Beneficiary (selected primary) | New Recipient

Beneficiary List:
- "SELECT BENEFICIARY" label

Beneficiary Card 1 (selected):
- Avatar "JD" initials circle
- "Jane Doe" name
- "****4567 • Mifos Bank" masked
- Radio selected indicator

Beneficiary Card 2:
- Avatar "MS" initials
- "Mike Smith"
- "****8901 • Mifos Bank"
- Radio unselected

Amount Input:
- "AMOUNT" label
- Large text field, "$" prefix
- "500.00" value 36px
- Primary color border focused

Remarks Input:
- "REMARKS (Optional)" label
- "Payment for dinner" entered
- Outlined unfocused

Continue Button:
- Full width filled button
- "CONTINUE TO REVIEW"
- Primary #6750A4
- 48px height, full radius
- 16px from bottom
```

---

## Screen 4: Transfer Confirmation

```
Transfer confirmation screen, Material Design 3, banking app, 393x852px

Top Bar:
- Back arrow, "Confirm Transfer" title

Summary Card:
- White elevated card, 16px radius
- Amount large centered "$500.00" 36px
- Arrow down icon

From Section:
- "FROM" label gray
- Wallet icon, "Primary Savings"
- "SA-0001234567"

To Section:
- "TO" label gray
- Avatar "JD", "Jane Doe"
- "****4567 • Mifos Bank"

Details:
- Transfer Type: Beneficiary Transfer
- Remarks: Payment for dinner
- Fee: $0.00
- Total: $500.00

Confirm Button:
- Full width, "CONFIRM TRANSFER"
- Primary filled

Cancel link below, centered gray
```

---

## Screen 5: Transfer Success

```
Transfer success screen, Material Design 3, banking app, 393x852px

Center content:

Success Animation:
- Large green checkmark 120px
- Circular green (#2E7D32) background with pulse
- Confetti particles around

Title:
- "Transfer Successful!" 28px bold

Amount:
- "$500.00" 36px

Details:
- "Sent to Jane Doe"
- "Dec 28, 2025 at 10:30 AM"
- Reference: TXN-2025122800001

Action Buttons:
- "Share Receipt" outlined button
- "Done" filled primary button

Bottom:
- "Transfer another" text link
```

---

## Screen 6: All Accounts

```
Accounts list screen, Material Design 3, banking app, 393x852px

Top Bar:
- "My Accounts" title 22px
- Filter icon right

Summary Card:
- "TOTAL BALANCE" label
- "$45,750.00" large
- Savings: $52,500 | Loans: -$6,750 | Shares: $0

Tabs:
- All | Savings | Loans | Shares
- "All" selected with underline indicator

Account List:

Savings Section:
- "SAVINGS ACCOUNTS (2)" header

Card 1:
- Wallet icon purple circle
- "Primary Savings"
- "SA-0001234567 • Active"
- "$35,000.00"
- Chevron right

Card 2:
- "Emergency Fund"
- "$17,500.00"

Loans Section:
- "LOAN ACCOUNTS (1)" header

Card:
- Bank icon
- "Personal Loan"
- "LA-0009876543 • Active"
- "-$6,750.00" red
- Progress 80%

FAB:
- Bottom right floating action button
- Plus icon, primary color
- "Open Account" label on long press
```

---

## Components

### Net Worth Card
```
Financial summary card component, Material Design 3:
- 361px width, auto height ~180px
- Gradient #6750A4 to #7E67B0
- 28px corner radius, 20px padding
- "TOTAL NET WORTH" white 12px + eye icon
- "$45,750.00" white 45px centered
- "+$1,250.00 this month" white 80%
- Divider white 20%
- 3 columns: Savings $52,500 | Loans -$6,750 | Shares $0
- Level 2 shadow
```

### Account Card
```
Bank account card component, Material Design 3:
- 361px width, ~88px height
- White #FFFBFE, 12px radius, level 1 shadow
- 16px padding
- Left: 40px icon circle #EADDFF
- Center: Title 16px, subtitle 12px gray
- Right: Balance 16px
- Loan variant: Add progress bar below
```

### Transaction Item
```
Transaction list item, Material Design 3:
- 361px width, 72px height
- Left: 40px indicator circle
  - Credit: green #C8E6C9 bg, down arrow #2E7D32
  - Debit: red #F9DEDC bg, up arrow #B3261E
- Center: Description 16px, Account+time 12px gray
- Right: Amount colored (green credit, red debit)
```

### Quick Action Button
```
Quick action circular button, Material Design 3:
- 64px total height
- 48px icon circle, #EADDFF background
- Icon 24px, #6750A4
- Label below 12px, #49454F
- Tap state: ripple effect
```

---

## Quick Start

1. Open [stitch.withgoogle.com](https://stitch.withgoogle.com)
2. Create project "Mifos Mobile - Dashboard"
3. Copy each screen prompt → Generate
4. Generate components separately for reuse
5. Export all to Figma when done
