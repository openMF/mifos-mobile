# Claude Product Cycle - Template Migration Plan

## Overview

Move claude-product-cycle architecture to `kmp-project-template` so all derived projects inherit the framework while maintaining project-specific implementations.

---

## Current State

```
kmp-project-template/           # Base template (no claude-product-cycle)
    ├── mifos-mobile/          # Has full claude-product-cycle
    ├── mobile-wallet/         # No claude-product-cycle
    ├── android-client/        # No claude-product-cycle
    └── mifos-x-group-banking/ # No claude-product-cycle
```

## Target State

```
kmp-project-template/
├── .claude/
│   └── commands/              # Framework commands (parameterized)
├── claude-product-cycle/
│   ├── _templates/            # Reusable templates
│   ├── _framework/            # Architecture documentation
│   └── PROJECT_SETUP.md       # How to configure for a project
│
├── mifos-mobile/              # Extends template
│   └── claude-product-cycle/
│       ├── design-spec-layer/ # Project-specific specs
│       ├── server-layer/      # Project-specific APIs
│       ├── client-layer/      # Project-specific mappings
│       ├── feature-layer/     # Project-specific features
│       └── platform-layer/    # Project-specific platforms
```

---

## Architecture Decision

### What Goes in Template (Reusable)

| Component | Purpose | Sync Strategy |
|-----------|---------|---------------|
| Commands Framework | `/gap-analysis`, `/gap-planning`, etc. | Override in projects |
| Layer Templates | SPEC.md, API.md structure | Copy on init |
| Index Templates | FEATURES_INDEX.md structure | Generate per project |
| Testing Framework | Test patterns, TestTag system | Inherit |
| Architecture Docs | 5-layer lifecycle, patterns | Reference |

### What Stays in Projects (Specific)

| Component | Purpose | Example |
|-----------|---------|---------|
| Feature Specs | SPEC.md content | "Login with biometrics" |
| API Documentation | Actual endpoints | `/self/authentication` |
| Mockups | UI designs | Figma links, tokens |
| Implementation Status | Progress tracking | ✅/⚠️/❌ per feature |
| Feature List | Project features | auth, home, transfer... |

---

## Proposed Directory Structure

### Template Repository (`kmp-project-template`)

```
kmp-project-template/
├── .claude/
│   ├── commands/
│   │   ├── gap-analysis.md       # Parameterized framework
│   │   ├── gap-planning.md       # Parameterized framework
│   │   ├── design.md             # Parameterized framework
│   │   ├── implement.md          # Parameterized framework
│   │   ├── verify.md             # Parameterized framework
│   │   ├── verify-tests.md       # Parameterized framework
│   │   ├── session-start.md      # Session management
│   │   ├── session-end.md        # Session management
│   │   └── projectstatus.md      # Project overview
│   └── settings.json             # Default Claude settings
│
├── claude-product-cycle/
│   ├── _framework/
│   │   ├── 5-LAYER-LIFECYCLE.md          # Architecture overview
│   │   ├── O1-LOOKUP-PATTERN.md          # Index file strategy
│   │   ├── COMMAND-REFERENCE.md          # All commands explained
│   │   └── TESTING-STRATEGY.md           # TDD approach
│   │
│   ├── _templates/
│   │   ├── design-spec-layer/
│   │   │   ├── FEATURES_INDEX.template.md
│   │   │   ├── MOCKUPS_INDEX.template.md
│   │   │   ├── TESTING_STATUS.template.md
│   │   │   └── feature/
│   │   │       ├── SPEC.template.md
│   │   │       ├── API.template.md
│   │   │       └── STATUS.template.md
│   │   │
│   │   ├── server-layer/
│   │   │   ├── API_INDEX.template.md
│   │   │   ├── API_REFERENCE.template.md
│   │   │   ├── TESTING_STATUS.template.md
│   │   │   └── endpoints/
│   │   │       └── CATEGORY.template.md
│   │   │
│   │   ├── client-layer/
│   │   │   ├── FEATURE_MAP.template.md
│   │   │   ├── LAYER_STATUS.template.md
│   │   │   └── TESTING_STATUS.template.md
│   │   │
│   │   ├── feature-layer/
│   │   │   ├── MODULES_INDEX.template.md
│   │   │   ├── SCREENS_INDEX.template.md
│   │   │   ├── LAYER_STATUS.template.md
│   │   │   └── TESTING_STATUS.template.md
│   │   │
│   │   ├── platform-layer/
│   │   │   ├── LAYER_STATUS.template.md
│   │   │   ├── TESTING_STATUS.template.md
│   │   │   └── platforms/
│   │   │       ├── ANDROID.template.md
│   │   │       ├── IOS.template.md
│   │   │       ├── DESKTOP.template.md
│   │   │       └── WEB.template.md
│   │   │
│   │   └── gap-analysis/
│   │       ├── dashboard.template.md
│   │       └── layer-*.template.md
│   │
│   ├── PROJECT_CONFIG.md         # How to configure
│   ├── SYNC_GUIDE.md             # How to sync updates
│   └── CHANGELOG.md              # Version history
│
└── CLAUDE.md                     # References claude-product-cycle
```

### Derived Project (`mifos-mobile`, `mobile-wallet`, etc.)

```
mifos-mobile/
├── .claude/
│   ├── commands/                 # Can override template commands
│   │   └── custom-command.md     # Project-specific commands
│   └── settings.json             # Project-specific settings
│
├── claude-product-cycle/
│   ├── PROJECT.md                # Project identity & config
│   │   - name: "Mifos Mobile"
│   │   - type: "Self-Service Banking"
│   │   - features: [auth, home, accounts, ...]
│   │   - api_base: "https://server/fineract-provider/api/v1/self/"
│   │
│   ├── design-spec-layer/
│   │   ├── FEATURES_INDEX.md     # Generated from template
│   │   ├── MOCKUPS_INDEX.md      # Project-specific content
│   │   ├── STATUS.md
│   │   ├── TESTING_STATUS.md
│   │   └── features/
│   │       ├── auth/
│   │       │   ├── SPEC.md       # Project-specific
│   │       │   ├── API.md        # Project-specific
│   │       │   ├── STATUS.md
│   │       │   └── mockups/
│   │       ├── home/
│   │       └── ... (all project features)
│   │
│   ├── server-layer/
│   │   ├── API_INDEX.md          # Project-specific endpoints
│   │   ├── API_REFERENCE.md
│   │   ├── TESTING_STATUS.md
│   │   └── endpoints/            # Project-specific
│   │
│   ├── client-layer/
│   │   ├── FEATURE_MAP.md        # Project-specific mappings
│   │   ├── LAYER_STATUS.md
│   │   └── TESTING_STATUS.md
│   │
│   ├── feature-layer/
│   │   ├── MODULES_INDEX.md      # Project-specific modules
│   │   ├── SCREENS_INDEX.md      # Project-specific screens
│   │   ├── LAYER_STATUS.md
│   │   └── TESTING_STATUS.md
│   │
│   └── platform-layer/
│       ├── LAYER_STATUS.md       # Project-specific
│       ├── TESTING_STATUS.md
│       └── platforms/
│
└── CLAUDE.md                     # Project-specific instructions
```

---

## Project Configuration (`PROJECT.md`)

Each derived project has a `PROJECT.md` that configures the framework:

```markdown
# Project Configuration

## Identity

| Key | Value |
|-----|-------|
| name | Mifos Mobile |
| type | Self-Service Banking |
| repo | openMF/mifos-mobile |
| template_version | 1.0.0 |

## API Configuration

| Key | Value |
|-----|-------|
| base_url | https://{server}/fineract-provider/api/v1/self/ |
| auth_type | Basic + Tenant Header |
| demo_server | tt.mifos.community |
| demo_user | maria / password |

## Features

| # | Feature | Design Dir | Feature Dir |
|:-:|---------|------------|-------------|
| 1 | auth | features/auth/ | feature/auth/ |
| 2 | home | features/home/ | feature/home/ |
| ... | ... | ... | ... |

## Platforms

| Platform | Module | Status |
|----------|--------|--------|
| Android | cmp-android | Primary |
| iOS | cmp-ios | CocoaPods |
| Desktop | cmp-desktop | JVM |
| Web | cmp-web | Experimental |
```

---

## Command Parameterization

Commands in template read from `PROJECT.md` to adapt behavior:

### Template Command (gap-analysis.md)

```markdown
# Gap Analysis Command

## Instructions

### Step 0: Read Project Config

Read `claude-product-cycle/PROJECT.md` to get:
- Feature list
- API base URL
- Platform configuration

### Step 1: Read O(1) Index Files
[... rest of command using project config ...]
```

### Override Mechanism

Projects can override commands by creating same file in `.claude/commands/`:

```
Template: kmp-project-template/.claude/commands/gap-analysis.md
Override: mifos-mobile/.claude/commands/gap-analysis.md (takes precedence)
```

---

## Sync Strategy

### Option A: Git Subtree (Recommended)

```bash
# In derived project, add template as subtree
git subtree add --prefix=claude-product-cycle/_framework \
  https://github.com/openMF/kmp-project-template.git \
  main --squash

# Pull updates from template
git subtree pull --prefix=claude-product-cycle/_framework \
  https://github.com/openMF/kmp-project-template.git \
  main --squash
```

**Pros**:
- Single repo, no submodule complexity
- Can modify locally if needed
- Easy to pull updates

**Cons**:
- Subtree history can get messy
- Manual pull required

### Option B: Git Submodule

```bash
# Add template as submodule
git submodule add https://github.com/openMF/kmp-project-template.git \
  claude-product-cycle/_framework

# Update submodule
git submodule update --remote
```

**Pros**:
- Clear separation
- Explicit versioning

**Cons**:
- Submodule complexity
- Extra clone steps

### Option C: Copy + Version Tag (Simplest)

```bash
# Copy template files on setup
cp -r kmp-project-template/claude-product-cycle/_framework/ \
      mifos-mobile/claude-product-cycle/_framework/

# Track version in PROJECT.md
template_version: 1.0.0
```

**Pros**:
- Simplest to understand
- No git complexity

**Cons**:
- Manual sync
- Can diverge

### Recommendation: Option A (Git Subtree)

Best balance of simplicity and maintainability.

---

## Migration Steps

### Phase 1: Prepare Template (Week 1)

1. **Create framework docs in template**
   ```bash
   mkdir -p kmp-project-template/claude-product-cycle/_framework
   mkdir -p kmp-project-template/claude-product-cycle/_templates
   ```

2. **Extract reusable content from mifos-mobile**
   - Copy command files to template
   - Parameterize hardcoded values
   - Create template files with `{{PLACEHOLDER}}`

3. **Create PROJECT_CONFIG.md template**

4. **Test in template repo**

### Phase 2: Migrate mifos-mobile (Week 2)

1. **Restructure claude-product-cycle**
   ```bash
   # Move framework to _framework/
   # Keep project-specific in layer folders
   ```

2. **Create PROJECT.md with mifos-mobile config**

3. **Update commands to read from PROJECT.md**

4. **Verify all commands work**

### Phase 3: Rollout to Other Projects (Week 3-4)

1. **mobile-wallet (mifos-pay)**
   - Clone template structure
   - Create PROJECT.md
   - Generate initial index files
   - Add feature specs

2. **android-client (field officer)**
   - Same process

3. **mifos-x-group-banking**
   - Same process

### Phase 4: Establish Sync Process (Week 5)

1. **Document sync procedure**
2. **Create GitHub Action for version check**
3. **Add CHANGELOG.md to template**

---

## Project-Specific Customizations

### mifos-mobile (Self-Service)

| Aspect | Value |
|--------|-------|
| User Type | End User (Client) |
| API Prefix | `/self/` |
| Features | 17 (auth, accounts, transfer, etc.) |
| Auth | Username/Password + Passcode |

### mobile-wallet (Mifos Pay)

| Aspect | Value |
|--------|-------|
| User Type | Wallet User |
| API Prefix | `/wallet/` (different API) |
| Features | wallet, send, receive, history, etc. |
| Auth | Phone + OTP |

### android-client (Field Officer)

| Aspect | Value |
|--------|-------|
| User Type | Staff (Field Officer) |
| API Prefix | `/` (full API access) |
| Features | clients, loans, groups, collections, etc. |
| Auth | Username/Password + Staff permissions |

### mifos-x-group-banking

| Aspect | Value |
|--------|-------|
| User Type | Group Leader |
| API Prefix | `/groups/` |
| Features | groups, meetings, attendance, collections |
| Auth | Username/Password + Group permissions |

---

## Template Versioning

```markdown
# CHANGELOG.md (in template)

## [1.1.0] - 2025-02-01
### Added
- Testing documentation templates
- /verify-tests command

### Changed
- Improved gap-analysis comprehensive view

## [1.0.0] - 2025-01-05
### Added
- Initial 5-layer lifecycle framework
- O(1) lookup pattern
- All command templates
```

### Version Compatibility

| Template Version | mifos-mobile | mobile-wallet | android-client |
|------------------|--------------|---------------|----------------|
| 1.0.0 | ✅ | - | - |
| 1.1.0 | ✅ | ✅ | - |

---

## Files to Create in Template

### Priority 1: Framework Documentation

| File | Purpose | Lines |
|------|---------|:-----:|
| `_framework/5-LAYER-LIFECYCLE.md` | Core architecture | ~150 |
| `_framework/O1-LOOKUP-PATTERN.md` | Index strategy | ~100 |
| `_framework/COMMAND-REFERENCE.md` | All commands | ~200 |
| `_framework/TESTING-STRATEGY.md` | TDD approach | ~150 |

### Priority 2: Templates

| File | Purpose |
|------|---------|
| `_templates/design-spec-layer/*.template.md` | Design layer templates |
| `_templates/server-layer/*.template.md` | Server layer templates |
| `_templates/client-layer/*.template.md` | Client layer templates |
| `_templates/feature-layer/*.template.md` | Feature layer templates |
| `_templates/platform-layer/*.template.md` | Platform layer templates |

### Priority 3: Commands

| File | Purpose |
|------|---------|
| `.claude/commands/gap-analysis.md` | Parameterized |
| `.claude/commands/gap-planning.md` | Parameterized |
| `.claude/commands/design.md` | Parameterized |
| `.claude/commands/implement.md` | Parameterized |
| `.claude/commands/verify.md` | Parameterized |

---

## Success Criteria

### Template

- [ ] All framework docs in `_framework/`
- [ ] All templates in `_templates/`
- [ ] Parameterized commands in `.claude/commands/`
- [ ] PROJECT_CONFIG.md template
- [ ] SYNC_GUIDE.md documentation

### Each Derived Project

- [ ] PROJECT.md configured
- [ ] Feature specs in design-spec-layer
- [ ] API docs in server-layer
- [ ] Index files generated
- [ ] Commands working with project config

---

## Open Questions

1. **Command Override Strategy**
   - Should projects fully override commands or extend them?
   - How to handle project-specific commands?

2. **Sync Frequency**
   - How often should projects sync from template?
   - Breaking change policy?

3. **Feature Naming**
   - Standardize feature names across projects?
   - Or allow project-specific naming?

4. **Testing**
   - Should test framework be in template?
   - Or project-specific?

---

## Next Steps

1. Review this plan
2. Decide on sync strategy (subtree vs submodule vs copy)
3. Start Phase 1: Prepare template
4. Create PR to kmp-project-template

---

## Commands After Migration

```bash
# In any derived project
/gap-analysis          # Reads PROJECT.md, shows project-specific status
/gap-planning design   # Plans based on project features
/implement auth        # Implements based on project specs

# Sync from template
git subtree pull --prefix=claude-product-cycle/_framework \
  https://github.com/openMF/kmp-project-template.git main --squash
```
