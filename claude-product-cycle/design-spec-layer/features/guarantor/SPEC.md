# Guarantor - Feature Specification

> **Purpose**: Manage loan guarantors for self-service users
> **User Value**: Allow users to view, add, update, and delete guarantors for their loans
> **Last Updated**: 2025-12-29

---

## 1. Overview

### 1.1 Feature Summary
The Guarantor feature enables loan account holders to manage guarantors associated with their loans. Users can view a list of existing guarantors, see detailed information about each guarantor, add new guarantors, update guarantor details, and delete guarantors when needed.

### 1.2 User Stories
- As a loan account holder, I want to view all guarantors linked to my loan so that I can track who has guaranteed my loan
- As a user, I want to add a new guarantor to my loan so that I can meet loan requirements
- As a user, I want to view detailed information about a guarantor so that I can verify their details
- As a user, I want to update guarantor information so that I can keep records accurate
- As a user, I want to delete a guarantor so that I can remove outdated or incorrect entries

---

## 2. Screen Layouts

### 2.1 Guarantor List Screen

```
+------------------------------------------+
|  <-         View Guarantor               |
+------------------------------------------+
|                                          |
|  +------------------------------------+  |
|  |  John Doe                          |  |
|  |  External                          |  |
|  +------------------------------------+  |
|                                          |
|  +------------------------------------+  |
|  |  Michael Brown                     |  |
|  |  External                          |  |
|  +------------------------------------+  |
|                                          |
|  +------------------------------------+  |
|  |  [Empty State if no guarantors]    |  |
|  +------------------------------------+  |
|                                          |
|                                          |
|                                    [+]   |
+------------------------------------------+
```

### 2.2 Guarantor Detail Screen

```
+------------------------------------------+
|  <-       Guarantor Details         ...  |
+------------------------------------------+
|                                          |
|  First Name                              |
|  John                                    |
|  ----------------------------------------|
|                                          |
|  Last Name                               |
|  Doe                                     |
|  ----------------------------------------|
|                                          |
|  City                                    |
|  New York                                |
|  ----------------------------------------|
|                                          |
|  Guarantor Type                          |
|  External                                |
|  ----------------------------------------|
|                                          |
+------------------------------------------+

Menu Options (on ... click):
+------------------------+
|  Update Guarantor      |
|  Delete Guarantor      |
+------------------------+
```

### 2.3 Add/Update Guarantor Screen

```
+------------------------------------------+
|  <-       Add Guarantor                  |
+------------------------------------------+
|                                          |
|  Guarantor Type                          |
|  +------------------------------------+  |
|  |  Select Guarantor Type         v   |  |
|  +------------------------------------+  |
|                                          |
|  First Name                              |
|  +------------------------------------+  |
|  |                                    |  |
|  +------------------------------------+  |
|                                          |
|  Last Name                               |
|  +------------------------------------+  |
|  |                                    |  |
|  +------------------------------------+  |
|                                          |
|  City                                    |
|  +------------------------------------+  |
|  |                                    |  |
|  +------------------------------------+  |
|                                          |
|  +------------------------------------+  |
|  |           SUBMIT                   |  |
|  +------------------------------------+  |
|                                          |
+------------------------------------------+
```

### 2.4 Sections Table

| # | Screen | Description | Priority |
|---|--------|-------------|----------|
| 1 | GuarantorListScreen | Display list of guarantors for a loan | P0 |
| 2 | GuarantorDetailScreen | Show detailed guarantor information | P0 |
| 3 | AddGuarantorScreen | Form to add or update guarantor | P0 |

---

## 3. User Interactions

| Action | Trigger | Result | API Call |
|--------|---------|--------|----------|
| View Guarantor List | Navigate from loan account | Display all active guarantors | GET /loans/{loanId}/guarantors |
| View Guarantor Detail | Click on guarantor item | Show guarantor details | (uses cached list data) |
| Add New Guarantor | Click FAB (+) button | Open add guarantor form | POST /loans/{loanId}/guarantors |
| Update Guarantor | Click Update in menu | Open edit form with prefilled data | PUT /loans/{loanId}/guarantors/{id} |
| Delete Guarantor | Click Delete in menu | Show confirmation, then delete | DELETE /loans/{loanId}/guarantors/{id} |
| Submit Guarantor | Click Submit button | Validate and save guarantor | POST or PUT based on mode |
| Navigate Back | Click back arrow | Return to previous screen | - |

---

## 4. State Model

```kotlin
// Guarantor List State
@Parcelize
data class GuarantorListState(
    val loanId: Long? = -1L,
    val dialogState: DialogState? = null,
    val isOnline: Boolean = false,
    @IgnoredOnParcel
    val guarantorList: List<GuarantorPayload?>? = null,
) : Parcelable {

    sealed interface DialogState : Parcelable {
        @Parcelize
        data object Loading : DialogState

        @Parcelize
        data class ShowToast(val message: String) : DialogState
    }
}

// Guarantor List Events
sealed interface GuarantorListEvent {
    data object NavigateBack : GuarantorListEvent
    data class ShowToast(val message: String) : GuarantorListEvent
    data class AddGuarantor(val value: Long) : GuarantorListEvent
    data class GuarantorClicked(val index: Int, val loanId: Long) : GuarantorListEvent
}

// Guarantor List Actions
sealed interface GuarantorListAction {
    data object OnNavigateBackClick : GuarantorListAction
    data class OnGuarantorClicked(val index: Int) : GuarantorListAction
    data object OnAddGuarantor : GuarantorListAction
    data object DismissDialog : GuarantorListAction
}

// Guarantor Detail State
@Parcelize
data class GuarantorDetailState(
    val loanId: Long? = null,
    val index: Int? = null,
    val dialogState: DialogState?,
    @IgnoredOnParcel
    val guarantor: GuarantorPayload? = null,
    val isOnline: Boolean = false,
    val showDialog: Boolean = false,
) : Parcelable {
    sealed interface DialogState : Parcelable {
        @Parcelize
        data object Loading : DialogState

        @Parcelize
        data class ShowToast(val message: String) : DialogState
    }
}

// Guarantor Detail Events
sealed interface GuarantorDetailEvent {
    data object NavigateBack : GuarantorDetailEvent
    data class ShowToast(val message: String) : GuarantorDetailEvent
    data class UpdateGuarantor(val index: Int, val loanId: Long) : GuarantorDetailEvent
}

// Guarantor Detail Actions
sealed interface GuarantorDetailAction {
    data object NavigateBack : GuarantorDetailAction
    data object DeleteGuarantor : GuarantorDetailAction
    data object UpdateGuarantor : GuarantorDetailAction
    data object UpdateMenuDialogValue : GuarantorDetailAction
    data object DismissDialog : GuarantorDetailAction
}

// Add Guarantor State
@Parcelize
data class AddGuarantorState(
    val index: Int = -1,
    val loanId: Long? = -1L,
    val dialogState: DialogState?,
    val isOnline: Boolean = false,
    var firstName: String = "",
    var lastName: String = "",
    var city: String = "",
    @IgnoredOnParcel
    val guarantorItem: GuarantorPayload? = null,
    @IgnoredOnParcel
    val templatePayload: GuarantorTemplatePayload? = null,
    @IgnoredOnParcel
    var guarantorType: GuarantorType = GuarantorType(),
) : Parcelable {

    sealed interface DialogState : Parcelable {
        @Parcelize
        data object Loading : DialogState

        @Parcelize
        data class Error(val message: String) : DialogState
    }
}

// Add Guarantor Events
sealed interface AddGuarantorEvent {
    data object NavigateBack : AddGuarantorEvent
    data class ShowToast(val message: String) : AddGuarantorEvent
    data class Success(val message: String) : AddGuarantorEvent
}

// Add Guarantor Actions
sealed interface AddGuarantorAction {
    data class OnFirstNameChange(val firstName: String) : AddGuarantorAction
    data class OnLastnameChange(val lastname: String) : AddGuarantorAction
    data class OnCityChange(val city: String) : AddGuarantorAction
    data class SetGuarantortype(val type: GuarantorType) : AddGuarantorAction
    data object NavigateBack : AddGuarantorAction
    data class ValidateFields(val payload: GuarantorApplicationPayload?) : AddGuarantorAction
    data object DismissDialog : AddGuarantorAction
}
```

---

## 5. API Requirements

| Endpoint | Method | Purpose | Status |
|----------|--------|---------|--------|
| /loans/{loanId}/guarantors/template | GET | Fetch guarantor type options | Implemented |
| /loans/{loanId}/guarantors | GET | Fetch list of guarantors | Implemented |
| /loans/{loanId}/guarantors | POST | Create new guarantor | Implemented |
| /loans/{loanId}/guarantors/{guarantorId} | PUT | Update existing guarantor | Implemented |
| /loans/{loanId}/guarantors/{guarantorId} | DELETE | Delete guarantor | Implemented |

---

## 6. Edge Cases & Error Handling

| Scenario | Handling |
|----------|----------|
| No internet connection | Show snackbar with "Internet not connected" message |
| Empty guarantor list | Display empty state component |
| Failed to load guarantors | Show error toast message |
| First name empty | Show error: "First name can not be empty" |
| Last name empty | Show error: "Last name can't be empty" |
| Guarantor type not selected | Show error: "Guarantor type can not be empty or null" |
| Create guarantor success | Show success toast, navigate back |
| Update guarantor success | Show success toast, navigate back |
| Delete guarantor success | Show "Guarantor deleted successfully" toast |
| API error on create/update/delete | Show error message from server |

---

## 7. Navigation Flow

```
GUARANTOR_GRAPH (Start: GuarantorListScreen)
|-- GuarantorListScreen (route: guarantor_list_screen_route/{loanId})
|   |-- --> AddGuarantorScreen (FAB click, index=-1)
|   |-- --> GuarantorDetailScreen (item click)
|
|-- GuarantorDetailScreen (route: guarantor_detail_screen_route/{loanId}/{index})
|   |-- --> AddGuarantorScreen (Update menu, with index)
|   |-- <-- Back to GuarantorListScreen
|
|-- AddGuarantorScreen (route: guarantor_add_screen_route/{loanId}/{index})
    |-- <-- Back to previous screen (on success or back)
```

---

## 8. Validation Rules

| Field | Validation | Error Message |
|-------|------------|---------------|
| First Name | Required, non-empty | "First name can not be empty" |
| Last Name | Required, non-empty | "Last name can't be empty" |
| Guarantor Type | Required, must be selected | "Guarantor type can not be empty or null" |
| City | Optional | - |

---

## 9. Implementation Notes

1. **Fake Data Source**: The current implementation uses fake demo data for the guarantor list (`getDemoGuarantorPayloads()`) as the API does not return data consistently. The real API call is commented out in `GuarantorRepositoryImp`.

2. **Status Filtering**: Guarantor list displays only active guarantors (filtered by `status == true`).

3. **Guarantor Type Filtering**: The add screen filters guarantor type options to only show type with `id == 3L` (External guarantor).

4. **Index-based Detail Access**: The detail screen retrieves guarantor data by index from the list rather than making a separate API call.

---

## Changelog

| Date | Change |
|------|--------|
| 2025-12-29 | Initial specification from codebase analysis |
