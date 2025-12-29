# Passcode - Feature Specification

> **Purpose**: Secure local authentication for app access
> **User Value**: Quick and secure app unlock without entering full credentials
> **Last Updated**: 2025-12-29

---

## 1. Overview

### 1.1 Feature Summary
The Passcode feature provides a local 4-digit PIN-based authentication system that enables users to quickly unlock the app after initial login. It includes two main flows: setting up a new passcode (with confirmation) and verifying an existing passcode for subsequent app access.

### 1.2 User Stories
- As a user, I want to set up a 4-digit passcode so that I can quickly access the app without entering my full credentials
- As a user, I want to verify my passcode on app launch so that my account remains secure
- As a user, I want visual feedback when entering my passcode so I know how many digits I have entered
- As a user, I want to be notified if my passcodes do not match during setup so I can try again
- As a user, I want to see an error state if I enter the wrong passcode during verification

---

## 2. Screen Layouts

### 2.1 Set Passcode Screen (Initial Setup)

```
+-------------------------------------------+
|                                           |
|              [Lock Icon]                  |
|           (primary color bg)              |
|                                           |
|           Set Up Passcode                 |
|                                           |
|    Choose a 4-digit PIN to secure         |
|            your account                   |
|                                           |
|           O   O   O   O                   |
|        (empty dots for input)             |
|                                           |
|                                           |
|         [Powered by Mifos]                |
|                                           |
+---------+---------+---------+-------------+
|         |         |         |             |
|    1    |    2    |    3    |             |
|         |   ABC   |   DEF   |             |
+---------+---------+---------+-------------+
|         |         |         |             |
|    4    |    5    |    6    |             |
|   GHI   |   JKL   |   MNO   |             |
+---------+---------+---------+-------------+
|         |         |         |             |
|    7    |    8    |    9    |             |
|  PQRS   |   TUV   |  WXYZ   |             |
+---------+---------+---------+-------------+
|         |         |         |             |
|   <X    |    0    |   ->    |             |
|Backspace|         |  Send   |             |
+---------+---------+---------+-------------+
```

### 2.2 Confirm Passcode Screen (After Initial Entry)

```
+-------------------------------------------+
|                                           |
|              [Lock Icon]                  |
|           (primary color bg)              |
|                                           |
|         Confirm Passcode                  |
|                                           |
|    Re-enter your 4-digit PIN to           |
|              confirm                      |
|                                           |
|           *   *   O   O                   |
|       (filled dots show progress)         |
|                                           |
|                                           |
|         [Powered by Mifos]                |
|                                           |
+---------+---------+---------+-------------+
|         |         |         |             |
|    1    |    2    |    3    |             |
|         |   ABC   |   DEF   |             |
+---------+---------+---------+-------------+
|         |         |         |             |
|    4    |    5    |    6    |             |
|   GHI   |   JKL   |   MNO   |             |
+---------+---------+---------+-------------+
|         |         |         |             |
|    7    |    8    |    9    |             |
|  PQRS   |   TUV   |  WXYZ   |             |
+---------+---------+---------+-------------+
|         |         |         |             |
|   <X    |    0    |   ->    |             |
|Backspace|         |  Send   |             |
+---------+---------+---------+-------------+
```

### 2.3 Verify Passcode Screen (App Unlock)

```
+-------------------------------------------+
|                                           |
|              [Lock Icon]                  |
|           (primary color bg)              |
|                                           |
|            Authenticate                   |
|                                           |
|    Enter your passcode to unlock          |
|            the app                        |
|                                           |
|           *   *   *   O                   |
|        (3 digits entered)                 |
|                                           |
|                                           |
|         [Powered by Mifos]                |
|                                           |
+---------+---------+---------+-------------+
|         |         |         |             |
|    1    |    2    |    3    |             |
|         |   ABC   |   DEF   |             |
+---------+---------+---------+-------------+
|         |         |         |             |
|    4    |    5    |    6    |             |
|   GHI   |   JKL   |   MNO   |             |
+---------+---------+---------+-------------+
|         |         |         |             |
|    7    |    8    |    9    |             |
|  PQRS   |   TUV   |  WXYZ   |             |
+---------+---------+---------+-------------+
|         |         |         |             |
|   <X    |    0    |   ->    |             |
|Backspace|         |  Send   |             |
+---------+---------+---------+-------------+
```

### 2.4 Error State (Mismatched/Wrong Passcode)

```
+-------------------------------------------+
|                                           |
|              [Lock Icon]                  |
|                                           |
|           Set Up Passcode                 |
|                                           |
|    Choose a 4-digit PIN to secure         |
|            your account                   |
|                                           |
|           O   O   O   O                   |
|        (red border - error state)         |
|                                           |
+-------------------------------------------+
```

### 2.5 Sections Table

| # | Screen | Description | Priority |
|---|--------|-------------|----------|
| 1 | PasscodeScreen | Initial passcode setup (Set mode) | P0 |
| 2 | PasscodeScreen | Confirm passcode (Confirm mode) | P0 |
| 3 | VerifyPasscodeScreen | Verify passcode on app unlock | P0 |
| 4 | NumericKeyboard | Shared keyboard component | P0 |

---

## 3. User Interactions

| Action | Trigger | Result | API Call |
|--------|---------|--------|----------|
| Enter Digit | Tap digit key (0-9) | Appends digit to passcode, fills dot indicator | None (Local) |
| Delete Digit | Tap backspace key | Removes last digit, unfills dot indicator | None (Local) |
| Submit Passcode (Set) | Auto on 4th digit | Transitions to Confirm mode | None (Local) |
| Submit Passcode (Confirm) | Auto on 4th digit | Validates match, saves to storage | None (Local) |
| Submit Passcode (Verify) | Auto on 4th digit | Validates against stored, navigates forward | None (Local) |
| Mismatch Error | Passcodes don't match | Shows error state, resets to Set mode | None (Local) |
| Wrong Passcode | Verify fails | Shows error state, clears input | None (Local) |

---

## 4. State Model

### 4.1 Set/Confirm Passcode State (PasscodeViewModel)

```kotlin
// PasscodeState - Used for setting up new passcode
internal data class PasscodeState(
    internal val storedPasscode: String = "",
    val maxDigits: Int = 4,
    val filledDots: Int = 0,
    val passcode: String = "",
    val mode: PasscodeMode = PasscodeMode.Set,
    val firstPasscode: String = "",
    val passcodeError: Boolean = false,
)

// PasscodeMode - Current phase of passcode setup
enum class PasscodeMode {
    Set,      // Initial entry
    Confirm,  // Re-entry for confirmation
}

// PasscodeEvent - One-time effects
internal sealed interface PasscodeEvent {
    data class OnPasscodeConfirm(
        val eventType: String,          // "SUCCESS"
        val eventDestination: String,   // "UNLOCKED"
        val title: String,              // "Setup Successful"
        val subtitle: String,           // "Your passcode has been set"
        val buttonText: String,         // "Continue"
    ) : PasscodeEvent
}

// PasscodeAction - User interactions
internal sealed interface PasscodeAction {
    data object OnContinueClick : PasscodeAction
    data object OnBackspaceClick : PasscodeAction
    data class OnDigitClick(val digit: String) : PasscodeAction
}
```

### 4.2 Verify Passcode State (VerifyPasscodeViewModel)

```kotlin
// VerifyPasscodeState - Used for verifying existing passcode
internal data class VerifyPasscodeState(
    internal val storedPasscode: String = "",
    val maxDigits: Int = 4,
    val filledDots: Int = 0,
    val passcode: String = "",
    val passcodeError: Boolean = false,
)

// VerifyPasscodeEvent - One-time effects
internal sealed interface VerifyPasscodeEvent {
    data object PasscodeAccepted : VerifyPasscodeEvent
}

// VerifyPasscodeAction - User interactions
internal sealed interface VerifyPasscodeAction {
    data object OnContinueClick : VerifyPasscodeAction
    data object OnBackspaceClick : VerifyPasscodeAction
    data class OnDigitClick(val digit: String) : VerifyPasscodeAction
}
```

### 4.3 Library Passcode State (mifos-passcode library)

```kotlin
// PasscodeState - Full-featured passcode with visibility toggle
@Parcelize
data class PasscodeState(
    val hasPasscode: Boolean = false,
    val activeStep: Step = Step.Create,
    val filledDots: Int = 0,
    val passcodeVisible: Boolean = false,
    val currentPasscodeInput: String = "",
    val isPasscodeAlreadySet: Boolean = false,
) : Parcelable

// Step enum
enum class Step(var index: Int) {
    Create(0),
    Confirm(1),
}

// Constants
object Constants {
    const val STEPS_COUNT = 2
    const val PASSCODE_LENGTH = 4
    const val VIBRATE_FEEDBACK_DURATION = 300L
}
```

---

## 5. API Requirements

| Endpoint | Method | Purpose | Status |
|----------|--------|---------|--------|
| N/A | N/A | This is a local-only feature | N/A |

> **Note**: The Passcode feature does not require any API calls. All passcode data is stored locally on the device using DataStore preferences.

---

## 6. Local Storage

### 6.1 Storage Mechanism
The passcode is stored using `multiplatform-settings` library with serialization:

```kotlin
@Serializable
data class PasscodePreferencesProto(
    val passcode: String,
    val hasPasscode: Boolean,
) {
    companion object {
        val DEFAULT = PasscodePreferencesProto(passcode = "", hasPasscode = false)
    }
}

// Storage key
private const val PASSCODE_INFO_KEY = "passcodeInfo"
```

### 6.2 PasscodeManager API

```kotlin
class PasscodeManager {
    val getPasscode: StateFlow<String>   // Current stored passcode
    val hasPasscode: StateFlow<Boolean>  // Whether passcode is set

    suspend fun savePasscode(passcode: String)  // Save new passcode
    suspend fun clearPasscode()                  // Clear stored passcode
}
```

### 6.3 UserPreferencesRepository Integration

```kotlin
interface UserPreferencesRepository {
    val passcode: Flow<String>
    suspend fun setPasscode(passcode: String)
}
```

---

## 7. Validation Rules

| Rule | Validation | Error Behavior |
|------|------------|----------------|
| Passcode Length | Exactly 4 digits | Cannot submit until 4 digits entered |
| Digits Only | All characters must be 0-9 | Non-digit input ignored |
| Passcode Match | Confirm must match initial | Shows error, resets to Set mode |
| Verify Match | Input must match stored | Shows error, clears input |

---

## 8. Edge Cases & Error Handling

| Scenario | Handling |
|----------|----------|
| Passcode mismatch during setup | Display error state (red dots), reset to Set mode, clear input |
| Wrong passcode during verification | Display error state (red border), clear input, allow retry |
| Rapid digit entry | Input capped at maxDigits (4), extra taps ignored |
| Backspace on empty | No-op, safely ignored |
| App killed during setup | Passcode not saved until confirmed, restart setup flow |
| No stored passcode on verify | Should not happen - navigation logic prevents this |
| Multiple 4th digit attempts | Auto-validates on 4th digit, no manual submit needed |

---

## 9. Navigation Flow

```
PASSCODE_GRAPH
├── PasscodeRoute.Standard (Set/Confirm)
│   └── On Success → StatusScreen → MAIN_GRAPH
│
└── VerifyPasscodeRoute.Standard (Verify)
    └── On Success → MAIN_GRAPH (HomeScreen)

Navigation Integration:
- After login: AUTH_GRAPH → PASSCODE_GRAPH (PasscodeRoute)
- On app resume: ROOT_GRAPH → PASSCODE_GRAPH (VerifyPasscodeRoute)
```

---

## 10. UI Components

### 10.1 NumericKeyboard

```kotlin
@Composable
fun NumericKeyboard(
    onDigitClick: (String) -> Unit,
    onBackspaceClick: () -> Unit,
    onSendClick: () -> Unit,
    isSendEnabled: Boolean,
    modifier: Modifier = Modifier,
)

// Key types
sealed class PasscodeKey {
    data class Digit(val number: String) : PasscodeKey()
    object Backspace : PasscodeKey()
    object Send : PasscodeKey()
}
```

### 10.2 Digit Button with Letters

```kotlin
@Composable
fun DigitKeyButton(
    digit: String,
    letters: String,  // e.g., "ABC" for digit "2"
    onClick: () -> Unit,
)

// Letter mapping
fun getLettersForDigit(digit: String): String {
    return when (digit) {
        "2" -> "ABC"
        "3" -> "DEF"
        "4" -> "GHI"
        "5" -> "JKL"
        "6" -> "MNO"
        "7" -> "PQRS"
        "8" -> "TUV"
        "9" -> "WXYZ"
        else -> ""
    }
}
```

### 10.3 Dot Indicators

- Empty state: Transparent fill with primary border
- Filled state: Primary color fill with primary border
- Error state: Error color border (and fill for already-filled dots)

---

## 11. Dependency Injection

```kotlin
val PasscodeModule = module {
    viewModelOf(::PasscodeViewModel)
    viewModelOf(::VerifyPasscodeViewModel)
}
```

Dependencies:
- `PasscodeViewModel`: UserPreferencesRepository
- `VerifyPasscodeViewModel`: UserPreferencesRepository, ResultNavigator

---

## Changelog

| Date | Change |
|------|--------|
| 2025-12-29 | Initial specification created from implementation analysis |
