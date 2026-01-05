# Notification - API Reference

---

## Endpoints Required

### 1. Get User Notification Registration ID

**Endpoint**: `GET /self/device/registration/client/{clientId}`

**Purpose**: Fetch the notification registration ID for a client to manage push notification settings

**Request**:
```
Headers:
  Authorization: Basic {token}
  Fineract-Platform-TenantId: {tenant}

Path Parameters:
  clientId: Long - The client's unique identifier
```

**Response**:
```json
{
    "id": 123
}
```

**Kotlin DTO**: Uses `NotificationUserDetail` from `core/model/entity/notification/`

**Status**: Implemented in `NotificationService`

---

### 2. Register Device for Notifications

**Endpoint**: `POST /self/device/registration`

**Purpose**: Register a device to receive push notifications for a client

**Request**:
```
Headers:
  Authorization: Basic {token}
  Fineract-Platform-TenantId: {tenant}
  Content-Type: application/json

Body:
{
    "clientId": 1,
    "registrationId": "fcm_token_string_here"
}
```

**Response**: `HttpResponse` (success/failure status)

**Kotlin DTO**: Uses `NotificationRegisterPayload` from `core/model/entity/notification/`

**Status**: Implemented in `NotificationService`

---

### 3. Update Notification Registration

**Endpoint**: `PUT /self/device/registration/{id}`

**Purpose**: Update an existing device registration (e.g., when FCM token refreshes)

**Request**:
```
Headers:
  Authorization: Basic {token}
  Fineract-Platform-TenantId: {tenant}
  Content-Type: application/json

Path Parameters:
  id: Long - The registration ID to update

Body:
{
    "clientId": 1,
    "registrationId": "new_fcm_token_string"
}
```

**Response**: `HttpResponse` (success/failure status)

**Status**: Implemented in `NotificationService`

---

## Local Database Operations

The notification feature primarily uses local database storage for notifications. Notifications are received via push notifications (FCM) and stored locally.

### Database Entity

```kotlin
@Entity(tableName = "mifos_notification")
data class MifosNotificationEntity(
    @PrimaryKey(autoGenerate = false)
    val timeStamp: Long,
    val msg: String?,
    val read: Boolean?,
)
```

### Repository Operations

| Operation | Method | Description |
|-----------|--------|-------------|
| Load all | `loadNotifications()` | Get all stored notifications |
| Get unread count | `getUnReadNotificationCount()` | Count unread notifications |
| Save notification | `saveNotification(notification)` | Persist a notification |
| Delete old | `deleteOldNotifications()` | Remove notifications > 30 days old |
| Update read status | `updateReadStatus(notification, isRead)` | Mark as read/unread |

---

## API Summary

| Endpoint | Service | Repository | Status |
|----------|---------|------------|--------|
| `/device/registration/client/{id}` | NotificationService | UserDetailRepository | Implemented |
| `/device/registration` | NotificationService | UserDetailRepository | Implemented |
| `/device/registration/{id}` | NotificationService | UserDetailRepository | Implemented |
| Local DB (notifications) | - | NotificationRepository | Partial* |

> *Note: Room DAO integration is currently commented out in the repository implementation

---

## Kotlin Implementation

### Service (NotificationService.kt)

```kotlin
interface NotificationService {
    @GET(ApiEndPoints.DEVICE + "/registration/client/{clientId}")
    fun getUserNotificationId(
        @Path("clientId") clientId: Long
    ): Flow<NotificationUserDetail>

    @POST(ApiEndPoints.DEVICE + "/registration")
    suspend fun registerNotification(
        @Body payload: NotificationRegisterPayload?
    ): HttpResponse

    @PUT(ApiEndPoints.DEVICE + "/registration/{id}")
    suspend fun updateRegisterNotification(
        @Path("id") id: Long,
        @Body payload: NotificationRegisterPayload?,
    ): HttpResponse
}
```

### Repository Interface (NotificationRepository.kt)

```kotlin
interface NotificationRepository {
    fun loadNotifications(): Flow<DataState<List<MifosNotification>>>

    fun getUnReadNotificationCount(): Flow<DataState<Int>>

    suspend fun saveNotification(notification: MifosNotification)

    suspend fun deleteOldNotifications()

    suspend fun updateReadStatus(notification: MifosNotification, isRead: Boolean)
}
```

### Repository Implementation (NotificationRepositoryImp.kt)

```kotlin
class NotificationRepositoryImp(
    // private val notificationDao: MifosNotificationDao,
    private val ioDispatcher: CoroutineDispatcher,
) : NotificationRepository {

    override fun loadNotifications(): Flow<DataState<List<MifosNotification>>> {
        // TODO: Uncomment when Room DAO is enabled
        // return notificationDao.getNotifications()
        //     .map { it.map { it.toModel() } }
        //     .flowOn(ioDispatcher)
        return flowOf(DataState.Success(emptyList<MifosNotification>()))
            .flowOn(ioDispatcher)
    }

    override fun getUnReadNotificationCount(): Flow<DataState<Int>> {
        // return notificationDao.getUnreadNotificationsCount().flowOn(ioDispatcher)
        return flowOf(DataState.Success(0))
            .flowOn(ioDispatcher)
    }

    override suspend fun saveNotification(notification: MifosNotification) {
        // withContext(ioDispatcher) {
        //     notificationDao.saveNotification(notification.toEntity())
        // }
    }

    override suspend fun deleteOldNotifications() {
        // return withContext(ioDispatcher) {
        //     val thirtyDaysInMillis = 2592000000L
        //     val cutoffTime = System.currentTimeMillis() - thirtyDaysInMillis
        //     notificationDao.deleteOldNotifications(cutoffTime)
        // }
    }

    override suspend fun updateReadStatus(notification: MifosNotification, isRead: Boolean) {
        // withContext(ioDispatcher) {
        //     notificationDao.updateReadStatus(notification.timeStamp, isRead)
        // }
    }
}
```

---

## Kotlin DTOs

### NotificationUserDetail

```kotlin
package org.mifos.mobile.core.model.entity.notification

import kotlinx.serialization.Serializable

@Serializable
data class NotificationUserDetail(
    val id: Int = 0,
)
```

### NotificationRegisterPayload

```kotlin
package org.mifos.mobile.core.model.entity.notification

data class NotificationRegisterPayload(
    val clientId: Long,
    val registrationId: String,
)
```

### MifosNotification (Domain Model)

```kotlin
package org.mifos.mobile.core.model.entity

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize

@Parcelize
@Serializable
data class MifosNotification(
    val timeStamp: Long,
    val msg: String? = null,
    val read: Boolean? = null,
) : Parcelable {
    fun isRead(): Boolean {
        return read == true
    }
}
```

### MifosNotificationEntity (Database Entity)

```kotlin
package org.mifos.mobile.core.database.entity

import org.mifos.mobile.core.database.Entity
import org.mifos.mobile.core.database.PrimaryKey

@Entity(tableName = "mifos_notification")
data class MifosNotificationEntity(
    @PrimaryKey(autoGenerate = false)
    val timeStamp: Long,
    val msg: String?,
    val read: Boolean?,
)
```

---

## API Endpoints Reference

| Constant | Value |
|----------|-------|
| `ApiEndPoints.DEVICE` | `"device"` |

Full endpoints:
- `device/registration/client/{clientId}` - GET user notification ID
- `device/registration` - POST register notification
- `device/registration/{id}` - PUT update registration

---

## Notes

- Notifications are received via Firebase Cloud Messaging (FCM) and stored locally
- The notification list is read from local database, not fetched from server
- Device registration endpoints are used for managing push notification tokens
- The current implementation has Room DAO integration commented out, returning empty data
- Timestamps are stored as Unix milliseconds for sorting and cleanup operations
- Notifications older than 30 days are automatically deleted on screen load
