# Gap Planning Command

Plan improvements for a specific feature by analyzing v2.0 design specifications vs current implementation.

## Usage

```
/gap-planning              # List available features (don't plan all)
/gap-planning home         # Plan v2.0 improvements for home feature
/gap-planning dashboard    # Plan full implementation for dashboard
/gap-planning accounts     # Plan v2.0 improvements for accounts feature
```

## Available Features

| # | Feature | Design Dir | Implementation Dir | Gap Type |
|:-:|---------|------------|-------------------|----------|
| 1 | `auth` | features/auth/ | feature/auth/ | v2.0 UI |
| 2 | `home` | features/home/ | feature/home/ | v2.0 UI |
| 3 | `accounts` | features/accounts/ | feature/account/ | v2.0 UI |
| 4 | `savings-account` | features/savings-account/ | feature/savings-account/ | v2.0 UI |
| 5 | `loan-account` | features/loan-account/ | feature/loan-account/ | v2.0 UI |
| 6 | `share-account` | features/share-account/ | feature/share-account/ | v2.0 UI |
| 7 | `beneficiary` | features/beneficiary/ | feature/beneficiary/ | v2.0 UI |
| 8 | `transfer` | features/transfer/ | feature/transfer-process/ | v2.0 UI |
| 9 | `recent-transaction` | features/recent-transaction/ | feature/recent-transaction/ | v2.0 UI |
| 10 | `notification` | features/notification/ | feature/notification/ | v2.0 UI |
| 11 | `settings` | features/settings/ | feature/settings/ | v2.0 UI |
| 12 | `passcode` | features/passcode/ | libs/mifos-passcode/ | v2.0 UI |
| 13 | `guarantor` | features/guarantor/ | feature/guarantor/ | v2.0 UI |
| 14 | `qr` | features/qr/ | feature/qr-code/ | v2.0 UI |
| 15 | `location` | features/location/ | feature/location/ | v2.0 UI |
| 16 | `client-charge` | features/client-charge/ | feature/user-profile/ | v2.0 UI |
| 17 | `dashboard` | features/dashboard/ | (NEW) | Full Feature |

## Instructions

### If No Parameter

Just list available features:

```markdown
## Gap Planning

Specify a feature to plan improvements:

| Feature | Current Status | Gap Type | Command |
|---------|---------------|----------|---------|
| auth | Client ✅ / Feature ✅ | v2.0 UI | `/gap-planning auth` |
| home | Client ✅ / Feature ✅ | v2.0 UI | `/gap-planning home` |
| dashboard | Client ✅ / Feature ❌ | Full Feature | `/gap-planning dashboard` |
| ... | ... | ... | ... |

**Gap Types:**
- **v2.0 UI**: Feature works but needs UI update to match MOCKUP v2.0
- **Full Feature**: Feature layer doesn't exist yet

Example: `/gap-planning home`
```

### If Feature Parameter Provided (e.g., `/gap-planning home`)

**Step 1**: Read ONLY these files:
- `claude-product-cycle/design-spec-layer/features/{FEATURE}/MOCKUP.md`
- `claude-product-cycle/design-spec-layer/features/{FEATURE}/STATUS.md`
- Actual implementation files (use Glob to find them)

**Step 2**: Analyze current state vs v2.0 design:
- Parse MOCKUP.md for v2.0 design requirements
- Check what UI components exist
- Identify visual/UX gaps
- Check for missing functionality

**Step 3**: Output improvement plan:

```markdown
## Improvement Plan: [Feature Name]

**Current**: Client ✅ | Feature ✅ | v2.0 UI ❌
**Target**: Client ✅ | Feature ✅ | v2.0 UI ✅

---

### Current Implementation

| Component | Type | Status | File |
|-----------|------|:------:|------|
| [Name] | ViewModel | ✅ | feature/.../viewmodel/...ViewModel.kt |
| [Name] | Screen | ⚠️ | feature/.../...Screen.kt |
| [Name] | Component | ❌ | (missing) |

---

### v2.0 Design Requirements (from MOCKUP.md)

| Design Element | Requirement | Current | Gap |
|----------------|-------------|:-------:|-----|
| Hero Card | Gradient #667EEA → #764BA2 | ❌ | Add gradient |
| Balance Text | 36sp ExtraBold, White | ⚠️ | Update typography |
| Analytics Chart | Line chart with gradient fill | ❌ | New component |
| Quick Actions | 5 icons, 56dp, rounded | ⚠️ | Update styling |
| ... | ... | ... | ... |

---

### Gaps to Address

| # | Gap | Type | Priority | Effort |
|---|-----|------|:--------:|:------:|
| 1 | Add HeroGradientCard | UI | P1 | S |
| 2 | Create SpendingAnalyticsChart | UI+Logic | P1 | M |
| 3 | Add QuickActionBar v2.0 | UI | P1 | S |
| 4 | Implement RecentRecipientsRow | UI+Data | P1 | M |
| 5 | Add micro-animations | UI | P2 | S |
| 6 | Implement dark mode colors | UI | P2 | S |

---

### Implementation Tasks

#### Task 1: Add HeroGradientCard Component (P1, Effort: S)

**Goal**: Create reusable hero card with primary gradient background

**Files to create/modify**:
- `core/designsystem/src/commonMain/.../component/HeroGradientCard.kt` - New component
- `feature/home/src/commonMain/.../HomeScreen.kt` - Use new component

**Code sketch**:
```kotlin
// core/designsystem/.../component/HeroGradientCard.kt
@Composable
fun HeroGradientCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF667EEA),
                            Color(0xFF764BA2)
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                    )
                )
                .padding(24.dp)
        ) {
            Column(content = content)
        }
    }
}
```

**Verify**:
- Run app, navigate to home
- See gradient card instead of solid color
- Check dark mode appearance

---

#### Task 2: Create SpendingAnalyticsChart (P1, Effort: M)

**Goal**: Add weekly spending chart with line graph and category breakdown

**Files to create/modify**:
- `feature/home/src/commonMain/.../component/SpendingAnalyticsCard.kt` - New
- `feature/home/src/commonMain/.../HomeViewModel.kt` - Add analytics state
- `feature/home/src/commonMain/.../HomeScreen.kt` - Include chart

**Code sketch**:
```kotlin
// SpendingAnalyticsCard.kt
@Composable
fun SpendingAnalyticsCard(
    weeklySpending: List<SpendingPoint>,
    categories: List<CategorySpending>,
    onCategoryClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    MifosCard(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("This Week", style = MaterialTheme.typography.titleMedium)
                Text("Dec 23-30 →", style = MaterialTheme.typography.bodySmall)
            }

            Spacer(Modifier.height(8.dp))

            // Amount with change
            Text(
                text = "$${weeklySpending.sumOf { it.amount }}",
                style = MaterialTheme.typography.headlineMedium
            )

            // Line Chart
            SpendingLineChart(
                data = weeklySpending,
                modifier = Modifier.height(80.dp).fillMaxWidth()
            )

            // Category chips (horizontal scroll)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories) { category ->
                    CategoryChip(
                        category = category,
                        onClick = { onCategoryClick(category.name) }
                    )
                }
            }
        }
    }
}
```

**Verify**:
- Chart renders with sample data
- Touch interactions work
- Categories are scrollable

---

#### Task 3: Update QuickActionBar to v2.0 (P1, Effort: S)

**Goal**: Update quick actions to match v2.0 design (56dp, rounded, icons)

**Files to modify**:
- `feature/home/src/commonMain/.../component/QuickActionsRow.kt`

**Code sketch**:
```kotlin
@Composable
fun QuickActionButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Surface(
            modifier = Modifier.size(56.dp),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.padding(16.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall
        )
    }
}
```

**Verify**: Quick actions have rounded containers, proper spacing

---

### After Completion (REQUIRED)

**Step 1: Verify Implementation**
```bash
./gradlew :feature:home:test
./gradlew :cmp-android:assembleDemoDebug
# Run app and visually verify v2.0 design
```

**Step 2: Cross-Update Documentation**

Update `claude-product-cycle/design-spec-layer/features/{FEATURE}/STATUS.md`:
```markdown
## Layer Status

| Layer | Status | Files |
|-------|--------|-------|
| Feature | ✅ | Updated to v2.0 design |

## v2.0 Implementation Status

| Component | Status |
|-----------|:------:|
| HeroGradientCard | ✅ |
| SpendingAnalyticsCard | ✅ |
| QuickActionsRow v2.0 | ✅ |
```

Update `claude-product-cycle/PRODUCT_MAP.md`:
```markdown
| 2 | **home** | ✅ | ✅ v2.0 | ✅ | ✅ v2.0 | v2.0 Implemented |
```

**Step 3: Commit Changes**
```bash
git add -A
git commit -m "feat(home): Implement v2.0 design with analytics and gradients

- Add HeroGradientCard component
- Create SpendingAnalyticsCard with line chart
- Update QuickActionsRow to v2.0 design
- Add category chips with horizontal scroll

🤖 Generated with Claude Code"
git push
```
```

## v2.0 Design Elements Reference

### Colors
```kotlin
// Primary Gradient
val PrimaryGradient = Brush.linearGradient(
    colors = listOf(Color(0xFF667EEA), Color(0xFF764BA2))
)

// Secondary Gradient (Success)
val SecondaryGradient = Brush.linearGradient(
    colors = listOf(Color(0xFF11998E), Color(0xFF38EF7D))
)

// Semantic Colors
val Success = Color(0xFF00D09C)
val Error = Color(0xFF FF4757)
val Warning = Color(0xFFFFB800)
```

### Typography
```kotlin
// Display - Balance amounts
val Display = TextStyle(
    fontSize = 36.sp,
    fontWeight = FontWeight.ExtraBold
)

// Headline - Section titles
val Headline = TextStyle(
    fontSize = 20.sp,
    fontWeight = FontWeight.Bold
)

// Body - Content
val Body = TextStyle(
    fontSize = 14.sp,
    fontWeight = FontWeight.Normal
)

// Label - Captions, badges
val Label = TextStyle(
    fontSize = 12.sp,
    fontWeight = FontWeight.Medium
)
```

### Spacing
```kotlin
object MifosSpacing {
    val xs = 4.dp
    val sm = 8.dp
    val md = 12.dp
    val lg = 16.dp
    val xl = 20.dp
    val xxl = 24.dp
}
```

### Common Components (from MOCKUP.md patterns)
```kotlin
// Hero Card: 24dp corner radius, gradient bg, 24dp padding
// Account Card: 16dp corner radius, 4dp left accent border
// Quick Action: 56dp container, 16dp corner radius, 24dp icon
// Progress Bar: 6dp height, 3dp corner radius
// Touch targets: 48dp minimum
```

## Output Rules

1. **Read minimal files** - Only MOCKUP.md, STATUS.md, and implementation files
2. **Be specific** - Exact file paths and component names
3. **Include code sketches** - Show actual Compose code, not just descriptions
4. **Reference MOCKUP.md specs** - Colors, sizes, spacing from design
5. **Estimate accurately** - S (<1hr), M (1-4hr), L (>4hr)
6. **Prioritize** - P0 (critical) > P1 (high) > P2 (nice-to-have)
7. **Verify steps** - How to confirm each task is done
8. **Update docs** - Always update STATUS.md and PRODUCT_MAP.md

## Priority Guidelines

| Priority | Criteria | Examples |
|----------|----------|----------|
| P0 | Broken functionality | Crashes, data loss, security |
| P1 | High-value v2.0 features | Hero cards, analytics charts, key UX |
| P2 | Polish and enhancement | Animations, dark mode tweaks, minor UI |

## Effort Guidelines

| Effort | Time | Scope |
|--------|------|-------|
| S | <1 hour | Single component, styling changes |
| M | 1-4 hours | Multiple components, new data flow |
| L | >4 hours | Major feature, architectural changes |

## Anti-Patterns

- Don't read entire codebase - only relevant feature files
- Don't plan improvements for other features
- Don't include long code blocks - sketches only
- Don't forget verification steps
- Don't skip documentation cross-updates
- Don't commit without updating STATUS.md and PRODUCT_MAP.md
- Don't mix P0 fixes with P2 enhancements in same task

## Example: Dashboard (New Feature)

```markdown
## Improvement Plan: Dashboard

**Current**: Client ✅ | Feature ❌ | v2.0 UI ❌
**Target**: Client ✅ | Feature ✅ | v2.0 UI ✅

---

### Current Implementation

| Component | Type | Status | File |
|-----------|------|:------:|------|
| ClientService | Network | ✅ | core/network/services/ClientService.kt |
| HomeRepository | Data | ✅ | core/data/repository/HomeRepository.kt |
| DashboardViewModel | Feature | ❌ | (missing) |
| DashboardScreen | Feature | ❌ | (missing) |

---

### Implementation Tasks

#### Task 1: Create Dashboard Feature Module (P0, Effort: L)

**Goal**: Create new feature module for unified dashboard

**Gradle setup**:
```kotlin
// feature/dashboard/build.gradle.kts
plugins {
    id("org.convention.cmp.feature")
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.designsystem)
    implementation(projects.core.ui)
}
```

**Files to create**:
- `feature/dashboard/src/commonMain/.../DashboardViewModel.kt`
- `feature/dashboard/src/commonMain/.../DashboardScreen.kt`
- `feature/dashboard/src/commonMain/.../di/DashboardModule.kt`
- `feature/dashboard/src/commonMain/.../navigation/DashboardNavigation.kt`

... (continued with code sketches for each)
```

## Workflow

```
/gap-planning              →  List features with gaps
        ↓
/gap-planning [Feature]    →  Detailed improvement plan
        ↓
Implement tasks            →  Code changes
        ↓
Verify & test              →  Run app, check design match
        ↓
Update STATUS.md           →  Mark components complete
        ↓
Update PRODUCT_MAP.md      →  Update feature status
        ↓
Commit with message        →  Done!
```
