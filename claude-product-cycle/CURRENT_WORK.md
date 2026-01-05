# Current Work

**Last Updated**: 2026-01-05
**Branch**: feature/design-specifications
**Session Note**: Phase 4 Complete - core:testing module created with fakes, fixtures, TestTags

---

## Active Tasks

| # | Task | Feature | Status | Files | Notes |
|---|------|---------|:------:|-------|-------|
| 1 | Command Rewrite | implement | ✅ Done | .claude/commands/implement.md | O(1) + Pattern Detection |
| 2 | Command Rewrite | client | ✅ Done | .claude/commands/client.md | O(1) + Pattern Detection |
| 3 | Command Rewrite | feature | ✅ Done | .claude/commands/feature.md | O(1) + TestTags |
| 4 | Command Rewrite | verify | ✅ Done | .claude/commands/verify.md | O(1) + Gap Detection |
| 5 | Command Rewrite | design | ✅ Done | .claude/commands/design.md | O(1) + Mockup Status |
| 6 | Command Rewrite | verify-tests | ✅ Done | .claude/commands/verify-tests.md | O(1) + Test Status |
| 7 | Design Token Integration | feature | ✅ Done | .claude/commands/feature.md | Phase 3.5 Token Integration |
| 8 | Design Tokens Index | design-spec | ✅ Done | DESIGN_TOKENS_INDEX.md | O(1) token lookup |
| 9 | Test Stub Generation | implement | ✅ Done | .claude/commands/implement.md | Phase 5: Auto-generate tests |
| 10 | TestTag Validation | verify | ✅ Done | .claude/commands/verify.md | Enhanced TestTag checks |
| 11 | Test Stubs Guide | docs | ✅ Done | TEST_STUBS_GUIDE.md | TDD reference |
| 12 | core:testing Module | testing | ✅ Done | core/testing/ | Fakes, fixtures, TestTags |
| 13 | Mockup Generation | home | ⏳ Next | features/home/mockups/ | Run `/design home mockup` |
| 14 | v2.0 UI Implementation | dashboard | Planned | feature/dashboard/ | After mockups done |

---

## In Progress

### Phase 1, 2 & 3 Complete: Commands + Design Integration + Testing Automation

**All 6 core commands now use O(1 lookup pattern**:

| Command | Index Files Used | Key Features |
|---------|------------------|--------------|
| `/implement` | FEATURE_MAP, MODULES_INDEX, SCREENS_INDEX | Pattern detection, TestTags |
| `/client` | FEATURE_MAP, API_INDEX | Service/Repository patterns |
| `/feature` | MODULES_INDEX, SCREENS_INDEX, DESIGN_TOKENS_INDEX | MVI pattern, TestTags, **Token Integration** |
| `/verify` | FEATURES_INDEX, FEATURE_MAP, MODULES_INDEX, SCREENS_INDEX, API_INDEX | Gap detection, verification scoring |
| `/design` | FEATURES_INDEX, MOCKUPS_INDEX, API_INDEX | Mockup status, tool selection |
| `/verify-tests` | TESTING_STATUS.md (all layers), MODULES_INDEX | Test dashboard, coverage tracking |

**Phase 2: Design Token Integration**:
- Created `DESIGN_TOKENS_INDEX.md` for O(1) token lookup
- Added Phase 3.5 to `/feature` command
- Supports both Google Stitch and MD3 token formats
- Auto-generates `${Feature}Theme.kt` with gradients/colors
- Auto-generates `${Feature}Animations.kt` if animations defined
- Maps tokens to existing `DesignToken` system

**Features with Design Tokens**: 8/17
- ✅ auth (google-stitch) - gradients, animations
- ✅ dashboard (md3) - components
- ✅ settings, guarantor, qr, passcode, location, client-charge (md3)

**Phase 3: Testing Automation**:
- Added Phase 5 to `/implement` command for test stub generation
- Enhanced `/verify` command with TestTag validation
- Created `TEST_STUBS_GUIDE.md` documentation
- Auto-generates: ViewModel tests, Screen tests, Fake repositories
- Validates: TestTag naming convention (`feature:component:id`)
- Supports TDD workflow: Red → Green → Refactor

**Testing Layer (6th Layer)**:
- Created `testing-layer/` with O(1) index files
- `LAYER_STATUS.md` - Test coverage dashboard (17 features)
- `TEST_PATTERNS.md` - ViewModel, Screen, Fake, Integration, Screenshot patterns
- `TEST_TAGS_INDEX.md` - TestTag specifications for all features
- `TEST_FIXTURES_INDEX.md` - Test fixture inventory
- `FAKE_REPOS_INDEX.md` - Fake repository inventory
- `patterns/` - Detailed pattern files (viewmodel-test.md, screen-test.md, etc.)
- `templates/` - Code templates (.kt.template files)

**Phase 4: core:testing Module** (NEW):
- Enabled `core:testing` in settings.gradle.kts
- Created KMP module with commonMain/androidMain source sets
- **TestTags**: Complete tags for all 17 features (auth, home, accounts, etc.)
- **Fake Repositories**: FakeUserAuthRepository, FakeHomeRepository, FakeAccountsRepository, FakeBeneficiaryRepository, FakeTransferRepository, FakeNotificationRepository
- **Test Fixtures**: UserFixture, ClientAccountsFixture, BeneficiaryFixture
- **Test Utils**: MainDispatcherRule, FlowTestExtensions, TestCoroutineExtensions
- **DI Module**: TestModule for Koin test setup
- Module compiles successfully ✅

---

## Design Layer - Phase 2: Mockup Generation

**Progress**: 2/17 features (12%)
- ✅ dashboard - mockups generated
- ✅ auth - mockups generated
- ⏳ home - next
- ⏳ 14 more features pending

**Commands**:
```
/gap-analysis design mockup      # See mockup progress (2/17)
/gap-planning design mockup      # Step-by-step plan
/design [feature] mockup         # Generate mockups for feature
```

---

## Recently Completed

| Date | Task | Feature | Outcome |
|------|------|---------|---------|
| 2026-01-05 | core:testing Module | core/testing/ | Fakes, fixtures, TestTags - compiles ✅ |
| 2026-01-05 | /gap-status command | commands | Plan progress tracking |
| 2026-01-05 | Testing Layer (6th Layer) | testing-layer/ | Full O(1) test infrastructure |
| 2026-01-05 | Phase 3: Testing Automation | implement/verify | Test stubs + TestTag validation |
| 2026-01-05 | TEST_STUBS_GUIDE.md | docs | TDD reference documentation |
| 2026-01-05 | Phase 2: Design Token Integration | feature | Phase 3.5 + DESIGN_TOKENS_INDEX.md |
| 2026-01-05 | Command Rewrite | verify | O(1) + Gap Detection + Verification Score |
| 2026-01-05 | Command Rewrite | design | O(1) + Mockup Status + Tool Selection |
| 2026-01-05 | Command Rewrite | verify-tests | O(1) + Test Dashboard + Coverage Tracking |
| 2026-01-05 | Command Rewrite | implement | O(1) + Pattern Detection + TestTags |
| 2026-01-05 | Command Rewrite | client | O(1) + Pattern Detection |
| 2026-01-05 | Command Rewrite | feature | O(1) + Pattern Detection + TestTags |
| 2026-01-03 | Auth mockups | auth | Generated PROMPTS.md + design-tokens.json |
| 2026-01-03 | MCP integration | design | Added tool selection, installed stitch-ai |
| 2026-01-03 | Commands README | commands | Full reference with all sub-commands |
| 2026-01-03 | Sub-section support | gap-analysis | Added {layer} {sub-section} syntax |

---

## Quick Context for Next Session

### Key Files to Read
1. This file (`CURRENT_WORK.md`)
2. `.claude/commands/implement.md` - E2E implementation with **Phase 5 Test Stubs**
3. `.claude/commands/feature.md` - Feature layer with **Phase 3.5 Token Integration**
4. `.claude/commands/verify.md` - Verification with O(1) + **TestTag Validation**
5. `.claude/commands/design.md` - Design with O(1) + Mockup Status
6. `.claude/commands/verify-tests.md` - Test verification with O(1)
7. `TEST_STUBS_GUIDE.md` - TDD test stub reference
8. `core/testing/` - **NEW** Testing module with fakes, fixtures, TestTags
9. `design-spec-layer/DESIGN_TOKENS_INDEX.md` - O(1) token lookup
10. `client-layer/FEATURE_MAP.md` - Service/Repository mapping
11. `feature-layer/MODULES_INDEX.md` - Module inventory
12. `feature-layer/SCREENS_INDEX.md` - Screen inventory

### Key Commands
- `/session-start` - Load this context
- `/gap-analysis` - Quick overview of all layers
- `/implement [feature]` - Full E2E implementation (updated)
- `/client [feature]` - Client layer only (updated)
- `/feature [feature]` - Feature layer only (updated)
- `/verify [feature]` - Verify implementation vs spec

### O(1) Index Files (Core Context)
| File | Purpose | Lines |
|------|---------|:-----:|
| FEATURE_MAP.md | Service → Feature mapping | ~170 |
| MODULES_INDEX.md | All feature modules | ~115 |
| SCREENS_INDEX.md | All 63 screens | ~270 |
| API_INDEX.md | All API endpoints | ~400 |
| FEATURES_INDEX.md | All 17 features | ~100 |
| DESIGN_TOKENS_INDEX.md | **NEW** Design tokens per feature | ~150 |

### Architecture Notes
- KMP: Android, iOS, Desktop, Web
- DI: Koin modules per feature
- Navigation: Jetbrains Compose Navigation
- Network: Ktorfit services
- State: MVI pattern (State, Event, Action)
- Testing: TestTags pattern (feature:component:id)

---

## Resume Instructions

1. Run `/session-start` to load context
2. Run `/gap-analysis` to see current status
3. Test new commands:
   - `/implement` - Should show feature list with O(1 lookup
   - `/client [feature]` - Should show service/repo status
   - `/feature [feature]` - Should show module/screen status
4. Continue mockup generation with `/design home mockup`
5. Eventually test full E2E with `/implement [new-feature]`

---

## Session History

| Date | Focus | Outcome |
|------|-------|---------|
| 2026-01-05 | Phase 4: core:testing Module | Created core/testing/ with fakes, fixtures, TestTags ✅ |
| 2026-01-05 | Testing Layer (6th Layer) | Created testing-layer/ with O(1 indexes, patterns, templates |
| 2026-01-05 | Phase 3: Testing Automation | Test stubs in /implement, TestTag validation in /verify |
| 2026-01-05 | Phase 2: Design-Code Integration | Token integration in /feature, DESIGN_TOKENS_INDEX.md |
| 2026-01-05 | Phase 1: Command Rewrite | All 6 core commands now O(1): verify, design, verify-tests |
| 2026-01-05 | Command rewrite | /implement, /client, /feature with O(1) + patterns |
| 2026-01-03 | Mockup generation | Auth mockups done, MCP integrated, 2/17 complete |
| 2026-01-03 | Command refactoring | Created template system, 5-layer structure |

---

## What's Next

1. **Write First Tests**: Use `core:testing` module to write ViewModel tests for auth feature
2. **Continue Mockups**: Generate mockups for remaining 15 features (`/design home mockup`)
3. **Dashboard Feature**: After mockups, implement new dashboard
4. **Phase 5: Session Persistence** (gap-planning roadmap)
   - Intelligent session checkpoints
   - Context continuity across restarts
5. **Phase 6: Documentation Integration** (gap-planning roadmap)
   - Auto-generate README updates
   - Architecture diagram generation
6. **Migration**: Move claude-product-cycle to separate repo (after validation)
