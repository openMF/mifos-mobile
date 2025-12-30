# Auth - API Reference

## Base URL
`https://tt.mifos.community/fineract-provider/api/v1/self/`

---

## Endpoints Required

### 1. User Login

**Endpoint**: `POST /authentication`

**Description**: Authenticate user with username and password

**Request**:
```json
{
  "username": "string",
  "password": "string"
}
```

**Response**:
```json
{
  "userId": 123,
  "username": "john_doe",
  "clients": [456],
  "isAuthenticated": true,
  "base64EncodedAuthenticationKey": "encoded_key",
  "officeName": "Head Office"
}
```

**Kotlin DTO**:
```kotlin
@Serializable
data class User(
    val userId: Long,
    val username: String?,
    val clients: List<Long>,
    val isAuthenticated: Boolean,
    val base64EncodedAuthenticationKey: String?,
    val officeName: String?,
)
```

**Status**: ✅ Implemented in AuthenticationService

---

### 2. User Registration

**Endpoint**: `POST /registration`

**Description**: Register a new user account

**Request**:
```json
{
  "accountNumber": "string",
  "authenticationMode": "email",
  "email": "user@example.com",
  "firstName": "John",
  "middleName": "M",
  "lastName": "Doe",
  "mobileNumber": "1234567890",
  "password": "securePassword123",
  "username": "john_doe"
}
```

**Response**:
```json
{
  "requestId": "12345"
}
```

**Kotlin DTO**:
```kotlin
@Serializable
data class RegisterPayload(
    val accountNumber: String,
    val authenticationMode: String = "email",
    val email: String,
    val firstName: String,
    val middleName: String,
    val lastName: String,
    val mobileNumber: String,
    val password: String,
    val username: String,
)
```

**Status**: ✅ Implemented in RegistrationService

---

### 3. Verify OTP

**Endpoint**: `POST /registration/user`

**Description**: Verify user registration with OTP

**Request**:
```json
{
  "authenticationToken": "123456",
  "requestId": "12345"
}
```

**Response**:
```json
{
  "message": "User verified successfully"
}
```

**Status**: ✅ Implemented in RegistrationService

---

### 4. Update Password

**Endpoint**: `PUT /user/password`

**Description**: Update user password

**Request**:
```json
{
  "newPassword": "newSecurePassword123",
  "confirmPassword": "newSecurePassword123"
}
```

**Response**:
```json
{
  "message": "Password updated successfully"
}
```

**Status**: ✅ Implemented in UserAuthRepository

---

## API Summary

| Endpoint | Service | Repository | Status |
|----------|---------|------------|--------|
| /authentication | AuthenticationService | UserAuthRepository | ✅ |
| /registration | RegistrationService | UserAuthRepository | ✅ |
| /registration/user | RegistrationService | UserAuthRepository | ✅ |
| /user/password | - | UserAuthRepository | ✅ |

---

## Error Responses

| Status Code | Error | Description |
|-------------|-------|-------------|
| 400 | Bad Request | Invalid credentials or payload |
| 401 | Unauthorized | Authentication failed |
| 403 | Forbidden | Account locked or disabled |
| 500 | Server Error | Internal server error |
