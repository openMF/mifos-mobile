# Beneficiary - Google Stitch Prompts

> **Tool**: [Google Stitch](https://stitch.withgoogle.com)
> **Style**: Material Design 3, Trusted Contacts Fintech
> **Format**: Copy each prompt block directly into Stitch

---

## Design System Reference

```
Primary Gradient: #667EEA -> #764BA2
Secondary Gradient: #11998E -> #38EF7D
Success/Verified: #00D09C
Warning/Pending: #FFB800
Error/Danger: #FF4757
Text Primary: #1F2937
Text Secondary: #6B7280
Surface: #FFFFFF
Screen Size: 393 x 852 pixels (Android)
```

---

## Screen 1: Trusted Contacts Hub

```
Mobile trusted contacts hub, Material Design 3, fintech, 393x852px

Top Bar:
- Back arrow, "Trusted Contacts" title
- Add contact, scan QR icons right

Search Bar:
- Search input with mic icon

Favorites Section:
- Primary gradient background card
- Star icon "FAVORITES" header
- Horizontal scroll avatars 64dp
- Each: initials, name, total transferred
- Add button at end

Suggested Section:
- "Suggested" header with "See All >"
- Card: avatar, name, context, gradient "Send $50" button
- Based on AI/history

Filter Chips:
- All (selected), Family, Business, Account type

Contacts List:
- Alphabetical sections: "A", "J" etc.
- Contact cards:
  - Avatar with verified badge (green ring) or pending (amber)
  - Name, account masked, account type
  - Relationship tag chip
  - Trust indicator: checkmark "Trusted" or hourglass "Pending"
  - Last transfer info
  - Send button on right

FAB:
- Gradient "+ Add" floating button bottom right

Bottom Navigation:
- Home, Accounts, FAB Transfer, Profile
```

---

## Screen 2: Contact Detail

```
Mobile contact detail, Material Design 3, fintech, 393x852px

Top Bar:
- Back arrow, "Contact" title
- Star (favorite), edit, menu icons

Hero Card:
- Primary gradient full width
- Large avatar with verified badge (green glow ring)
- Initials "JD" inside avatar
- Name "John Doe" 24sp white
- Account masked "****4567 • Savings"
- Trust badge: checkmark "Trusted Contact"
- Stats: "24 transfers • Since Mar 2024"

Quick Actions Row:
- Three buttons: "Send Money" (gradient filled), "Schedule Payment", "Request Money"

Transfer Summary Card:
- "Transfer Summary" header
- Two stat boxes: "Total Sent $12,450" | "Total Received $3,200"
- Horizontal bar: 80% sent (gradient), 20% received (gray)
- Stats: "Average Transfer: $518", "Most Common: Monthly payment"

Recent Activity Section:
- "Recent Activity" header with "See All >"
- Transaction items:
  - Arrow up-right (sent) or down-left (received)
  - "Sent $500", "3 days ago", "Monthly rent payment"
- Dividers between items

Account Information Card:
- "Account Information" header
- Key-value pairs: Account Number (with copy icon), Account Type, Office, Transfer Limit, Relationship tag, Added On

Security Footer:
- Lock icon "256-bit encryption • Verified contact"
```

---

## Screen 3: Quick Transfer from Contact

```
Mobile quick transfer, Material Design 3, fintech, 393x852px

Top Bar:
- Back arrow, "Send to John" title

Recipient Card:
- Avatar with verified badge
- Name, account masked, "Trusted Contact" badge

Amount Input:
- Large centered "$0.00" editable
- Underline primary gradient
- "Available: $5,420.00 in Savings" helper

Quick Amounts Section:
- "Quick Amounts (based on history)" label
- Four chips: $250 "Common", $500 "Usual" (starred), $750, $1,000

Note Section:
- "Add a note (optional)" label
- Input field "What's this for?"
- Recent notes chips: "Monthly rent", "Utilities split"

Number Keypad:
- 3x4 grid: 1-9, ., 0, backspace

Continue Button:
- Full width gradient "Review Transfer ->"
```

---

## Screen 4: Add New Contact - Smart Flow

```
Mobile add contact methods, Material Design 3, fintech, 393x852px

Top Bar:
- Back arrow, "Add Trusted Contact" title

Hero Section:
- Primary gradient
- Group icon "Add a Trusted Contact"
- "Choose how you'd like to add a new contact"

Method Cards:
- Card 1 (gradient, recommended):
  - Camera icon "Scan QR Code"
  - "Instantly add by scanning their QR"
  - Star badge "Fast"
- Card 2:
  - Phone icon "From Phone Contacts"
  - "Import from your existing contacts"
- Card 3:
  - Pencil icon "Enter Manually"
  - "Type account details directly"
- Card 4:
  - Link icon "Share Link"
  - "Get your link to share with contacts"

Tip Card:
- Lightbulb icon
- "QR scanning is the fastest and most secure way to add contacts with zero typing errors"

Security Footer:
- Lock icon "All contacts are verified before first transfer"
```

---

## Screen 5: Manual Entry Step 1 - Account

```
Mobile add contact step 1, Material Design 3, fintech, 393x852px

Top Bar:
- Back arrow, "Add Contact" title

Progress Indicator:
- Four steps: Account (filled), Office, Details, Review
- Connected line between steps

Section Header:
- Primary gradient "Account Information" with bank icon

Account Number Input:
- Label "Account Number *"
- Text field with "SA-" prefix, paste icon
- Helper text "Enter the complete account number"

Account Type Selector:
- Label "Account Type *"
- Three radio cards:
  - Savings Account (gradient selected, "Most common")
  - Loan Account
  - Share Account

Continue Button:
- Full width gradient "Continue ->"
```

---

## Screen 6: Manual Entry Step 2 - Office

```
Mobile add contact step 2, Material Design 3, fintech, 393x852px

Top Bar:
- Back arrow, "Add Contact" title

Progress Indicator:
- Account (checkmark), Office (filled), Details, Review

Section Header:
- Primary gradient "Office / Branch" with building icon

Search Input:
- Search icon "Search offices..."

Popular Offices:
- "Popular Offices" label
- Radio cards:
  - Head Office (gradient selected, "Popular" badge)
  - Branch Office
  - Regional Center
- Each with icon, name, description

Transfer Limit Input:
- Label "Transfer Limit *"
- Large "$10,000" editable
- Quick set chips: $5,000, $10,000 (starred), $25,000, $50,000

Navigation Buttons:
- "Back" outline, "Continue ->" gradient
```

---

## Screen 7: Manual Entry Step 3 - Details

```
Mobile add contact step 3, Material Design 3, fintech, 393x852px

Top Bar:
- Back arrow, "Add Contact" title

Progress Indicator:
- Account (check), Office (check), Details (filled), Review

Section Header:
- Primary gradient "Contact Details" with person icon

Contact Name Input:
- Label "Contact Name *"
- Text field "John Doe"
- Helper "How you'll recognize this contact"

Relationship Tags:
- Label "Relationship Tag (optional)"
- Chip grid 2x4:
  - Family, Friend, Business, Landlord
  - Vendor, School, Medical, Custom +

Favorite Checkbox:
- Unchecked "Add to Favorites"
- Helper "Quick access from home screen"

Preview Card:
- "Preview:" label
- Contact card mockup with entered data

Navigation Buttons:
- "Back" outline, "Review & Save ->" gradient
```

---

## Screen 8: Manual Entry Step 4 - Review

```
Mobile add contact step 4, Material Design 3, fintech, 393x852px

Top Bar:
- Back arrow, "Add Contact" title

Progress Indicator:
- All steps checkmarked, Review filled

Section Header:
- Primary gradient "Review & Confirm" with checkmark icon

Avatar Section:
- Large centered avatar "JD"
- Name "John Doe"
- Relationship tag "Business Partner"

Details Card:
- Key-value rows with edit pencil icons:
  - Account Number: SA-0001234567
  - Account Type: Savings Account
  - Office: Head Office
  - Transfer Limit: $10,000.00
  - Relationship: Business Partner

Security Badges:
- Lock "Contact will be verified before first transfer"
- Shield "Protected by 256-bit encryption"

Navigation Buttons:
- "Back" outline, "Add Trusted Contact" gradient
```

---

## Screen 9: Contact Added Success

```
Mobile contact added success, Material Design 3, 393x852px

Success Animation:
- Animated checkmark in green circle
- Confetti particles

Success Message:
- "Contact Added!" 28sp bold

Contact Preview Card:
- Avatar "JD"
- Name "John Doe"
- Account masked, relationship tag

Verification Notice:
- Hourglass icon "Verification pending"
- "First transfer will verify"

Action Buttons:
- "Send Money Now ->" gradient primary
- "Back to Contacts" outline secondary
```

---

## Screen 10: Edit Contact

```
Mobile edit contact, Material Design 3, fintech, 393x852px

Top Bar:
- Back arrow, "Edit Contact" title, "Save" text button

Hero Section:
- Primary gradient
- Avatar "JD"
- Account number masked

Info Banner:
- Info icon "Only name, relationship, and limit can be changed"

Editable Fields:
- Contact Name: "John Doe" with edit pencil
- Account Number: locked with lock icon
- Relationship Tags: chip selector with Business selected
- Transfer Limit: "$15,000" editable

Buttons:
- "Save Changes" gradient primary
- "Remove Contact" red/danger outline
```

---

## Screen 11: Delete Confirmation Modal

```
Mobile delete confirmation modal, Material Design 3

Dimmed Background

Modal Card:
- 24dp corners, centered

Warning Icon:
- Amber warning triangle

Title:
- "Remove Trusted Contact?"

Contact Preview:
- Avatar, name, account masked
- Stats: "24 transfers • $15,650 total"

Warning Text:
- "This will remove John from your trusted contacts."
- "You'll need to add them again to send money."
- Warning icon "This cannot be undone"

Buttons:
- "Cancel" outline
- "Remove" red filled
```

---

## Screen 12: Empty State

```
Mobile beneficiaries empty state, Material Design 3, 393x852px

Top Bar:
- Back arrow, "Trusted Contacts" title
- Add, scan icons

Search Bar visible

Center Content:
- Floating people illustration
- "No Trusted Contacts Yet" 20sp bold
- "Add your first contact to start sending money securely"

Benefits List:
- Checkmarks: Quick one-tap transfers, Secure verified contacts, Track payment history, Organized with tags

CTA Button:
- Gradient "Add Your First Contact"
```

---

## Components

### ContactCard
```
Contact card component, Material Design 3:
- Height: 88dp, padding: 16dp
- Avatar: 48dp with status ring (green verified, amber pending)
- Name: 16sp Medium, account: 14sp Regular
- Relationship chip, trust badge
- Send button on right
- Swipe actions: Edit, Delete
```

### TrustBadge
```
Trust badge component, Material Design 3:
- Verified: green checkmark, "Trusted Contact"
- Pending: amber hourglass, "Pending Verification"
- Height: 24dp pill shape
```

### QuickAmountChip
```
Quick amount chip, Material Design 3:
- Height: 48dp
- Amount: 16sp Medium
- Helper label: 12sp Secondary
- Selected: gradient fill, white text
- Starred: sparkle indicator
```

### RelationshipTag
```
Relationship tag chip, Material Design 3:
- Height: 32dp
- Emoji prefix: Family, Friend, Business, etc.
- Selected: primary border, tinted background
- Unselected: gray outline
```

---

## Quick Start

1. Open [stitch.withgoogle.com](https://stitch.withgoogle.com)
2. Create project "Mifos Mobile - Beneficiary"
3. Copy each screen prompt -> Generate
4. Generate components separately for reuse
5. Export all to Figma when done
