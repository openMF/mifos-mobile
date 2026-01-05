# Commands Layer

Slash command definitions for Claude Code integration with Mifos Mobile.

## Quick Reference

```
SESSION MANAGEMENT
├── /session-start              # Load context for new session
└── /session-end                # Save progress before ending

GAP ANALYSIS (Where are we?)
├── /gap-analysis               # Brief overview of all layers
├── /gap-analysis design        # Design layer status
│   ├── /gap-analysis design spec      # Specifications status
│   ├── /gap-analysis design mockup    # Mockups generation status
│   └── /gap-analysis design api       # API documentation status
├── /gap-analysis server        # Server layer status
├── /gap-analysis client        # Client layer status
│   ├── /gap-analysis client network   # Network services status
│   └── /gap-analysis client data      # Repositories status
├── /gap-analysis feature       # Feature layer status
│   └── /gap-analysis feature [name]   # Single feature status
├── /gap-analysis platform      # Platform layer status
│   ├── /gap-analysis platform android
│   ├── /gap-analysis platform ios
│   ├── /gap-analysis platform desktop
│   └── /gap-analysis platform web
└── /gap-analysis [feature]     # Specific feature (all 5 layers)

GAP PLANNING (What needs work?)
├── /gap-planning               # Brief overview of what needs planning
├── /gap-planning design        # Plan design layer work
│   ├── /gap-planning design spec      # Plan specification work
│   ├── /gap-planning design mockup    # Plan mockup generation
│   └── /gap-planning design api       # Plan API documentation
├── /gap-planning server        # Plan server layer work
├── /gap-planning client        # Plan client layer work
│   ├── /gap-planning client network   # Plan network services
│   └── /gap-planning client data      # Plan repositories
├── /gap-planning feature       # Plan feature layer work
│   └── /gap-planning feature [name]   # Plan specific feature
├── /gap-planning platform      # Plan platform layer work
│   ├── /gap-planning platform android
│   ├── /gap-planning platform ios
│   ├── /gap-planning platform desktop
│   └── /gap-planning platform web
└── /gap-planning [feature]     # Plan specific feature (all layers)

DESIGN LAYER (Specifications & Mockups)
├── /design                     # Show feature list
├── /design [feature]           # Full spec review/create
├── /design [feature] add [section]    # Add specific section
├── /design [feature] improve   # Suggest improvements
├── /design [feature] mockup    # Generate Figma mockups for feature
└── /design mockup              # Generate mockups for all features

IMPLEMENTATION
├── /implement [feature]        # Full E2E implementation (all layers)
├── /client [feature]           # Client layer only (Network + Data)
└── /feature [feature]          # Feature layer only (ViewModel + Screen)

VERIFICATION
├── /verify [feature]           # Validate implementation vs spec
└── /projectstatus              # Project overview and status
```

## Command Details

### Session Management

| Command | Purpose | When to Use |
|---------|---------|-------------|
| `/session-start` | Load context from CURRENT_WORK.md | Start of new session |
| `/session-end` | Save progress, update CURRENT_WORK.md | Before ending session |

### Gap Analysis

| Command | Purpose | Output |
|---------|---------|--------|
| `/gap-analysis` | Quick overview | Brief table of all 5 layers |
| `/gap-analysis design` | Design layer status | SPEC, API, MOCKUP, mockups/ status |
| `/gap-analysis design mockup` | Mockups status only | 17-feature mockup progress table |
| `/gap-analysis client` | Client layer status | Services + repositories status |
| `/gap-analysis feature` | Feature layer status | All features VM+Screen status |
| `/gap-analysis [name]` | Single feature status | All 5 layers for one feature |

### Gap Planning

| Command | Purpose | Output |
|---------|---------|--------|
| `/gap-planning` | What needs work | Priority-sorted task list |
| `/gap-planning design mockup` | Mockup generation plan | Step-by-step with commands |
| `/gap-planning client network` | Network services plan | Service implementation tasks |
| `/gap-planning feature [name]` | Feature implementation plan | v2.0 UI update tasks |

### Design Layer

| Command | Purpose | Output |
|---------|---------|--------|
| `/design` | Feature list | Available features with status |
| `/design [feature]` | Spec review | SPEC.md + API.md analysis |
| `/design [feature] mockup` | Generate mockups | PROMPTS.md + design-tokens.json |
| `/design mockup` | Generate all | Batch mockup generation |

### Implementation

| Command | Purpose | Layers |
|---------|---------|--------|
| `/implement [feature]` | Full E2E | Network → Data → ViewModel → Screen |
| `/client [feature]` | Client only | Network services + repositories |
| `/feature [feature]` | Feature only | ViewModel + Screen + Navigation |

### Verification

| Command | Purpose | Checks |
|---------|---------|--------|
| `/verify [feature]` | Implementation check | Spec compliance, tests, platforms |
| `/projectstatus` | Project overview | All features, all layers, metrics |

## 5-Layer Product Lifecycle

```
┌─────────────────────────────────────────────────────────────────┐
│                    PRODUCT LIFECYCLE                             │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  1. DESIGN LAYER (Entry Point)                                  │
│     ├── SPEC.md      → Requirements, user stories               │
│     ├── API.md       → Endpoint definitions                     │
│     ├── MOCKUP.md    → ASCII design v2.0                        │
│     └── mockups/     → Generated Figma prompts                  │
│                                                                  │
│  2. SERVER LAYER                                                 │
│     └── Fineract API → Endpoint availability                    │
│                                                                  │
│  3. CLIENT LAYER                                                 │
│     ├── Network      → Ktorfit services (core/network/)         │
│     └── Data         → Repositories (core/data/)                │
│                                                                  │
│  4. FEATURE LAYER                                                │
│     ├── ViewModel    → State management                         │
│     ├── Screen       → Compose UI                               │
│     ├── Navigation   → Route definitions                        │
│     └── DI           → Koin modules                             │
│                                                                  │
│  5. PLATFORM LAYER                                               │
│     ├── Android      → cmp-android/                             │
│     ├── iOS          → cmp-ios/                                 │
│     ├── Desktop      → cmp-desktop/                             │
│     └── Web          → cmp-web/                                 │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

## Workflow Examples

### Start New Session
```
/session-start
/gap-analysis                    # See overview
/gap-planning design mockup      # Plan next work
/design auth mockup              # Execute task
```

### Check Feature Status
```
/gap-analysis home               # All layers for home
/gap-planning feature home       # Plan v2.0 UI update
/feature home                    # Implement UI changes
/verify home                     # Validate implementation
```

### Full Feature Implementation
```
/design dashboard                # Review/create spec
/design dashboard mockup         # Generate mockups
/implement dashboard             # Full E2E implementation
/verify dashboard                # Validate
```

### End Session
```
/session-end                     # Save progress
```

## Design Layer Deep Dive

The Design Layer is the entry point where we design the whole application. All other layers depend on it.

### Sub-Sections

| Sub-Section | Files | Purpose |
|-------------|-------|---------|
| spec | SPEC.md | Requirements, user stories, acceptance criteria |
| api | API.md | Endpoint definitions, request/response schemas |
| mockup | MOCKUP.md | ASCII design v2.0 (visual layout) |
| mockups/ | PROMPTS.md, design-tokens.json | Generated AI prompts for Figma |

### Mockup Generation Workflow

```
MOCKUP.md (ASCII v2.0)
       ↓
/design [feature] mockup
       ↓
mockups/PROMPTS.md + design-tokens.json
       ↓
User: Google Stitch → Figma
       ↓
User: Update FIGMA_LINKS.md
       ↓
/implement [feature] (uses Figma via MCP)
```

### Feature List

| # | Feature | Design Path | Command |
|:-:|---------|-------------|---------|
| 1 | auth | features/auth/ | `/design auth` |
| 2 | home | features/home/ | `/design home` |
| 3 | accounts | features/accounts/ | `/design accounts` |
| 4 | savings-account | features/savings-account/ | `/design savings-account` |
| 5 | loan-account | features/loan-account/ | `/design loan-account` |
| 6 | share-account | features/share-account/ | `/design share-account` |
| 7 | beneficiary | features/beneficiary/ | `/design beneficiary` |
| 8 | transfer | features/transfer/ | `/design transfer` |
| 9 | recent-transaction | features/recent-transaction/ | `/design recent-transaction` |
| 10 | notification | features/notification/ | `/design notification` |
| 11 | settings | features/settings/ | `/design settings` |
| 12 | passcode | features/passcode/ | `/design passcode` |
| 13 | guarantor | features/guarantor/ | `/design guarantor` |
| 14 | qr | features/qr/ | `/design qr` |
| 15 | location | features/location/ | `/design location` |
| 16 | client-charge | features/client-charge/ | `/design client-charge` |
| 17 | dashboard | features/dashboard/ | `/design dashboard` |

## Files

| File | Command | Description |
|------|---------|-------------|
| `session-start.md` | `/session-start` | Load context for new session |
| `session-end.md` | `/session-end` | Save progress before ending |
| `gap-analysis.md` | `/gap-analysis` | Analyze implementation gaps |
| `gap-planning.md` | `/gap-planning` | Plan implementation tasks |
| `design.md` | `/design` | Feature specification |
| `implement.md` | `/implement` | Full E2E implementation |
| `client.md` | `/client` | Client layer implementation |
| `feature.md` | `/feature` | Feature layer implementation |
| `verify.md` | `/verify` | Validate implementation |
| `projectstatus.md` | `/projectstatus` | Project overview |
