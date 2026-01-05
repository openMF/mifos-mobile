# TestTags Index - O(1) Lookup

> **17 features** | **Pattern**: `{feature}:{component}:{id}` | **Last Updated**: 2026-01-05

---

## Quick Lookup

| # | Feature | TestTags Object | Required Tags | Status |
|:-:|---------|-----------------|:-------------:|:------:|
| 1 | auth | `AuthTestTags` | 8 | ❌ Not Created |
| 2 | home | `HomeTestTags` | 5 | ❌ Not Created |
| 3 | accounts | `AccountsTestTags` | 6 | ❌ Not Created |
| 4 | savings-account | `SavingsTestTags` | 7 | ❌ Not Created |
| 5 | loan-account | `LoanTestTags` | 7 | ❌ Not Created |
| 6 | share-account | `ShareTestTags` | 5 | ❌ Not Created |
| 7 | beneficiary | `BeneficiaryTestTags` | 8 | ❌ Not Created |
| 8 | transfer | `TransferTestTags` | 6 | ❌ Not Created |
| 9 | recent-transaction | `TransactionTestTags` | 4 | ❌ Not Created |
| 10 | notification | `NotificationTestTags` | 4 | ❌ Not Created |
| 11 | settings | `SettingsTestTags` | 10 | ❌ Not Created |
| 12 | passcode | `PasscodeTestTags` | 5 | ❌ Not Created |
| 13 | guarantor | `GuarantorTestTags` | 6 | ❌ Not Created |
| 14 | qr | `QrTestTags` | 5 | ❌ Not Created |
| 15 | location | `LocationTestTags` | 3 | ❌ Not Created |
| 16 | client-charge | `ChargeTestTags` | 4 | ❌ Not Created |
| 17 | dashboard | `DashboardTestTags` | 6 | ❌ Not Created |

---

## Naming Convention

### Pattern
```
{feature}:{component}:{identifier}
```

### Standard Components

| Component | Pattern | Example | Required |
|-----------|---------|---------|:--------:|
| Screen container | `{feature}:screen` | `auth:screen` | ✅ |
| Loading indicator | `{feature}:loading` | `auth:loading` | ✅ |
| Error container | `{feature}:error` | `auth:error` | ✅ |
| Empty state | `{feature}:empty` | `beneficiary:empty` | ⚠️ |
| List container | `{feature}:list` | `beneficiary:list` | ⚠️ |
| List item | `{feature}:item:{id}` | `beneficiary:item:123` | ⚠️ |
| Retry button | `{feature}:retry` | `auth:retry` | ⚠️ |
| Submit button | `{feature}:submit` | `auth:submit` | ⚠️ |
| Input field | `{feature}:input:{name}` | `auth:input:username` | ⚠️ |
| FAB | `{feature}:fab` | `beneficiary:fab` | ⚠️ |

**Legend**: ✅ Required | ⚠️ Recommended (if applicable)

---

## Feature TestTags Specifications

### 1. auth

**Object**: `feature/auth/src/commonMain/.../util/AuthTestTags.kt`

```kotlin
object AuthTestTags {
    // Screens
    const val LOGIN_SCREEN = "auth:login:screen"
    const val REGISTER_SCREEN = "auth:register:screen"
    const val OTP_SCREEN = "auth:otp:screen"

    // Common
    const val LOADING = "auth:loading"
    const val ERROR = "auth:error"

    // Login inputs
    const val USERNAME_INPUT = "auth:input:username"
    const val PASSWORD_INPUT = "auth:input:password"
    const val LOGIN_BUTTON = "auth:submit:login"

    // Register inputs
    const val EMAIL_INPUT = "auth:input:email"
    const val FIRST_NAME_INPUT = "auth:input:firstName"
    const val LAST_NAME_INPUT = "auth:input:lastName"
    const val MOBILE_INPUT = "auth:input:mobile"
    const val REGISTER_BUTTON = "auth:submit:register"

    // OTP
    const val OTP_INPUT = "auth:input:otp"
    const val VERIFY_BUTTON = "auth:submit:verify"
    const val RESEND_BUTTON = "auth:resend"
}
```

### 2. home

**Object**: `feature/home/src/commonMain/.../util/HomeTestTags.kt`

```kotlin
object HomeTestTags {
    const val SCREEN = "home:screen"
    const val LOADING = "home:loading"
    const val ERROR = "home:error"

    // Content
    const val USER_NAME = "home:userName"
    const val TOTAL_SAVINGS = "home:totalSavings"
    const val TOTAL_LOAN = "home:totalLoan"
    const val ACCOUNTS_SECTION = "home:accounts"
    const val QUICK_ACTIONS = "home:quickActions"

    // Actions
    const val TRANSFER_ACTION = "home:action:transfer"
    const val QR_ACTION = "home:action:qr"
    const val CHARGES_ACTION = "home:action:charges"
}
```

### 3. accounts

**Object**: `feature/account/src/commonMain/.../util/AccountsTestTags.kt`

```kotlin
object AccountsTestTags {
    const val SCREEN = "accounts:screen"
    const val LOADING = "accounts:loading"
    const val ERROR = "accounts:error"
    const val EMPTY = "accounts:empty"

    // Tabs
    const val SAVINGS_TAB = "accounts:tab:savings"
    const val LOAN_TAB = "accounts:tab:loan"
    const val SHARE_TAB = "accounts:tab:share"

    // Lists
    const val LIST = "accounts:list"
    fun item(accountId: Long) = "accounts:item:$accountId"
}
```

### 4. beneficiary

**Object**: `feature/beneficiary/src/commonMain/.../util/BeneficiaryTestTags.kt`

```kotlin
object BeneficiaryTestTags {
    const val SCREEN = "beneficiary:screen"
    const val LOADING = "beneficiary:loading"
    const val ERROR = "beneficiary:error"
    const val EMPTY = "beneficiary:empty"
    const val LIST = "beneficiary:list"
    const val FAB = "beneficiary:fab"
    const val RETRY = "beneficiary:retry"

    fun item(id: Long) = "beneficiary:item:$id"

    // Detail screen
    const val DETAIL_SCREEN = "beneficiary:detail:screen"
    const val NAME_TEXT = "beneficiary:detail:name"
    const val ACCOUNT_TEXT = "beneficiary:detail:account"
    const val EDIT_BUTTON = "beneficiary:detail:edit"
    const val DELETE_BUTTON = "beneficiary:detail:delete"

    // Add/Edit screen
    const val FORM_SCREEN = "beneficiary:form:screen"
    const val NAME_INPUT = "beneficiary:input:name"
    const val ACCOUNT_INPUT = "beneficiary:input:account"
    const val OFFICE_INPUT = "beneficiary:input:office"
    const val SUBMIT_BUTTON = "beneficiary:submit"
}
```

### 5. transfer

**Object**: `feature/transfer-process/src/commonMain/.../util/TransferTestTags.kt`

```kotlin
object TransferTestTags {
    const val SCREEN = "transfer:screen"
    const val LOADING = "transfer:loading"
    const val ERROR = "transfer:error"

    // Inputs
    const val FROM_ACCOUNT = "transfer:input:fromAccount"
    const val TO_BENEFICIARY = "transfer:input:toBeneficiary"
    const val AMOUNT_INPUT = "transfer:input:amount"
    const val REMARK_INPUT = "transfer:input:remark"
    const val DATE_INPUT = "transfer:input:date"

    // Actions
    const val REVIEW_BUTTON = "transfer:review"
    const val CONFIRM_BUTTON = "transfer:confirm"
    const val CANCEL_BUTTON = "transfer:cancel"

    // Success
    const val SUCCESS_SCREEN = "transfer:success:screen"
    const val DONE_BUTTON = "transfer:done"
}
```

### 6. loan-account

**Object**: `feature/loan-account/src/commonMain/.../util/LoanTestTags.kt`

```kotlin
object LoanTestTags {
    const val SCREEN = "loan:screen"
    const val LOADING = "loan:loading"
    const val ERROR = "loan:error"

    // Summary
    const val LOAN_AMOUNT = "loan:amount"
    const val OUTSTANDING = "loan:outstanding"
    const val STATUS = "loan:status"

    // Tabs
    const val SUMMARY_TAB = "loan:tab:summary"
    const val REPAYMENT_TAB = "loan:tab:repayment"
    const val TRANSACTIONS_TAB = "loan:tab:transactions"

    // Lists
    const val SCHEDULE_LIST = "loan:schedule:list"
    const val TRANSACTION_LIST = "loan:transaction:list"

    fun scheduleItem(index: Int) = "loan:schedule:item:$index"
    fun transactionItem(id: Long) = "loan:transaction:item:$id"
}
```

### 7. savings-account

**Object**: `feature/savings-account/src/commonMain/.../util/SavingsTestTags.kt`

```kotlin
object SavingsTestTags {
    const val SCREEN = "savings:screen"
    const val LOADING = "savings:loading"
    const val ERROR = "savings:error"

    // Summary
    const val BALANCE = "savings:balance"
    const val STATUS = "savings:status"
    const val ACCOUNT_NUMBER = "savings:accountNumber"

    // Tabs
    const val SUMMARY_TAB = "savings:tab:summary"
    const val TRANSACTIONS_TAB = "savings:tab:transactions"

    // Actions
    const val WITHDRAW_BUTTON = "savings:withdraw"
    const val DEPOSIT_BUTTON = "savings:deposit"
    const val TRANSFER_BUTTON = "savings:transfer"

    // Transaction list
    const val TRANSACTION_LIST = "savings:transaction:list"
    fun transactionItem(id: Long) = "savings:transaction:item:$id"
}
```

### 8. settings

**Object**: `feature/settings/src/commonMain/.../util/SettingsTestTags.kt`

```kotlin
object SettingsTestTags {
    const val SCREEN = "settings:screen"
    const val LOADING = "settings:loading"

    // Menu items
    const val CHANGE_PASSCODE = "settings:item:changePasscode"
    const val CHANGE_PASSWORD = "settings:item:changePassword"
    const val LANGUAGE = "settings:item:language"
    const val THEME = "settings:item:theme"
    const val NOTIFICATION = "settings:item:notification"
    const val ABOUT = "settings:item:about"
    const val LOGOUT = "settings:item:logout"

    // Change password screen
    const val CURRENT_PASSWORD = "settings:input:currentPassword"
    const val NEW_PASSWORD = "settings:input:newPassword"
    const val CONFIRM_PASSWORD = "settings:input:confirmPassword"
    const val SUBMIT_PASSWORD = "settings:submit:password"
}
```

### 9. notification

**Object**: `feature/notification/src/commonMain/.../util/NotificationTestTags.kt`

```kotlin
object NotificationTestTags {
    const val SCREEN = "notification:screen"
    const val LOADING = "notification:loading"
    const val ERROR = "notification:error"
    const val EMPTY = "notification:empty"
    const val LIST = "notification:list"

    fun item(id: Long) = "notification:item:$id"
}
```

### 10. qr

**Object**: `feature/qr-code/src/commonMain/.../util/QrTestTags.kt`

```kotlin
object QrTestTags {
    const val SCREEN = "qr:screen"
    const val LOADING = "qr:loading"
    const val ERROR = "qr:error"

    // Display
    const val QR_IMAGE = "qr:image"
    const val SHARE_BUTTON = "qr:share"

    // Scan
    const val SCAN_SCREEN = "qr:scan:screen"
    const val CAMERA_VIEW = "qr:scan:camera"
    const val RESULT_TEXT = "qr:scan:result"
}
```

---

## Validation Rules

### Required Tags (Must Exist)

```kotlin
val requiredTags = listOf(
    "${feature}:screen",   // Main screen container
    "${feature}:loading",  // Loading state
    "${feature}:error"     // Error state
)
```

### Recommended Tags (If Applicable)

```kotlin
val recommendedTags = listOf(
    "${feature}:empty",    // Empty state (for lists)
    "${feature}:list",     // List container
    "${feature}:retry",    // Retry button (for errors)
    "${feature}:fab"       // Floating action button
)
```

### Naming Validation Regex

```kotlin
val testTagPattern = Regex("^[a-z-]+:[a-z-]+(?::[a-z0-9-]+)?$")

// Valid examples:
// "auth:screen" ✅
// "beneficiary:item:123" ✅
// "transfer:input:amount" ✅

// Invalid examples:
// "AuthScreen" ❌ (no colons, PascalCase)
// "auth_screen" ❌ (underscore)
// "auth:LOADING" ❌ (uppercase)
```

---

## Usage in Compose

### Applying TestTags

```kotlin
@Composable
fun BeneficiaryScreen(state: BeneficiaryState) {
    Scaffold(
        modifier = Modifier.testTag(BeneficiaryTestTags.SCREEN)
    ) {
        when (state.uiState) {
            is Loading -> CircularProgressIndicator(
                modifier = Modifier.testTag(BeneficiaryTestTags.LOADING)
            )
            is Error -> ErrorView(
                modifier = Modifier.testTag(BeneficiaryTestTags.ERROR)
            )
            is Success -> BeneficiaryList(
                items = state.uiState.data,
                modifier = Modifier.testTag(BeneficiaryTestTags.LIST)
            )
        }
    }
}

@Composable
fun BeneficiaryItem(
    beneficiary: Beneficiary,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .testTag(BeneficiaryTestTags.item(beneficiary.id))
            .clickable(onClick = onClick)
    ) {
        // Content
    }
}
```

---

## O(1) Path Pattern

```
feature/[feature]/src/commonMain/kotlin/org/mifos/mobile/feature/[feature]/util/[Feature]TestTags.kt
```

---

## Auto-Update Rules

| Trigger | Action |
|---------|--------|
| `/implement [feature]` | Create TestTags object if missing |
| `/verify [feature]` | Validate TestTags exist and follow convention |
| New screen added | Add corresponding tags to object |

---

## Commands

```bash
# Check TestTag status
/gap-analysis testing tags

# Generate TestTags for feature
/implement [feature]           # Creates TestTags in Phase 4

# Validate TestTags
/verify [feature]              # Includes TestTag validation report
```
