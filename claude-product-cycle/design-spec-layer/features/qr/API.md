# QR Code - API Reference

> **Feature Type**: Local-Only Processing
> **Network Calls**: None
> **Last Updated**: 2025-12-29

---

## Overview

The QR Code feature is a **local-only feature** that does not make any API calls to the Mifos backend. All QR code generation, scanning, and decoding operations happen entirely on the client device.

---

## Local-Only Architecture

### Why No API Calls?

1. **Privacy**: Beneficiary data is encoded/decoded locally without server round-trips
2. **Speed**: Instant QR generation and scanning without network latency
3. **Offline Support**: Feature works without internet connectivity
4. **Simplicity**: No server-side infrastructure needed for QR operations

---

## QR Code Data Format

### Beneficiary JSON Schema

The QR code encodes a `Beneficiary` object as a JSON string:

```json
{
  "id": null,
  "name": "string | null",
  "officeName": "Office Name",
  "clientName": "John Doe",
  "accountType": {
    "id": 2,
    "code": "accountType.savings",
    "value": "Savings Account"
  },
  "accountNumber": "SA-0001234567",
  "transferLimit": null
}
```

### Field Definitions

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| id | Long? | No | Beneficiary ID (null for new) |
| name | String? | No | Display name |
| officeName | String? | Yes | Office/branch name |
| clientName | String? | Yes | Account holder name |
| accountType | AccountType? | Yes | Account type object |
| accountNumber | String? | Yes | Account number |
| transferLimit | Double? | No | Maximum transfer amount |

### AccountType Object

| Field | Type | Description |
|-------|------|-------------|
| id | Int? | Type identifier (e.g., 2 for savings) |
| code | String? | Type code (e.g., "accountType.savings") |
| value | String? | Display value (e.g., "Savings Account") |

---

## Kotlin Data Classes

### Beneficiary

```kotlin
@Serializable
@Parcelize
data class Beneficiary(
    val id: Long? = null,
    val name: String? = null,
    val officeName: String? = null,
    val clientName: String? = null,
    val accountType: AccountType? = null,
    val accountNumber: String? = null,
    val transferLimit: Double? = null,
) : Parcelable
```

### AccountType

```kotlin
@Serializable
@Parcelize
data class AccountType(
    val id: Int? = null,
    val code: String? = null,
    val value: String? = null,
) : Parcelable
```

---

## QR Code Generation

### Function Signature

```kotlin
fun getAccountDetailsInString(
    clientName: String,
    accountNumber: String,
    accountType: AccountType,
    officeName: String,
): String
```

### Implementation

```kotlin
fun getAccountDetailsInString(
    clientName: String,
    accountNumber: String,
    accountType: AccountType,
    officeName: String,
): String {
    val payload = Beneficiary(
        clientName = clientName,
        accountNumber = accountNumber,
        accountType = accountType,
        officeName = officeName,
    )
    return Json.encodeToString(payload)
}
```

### Example Output

Input:
- clientName: "Maria Garcia"
- accountNumber: "SA-0005678901"
- accountType: AccountType(id=2, code="accountType.savings", value="Savings Account")
- officeName: "Head Office"

Output (JSON String):
```json
{"id":null,"name":null,"officeName":"Head Office","clientName":"Maria Garcia","accountType":{"id":2,"code":"accountType.savings","value":"Savings Account"},"accountNumber":"SA-0005678901","transferLimit":null}
```

---

## QR Code Decoding

### Function Signature

```kotlin
expect fun decodeQrCode(bitmap: ImageBitmap): String?
```

### Platform Implementations

| Platform | Implementation |
|----------|----------------|
| Android | ML Kit Barcode Scanning |
| iOS | CoreImage CIDetector |
| Desktop | ZXing library |
| Web (JS) | ZXing-JS library |
| Web (WASM) | ZXing-JS library |

### Decoding Flow

```kotlin
// 1. Decode QR from image
val result = decodeQrCode(bitmap)?.trim()

// 2. Validate JSON format
val isJsonWrapped = result?.startsWith("{") == true && result.endsWith("}")
if (!isJsonWrapped) {
    showError(Res.string.feature_qr_invalid)
    return
}

// 3. Parse to Beneficiary
try {
    val beneficiary = Json.decodeFromString<Beneficiary>(result)
    // Navigate to beneficiary application
} catch (e: Exception) {
    showError(Res.string.feature_qr_invalid)
}
```

---

## QR Code Styling Options

### QR Display Configuration

```kotlin
val options = QrOptions(
    shapes = QrShapes(
        code = QrCodeShape.Default,
        lightPixel = QrPixelShape.circle(),
        darkPixel = QrPixelShape.circle(),
        ball = QrBallShape.roundCorners(0.2f),
        frame = QrFrameShape.roundCorners(0.2f),
    ),
    colors = QrColors(
        light = QrBrush.solid(Color(0xFFFFFFFF)),  // White
        dark = QrBrush.solid(Color(0xFF0673BA)),   // Mifos Blue
        ball = QrBrush.solid(Color(0xFF6e6e6e)),   // Gray
        frame = QrBrush.solid(Color(0xFF6e6e6e)),  // Gray
    ),
    errorCorrectionLevel = QrErrorCorrectionLevel.Medium,
)
```

### Color Palette

| Element | Color Code | Usage |
|---------|------------|-------|
| Light pixels | #FFFFFF | Background modules |
| Dark pixels | #0673BA | Data modules (Mifos brand blue) |
| Ball | #6E6E6E | Corner ball elements |
| Frame | #6E6E6E | Corner frame elements |

---

## Supported Code Types

```kotlin
enum class CodeType {
    Codabar,
    Code39,
    Code93,
    Code128,
    EAN8,
    EAN13,
    ITF,
    UPCE,
    Aztec,
    DataMatrix,
    PDF417,
    QR,  // Used for beneficiary data
}
```

**Note**: Only `CodeType.QR` is used for beneficiary data exchange.

---

## Camera Permission Handling

### Permission States

```kotlin
enum class CameraPermissionStatus {
    Denied,
    Granted,
}
```

### Permission Interface

```kotlin
interface CameraPermissionState {
    val status: CameraPermissionStatus
    fun requestCameraPermission()
    fun goToSettings()
}
```

### Permission Flow

```kotlin
@Composable
fun QrScannerWithPermissions(
    types: List<CodeType>,
    onScanned: (String) -> Boolean,
    permissionDeniedContent: @Composable (CameraPermissionState) -> Unit,
) {
    val permissionState = rememberCameraPermissionState()

    LaunchedEffect(Unit) {
        if (permissionState.status == CameraPermissionStatus.Denied) {
            permissionState.requestCameraPermission()
        }
    }

    if (permissionState.status == CameraPermissionStatus.Granted) {
        QrCodeScanner(types = types, onScanned = onScanned)
    } else {
        permissionDeniedContent(permissionState)
    }
}
```

---

## Error Handling

### Error Types

| Error | Cause | Message Resource |
|-------|-------|------------------|
| Invalid QR | Non-JSON content or invalid format | `feature_qr_invalid` |
| Parse Error | JSON parsing exception | `feature_qr_error` |
| Decode Failure | Unable to read QR from image | `feature_qr_invalid` |

### Validation Rules

1. **JSON Wrapper Check**: Content must start with `{` and end with `}`
2. **Deserialization**: Must successfully parse to `Beneficiary` class
3. **Null Handling**: All fields are nullable, but core fields should be present

---

## Integration with Beneficiary Feature

### Navigation After Successful Scan

```kotlin
// From QrCodeReaderViewModel
QrCodeReaderEvent.NavigateToBeneficiary(
    beneficiary = beneficiary,
    beneficiaryState = BeneficiaryState.CREATE_QR,
)

// From QrCodeImportViewModel
QrCodeImportEvent.OpenBeneficiaryApplication(
    beneficiary = beneficiary,
    beneficiaryState = BeneficiaryState.CREATE_QR,
)
```

### BeneficiaryState Enum

```kotlin
enum class BeneficiaryState {
    CREATE,       // Manual creation
    CREATE_QR,    // Created from QR scan
    UPDATE,       // Editing existing
    VIEW,         // View only
}
```

---

## Dependencies

### External Libraries

| Library | Purpose | Platform |
|---------|---------|----------|
| qrose | QR code generation | All (Compose Multiplatform) |
| ML Kit Barcode | QR decoding | Android |
| CoreImage | QR decoding | iOS |
| ZXing | QR decoding | Desktop |
| ZXing-JS | QR decoding | Web |

### Core Modules

| Module | Purpose |
|--------|---------|
| core:qrcode | Platform-specific QR utilities |
| core:model | Beneficiary, AccountType DTOs |
| core:ui | Base ViewModel, EventsEffect |
| core:designsystem | UI components, theming |

---

## Sample QR Code Content

### Minimal Valid QR

```json
{"clientName":"Test User","accountNumber":"123456","accountType":{"id":2},"officeName":"Main"}
```

### Full QR with All Fields

```json
{
  "id": 1001,
  "name": "Maria's Savings",
  "officeName": "Head Office",
  "clientName": "Maria Garcia",
  "accountType": {
    "id": 2,
    "code": "accountType.savings",
    "value": "Savings Account"
  },
  "accountNumber": "SA-0005678901",
  "transferLimit": 10000.00
}
```

---

## Changelog

| Date | Change |
|------|--------|
| 2025-12-29 | Initial documentation for local-only QR feature |
