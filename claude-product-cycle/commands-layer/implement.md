# /implement - E2E Feature Implementation

## Purpose
Full end-to-end implementation of a feature including client layer (Network + Data) and feature layer (UI).

---

## Command Variants

```
/implement                       → Show feature status list
/implement [Feature]             → Full E2E implementation
/implement [Feature] --quick     → Skip validations
/implement [Feature] --no-git    → Skip git integration
/implement improve [Feature]     → Improve existing feature
```

---

## E2E Pipeline

```
┌─────────────────────────────────────────────────────────────────────┐
│  /implement [Feature] - E2E AUTOMATED PIPELINE                      │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│  ✅ Git Integration     - Auto branch, commits after each phase     │
│  ✅ Dependency Check    - Validate all dependencies before start    │
│  ✅ Auto-Build          - Gradle build after each layer             │
│  ✅ Lint & Format       - Run detekt, spotless                      │
│  ✅ Checkpoints         - Review/improve after each layer           │
│  ✅ Progress Dashboard  - Real-time progress tracking               │
│                                                                      │
│  FULL PIPELINE:                                                      │
│  ┌───────┐  ┌────────┐  ┌────────┐  ┌─────────┐  ┌───────┐         │
│  │  GIT  │─▶│VALIDATE│─▶│ CLIENT │─▶│ FEATURE │─▶│ BUILD │         │
│  └───────┘  └────────┘  └───┬────┘  └────┬────┘  └───┬───┘         │
│   branch     deps           │            │           │              │
│                        [checkpoint] [checkpoint] [commit]           │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘
```

---

## Key Files

1. `claude-product-cycle/design-spec-layer/features/[feature]/SPEC.md` - What to build
2. `claude-product-cycle/design-spec-layer/features/[feature]/API.md` - APIs needed
3. `claude-product-cycle/design-spec-layer/_shared/PATTERNS.md` - Implementation patterns

---

## Implementation Flow

```
┌─────────────────────────────────────────────────────────────────────┐
│  E2E IMPLEMENTATION PIPELINE                                         │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│  PHASE 0: GIT SETUP                                                  │
│  ├─→ Check working directory is clean                               │
│  ├─→ Create branch: git checkout -b feature/{name}                  │
│  └─→ [AUTO-CONTINUE]                                                │
│                                                                      │
│  PHASE 1: DEPENDENCY VALIDATION                                      │
│  ├─→ Read SPEC.md + API.md                                          │
│  ├─→ Check required services exist                                   │
│  ├─→ Check Kotlin dependencies available                            │
│  ├─→ Identify gaps                                                   │
│  └─→ [AUTO-CONTINUE if all deps satisfied]                          │
│                                                                      │
│  PHASE 2: CLIENT LAYER                                               │
│  ├─→ Create/update DTOs in core/network/model/ (if needed)          │
│  ├─→ Create/update Service in core/network/services/                │
│  ├─→ Create/update Repository in core/data/repository/              │
│  ├─→ Register in DI modules                                         │
│  ├─→ 🔨 BUILD: ./gradlew :core:network:build :core:data:build       │
│  ├─→ 🧹 LINT: spotlessApply                                         │
│  ├─→ 📝 COMMIT: git commit -m "feat({name}): Add client layer"      │
│  └─→ ⏸️ CHECKPOINT: Client Summary + Options                         │
│                                                                      │
│  PHASE 3: FEATURE LAYER                                              │
│  ├─→ Create ViewModel (State, Event, Action)                        │
│  ├─→ Create Screen (Compose UI)                                     │
│  ├─→ Create Components                                               │
│  ├─→ Create Navigation                                               │
│  ├─→ Register in DI module                                           │
│  ├─→ 🔨 BUILD: ./gradlew :feature:{name}:build                      │
│  ├─→ 🧹 LINT: spotlessApply detekt                                  │
│  ├─→ 📝 COMMIT: git commit -m "feat({name}): Add feature layer"     │
│  └─→ ⏸️ CHECKPOINT: Feature Summary + Options                        │
│                                                                      │
│  PHASE 4: FINALIZE                                                   │
│  ├─→ Update feature's STATUS.md                                     │
│  ├─→ Update main STATUS.md                                          │
│  ├─→ 🔨 FINAL BUILD: ./gradlew build                                │
│  └─→ 📝 COMMIT: git commit -m "docs({name}): Update status"         │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘
```

---

## Checkpoint Templates

### After CLIENT Layer:

```
┌──────────────────────────────────────────────────────────────────────┐
│  ✅ CLIENT LAYER COMPLETE                                            │
├──────────────────────────────────────────────────────────────────────┤
│                                                                       │
│  Created/Updated Files:                                               │
│  ├─ core/network/model/[Feature]Dto.kt                               │
│  ├─ core/network/services/[Feature]Service.kt                        │
│  ├─ core/data/repository/[Feature]Repository.kt                      │
│  └─ core/data/repositoryImpl/[Feature]RepositoryImpl.kt              │
│                                                                       │
│  Registered in DI:                                                    │
│  ├─ NetworkModule: [Feature]Service ✅                                │
│  └─ DataModule: [Feature]Repository ✅                                │
│                                                                       │
│  🔨 BUILD: :core:network ✅ :core:data ✅                             │
│  🧹 LINT: spotlessApply ✅                                            │
│  📝 COMMIT: feat([feature]): Add client layer                        │
│                                                                       │
├──────────────────────────────────────────────────────────────────────┤
│  Options:                                                             │
│  • c / continue  → Proceed to FEATURE layer                          │
│  • i / improve   → Describe what to improve                          │
│  • v / view      → Show file content                                 │
└──────────────────────────────────────────────────────────────────────┘
```

### After FEATURE Layer:

```
┌──────────────────────────────────────────────────────────────────────┐
│  ✅ FEATURE LAYER COMPLETE                                           │
├──────────────────────────────────────────────────────────────────────┤
│                                                                       │
│  Created/Updated Files:                                               │
│  ├─ feature/[name]/[Feature]ViewModel.kt                             │
│  ├─ feature/[name]/[Feature]Screen.kt                                │
│  ├─ feature/[name]/components/*.kt                                   │
│  ├─ feature/[name]/navigation/[Feature]Navigation.kt                 │
│  └─ feature/[name]/di/[Feature]Module.kt                             │
│                                                                       │
│  Navigation:                                                          │
│  └─ Route registered ✅                                               │
│                                                                       │
│  🔨 BUILD: :feature:[name] ✅                                         │
│  🧹 LINT: spotlessApply ✅ detekt ✅                                  │
│  📝 COMMIT: feat([feature]): Add feature layer                       │
│                                                                       │
├──────────────────────────────────────────────────────────────────────┤
│  Options:                                                             │
│  • c / continue  → Complete implementation, update status            │
│  • i / improve   → Describe improvement                              │
│  • v / view [file] → Show specific file content                      │
└──────────────────────────────────────────────────────────────────────┘
```

---

## Final Report Template

```
╔══════════════════════════════════════════════════════════════════════╗
║  /implement [Feature] - COMPLETE                                      ║
╠══════════════════════════════════════════════════════════════════════╣
║                                                                       ║
║  ✅ PHASE 0: GIT SETUP                                                ║
║     └─ Branch: feature/[name]                                         ║
║                                                                       ║
║  ✅ PHASE 1: DEPENDENCY VALIDATION                                    ║
║     └─ All dependencies satisfied                                     ║
║                                                                       ║
║  ✅ PHASE 2: CLIENT LAYER                                             ║
║     ├─ Files: [count] created/updated                                 ║
║     ├─ Build: :core:network ✅ :core:data ✅                          ║
║     └─ Commit: feat([feature]): Add client layer                     ║
║                                                                       ║
║  ✅ PHASE 3: FEATURE LAYER                                            ║
║     ├─ Files: [count] created/updated                                 ║
║     ├─ Build: :feature:[name] ✅                                      ║
║     └─ Commit: feat([feature]): Add feature layer                    ║
║                                                                       ║
║  ✅ PHASE 4: FINALIZE                                                 ║
║     ├─ Updated: STATUS.md                                             ║
║     ├─ Final Build: ./gradlew build ✅                                ║
║     └─ Commit: docs([feature]): Update status                        ║
║                                                                       ║
╠══════════════════════════════════════════════════════════════════════╣
║  📊 SUMMARY                                                           ║
║  ├─ Files: +[count] created, ~[count] modified                        ║
║  ├─ Commits: [count]                                                  ║
║  └─ Errors: 0                                                         ║
║                                                                       ║
╠══════════════════════════════════════════════════════════════════════╣
║  🎉 IMPLEMENTATION COMPLETE                                           ║
║                                                                       ║
║  Next steps:                                                          ║
║  • Push branch: git push -u origin feature/[name]                    ║
║  • Create PR: gh pr create                                           ║
║  • Verify: /verify [Feature]                                         ║
║                                                                       ║
╚══════════════════════════════════════════════════════════════════════╝
```

---

## Cross-Update Rules

After ANY implementation:
1. Update feature's `STATUS.md`
2. Update main `claude-product-cycle/design-spec-layer/STATUS.md`
3. Add changelog entries

---

## If No Feature Name Provided

Show feature list:

```
📋 FEATURES AVAILABLE FOR IMPLEMENTATION:

| Feature | Status | Client | Feature | Command |
|---------|--------|--------|---------|---------|
| auth | ✅ Done | ✅ | ✅ | /implement auth |
| home | ✅ Done | ✅ | ✅ | /implement home |
...

Which feature do you want to implement?
```
