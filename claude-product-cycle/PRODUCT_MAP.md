# PRODUCT MAP - Mifos Mobile

> **Purpose**: Central navigation hub for feature development across sessions
> **Last Updated**: 2025-12-30
> **Current Focus**: Design Layer Refinement (2025 Fintech Patterns)

---

## Quick Resume Commands

```bash
# Full 5-layer gap analysis dashboard
/gap-analysis

# Layer-specific analysis
/gap-analysis design      # Design layer (SPEC, MOCKUP, API docs)
/gap-analysis server      # Server layer (Fineract endpoints)
/gap-analysis client      # Client layer (Network, Data)
/gap-analysis feature     # Feature layer (ViewModel, Screen)
/gap-analysis platform    # Platform layer (Android, iOS, Desktop, Web)

# Feature-specific analysis (all 5 layers)
/gap-analysis [feature-name]

# Plan improvements
/gap-planning [feature-name]

# Continue design work on specific feature
/design [feature-name]

# Implement after design is approved
/implement [feature-name]

# Verify implementation matches spec
/verify [feature-name]

# Check current status
/projectstatus
```

---

## Feature Registry

### Core Features (16 Total)

| # | Feature | Design | Mockup | Client | UI | Status |
|---|---------|--------|--------|--------|-----|--------|
| 1 | **auth** | ✅ | ✅ v2.0 | ✅ | ✅ | Mockup Redesigned |
| 2 | **home** | ✅ | ✅ v2.0 | ✅ | ✅ | Mockup Redesigned |
| 3 | **accounts** | ✅ | ✅ v2.0 | ✅ | ✅ | Mockup Redesigned |
| 4 | **savings-account** | ✅ | ✅ v2.0 | ✅ | ✅ | Mockup Redesigned |
| 5 | **loan-account** | ✅ | ✅ v2.0 | ✅ | ✅ | Mockup Redesigned |
| 6 | **share-account** | ✅ | ✅ v2.0 | ✅ | ✅ | Mockup Redesigned |
| 7 | **beneficiary** | ✅ | ✅ v2.0 | ✅ | ✅ | Mockup Redesigned |
| 8 | **transfer** | ✅ | ✅ v2.0 | ✅ | ✅ | Mockup Redesigned |
| 9 | **recent-transaction** | ✅ | ✅ v2.0 | ✅ | ✅ | Mockup Redesigned |
| 10 | **notification** | ✅ | ✅ v2.0 | ✅ | ✅ | Mockup Redesigned |
| 11 | **settings** | ✅ | ✅ v2.0 | - | ✅ | Mockup Redesigned |
| 12 | **passcode** | ✅ | ✅ v2.0 | - | ✅ | Mockup Redesigned |
| 13 | **guarantor** | ✅ | ✅ v2.0 | ✅ | ✅ | Mockup Redesigned |
| 14 | **qr** | ✅ | ✅ v2.0 | - | ✅ | Mockup Redesigned |
| 15 | **location** | ✅ | ✅ v2.0 | - | ✅ | Mockup Redesigned |
| 16 | **client-charge** | ✅ | ✅ v2.0 | ✅ | ✅ | Mockup Redesigned |

### New Features (Planned)

| # | Feature | Design | Mockup | Client | UI | Status |
|---|---------|--------|--------|--------|-----|--------|
| 17 | **dashboard** | ✅ | ✅ v2.0 | ✅ | ❌ | Mockup Redesigned |

---

## 5-Layer Product Lifecycle

```
┌─────────────────────────────────────────────────────────────┐
│                    PRODUCT LIFECYCLE                         │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  1. DESIGN LAYER     →  SPEC + MOCKUP + API documentation   │
│         ↓                                                    │
│  2. SERVER LAYER     →  Fineract API endpoints (actual)     │
│         ↓                                                    │
│  3. CLIENT LAYER     →  Network services, Repositories      │
│         ↓                                                    │
│  4. FEATURE LAYER    →  ViewModels, Screens, Navigation     │
│         ↓                                                    │
│  5. PLATFORM LAYER   →  Android, iOS, Desktop, Web          │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

## Layer Definitions

### 1. Design Layer (`/design`, `/gap-analysis design`)
**Location**: `claude-product-cycle/design-spec-layer/`

| Component | Location | Purpose |
|-----------|----------|---------|
| SPEC.md | `features/[feature]/SPEC.md` | Requirements, user stories |
| MOCKUP.md | `features/[feature]/MOCKUP.md` | UI/UX designs, v2.0 patterns |
| API.md | `features/[feature]/API.md` | Endpoint definitions |
| STATUS.md | `features/[feature]/STATUS.md` | Implementation status |
| mockup-tools/ | `mockup-tools/` | Figma plugin, templates |

### 2. Server Layer (`/gap-analysis server`)
**Location**: `claude-product-cycle/server-layer/`

| Component | Location | Purpose |
|-----------|----------|---------|
| FINERACT_API.md | `server-layer/FINERACT_API.md` | All available endpoints |
| Base URL | `https://tt.mifos.community/fineract-provider/api/v1/self/` | Demo server |

### 3. Client Layer (`/client`, `/gap-analysis client`)
**Location**: `core/network/` + `core/data/`

| Component | Location |
|-----------|----------|
| API Service | `core/network/services/[Feature]Service.kt` |
| Repository | `core/data/repository/[Feature]Repository.kt` |
| Models | `core/model/[feature]/` |

### 4. Feature Layer (`/feature`, `/gap-analysis feature`)
**Location**: `feature/[feature]/`

| Component | Location |
|-----------|----------|
| ViewModel | `feature/[feature]/src/.../viewmodel/` |
| Screen | `feature/[feature]/src/.../ui/` |
| Navigation | `cmp-navigation/src/.../navigation/` |
| DI Module | `feature/[feature]/src/.../di/` |

### 5. Platform Layer (`/gap-analysis platform`)
**Location**: `cmp-android/`, `cmp-ios/`, `cmp-desktop/`, `cmp-web/`

| Platform | Module | Status |
|----------|--------|--------|
| 🤖 Android | `cmp-android/` | Production Ready |
| 🍎 iOS | `cmp-ios/` | Production Ready |
| 🖥️ Desktop | `cmp-desktop/` | Beta |
| 🌐 Web | `cmp-web/` | Alpha |

---

## Current Work Session

### In Progress: Design Layer Enhancement

**Goal**: Upgrade all mockups to 2025 professional fintech standards

**Reference Sources**:
- [Finix Banking UI Kit](https://dribbble.com/shots/25774459-Finix-Mobile-Banking-UI-Kit)
- [Purrweb Best Practices](https://www.purrweb.com/blog/banking-app-design/)
- [Mobbin Revolut](https://mobbin.com/explore/screens/d66df812-03a0-447f-879b-165318d669c8)

**2025 Patterns to Add**:
| Pattern | Priority | Features Affected |
|---------|----------|-------------------|
| Spending Analytics Chart | High | home, accounts |
| Recent Recipients (P2P) | High | home, transfer |
| Upcoming Bills Section | High | home |
| AI Assistant Entry Point | Medium | home |
| Gamification Elements | Medium | home, savings-account |
| Quick Amount Shortcuts | Medium | transfer |
| Card Freeze Quick Action | Low | home, settings |

### Todo Queue

1. ⏳ **home** - Add 2025 enhancements section (IN PROGRESS)
2. ⬚ **accounts** - Add spending analytics integration
3. ⬚ **transfer** - Add quick amounts, recent recipients
4. ⬚ **dashboard** - Create full MOCKUP.md (NEW FEATURE)
5. ⬚ Remaining 12 features - Review and polish

---

## Feature Dependencies

```
┌─────────────────────────────────────────────────────────────────┐
│                      NAVIGATION GRAPH                            │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   ROOT_GRAPH                                                     │
│       │                                                          │
│       ├── AUTH_GRAPH                                             │
│       │       └── auth ──────────────────────────┐               │
│       │                                          │               │
│       ├── PASSCODE_GRAPH                         │               │
│       │       └── passcode ◄─────────────────────┘               │
│       │               │                                          │
│       │               ▼                                          │
│       └── MAIN_GRAPH ◄───────────────────────────────────────   │
│               │                                                  │
│               ├── home (Tab 1) ─────────────────────────────┐   │
│               │       ├── → accounts                         │   │
│               │       ├── → transfer                         │   │
│               │       ├── → notification                     │   │
│               │       └── → qr                               │   │
│               │                                              │   │
│               ├── accounts (Tab 2) ──────────────────────────┤   │
│               │       ├── → savings-account                  │   │
│               │       ├── → loan-account                     │   │
│               │       └── → share-account                    │   │
│               │                                              │   │
│               ├── transfer (FAB) ────────────────────────────┤   │
│               │       └── → beneficiary                      │   │
│               │                                              │   │
│               └── profile (Tab 4) ───────────────────────────┘   │
│                       ├── → settings                             │
│                       ├── → client-charge                        │
│                       ├── → recent-transaction                   │
│                       ├── → guarantor                            │
│                       └── → location                             │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

## File Paths Quick Reference

### Design Specs
```
claude-product-cycle/design-spec-layer/
├── _shared/
│   └── COMPONENTS.md          # Shared design system
├── features/
│   ├── auth/
│   │   ├── SPEC.md
│   │   ├── API.md
│   │   ├── MOCKUP.md
│   │   └── STATUS.md
│   ├── home/
│   │   ├── SPEC.md
│   │   ├── API.md
│   │   ├── MOCKUP.md          # 🔄 Enhancing with 2025 patterns
│   │   └── STATUS.md
│   ├── accounts/
│   ├── savings-account/
│   ├── loan-account/
│   ├── share-account/
│   ├── beneficiary/
│   ├── transfer/
│   ├── recent-transaction/
│   ├── notification/
│   ├── settings/
│   ├── passcode/
│   ├── guarantor/
│   ├── qr/
│   ├── location/
│   ├── client-charge/
│   └── dashboard/             # 📋 Needs MOCKUP.md
└── STATUS.md                  # Master status tracker
```

### Implementation
```
feature/
├── auth/
├── home/
├── account/                   # accounts feature
├── savings-account/
├── loan-account/
├── share-account/
├── beneficiary/
├── transfer-process/          # transfer feature
├── recent-transaction/
├── notification/
├── settings/
├── guarantor/
├── qr-code/                   # qr feature
├── location/
└── user-profile/              # includes client-charge

core/
├── network/services/          # API services
├── data/repository/           # Repositories
└── model/                     # Domain models
```

---

## Resume Instructions

When resuming work after context compaction:

### 1. Read This File First
```
Read: claude-product-cycle/PRODUCT_MAP.md
```

### 2. Check Current Session Status
```
Look at "Current Work Session" section above
```

### 3. Continue From Todo Queue
```
Pick the first ⏳ or ⬚ item and continue
```

### 4. For Design Work
```
Read: claude-product-cycle/design-spec-layer/_shared/COMPONENTS.md
Read: claude-product-cycle/design-spec-layer/features/[feature]/MOCKUP.md
```

### 5. For Implementation Work
```
Read: claude-product-cycle/design-spec-layer/features/[feature]/SPEC.md
Read: claude-product-cycle/design-spec-layer/features/[feature]/API.md
```

---

## Design System Quick Reference

### Color Palette
| Name | Light | Dark | Usage |
|------|-------|------|-------|
| Primary Gradient | #667EEA → #764BA2 | Same (85%) | Hero cards, CTAs |
| Secondary Gradient | #11998E → #38EF7D | Same | Success, savings |
| Accent Gradient | #FC466B → #3F5EFB | Same | Alerts, special |
| Success | #00D09C | #00D09C | Positive amounts |
| Error | #FF4757 | #FF4757 | Negative, urgent |
| Warning | #FFB800 | #FFB800 | Pending states |

### Typography
| Style | Size | Weight | Usage |
|-------|------|--------|-------|
| Display | 36sp | ExtraBold | Balance amounts |
| Headline | 20sp | Bold | Section titles |
| Body | 14sp | Regular | Content text |
| Label | 12sp | Medium | Captions, badges |

### Spacing
| Token | Value |
|-------|-------|
| xs | 4dp |
| sm | 8dp |
| md | 12dp |
| lg | 16dp |
| xl | 20dp |
| xxl | 24dp |

---

## Changelog

| Date | Change |
|------|--------|
| 2025-12-30 | Created PRODUCT_MAP.md for session continuity |
