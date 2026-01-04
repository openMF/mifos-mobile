# Beneficiary - Figma First Draft Prompts

> **Tool**: Figma First Draft (Shift+I in Figma)
> **Style**: Trusted Contacts Fintech, Material Design 3
> **Format**: Natural language prompts optimized for Figma AI

---

## How to Use

1. Open Figma -> Create new design file
2. Press `Shift + I` or click "Actions" -> "First Draft"
3. Copy each prompt below
4. Generate -> Iterate -> Refine

---

## Screen 1: Trusted Contacts Hub

```
Design a modern mobile trusted contacts hub for a fintech banking app inspired by Venmo and Zelle.

At the top, show a back arrow with "Trusted Contacts" title and add contact/scan QR icons on the right.

Add a search bar with a microphone icon for voice search.

Create a stunning Favorites section with a purple gradient background. Show a star icon with "FAVORITES" header. Display horizontally scrolling circular avatars (64px) with user initials inside, names below, and total amount transferred to each. Include an "Add" button at the end.

Add a Suggested section with "Suggested" header and "See All >" link. Show a suggestion card with avatar, name, context text like "Sent 3x this month - Monthly rent", and a gradient "Send $50" quick action button.

Create filter chips in a row: All (selected with gradient), Family, Business, Account type dropdown.

Show an alphabetically organized contacts list. Each contact card should have an avatar with a verification ring (green for verified, amber for pending), name in medium weight, masked account number, account type, a relationship tag chip (like "Family" or "Business"), a trust indicator badge, last transfer info, and a "Send" button on the right.

Add a floating action button with gradient background and "+ Add" text in the bottom right corner.

Style it like Venmo's contact list with trust indicators - clean and relationship-focused with purple accent colors.
```

---

## Screen 2: Contact Detail

```
Design a contact detail screen showing comprehensive information about a trusted payment contact.

Show a back arrow with "Contact" title and star (favorite), edit, and menu icons on the right.

Create a hero card with purple gradient background. Display a large circular avatar (80px) with a glowing green verification ring around it and initials inside. Show the contact name "John Doe" in large white text, masked account number with account type, and a trust badge showing "Trusted Contact" with checkmark. Add stats showing "24 transfers • Since Mar 2024".

Add a Quick Actions row with three buttons: "Send Money" as a gradient filled button, "Schedule Payment" as outlined, and "Request Money" as outlined.

Create a Transfer Summary card showing relationship analytics. Display two stat boxes side by side: "Total Sent $12,450" and "Total Received $3,200". Add a horizontal progress bar showing 80% sent in gradient color and 20% received in gray. Show additional stats: "Average Transfer: $518" and "Most Common: Monthly payment".

Add a Recent Activity section with "See All >" link. Show transaction items with directional arrows (up-right for sent in red, down-left for received in green), amount, time ago, and description. Use dividers between items.

Create an Account Information section with key-value pairs in a clean grid: Account Number with copy icon, Account Type, Office, Transfer Limit, Relationship tag, and Added On date.

Add a security footer with lock icon showing "256-bit encryption • Verified contact".

Make it feel like a trusted relationship hub with clear visual hierarchy.
```

---

## Screen 3: Quick Transfer from Contact

```
Design a quick transfer screen for sending money to a specific trusted contact.

Show a back arrow with "Send to John" as the title.

Create a recipient card at the top showing the contact's avatar with verified badge, name, masked account number, and "Trusted Contact" indicator.

Add a large centered amount input showing "$0.00" that updates as the user types. Use a gradient underline below the amount. Show "Available: $5,420.00 in Savings" as helper text below.

Create a Quick Amounts section with the label "Quick Amounts (based on history)". Show four amount chips: $250 labeled "Common", $500 labeled "Usual" with a star indicator (AI suggestion), $750, and $1,000.

Add a Note section with "Add a note (optional)" label, an input field with placeholder "What's this for?", and chips for recent notes used with this contact like "Monthly rent" and "Utilities split".

Display a custom number keypad in a 3x4 grid: numbers 1-9, decimal point, 0, and backspace.

Add a full-width gradient button at the bottom: "Review Transfer ->".

Style it for fast, frictionless payments with smart suggestions based on transfer history.
```

---

## Screen 4: Add New Contact - Smart Flow

```
Design an add new contact screen offering multiple methods to add trusted contacts.

Show a back arrow with "Add Trusted Contact" title.

Create a hero section with purple gradient background showing a group/people icon and "Add a Trusted Contact" header with "Choose how you'd like to add a new contact" subtitle.

Display four method cards stacked vertically:

1. First card (recommended) - Use gradient background with camera icon, "Scan QR Code" title, "Instantly add by scanning their QR" description, and a "Fast" badge with star.

2. Second card - Phone icon with "From Phone Contacts" title and "Import from your existing contacts" description.

3. Third card - Pencil icon with "Enter Manually" title and "Type account details directly" description.

4. Fourth card - Link icon with "Share Link" title and "Get your link to share with contacts" description.

Add a tip card with a lightbulb icon explaining "QR scanning is the fastest and most secure way to add contacts with zero typing errors".

Include a security footer with lock icon: "All contacts are verified before first transfer".

Make the QR option stand out as the recommended path while keeping other options accessible.
```

---

## Screen 5: Manual Entry Flow - Step 1 Account

```
Design step 1 of a 4-step contact addition flow for entering account information.

Show a back arrow with "Add Contact" title.

Create a progress indicator with four connected steps: Account (active/filled), Office, Details, Review. Use a line connecting the steps with the current step highlighted in gradient color.

Add a section header with gradient background showing a bank icon and "Account Information" title.

Create an Account Number input field with label "Account Number *", a text field prefixed with "SA-", a paste/copy icon, and helper text "Enter the complete account number".

Add an Account Type selector with label "Account Type *" and three radio card options stacked vertically: "Savings Account" (selected with gradient border, marked "Most common for transfers"), "Loan Account" ("For loan repayments"), and "Share Account" ("For share purchases").

Include a full-width gradient "Continue ->" button at the bottom.

Style it as a clean form wizard with clear progress indication.
```

---

## Screen 6: Manual Entry Flow - Step 2 Office

```
Design step 2 of the contact addition flow for selecting office/branch.

Show a back arrow with "Add Contact" title.

Create a progress indicator showing Account (checkmarked complete), Office (active/filled), Details, Review.

Add a section header with gradient background showing a building icon and "Office / Branch" title.

Include a search input with placeholder "Search offices...".

Create a Popular Offices section with radio cards for: "Head Office" (selected with gradient, has "Popular" badge, "Main branch, downtown"), "Branch Office" ("Secondary branch, midtown"), and "Regional Center" ("Regional hub, north district").

Add a Transfer Limit input section with label "Transfer Limit *", a large editable amount "$10,000", and quick set chips: $5,000, $10,000 (starred as most common), $25,000, $50,000.

Include two navigation buttons at the bottom: "← Back" as outlined and "Continue →" as gradient filled.
```

---

## Screen 7: Manual Entry Flow - Step 3 Details

```
Design step 3 of the contact addition flow for entering contact details.

Show a back arrow with "Add Contact" title.

Create a progress indicator showing Account and Office (checkmarked), Details (active/filled), Review.

Add a section header with gradient background showing a person icon and "Contact Details" title.

Create a Contact Name input with label "Contact Name *", text field showing "John Doe", and helper text "How you'll recognize this contact".

Add a Relationship Tags section with label "Relationship Tag (optional)" and a 2x4 chip grid: Family (with emoji), Friend, Business, Landlord, Vendor, School, Medical, and Custom (+).

Include a checkbox option "Add to Favorites" with helper text "Quick access from home screen".

Create a live Preview section showing how the contact card will appear with the entered information.

Include navigation buttons: "← Back" and "Review & Save →" gradient button.
```

---

## Screen 8: Manual Entry Flow - Step 4 Review

```
Design the final review step of the contact addition flow.

Show a back arrow with "Add Contact" title.

Create a progress indicator showing all previous steps checkmarked and Review as active.

Add a section header with gradient background showing a checkmark icon and "Review & Confirm" title.

Display a centered avatar section with large initials "JD", name "John Doe" below, and relationship tag "Business Partner".

Create a details card with key-value rows, each with an edit pencil icon on the right: Account Number (SA-0001234567), Account Type (Savings Account with icon), Office (Head Office with icon), Transfer Limit ($10,000.00), and Relationship (Business Partner with icon).

Add two security badges: lock icon with "Contact will be verified before first transfer" and shield icon with "Protected by 256-bit encryption".

Include navigation buttons: "← Back" and "Add Trusted Contact" as the primary gradient action.

Make it feel like a final confirmation with easy inline editing.
```

---

## Screen 9: Contact Added Success

```
Design a success screen celebrating a newly added contact.

Center a large success animation with a green circular checkmark that has a drawing animation. Add confetti particles floating around.

Show "Contact Added!" as the main headline in large bold text.

Display the newly added contact in a preview card: avatar with initials, name, masked account number, and relationship tag.

Add a verification notice with hourglass icon: "Verification pending" with "First transfer will verify" explanation.

Include two action buttons: "Send Money Now ->" as the primary gradient button (most likely next action) and "Back to Contacts" as a secondary outlined button.

Make it feel celebratory and guide the user toward their next action.
```

---

## Screen 10: Edit Contact

```
Design an edit contact screen allowing modification of editable fields.

Show a back arrow with "Edit Contact" title and "Save" text button on the right.

Create a hero section with gradient background showing the contact's avatar and masked account number (non-editable).

Add an info banner with info icon explaining "Only name, relationship, and limit can be changed".

Create editable fields: Contact Name with edit pencil icon, Account Number locked with lock icon and explanation "Cannot be changed for security", Relationship Tags as a chip selector with current selection highlighted, and Transfer Limit as an editable amount with note "Increased from $10,000".

Include two buttons: "Save Changes" as gradient primary and "Remove Contact" as a red/danger outlined button.

Clearly distinguish between editable and locked fields for security.
```

---

## Screen 11: Delete Confirmation Modal

```
Design a confirmation modal for removing a trusted contact.

Create a dimmed background overlay.

Show a centered modal card with 24dp rounded corners.

Display a large amber warning triangle icon at the top.

Add the title "Remove Trusted Contact?" in bold.

Show a preview of the contact being deleted: avatar, name, masked account number, with stats "24 transfers • $15,650 total" to emphasize the relationship history.

Include warning text: "This will remove John from your trusted contacts. You'll need to add them again to send money." Add a warning icon with "This cannot be undone".

Display two action buttons: "Cancel" as outlined (safe option) and "Remove" as red filled (destructive action).

Make the consequences clear while keeping the safe option prominent.
```

---

## Screen 12: Empty State

```
Design an empty state for when the user has no trusted contacts.

Keep the header with "Trusted Contacts" title and add/scan icons.

Show the search bar as visible but inactive.

Center a friendly illustration of people/contacts with a subtle floating animation.

Display "No Trusted Contacts Yet" as the headline with "Add your first contact to start sending money securely" as the description.

Create a benefits list with checkmarks: Quick one-tap transfers, Secure verified contacts, Track payment history, Organized with tags.

Add a prominent gradient button: "Add Your First Contact".

Make it feel welcoming and motivating, not empty or broken.
```

---

## Component Prompts

### Trust Badge Component
```
Design a trust verification badge component.

Create two variants:
1. Verified: Green background pill with white checkmark icon and "Trusted Contact" text
2. Pending: Amber background pill with hourglass icon and "Pending Verification" text

Height should be 24dp with rounded pill shape.
```

### Contact Card Component
```
Design a contact card for the contacts list.

Create a full-width card about 88dp tall with 16dp padding.

On the left, show a 48dp circular avatar with a colored ring around it (green for verified, amber for pending) and initials inside.

Show the contact name in medium weight, masked account number and type in smaller gray text below.

Add a relationship tag chip (like "Family" with emoji).

Show a trust indicator badge.

Display last transfer info in small text.

Include a "Send" button on the right side.

Add swipe actions for Edit and Delete.
```

---

## Tips for Figma First Draft

1. **Iterate**: Generate once, then refine with follow-up prompts
2. **Colors**: Use "#667EEA to #764BA2" for primary gradient
3. **Reference**: Say "like Venmo" or "Zelle trusted contacts" for context
4. **Trust Indicators**: Emphasize verified vs pending states
5. **Relationships**: Include tags and transfer history
6. **Responsive**: Mention "393px width for mobile" for correct sizing

---

## After Generation

1. Review generated designs for consistency
2. Create a color style library
3. Build component variants for trust states
4. Link screens with prototype connections
5. Export and update `FIGMA_LINKS.md`
