# Gap Planning Command

Plan concrete implementation tasks based on gaps identified by `/gap-analysis`.

## Usage

```
/gap-planning                    # Full planning dashboard (all layers)
/gap-planning design             # Plan design layer fixes
/gap-planning server             # Plan server layer fixes
/gap-planning client             # Plan client layer fixes
/gap-planning feature            # Plan feature layer fixes
/gap-planning platform           # Plan platform layer fixes
/gap-planning [feature-name]     # Plan specific feature fixes
```

## Prerequisites

Run `/gap-analysis` first to identify gaps.

## Instructions

### Step 1: Determine Template

| Parameter | Template | Plans For |
|-----------|----------|-----------|
| (none) | `templates/gap-planning/dashboard.md` | All layers prioritized |
| `design` | `templates/gap-planning/layer-design.md` | Missing specs/mockups |
| `server` | `templates/gap-planning/layer-server.md` | Undocumented endpoints |
| `client` | `templates/gap-planning/layer-client.md` | Missing services/repos |
| `feature` | `templates/gap-planning/layer-feature.md` | Missing features, v2.0 UI |
| `platform` | `templates/gap-planning/layer-platform.md` | Platform-specific fixes |
| `[name]` | See Step 2 | Specific feature |

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
