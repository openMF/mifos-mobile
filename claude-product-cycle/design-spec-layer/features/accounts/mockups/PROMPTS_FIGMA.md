# Accounts - Figma First Draft Prompts

> **Tool**: Figma First Draft (Shift+I in Figma)
> **Style**: Premium Fintech, Financial Command Center
> **Format**: Natural language prompts optimized for Figma AI

---

## How to Use

1. Open Figma → Create new design file
2. Press `Shift + I` or click "Actions" → "First Draft"
3. Copy each prompt below
4. Generate → Iterate → Refine

---

## Screen 1: Main Accounts View

```
Design a modern mobile accounts dashboard for "Mifos Mobile" fintech app that serves as a financial command center.

At the top, show "My Accounts" as the title with search, quick actions, and notification icons on the right. Add a filter button.

Create a horizontal tab bar with 5 tabs: All, Savings, Loans, Shares, and Goals. The "All" tab should be selected with a pill-shaped indicator below it.

Design a stunning hero card with a purple gradient background displaying the user's net worth of $18,450.00 in large white text. Add an eye icon to toggle visibility. Show two stat chips below: "+$1,250 This Month" in green and "-$380 Expenses" in a softer color. Include a Financial Health Score section with a circular progress ring showing 85 out of 100 with "EXCELLENT" rating and "+5 from last month" indicator.

Add a Spending Analytics section with a "This Week" dropdown. Show "$1,240 spent" prominently with "-12% vs last week" in green. Include a bar chart showing daily spending for Mon through Sun, with today's bar having a special dotted outline. Below the chart, add horizontal scrolling category chips: Food $320 (26%), Transport $180 (15%), Shopping $450 (36%), Bills $290 (23%) - each with a mini progress bar.

Create an AI Insights section with cards that have a subtle gradient border. Show a lightbulb icon with suggestions like "You could save $85/month by switching to a cheaper internet plan" with "Explore Options" and "Dismiss" buttons.

Design a Savings Accounts section showing 3 accounts with a total of $12,500. Add a "7-day streak!" badge with a fire emoji and "+$850 this month". Each account card should be white with rounded corners and swipeable. Show Primary Savings with a wallet icon, ****4521, $8,200.00 balance, and a goal progress bar at 65% showing "$8,200 / $12,600". Include gamification stats: fire emoji "7 days streak", trophy "3 goals", lightning "Auto-save ON $50/week".

Add a Loan Accounts section showing outstanding total of $6,200 and "Debt-free in 18 months" projection. Show Personal Loan with -$4,500 in red, 60% progress bar, next payment $350 due Jan 15 with "Schedule Payment" and "Pay Now" buttons.

Create an Upcoming Bills section with an overdue electricity bill showing a red urgency border, "$85.00", "OVERDUE - Was due Dec 28" with pulsing indicator, and "PAY NOW" and "Snooze" buttons. Show other upcoming bills with amber indicators.

At the bottom, add navigation with Home, Accounts (selected), a floating action button for Transfer in the center that pops up above the nav bar, and Profile.

Style it like Revolut or N26 - premium, data-rich, with purple as the primary accent color.
```

---

## Screen 2: Goals Tab View

```
Design a savings goals dashboard for a banking app when the Goals tab is selected.

Keep the same header and tab bar, but now show Goals as the selected tab.

Create a gradient hero card showing Goals Overview with key stats: 4 Active goals, 2 Achieved, 1 Paused, and $20,600 Total Goal amount. Show an overall progress bar at 62% with "$12,500 saved of $20,600 target".

Add an Achievements section with a horizontal row of badges. Show earned badges (7-day streak with fire, $5K saved with diamond, Goal Master with target) in full color with a golden glow. Show a Super Saver badge at 55% progress in grayscale with a progress ring. Show locked badges with a lock icon and question marks.

Create an Active Goals section with a "+ Create Goal" button. Show the Emergency Fund goal with a shield emoji, 65% progress ring, "$8,200 / $12,600", a "7-day streak" indicator with fire, and "Est. complete: Mar 2025". Include an auto-save info card showing "$50/week on Fridays" with Edit and Pause buttons.

Show the Bali Trip 2025 goal with a palm tree emoji, 45% progress ring, "$3,500 / $8,000", and an "Boost available!" callout. Add a suggestion card: "Add $200 now to reach goal 2 weeks earlier!" with "Boost Now" and "Maybe Later" buttons.

Add an Achieved Goals section with celebration emoji. Show completed goals like "New Laptop - $1,500 saved - Completed Dec 2024" with checkmarks.
```

---

## Screen 3: Spending Category Drill-Down

```
Design a category detail screen when the user taps on a spending category like Food & Dining.

Show a back arrow, "Food & Dining" as the title with a food emoji, and a Share button. Add a "This Week" dropdown to change the time period.

Create a hero section showing "$320.45 spent this week" prominently. Include a line chart showing the spending trend across the week. Add a comparison indicator showing "+15% vs last week" and a budget tracker showing "Budget: $400/week" with a progress bar at 80%.

Design a Sub-Categories section breaking down the spending: Restaurants $185 (58%), Coffee & Cafes $68.50 (21%), Groceries $45.95 (14%), Fast Food $21.00 (7%). Each should have a progress bar showing its percentage.

Add a Recent Transactions section grouped by date. For Today, show transactions like "Pizza Palace -$24.50, Restaurants, 2:30 PM, Visa ****4521" with appropriate category icons. For Yesterday, show "Whole Foods -$45.95" and "McDonald's -$12.50".

Include an AI Insight card at the bottom with a lightbulb: "You spent 58% on restaurants this week. Cooking at home 2 more days could save ~$50" with "Set Cooking Goal" and "Dismiss" buttons.
```

---

## Screen 4: Loading State

```
Design a loading skeleton screen for the accounts dashboard.

Keep the header visible with "My Accounts" but show shimmer placeholders for the filter tabs.

Create a hero card skeleton with the gradient background shape but shimmer placeholders where the net worth balance and stat chips would be. Show a shimmer circle where the health score ring would appear.

Show shimmer rectangles for the analytics section - bars for the chart and pill shapes for the category chips.

Display account card skeletons with shimmer for the icon circle on the left, text lines in the middle, and balance on the right.

The shimmer should have a subtle wave animation moving left to right. Use light gray gradients for the placeholder areas. Stagger the animation start times for visual interest. Make it feel premium and polished, not jarring.
```

---

## Screen 5: Empty State

```
Design an empty state for the accounts screen when a user has no accounts yet.

Show the same header and tab bar with all tabs visible.

In the center, display a friendly illustration of a briefcase or portfolio floating gently with a subtle up-down animation.

Show "Start Your Financial Journey" as a bold headline centered below the illustration. Add descriptive text: "Open your first account and take control of your finances with Mifos Mobile."

Create a "Suggested For You" section with two cards:
1. "Open Savings Account" with a bank icon, "Start saving with 4.5% APY", "Min. deposit: $0 • No monthly fees", and an "Open Now" button
2. "Join Investment Club" with a chart icon, "Community shares with 3% dividends", "Min. shares: 1 @ $50", and a "Learn More" button

Add a text link at the bottom: "Explore All Account Types →"

Make it feel encouraging and welcoming, like an opportunity rather than emptiness.
```

---

## Screen 6: Error State

```
Design an error state screen for when the accounts fail to load.

Keep the header and tabs visible normally.

In the center, show a cloud with an X icon illustration in a soft coral/red tint to indicate something went wrong without being alarming.

Display "Something Went Wrong" as the main heading in bold text.

Add explanatory text: "We couldn't load your accounts right now. Please check your connection and try again."

Create a "Try Again" button with a purple gradient background.

Below, add a "Check Network Settings" text link for users who need additional help.

Keep it calm and reassuring - not panic-inducing. The user should feel confident the app will work once the issue is resolved.
```

---

## Screen 7: Offline Mode

```
Design an offline state for the accounts screen showing cached data.

At the very top, add a slim warning banner in soft amber/yellow showing a no-wifi antenna icon and "You're offline" with a "Reconnect" button on the right.

Show the main header with "My Accounts" and add "Last updated 2 hours ago" as a subtitle in gray text.

Display the rest of the accounts content but at reduced opacity (about 70%) to indicate it's cached data. Add "(Cached)" labels to section headers.

Show the Net Worth card with a desaturated overlay and a "Data may be outdated" warning message.

For the Spending Analytics section, show an "Unavailable" badge and a placeholder message: "Data requires internet connection."

Display the account cards normally but with a banner at the top: "Read-only mode • Cannot make transactions". Disable the swipe actions visually.

The user should understand they can still see their data but it might not be current and they can't perform actions.
```

---

## Component Prompts

### Net Worth Hero Card
```
Design a financial net worth card for a premium banking app.

Use a beautiful diagonal gradient from purple #667EEA to deeper purple #764BA2. Make it full width with generous rounded corners (24px).

Show "NET WORTH" as a small white label with an eye icon to toggle balance visibility.

Display the main balance "$18,450.00" in large white bold text, centered, about 36sp font size.

Below, add two pill-shaped stat chips with semi-transparent white backgrounds: one showing an up arrow with "+$1,250 This Month" and another showing a down arrow with "-$380 Expenses".

Include a Financial Health Score section with a circular progress ring showing 85 out of 100 with "EXCELLENT" rating below it and "+5 from last month" change indicator.

Add a subtle shadow that uses the gradient colors at low opacity to create a glowing effect.
```

### Spending Analytics Chart
```
Design a weekly spending analytics section for a banking dashboard.

Create a bar chart with 7 bars representing Monday through Sunday. Each bar should be about 24px wide with the primary purple gradient color. Today's bar should have a special dotted outline to indicate the current day.

The chart height should be about 120px. Show the day labels below each bar.

Add a touch interaction where tapping a bar shows a tooltip with the exact spending amount for that day.

Below the chart, add horizontally scrolling category chips. Each chip shows a category emoji (like a burger for Food), the amount ($320), and the percentage (26%). Include a mini progress bar within each chip.

Animate the bars growing from the bottom when the section appears, with a staggered animation.
```

### AI Insight Card
```
Design an AI-powered insight card for a fintech app.

Create a card with a subtle gradient border (about 1px) that hints at the app's primary colors. Give the card a very light gradient background (5% opacity).

On the left, show a lightbulb icon indicating it's an AI suggestion.

In the middle, display the insight text: "You could save $85/month by switching to a cheaper internet plan."

At the bottom or right, add two action buttons: "Explore Options" as a primary action and "Dismiss" as a secondary/outlined action.

Make the card dismissible with a swipe-right gesture. Add a subtle animation when it appears, sliding in from the right.
```

### Account Card with Gamification
```
Design an enhanced bank account card with gamification elements.

Create a white card with soft shadows and 20px rounded corners.

On the left, show a circular icon container with a light gradient background (15% opacity) containing a wallet or bank icon.

In the middle section, display the account name "Primary Savings" in medium weight and the masked account number "****4521" in smaller gray text below.

On the right, align the balance "$8,200.00" in a larger, bold style with a change indicator "+$200 this month" in green below it.

Add a goal progress section showing an emoji (like a target), the goal name "Emergency Fund", a progress bar at 65%, and the amounts "$8,200 / $12,600".

At the bottom, include a row of gamification stats: fire emoji with "7 days streak", trophy emoji with "3 goals reached", and lightning bolt with "Auto-save ON $50/week".

Make the entire card swipeable to reveal quick action buttons.
```

---

## Tips for Figma First Draft

1. **Iterate**: Generate once, then refine with follow-up prompts
2. **Colors**: Mention "purple accent #667EEA" for consistency
3. **Reference**: Say "like Revolut" or "N26-style" for context
4. **States**: Ask for "show loading, empty, and error states"
5. **Responsive**: Mention "393px width for mobile" for correct sizing

---

## After Generation

1. Review generated designs for consistency
2. Create a color style library
3. Build component variants
4. Link screens with prototype connections
5. Export and update `FIGMA_LINKS.md`
