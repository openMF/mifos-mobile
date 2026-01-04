# Dashboard - Figma First Draft Prompts

> **Tool**: Figma First Draft (Shift+I in Figma)
> **Style**: Modern Banking, Material Design 3
> **Format**: Natural language prompts optimized for Figma AI

---

## How to Use

1. Open Figma → Create new design file
2. Press `Shift + I` or click "Actions" → "First Draft"
3. Copy each prompt below
4. Generate → Iterate → Refine

---

## Screen 1: Main Dashboard

```
Design a modern mobile banking dashboard for "Mifos Mobile" app.

At the top, show a personalized greeting "Good morning, John" with today's date below it. Add notification and settings icons on the right side.

Create a large purple gradient hero card showing the user's total net worth of $45,750.00 in big white text. Include an eye icon to hide/show the balance. Below the amount, show "+$1,250.00 this month" as a positive indicator. At the bottom of the card, display a breakdown: Savings $52,500, Loans -$6,750, Shares $0.

Add a Quick Actions section with 4 circular buttons in a row: Transfer (send money icon), Deposit (download icon), Invest (chart icon), and Beneficiary (people icon). Each should have a light purple background circle with the icon inside.

Create a "My Accounts" section with a "View All" link. Show 3 account cards stacked:
1. Primary Savings - $35,000.00, active, 4.5% interest rate
2. Emergency Fund - $17,500.00, 3.2% interest rate
3. Personal Loan - negative $6,750.00 in red, with a progress bar showing 80% paid, due Jan 15

Add a "Recent Activity" section showing today's transactions:
- Salary Credit: +$4,500.00 in green with a down arrow icon
- Bill Payment: -$150.00 in red with an up arrow icon

At the bottom, add a navigation bar with 5 tabs: Home (selected with indicator), Accounts, Transfer, Activity, Profile.

Style it like Revolut or Monzo - clean, modern, with purple as the primary accent color.
```

---

## Screen 2: Account Detail

```
Design a savings account detail screen for a banking app.

Show a back arrow and "Primary Savings" as the title in the top bar, with a three-dot menu on the right.

Display the available balance prominently: $35,000.00 in large centered text. Below it show "Available Balance", the account number "SA-0001234567", and status "Active since Jan 2023".

Create 4 action buttons in a row: Transfer, Withdraw, Statement, and QR Pay. Each should be an outlined button with an icon above the label.

Add an Account Details card showing key information in a two-column layout:
- Product Name: Savings Plus
- Interest Rate: 4.5% per annum
- Total Deposits: $42,500.00
- Total Withdrawals: $7,500.00
- Total Interest Earned: $1,250.00
- Minimum Balance: $500.00

Show a Transactions section with a filter dropdown. List recent transactions:
- Dec 28: Salary Credit +$4,500.00 (green)
- Dec 27: Transfer to John -$500.00 (red)
- Dec 26: Interest Posting +$45.50 (green)
- Dec 25: ATM Withdrawal -$200.00 (red)

Add a Charges section showing:
- Monthly Maintenance, Due Jan 1, $5.00
- Annual Fee, Paid Dec 1, $25.00 with a checkmark
```

---

## Screen 3: Transfer Money

```
Design a money transfer screen for a banking app.

Show a back arrow and "Transfer Money" as the title.

Create a "From Account" selector that looks like a dropdown. Display a wallet icon, "Primary Savings" as the account name, and "Available: $35,000.00" in green. Add a chevron icon indicating it can be tapped to change.

Add transfer type options as a row of 3 radio buttons: "My Account", "Beneficiary" (selected), and "New Recipient".

Show a beneficiary selection section with 2 cards:
1. Jane Doe - with avatar showing "JD" initials, masked account "****4567 • Mifos Bank", selected state
2. Mike Smith - with avatar showing "MS" initials, masked account "****8901 • Mifos Bank", unselected

Create a large amount input field with a dollar sign prefix, showing "500.00" entered. Make it prominent and easy to read.

Add a remarks field labeled "Remarks (Optional)" with "Payment for dinner" as example text.

At the bottom, add a prominent "Continue to Review" button in purple that spans the full width.
```

---

## Screen 4: Transfer Confirmation

```
Design a transfer confirmation screen for a banking app.

Show a back arrow and "Confirm Transfer" title.

Create a summary card in the center showing the transfer amount "$500.00" prominently with a downward arrow icon below it.

Display the From section:
- Wallet icon with "Primary Savings"
- Account number below

Display the To section:
- Avatar with initials "JD"
- "Jane Doe" name
- Masked account number

Show transfer details:
- Transfer Type: Beneficiary Transfer
- Remarks: Payment for dinner
- Fee: $0.00
- Total: $500.00

Add a large "Confirm Transfer" button in purple at the bottom.

Below the button, add a "Cancel" text link in gray.
```

---

## Screen 5: Transfer Success

```
Design a success celebration screen shown after completing a money transfer.

In the center, show a large green checkmark icon with a circular background. Add subtle confetti or celebration particles around it to make it feel rewarding.

Display "Transfer Successful!" as a bold heading.

Show the amount "$500.00" prominently below.

Add details:
- "Sent to Jane Doe"
- Date and time "Dec 28, 2025 at 10:30 AM"
- Reference number "TXN-2025122800001"

Create two buttons:
1. "Share Receipt" as an outlined button
2. "Done" as a filled purple button

At the bottom, add a "Transfer another" text link for quick repeat actions.

Make it feel celebratory but professional, like a premium banking app confirmation.
```

---

## Screen 6: All Accounts

```
Design an accounts list screen for a banking app.

Show "My Accounts" as the title with a filter icon on the right.

Create a summary card at the top showing:
- "Total Balance" label
- "$45,750.00" in large text
- Breakdown: Savings $52,500, Loans -$6,750, Shares $0

Add tabs below: All (selected), Savings, Loans, Shares. The selected tab should have an underline indicator.

Group accounts by type:

Savings Accounts section (2):
1. Primary Savings
   - Wallet icon in purple circle
   - Account number and Active status
   - $35,000.00 balance
   - Chevron indicating tappable

2. Emergency Fund
   - $17,500.00 balance

Loan Accounts section (1):
1. Personal Loan
   - Bank icon
   - Account number and Active status
   - -$6,750.00 in red
   - Progress bar showing 80% paid

Add a floating action button (FAB) in the bottom right corner with a plus icon for "Open Account".
```

---

## Screen 7: Activity/Transactions

```
Design a transaction history screen for a banking app.

Show "Activity" as the title with filter and search icons on the right.

Add a date range selector showing "This Month" with dropdown arrows.

Create a summary row showing:
- Income: +$8,500.00 in green
- Expenses: -$2,340.00 in red
- Net: +$6,160.00

Group transactions by date:

Today section:
- Salary Credit from "Employer Inc" at 09:30 AM, +$4,500.00 green
- Bill Payment to "Electric Co" at 08:15 AM, -$150.00 red

Yesterday section:
- Transfer to "Jane Doe" at 03:45 PM, -$500.00 red
- Interest Credit at 12:00 AM, +$45.50 green

Each transaction should show:
- Category icon in a colored circle (green for credit, red for debit)
- Description and merchant/account name
- Time
- Amount with color coding

Add "Load More" at the bottom for pagination.
```

---

## Component Prompts

### Net Worth Hero Card
```
Design a financial summary card for a banking app dashboard.

Use a purple gradient background from dark purple to lighter purple. Make it full width with generous rounded corners (about 28px).

Show "TOTAL NET WORTH" as a small label with an eye icon next to it for privacy toggle.

Display "$45,750.00" as the main balance in large white text, centered.

Below, show "+$1,250.00 this month" with an up arrow to indicate positive change.

Add a subtle divider line.

At the bottom, create 3 equal columns showing: Savings $52,500, Loans -$6,750, Shares $0 - all in white text.
```

### Account Card
```
Design a bank account card component.

White background with subtle shadow, rounded corners (12px).

Layout horizontally:
- Left: A 40px circular icon container with light purple background and wallet icon
- Middle: Account name in medium weight, account number and status in smaller gray text below
- Right: Balance amount aligned right

For loan variant: Add a thin progress bar below the content showing percentage paid.
```

### Transaction Item
```
Design a transaction list item for a banking app.

Create two variants:
1. Credit (money received): Green circular background with down arrow, amount in green with + prefix
2. Debit (money spent): Red circular background with up arrow, amount in red with - prefix

Each shows:
- Category indicator circle on left
- Description and account/merchant name in the middle
- Time in small gray text
- Amount on the right with appropriate color
```

### Quick Action Button
```
Design a quick action button for a banking dashboard.

Create a circular button with light purple background (48px diameter).
Place an icon in the center in darker purple.
Add a label below the circle in small text.

Show 4 variants: Transfer, Deposit, Invest, Beneficiary - each with appropriate icons.
```

---

## Tips for Figma First Draft

1. **Iterate**: Generate once, then refine with follow-up prompts
2. **Colors**: Mention "purple accent #6750A4" for consistency
3. **Reference**: Say "like Revolut" or "Monzo-style" for context
4. **States**: Ask for "show selected and unselected states"
5. **Responsive**: Mention "393px width for mobile" for correct sizing

---

## After Generation

1. Review generated designs for consistency
2. Create a color style library
3. Build component variants
4. Link screens with prototype connections
5. Export and update `FIGMA_LINKS.md`
