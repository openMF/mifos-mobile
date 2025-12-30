# Dashboard - AI Mockup Prompts

> **Generated from**: features/dashboard/SPEC.md
> **Generated on**: 2025-12-28
> **Feature**: Unified Account Management Dashboard

---

## Quick Reference

| AI Tool | Best For | Export to Figma |
|---------|----------|-----------------|
| Google Stitch | Material Design, detailed prompts | Yes (native) |
| Uizard | Quick iterations, sketches | No |
| Visily | Component-focused, lo-fi to hi-fi | Yes |

---

## Screen 1: Main Dashboard

### Google Stitch Prompt (Recommended)

```
Create a mobile banking dashboard screen with Material Design 3:

**App Context:**
Mifos Mobile - Self-service banking app for viewing accounts, transactions, and making transfers.

**Screen Size:** 393 x 852 pixels (iPhone 14 Pro equivalent)

**Header Section:**
- Status bar at top (time, battery, signal icons)
- Greeting text "Good morning, John" in Headline Small (24px)
- Date "December 28, 2025" in Body Medium (14px), gray color
- Two icon buttons on right: notification bell and settings gear

**Hero Card - Net Worth:**
- Full-width card with purple gradient background (#6750A4)
- "TOTAL NET WORTH" label in white, Label Medium (12px)
- Eye icon for privacy toggle on right of label
- Large amount "$ 45,750.00" in Display Medium (45px), white
- Monthly change "+$1,250.00 this month" with up arrow, white 80% opacity
- Three-column breakdown below:
  - Savings: $52,500
  - Loans: -$6,750
  - Shares: $0
- Card has 28px corner radius

**Quick Actions Row:**
- "QUICK ACTIONS" section label
- 4 circular buttons in a row, evenly spaced:
  1. Transfer (money send icon)
  2. Deposit (download icon)
  3. Invest (chart icon)
  4. Beneficiary (people icon)
- Each has 48px icon container with primary container color (#EADDFF)
- Label below each icon in Label Medium

**My Accounts Section:**
- "MY ACCOUNTS" header with "View All →" link on right
- Three account cards stacked vertically:

Card 1 - Savings:
- 40px circular icon (wallet) with purple background
- "Primary Savings" title, "SA-0001234567 • Active" subtitle
- "$ 35,000.00" balance on right
- "Interest Rate: 4.5% p.a." in small text
- White background, 12px corner radius, subtle shadow

Card 2 - Emergency Fund:
- Same layout as Card 1
- Balance: "$ 17,500.00"
- "Interest Rate: 3.2% p.a."

Card 3 - Loan:
- Bank icon instead of wallet
- "Personal Loan" title
- Balance: "-$ 6,750.00" (red text)
- Progress bar showing 80% paid (purple fill on gray track)
- "Due: Jan 15" text

**Recent Activity Section:**
- "RECENT ACTIVITY" header with "View All →" link
- "Today" date label in gray
- Two transaction items:

Transaction 1:
- Green circle with down arrow (credit)
- "Salary Credit" description
- "Primary Savings • 09:30 AM" in gray
- "+ $4,500.00" in green on right

Transaction 2:
- Red circle with up arrow (debit)
- "Bill Payment" description
- "Primary Savings • 08:15 AM" in gray
- "- $150.00" in red on right

**Bottom Navigation:**
- 80px height, surface container background
- 5 tabs with icons and labels:
  1. Home (house icon) - SELECTED with indicator pill
  2. Accounts (card icon)
  3. Transfer (send icon)
  4. Activity (chart icon)
  5. Profile (person icon)
- Selected tab has secondary container color pill behind icon

**Style Guidelines:**
- Font: Inter (all weights)
- Primary color: #6750A4
- Surface: #FFFBFE
- On Surface: #1C1B1F
- On Surface Variant: #49454F
- Error/Debit: #B3261E
- Success/Credit: #2E7D32
- Standard padding: 16px horizontal
- Component gap: 8-16px
- Card shadow: subtle (level 1)
```

### Uizard Prompt

```
Mobile banking dashboard app:

Header:
- "Good morning, John" greeting
- Notification and settings icons

Main card (purple):
- "$45,750.00" large balance
- Eye icon to hide amount
- Savings/Loans/Shares breakdown

4 quick action buttons:
- Transfer, Deposit, Invest, Beneficiary

Account list:
- Primary Savings: $35,000
- Emergency Fund: $17,500
- Personal Loan: -$6,750 with progress bar

Recent transactions:
- Salary +$4,500 (green)
- Bill Payment -$150 (red)

Bottom navigation:
- Home, Accounts, Transfer, Activity, Profile

Style: Material Design 3, purple accent (#6750A4), clean white background
```

### Visily Prompt

```
Design a financial dashboard mobile screen:

Components needed:
□ Status bar: Time, battery, signal
□ Header: Greeting "Good morning, John", date, notification bell, settings gear
□ Hero card (purple #6750A4):
  - "TOTAL NET WORTH" label with eye toggle
  - Large balance "$45,750.00"
  - Monthly change indicator
  - 3-column breakdown: Savings | Loans | Shares
□ Quick actions row: 4 circular icon buttons with labels
□ Section header: "MY ACCOUNTS" with "View All" link
□ Account card x3: Icon, name, number, status, balance
□ Section header: "RECENT ACTIVITY" with "View All" link
□ Transaction items: Icon, description, account, time, amount (colored)
□ Bottom navigation: 5 tabs with icons and labels, selected state

Material Design 3 styling
393x852 pixels
Inter font family
```

---

## Screen 2: Account Detail

### Google Stitch Prompt

```
Create a savings account detail screen with Material Design 3:

**Screen Size:** 393 x 852 pixels

**Top Bar:**
- Back arrow on left
- "Primary Savings" as title in Title Large (22px)
- Overflow menu (3 dots) on right
- 64px height, surface color background

**Balance Hero:**
- Large centered amount "$ 35,000.00" in Display Small (36px)
- "Available Balance" label below in Body Medium
- Account number "SA-0001234567" in gray
- "Status: Active • Since: Jan 2023"
- White background, subtle bottom border

**Action Buttons Row:**
- 4 equal-width buttons:
  1. Transfer (outlined)
  2. Withdraw (outlined)
  3. Statement (outlined)
  4. QR Pay (outlined)
- Each has icon above label
- 8px gap between buttons
- 16px horizontal padding

**Account Details Card:**
- "ACCOUNT DETAILS" header
- Key-value pairs in two columns:
  - Product Name: Savings Plus
  - Interest Rate: 4.5% per annum
  - Total Deposits: $42,500.00
  - Total Withdrawals: $7,500.00
  - Total Interest: $1,250.00
  - Minimum Balance: $500.00
- Elevated card with 12px radius

**Transactions Section:**
- "TRANSACTIONS" header with Filter dropdown on right
- List of transactions:
  - Dec 28: Salary Credit +$4,500.00
  - Dec 27: Transfer to John -$500.00
  - Dec 26: Interest Posting +$45.50
  - Dec 25: ATM Withdrawal -$200.00
  - Dec 24: Online Purchase -$89.99
- Each row: Date, Description, Amount (colored)
- Dividers between items

**Charges Section:**
- "CHARGES" header
- Two charge items:
  - Monthly Maintenance | Due: Jan 1 | $5.00
  - Annual Fee | Paid: Dec 1 | $25.00 ✓
- Gray background for paid items

**Style:** Material Design 3, same colors as dashboard
```

---

## Screen 3: Transfer Flow

### Google Stitch Prompt

```
Create a money transfer screen with Material Design 3:

**Screen Size:** 393 x 852 pixels

**Top Bar:**
- Back arrow on left
- "Transfer Money" title
- No right actions
- Surface color background

**From Account Selector:**
- "FROM ACCOUNT" label in Label Medium, gray
- Dropdown/selector showing:
  - Wallet icon
  - "Primary Savings" account name
  - "Available: $35,000.00" in green
- Chevron down icon on right
- Outlined container with 4px radius

**Transfer Type Toggle:**
- "TO" label
- Three radio options in a row:
  - ○ My Account
  - ● Beneficiary (selected)
  - ○ New Recipient
- Selected option has primary color

**Beneficiary List:**
- "SELECT BENEFICIARY" label
- Two beneficiary cards:

Card 1:
- Avatar with "JD" initials
- "Jane Doe" name
- "****4567 • Mifos Bank" masked account
- Radio selected indicator

Card 2:
- Avatar with "MS" initials
- "Mike Smith" name
- "****8901 • Mifos Bank" masked account
- Radio unselected

**Amount Input:**
- "AMOUNT" label
- Large text field with currency prefix "$"
- "500.00" entered value in Display Small
- Outlined style with primary color border (focused)

**Remarks Input:**
- "REMARKS (Optional)" label
- Text field with "Payment for dinner" entered
- Outlined style, unfocused

**Continue Button:**
- Full-width filled button
- "CONTINUE TO REVIEW" label
- Primary color (#6750A4)
- 48px height, full corner radius
- 16px from bottom

**Style:** Material Design 3, form-focused layout
```

---

## Component Prompts

### Net Worth Card (Standalone)

```
Create a financial summary card component:

- Width: 361px (full width minus 32px padding)
- Height: Auto (approximately 180px)
- Background: Linear gradient from #6750A4 to #7E67B0
- Corner radius: 28px
- Padding: 20px horizontal, 20px vertical

Content:
- Top row: "TOTAL NET WORTH" (Label Medium, white) + eye icon
- Main amount: "$45,750.00" (Display Medium, white, centered)
- Change indicator: "↑ +$1,250.00 this month" (Body Medium, white 80%)
- Divider line (white 20%)
- Bottom row with 3 equal columns:
  - Savings: $52,500 (white)
  - Loans: -$6,750 (white)
  - Shares: $0 (white)

Shadow: Level 2 elevation
```

### Account Card (Standalone)

```
Create a bank account card component:

- Width: 361px
- Height: Auto (approximately 88px)
- Background: White (#FFFBFE)
- Corner radius: 12px
- Shadow: Level 1 elevation
- Padding: 16px

Layout (horizontal):
- Left: 40px circular icon container (#EADDFF background)
- Center (flex):
  - Account name in Title Medium (#1C1B1F)
  - Account number + status in Body Small (#49454F)
  - Optional: Interest rate or due date
- Right: Balance in Title Medium

For loan variant:
- Add 8px tall progress bar below content
- Track: #E7E0EC, Fill: #6750A4
- "XX% paid" label below
```

### Transaction Item (Standalone)

```
Create a transaction list item component:

- Width: 361px
- Height: 72px
- Background: Transparent
- Padding: 8px vertical, 0 horizontal

Layout (horizontal):
- Left: 40px circular indicator
  - Credit: Green (#C8E6C9) background, down arrow (#2E7D32)
  - Debit: Red (#F9DEDC) background, up arrow (#B3261E)
- Center (flex):
  - Description in Body Large (#1C1B1F)
  - Account + time in Body Small (#49454F)
- Right: Amount in Title Medium
  - Credit: Green (#2E7D32) with + prefix
  - Debit: Red (#B3261E) with - prefix
```

---

## Export Instructions

### Google Stitch → Figma

1. Go to [stitch.withgoogle.com](https://stitch.withgoogle.com/)
2. Sign in with Google account
3. Paste the prompt above
4. Click "Generate"
5. Review and iterate if needed
6. Click "Export to Figma"
7. Open in Figma and refine

### After Figma Export

1. **Authenticate Figma MCP** (already connected):
   ```
   # MCP will prompt for authentication on first use
   ```

2. **Get Figma file URL** after export

3. **Run implementation**:
   ```
   /implement dashboard
   ```
   Claude will read the Figma design via MCP and generate matching Compose code.

---

## Design Token Reference

```json
{
  "colors": {
    "primary": "#6750A4",
    "onPrimary": "#FFFFFF",
    "primaryContainer": "#EADDFF",
    "surface": "#FFFBFE",
    "onSurface": "#1C1B1F",
    "onSurfaceVariant": "#49454F",
    "error": "#B3261E",
    "success": "#2E7D32"
  },
  "typography": {
    "displayMedium": "45px / 400",
    "displaySmall": "36px / 400",
    "headlineSmall": "24px / 400",
    "titleLarge": "22px / 400",
    "titleMedium": "16px / 500",
    "bodyLarge": "16px / 400",
    "bodyMedium": "14px / 400",
    "bodySmall": "12px / 400",
    "labelMedium": "12px / 500"
  },
  "spacing": {
    "screenPadding": "16px",
    "sectionGap": "24px",
    "componentGap": "8px",
    "cardPadding": "16px"
  },
  "radius": {
    "card": "12px",
    "heroCard": "28px",
    "button": "24px",
    "input": "4px"
  }
}
```
