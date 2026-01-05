# Notification Endpoints

> **Category**: Notification
> **Endpoints**: 3
> **Service**: `NotificationService`

---

## GET /device/registration/client/{clientId}

**Purpose**: Get notification registration ID for client

**Service**: `NotificationService.getUserNotificationId(clientId)`

**Response**:
```json
{
    "id": 123,
    "clientId": 1,
    "registrationId": "fcm_token_here"
}
```

**DTO**: `NotificationRegisterPayload`

---

## POST /device/registration

**Purpose**: Register device for push notifications

**Service**: `NotificationService.registerNotification(payload)`

**Request**:
```json
{
    "clientId": 1,
    "registrationId": "fcm_token_here"
}
```

**Response**:
```json
{
    "resourceId": 123
}
```

**DTO**: `NotificationRegisterPayload`
```kotlin
@Serializable
data class NotificationRegisterPayload(
    val id: Long? = null,
    val clientId: Long? = null,
    val registrationId: String? = null,
)
```

---

## PUT /device/registration/{id}

**Purpose**: Update notification registration (refresh FCM token)

**Service**: `NotificationService.updateRegisterNotification(id, payload)`

**Request**:
```json
{
    "clientId": 1,
    "registrationId": "new_fcm_token_here"
}
```

**Response**:
```json
{
    "resourceId": 123,
    "changes": {
        "registrationId": "new_fcm_token_here"
    }
}
```

---

## Related

- Design Layer: [notification/API.md](../../design-spec-layer/features/notification/API.md)
- Patterns: [CLIENT_PATTERNS.md](../CLIENT_PATTERNS.md)
