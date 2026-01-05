# Client Layer Analysis Template

## Client Layer Gap Analysis

**Location**: `core/network/` + `core/data/` + `core/model/`
**Reference**: `claude-product-cycle/client-layer/LAYER_STATUS.md`
**Last Updated**: {{DATE}}

---

### Implementation Status

| # | Feature | Service | Repository | Models | DI | Complete |
|:-:|---------|:-------:|:----------:|:------:|:--:|:--------:|
{{CLIENT_STATUS_ROWS}}

---

### File Locations

| Component | Pattern | Example |
|-----------|---------|---------|
| Service | `core/network/services/{Feature}Service.kt` | `AuthService.kt` |
| Repository | `core/data/repository/{Feature}Repository.kt` | `AuthRepository.kt` |
| Models | `core/model/{feature}/` | `core/model/auth/` |
| DI | `core/data/di/DataModule.kt` | Single module |

---

### Layer Breakdown

```
CLIENT LAYER BREAKDOWN
├── Network Services   {{SERVICE_BAR}} {{SERVICE_COUNT}}/{{SERVICE_TOTAL}} ({{SERVICE_PCT}}%)
├── Repositories       {{REPO_BAR}} {{REPO_COUNT}}/{{REPO_TOTAL}} ({{REPO_PCT}}%)
├── Domain Models      {{MODEL_BAR}} {{MODEL_COUNT}}/{{MODEL_TOTAL}} ({{MODEL_PCT}}%)
└── DI Modules         {{DI_BAR}} {{DI_COUNT}}/{{DI_TOTAL}} ({{DI_PCT}}%)

OVERALL                {{OVERALL_BAR}} {{OVERALL_PCT}}%
```

---

### Gaps

{{CLIENT_GAPS_TABLE}}

---

**Next**: `/gap-analysis feature` to check UI layer
