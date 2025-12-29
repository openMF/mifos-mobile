# QR Code - Feature Specification

> **Purpose**: QR code generation, scanning, and import for beneficiary data exchange
> **User Value**: Quick and error-free beneficiary data transfer via QR codes
> **Last Updated**: 2025-12-29
> **Status**: Production Design

---

## 1. Overview

### 1.1 Feature Summary
The QR Code feature enables users to share and receive beneficiary account information through QR codes. Users can generate QR codes containing their account details for others to scan, scan QR codes to add new beneficiaries, or import QR codes from saved images. This provides a quick, error-free method for exchanging financial account information without manual data entry.

### 1.2 User Stories
- As a user, I want to generate a QR code with my account details to share with others
- As a user, I want to scan a QR code to quickly add a new beneficiary
- As a user, I want to import a QR code from my photo gallery
- As a user, I want clear instructions on how to use QR code scanning
- As a user, I want to be warned about security considerations when scanning QR codes
- As a user, I want to see clear error messages when QR code parsing fails

### 1.3 Design Principles
- **Simplicity**: One-tap access to QR scanning and generation
- **Security**: Clear warnings about scanning unknown QR codes
- **Reliability**: Robust error handling for invalid QR formats
- **Cross-Platform**: Works on Android, iOS, Desktop, and Web

---

## 2. Screen Layout

### 2.1 QR Code Display Screen

```
+-------------------------------------------------------------+
|  <- QR Code                                                  |
+-------------------------------------------------------------+
|                                                              |
|                    Scan Your QR                              |
|                                                              |
|            +-----------------------------+                   |
|                                                              |
|     Show this QR code to anyone who wants to send            |
|     you money. They can scan it to add you as a              |
|     beneficiary.                                             |
|                                                              |
|            +-----------------------------+                   |
|            |                             |                   |
|            |    +-----+     +-----+      |                   |
|            |    |     |     |     |      |                   |
|            |    +-----+     +-----+      |                   |
|            |                             |                   |
|            |         QR CODE             |                   |
|            |         IMAGE               |                   |
|            |                             |                   |
|            |    +-----+                  |                   |
|            |    |     |                  |                   |
|            |    +-----+                  |                   |
|            |                             |                   |
|            +-----------------------------+                   |
|                                                              |
|     Align the code within the frame for automatic            |
|     scanning. Ensure good lighting.                          |
|                                                              |
|                                                              |
|                                                              |
|                                                              |
|              Generated on Dec 29, 2025                       |
|                                                              |
|  [Powered by Mifos]                                          |
+-------------------------------------------------------------+
```

### 2.2 QR Code Reader Screen

```
+-------------------------------------------------------------+
|  <- QR Code                                                  |
+-------------------------------------------------------------+
|                                                              |
|     Align the QR code within the frame to scan it            |
|                                                              |
|  +-------------------------------------------------------+   |
|  |                                                       |   |
|  |   +--+                                         +--+   |   |
|  |   |  |                                         |  |   |   |
|  |   +                                             +    |   |
|  |                                                       |   |
|  |                                                       |   |
|  |                  CAMERA VIEWFINDER                    |   |
|  |                                                       |   |
|  |                                                       |   |
|  |   +                                             +    |   |
|  |   |  |                                         |  |   |   |
|  |   +--+                                         +--+   |   |
|  |                                                       |   |
|  |   +-----------------------------------------------+   |   |
|  |   |  (!) Warning                                   |   |   |
|  |   |                                               |   |   |
|  |   |  Only scan QR codes from sources you trust.   |   |   |
|  |   |  Verify the sender's identity before          |   |   |
|  |   |  making any transfer.                         |   |   |
|  |   +-----------------------------------------------+   |   |
|  |                                                       |   |
|  +-------------------------------------------------------+   |
|                                                              |
|  +-------------------------------------------------------+   |
|  |              UPLOAD QR FROM GALLERY                    |   |
|  +-------------------------------------------------------+   |
|                                                              |
|  [Powered by Mifos]                                          |
+-------------------------------------------------------------+
```

### 2.3 QR Code Import Screen

```
+-------------------------------------------------------------+
|  <- QR Code                                                  |
+-------------------------------------------------------------+
|                                                              |
|  +-------------------------------------------------------+   |
|  |                                                       |   |
|  |                                                       |   |
|  |                                                       |   |
|  |                                                       |   |
|  |                   IMAGE PICKER                        |   |
|  |                   / CROPPER                           |   |
|  |                                                       |   |
|  |           (Platform-specific implementation)          |   |
|  |                                                       |   |
|  |                                                       |   |
|  |                                                       |   |
|  |                                                       |   |
|  +-------------------------------------------------------+   |
|                                                              |
|  [Powered by Mifos]                                          |
+-------------------------------------------------------------+
```

### 2.4 Error Dialog

```
+-----------------------------------------------+
|                                               |
|          Invalid QR Code                      |
|                                               |
|   The scanned QR code does not contain        |
|   valid beneficiary information.              |
|                                               |
|                    [ OK ]                     |
|                                               |
+-----------------------------------------------+
```

---

## 3. Sections Table

| Section | Description | Components |
|---------|-------------|------------|
| QR Display | Shows generated QR code | QR image, instructions, date stamp |
| QR Reader | Camera-based QR scanning | Camera viewfinder, corner markers, warning card |
| QR Import | Gallery-based QR import | Image picker, cropper, proceed button |
| Warning Card | Security notice overlay | Warning icon, title, description |
| Error Dialog | Invalid QR notification | Error message, dismiss button |
| Navigation Bar | Screen header | Back button, title |
| Footer | Branding | Mifos powered card |

---

## 4. User Interactions Table

| Interaction | Component | Action | Result |
|-------------|-----------|--------|--------|
| Tap back button | Top bar | Navigate back | Returns to previous screen |
| Camera scan | QR Reader | Auto-detect QR | Parses and navigates to beneficiary form |
| Tap "Upload QR from Gallery" | Button | Open import screen | Navigates to QrCodeImportScreen |
| Select image | Image picker | Choose gallery image | Loads image for QR decoding |
| Tap proceed/confirm | Import screen | Decode QR from image | Parses and navigates to beneficiary form |
| Dismiss error dialog | Dialog button | Close dialog | Returns to scanning state |
| Request camera permission | System dialog | Grant/deny | Enables scanner or shows permission denied UI |
| Tap "Open Settings" | Permission denied | Open system settings | Navigates to app permission settings |

---

## 5. State Model

### 5.1 QrCodeReaderState

```kotlin
internal data class QrCodeReaderState(
    val dialogState: DialogState?,
) {
    sealed interface DialogState {
        data class Error(val message: StringResource) : DialogState
    }
}
```

### 5.2 QrCodeReaderAction

```kotlin
sealed interface QrCodeReaderAction {
    data class ScanQrCode(val data: String) : QrCodeReaderAction
    data object OnNavigateToUpload : QrCodeReaderAction
    data object OnNavigate : QrCodeReaderAction
    data object OnDismiss : QrCodeReaderAction
}
```

### 5.3 QrCodeReaderEvent

```kotlin
sealed interface QrCodeReaderEvent {
    data object Navigate : QrCodeReaderEvent
    data object NavigateToUploadQr : QrCodeReaderEvent
    data class NavigateToBeneficiary(
        val beneficiary: Beneficiary,
        val beneficiaryState: BeneficiaryState = BeneficiaryState.CREATE_QR,
    ) : QrCodeReaderEvent
}
```

### 5.4 QrCodeDisplayState

```kotlin
data class QrCodeDisplayState(
    val option: String = "",
    val qrArgs: String? = null,
    val viewState: QrViewState = QrViewState.Loading,
) {
    sealed interface QrViewState {
        data object Loading : QrViewState
        data class Content(val data: String) : QrViewState {
            val options: QrOptions  // QR styling options
        }
    }
}
```

### 5.5 QrCodeDisplayAction

```kotlin
sealed interface QrCodeDisplayAction {
    data object OnNavigate : QrCodeDisplayAction
}
```

### 5.6 QrCodeDisplayEvent

```kotlin
sealed interface QrCodeDisplayEvent {
    data object Navigate : QrCodeDisplayEvent
}
```

### 5.7 QrCodeImportState

```kotlin
internal data class QrCodeImportState(
    val qrCodeResult: String? = null,
    val dialogState: DialogState?,
) {
    sealed interface DialogState {
        data class Error(val message: StringResource) : DialogState
        data object Loading : DialogState
    }
}
```

### 5.8 QrCodeImportAction

```kotlin
sealed interface QrCodeImportAction {
    data object OnNavigate : QrCodeImportAction
    data class Proceed(val bitmap: ImageBitmap) : QrCodeImportAction
    data object DismissDialog : QrCodeImportAction
}
```

### 5.9 QrCodeImportEvent

```kotlin
sealed interface QrCodeImportEvent {
    data object Navigate : QrCodeImportEvent
    data class OpenBeneficiaryApplication(
        val beneficiary: Beneficiary,
        val beneficiaryState: BeneficiaryState,
    ) : QrCodeImportEvent
}
```

---

## 6. Navigation Routes

| Route | Class | Parameters | Purpose |
|-------|-------|------------|---------|
| QrGraphRoute | data object | None | QR feature navigation graph |
| QrCodeDisplayRoute | data class | qrString: String | Display generated QR code |
| QrCodeReaderRoute | data object | None | Camera-based QR scanning |
| QrCodeImportRoute | data object | None | Gallery-based QR import |

### 6.1 Navigation Graph Structure

```
QrGraphRoute
    |
    +-- QrCodeDisplayRoute (startDestination)
    |
    +-- QrCodeReaderRoute
    |       |
    |       +-- navigateToQrImportScreen
    |       +-- openBeneficiaryApplication
    |
    +-- QrCodeImportRoute
            |
            +-- openBeneficiaryApplication
```

---

## 7. API Requirements

| Endpoint | Method | Purpose | Priority |
|----------|--------|---------|----------|
| None | - | This is a local-only feature | - |

**Note**: The QR Code feature operates entirely locally without any API calls. QR codes are:
- Generated locally using JSON serialization of Beneficiary data
- Decoded locally using platform-specific QR code decoders
- Camera permission is the only system-level requirement

---

## 8. QR Code Data Format

### 8.1 Beneficiary JSON Schema

```json
{
  "id": null,
  "name": "string | null",
  "officeName": "string | null",
  "clientName": "string",
  "accountType": {
    "id": 2,
    "code": "accountType.savings",
    "value": "Savings Account"
  },
  "accountNumber": "SA-0001234567",
  "transferLimit": null
}
```

### 8.2 Required Fields for QR Generation

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| clientName | String | Yes | Name of the account holder |
| accountNumber | String | Yes | Account number |
| accountType | AccountType | Yes | Type of account (savings, loan, etc.) |
| officeName | String | Yes | Name of the office/branch |

### 8.3 QR Generation Function

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

### 8.4 QR Code Styling (Display)

```kotlin
val shapes = QrShapes(
    code = QrCodeShape.Default,
    lightPixel = QrPixelShape.circle(),
    darkPixel = QrPixelShape.circle(),
    ball = QrBallShape.roundCorners(0.2f),
    frame = QrFrameShape.roundCorners(0.2f),
)

val colors = QrColors(
    light = Color(0xFFFFFFFF),  // White
    dark = Color(0xFF0673BA),   // Mifos Blue
    ball = Color(0xFF6e6e6e),   // Gray
    frame = Color(0xFF6e6e6e),  // Gray
)

val errorCorrectionLevel = QrErrorCorrectionLevel.Medium
```

---

## 9. Edge Cases and Error Handling

| Scenario | Detection | UI Behavior | Recovery |
|----------|-----------|-------------|----------|
| Non-JSON QR code | String doesn't start/end with { } | Show "Invalid QR Code" error dialog | Dismiss and continue scanning |
| Invalid JSON structure | JSON parsing exception | Show "Invalid QR Code" error dialog | Dismiss and continue scanning |
| Missing required fields | Beneficiary deserialization fails | Show "Error" dialog | Dismiss and continue scanning |
| Camera permission denied | CameraPermissionStatus.Denied | Show permission denied UI with "Open Settings" button | User grants permission manually |
| Empty QR code | Null or empty string result | Show "Invalid QR Code" error dialog | Dismiss and continue scanning |
| Image decode failure | decodeQrCode returns null | Show "Invalid QR Code" error dialog | User can retry with different image |
| No camera available | Platform limitation | Fall back to import option | Use "Upload QR from Gallery" |

---

## 10. Platform-Specific Implementations

| Component | Android | iOS | Desktop | Web |
|-----------|---------|-----|---------|-----|
| QrCodeScanner | CameraX + ML Kit | AVFoundation | Not supported | MediaDevices API |
| decodeQrCode | ML Kit BarcodeScanning | CoreImage | ZXing | ZXing-JS |
| CameraPermissionState | Accompanist Permissions | NSCameraUsageDescription | N/A | Permissions API |
| QrCodeImagePicker | Photo Picker | PHPicker | File Chooser | File Input |

---

## 11. Dependency Injection

```kotlin
val QrModule = module {
    viewModelOf(::QrCodeImportViewModel)
    viewModelOf(::QrCodeReaderViewModel)
    viewModelOf(::QrCodeDisplayViewModel)
}
```

---

## 12. Accessibility

| Feature | Implementation |
|---------|----------------|
| Screen Reader | contentDescription on QR image, buttons, and warning text |
| Focus Navigation | Logical tab order through scanner, warning, and button |
| Color Contrast | WCAG AA compliant (4.5:1 minimum) |
| Touch Targets | 48dp minimum for all interactive elements |
| Camera Alternative | "Upload from Gallery" option for accessibility |

---

## 13. Security Considerations

| Feature | Implementation |
|---------|----------------|
| QR Validation | Only parse JSON-formatted Beneficiary data |
| Warning Display | Prominent security warning on scanner screen |
| No External URLs | QR codes only contain account data, not executable content |
| Local Processing | All QR generation/decoding happens on-device |
| Permission Handling | Camera access only requested when needed |

---

## Changelog

| Date | Change |
|------|--------|
| 2025-12-29 | Initial production-level specification |
