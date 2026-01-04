# Home Dashboard - Figma First Draft Prompts

> **Tool**: Figma First Draft (Shift+I in Figma)
> **Style**: Premium Fintech, Modern Banking App
> **Format**: Natural language prompts optimized for Figma AI

---

## How to Use

1. Open Figma → Create new design file
2. Press `Shift + I` or click "Actions" → "First Draft"
3. Copy each prompt below
4. Generate → Iterate → Refine

---

## Screen 1: Home Dashboard

```
Design a modern mobile banking home dashboard for "Mifos Mobile" app.

At the top, show a personalized greeting "Good Morning" with the user's name "Maria" below it in large bold text. Add a notification bell icon on the right with a badge showing 3 notifications.

Create a large hero card with a beautiful purple gradient background. Show "Total Balance" as a small label with an eye icon to toggle visibility. Display "$12,450.00" in big white bold text centered on the card. Below the balance, show two small stat chips - one green showing "+$2,450 This Month" for income, and one showing "-$890 Expenses" in a softer color.

Add a "This Week" spending analytics section with a mini line chart showing daily spending. Show "$847.50 spent" prominently with a "-12% vs last week" indicator in green. Below the chart, add horizontal scrolling category chips showing Shopping $342, Food $215, Transport $145 with small progress bars.

Create a Quick Actions row with 5 circular buttons: Send (arrow icon), Request (download icon), QR (qr code icon), Cards (card icon), and Freeze (lock icon). Each should have a light purple circular background with the icon inside and label below.

Add a "Send Again" section showing recent contacts as circular avatars in a horizontal scroll. Show initials like JD, SK, MR with different colors, plus a "+" button for new recipient.

Create a "My Accounts" section with expandable cards. Show a Primary Savings account with wallet icon, "$8,200.00" balance, "82% to goal" progress bar, and an Active status badge. Show a Home Loan with bank icon, "-$45,200.00" in red, "28% paid" progress bar, and "Due: Jan 15" indicator.

Add an "Upcoming Bills" section with bill cards. Show an Electricity Bill due tomorrow with a red urgency indicator on the left border, "$142.50", with "Pay Now" and "Schedule" buttons. Show Rent Payment with normal priority.

Include a "Savings Goals" horizontal scroll with cards showing circular progress rings. Show "Vacation Fund" at 60% with a fire emoji and "15 day streak" gamification element.

Add a "Recent Activity" section showing today's transactions with category icons, descriptions, times, and amounts. Amazon Prime -$14.99 in red, Salary Deposit +$3,500.00 in green.

Include an AI Assistant card with gradient border, robot icon, and suggestion chips like "How much did I spend?" and "Transfer to John".

At the bottom, add navigation with Home (selected), Accounts, a floating action button for Transfer that pops up above the nav bar, and Profile. Style it like Revolut or Cash App - clean and modern with purple as the primary color.
```

---

## Screen 2: Loading State

```
Design a loading skeleton screen for a banking app home dashboard.

Keep the greeting "Good Morning" visible at the top. Show the user's name as a shimmer placeholder bar.

Create a hero balance card with gradient background but show the balance and stat areas as animated shimmer rectangles.

Show the quick action buttons as shimmer circles in a row.

Display account cards as white card shapes with shimmer placeholders for the icon, text lines, and balance.

The shimmer should look like a subtle wave animation moving left to right. Use light gray gradients for the placeholder areas. Make it feel premium and polished.
```

---

## Screen 3: Empty State

```
Design an empty state for a banking app when the user has no accounts yet.

Show the same greeting header with name and notifications.

Display a hero card with "$0.00" balance and $0 for both income and expenses.

Keep the quick actions row visible but perhaps slightly dimmed.

In the center, show a friendly illustration of a document or chart floating gently. Below it, display "No Accounts Yet" as a bold headline.

Add descriptive text: "Start your financial journey by opening your first account."

Create a prominent gradient button saying "+ Open New Account" and a smaller "Learn More" text link below.

Make it feel encouraging and welcoming, not empty or broken.
```

---

## Screen 4: Error State

```
Design an error state screen for when the banking app fails to load data.

Show the greeting header normally.

In the center, display a warning triangle illustration with a subtle red/coral tint to indicate something went wrong.

Show "Something Went Wrong" as the main heading in bold text.

Add explanatory text: "We couldn't load your account information. Please check your connection and try again."

Create a "Try Again" button with purple gradient background.

Below, add a "Contact Support" text link for users who need help.

Keep it calm and reassuring - not alarming. Use soft red accents, not harsh ones.
```

---

## Screen 5: Offline Mode

```
Design an offline banner state for a banking app.

At the very top, add a slim warning banner in soft amber/yellow showing an antenna icon and "You're offline. Some features unavailable."

Show the rest of the home dashboard content but at reduced opacity (about 70%) to indicate it's cached data.

Add a "Last sync: 2 min ago" indicator somewhere visible.

The user should understand they can still see their data but it might not be current. If they pull to refresh, show "Trying to reconnect..." message.
```

---

## Component Prompts

### Hero Balance Card
```
Design a financial balance card for a banking app dashboard.

Use a beautiful diagonal gradient from purple-blue #667EEA to purple #764BA2. Make it full width with generous rounded corners about 24px.

Show "Total Balance" as a small white label with an eye icon next to it for hiding the balance.

Display the main balance "$12,450.00" in large white bold text, centered.

Below, add two small pill-shaped indicators: one showing an up arrow with "+$2,450 This Month" and another showing a down arrow with "-$890 Expenses". Give them semi-transparent white backgrounds.

Add a subtle shadow that uses the gradient colors at low opacity to create a glow effect.
```

### Quick Action Button
```
Design a quick action button for a banking dashboard.

Create a square container with rounded corners (16px) that's about 56x56 pixels.

Give it a very light purple tinted background. Place an icon in the center in a deeper purple color.

Add a label below the square in small dark text.

Show the default state, pressed state with slight scale down and ripple effect, and disabled state at 50% opacity.
```

### Account Card
```
Design a bank account card component.

Create a white card with soft shadow and 16px rounded corners.

On the left, show a circular icon container with a light tinted background and account type icon inside.

In the middle, show the account name in medium weight and the account number in smaller gray text below.

On the right, align the balance amount with a small trend indicator arrow.

For loan accounts, add a thin progress bar at the bottom showing percentage paid.

Include an Active status badge in green.
```

### Transaction Item
```
Design a transaction list item for a banking app.

Create two variants - one for money received (credit) and one for money spent (debit).

For credit: Show a green-tinted circular background with a download arrow, and the amount in green with a + prefix.

For debit: Show a red-tinted circular background with an upload arrow, and the amount in red with a - prefix.

Each shows a merchant logo or category icon in the circle, the description and account name in the middle, time in small gray text, and the amount on the right.
```

---

## Tips for Figma First Draft

1. **Iterate**: Generate once, then refine with follow-up prompts
2. **Colors**: Mention "purple accent #667EEA" for consistency
3. **Reference**: Say "like Revolut" or "Cash App style" for context
4. **States**: Ask for "show loading, empty, and error states"
5. **Responsive**: Mention "393px width for mobile" for correct sizing

---

## After Generation

1. Review generated designs for consistency
2. Create a color style library
3. Build component variants
4. Link screens with prototype connections
5. Export and update `FIGMA_LINKS.md`
