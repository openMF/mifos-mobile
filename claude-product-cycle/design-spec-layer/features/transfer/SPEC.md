# Transfer - Feature Specification

> **Purpose**: Fund transfer functionality between accounts (self and third-party)
> **User Value**: Seamless money transfers between own accounts and to beneficiaries
> **Last Updated**: 2025-12-29
> **Status**: Production Design

---

## 1. Overview

### 1.1 Feature Summary
The Transfer feature enables users to move funds between their own accounts (self-transfer) or to third-party beneficiaries (TPT). It consists of three main flows:
1. **Make Transfer Screen**: Initial form to select accounts, enter amount, and remarks
2. **Transfer Process Screen**: Review and confirm transfer details before execution
3. **Third-Party Transfer Screen**: Dedicated flow for transfers to external beneficiaries

### 1.2 User Stories
- As a user, I want to transfer money between my savings accounts
- As a user, I want to repay my loan from my savings account
- As a user, I want to send money to a saved beneficiary
- As a user, I want to review transfer details before confirming
- As a user, I want to add remarks to track my transfers
- As a user, I want to see a confirmation after successful transfer
- As a user, I want to authenticate before executing high-value transfers

### 1.3 Design Principles
- **Security**: Authentication required before transfer execution
- **Clarity**: Clear display of from/to accounts with amounts
- **Validation**: Real-time validation of amount and remarks
- **Confirmation**: Mandatory review step before execution
- **Feedback**: Clear success/failure status with transfer ID

---

## 2. Screen Layout

### 2.1 Make Transfer Screen

```
+---------------------------------------------------------------+
|  <- Make Transfer                                              |
+---------------------------------------------------------------+
|                                                                |
|  PAY TO                                                        |
|  +-----------------------------------------------------------+|
|  |  SA-0001234567          John Doe                    v     ||
|  +-----------------------------------------------------------+|
|                                                                |
|  PAY FROM                                                      |
|  +-----------------------------------------------------------+|
|  |  [Icon] Primary Savings                                   ||
|  |         SA-0009876543                              v      ||
|  |         Available: $35,000.00                             ||
|  +-----------------------------------------------------------+|
|                                                                |
|  AMOUNT                                                        |
|  +-----------------------------------------------------------+|
|  |  $                                                        ||
|  |           500.00                                          ||
|  +-----------------------------------------------------------+|
|  |  [Error: Amount is required]                              ||
|                                                                |
|  REMARKS                                                       |
|  +-----------------------------------------------------------+|
|  |  Payment for services                                     ||
|  +-----------------------------------------------------------+|
|  |  [Error: Remarks cannot be empty]                         ||
|                                                                |
|  +-----------------------------------------------------------+|
|  |                   MAKE TRANSFER                           ||
|  +-----------------------------------------------------------+|
|                                                                |
|  [Powered by Mifos]                                            |
+---------------------------------------------------------------+
```

### 2.2 Transfer Process Screen (Review & Confirm)

```
+---------------------------------------------------------------+
|  <- Transfer                                                   |
+---------------------------------------------------------------+
|                                                                |
|  PAY FROM                                                      |
|  +-----------------------------------------------------------+|
|  |  Account Number        SA-0009876543                      ||
|  |  Customer Name         John Doe                           ||
|  +-----------------------------------------------------------+|
|                                                                |
|  PAY TO                                                        |
|  +-----------------------------------------------------------+|
|  |  Account Number        SA-0001234567                      ||
|  |  Beneficiary Name      Jane Doe                           ||
|  +-----------------------------------------------------------+|
|                                                                |
|  TRANSFER DETAILS                                              |
|  +-----------------------------------------------------------+|
|  |  Amount                $500.00                            ||
|  |  Date                  29 December 2025                   ||
|  |  Remark                Payment for services               ||
|  +-----------------------------------------------------------+|
|                                                                |
|  +-----------------------------------------------------------+|
|  |                      CANCEL                               ||
|  +-----------------------------------------------------------+|
|                                                                |
|  +-----------------------------------------------------------+|
|  |                     TRANSFER                              ||
|  +-----------------------------------------------------------+|
|                                                                |
|  [Powered by Mifos]                                            |
+---------------------------------------------------------------+
```

### 2.3 Third-Party Transfer Screen

```
+---------------------------------------------------------------+
|  [Logo] Home                                          [Bell]   |
+---------------------------------------------------------------+
|                                                                |
|  ORIGIN ACCOUNT                                                |
|  +-----------------------------------------------------------+|
|  |  [Icon] Primary Savings                                   ||
|  |         SA-0009876543                              v      ||
|  |         Available: $35,000.00                             ||
|  +-----------------------------------------------------------+|
|                                                                |
|  DESTINATION                                                   |
|  +-----------------------------------------------------------+|
|  |  SA-0007654321          Jane Doe                    v     ||
|  +-----------------------------------------------------------+|
|                                                                |
|           Don't have a beneficiary? Add one here              |
|                                                                |
|  AMOUNT                                                        |
|  +-----------------------------------------------------------+|
|  |  $                                                        ||
|  |           1000.00                                         ||
|  +-----------------------------------------------------------+|
|                                                                |
|  REMARKS                                                       |
|  +-----------------------------------------------------------+|
|  |  Monthly support                                          ||
|  +-----------------------------------------------------------+|
|                                                                |
|  +-----------------------------------------------------------+|
|  |                   TRANSFER                                ||
|  +-----------------------------------------------------------+|
|                                                                |
+---------------------------------------------------------------+
```

---

## 3. Sections Table

| Section | Description | Components |
|---------|-------------|------------|
| Top Bar | Navigation and title | Back button, Screen title |
| Pay To Dropdown | Destination account selection | Account number, Client name, Dropdown |
| Pay From Dropdown | Source account selection | Account icon, Account name, Number, Balance |
| Amount Field | Transfer amount input | Currency symbol, Numeric input, Error text |
| Remarks Field | Transfer description | Text input, Error text |
| Action Button | Submit transfer | Primary button |
| Powered Card | Branding footer | Mifos logo |

---

## 4. User Interactions Table

| Interaction | Action | Outcome |
|-------------|--------|---------|
| Select Pay To Account | OnToAccountSelected | Updates toAccount, filters fromAccountOptions |
| Select Pay From Account | OnFromAccountSelected | Updates fromAccount, filters toAccountOptions |
| Enter Amount | OnAmountChanged | Validates amount with 300ms debounce |
| Enter Remarks | OnRemarksChanged | Validates remarks with 300ms debounce |
| Tap Make Transfer | OnMakeTransferClicked | Validates all fields, navigates to review |
| Tap Cancel (Review) | OnNavigate | Returns to previous screen |
| Tap Transfer (Review) | RequestTransfer | Triggers authentication flow |
| Authentication Success | Internal.MakeTransfer | Executes transfer API call |
| Tap Retry | OnRetry | Refetches account options template |
| Tap Add Beneficiary | OnAddBeneficiaryClicked | Navigates to beneficiary creation |
| Tap Notification | OnNotificationClicked | Navigates to notifications |

---

## 5. State Model

### 5.1 MakeTransferState (from MakeTransferViewModel.kt)

```kotlin
internal data class MakeTransferState(
    val accountId: Long = -1L,
    val clientId: Long = -1L,
    val outstandingBalance: Double? = null,
    val transferType: String? = null,
    val transferTarget: TransferType? = null,
    val transferSuccessDestination: String = "",
    val amount: String = "",
    val amountError: StringResource? = null,
    val remark: String = "",
    val remarkError: StringResource? = null,
    var accountOptionsTemplate: AccountOptionsTemplate = AccountOptionsTemplate(),
    var fromAccountOptions: List<AccountOption> = emptyList(),
    var toAccountOptions: List<AccountOption> = emptyList(),
    val fromAccount: AccountOption? = null,
    val toAccount: AccountOption? = null,
    val dialogState: DialogState? = null,
    val networkStatus: Boolean = false,
    val uiState: MakeTransferScreenState? = null,
) {
    sealed interface DialogState {
        data class Error(val message: String) : DialogState
    }

    sealed interface MakeTransferScreenState {
        data object Loading : MakeTransferScreenState
        data class Error(val message: StringResource) : MakeTransferScreenState
        data object Success : MakeTransferScreenState
        data object Network : MakeTransferScreenState
        data object OverlayLoading : MakeTransferScreenState
    }

    val isEnabled = networkStatus &&
        fromAccount != null &&
        toAccount != null &&
        amount.isNotBlank() &&
        remark.isNotBlank() &&
        amountError == null &&
        remarkError == null
}
```

### 5.2 MakeTransferAction

```kotlin
internal sealed interface MakeTransferAction {
    data class OnToAccountSelected(val accountNo: String) : MakeTransferAction
    data class OnFromAccountSelected(val accountNo: String) : MakeTransferAction
    data class OnAmountChanged(val amount: String) : MakeTransferAction
    data class OnRemarksChanged(val remark: String) : MakeTransferAction
    data object OnMakeTransferClicked : MakeTransferAction
    data object DismissDialog : MakeTransferAction
    data object NavigateBack : MakeTransferAction
    data object OnRetry : MakeTransferAction

    sealed interface Internal : MakeTransferAction {
        data object PerformTransfer : Internal
        data class ReceiveAccountOptionsTemplateResult(val dataState: DataState<AccountOptionsTemplate>) : Internal
        data class ReceiveActiveAccountsResult(val dataState: DataState<ClientAccounts>) : Internal
        data class ReceiveNetworkStatus(val isOnline: Boolean) : Internal
    }
}
```

### 5.3 MakeTransferEvent

```kotlin
internal sealed interface MakeTransferEvent {
    data object NavigateBack : MakeTransferEvent
    data class NavigateToTransferScreen(
        val reviewTransferPayload: ReviewTransferPayload,
        val transferType: TransferType,
        val destination: String,
    ) : MakeTransferEvent
}
```

### 5.4 TransferProcessState (from TransferProcessViewModel.kt)

```kotlin
data class TransferProcessState(
    val transferDestination: String? = null,
    val transferType: TransferType? = null,
    val transferPayload: TransferPayload? = null,
    val networkStatus: Boolean = false,
    val uiState: ScreenUiState? = ScreenUiState.Success,
    val showOverlay: Boolean = false,
    val fromClientName: String? = null,
    val toClientName: String? = null,
)
```

### 5.5 TransferProcessAction

```kotlin
sealed interface TransferProcessAction {
    data object RequestTransfer : TransferProcessAction
    data object OnNavigate : TransferProcessAction
    data class ReceiveNetworkStatus(val isOnline: Boolean) : TransferProcessAction

    sealed interface Internal : TransferProcessAction {
        data class ReceiveAuthenticationResult(val result: Boolean) : Internal
        data object MakeTransfer : Internal
    }
}
```

### 5.6 TransferProcessEvent

```kotlin
sealed interface TransferProcessEvent {
    data object Navigate : TransferProcessEvent
    data class NavigateToAuthenticate(val status: String = EventType.SUCCESS.name) : TransferProcessEvent
    data class NavigateToStatus(
        val eventType: String,
        val eventDestination: String,
        val title: String,
        val subtitle: String,
        val buttonText: String,
    ) : TransferProcessEvent
}
```

### 5.7 TptState (Third-Party Transfer from TptViewModel.kt)

```kotlin
internal data class TptState(
    val accountId: Long = -1L,
    val clientId: Long = -1L,
    val outstandingBalance: Double? = null,
    val amount: String = "",
    val amountError: StringResource? = null,
    val remark: String = "",
    val remarkError: StringResource? = null,
    var accountOptionsTemplate: AccountOptionsTemplate = AccountOptionsTemplate(),
    var fromAccountOptions: List<AccountOption> = emptyList(),
    var toAccountOptions: List<AccountOption> = emptyList(),
    val fromAccount: AccountOption? = null,
    val toAccount: AccountOption? = null,
    val dialogState: DialogState? = null,
    val networkStatus: Boolean = false,
    val uiState: ScreenUiState? = ScreenUiState.Loading,
) {
    sealed interface DialogState {
        data class Error(val message: StringResource) : DialogState
    }

    val isEnabled: Boolean = fromAccount != null &&
        toAccount != null &&
        amount.isNotBlank() &&
        remark.isNotBlank() &&
        amountError == null &&
        remarkError == null
}
```

---

## 6. API Requirements

| Endpoint | Method | Purpose | Priority |
|----------|--------|---------|----------|
| /self/accounttransfers/template | GET | Self-transfer account options | P0 |
| /self/accounttransfers/template?type=tpt | GET | Third-party transfer template | P0 |
| /self/accounttransfers | POST | Execute self-transfer | P0 |
| /self/accounttransfers?type=tpt | POST | Execute third-party transfer | P0 |
| /self/clients/{id}/accounts | GET | Fetch active accounts (fallback) | P1 |

---

## 7. Validation Rules

### 7.1 Amount Validation
```kotlin
private fun validateAmount(amount: String) = when {
    amount.isBlank() -> ValidationResult.Error(Res.string.feature_make_transfer_error_amount_required)
    !Regex("^\\d+(\\.\\d+)?$").matches(amount) ->
        ValidationResult.Error(Res.string.feature_make_transfer_error_amount_invalid)
    else -> ValidationResult.Success
}
```

### 7.2 Remarks Validation
```kotlin
private fun validateRemark(remark: String): ValidationResult = when {
    remark.isEmpty() -> ValidationResult.Error(Res.string.feature_make_transfer_error_remarks_empty)
    !ValidationHelper.isValidName(remark) ->
        ValidationResult.Error(Res.string.feature_make_transfer_error_remarks_invalid)
    else -> ValidationResult.Success
}
```

### 7.3 Validation Debouncing
- 300ms delay between keystrokes before validation triggers
- Prevents excessive UI updates during rapid typing

---

## 8. Edge Cases & Error Handling

| Scenario | UI Behavior | Recovery |
|----------|-------------|----------|
| No network | Show network error state | Auto-retry on reconnect |
| API timeout | Show error state | Pull-to-refresh / Retry button |
| Invalid amount format | Show inline error | Clear error on valid input |
| Empty remarks | Show inline error | Clear error on valid input |
| Same from/to account | Filter out from options | Auto-filtering prevents selection |
| Insufficient balance | API returns error | Show error dialog |
| Transfer failed | Navigate to status screen | Show failure with retry option |
| Authentication failed | Stay on review screen | User can retry authentication |
| Account ID not found | Fetch active accounts | Auto-fallback to active loan |

---

## 9. Transfer Types

| Type | Enum Value | Description | Endpoint |
|------|------------|-------------|----------|
| Self Transfer | SELF | Between own accounts | /accounttransfers |
| Third Party | TPT | To beneficiary accounts | /accounttransfers?type=tpt |

---

## 10. Navigation Flow

```
                    +------------------+
                    |   Entry Point    |
                    | (Dashboard/Home) |
                    +--------+---------+
                             |
            +----------------+----------------+
            |                                 |
            v                                 v
+-------------------+              +--------------------+
|  Make Transfer    |              |  Third-Party       |
|     Screen        |              |  Transfer Screen   |
+--------+----------+              +---------+----------+
         |                                   |
         v                                   v
+-------------------+              +--------------------+
| Transfer Process  |<-------------| Transfer Process   |
|  (Review) Screen  |              |   (Review) Screen  |
+--------+----------+              +--------------------+
         |
         v
+-------------------+
|  Authentication   |
|     Screen        |
+--------+----------+
         |
    +----+----+
    |         |
    v         v
+-------+  +-------+
|Success|  |Failure|
|Status |  |Status |
+-------+  +-------+
```

---

## 11. Route Parameters

### 11.1 MakeTransferRoute
```kotlin
@Serializable
data class MakeTransferRoute(
    val accountId: Long = -1L,
    val accountNo: String? = null,
    val amount: String? = null,
    val outstandingBalance: String? = null,
    val transferType: String? = null,
    val transferTarget: String? = null,
    val transferSuccessDestination: String? = null,
)
```

### 11.2 TransferProcessRoute
```kotlin
@Serializable
data class TransferProcessRoute(
    val fromOfficeId: Int? = null,
    val fromClientId: Long? = null,
    val fromAccountType: Int? = null,
    val fromAccountId: String? = null,
    val fromClientName: String? = null,
    val toOfficeId: Int? = null,
    val toClientId: Long? = null,
    val toAccountType: Int? = null,
    val toAccountId: String? = null,
    val toClientName: String? = null,
    val transferAmount: String? = null,
    val transferDescription: String? = null,
    val transferType: String = TransferType.SELF.name,
    val transferSuccessDestination: String = "",
)
```

---

## 12. Success/Failure Destinations

| Destination | Description | Navigation After |
|-------------|-------------|------------------|
| HOME | Dashboard home | Pop to home |
| SAVINGS_ACCOUNT | Savings detail | Pop to savings screen |
| LOAN_ACCOUNT | Loan detail | Pop to loan screen |
| PREVIOUS_SCREEN | Back navigation | Pop back stack |

---

## 13. Accessibility

| Feature | Implementation |
|---------|----------------|
| Screen Reader | contentDescription on all form elements |
| Focus Order | Logical tab order: To -> From -> Amount -> Remarks -> Button |
| Error Announcements | Live regions for validation errors |
| Touch Targets | 48dp minimum for all interactive elements |
| Font Scaling | Supports 200% scaling |

---

## 14. Security Considerations

| Feature | Implementation |
|---------|----------------|
| Authentication | Passcode required before transfer execution |
| Amount Masking | Optional masking in review screen |
| Session Timeout | Auto-logout after inactivity |
| Network Security | HTTPS only, certificate pinning |
| Input Validation | Server-side validation for all fields |

---

## Changelog

| Date | Change |
|------|--------|
| 2025-12-29 | Initial specification based on implementation code |
