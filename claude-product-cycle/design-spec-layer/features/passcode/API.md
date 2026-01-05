# Passcode - API Reference

## Overview

> **Important**: The Passcode feature is a **local-only feature** with **no API calls** to the backend server. All passcode data is stored and validated locally on the device.

---

## No Backend API Required

The Passcode feature operates entirely offline and does not communicate with any remote server. This design choice provides:

1. **Faster Authentication**: No network latency for passcode verification
2. **Offline Capability**: Works without internet connectivity
3. **Privacy**: Passcode never transmitted over the network
4. **Security**: Reduces attack surface by keeping sensitive data local

---

## Local Storage Mechanism

### Storage Library
The feature uses the `multiplatform-settings` library (com.russhwolf.settings) for cross-platform local storage.

### Data Model

```kotlin
@Serializable
data class PasscodePreferencesProto(
    val passcode: String,
    val hasPasscode: Boolean,
) {
    companion object {
        val DEFAULT = PasscodePreferencesProto(
            passcode = "",
            hasPasscode = false
        )
    }
}
```

### Storage Key
```kotlin
private const val PASSCODE_INFO_KEY = "passcodeInfo"
```

---

## Data Access Layer

### PasscodePreferencesDataSource

Low-level data access for passcode preferences.

```kotlin
class PasscodePreferencesDataSource(
    private val settings: Settings,
    private val dispatcher: CoroutineDispatcher,
) {
    // Reactive streams for passcode data
    val passcode: Flow<String>
    val hasPasscode: Flow<Boolean>

    // Update passcode settings
    suspend fun updatePasscodeSettings(passcodePreferences: PasscodePreferencesProto)

    // Clear all passcode data
    suspend fun clearInfo()
}
```

### PasscodeManager

Higher-level manager with StateFlow caching.

```kotlin
class PasscodeManager(
    private val source: PasscodePreferencesDataSource,
    dispatcher: CoroutineDispatcher,
) {
    // Cached passcode value (eagerly shared)
    val getPasscode: StateFlow<String>

    // Whether a passcode is currently set
    val hasPasscode: StateFlow<Boolean>

    // Save a new passcode
    suspend fun savePasscode(passcode: String)

    // Clear the stored passcode
    suspend fun clearPasscode()
}
```

### UserPreferencesRepository Integration

The feature module uses `UserPreferencesRepository` from `core:datastore`:

```kotlin
interface UserPreferencesRepository {
    val passcode: Flow<String>
    suspend fun setPasscode(passcode: String)
}
```

---

## Storage Operations

### Save Passcode

**When**: After successful passcode confirmation (Set + Confirm match)

```kotlin
// In PasscodeViewModel
userPreferencesRepository.setPasscode(confirm)

// Internally calls
passcodeManager.savePasscode(passcode)

// Which updates settings
settings.encodeValue(
    key = PASSCODE_INFO_KEY,
    serializer = PasscodePreferencesProto.serializer(),
    value = PasscodePreferencesProto(
        passcode = passcode,
        hasPasscode = passcode.isNotEmpty()
    )
)
```

### Read Passcode

**When**: On VerifyPasscodeScreen initialization

```kotlin
// In VerifyPasscodeViewModel
userPreferencesRepository.passcode.collect { passcode ->
    mutableStateFlow.update {
        it.copy(storedPasscode = passcode)
    }
}
```

### Clear Passcode

**When**: User logs out or resets app

```kotlin
passcodeManager.clearPasscode()

// Which calls
settings.remove(PASSCODE_INFO_KEY)
```

---

## Platform-Specific Storage

The `multiplatform-settings` library provides platform-specific implementations:

| Platform | Storage Backend |
|----------|-----------------|
| Android | SharedPreferences |
| iOS | NSUserDefaults |
| Desktop (JVM) | Java Preferences API |
| Web (JS/WASM) | localStorage |

---

## Security Considerations

### Current Implementation
- Passcode is stored as plain text in preferences
- Validation is done by string comparison

### Recommended Enhancements
1. **Hash the passcode** before storage (e.g., using bcrypt or PBKDF2)
2. **Encrypt preferences** using platform-specific secure storage
3. **Add rate limiting** for failed attempts
4. **Implement lockout** after multiple failed attempts
5. **Consider biometric** integration as alternative

---

## Error Responses

Since this is a local-only feature, there are no HTTP error responses. Local errors include:

| Error Type | Cause | Handling |
|------------|-------|----------|
| Storage Read Error | Corrupted preferences | Return default values |
| Storage Write Error | Disk full or permissions | Show error dialog, retry |
| Serialization Error | Schema mismatch | Clear and reset preferences |

---

## API Summary

| Operation | Layer | Method | Storage |
|-----------|-------|--------|---------|
| Set Passcode | PasscodeViewModel | setPasscode() | Local |
| Verify Passcode | VerifyPasscodeViewModel | compare with stored | Local |
| Get Passcode | UserPreferencesRepository | passcode flow | Local |
| Clear Passcode | PasscodeManager | clearPasscode() | Local |

---

## Related Files

| File | Purpose |
|------|---------|
| `libs/mifos-passcode/src/commonMain/kotlin/org/mifos/library/passcode/data/PasscodePreferencesDataSource.kt` | Low-level storage access |
| `libs/mifos-passcode/src/commonMain/kotlin/org/mifos/library/passcode/data/PasscodeManager.kt` | High-level passcode operations |
| `libs/mifos-passcode/src/commonMain/kotlin/org/mifos/library/passcode/model/PasscodePreferencesProto.kt` | Data model |
| `core/datastore/src/commonMain/kotlin/org/mifos/mobile/core/datastore/UserPreferencesRepository.kt` | Repository interface |
