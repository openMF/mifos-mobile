# Loan Account - Google Stitch Prompts

> **Tool**: [Google Stitch](https://stitch.withgoogle.com)
> **Style**: Material Design 3, Debt Freedom Fintech
> **Format**: Copy each prompt block directly into Stitch

---

## Design System Reference

```
Primary Gradient: #667EEA -> #764BA2
Secondary Gradient: #11998E -> #38EF7D
Success/Credit: #00D09C
Error/Debit: #FF4757
Warning: #FFB800
Text Primary: #1F2937
Text Secondary: #6B7280
Surface: #FFFFFF
Screen Size: 393 x 852 pixels (Android)
```

---

## Screen 1: Loan Dashboard

```
Mobile loan dashboard screen, Material Design 3, debt freedom fintech, 393x852px

Top Bar:
- Back arrow, "My Loans" title
- Add and help icons right

Debt Freedom Hero Card:
- Full width, gradient #667EEA to #764BA2, 24dp radius
- Animated flame icon + "Your Debt Freedom Journey" header
- "Total Outstanding" label
- "$12,450.00" balance 36sp white ExtraBold
- Combined progress bar 62% with "62% Paid" label
- Two stat chips: calendar "8 months to freedom", fire "24 streak on-time"
- Animated particles on high streak

Next Payment Alert Card:
- Warning gradient background if due soon
- Calendar icon with date (JAN 15)
- "$450.00" amount 28sp bold, "Personal Loan" subtitle
- Urgency badge: clock icon "3 days" amber
- Primary CTA button: "Pay Now - $450"

Payment Streak Section:
- "PAYMENT STREAK" header with fire icon
- "24 Month Streak!" prominent
- Monthly grid 3 rows x 10 columns
- Green checkmarks for paid months
- Current month highlighted with asterisk
- Badge: "Perfect Payer 2024" unlocked

My Loans Section:
- "MY LOANS" header, "Active Loans (2)", "View All >" link
- Loan Card 1: Personal Loan
  - Bank icon, #000123456
  - "Outstanding: $3,000.00 of $5,000"
  - Progress bar 60%
  - Stats: EMI $450, Next Jan 15
  - Actions: [Pay Now] [Schedule]
- Loan Card 2: Home Improvement
  - $9,450.00 of $15,000, 37% progress
  - EMI $625, Next Jan 20

Smart Suggestion Card:
- Success gradient #11998E to #38EF7D
- Sparkle icon "Smart Tip"
- "Pay $50 extra this month to save $127 in interest and finish 2 months early!"
- Actions: [Dismiss] [Apply >]

Quick Actions Row:
- 4 circular buttons: Payoff Calc, Payment History, Remind Me, Pay via QR

Bottom Navigation:
- Home, Accounts, FAB +, Cards, Profile
```

---

## Screen 2: Loan Detail View

```
Mobile loan detail screen, Material Design 3, debt freedom fintech, 393x852px

Top Bar:
- Back arrow, "Personal Loan" title
- "#000123456" subtitle, menu icon

Loan Hero Card:
- Primary gradient, 24dp radius
- "Outstanding Balance" label
- "-$3,000.00" balance 36sp white ExtraBold (negative)
- Progress bar showing percentage paid
- Two stat chips: "Principal $5,000", "Interest 12% p.a."
- Green "Active" status pill

Debt Freedom Countdown:
- Large circular countdown display 80dp
- "241 days" number animated
- "Until you're debt-free!" text
- "Target: September 15, 2025"
- Progress bar 60%
- "$2,000 paid • $3,000 to go"

Milestone Progress:
- "MILESTONES" header with trophy icon
- Horizontal progress track with nodes at 25%, 50%, 60% (current), 75%, 100%
- Checkmarks for completed, current highlighted
- Badge card: "Halfway Hero! Unlocked at 50%"
- "Next milestone: 75% - $1,250 to go"

Next Payment Card:
- Calendar icon with date JAN 15
- "$450.00" total, breakdown:
  - Principal: $375.00
  - Interest: $50.00
  - Fees: $25.00
- Warning badge "3 days left"
- Primary button: "Pay Now - $450.00"

Early Payoff Calculator Card:
- Success gradient
- Sparkle icon "Pay Off Early?"
- "If you pay $550/month instead:"
- Two stat boxes: "Save $127.50 interest", "Finish 2 months earlier"
- "Calculate Custom Amount >" link

Loan Actions List:
- Menu items with icons and chevrons:
  - Make Payment
  - Repayment Schedule (View all 12 installments)
  - Payment History (8 payments completed)
  - Loan Summary
  - Charges & Fees
  - Guarantor Details
  - Generate QR

Loan Details Grid:
- 2x3 grid layout:
  - Principal: $5,000.00, Interest: 12% p.a.
  - Term: 12 months, EMI: $450.00
  - Disbursed: Jan 2024, Maturity: Dec 2024
```

---

## Screen 3: Early Payoff Calculator

```
Mobile payoff calculator screen, Material Design 3, fintech, 393x852px

Top Bar:
- Back arrow, "Payoff Calculator" title

Current Loan Status Card:
- "Personal Loan #000123456"
- Remaining Balance: $3,000.00
- Current EMI: $450/month
- Months Remaining: 8
- Interest Left to Pay: $240.00

Payment Amount Slider:
- "How much can you pay monthly?" label
- Large display "$600" updating live
- Slider track from $450 (MIN) to $650 (MAX)
- Quick add chips: +$50, +$100, +$150

Savings Visualization Card:
- Primary gradient background
- "YOUR SAVINGS" header
- Large animated counter "$127.50 Interest Saved"
- Two stat boxes:
  - Calendar icon "3 months earlier"
  - Rocket icon "Aug 2025 payoff date"

Comparison Chart:
- "Current vs New Plan" header
- Bar chart comparing:
  - New Plan: 5 months, less total interest
  - Current Plan: 8 months, more interest
  - Interest savings highlighted in green
- Legend: Principal (solid), Interest (filled)

Payment Schedule Preview:
- "New Payment Schedule" header
- List items:
  - Jan 2025: $600.00 (current marker)
  - Feb 2025: $600.00
  - Mar 2025: $600.00
  - Apr 2025: $600.00
  - May 2025: $387.50 (END marker)
- Summary: 5 payments, $2,987.50 total, $112.50 interest

Action Buttons:
- Primary gradient: "Apply New Payment Plan"
- Secondary text link: "Keep Current Plan"
```

---

## Screen 4: Repayment Schedule

```
Mobile repayment schedule screen, Material Design 3, fintech, 393x852px

Top Bar:
- Back arrow, "Repayment Schedule" title, menu icon

Schedule Overview Card:
- "Personal Loan #000123456"
- Three stat boxes: "Paid 8 of 12", "Remaining 4 left", "Total 12"

Visual Timeline:
- "2024" year header
- Monthly timeline Jan-Jun, Jul-Dec
- Circle nodes connected by lines
- Green checkmarks [v] for paid months
- Asterisk [*] for current month
- Empty circles for upcoming
- Legend: Paid, Due, Upcoming

Upcoming Installments Section:
- "UPCOMING INSTALLMENTS" header

Installment Card #9 (Current, Highlighted):
- Warning gradient background
- "Due: January 15, 2025" with [!] icon
- Breakdown:
  - Principal: $375.00
  - Interest: $50.00
  - Fees: $25.00
- Total Due: $450.00
- Primary button: "Pay This - $450"

Installment Cards #10-12:
- Standard cards for Feb, Mar, Apr
- "Pay Early - Save $12.50" for next month
- "Pay Early" buttons for others
- Final installment marked with [END]

Completed Payments Section:
- "Completed (8)" header + "View All >" link
- List items with month icons:
  - Dec 15, 2024: $450.00 [v] On Time
  - Nov 15, 2024: $450.00 [v] On Time
  - Oct 15, 2024: $450.00 [v] On Time
```

---

## Screen 5: Make Payment Flow

```
Mobile make payment screen - Step 1, Material Design 3, fintech, 393x852px

Top Bar:
- Close X, "Make Payment" title, "1/3" step indicator

Progress Indicator:
- Three step circles connected: [1] Amount, [2] Review, [3] Confirm
- Step 1 active

Loan Summary:
- "Personal Loan #000123456"
- "Outstanding: $3,000.00"
- "Next Due: Jan 15 - $450.00"

Payment Amount:
- Large centered editable "$450.00"
- [EDIT] button below

Payment Options:
- Radio button list:
  - [*] Pay this installment: $450.00 - Due Jan 15
  - [ ] Pay multiple installments
  - [ ] Pay custom amount
  - [ ] Pay off entire loan: $3,000.00 - Save $240!

Smart Suggestion Card:
- Primary gradient
- Sparkle icon "Tip: Pay $500 instead"
- "Save $15.50 in interest and finish 1 month earlier!"
- Button: "Apply $500 Payment"

Continue Button:
- Full width gradient: "Continue to Review"

---

Step 2: Review Screen

Top Bar:
- Back arrow, "Make Payment" title, "2/3"

Progress Indicator:
- Step 2 active, Step 1 checked

Payment Source:
- "Pay From" label
- Account card: wallet icon, Primary Savings ****4521
- "Available: $8,200.00" green
- "Change Account v" link

Payment Breakdown:
- Principal: $375.00
- Interest: $50.00
- Fees: $25.00
- Divider
- Total Payment: $450.00 (bold)

After Payment Card:
- "After This Payment" header
- Remaining Balance: $2,550.00
- Payments Left: 3
- New Payoff Date: Apr 15, 2025
- Progress bar 66%

Streak Warning Card:
- Primary gradient
- Fire icon "Keep your 24-month streak alive!"
- "Pay on time to maintain your Perfect Payer status."

Confirm Button:
- Full width gradient: "Confirm - $450.00"
- Lock icon "Secured with 256-bit encryption"

---

Step 3: Success Screen

Top Bar:
- Close X, "Payment Complete"

Success Card:
- Primary gradient
- Animated checkmark with confetti
- "Payment Successful!"
- "-$450.00" amount
- "From: Primary Savings"

Streak Update:
- Fire icon "STREAK CONTINUES!"
- "25 MONTHS" large animated number
- "On-Time Payments"
- Updated streak grid with checkmarks

Milestone Check:
- "Progress: 66% Complete!"
- Progress bar
- Trophy icon "9% to next milestone! (75% - Finish Line)"

Action Buttons:
- Three buttons: View Receipt, Home, Done (gradient)
```

---

## Screen 6: Overdue Payment State

```
Mobile loan overdue screen, Material Design 3, fintech, 393x852px

Top Bar:
- Back arrow, "Personal Loan" title, "#000123456"

Overdue Hero Card:
- Error gradient #FF4757 to #FF6B7A
- [!] icon, "Outstanding Balance"
- "-$3,275.00" (includes penalty) 36sp
- Progress bar
- Two stat chips: "Overdue $450.00" red, "Penalty +$25.00"
- Pulsing red "OVERDUE" status badge
- Shake animation on load

Overdue Alert Card:
- Error background 10% opacity
- Red left border 4dp
- [!] "Payment Overdue by 5 Days" header
- "Your payment of $450.00 was due on January 15, 2025"
- "Late fee applied: $25.00" red text
- Primary button: "Pay $475.00 Now"

Streak Broken Notice:
- Broken/sad fire icon
- "Streak Paused" header
- "Your 24-month streak has been paused. Pay now to restart it!"
- Monthly grid showing break in January

Penalty Breakdown Card:
- Original EMI: $450.00
- Late Fee (5%): +$22.50 red
- Additional Charge: +$2.50
- Divider
- Total Due Now: $475.00 bold red
- Warning: "Additional penalties may accrue daily until paid"

Payment Options:
- Primary gradient button: "Pay Full Amount - $475"
- Secondary card: "Set Up Payment Plan >" (split into 2-3 payments)
- Tertiary card: "Contact Support >" (request fee waiver)
```

---

## Screen 7: Loan Application Flow

```
Mobile loan application - Step 1, Material Design 3, fintech, 393x852px

Top Bar:
- Close X, "Apply for Loan" title, "1/4"

Progress Indicator:
- Four steps: [1] Product, [2] Amount, [3] Review, [4] Submit

Loan Products:
- "Choose a loan product" header

Product Card 1 (Selected):
- Primary gradient border
- Bank icon, "Personal Loan" [*] radio selected
- "Up to $10,000"
- "12% p.a. | 6-24 months"
- "Best for: Emergency funds, debt consolidation"

Product Card 2:
- Standard border
- "Bronze Loan" [ ] radio
- "Up to $5,000", "10% p.a. | 3-12 months"

Product Card 3:
- "Home Improvement" [ ] radio
- "Up to $50,000", "8% p.a. | 12-60 months"

Continue Button:
- Full width gradient: "Continue - Amount"

---

Step 2: Amount & Term

Top Bar:
- Back arrow, "Apply for Loan", "2/4"

Loan Amount:
- "Personal Loan" subtitle
- "How much do you need?" label
- Large centered "$5,000" editable
- "Min: $500 Max: $10,000"
- Slider $500 to $10,000
- Quick amount chips: $2,000, $5,000, $7,500

Loan Term:
- "Select loan term" label
- Term chips: 6 months, [12 months] selected, 18 months, 24 months, Custom

EMI Preview Card:
- Primary gradient
- "Estimated Monthly Payment"
- "$450.00 per month" large
- Two stat boxes: "Total Interest $400.00", "Interest Rate 12% p.a."
- "Total Repayable: $5,400.00"

Continue Button:
- Full width gradient: "Continue - Review"

---

Step 3: Review & Submit

Application Summary:
- "Review Your Application" header
- Loan Details section:
  - Product: Personal Loan
  - Amount: $5,000.00
  - Term: 12 months
  - Interest Rate: 12% p.a.
- Payment Details:
  - Monthly EMI: $450.00
  - First Payment: Feb 15, 2025
  - Last Payment: Jan 15, 2026
- Cost Summary:
  - Principal: $5,000.00
  - Total Interest: $400.00
  - Processing Fee: $50.00
  - Total Repayable: $5,450.00 bold

Payment Projection Card:
- Calendar icon
- "Your Debt Freedom Date"
- "January 15, 2026"
- "12 monthly payments of $450.00 each"

Terms Acceptance:
- Three checkboxes:
  - [v] I agree to Terms & Conditions and Privacy Policy
  - [v] I consent to credit check
  - [v] I understand the repayment obligations

Submit Button:
- Full width gradient: "Submit Application"

---

Step 4: Application Submitted

Success Card:
- Animated checkmark + confetti
- "Application Submitted!"
- "Your loan application has been submitted successfully."
- "Application ID: #LA-2025-0123"

Next Steps:
- "What happens next?" header
- Three step cards with icons:
  - [1] Review: Our team will review (1-2 business days)
  - [2] Approval: You'll get a notification
  - [3] Disbursement: Funds credited to your account

Action Buttons:
- My Loans, Home, Done (gradient)
```

---

## Screen 8: Achievements

```
Mobile loan achievements screen, Material Design 3, gamification, 393x852px

Top Bar:
- Back arrow, "My Achievements" title

Achievement Stats:
- Three stat boxes: "12 Badges", "24 Streak", "$500 Saved"

Unlocked Badges Section:
- "Unlocked (8)" header
- 4x2 badge grid:
  - Fire Streak Pro, Star Early Bird, Half Halfway, 25% First Step
  - Save Smart Saver, Clock On-Time, Calc Payoff, Lock Secure User
- Each badge: icon, name, full color with glow

Locked Badges Section:
- "Locked (4)" header
- Grayed out badges: 75% Finish Line, 100% Debt-Free, Year Streak, Gold Elite
- Progress hint card: "Finish Line: Pay off 75%"
- Progress bar 60% -> 75%

Recent Activity:
- Timeline list:
  - Halfway Hero unlocked! Jan 10, 2025
  - 24-month streak reached! Jan 5, 2025
  - Smart Saver badge earned Dec 15, 2024
```

---

## Screen 9: Loading State

```
Mobile loan loading skeleton, Material Design 3, 393x852px

Top Bar: Same as success

Hero Card Shimmer:
- Gradient card shape with shimmer
- Balance placeholder 200dp
- Progress bar shimmer
- Stat chip shimmers

Countdown Shimmer:
- Circular shimmer 80dp
- Text line shimmers below

Actions List Shimmer:
- Multiple row shimmers with icon circles

Shimmer animation: left-to-right sweep, 1000ms infinite
Gradient: #E1E4E8 -> #F8F9FA -> #E1E4E8
```

---

## Screen 10: Empty State

```
Mobile loan empty state, Material Design 3, 393x852px

Top Bar: Same as success

Center Content:
- Loan document illustration 160dp
- "No Loan Accounts" title 20sp bold
- "You don't have any active loans. Apply for a loan to get started!"

Benefits Card:
- "WHY GET A LOAN?" header
- Checklist:
  - Flexible Terms (6-60 month options)
  - Competitive Rates (Starting from 8% p.a.)
  - Quick Approval (Decision in 24 hours)
  - No Hidden Fees (Transparent pricing)

CTA:
- Full width gradient button: "Apply for a Loan"
```

---

## Components

### LoanDashboardHero
```
Loan dashboard hero card, Material Design 3:
- Min height: 220dp, padding: 24dp
- Gradient #667EEA to #764BA2, 24dp radius
- Animated flame icon on high streak
- Progress bar with gradient fill
- Stats chips semi-transparent white
- Shadow: 12dp blur, gradient at 25%
```

### DebtCountdown
```
Debt countdown component, Material Design 3:
- Height: 160dp
- Large circular display 80dp diameter
- Days number animated counter
- Progress track 8dp thickness
- Pulse effect when near completion
```

### PaymentStreakGrid
```
Payment streak grid, Material Design 3:
- Cell size: 32dp x 32dp, spacing: 4dp
- Completed: #00D09C green check
- Current: #FFB800 amber with pulse
- Missed: #FF4757 red X
- Future: #E1E4E8 gray
```

### MilestoneProgress
```
Milestone progress bar, Material Design 3:
- Horizontal track with gradient
- Node circles at 25%, 50%, 75%, 100%
- Completed nodes filled
- Current node pulsing
- Badge card below current
```

### LoanCard
```
Loan account card, Material Design 3:
- Min height: 140dp, 16dp padding
- White surface, 16dp radius, 2dp elevation
- Left: icon container with gradient
- Progress bar 8dp below content
- Quick action buttons row
- Overdue state: red border + shake
```

### AchievementBadge
```
Achievement badge, Material Design 3:
- Size: 72dp x 72dp
- Icon container 48dp with 36dp icon
- Unlocked: full color + golden glow
- Locked: 40% opacity + lock overlay
- Progress variant: grayscale + progress ring
```

---

## Quick Start

1. Open [stitch.withgoogle.com](https://stitch.withgoogle.com)
2. Create project "Mifos Mobile - Loan Account"
3. Copy each screen prompt -> Generate
4. Generate components separately for reuse
5. Export all to Figma when done
