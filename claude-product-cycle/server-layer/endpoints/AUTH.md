# Authentication Endpoints

> **Category**: Authentication
> **Endpoints**: 3
> **Service**: `AuthenticationService`, `RegistrationService`

---

## POST /authentication

**Purpose**: Login with username and password

**Service**: `AuthenticationService.authenticate()`

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

**DTO**: `User`
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

---

## POST /registration

**Purpose**: Register new client

**Service**: `RegistrationService.register()`

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

**DTO**: `RegisterPayload`

---

## POST /registration/user

**Purpose**: Verify OTP for registration

**Service**: `RegistrationService.verifyOtp()`

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

---

## Related

- Design Layer: [auth/API.md](../../design-spec-layer/features/auth/API.md)
- Patterns: [CLIENT_PATTERNS.md](../CLIENT_PATTERNS.md)
- Errors: [ERROR_HANDLING.md](../ERROR_HANDLING.md)
