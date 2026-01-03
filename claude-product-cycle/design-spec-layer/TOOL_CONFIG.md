# Design Tools Configuration

Configuration for AI design tools and MCP connections used in mockup generation.

---

## Selected AI Design Tool

**Current Tool**: Google Stitch

| Setting | Value |
|---------|-------|
| Primary Tool | `google-stitch` |
| Fallback Tool | `figma` |
| Prompt Format | `md3-detailed` |

### Available Tools

| ID | Tool | Website | MCP | Status |
|----|------|---------|:---:|:------:|
| `google-stitch` | Google Stitch | [stitch.withgoogle.com](https://stitch.withgoogle.com/) | ✅ | **Selected** |
| `figma` | Figma | [figma.com](https://www.figma.com/) | ✅ | Available |
| `uizard` | Uizard | [uizard.io](https://uizard.io/) | ❌ | Available |
| `visily` | Visily | [visily.ai](https://www.visily.ai/) | ❌ | Available |

---

## MCP Servers

### Google Stitch MCP

**Repository**: [github.com/StitchAI/stitch-ai-mcp](https://github.com/StitchAI/stitch-ai-mcp)

**Install**:
```bash
claude mcp add stitch-ai -- npx -y stitch-ai-mcp
```

**Features**:
- Generate UI designs from text prompts
- Material Design 3 native support
- Export to various formats
- Direct Figma integration

**Status**: Not Connected

---

### Figma MCP

**Documentation**: [Claude Code MCP Docs](https://github.com/anthropics/claude-code/tree/main/docs/mcp)

**Install**:
```bash
# Get your Figma token from: https://www.figma.com/developers/api#access-tokens
claude mcp add figma -- npx -y figma-mcp --token YOUR_FIGMA_TOKEN
```

**Features**:
- Read Figma files and components
- Extract design tokens
- Get component specifications
- Access design system

**Status**: Not Connected

---

## Prompt Formats

### MD3 Detailed (Google Stitch)

Best for Material Design 3 apps. Includes:
- Detailed component specifications
- Color tokens with gradients
- Typography scale
- Spacing and radius values
- Animation hints

### Figma Native

Best for Figma AI. Includes:
- Component structure
- Auto-layout hints
- Variant specifications
- Design system references

### Concise (Uizard/Visily)

Best for quick prototypes. Includes:
- Screen description
- Key components list
- Basic styling

---

## How to Change Tool

### Option 1: Edit this file

Change the "Current Tool" section above.

### Option 2: Select at runtime

When running `/design [feature] mockup`, you'll be prompted to select a tool.

### Option 3: Use command argument

```
/design auth mockup --tool=google-stitch
/design auth mockup --tool=figma
/design auth mockup --tool=uizard
```

---

## Workflow by Tool

### Google Stitch (Recommended)

```
1. /design [feature] mockup
2. Claude generates PROMPTS.md with Stitch format
3. If MCP connected: Claude sends to Stitch directly
   If no MCP: Copy prompt to stitch.withgoogle.com
4. Generate design
5. Export to Figma
6. Update FIGMA_LINKS.md
```

### Figma + AI

```
1. /design [feature] mockup
2. Claude generates PROMPTS.md with Figma format
3. Open Figma, use AI feature
4. Paste prompt
5. Generate design
6. Update FIGMA_LINKS.md
```

### Uizard / Visily

```
1. /design [feature] mockup
2. Claude generates PROMPTS.md with concise format
3. Open tool website
4. Paste prompt
5. Generate design
6. Export to Figma
7. Update FIGMA_LINKS.md
```

---

## Check MCP Status

Run in terminal:
```bash
claude mcp list
```

Expected output (if configured):
```
MCP Servers:
  stitch-ai    ✓ connected
  figma        ✓ connected
```

---

## Troubleshooting

### MCP Not Connecting

1. Check Node.js is installed: `node --version`
2. Check npx works: `npx --version`
3. Try reinstalling: `claude mcp remove stitch-ai && claude mcp add stitch-ai -- npx -y stitch-ai-mcp`

### Figma Token Issues

1. Generate new token at: https://www.figma.com/developers/api#access-tokens
2. Ensure token has read access
3. Reinstall with new token

### Stitch Not Generating

1. Check you're signed into Google account
2. Verify prompt format is correct
3. Try simpler prompt first
