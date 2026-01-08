# /project-set - Set Active Project

Switch the active project for all commands.

## Usage

```
/project-set mifos-mobile        # Switch to mifos-mobile project
/project-set test-project        # Switch to test-project
```

## Instructions

### Step 1: Validate Project Exists

```bash
ls claude-product-cycle/workspaces/{{PROJECT_NAME}}/
```

If directory doesn't exist, show error:
```markdown
## Error: Project Not Found

Project `{{PROJECT_NAME}}` does not exist.

**Available projects**:
{{PROJECT_LIST}}

Run `/project-add` to create a new project.
```

### Step 2: Update ACTIVE_PROJECT

```bash
echo "{{PROJECT_NAME}}" > claude-product-cycle/ACTIVE_PROJECT
```

### Step 3: Confirm Switch

```markdown
## Project Switched

**Active Project**: {{PROJECT_NAME}}
**Workspace**: `workspaces/{{PROJECT_NAME}}/`

---

### Project Summary

| Layer | Status |
|-------|:------:|
| Design | {{DESIGN_STATUS}} |
| Server | {{SERVER_STATUS}} |
| Client | {{CLIENT_STATUS}} |
| Feature | {{FEATURE_STATUS}} |
| Platform | {{PLATFORM_STATUS}} |

---

**Next**: Run `/gap-analysis` to see current status
```

## Output Rules

1. Validate project exists before switching
2. Update ACTIVE_PROJECT file
3. Show project summary after switch
