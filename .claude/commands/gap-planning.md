# Gap Planning Command

Brief entry point to plan implementation tasks. Creates step-by-step plans that persist across sessions.

## Usage

```
/gap-planning                        # Brief overview (what needs planning)
/gap-planning design                 # Plan design layer work
/gap-planning design mockup          # Plan mockup generation specifically
/gap-planning design spec            # Plan specification work
/gap-planning server                 # Plan server layer work
/gap-planning client                 # Plan client layer work
/gap-planning client network         # Plan network services
/gap-planning client data            # Plan repositories
/gap-planning feature                # Plan feature layer work
/gap-planning feature [name]         # Plan specific feature
/gap-planning platform               # Plan platform layer work
/gap-planning platform android       # Plan Android-specific work
/gap-planning [feature-name]         # Plan specific feature (all layers)
```

## Brief Overview Output (No Parameters)

When `/gap-planning` is called without parameters, show a **brief summary**:

```
## Gap Planning - What Needs Work

| Layer | Gaps | Priority | Next Plan |
|-------|:----:|:--------:|-----------|
| Design | mockups (16) | P1 | /gap-planning design mockup |
| Server | - | - | - |
| Client | 1 service | P2 | /gap-planning client |
| Feature | dashboard | P0 | /gap-planning feature dashboard |
| Platform | web fixes | P2 | /gap-planning platform web |

**Current Focus**: Design Layer → Mockup Generation (Phase 2)
**Next Step**: Run `/gap-planning design mockup` to get step-by-step tasks.
```

## Prerequisites

Run `/gap-analysis` first to identify gaps, or run `/gap-planning` directly to see what needs work.

## Instructions

### Step 1: Determine Template

**Layer Parameters**:
| Parameter | Template | Plans For |
|-----------|----------|-----------|
| (none) | Brief summary | What needs planning |
| `design` | `layer-design.md` | Design layer (specs + mockups) |
| `server` | `layer-server.md` | Server documentation |
| `client` | `layer-client.md` | Network + Data layers |
| `feature` | `layer-feature.md` | Feature implementation |
| `platform` | `layer-platform.md` | Platform-specific work |
| `[name]` | `feature-*.md` | Specific feature |

**Sub-Section Parameters** (layer + sub-section):
| Parameters | Template | Plans For |
|------------|----------|-----------|
| `design mockup` | `subsection/design-mockup.md` | Mockup generation (Google Stitch) |
| `design spec` | `subsection/design-spec.md` | Specification updates |
| `design api` | `subsection/design-api.md` | API documentation |
| `client network` | `subsection/client-network.md` | Network services |
| `client data` | `subsection/client-data.md` | Repositories |
| `feature [name]` | `subsection/feature-single.md` | Single feature plan |
| `platform android` | `subsection/platform-android.md` | Android-specific |
| `platform ios` | `subsection/platform-ios.md` | iOS-specific |
| `platform desktop` | `subsection/platform-desktop.md` | Desktop-specific |
| `platform web` | `subsection/platform-web.md` | Web-specific |

### Step 2: For Feature Parameter

Determine gap type by checking if `feature/[name]/` exists:

| Condition | Gap Type | Template |
|-----------|----------|----------|
| Directory missing | New feature | `templates/gap-planning/feature-new.md` |
| Directory exists | v2.0 UI update | `templates/gap-planning/feature-v2.md` |

### Step 3: Read Required Files

| Layer | Files to Read |
|-------|---------------|
| Design | `design-spec-layer/STATUS.md`, feature STATUS.md files |
| Server | `server-layer/FINERACT_API.md`, feature API.md files |
| Client | `client-layer/LAYER_STATUS.md`, check `core/` |
| Feature | `feature-layer/LAYER_STATUS.md`, check `feature/` |
| Platform | Check `cmp-*/` modules |
| [name] | All design files + current implementation |

### Step 4: Fill Template

Read template and replace placeholders with:
- Actual gaps from status files
- Concrete task lists
- Real file paths
- Code sketches
- Verification steps

## Template Reference

```
templates/gap-planning/
├── dashboard.md        # Full planning dashboard
├── layer-design.md     # Design layer plan
├── layer-server.md     # Server layer plan
├── layer-client.md     # Client layer plan
├── layer-feature.md    # Feature layer plan
├── layer-platform.md   # Platform layer plan
├── feature-new.md      # New feature creation
├── feature-v2.md       # v2.0 UI upgrade
└── task-template.md    # Individual task format
```

## Priority Guidelines

| Priority | Criteria | Examples |
|----------|----------|----------|
| P0 | Critical - blocks other work | Missing feature module |
| P1 | High value - user-facing | v2.0 UI, new screens |
| P2 | Polish - nice to have | Animations, dark mode |

## Effort Guidelines

| Effort | Time | Scope |
|--------|------|-------|
| S | <1 hour | Single file, styling |
| M | 1-4 hours | Multiple files, component |
| L | >4 hours | Feature module, architecture |

## Output Rules

1. Read actual status files first
2. Create prioritized task list (P0 → P1 → P2)
3. Include specific file paths
4. Provide code sketches (not full code)
5. Add verification steps
6. End with next command suggestion

## Workflow

```
/gap-analysis              →  Identify gaps
        ↓
/gap-planning [target]     →  Create task list (this command)
        ↓
/implement [target]        →  Execute tasks
        ↓
/verify [target]           →  Confirm completion
```
