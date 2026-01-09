# /project-list - List All Projects

List all available projects in the multi-project workspace.

## Usage

```
/project-list                    # List all projects
```

## Instructions

### Step 1: Read Workspaces

```bash
ls claude-product-cycle/workspaces/
```

### Step 2: Get Active Project

```bash
cat claude-product-cycle/ACTIVE_PROJECT
```

### Step 3: Display Project List

```markdown
## Available Projects

| # | Project | Status | Path |
|:-:|---------|:------:|------|
{{PROJECT_ROWS}}

**Active**: {{ACTIVE_PROJECT}}

---

### Commands

- `/project-set [name]` - Switch to a project
- `/project-add` - Add new project
```

## Output Rules

1. List all directories in workspaces/
2. Mark active project with arrow (→)
3. Show path to each workspace
