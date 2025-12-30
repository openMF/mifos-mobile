# AI Mockup Prompt Template

> Use this template to generate prompts for AI design tools

---

## Base Prompt Structure

```
Create a [SCREEN_TYPE] mobile screen with Material Design 3:

**App Context:**
[APP_NAME] - [APP_DESCRIPTION]

**Screen Purpose:**
[SCREEN_PURPOSE]

**Header Section:**
- Top App Bar with [TITLE]
- [NAVIGATION_ICON] on left (back arrow / menu)
- [ACTION_ICONS] on right (e.g., notifications, settings)

**Main Content:**
[For each section from SPEC.md]
- [SECTION_NAME]: [SECTION_DESCRIPTION]
  - [COMPONENT_1]
  - [COMPONENT_2]

**Footer Section:**
- [BOTTOM_NAV / FAB / BUTTONS]

**Style Guidelines:**
- Color scheme: Purple primary (#6750A4), white surface (#FFFBFE)
- Typography: Inter font family, Material Design 3 type scale
- Spacing: 16px standard padding, 8px component gaps
- Corner radius: 12-28px for cards, full round for FABs
- Platform: Android mobile app
- Screen size: 393 x 852 pixels (iPhone 14 Pro equivalent)

**Interaction States:**
- Default, Hover/Press, Disabled states for buttons
- Selected/Unselected for navigation items
- Loading skeleton for data areas
```

---

## Tool-Specific Formats

### Google Stitch

Google Stitch works best with detailed, structured prompts:

```
Create a mobile banking dashboard with Material Design 3:

Screen shows user's financial overview with:
1. Greeting section with user name and date
2. Net worth card showing total balance ($45,750) with breakdown:
   - Savings: $52,500
   - Loans: -$6,750
   - Shares: $0
3. Quick action buttons: Transfer, Deposit, Invest, Beneficiary
4. Account cards list showing savings and loan accounts
5. Recent transactions with credit/debit indicators
6. Bottom navigation with 5 tabs

Style: Clean, modern Material Design 3
Colors: Purple (#6750A4) primary on white (#FFFBFE) surface
Typography: Inter font, clear hierarchy
Platform: Android mobile, 393x852px
```

### Uizard

Uizard prefers concise, key-feature focused prompts:

```
Mobile banking app dashboard:
- Header with greeting and notification icon
- Large card showing net worth with savings/loans breakdown
- 4 quick action buttons in a row
- List of account cards with balances
- Recent transactions section
- Bottom tab navigation

Style: Modern Material Design, purple accent
```

### Visily

Visily works well with component-focused prompts:

```
Design a financial dashboard mobile screen:

Components needed:
□ Top bar: "Dashboard" title, notification bell, settings gear
□ Hero card: Net worth display with eye toggle for privacy
□ Action row: 4 circular icon buttons with labels
□ Card list: Account cards with icon, name, balance, status
□ Transaction list: Grouped by date, credit/debit indicators
□ Bottom nav: 5 tabs with icons and labels

Material Design 3 styling
Purple primary color (#6750A4)
```

---

## Component Prompt Templates

### Net Worth Card
```
Create a hero card component showing:
- "TOTAL NET WORTH" label at top
- Large currency amount in white text
- Eye icon for privacy toggle
- Monthly change indicator (e.g., "+$1,250 this month")
- Three-column breakdown: Savings | Loans | Shares
- Purple gradient background (#6750A4)
- Rounded corners (28px radius)
```

### Account Card
```
Create an account card showing:
- Left: Circular icon with colored background
- Center: Account name (bold), account number, status badge
- Right: Balance amount
- Optional: Progress bar for loans
- White background with subtle shadow
- 12px corner radius
```

### Transaction Item
```
Create a transaction list item showing:
- Left: Circular icon (green arrow down for credit, red arrow up for debit)
- Center: Transaction description, account name, time
- Right: Amount with + or - prefix, colored by type
- 72px height, no background
```

### Quick Action Button
```
Create a quick action button showing:
- Circular icon container (48px) with primary container color
- Icon in center
- Label text below
- Vertical stack layout
- Tappable area extends to label
```

---

## Screen-Specific Prompts

### Dashboard Screen
```
Create a banking app dashboard:
- Top bar: "Dashboard" title, notification and settings icons
- Greeting: "Good morning, [Name]" with date
- Net worth card: Large balance display with breakdown
- Quick actions: 4 buttons (Transfer, Deposit, Invest, Beneficiary)
- "My Accounts" section: 2-3 account cards
- "Recent Activity": Grouped transaction list
- Bottom nav: Home, Accounts, Transfer, Activity, Profile
```

### Login Screen
```
Create a login screen:
- Centered logo placeholder (120px circle)
- "Welcome Back" headline
- "Sign in to continue" subtitle
- Username text field (outlined)
- Password field with eye toggle
- "Forgot Password?" link
- "SIGN IN" primary button (full width)
- "Don't have an account? Sign Up" link at bottom
- No bottom navigation
```

### Account Detail Screen
```
Create an account detail screen:
- Top bar with back arrow and account name
- Hero section: Large balance, account number, status
- Action buttons row: Transfer, Withdraw, Statement, QR Pay
- Account details card: Product name, interest rate, totals
- Transaction list with date headers
- Charges section at bottom
```

---

## Tips for Better Results

1. **Be Specific**: Include exact colors, sizes, and spacing values
2. **Use Hierarchy**: List items in visual order (top to bottom)
3. **Reference Standards**: Mention "Material Design 3" for consistency
4. **Include Context**: Explain what the app does and who uses it
5. **Iterate**: Generate multiple times and combine best elements
6. **Export Early**: Get design into Figma for refinement
