# Commands Layer

This directory contains slash command definitions for Claude Code integration with the Mifos Mobile project.

## Available Commands

| Command | File | Description |
|---------|------|-------------|
| `/projectstatus` | projectstatus.md | Display project overview and feature status |
| `/design [Feature]` | design.md | Create or update feature specification |
| `/implement [Feature]` | implement.md | Full E2E feature implementation |
| `/client [Feature]` | client.md | Implement Network + Data layers |
| `/feature [Feature]` | feature.md | Implement UI layer (ViewModel + Screen) |
| `/verify [Feature]` | verify.md | Validate implementation vs specification |

## Usage

Copy these files to your `.claude/commands/` directory or reference them directly when working with Claude Code.

## Workflow

```
/projectstatus          → See current state
       │
       ▼
/design [Feature]       → Create specification (Opus)
       │
       ▼
/implement [Feature]    → Full implementation (Sonnet)
       │
       ▼
/verify [Feature]       → Validate implementation
```
