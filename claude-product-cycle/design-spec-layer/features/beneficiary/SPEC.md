# Beneficiary - Feature Specification

> **Purpose**: Manage third-party transfer beneficiaries for quick fund transfers
> **User Value**: Save and manage trusted recipients for faster, secure money transfers
> **Last Updated**: 2025-12-29
> **Status**: Production Design

---

## 1. Overview

### 1.1 Feature Summary
The Beneficiary feature enables users to manage a list of trusted recipients for third-party transfers (TPT). Users can view all saved beneficiaries, add new ones manually or via QR code, view beneficiary details, update beneficiary information, and delete beneficiaries. This feature streamlines the fund transfer process by eliminating the need to repeatedly enter recipient details.

### 1.2 User Stories
- As a user, I want to view all my saved beneficiaries in a list
- As a user, I want to filter beneficiaries by office or account type
- As a user, I want to add a new beneficiary manually with their account details
- As a user, I want to add a beneficiary by scanning a QR code
- As a user, I want to view detailed information about a beneficiary
- As a user, I want to update a beneficiary's name and transfer limit
- As a user, I want to delete a beneficiary I no longer need
- As a user, I want to confirm my actions before creating/updating beneficiaries

### 1.3 Design Principles
- **Security**: Authentication required before creating/updating beneficiaries
- **Simplicity**: Minimal steps to add or manage beneficiaries
- **Clarity**: Clear display of beneficiary information
- **Confirmation**: Multi-step process with review before submission

---

## 2. Screen Layout

### 2.1 Beneficiary List Screen

```
+-------------------------------------------------------------+
|  <- Manage Beneficiaries                                     |
+-------------------------------------------------------------+
|                                                              |
|                               [ Add + ]  [ Filter  ]         |
|                                                              |
|  +-------------------------------------------------------+  |
|  |  +---+                                                 |  |
|  |  |   |  John Doe                                       |  |
|  |  | JD|  SA-0001234567                                  |  |
|  |  |   |  Head Office  |  Savings Account                |  |
|  |  +---+                                                 |  |
|  +-------------------------------------------------------+  |
|                                                              |
|  +-------------------------------------------------------+  |
|  |  +---+                                                 |  |
|  |  |   |  Jane Smith                                     |  |
|  |  | JS|  LA-0009876543                                  |  |
|  |  |   |  Branch Office  |  Loan Account                 |  |
|  |  +---+                                                 |  |
|  +-------------------------------------------------------+  |
|                                                              |
|  +-------------------------------------------------------+  |
|  |  +---+                                                 |  |
|  |  |   |  Mike Johnson                                   |  |
|  |  | MJ|  SA-0005678901                                  |  |
|  |  |   |  Head Office  |  Savings Account                |  |
|  |  +---+                                                 |  |
|  +-------------------------------------------------------+  |
|                                                              |
|  [Powered by Mifos]                                          |
+-------------------------------------------------------------+
```

### 2.2 Beneficiary List - Filter Dialog

```
+-------------------------------------------------------------+
|  <- Manage Beneficiaries                                     |
+-------------------------------------------------------------+
|                                                              |
|  [ Reset ]                            [ Apply ]  [ X ]       |
|                                                              |
|  ---------------------------------------------------------  |
|                                                              |
|  Linked With                                   2 selected v  |
|  +-------------------------------------------------------+  |
|  |  [ ] Head Office                                       |  |
|  |  [x] Branch Office                                     |  |
|  |  [x] Regional Office                                   |  |
|  +-------------------------------------------------------+  |
|                                                              |
|  Type of Account                               1 selected v  |
|  +-------------------------------------------------------+  |
|  |  [x] Savings Account                                   |  |
|  |  [ ] Loan Account                                      |  |
|  |  [ ] Share Account                                     |  |
|  +-------------------------------------------------------+  |
|                                                              |
|  [Powered by Mifos]                                          |
+-------------------------------------------------------------+
```

### 2.3 Beneficiary List - Empty State

```
+-------------------------------------------------------------+
|  <- Manage Beneficiaries                                     |
+-------------------------------------------------------------+
|                                                              |
|                                                              |
|                       +--------+                             |
|                       |   !    |                             |
|                       +--------+                             |
|                                                              |
|           No beneficiaries found, please add                 |
|                                                              |
|           +----------------------------------+               |
|           |        Add Beneficiary           |               |
|           +----------------------------------+               |
|                                                              |
|  [Powered by Mifos]                                          |
+-------------------------------------------------------------+
```

### 2.4 Add/Update Beneficiary Screen

```
+-------------------------------------------------------------+
|  <- Add Beneficiary                                          |
+-------------------------------------------------------------+
|                                                              |
|  +-------------------------------------------------------+  |
|  |  Beneficiary Name *                                    |  |
|  |  [                                                   ] |  |
|  +-------------------------------------------------------+  |
|                                                              |
|  +-------------------------------------------------------+  |
|  |  Account Number *                                      |  |
|  |  [                                                   ] |  |
|  +-------------------------------------------------------+  |
|                                                              |
|  +-------------------------------------------------------+  |
|  |  Select Account Type *                             v   |  |
|  |  [                                                   ] |  |
|  +-------------------------------------------------------+  |
|                                                              |
|  +-------------------------------------------------------+  |
|  |  Office Name *                                         |  |
|  |  [                                                   ] |  |
|  +-------------------------------------------------------+  |
|                                                              |
|  +-------------------------------------------------------+  |
|  |  Transfer Limit *                                      |  |
|  |  [                                                   ] |  |
|  +-------------------------------------------------------+  |
|                                                              |
|  +-------------------------------------------------------+  |
|  |              Submit Beneficiary                        |  |
|  +-------------------------------------------------------+  |
|                                                              |
|  Skip the form? Upload or scan QR code                       |
|                                                              |
+-------------------------------------------------------------+
```

### 2.5 Beneficiary Confirmation Screen

```
+-------------------------------------------------------------+
|  <- Add Beneficiary                                          |
+-------------------------------------------------------------+
|                                                              |
|  Validate Details                                            |
|                                                              |
|  +-------------------------------------------------------+  |
|  |                                                        |  |
|  |  Beneficiary Name            John Doe                  |  |
|  |  ----------------------------------------------------- |  |
|  |  Office                      Head Office               |  |
|  |  ----------------------------------------------------- |  |
|  |  Account Type                Savings Account           |  |
|  |  ----------------------------------------------------- |  |
|  |  Account Number              SA-0001234567             |  |
|  |  ----------------------------------------------------- |  |
|  |  Transfer Limit              10000                     |  |
|  |                                                        |  |
|  +-------------------------------------------------------+  |
|                                                              |
|  +-------------------------------------------------------+  |
|  |              Confirm Details                           |  |
|  +-------------------------------------------------------+  |
|                                                              |
|  [Powered by Mifos]                                          |
+-------------------------------------------------------------+
```

### 2.6 Beneficiary Detail Screen

```
+-------------------------------------------------------------+
|  <- Beneficiary Detail                                       |
+-------------------------------------------------------------+
|                                                              |
|                               [ Delete  ]  [ Update  ]       |
|                                                              |
|  +-------------------------------------------------------+  |
|  |                     +-------+                          |  |
|  |                     |       |                          |  |
|  |                     |  JD   |                          |  |
|  |                     |       |                          |  |
|  |                     +-------+                          |  |
|  |                     John Doe                           |  |
|  |                   SA-0001234567                        |  |
|  +-------------------------------------------------------+  |
|                                                              |
|  +-------------------------------------------------------+  |
|  |  Beneficiary Name                                      |  |
|  |  [  John Doe                                        ]  |  |
|  +-------------------------------------------------------+  |
|                                                              |
|  +-------------------------------------------------------+  |
|  |  Account Number                                        |  |
|  |  [  SA-0001234567                                   ]  |  |
|  +-------------------------------------------------------+  |
|                                                              |
|  +-------------------------------------------------------+  |
|  |  Select Account Type                                   |  |
|  |  [  Savings Account                                 ]  |  |
|  +-------------------------------------------------------+  |
|                                                              |
|  +-------------------------------------------------------+  |
|  |  Office Name                                           |  |
|  |  [  Head Office                                     ]  |  |
|  +-------------------------------------------------------+  |
|                                                              |
|  +-------------------------------------------------------+  |
|  |  Transfer Limit                                        |  |
|  |  [  10000                                           ]  |  |
|  +-------------------------------------------------------+  |
|                                                              |
+-------------------------------------------------------------+
```

### 2.7 Delete Confirmation Dialog

```
+-------------------------------------------------------------+
|                                                              |
|  +-------------------------------------------------------+  |
|  |                                                        |  |
|  |                    [Delete Icon]                       |  |
|  |                                                        |  |
|  |                  Delete Beneficiary                    |  |
|  |                                                        |  |
|  |    Are you sure you want to delete this beneficiary?   |  |
|  |                                                        |  |
|  |   [ Cancel ]                         [ Delete ]        |  |
|  |                                                        |  |
|  +-------------------------------------------------------+  |
|                                                              |
+-------------------------------------------------------------+
```

---

## 3. Sections Table

| Section | Component | Description | Priority |
|---------|-----------|-------------|----------|
| List Header | TopAppBar | Title "Manage Beneficiaries" with back navigation | P0 |
| Action Bar | Row | Add and Filter action buttons | P0 |
| Beneficiary Card | MifosBeneficiariesCard | Card with name, account, office, type | P0 |
| Filter Dialog | FilterSection | Office and Account Type filters with checkboxes | P1 |
| Empty State | EmptyDataView | Icon with message and Add button | P0 |
| Form Fields | MifosOutlinedTextField | Name, Account Number, Office, Transfer Limit | P0 |
| Account Type Dropdown | MifosDropDownTextField | Select account type (Savings/Loan/Share) | P0 |
| Submit Button | MifosButton | Submit Beneficiary action | P0 |
| QR Link | Text with ClickModifier | "Skip the form? Upload or scan QR code" | P1 |
| Details Card | MifosDetailsCard | Read-only beneficiary information | P0 |
| Confirm Button | MifosButton | Confirm Details action | P0 |
| Detail Top Card | MifosBeneficiaryTopCard | Avatar, name, account number | P0 |
| Delete Dialog | MifosAlertDialog | Delete confirmation with Cancel/Delete buttons | P0 |
| Powered Footer | MifosPoweredCard | "Powered by Mifos" branding | P2 |

---

## 4. User Interactions Table

| Interaction | Action | Effect | Navigation |
|-------------|--------|--------|------------|
| Tap Back Arrow | OnNavigate | Navigate to previous screen | popBackStack() |
| Tap Beneficiary Card | OnBeneficiaryItemClick | View beneficiary details | BeneficiaryDetailScreen |
| Tap Add Button | OnAddBeneficiaryClicked | Navigate to add form | BeneficiaryApplicationScreen |
| Tap Filter Button | ToggleFilter | Show filter dialog | FilterDialog |
| Apply Filter | GetFilterResults | Filter list by selected criteria | Dismiss dialog, update list |
| Reset Filter | ResetFilters | Clear all filters | Reset to full list |
| Submit Form | SubmitBeneficiary | Validate and navigate to confirmation | ConfirmationScreen |
| Confirm Details | SubmitBeneficiary | Navigate to authentication | AuthenticateScreen |
| Tap Update | OnUpdateBeneficiary | Navigate to edit form | BeneficiaryApplicationScreen (UPDATE) |
| Tap Delete | ShowDeleteConfirmation | Show delete dialog | Delete confirmation dialog |
| Confirm Delete | DeleteBeneficiary | Delete and navigate back | BeneficiaryListScreen |
| Tap QR Link | NavigateToQR | Navigate to QR scanner | QR Screen |
| Pull to Refresh | RefreshBeneficiaries | Reload beneficiary list | Refresh current screen |

---

## 5. State Model

### 5.1 BeneficiaryListState

```kotlin
data class BeneficiaryListState(
    val networkStatus: Boolean = false,
    val isRefreshing: Boolean = false,
    val beneficiaries: List<Beneficiary> = emptyList(),
    val template: BeneficiaryTemplate? = null,
    val selectedAccounts: Set<String> = emptySet(),
    val selectedOffices: Set<String> = emptySet(),
    val offices: List<String?> = emptyList(),
    val isEmpty: Boolean = false,
    val isFilteredEmpty: Boolean = false,
    val filteredBeneficiaries: List<Beneficiary> = emptyList(),
    val dialogState: DialogState? = null,
    val uiState: ScreenUiState = ScreenUiState.Loading,
) {
    sealed interface DialogState {
        data object Filters : DialogState
    }
    val isAnyFilterSelected = selectedAccounts.isNotEmpty() || selectedOffices.isNotEmpty()
}

sealed interface BeneficiaryListAction {
    data object RefreshBeneficiaries : BeneficiaryListAction
    data object OnAddBeneficiaryClicked : BeneficiaryListAction
    data class OnBeneficiaryItemClick(val position: Long) : BeneficiaryListAction
    data object OnNavigate : BeneficiaryListAction
    data object ToggleFilter : BeneficiaryListAction
    data object ResetFilters : BeneficiaryListAction
    data object GetFilterResults : BeneficiaryListAction
    data object DismissDialog : BeneficiaryListAction
    data class OnAccountChange(val account: String) : BeneficiaryListAction
    data class OnOfficeChange(val office: String) : BeneficiaryListAction
    data object LoadBeneficiaries : BeneficiaryListAction
    data class ReceiveNetworkStatus(val isOnline: Boolean) : BeneficiaryListAction

    sealed interface Internal : BeneficiaryListAction {
        data class ReceiveBeneficiaryResult(
            val beneficiaryList: DataState<List<Beneficiary>>,
        ) : Internal
    }
}

sealed interface BeneficiaryListEvent {
    data object AddBeneficiaryClicked : BeneficiaryListEvent
    data class BeneficiaryItemClick(val position: Long) : BeneficiaryListEvent
    data object Navigate : BeneficiaryListEvent
}
```

### 5.2 BeneficiaryApplicationState

```kotlin
data class BeneficiaryApplicationState(
    val topBarTitle: StringResource = Res.string.add_beneficiary,
    val beneficiaryId: Long = -1L,
    val networkUnavailable: Boolean = false,
    val template: BeneficiaryTemplate? = null,
    val beneficiary: Beneficiary? = null,
    val beneficiaryState: BeneficiaryState = BeneficiaryState.CREATE_MANUAL,
    val dialogState: DialogState? = null,

    val accountTypeError: StringResource? = null,
    val accountNumberError: StringResource? = null,
    val officeNameError: StringResource? = null,
    val transferLimitError: StringResource? = null,
    val beneficiaryNameError: StringResource? = null,

    val accountType: Int = -1,
    val accountNumber: String = "",
    val officeName: String = "",
    val transferLimit: String = "",
    val beneficiaryName: String = "",

    val networkStatus: Boolean = false,
    val uiState: ScreenUiState = ScreenUiState.Loading,
    val shoeOverlay: Boolean = false,
) {
    sealed interface DialogState {
        data class Error(val message: String) : DialogState
    }
    val isEnabled = accountType != -1 &&
        accountNumber.isNotEmpty() &&
        officeName.isNotEmpty() &&
        transferLimit.isNotEmpty() &&
        beneficiaryName.isNotEmpty()
}

sealed interface BeneficiaryApplicationAction {
    data object LoadBeneficiaryTemplate : BeneficiaryApplicationAction
    data object SubmitBeneficiary : BeneficiaryApplicationAction
    data object OnNavigate : BeneficiaryApplicationAction
    data object OnRetry : BeneficiaryApplicationAction
    data object NavigateToQR : BeneficiaryApplicationAction
    data class OnAccountTypeChanged(val accountType: Int) : BeneficiaryApplicationAction
    data class OnAccountNumberChanged(val accountNumber: String) : BeneficiaryApplicationAction
    data class OnOfficeNameChanged(val officeName: String) : BeneficiaryApplicationAction
    data class OnTransferLimitChanged(val transferLimit: String) : BeneficiaryApplicationAction
    data class OnBeneficiaryNameChanged(val beneficiaryName: String) : BeneficiaryApplicationAction
    data class ReceiveNetworkStatus(val isOnline: Boolean) : BeneficiaryApplicationAction

    sealed interface Internal : BeneficiaryApplicationAction {
        data class ReceiveBeneficiaryResult(
            val beneficiaryList: DataState<List<Beneficiary>>,
            val beneficiaryTemplate: DataState<BeneficiaryTemplate>,
        ) : Internal
    }
}

sealed interface BeneficiaryApplicationEvent {
    data object Navigate : BeneficiaryApplicationEvent
    data class SubmitBeneficiary(
        val beneficiaryId: Long,
        val beneficiaryState: String,
        val name: String,
        val officeName: String,
        val accountType: Int,
        val accountNumber: String,
        val transferLimit: Int,
    ) : BeneficiaryApplicationEvent
    data object NavigateToQR : BeneficiaryApplicationEvent
}
```

### 5.3 BeneficiaryApplicationConfirmationState

```kotlin
data class BeneficiaryApplicationConfirmationState(
    val details: Map<StringResource, String> = emptyMap(),
    val topBarTitle: StringResource = Res.string.add_beneficiary,
    val beneficiaryId: Long,
    val name: String,
    val officeName: String,
    val accountType: Int,
    val accountNumber: String,
    val transferLimit: Int,
    val networkUnavailable: Boolean = false,
    val beneficiaryState: BeneficiaryState = BeneficiaryState.CREATE_MANUAL,

    val dialogState: DialogState? = null,
    val uiState: ScreenUiState? = ScreenUiState.Success,
    val showOverlay: Boolean = false,
    val networkStatus: Boolean = false,
) {
    sealed interface DialogState {
        data object Network : DialogState
    }
}

sealed interface BeneficiaryApplicationConfirmationAction {
    data object SubmitBeneficiary : BeneficiaryApplicationConfirmationAction
    data class ReceiveNetworkStatus(val isOnline: Boolean) : BeneficiaryApplicationConfirmationAction
    data object OnNavigate : BeneficiaryApplicationConfirmationAction

    sealed interface Internal : BeneficiaryApplicationConfirmationAction {
        data class ReceiveAuthenticationResult(val result: Boolean) : Internal
        data class ReceiveSubmitBeneficiary(val result: DataState<String>) : Internal
        data class ReceiveUpdateBeneficiary(val result: DataState<String>) : Internal
    }
}

sealed interface BeneficiaryApplicationConfirmationEvent {
    data object Navigate : BeneficiaryApplicationConfirmationEvent
    data class NavigateToStatus(
        val eventType: String,
        val eventDestination: String,
        val title: String,
        val subtitle: String,
        val buttonText: String,
    ) : BeneficiaryApplicationConfirmationEvent
    data class NavigateToAuthenticate(
        val status: String = EventType.SUCCESS.name,
    ) : BeneficiaryApplicationConfirmationEvent
}
```

### 5.4 BeneficiaryDetailState

```kotlin
data class BeneficiaryDetailState(
    val beneficiaryId: Long = -1L,
    val beneficiary: Beneficiary? = null,
    val beneficiaryDialog: DialogState? = null,

    val networkStatus: Boolean = false,
    val uiState: ScreenUiState = ScreenUiState.Loading,
    val showOverlay: Boolean = false,
) {
    sealed interface DialogState {
        data class Error(val message: String) : DialogState
        data class Confirmation(val message: String) : DialogState
    }
}

sealed interface BeneficiaryDetailAction {
    data object OnRefresh : BeneficiaryDetailAction
    data object OnUpdateBeneficiary : BeneficiaryDetailAction
    data object DeleteBeneficiary : BeneficiaryDetailAction
    data object OnNavigate : BeneficiaryDetailAction
    data object ErrorDialogDismiss : BeneficiaryDetailAction
    data object ShowDeleteConfirmation : BeneficiaryDetailAction
    data class ReceiveNetworkStatus(val isOnline: Boolean) : BeneficiaryDetailAction

    sealed interface Internal : BeneficiaryDetailAction {
        data class ReceiveBeneficiaryResult(val result: DataState<List<Beneficiary>>) : Internal
        data class ReceiveDeleteBeneficiary(val result: DataState<String>) : Internal
    }
}

sealed interface BeneficiaryDetailEvent {
    data object NavigateBack : BeneficiaryDetailEvent
    data class UpdateBeneficiary(val beneficiaryId: Long) : BeneficiaryDetailEvent
}
```

### 5.5 BeneficiaryState Enum

```kotlin
enum class BeneficiaryState {
    CREATE_MANUAL,
    CREATE_QR,
    UPDATE,
}
```

---

## 6. API Requirements

| Endpoint | Method | Purpose | Priority |
|----------|--------|---------|----------|
| /self/beneficiaries/tpt | GET | Fetch list of all beneficiaries | P0 |
| /self/beneficiaries/tpt/template | GET | Fetch account type options | P0 |
| /self/beneficiaries/tpt | POST | Create new beneficiary | P0 |
| /self/beneficiaries/tpt/{id} | PUT | Update beneficiary name/limit | P0 |
| /self/beneficiaries/tpt/{id} | DELETE | Delete a beneficiary | P0 |

---

## 7. Edge Cases & Error Handling

| Scenario | UI Behavior | Recovery |
|----------|-------------|----------|
| No internet | Show network error with retry | Auto-retry on reconnect |
| API timeout | Show error state | Pull-to-refresh or Retry button |
| Empty list | Show empty state with Add CTA | Tap "Add Beneficiary" |
| No filter results | Show "No filtered beneficiary found" | Reset filters |
| Invalid form input | Show field-level error messages | Fix errors and resubmit |
| Create failed | Navigate to status screen with failure | "Try Again" button |
| Update failed | Navigate to status screen with failure | "Try Again" button |
| Delete failed | Show error dialog | Dismiss and retry |
| Authentication failed | Stay on confirmation screen | Re-authenticate |
| Duplicate beneficiary | API error shown | Edit details |

---

## 8. Form Validation Rules

| Field | Validation | Error Message |
|-------|------------|---------------|
| Beneficiary Name | Required, non-empty | "Enter beneficiary name" |
| Account Number | Required (except UPDATE), non-empty | "Enter account number" |
| Account Type | Required (except UPDATE), must be selected | "Select account type" |
| Office Name | Required (except UPDATE), non-empty | "Enter office name" |
| Transfer Limit | Required, must be valid number | "Enter transfer limit" / "Invalid amount" |

---

## 9. Navigation Flow

```
BeneficiaryNavRoute (Root)
    |
    +-- BeneficiaryListScreen (Start Destination)
    |       |
    |       +-- [Add] --> BeneficiaryApplicationScreen (CREATE_MANUAL)
    |       |                   |
    |       |                   +-- [Submit] --> BeneficiaryConfirmationScreen
    |       |                   |                   |
    |       |                   |                   +-- [Confirm] --> AuthenticateScreen
    |       |                   |                   |                   |
    |       |                   |                   |                   +-- [Success] --> StatusScreen
    |       |                   |                   |                   |
    |       |                   |                   |                   +-- [Failure] --> StatusScreen
    |       |                   |
    |       |                   +-- [QR Link] --> QRScreen
    |       |
    |       +-- [Card Tap] --> BeneficiaryDetailScreen
    |                               |
    |                               +-- [Update] --> BeneficiaryApplicationScreen (UPDATE)
    |                               |                   |
    |                               |                   +-- (same as above)
    |                               |
    |                               +-- [Delete] --> Confirmation Dialog
    |                                                   |
    |                                                   +-- [Confirm] --> Delete API --> NavigateBack
```

---

## 10. Performance Requirements

| Metric | Target | Implementation |
|--------|--------|----------------|
| List load | < 1s | Lazy loading with LazyColumn |
| Filter apply | < 100ms | In-memory filtering |
| Form validation | Instant | Local validation |
| API calls | < 2s | Network timeout handling |
| Screen transition | < 300ms | Compose navigation |

---

## 11. Accessibility

| Feature | Implementation |
|---------|----------------|
| Screen Reader | contentDescription on all interactive elements |
| Font Scaling | Supports up to 200% scaling |
| Touch Targets | 48dp minimum tap target |
| Color Contrast | WCAG AA compliant (4.5:1) |
| Focus Navigation | Logical tab order through form fields |

---

## 12. Security Considerations

| Feature | Implementation |
|---------|----------------|
| Authentication | Required before create/update submission |
| Transfer Limit | Enforced per beneficiary |
| Account Validation | Server-side account number validation |
| Session Management | Uses existing auth session |

---

## Changelog

| Date | Change |
|------|--------|
| 2025-12-29 | Initial production-level specification based on implementation |
