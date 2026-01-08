# /project-add - Add New Project (Interactive)

Create a new project workspace with the 5-layer structure.

## Usage

```
/project-add                     # Interactive project creation
/project-add "project-name"      # Create with specified name
```

## Instructions

### Step 1: Gather Project Info

If no name provided, ask:
```markdown
## New Project Setup

Please provide:
1. **Project Name** (lowercase, hyphenated): e.g., `my-app`, `fineract-client`
2. **Project Type**: Android | iOS | KMP | Web
3. **Description**: Brief project description
```

### Step 2: Create Workspace Structure

```bash
# Create workspace directory
mkdir -p claude-product-cycle/workspaces/{{PROJECT_NAME}}

# Create 6 layer directories
mkdir -p claude-product-cycle/workspaces/{{PROJECT_NAME}}/design-spec-layer/features
mkdir -p claude-product-cycle/workspaces/{{PROJECT_NAME}}/server-layer/endpoints
mkdir -p claude-product-cycle/workspaces/{{PROJECT_NAME}}/client-layer
mkdir -p claude-product-cycle/workspaces/{{PROJECT_NAME}}/feature-layer
mkdir -p claude-product-cycle/workspaces/{{PROJECT_NAME}}/platform-layer
mkdir -p claude-product-cycle/workspaces/{{PROJECT_NAME}}/testing-layer
```

### Step 3: Create PROJECT.md

Create `workspaces/{{PROJECT_NAME}}/PROJECT.md`:

```markdown
# {{PROJECT_NAME}}

**Type**: {{PROJECT_TYPE}}
**Created**: {{DATE}}
**Description**: {{DESCRIPTION}}

---

## Quick Start

```bash
/project-set {{PROJECT_NAME}}    # Activate this project
/gap-analysis                    # Check status
/design [feature]                # Start designing
```

---

## Structure

```
workspaces/{{PROJECT_NAME}}/
├── PROJECT.md              # This file
├── design-spec-layer/      # Feature specifications
│   └── features/           # Per-feature specs
├── server-layer/           # API documentation
│   └── endpoints/          # Per-endpoint docs
├── client-layer/           # Network/data layer tracking
├── feature-layer/          # UI layer tracking
├── platform-layer/         # Platform-specific tracking
└── testing-layer/          # Test tracking
```

---

## Status

| Layer | Progress | Next Action |
|-------|:--------:|-------------|
| Design | 0% | `/design [feature]` |
| Server | 0% | Document APIs |
| Client | 0% | `/client [feature]` |
| Feature | 0% | `/feature [feature]` |
| Platform | 0% | Platform setup |
```

### Step 4: Create Layer Index Files

**design-spec-layer/STATUS.md**:
```markdown
# Design Layer Status

**Project**: {{PROJECT_NAME}}
**Last Updated**: {{DATE}}

## Features

| Feature | SPEC | MOCKUP | API | STATUS | Complete |
|---------|:----:|:------:|:---:|:------:|:--------:|
| (none yet) | - | - | - | - | - |

---

**Next**: Run `/design [feature]` to add first feature
```

**client-layer/LAYER_STATUS.md**:
```markdown
# Client Layer Status

**Project**: {{PROJECT_NAME}}
**Last Updated**: {{DATE}}

## Services

| Service | Repository | Models | DI | Complete |
|---------|:----------:|:------:|:--:|:--------:|
| (none yet) | - | - | - | - |
```

**feature-layer/LAYER_STATUS.md**:
```markdown
# Feature Layer Status

**Project**: {{PROJECT_NAME}}
**Last Updated**: {{DATE}}

## Features

| Feature | ViewModel | Screen | Navigation | DI | Complete |
|---------|:---------:|:------:|:----------:|:--:|:--------:|
| (none yet) | - | - | - | - | - |
```

### Step 5: Optionally Set Active

Ask user:
```markdown
## Project Created

**Name**: {{PROJECT_NAME}}
**Location**: `workspaces/{{PROJECT_NAME}}/`

Would you like to set this as the active project?

1. **Yes** - Set active and start working
2. **No** - Keep current project active
```

If yes:
```bash
echo "{{PROJECT_NAME}}" > claude-product-cycle/ACTIVE_PROJECT
```

### Step 6: Show Completion

```markdown
## Project Created Successfully

**Project**: {{PROJECT_NAME}}
**Workspace**: `workspaces/{{PROJECT_NAME}}/`
**Active**: {{IS_ACTIVE}}

---

### Structure Created

```
workspaces/{{PROJECT_NAME}}/
├── PROJECT.md              ✅
├── design-spec-layer/      ✅
│   ├── STATUS.md           ✅
│   └── features/           ✅
├── server-layer/           ✅
│   └── endpoints/          ✅
├── client-layer/           ✅
│   └── LAYER_STATUS.md     ✅
├── feature-layer/          ✅
│   └── LAYER_STATUS.md     ✅
├── platform-layer/         ✅
└── testing-layer/          ✅
```

---

### Next Steps

1. `/project-set {{PROJECT_NAME}}` - Activate (if not active)
2. `/design [feature]` - Create first feature spec
3. `/gap-analysis` - View project status

---

**Ready to build!**
```

## Output Rules

1. Create complete workspace structure
2. Initialize all index/status files
3. Ask before setting as active
4. Show clear next steps
