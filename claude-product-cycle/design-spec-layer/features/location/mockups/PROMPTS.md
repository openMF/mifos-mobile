# Location Feature - AI Design Tool Prompts

> **Generated from**: MOCKUP.md v2.0
> **Design Pattern**: Location Intelligence Hub
> **Primary Gradient**: #667EEA (Purple) -> #764BA2 (Deep Purple)
> **Generated**: 2025-01-04

---

## Google Stitch Prompts

### Prompt 1: Branch Finder Map View

```
Create a Branch Finder Map screen for a fintech banking app.

Design specifications:
- Material Design 3 with map-first design
- Primary gradient: #667EEA to #764BA2
- Full-screen map with overlays

Header (Floating):
- Back button (white, shadowed)
- Search bar: "Search branches, ATMs..."
- Filter icon button
- Floating on top of map

Map View:
- Full-screen interactive map
- Custom map markers:
  - Branch: Gradient pin with building icon
  - ATM: Blue pin with ATM icon
  - Partner: Gray pin with partner icon
- Marker clusters for zoom out
- User location: Blue pulsing dot

Bottom Sheet (Collapsed):
- Peek height showing:
  - "15 locations nearby" header
  - Horizontal quick filters: All, Branches, ATMs, 24/7
  - Pull handle indicator

Quick Stats Row (on sheet):
- "Nearest: 0.3 mi" | "Open Now: 8" | "24/7: 3"

Selected Location Preview:
- When marker tapped, show preview card above sheet:
  - Branch name
  - Distance and walk time
  - Open/Closed status
  - Quick actions: Directions, Call, Details

Style: Map-centric, quick discovery, location-aware
```

### Prompt 2: Branch List View

```
Create a Branch List View screen for a fintech banking app.

Design specifications:
- Material Design 3 with list patterns
- Location cards with rich information

Header:
- Back button, "Nearby Locations" title
- Map/List toggle icon
- Filter action

Search & Filters:
- Search bar with location icon
- Filter chips: All, Branches (selected), ATMs, Open Now, 24/7

Sort Options:
- Dropdown: "Sort by: Distance" / "Wait Time" / "Rating"

Location Cards (scrollable list):
Card structure:
- Left: Type icon with status indicator (open=green, closed=red)
- Main content:
  - Branch name (16sp bold)
  - Address (14sp secondary)
  - Distance: "0.3 mi · 5 min walk"
  - Hours: "Open until 6:00 PM" or "Closed · Opens 9 AM"
- Right:
  - Wait time badge: "~10 min wait"
  - Rating: "4.8 (125)"
  - Chevron

Card actions (expandable):
- Directions, Call, Book Appointment, Share

Empty State:
- No locations found illustration
- "No locations match your filters"
- "Try expanding your search area"

Style: Rich information cards, quick scanning, actionable
```

### Prompt 3: Branch Detail Screen

```
Create a Branch Detail screen for a fintech banking app.

Design specifications:
- Material Design 3 with comprehensive location info
- Hero image with floating actions

Hero Section:
- Branch photo (full width, 200dp height)
- Gradient overlay at bottom
- Floating back button (white, top-left)
- Floating favorite heart icon (top-right)

Branch Info Card (overlapping hero):
- Branch name: "Mifos Downtown Branch"
- Rating: 4.8 stars (125 reviews)
- Status chip: "Open Now" (green) or "Closed" (red)
- Type badge: "Full Service Branch"

Quick Actions Row:
- 4 action buttons: Directions, Call, Book, Share
- Each: Icon + label, outlined style

Live Wait Time Card:
- Current wait: "~10 minutes"
- Visual: People icon with count
- Bar chart: Busy times by hour
- "Usually busier at 12 PM"
- Real-time update indicator

Hours Section:
- Weekly schedule with today highlighted
- Special hours noted (holidays)
- "Last updated 2 hours ago"

Services Available:
- Checklist with icons:
  - Account Opening
  - Loan Services
  - Foreign Exchange
  - Safe Deposit Boxes
  - Notary Services
  - Wheelchair Accessible

Reviews Section:
- Overall rating with star breakdown
- Recent reviews (2-3 cards):
  - User avatar, name, date
  - Star rating
  - Comment text
  - Helpful button
- "See all 125 reviews" link

Contact & Address:
- Full address with copy button
- Phone number with call button
- Email with compose button
- Website link

Book Appointment CTA:
- Sticky bottom button: "Book Appointment"
- Gradient filled, full width

Style: Comprehensive info, real-time data, booking-focused
```

### Prompt 4: Book Appointment Flow

```
Create a Book Appointment screen for branch visits in a fintech app.

Design specifications:
- Material Design 3 with booking wizard
- Step-by-step appointment creation

Header:
- Back/Close button
- "Book Appointment" title
- Step indicator: 1 of 3

Branch Context:
- Small card showing selected branch
- Name, address, distance
- Change link

Service Selection:
- Title: "What do you need help with?"
- Service option cards (selectable):
  - Account Opening (30 min)
  - Loan Consultation (45 min)
  - Investment Advisory (60 min)
  - General Inquiry (15 min)
  - Document Verification (20 min)
- Each shows estimated duration
- Icon + title + duration

Date Selection:
- Calendar view for current/next month
- Available dates highlighted
- Selected date with gradient circle
- Unavailable dates grayed out

Time Slot Selection:
- Horizontal scroll of available times
- Time chips: 9:00 AM, 9:30 AM, 10:00 AM...
- Selected: Gradient filled
- Unavailable: Crossed out

Specialist Selection (optional):
- "Prefer a specific specialist?" toggle
- If on, show available specialists:
  - Avatar, name, specialty, rating
  - "Next available: Today 2:00 PM"

Summary Card:
- Service, Date, Time, Branch
- Estimated duration
- Specialist (if selected)

Notes Field:
- "Any additional notes?" text area
- Character limit indicator

Confirm Button:
- "Confirm Appointment" gradient button
- Terms text below

Style: Guided booking, clear availability, professional
```

### Prompt 5: AR Branch Finder (Premium)

```
Create an AR Branch Finder screen using augmented reality for a fintech app.

Design specifications:
- Material Design 3 with AR overlay patterns
- Camera view with AR annotations

Camera View:
- Full screen camera feed
- AR permission prompt if needed

AR Overlays:
- Floating branch markers in 3D space
- Distance labels on each marker
- Directional arrows on ground (optional)
- Line connecting user to nearest branch

Branch Marker (AR):
- 3D pin with gradient
- Name label floating above
- Distance: "150m ahead"
- Walk time: "2 min"
- Pulsing animation

Mini Map:
- Small radar-style map in corner
- Shows user orientation
- Branch dots with direction

Info Panel (bottom):
- Selected branch details
- Quick actions: Navigate, Details
- Swipe between nearby branches

Controls:
- Switch to Map view button
- Flashlight toggle (for low light)
- Refresh AR button

Accuracy Indicator:
- GPS accuracy status
- "High precision" or "Move for better signal"

Style: Immersive AR, clear annotations, easy navigation
```

---

## Figma MCP Prompts

### Prompt 1: Branch Finder Frame

```
Create a Figma frame for "Location - Branch Finder" mobile screen (375x812px).

Components needed:
1. Floating Header:
   - Back button (white, shadow)
   - Search bar component
   - Filter icon button

2. Map View (placeholder):
   - Full screen
   - Custom marker components:
     - Branch marker (gradient)
     - ATM marker (blue)
     - User location (pulsing dot)

3. Bottom Sheet Component:
   - Collapsed state (peek)
   - Expanded state (list)
   - Drag handle
   - Filter chips row

4. Location Card Component:
   - Type icon with status dot
   - Name, address, distance
   - Wait time badge
   - Rating display
   - Action buttons (collapsed)

5. Quick Filter Chips:
   - All, Branches, ATMs, Open Now, 24/7
   - Selected/unselected states

Design tokens:
- Primary: #667EEA
- Map markers: gradient, blue, gray
- Open status: #00D09C
- Closed status: #FF4757
```

### Prompt 2: Complete Location Flow

```
Create a Figma prototype flow for Location feature with these frames:

Frame 1: Map View
- Full map with markers
- Floating search
- Bottom sheet collapsed

Frame 2: List View
- Location cards list
- Filters applied
- Sort options

Frame 3: Branch Detail
- Hero image
- Info cards
- Services list
- Reviews section
- Book button

Frame 4-6: Book Appointment Flow
- Service selection
- Date/time picker
- Confirmation

Frame 7: Appointment Success
- Confirmation animation
- Details summary
- Add to calendar option

Frame 8: AR View (optional)
- Camera placeholder
- AR overlay mockups
- Mini map

Prototype connections:
- Map marker tap -> Detail preview
- Detail preview -> Full detail
- Book button -> Appointment flow
- List/Map toggle switches views

Component variants:
- Location card: default/selected/expanded
- Map marker: branch/atm/partner
- Time slot: available/selected/unavailable
```

---

## Design Tokens

```json
{
  "feature": "location",
  "version": "2.0",
  "designPattern": "Location Intelligence Hub",
  "colors": {
    "primary": "#667EEA",
    "secondary": "#764BA2",
    "success": "#00D09C",
    "error": "#FF4757",
    "warning": "#FFB800",
    "surface": "#FFFFFF",
    "background": "#F8F9FA",
    "mapOverlay": "rgba(0,0,0,0.5)"
  },
  "markers": {
    "branch": {
      "fill": "gradient",
      "size": 48
    },
    "atm": {
      "fill": "#2196F3",
      "size": 40
    },
    "partner": {
      "fill": "#9CA3AF",
      "size": 36
    },
    "userLocation": {
      "fill": "#2196F3",
      "pulse": true,
      "size": 24
    }
  },
  "components": {
    "locationCard": {
      "height": "auto",
      "minHeight": 96,
      "padding": 16,
      "radius": 16,
      "iconSize": 40,
      "gap": 12
    },
    "bottomSheet": {
      "peekHeight": 160,
      "expandedHeight": "70%",
      "handleHeight": 4,
      "handleWidth": 40,
      "radius": 24
    },
    "waitTimeBadge": {
      "height": 28,
      "padding": 12,
      "radius": 14,
      "background": "#FFB80015"
    },
    "timeSlot": {
      "height": 44,
      "padding": 16,
      "radius": 22,
      "gap": 8
    },
    "serviceCard": {
      "padding": 16,
      "radius": 12,
      "iconSize": 32
    }
  },
  "animation": {
    "markerBounce": {
      "duration": 300,
      "type": "spring"
    },
    "sheetDrag": {
      "duration": 200,
      "easing": "ease-out"
    },
    "userPulse": {
      "duration": 2000,
      "type": "infinite"
    }
  }
}
```

---

## Screen Checklist

| Screen | Stitch Prompt | Figma Prompt | Status |
|--------|---------------|--------------|--------|
| Branch Finder Map | 1 | 1, 2 | Ready |
| Branch List View | 2 | 2 | Ready |
| Branch Detail | 3 | 2 | Ready |
| Book Appointment | 4 | 2 | Ready |
| AR Branch Finder | 5 | 2 | Ready |
| Appointment Success | - | 2 | Pending |
