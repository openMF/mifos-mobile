# Auth - Feature Specification

> **Purpose**: User authentication, registration, password recovery
> **User Value**: Secure access to banking services
> **Last Updated**: 2025-12-27

---

## 1. Overview

### 1.1 Feature Summary
The Auth feature provides comprehensive authentication flows including login, registration, OTP verification, password recovery, and ID document upload for new users.

### 1.2 User Stories
- As a user, I want to log in with my credentials so that I can access my accounts
- As a new user, I want to register an account so that I can use the banking app
- As a user, I want to recover my password if I forget it

---

## 2. Screen Layouts

### 2.1 Login Screen

```
┌─────────────────────────────────────────┐
│                                         │
│           [Mifos Logo]                  │
│                                         │
│  ┌─────────────────────────────────┐   │
│  │  Username                        │   │
│  └─────────────────────────────────┘   │
│                                         │
│  ┌─────────────────────────────────┐   │
│  │  Password                    👁   │   │
│  └─────────────────────────────────┘   │
│                                         │
│         Forgot Password?                │
│                                         │
│  ┌─────────────────────────────────┐   │
│  │         SIGN IN                  │   │
│  └─────────────────────────────────┘   │
│                                         │
│      Don't have an account? Sign Up     │
│                                         │
└─────────────────────────────────────────┘
```

### 2.2 Registration Screen

```
┌─────────────────────────────────────────┐
│  ← Back          Register               │
├─────────────────────────────────────────┤
│                                         │
│  First Name        [________________]   │
│  Middle Name       [________________]   │
│  Last Name         [________________]   │
│  Email             [________________]   │
│  Mobile Number     [________________]   │
│  Account Number    [________________]   │
│  Password          [________________]   │
│    [Password Strength Indicator]        │
│  Confirm Password  [________________]   │
│                                         │
│  ┌─────────────────────────────────┐   │
│  │         REGISTER                 │   │
│  └─────────────────────────────────┘   │
│                                         │
│      Already have an account? Login     │
│                                         │
└─────────────────────────────────────────┘
```

### 2.3 Sections Table

| # | Screen | Description | Priority |
|---|--------|-------------|----------|
| 1 | LoginScreen | Username/password login | P0 |
| 2 | RegistrationScreen | New user registration form | P0 |
| 3 | OtpAuthenticationScreen | OTP verification | P0 |
| 4 | RecoverPasswordScreen | Password recovery | P1 |
| 5 | SetPasswordScreen | Set new password | P1 |
| 6 | UploadIdScreen | ID document upload | P1 |

---

## 3. User Interactions

| Action | Trigger | Result | API Call |
|--------|---------|--------|----------|
| Login | Click Sign In | Validate & authenticate | POST /authentication |
| Register | Click Register | Submit registration | POST /registration |
| Verify OTP | Click Next | Verify user | POST /registration/user |
| Recover Password | Click Recover | Send recovery code | POST /user/password |
| Set Password | Click Submit | Update password | PUT /user/password |

---

## 4. State Model

```kotlin
// Login State
data class LoginState(
    val username: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val clientName: String = "",
    val isError: Boolean = false,
    val userNameError: StringResource? = null,
    val passwordError: StringResource? = null,
    val dialogState: DialogState? = null,
    val uiState: ScreenUiState?,
    val showOverlay: Boolean = false,
) {
    sealed interface DialogState {
        data class Error(val message: String) : DialogState
    }

    val isLoginButtonEnabled: Boolean
        get() = username.isNotEmpty() && password.length >= 8
}

// Login Events
sealed interface LoginEvent {
    data object NavigateToSignup : LoginEvent
    data object NavigateToPasscode : LoginEvent
    data object NavigateToForgotPassword : LoginEvent
    data class ShowToast(val message: String) : LoginEvent
}

// Login Actions
sealed interface LoginAction {
    data class UsernameChanged(val username: String) : LoginAction
    data class PasswordChanged(val password: String) : LoginAction
    data object TogglePasswordVisibility : LoginAction
    data object ErrorDialogDismiss : LoginAction
    data object LoginClicked : LoginAction
    data object SignupClicked : LoginAction
    data object NavigateToForgotPassword : LoginAction
}

// Registration State
data class SignUpState(
    val customerAccount: String = "",
    val firstName: String = "",
    val middleName: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val mobileNumber: String = "",
    val dialogState: SignUpDialog? = null,
    val uiState: ScreenUiState = ScreenUiState.Success,
    val showOverlay: Boolean = false,
    val isPasswordChanged: Boolean = false,
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val passwordFeedback: List<StringResource> = emptyList(),
    val passwordStrengthState: PasswordStrengthState = PasswordStrengthState.NONE,
    // Validation errors
    val firstNameError: StringResource? = null,
    val middleNameError: StringResource? = null,
    val lastNameError: StringResource? = null,
    val emailError: StringResource? = null,
    val mobileNumberError: StringResource? = null,
    val customerAccountError: StringResource? = null,
    val passwordError: StringResource? = null,
    val confirmPasswordError: StringResource? = null,
) {
    sealed interface SignUpDialog {
        data class Error(val message: String) : SignUpDialog
    }

    val isSubmitButtonEnabled: Boolean
        get() = customerAccount.isNotBlank() &&
            firstName.isNotBlank() &&
            lastName.isNotBlank() &&
            email.isNotBlank() &&
            password.isNotBlank() &&
            confirmPassword.isNotBlank()
}

// Registration Events
sealed interface SignUpEvent {
    data class ShowToast(val message: String) : SignUpEvent
    data object NavigateToUploadDocuments : SignUpEvent
    data object NavigateToLogin : SignUpEvent
}
```

---

## 5. API Requirements

| Endpoint | Method | Purpose | Status |
|----------|--------|---------|--------|
| /self/authentication | POST | User login | ✅ Exists |
| /self/registration | POST | Register user | ✅ Exists |
| /self/registration/user | POST | Verify OTP | ✅ Exists |
| /self/user/password | PUT | Update password | ✅ Exists |

---

## 6. Validation Rules

| Field | Validation | Error Message |
|-------|------------|---------------|
| Username | Required, non-empty | "Enter username" |
| Password | Required, min 8 chars | "Password must be 8+ chars" |
| Email | Valid email format | "Invalid email format" |
| Phone | 10+ digits, numeric | "Invalid phone number" |
| Names | Letters only | "No special characters allowed" |

---

## 7. Password Strength

| Level | Score | Requirements |
|-------|-------|--------------|
| WEAK_1 | 0 | < 8 chars |
| WEAK_2 | 1 | 8 chars, no variety |
| WEAK_3 | 2 | 8 chars, some variety |
| GOOD | 3 | 8 chars, uppercase/lowercase |
| STRONG | 4 | 8 chars, includes numbers |
| VERY_STRONG | 5 | 8 chars, includes symbols |

---

## 8. Navigation Flow

```
AUTH_GRAPH (Start: LoginRoute)
├── LoginRoute
│   ├── → RegistrationRoute (Sign Up)
│   ├── → RecoverPasswordRoute (Forgot Password)
│   └── → PasscodeScreen (Login success)
├── RegistrationRoute
│   └── → UploadIdRoute → OtpAuthenticationRoute
├── OtpAuthenticationRoute
│   └── → StatusScreen (Success/Failure)
├── RecoverPasswordRoute
│   └── → OtpAuthenticationRoute → SetPasswordRoute
└── SetPasswordRoute
    └── → StatusScreen → LoginRoute
```

---

## Changelog

| Date | Change |
|------|--------|
| 2025-12-29 | Updated state models to match actual implementation, added Events/Actions |
| 2025-12-27 | Initial spec from codebase analysis |
