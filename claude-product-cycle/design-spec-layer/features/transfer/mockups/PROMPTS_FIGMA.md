# Transfer/Pay - Figma First Draft Prompts

> **Tool**: Figma First Draft (Shift+I in Figma)
> **Style**: Modern P2P Payments, Material Design 3
> **Format**: Natural language prompts optimized for Figma AI

---

## How to Use

1. Open Figma -> Create new design file
2. Press `Shift + I` or click "Actions" -> "First Draft"
3. Copy each prompt below
4. Generate -> Iterate -> Refine

---

## Screen 1: Payment Hub (Main Entry)

```
Design a modern mobile payment hub for a fintech app inspired by Venmo and Cash App.

At the top, show a back arrow on the left and "History" text button on the right.

Create a mode selector with three pill-shaped tabs in a row: "Send" with money icon (selected with gradient), "Request" with request icon, and "Split" with group icon. The selected tab should have a purple gradient background.

Add a From Account card with full-width purple gradient background. Show "From" label with "Change" dropdown arrow, bank icon with "Primary Savings" account name, masked account number "****4521", and "Available: $8,200.00" with an eye toggle to show/hide balance.

Create a Recent recipients section with "RECENT" header and "See All >" link. Show horizontally scrolling circular avatars (72px) with user initials, names below, last amount sent, and time ago. Include an "Add new" button with plus icon at the end.

Add a "TO" section with a search input placeholder "Name, phone, email, or account..." and three method cards below labeled "OR": Contacts with phone icon, Scan QR with camera icon, and Payment Link with link icon.

Create a Suggested section with lightbulb icon showing AI suggestions: "You usually send John $50 on Fridays" with a gradient "Quick Send: $50 to John" button.

Add a pending request card showing "Maria requested $120 for dinner" with time "3 hours ago" and two buttons: "Decline" outline and "Pay $120" gradient.

Style it like Venmo's payment screen with conversation-first design and smart suggestions.
```

---

## Screen 2: Send Money - Recipient Selected

```
Design a send money screen after selecting a recipient.

Show a back arrow with "Pay John" as the title.

Create a centered recipient card with a large circular avatar (80px), name "John Doe" in medium weight, and username "@johndoe" below.

Add a large amount input display with "$0" placeholder in extra bold text (44sp) with an underline. Show "Tap to enter" as helper text below.

Create a Quick Amounts section with label and four amount chips: $10, $25, $50, $100.

Add a History-Based Amounts section with two chips showing amounts based on previous transfers: "$50 - Your usual amount" and "$35 - Last payment".

Create a "WHAT'S IT FOR?" section with a note input field showing placeholder "Add a note..." with an emoji button on the right. Below it, show Recent Notes with this contact as emoji chips: Pizza, Coffee, Movie, Uber.

Display a full-width button at the bottom in disabled/gray state: "Enter an amount".

Style it for quick, intuitive money sending with smart suggestions.
```

---

## Screen 3: Send Money - Amount Entered

```
Design the send money screen with an amount entered ready for payment.

Show a back arrow with "Pay John" as the title.

Create a centered recipient card with avatar and name "John Doe".

Display a large amount "$50.00" in extra bold text with a gradient underline. Show "Remaining: $8,150.00" as helper text below.

Add a filled note input showing a pizza emoji with "Pizza night!" text and a clear X button.

Create a custom number keypad in a 3x4 grid layout: 1-9 in the first three rows, then decimal point, 0, and backspace. Each key should be 64dp height with subtle background on press.

Add a full-width gradient button at the bottom: "Pay $50.00" with a money/send icon. Below it, add a hint: "SWIPE UP TO PAY" with arrows indicating the gesture.

Make it feel ready for a quick payment with clear visual hierarchy.
```

---

## Screen 4: Request Money Mode

```
Design a request money screen for requesting payment from others.

Show a back arrow on the left and "History" button on the right.

Create a mode selector with Send, Request (selected with gradient), and Split tabs.

Add a "REQUEST FROM" section with a search input "Search people..." and below it show "SELECTED (2)" with selected contacts as removable chips: "John Doe" with X and "Maria Santos" with X.

Create an amount section with label "AMOUNT TO REQUEST (Total)" and large editable "$100.00" centered. Show helper text "Split: $50.00 each (2 people)".

Add Split Options with two radio buttons: "Split Equally" and "Custom Split" (selected).

Show Custom Amounts section with each person's name and their custom amount: John Doe $60.00 and Maria Santos $40.00 (both editable). Include a divider and total summary "Total: $100.00".

Add a note input with pasta emoji "Dinner at Italian Place".

Include a full-width gradient button: "Request $100.00 (2 people)".

Style it for easy group requests with flexible splitting options.
```

---

## Screen 5: Split Bill Mode

```
Design a bill splitting screen for dividing costs among multiple people.

Show a back arrow with "Split Bill" as the title.

Create a Total Bill section with label and large editable amount "$156.50" centered. Add a button for "Scan receipt to auto-fill" with camera icon.

Add a "SPLITTING WITH (4 people)" section with "+ Add Person" button. Show person cards with progress bars:
- "You (Paying now)": $39.13, 25% progress bar filled
- "John Doe": $39.13, 25%, note "Request will be sent"
- "Maria Santos": $39.12, 25%
- "Alex Kumar": $39.12, 25%

Create a Split Method selector with balance scale icon and four radio options: Equal (selected), Custom, By Item, % Based.

Add a note input with pizza emoji "Dinner at Pizza Palace".

Include a full-width gradient button: "Pay $39.13 & Request from 3 people".

Make it easy to split bills fairly with multiple calculation methods.
```

---

## Screen 6: Schedule Recurring Payment

```
Design a recurring payment scheduling screen.

Show a back arrow with "Schedule Payment" as the title.

Create a recipient and amount summary card showing "TO" section with avatar "John Doe" and "@johndoe", and "AMOUNT" section with "$50.00" in large text.

Add a Schedule section with "Frequency" label and four radio chips: Once, Weekly (selected), Monthly, Custom.

Create a Day of Week selector with seven circular chips for each day (S M T W T F S). Friday should be selected with gradient fill.

Add date pickers in a row: Start Date showing "Jan 3, 2025" and End Date showing "No end date" with calendar icons.

Create a Notification Preferences section with checkboxes: "Notify me before each payment" (checked), "Notify me if payment fails" (checked), and "Send receipt to email" (unchecked).

Add a note input with pizza emoji "Weekly pizza fund".

Show a summary card: "$50.00 every Friday starting Jan 3, 2025" with "Next payment: Jan 3, 2025".

Include a full-width gradient button: "Schedule Recurring Payment" with calendar icon.
```

---

## Screen 7: Payment Confirmation (Swipe to Pay)

```
Design a payment confirmation screen with swipe-to-pay interaction.

Show a close X button in the top left only.

Create a centered payment summary showing a large avatar of "John Doe", the amount "$50.00" prominently, and the note "Pizza night!" with pizza emoji below.

Add a transaction details section showing: From "Primary Savings ****4521", To "John Doe @johndoe", and Date "Dec 30, 2025".

Create a Swipe to Pay zone at the bottom with a gradient track, a draggable white thumb circle with arrow icon, and text "SWIPE TO PAY" with animated arrows. Add instruction text "Swipe right to confirm payment".

Show a security indicator with lock icon: "Secured with Face ID / Touch ID".

Style it as the final confirmation step with a satisfying swipe interaction.
```

---

## Screen 8: Success State (Animated)

```
Design a payment success screen with celebratory animations.

Show a "Done" text button in the top right only.

Add confetti particle animation across the top of the screen in multiple colors.

Create a large success indicator in the center: a green circular background with a checkmark icon that animates drawing in.

Display "Payment Sent!" as a large bold headline.

Add a transaction summary card showing the avatar thumbnail, "$50.00 -> John Doe", and the pizza emoji note "Pizza night!".

Show transaction details: Transaction ID, Date & Time, and From account.

Create two action buttons in a row: "Share" with share icon and "Receipt" with download icon.

Add a full-width gradient button: "Pay Again".

Make it feel celebratory and rewarding with clear next steps.
```

---

## Screen 9: Payment History / Activity

```
Design a payment history screen showing all transaction activity.

Show a back arrow with "Payment History" title and a filter dropdown on the right.

Add a search input with placeholder "Search payments...".

Create filter tabs in a row: All (selected), Sent, Received, Pending, Scheduled.

Add a TODAY section with transaction cards showing:
- Avatar, "You paid John Doe", "-$50.00" in red, pizza emoji, time "10:45 AM", green "Complete" badge
- Avatar, "Maria paid you", "+$120.00" in green, pasta emoji, time "9:30 AM", green "Complete" badge

Add a YESTERDAY section with:
- Avatar, "You requested from Alex", "$35.00", coffee emoji, time, amber "Pending" badge with "Remind" and "Cancel" buttons
- Avatar, "You paid Sarah Lee", "-$200.00", house emoji, "Complete" badge

Add a SCHEDULED section with:
- Recurring icon, "Weekly to John Doe", "$50.00", pizza emoji, "Every Friday", blue "Scheduled" badge
- "Next: Jan 3, 2025" with Edit and Cancel buttons

Style it like a chat-style activity feed with clear status indicators.
```

---

## Screen 10: QR Scan Screen

```
Design a QR code scanning screen for payments.

Show a back arrow with "Scan to Pay" as the title and a flashlight toggle icon on the right.

Add instruction text at the top: "Point your camera at a payment QR code".

Create a full-screen camera viewfinder area with a scanning frame overlay. Add animated corner brackets in gradient color that pulse to indicate scanning is active.

At the bottom, add three action buttons in a row: "My QR" with camera icon to show your own code, "Gallery" with image icon to select from photos, and "NFC" with contactless icon for tap-to-pay.

Make it feel focused on the scanning task with clear visual feedback.
```

---

## Screen 11: My QR Code (Receive)

```
Design a personal QR code screen for receiving payments.

Show a back arrow with "My QR Code" as the title and a share icon on the right.

Create a profile section at the top with centered avatar, name "Your Name", and username "@username".

Display a large QR code in a white card with rounded corners. The QR code should encode the user's payment information.

Add an optional amount input section: "Set amount to receive" label with an editable "$0.00" input. Show helper text: "Anyone who scans can pay you this amount".

Include two action buttons at the bottom: "Copy Link" with clipboard icon and "Save Image" with download icon.

Make it easy to share and customize for receiving payments.
```

---

## Component Prompts

### Swipe to Pay Button
```
Design an interactive swipe-to-pay confirmation button.

Create a track about 64dp tall with a gradient purple background and 32dp border radius.

Add a draggable thumb circle (48dp) in white with an arrow-right icon inside. It should start on the left side.

Show "SWIPE TO PAY" text in the center with animated arrow indicators.

When the user swipes right past 80% of the width, the button should morph the thumb into a checkmark and trigger success.

Add a subtle shimmer animation across the track when idle. Include haptic feedback at the threshold and on complete.
```

### Recent Recipient Avatar Card
```
Design a recent recipient card for horizontal scrolling.

Create a 72dp wide by 96dp tall card with 8dp padding.

At the top, show a 48dp circular avatar with a gradient background based on user identity and initials inside if no photo.

Below the avatar, show the contact name (max 8 characters) in 12sp medium weight.

Show the last sent amount in 11sp regular secondary color.

Show the time ago in 10sp regular tertiary color.

On tap, it should open the payment screen pre-filled with this recipient and suggest the last sent amount.
```

---

## Tips for Figma First Draft

1. **Iterate**: Generate once, then refine with follow-up prompts
2. **Colors**: Use "#667EEA to #764BA2" for primary gradient
3. **Reference**: Say "like Venmo" or "Cash App style" for P2P context
4. **Interactions**: Emphasize swipe gestures and smart suggestions
5. **Social**: Include notes, emojis, and payment reactions
6. **Responsive**: Mention "393px width for mobile" for correct sizing

---

## After Generation

1. Review generated designs for consistency
2. Create a color style library
3. Build component variants for states
4. Add prototype connections for swipe interactions
5. Export and update `FIGMA_LINKS.md`
