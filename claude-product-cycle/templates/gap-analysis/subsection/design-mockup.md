# Gap Analysis: Design → Mockups Sub-Section

## Mockups Generation Status

**Phase**: Phase 2 - Mockup Generation
**Progress**: {{MOCKUPS_COUNT}}/17 features ({{MOCKUPS_PCT}}%)

---

### 🔌 Design Tools & MCP Connections

Before generating mockups, ensure your design tools are configured:

#### AI Design Tools

| Tool | Status | Website | MCP Available |
|------|:------:|---------|:-------------:|
| Google Stitch | {{STITCH_STATUS}} | [stitch.withgoogle.com](https://stitch.withgoogle.com/) | ✅ [stitch-ai-mcp](https://github.com/StitchAI/stitch-ai-mcp) |
| Figma | {{FIGMA_STATUS}} | [figma.com](https://www.figma.com/) | ✅ [figma-mcp](https://github.com/anthropics/claude-code/tree/main/docs/mcp) |
| Uizard | {{UIZARD_STATUS}} | [uizard.io](https://uizard.io/) | ❌ |
| Visily | {{VISILY_STATUS}} | [visily.ai](https://www.visily.ai/) | ❌ |

**Recommended**: Google Stitch (Material Design 3 native, has MCP)

#### MCP Connection Status

```
MCP SERVERS
├── figma          {{FIGMA_MCP_STATUS}}
├── stitch-ai      {{STITCH_MCP_STATUS}}
└── (other)        {{OTHER_MCP_STATUS}}
```

#### Setup MCP (if not connected)

**Google Stitch MCP** (Recommended):
```bash
claude mcp add stitch-ai -- npx -y stitch-ai-mcp
```

**Figma MCP**:
```bash
claude mcp add figma -- npx -y figma-mcp --token YOUR_FIGMA_TOKEN
```

**Check MCP Status**:
```bash
claude mcp list
```

---

### 🎨 Choose Your AI Design Tool

Select which AI tool to use for mockup generation:

| Option | Tool | Best For | Prompt Format |
|:------:|------|----------|---------------|
| **1** | Google Stitch | Material Design 3, Android/KMP | Detailed MD3 prompts |
| **2** | Figma + AI | Custom designs, team collaboration | Figma-native prompts |
| **3** | Uizard | Quick prototypes | Concise feature prompts |
| **4** | Visily | Component-focused | Checklist prompts |

**Current Selection**: {{SELECTED_TOOL}}

To change: Update `design-spec-layer/TOOL_CONFIG.md` or select when running `/design [feature] mockup`

---

### Status Overview

```
MOCKUP GENERATION PROGRESS
{{MOCKUPS_PROGRESS_BAR}} {{MOCKUPS_PCT}}%

├── PROMPTS.md     {{PROMPTS_COUNT}}/17 generated
├── design-tokens  {{TOKENS_COUNT}}/17 generated
└── Figma URLs     {{FIGMA_COUNT}}/17 captured
```

---

### Feature Status

| # | Feature | MOCKUP.md | mockups/ | PROMPTS.md | design-tokens | Figma |
|:-:|---------|:---------:|:--------:|:----------:|:-------------:|:-----:|
| 1 | auth | v2.0 ✅ | {{AUTH_MOCKUPS}} | {{AUTH_PROMPTS}} | {{AUTH_TOKENS}} | {{AUTH_FIGMA}} |
| 2 | home | v2.0 ✅ | {{HOME_MOCKUPS}} | {{HOME_PROMPTS}} | {{HOME_TOKENS}} | {{HOME_FIGMA}} |
| 3 | accounts | v2.0 ✅ | {{ACCOUNTS_MOCKUPS}} | {{ACCOUNTS_PROMPTS}} | {{ACCOUNTS_TOKENS}} | {{ACCOUNTS_FIGMA}} |
| 4 | savings-account | v2.0 ✅ | {{SAVINGS_MOCKUPS}} | {{SAVINGS_PROMPTS}} | {{SAVINGS_TOKENS}} | {{SAVINGS_FIGMA}} |
| 5 | loan-account | v2.0 ✅ | {{LOAN_MOCKUPS}} | {{LOAN_PROMPTS}} | {{LOAN_TOKENS}} | {{LOAN_FIGMA}} |
| 6 | share-account | v2.0 ✅ | {{SHARE_MOCKUPS}} | {{SHARE_PROMPTS}} | {{SHARE_TOKENS}} | {{SHARE_FIGMA}} |
| 7 | beneficiary | v2.0 ✅ | {{BENEFICIARY_MOCKUPS}} | {{BENEFICIARY_PROMPTS}} | {{BENEFICIARY_TOKENS}} | {{BENEFICIARY_FIGMA}} |
| 8 | transfer | v2.0 ✅ | {{TRANSFER_MOCKUPS}} | {{TRANSFER_PROMPTS}} | {{TRANSFER_TOKENS}} | {{TRANSFER_FIGMA}} |
| 9 | recent-transaction | v2.0 ✅ | {{RECENT_MOCKUPS}} | {{RECENT_PROMPTS}} | {{RECENT_TOKENS}} | {{RECENT_FIGMA}} |
| 10 | notification | v2.0 ✅ | {{NOTIFICATION_MOCKUPS}} | {{NOTIFICATION_PROMPTS}} | {{NOTIFICATION_TOKENS}} | {{NOTIFICATION_FIGMA}} |
| 11 | settings | v2.0 ✅ | {{SETTINGS_MOCKUPS}} | {{SETTINGS_PROMPTS}} | {{SETTINGS_TOKENS}} | {{SETTINGS_FIGMA}} |
| 12 | passcode | v2.0 ✅ | {{PASSCODE_MOCKUPS}} | {{PASSCODE_PROMPTS}} | {{PASSCODE_TOKENS}} | {{PASSCODE_FIGMA}} |
| 13 | guarantor | v2.0 ✅ | {{GUARANTOR_MOCKUPS}} | {{GUARANTOR_PROMPTS}} | {{GUARANTOR_TOKENS}} | {{GUARANTOR_FIGMA}} |
| 14 | qr | v2.0 ✅ | {{QR_MOCKUPS}} | {{QR_PROMPTS}} | {{QR_TOKENS}} | {{QR_FIGMA}} |
| 15 | location | v2.0 ✅ | {{LOCATION_MOCKUPS}} | {{LOCATION_PROMPTS}} | {{LOCATION_TOKENS}} | {{LOCATION_FIGMA}} |
| 16 | client-charge | v2.0 ✅ | {{CLIENT_MOCKUPS}} | {{CLIENT_PROMPTS}} | {{CLIENT_TOKENS}} | {{CLIENT_FIGMA}} |
| 17 | dashboard | v2.0 ✅ | {{DASHBOARD_MOCKUPS}} | {{DASHBOARD_PROMPTS}} | {{DASHBOARD_TOKENS}} | {{DASHBOARD_FIGMA}} |

**Legend**: ✅ Complete | ❌ Missing | ⏳ Pending

---

### Pending Features ({{PENDING_COUNT}})

{{PENDING_FEATURES_LIST}}

---

### Next Actions

**1. Setup MCP (if not configured)**:
```bash
claude mcp add stitch-ai -- npx -y stitch-ai-mcp
```

**2. Generate mockups for next feature**:
```
/design {{NEXT_FEATURE}} mockup
```

**3. Or run full planning**:
```
/gap-planning design mockup
```

---

### Workflow Reference

```
┌─────────────────────────────────────────────────────────────────┐
│                 MOCKUP GENERATION WORKFLOW                       │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  1. MOCKUP.md (ASCII v2.0 design)                               │
│         ↓                                                        │
│  2. /design [feature] mockup                                     │
│         ↓                                                        │
│     ┌───────────────────────────────────────┐                   │
│     │ Generates:                             │                   │
│     │ • mockups/PROMPTS.md (AI tool prompts)│                   │
│     │ • mockups/design-tokens.json          │                   │
│     └───────────────────────────────────────┘                   │
│         ↓                                                        │
│  3. AI Design Tool (choose one):                                │
│     • Google Stitch (via MCP or web)                            │
│     • Figma AI                                                   │
│     • Uizard / Visily                                           │
│         ↓                                                        │
│  4. Export to Figma                                              │
│         ↓                                                        │
│  5. Update mockups/FIGMA_LINKS.md                               │
│         ↓                                                        │
│  6. /implement [feature] (reads from Figma MCP)                 │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

### MCP Integration Benefits

| With MCP | Without MCP |
|----------|-------------|
| Claude can directly generate designs | Manual copy/paste prompts |
| Auto-export to Figma | Manual export |
| Read designs back for implementation | Manual reference |
| Seamless workflow | Multiple tool switches |

**Recommended Setup**: Google Stitch MCP + Figma MCP for end-to-end automation
