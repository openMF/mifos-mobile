# Settings - API Reference

## Overview

The Settings feature is **primarily local/offline**. Most operations (passcode, theme, language) are stored locally using DataStore and do not require API calls. Only password change and profile fetching require server communication.

## Base URL
`https://tt.mifos.community/fineract-provider/api/v1/self/`

---

## Endpoints Required

### 1. Get Client Information

**Endpoint**: `GET /clients/{clientId}`

**Description**: Fetches client profile information for displaying on the settings screen

**Headers**:
```
Authorization: Basic {base64EncodedAuthenticationKey}
Fineract-Platform-TenantId: default
Content-Type: application/json
```

**Response**:
```json
{
  "id": 123,
  "accountNo": "000000123",
  "displayName": "John Doe",
  "firstname": "John",
  "lastname": "Doe",
  "mobileNo": "1234567890",
  "emailAddress": "john.doe@example.com",
  "status": {
    "id": 300,
    "code": "clientStatusType.active",
    "value": "Active"
  }
}
```

**Kotlin DTO**:
```kotlin
@Serializable
data class Client(
    val id: Long,
    val accountNo: String?,
    val displayName: String?,
    val firstname: String?,
    val lastname: String?,
    val mobileNo: String?,
    val emailAddress: String?,
    val status: ClientStatus?,
)

@Serializable
data class ClientStatus(
    val id: Long,
    val code: String?,
    val value: String?,
)
```

**Status**: Implemented in HomeRepository

---

### 2. Get Client Image

**Endpoint**: `GET /clients/{clientId}/images`

**Description**: Fetches the client's profile image as Base64 encoded string

**Headers**:
```
Authorization: Basic {base64EncodedAuthenticationKey}
Fineract-Platform-TenantId: default
Accept: text/plain
```

**Response**:
```
data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAA...
```

**Status**: Implemented in HomeRepository

---

### 3. Update Account Password

**Endpoint**: `PUT /user/password`

**Description**: Updates the user's account password. After successful update, user is automatically logged out.

**Headers**:
```
Authorization: Basic {base64EncodedAuthenticationKey}
Fineract-Platform-TenantId: default
Content-Type: application/json
```

**Request**:
```json
{
  "newPassword": "newSecurePassword123!",
  "confirmPassword": "newSecurePassword123!"
}
```

**Response (Success)**:
```json
{
  "message": "Password updated successfully"
}
```

**Response (Error)**:
```json
{
  "developerMessage": "The password does not match",
  "httpStatusCode": "400",
  "defaultUserMessage": "Password validation failed"
}
```

**Kotlin DTO**:
```kotlin
@Serializable
data class UpdatePasswordPayload(
    val newPassword: String,
    val confirmPassword: String,
)
```

**Status**: Implemented in UserAuthRepository

---

## API Summary

| Endpoint | Method | Service | Repository | Status |
|----------|--------|---------|------------|--------|
| /clients/{id} | GET | ClientService | HomeRepository | Implemented |
| /clients/{id}/images | GET | ClientService | HomeRepository | Implemented |
| /user/password | PUT | UserService | UserAuthRepository | Implemented |

---

## Local Storage (No API Required)

The following settings are stored locally and do not require API communication:

### DataStore Keys (UserPreferences)

| Setting | Storage Key | Type | Description |
|---------|-------------|------|-------------|
| Passcode | passcode | String | 4-digit app passcode (encrypted) |
| Theme | darkThemeConfig | Enum | FOLLOW_SYSTEM, DARK, LIGHT, BASED_ON_TIME |
| Time-Based Theme | timeBasedThemeConfig | Object | Start/end hours for dark mode |
| Language | language | Enum | LanguageConfig enum value |
| Client ID | clientId | Long | Cached client identifier |
| Auth Token | authToken | String | Authentication key |

### Time-Based Theme Config

```kotlin
@Serializable
data class TimeBasedTheme(
    val hourStart: Int,     // Dark mode start hour (0-23)
    val timeStart: Int,     // Dark mode start minute (0-59)
    val hourEnd: Int,       // Dark mode end hour (0-23)
    val timeEnd: Int,       // Dark mode end minute (0-59)
)
```

### Language Options

```kotlin
enum class LanguageConfig(val languageCode: String, val languageName: String) {
    ENGLISH("en", "English"),
    HINDI("hi", "Hindi"),
    SPANISH("es", "Spanish"),
    FRENCH("fr", "French"),
    ARABIC("ar", "Arabic"),
    PORTUGUESE("pt", "Portuguese"),
    BENGALI("bn", "Bengali"),
    CHINESE("zh", "Chinese"),
    INDONESIAN("id", "Indonesian"),
    JAPANESE("ja", "Japanese"),
    // ... more languages
}
```

### Theme Options

```kotlin
enum class MifosThemeConfig {
    FOLLOW_SYSTEM,    // Follow OS dark mode setting
    DARK,             // Always dark mode
    LIGHT,            // Always light mode
    BASED_ON_TIME     // Dark mode during configured hours
}
```

---

## Error Responses

| Status Code | Error | Description |
|-------------|-------|-------------|
| 400 | Bad Request | Invalid password format or mismatch |
| 401 | Unauthorized | Session expired, token invalid |
| 403 | Forbidden | Account locked or disabled |
| 404 | Not Found | Client ID not found |
| 500 | Server Error | Internal server error |

---

## Security Considerations

### Password Update
- Old password is validated client-side against stored value
- New password is sent to server for update
- Maximum 5 failed attempts before blocking
- Automatic logout after successful password change
- All sensitive data cleared from ViewModel on disposal

### Passcode (Local Only)
- 4-digit numeric passcode stored locally
- Used for quick app authentication (not account auth)
- Validated against locally stored value
- Never sent to server

### Logout
- Clears all local data from DataStore
- Clears authentication token
- Navigates to login screen
- Does not require API call

---

## Platform Actions (No API Required)

| Action | Platform | Implementation |
|--------|----------|----------------|
| Call Helpline | All | ShareUtils.callHelpline() |
| Email Support | All | ShareUtils.mailHelpline() |
| Rate App | Android | AppReviewManager.promptForReview() |
| Rate App | iOS | SKStoreReviewController |

---

## FAQ Data (Local Resource)

FAQ questions and answers are loaded from local string resources:
- `faq_qs` - String array of questions
- `faq_ans` - String array of corresponding answers

No API call required for FAQ content.
