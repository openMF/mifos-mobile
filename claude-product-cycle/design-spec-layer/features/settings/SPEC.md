# Settings - Feature Specification

> **Purpose**: User preferences, account security, app configuration, and support access
> **User Value**: Personalize app experience, manage security, access help resources
> **Last Updated**: 2025-12-29

---

## 1. Overview

### 1.1 Feature Summary
The Settings feature provides comprehensive user configuration options including profile display, password/passcode management, theme selection, language preferences, and access to support resources (FAQ, Help, About Us, App Info). Users can also logout from the application.

### 1.2 User Stories
- As a user, I want to view my profile information so I know I am logged into the correct account
- As a user, I want to change my password to maintain account security
- As a user, I want to update my passcode for quicker app authentication
- As a user, I want to change the app language to use the app in my preferred language
- As a user, I want to switch between light/dark themes for comfortable viewing
- As a user, I want to access FAQ and help resources when I have questions
- As a user, I want to logout securely from the application

---

## 2. Screen Layouts

### 2.1 Main Settings Screen

```
+-------------------------------------------+
|  <- Back          Settings                |
+-------------------------------------------+
|                                           |
|  +-----+  John Doe                        |
|  |     |  Customer Account No: 12345678   |
|  +-----+                                  |
|                                           |
+-------------------------------------------+
|  [Key] Change Password                    |
|        Update your account password       |
+-------------------------------------------+
|  [Pin] Authorization Passcode             |
|        Set your app passcode              |
+-------------------------------------------+
|  [Lng] Language                           |
|        Change app language                |
+-------------------------------------------+
|  [Thm] Display Theme                      |
|        Light, Dark, or System             |
+-------------------------------------------+
|  [Ppl] About Us                           |
|        Learn about Mifos Initiative       |
+-------------------------------------------+
|  [Str] Rate Us                            |
|        Share your feedback                |
+-------------------------------------------+
|  [?]   FAQ                                |
|        Frequently asked questions         |
+-------------------------------------------+
|  [Msg] Help                               |
|        Contact support                    |
+-------------------------------------------+
|  [App] App Info                           |
|        Version and legal info             |
+-------------------------------------------+
|  [Out] Logout                             |
|        Sign out of your account           |
+-------------------------------------------+
```

### 2.2 Change Password Screen

```
+-------------------------------------------+
|  <- Back          Password                |
+-------------------------------------------+
|                                           |
|  Old Password                             |
|  +-------------------------------------+  |
|  | ********                         [eye] |
|  +-------------------------------------+  |
|                                           |
|  New Password                             |
|  +-------------------------------------+  |
|  | ********                         [eye] |
|  +-------------------------------------+  |
|                                           |
|  +=====================================+  |
|  | Password Strength: [====----] GOOD  |  |
|  | - Minimum 12 characters             |  |
|  | - Include uppercase & lowercase     |  |
|  | - Include numbers                   |  |
|  +=====================================+  |
|                                           |
|  Confirm New Password                     |
|  +-------------------------------------+  |
|  | ********                         [eye] |
|  +-------------------------------------+  |
|                                           |
|  +-------------------------------------+  |
|  |              NEXT                   |  |
|  +-------------------------------------+  |
|                                           |
+-------------------------------------------+
```

### 2.3 Update Passcode Screen

```
+-------------------------------------------+
|  <- Back     Authorization Passcode       |
+-------------------------------------------+
|                                           |
|  Old Passcode                             |
|  +-------------------------------------+  |
|  | ****                             [eye] |
|  +-------------------------------------+  |
|                                           |
|  New Passcode                             |
|  +-------------------------------------+  |
|  | ****                             [eye] |
|  +-------------------------------------+  |
|                                           |
|  Confirm New Passcode                     |
|  +-------------------------------------+  |
|  | ****                             [eye] |
|  +-------------------------------------+  |
|                                           |
|  +-------------------------------------+  |
|  |              NEXT                   |  |
|  +-------------------------------------+  |
|                                           |
+-------------------------------------------+
```

### 2.4 Language Selection Screen

```
+-------------------------------------------+
|  <- Back          Language                |
+-------------------------------------------+
|                                           |
|  ( ) English                              |
|  ( ) Hindi                                |
|  ( ) Spanish                              |
|  ( ) French                               |
|  ( ) Arabic                               |
|  ( ) Portuguese                           |
|  ( ) Bengali                              |
|  ( ) Chinese                              |
|  ( ) Indonesian                           |
|  ( ) Japanese                             |
|  ...                                      |
|                                           |
+-------------------------------------------+
|  +-------------------------------------+  |
|  |         CHANGE LANGUAGE             |  |
|  +-------------------------------------+  |
+-------------------------------------------+
```

### 2.5 Theme Selection Screen

```
+-------------------------------------------+
|  <- Back         Display Theme            |
+-------------------------------------------+
|                                           |
|  ( ) Follow System                        |
|                                           |
|  ( ) Dark Mode                            |
|                                           |
|  ( ) Light Mode                           |
|                                           |
|  ( ) Based on Time                        |
|      Dark [18:00 - 06:00]                 |
|      Light [06:00 - 18:00]                |
|                                           |
|  +-------------------------------------+  |
|  |            APPLY THEME              |  |
|  +-------------------------------------+  |
|                                           |
+-------------------------------------------+
```

### 2.6 Time-Based Theme Dialog

```
+-------------------------------------+
|     Choose Dark Mode Time           |
|                                     |
|  Dark mode starts at                |
|  +---------------------------------+|
|  | 18:00                           ||
|  +---------------------------------+|
|                                     |
|  Dark mode ends at                  |
|  +---------------------------------+|
|  | 06:00                           ||
|  +---------------------------------+|
|                                     |
|  [Cancel]               [Apply]     |
+-------------------------------------+
```

### 2.7 FAQ Screen

```
+-------------------------------------------+
|  <- Back             FAQ                  |
+-------------------------------------------+
|                                           |
|  v How do I apply for a loan?             |
|  +-------------------------------------+  |
|  | Click on "Apply for Loan" on the   |  |
|  | Home Screen...                      |  |
|  +-------------------------------------+  |
|                                           |
|  > What is Mifos?                         |
|                                           |
|  > How do I view account transactions?    |
|                                           |
|  > How to transfer money?                 |
|                                           |
|  > How do I contact support?              |
|                                           |
|  ...                                      |
|                                           |
+-------------------------------------------+
|         Still have doubts? Contact Us     |
+-------------------------------------------+
```

### 2.8 Help Screen

```
+-------------------------------------------+
|  <- Back             Help                 |
+-------------------------------------------+
|                                           |
|  +=====================================+  |
|  |  Have any Doubts?                   |  |
|  |  Check our FAQ section              |  |
|  +=====================================+  |
|                                           |
|  +=====================================+  |
|  |  Still Have Doubts?                 |  |
|  |  Reach out to us via phone          |  |
|  |                                     |  |
|  |  +-------------------------------+  |  |
|  |  |      CALL US                  |  |  |
|  |  +-------------------------------+  |  |
|  +=====================================+  |
|                                           |
|  +=====================================+  |
|  |  Still Have Issues?                 |  |
|  |  Reach out to us via email          |  |
|  |                                     |  |
|  |  +-------------------------------+  |  |
|  |  |      MAIL US                  |  |  |
|  |  +-------------------------------+  |  |
|  +=====================================+  |
|                                           |
+-------------------------------------------+
```

### 2.9 About Us Screen

```
+-------------------------------------------+
|  <- Back           About Us               |
+-------------------------------------------+
|                                           |
|  +=====================================+  |
|  |  [Logo] Mifos                       |  |
|  |                                     |  |
|  |  Who Are We?                        |  |
|  |  Mifos is a community-built open    |  |
|  |  source platform for financial      |  |
|  |  inclusion...                       |  |
|  |                                     |  |
|  |  What Does Mifos Do?                |  |
|  |  - Provides banking solutions       |  |
|  |  - Enables microfinance services    |  |
|  |  - Supports financial inclusion     |  |
|  |                                     |  |
|  |       [Illustration Image]          |  |
|  +=====================================+  |
|                                           |
+-------------------------------------------+
```

### 2.10 App Info Screen

```
+-------------------------------------------+
|  <- Back           App Info               |
+-------------------------------------------+
|                                           |
|  +=====================================+  |
|  |  [Logo] Mifos                       |  |
|  |                                     |  |
|  |  Mifos Electronic Banking           |  |
|  |  All Rights Reserved                |  |
|  |                                     |  |
|  |  Mifos Mobile App                   |  |
|  |  All Rights Reserved                |  |
|  |  Version 1.0.0                      |  |
|  |                                     |  |
|  |      [App Info Illustration]        |  |
|  +=====================================+  |
|                                           |
+-------------------------------------------+
```

### 2.11 Logout Confirmation Dialog

```
+-------------------------------------+
|       Logout?                       |
|                                     |
|  Are you sure you want to logout    |
|  from the application?              |
|                                     |
|  +-------------------------------+  |
|  |           LOGOUT              |  |
|  +-------------------------------+  |
|                                     |
|   Changed your mind? Go Back        |
+-------------------------------------+
```

---

## 3. Sections Table

| # | Screen | Description | Priority |
|---|--------|-------------|----------|
| 1 | SettingsScreen | Main settings hub with profile card | P0 |
| 2 | ChangePasswordScreen | Update account password | P0 |
| 3 | UpdatePasscodeScreen | Update app passcode | P0 |
| 4 | LanguageScreen | App language selection | P1 |
| 5 | ChangeThemeScreen | Theme preference selection | P1 |
| 6 | FAQScreen | Frequently asked questions | P2 |
| 7 | HelpScreen | Contact support options | P2 |
| 8 | AboutScreen | About Mifos Initiative | P2 |
| 9 | AppInfoScreen | App version and legal info | P2 |
| 10 | LogoutDialog | Logout confirmation | P0 |

---

## 4. User Interactions

| Action | Trigger | Result | API Call |
|--------|---------|--------|----------|
| View Profile | Load Settings | Display user name & account | GET /clients |
| Change Password | Click Submit | Update password & logout | PUT /user/password |
| Update Passcode | Click Next | Save new passcode locally | None (Local) |
| Change Language | Click Apply | Update app locale | None (Local) |
| Change Theme | Click Apply | Update app theme | None (Local) |
| View FAQ | Click FAQ Item | Expand answer accordion | None (Local) |
| Call Support | Click Call Us | Open phone dialer | None (Platform) |
| Email Support | Click Mail Us | Open email client | None (Platform) |
| Rate App | Click Rate Us | Open app review | None (Platform) |
| Logout | Click Logout | Clear data & navigate to login | None (Local) |

---

## 5. State Model

```kotlin
// Settings Main State
@Immutable
data class SettingsState(
    val settingsItems: ImmutableList<SettingsItems>,
    val clientId: Long? = null,
    val client: Client? = null,
    val profileImage: ByteArray? = null,
    val dialogState: DialogState? = null,
    val isUserLoading: Boolean = true,
    val isUserLoaded: Boolean = false,
    val uiState: ScreenUiState? = ScreenUiState.Success,
    val networkStatus: Boolean = false,
) {
    sealed interface DialogState {
        data class Logout(
            val title: StringResource,
            val message: StringResource,
        ) : DialogState
    }
}

// Settings Actions
sealed interface SettingsAction {
    data object OnNavigateBack : SettingsAction
    data object DismissDialog : SettingsAction
    data object Retry : SettingsAction
    data class NavigateTo(val item: SettingsItems) : SettingsAction
    data object LogoutDialog : SettingsAction
    data object Logout : SettingsAction
    data class ReceiveNetworkStatus(val isOnline: Boolean) : SettingsAction
    data object RateApp : SettingsAction

    sealed interface Internal : SettingsAction {
        data class ReceiveClientInfo(val dataState: DataState<Client>) : Internal
        data class ReceiveClientImage(val dataState: DataState<String>) : Internal
    }
}

// Settings Events
sealed interface SettingsEvents {
    data object NavigateBack : SettingsEvents
    data class NavigateTo(val item: SettingsItems) : SettingsEvents
    data object RateApp : SettingsEvents
}

// Password State
data class PasswordState(
    internal val currentPassword: String = "",
    val oldPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val oldPasswordError: StringResource? = null,
    val newPasswordError: StringResource? = null,
    val confirmPasswordError: StringResource? = null,
    val oldPasswordVisible: Boolean = false,
    val newPasswordVisible: Boolean = false,
    val confirmPasswordVisible: Boolean = false,
    val passwordStrengthState: PasswordStrengthState = PasswordStrengthState.NONE,
    val passwordFeedback: List<StringResource> = emptyList(),
    val dialogState: DialogState? = null,
) {
    sealed interface DialogState {
        data object Loading : DialogState
        data class Success(val message: StringResource) : DialogState
        data class Error(val message: StringResource) : DialogState
    }

    val isEnabled = oldPasswordError == null &&
        newPasswordError == null &&
        confirmPasswordError == null &&
        oldPassword.isNotEmpty() &&
        newPassword.isNotEmpty() &&
        confirmPassword.isNotEmpty()
}

// Password Actions
sealed interface PasswordAction {
    data class OnOldPasswordChange(val currentPassword: String) : PasswordAction
    data class OnNewPasswordChange(val newPassword: String) : PasswordAction
    data class OnConfirmPasswordChange(val confirmPassword: String) : PasswordAction
    data object OldPasswordVisibleClick : PasswordAction
    data object NewPasswordVisibleClick : PasswordAction
    data object ConfirmPasswordVisibleClick : PasswordAction
    data object SubmitClick : PasswordAction
    data object RetrySubmit : PasswordAction
    data object NavigateBack : PasswordAction
    data object DismissDialog : PasswordAction

    sealed interface Internal : PasswordAction {
        data class UpdatePasswordResult(val result: DataState<String>) : Internal
        data class ReceivePasswordStrengthResult(val result: PasswordStrengthResult) : Internal
        data class OldPasswordReceived(val password: String?) : Internal
    }
}

// Passcode State
data class PasscodeState(
    internal val currentPasscode: String = "",
    val oldPasscode: String = "",
    val newPasscode: String = "",
    val confirmPasscode: String = "",
    val oldPasscodeError: StringResource? = null,
    val newPasscodeError: StringResource? = null,
    val confirmPasscodeError: StringResource? = null,
    val isOldPasscodeVisible: Boolean = false,
    val isNewPasscodeVisible: Boolean = false,
    val isConfirmPasscodeVisible: Boolean = false,
    val dialogState: DialogState? = null,
) {
    sealed interface DialogState {
        data object Loading : DialogState
        data class Shown(val message: StringResource) : DialogState
    }
}

// Passcode Actions
sealed interface PasscodeAction {
    data class OnOldPasscodeChange(val oldPasscode: String) : PasscodeAction
    data class OnNewPasscodeChange(val newPasscode: String) : PasscodeAction
    data class OnConfirmPasscodeChange(val confirmPasscode: String) : PasscodeAction
    data object NewPasscodeVisibleClick : PasscodeAction
    data object OldPasscodeVisibleClick : PasscodeAction
    data object ConfirmPasscodeVisibleClick : PasscodeAction
    data object SubmitClick : PasscodeAction
    data object NavigateBackClick : PasscodeAction
    data object DismissDialog : PasscodeAction
    data object NavigateToPasscodeScreen : PasscodeAction

    sealed interface Internal : PasscodeAction {
        data class UpdatePasscodeResult(val result: StringResource) : Internal
        data class CurrentPasscodeReceived(val passcode: String) : Internal
    }
}

// Theme State
data class ThemeState(
    val currentTheme: MifosThemeConfig,
    val showTimeBasedDialog: Boolean = false,
    val timeBasedTheme: TimeBasedTheme = TimeBasedTheme(
        hourStart = 6,
        hourEnd = 18,
        timeStart = 0,
        timeEnd = 0,
    ),
) {
    val themeOptions = listOf(
        MifosThemeConfig.FOLLOW_SYSTEM to Res.string.feature_settings_theme_system,
        MifosThemeConfig.DARK to Res.string.feature_settings_theme_dark,
        MifosThemeConfig.LIGHT to Res.string.feature_settings_theme_light,
        MifosThemeConfig.BASED_ON_TIME to Res.string.feature_settings_theme_based_on_time,
    )
}

// Theme Actions
sealed interface ThemeAction {
    data object SetTheme : ThemeAction
    data class ThemeSelection(val theme: MifosThemeConfig) : ThemeAction
    data object NavigateBack : ThemeAction
    data object ShowTimeBasedDialog : ThemeAction
    data object HideTimeBasedDialog : ThemeAction
    data class UpdateTimeBasedTheme(val theme: TimeBasedTheme) : ThemeAction

    sealed interface Internal : ThemeAction {
        data class LoadTheme(val theme: MifosThemeConfig) : Internal
        data class LoadTimeBasedTheme(val theme: TimeBasedTheme) : Internal
    }
}

// Language State
data class LanguageState(
    val selectedLanguage: LanguageConfig,
    val currentLanguage: LanguageConfig,
)

// Language Actions
sealed interface LanguageAction {
    data object OnNavigateBack : LanguageAction
    data class LanguageSelected(val language: LanguageConfig) : LanguageAction
    data class SetLanguage(val languageConfig: LanguageConfig) : LanguageAction

    sealed interface Internal : LanguageAction {
        data class LoadLanguage(val language: LanguageConfig) : Internal
    }
}

// FAQ State
data class FaqState(
    val faqList: List<FAQ> = emptyList(),
    val selectedFaqPosition: Int = 0,
)

// FAQ Actions
sealed interface FaqAction {
    data object NavigateBack : FaqAction
    data object NavigateToHelp : FaqAction
    data class UpdateFaqPosition(val position: Int) : FaqAction

    sealed interface Internal : FaqAction {
        data object LoadFaqList : Internal
    }
}
```

---

## 6. API Requirements

| Endpoint | Method | Purpose | Status |
|----------|--------|---------|--------|
| /self/clients/{clientId} | GET | Get client info | Exists |
| /self/clients/{clientId}/images | GET | Get profile image | Exists |
| /self/user/password | PUT | Update password | Exists |

Note: Most settings operations are local (passcode, theme, language) and do not require API calls.

---

## 7. Validation Rules

### Password Validation

| Field | Validation | Error Message |
|-------|------------|---------------|
| Old Password | Required, min 8 chars | "Password is required" |
| Old Password | Must match stored | "Current password is incorrect" |
| New Password | Required, min 8 chars | "Password must be at least 8 characters" |
| New Password | No consecutive repeating chars | "Password has repeating characters" |
| New Password | Different from old | "New password must differ from current" |
| Confirm Password | Must match new | "Passwords do not match" |

### Passcode Validation

| Field | Validation | Error Message |
|-------|------------|---------------|
| Old Passcode | Required, exactly 4 digits | "Passcode must be 4 digits" |
| Old Passcode | Numeric only | "Passcode must be numeric" |
| Old Passcode | Must match stored | "Current passcode is incorrect" |
| New Passcode | Required, exactly 4 digits | "Passcode must be 4 digits" |
| Confirm Passcode | Must match new | "Passcodes do not match" |

---

## 8. Password Strength

| Level | Score | Visual | Requirements |
|-------|-------|--------|--------------|
| NONE | - | Empty | No password entered |
| WEAK_1 | 0 | [=-----] | Very weak |
| WEAK_2 | 1 | [==----] | < 8 chars |
| WEAK_3 | 2 | [===---] | 8 chars, no variety |
| GOOD | 3 | [====--] | Uppercase + lowercase |
| STRONG | 4 | [=====- ] | Includes numbers |
| VERY_STRONG | 5 | [======] | Includes symbols |

---

## 9. Settings Items

| Item | Icon | Route | Description |
|------|------|-------|-------------|
| Password | PersonPasskey | PASSWORD | Change account password |
| AuthPasscode | TableCellEdit | AUTH_PASSCODE | Set app passcode |
| Language | BookLetter | LANGUAGE | Change app language |
| Theme | DarkTheme | THEME | Display theme settings |
| AboutUs | PeopleCommunity | ABOUT_US | About Mifos Initiative |
| RateUs | RateUs | RATE_US | App review prompt |
| FAQ | QuestionCircle | FAQ | Frequently asked questions |
| Help | ChatMultiple | HELP | Contact support |
| AppInfo | AppRecent | APP_INFO | Version and legal |
| Logout | SignOut | LOGOUT | Sign out of app |

---

## 10. Navigation Flow

```
SETTINGS_GRAPH (Start: SettingsRoute)
|
+-- SettingsRoute (Main Hub)
|   +-- --> ChangePasswordRoute
|   |       +-- Success --> Auto Logout (3s delay)
|   |
|   +-- --> UpdatePasscodeRoute
|   |       +-- Success --> Navigate Back
|   |
|   +-- --> LanguageRoute
|   |       +-- Apply --> Navigate Back (App reloads)
|   |
|   +-- --> ThemeRoute
|   |       +-- Apply --> Navigate Back (Theme applied)
|   |
|   +-- --> AboutRoute
|   |
|   +-- --> FAQRoute
|   |       +-- Contact Us --> HelpRoute
|   |
|   +-- --> HelpRoute
|   |       +-- FAQ Card --> FAQRoute
|   |       +-- Call Us --> Phone Dialer
|   |       +-- Mail Us --> Email Client
|   |
|   +-- --> AppInfoRoute
|   |
|   +-- --> Rate Us (In-App Review)
|   |
|   +-- --> Logout Dialog
|           +-- Confirm --> Clear Data --> Login Screen
```

---

## 11. Edge Cases & Error Handling

### Network Errors
- Settings screen monitors network status
- Profile card shows loading/error state when offline
- Retry button available for failed profile fetch

### Password Change
- Maximum 5 failed attempts before blocking
- Too many attempts shows error dialog
- Auto logout after successful password change (3 second delay)
- Sensitive data cleared on ViewModel disposal

### Passcode Change
- 4-digit numeric input enforced
- Debounced validation (300ms delay)
- Success dialog with navigation to passcode screen

### Theme Selection
- Time-based theme shows picker dialog
- Persisted to DataStore immediately
- App theme updates in real-time

### Language Change
- App locale updates immediately
- Persisted to UserPreferences
- UI refreshes with new strings

---

## Changelog

| Date | Change |
|------|--------|
| 2025-12-29 | Initial spec from codebase analysis |
