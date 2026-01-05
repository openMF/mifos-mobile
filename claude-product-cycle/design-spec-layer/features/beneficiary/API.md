# Beneficiary - API Reference

> **Base URL**: `https://tt.mifos.community/fineract-provider/api/v1/self/`
> **Authentication**: Basic Auth with `Fineract-Platform-TenantId` header
> **Last Updated**: 2025-12-29

---

## Authentication Headers

All endpoints require:
```
Headers:
  Authorization: Basic {base64EncodedAuthenticationKey}
  Fineract-Platform-TenantId: {tenantId}
  Content-Type: application/json
```

---

## 1. Get Beneficiary List

### GET /self/beneficiaries/tpt

**Purpose**: Fetch all third-party transfer beneficiaries for the authenticated user

**Request**:
```
GET /self/beneficiaries/tpt
```

**Response**:
```json
[
  {
    "id": 5001,
    "name": "John Doe",
    "officeName": "Head Office",
    "clientName": "John Doe",
    "accountType": {
      "id": 2,
      "code": "accountType.savings",
      "value": "Savings Account"
    },
    "accountNumber": "SA-0001234567",
    "transferLimit": 10000.00
  },
  {
    "id": 5002,
    "name": "Jane Smith",
    "officeName": "Branch Office",
    "clientName": "Jane Smith",
    "accountType": {
      "id": 1,
      "code": "accountType.loan",
      "value": "Loan Account"
    },
    "accountNumber": "LA-0009876543",
    "transferLimit": 5000.00
  }
]
```

**Kotlin DTO**:
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

**Status**: Implemented in `BeneficiaryService.beneficiaryList()`

---

## 2. Get Beneficiary Template

### GET /self/beneficiaries/tpt/template

**Purpose**: Fetch account type options for beneficiary creation form

**Request**:
```
GET /self/beneficiaries/tpt/template
```

**Response**:
```json
{
  "accountTypeOptions": [
    {
      "id": 0,
      "code": "accountType.share",
      "value": "Share Account"
    },
    {
      "id": 1,
      "code": "accountType.loan",
      "value": "Loan Account"
    },
    {
      "id": 2,
      "code": "accountType.savings",
      "value": "Savings Account"
    }
  ]
}
```

**Kotlin DTO**:
```kotlin
@Serializable
@Parcelize
data class BeneficiaryTemplate(
    val accountTypeOptions: List<AccountTypeOption>? = null,
) : Parcelable

@Serializable
@Parcelize
data class AccountTypeOption(
    val id: Int? = null,
    val code: String? = null,
    val value: String? = null,
) : Parcelable
```

**Status**: Implemented in `BeneficiaryService.beneficiaryTemplate()`

---

## 3. Create Beneficiary

### POST /self/beneficiaries/tpt

**Purpose**: Create a new third-party transfer beneficiary

**Request**:
```json
POST /self/beneficiaries/tpt

{
  "locale": "en",
  "name": "John Doe",
  "accountNumber": "SA-0001234567",
  "accountType": 2,
  "transferLimit": 10000,
  "officeName": "Head Office"
}
```

**Request Payload DTO**:
```kotlin
@Serializable
@Parcelize
data class BeneficiaryPayload(
    val locale: String? = null,
    val name: String? = null,
    val accountNumber: String? = null,
    val accountType: Int? = 0,
    val transferLimit: Int? = 0,
    val officeName: String? = null,
) : Parcelable
```

**Request Fields**:
| Field | Type | Required | Description |
|-------|------|----------|-------------|
| locale | String | Yes | Locale for the request (e.g., "en") |
| name | String | Yes | Beneficiary's display name |
| accountNumber | String | Yes | Target account number |
| accountType | Int | Yes | Account type ID (0=Share, 1=Loan, 2=Savings) |
| transferLimit | Int | Yes | Maximum transfer amount allowed |
| officeName | String | Yes | Office name where the account is held |

**Response (Success)**:
```json
{
  "resourceId": 5003
}
```

**Response (Error)**:
```json
{
  "developerMessage": "The account number does not exist",
  "httpStatusCode": "400",
  "defaultUserMessage": "Account not found",
  "userMessageGlobalisationCode": "error.msg.beneficiary.account.not.found",
  "errors": []
}
```

**Status**: Implemented in `BeneficiaryService.createBeneficiary()`

---

## 4. Update Beneficiary

### PUT /self/beneficiaries/tpt/{beneficiaryId}

**Purpose**: Update an existing beneficiary's name and transfer limit

**Request**:
```json
PUT /self/beneficiaries/tpt/5001

{
  "name": "John Doe Updated",
  "transferLimit": 15000
}
```

**Request Payload DTO**:
```kotlin
@Serializable
data class BeneficiaryUpdatePayload(
    val name: String? = null,
    val transferLimit: Int = 0,
)
```

**Request Fields**:
| Field | Type | Required | Description |
|-------|------|----------|-------------|
| name | String | Yes | Updated beneficiary name |
| transferLimit | Int | Yes | Updated transfer limit |

**Note**: Account number, account type, and office name cannot be updated. Only name and transfer limit can be modified.

**Response (Success)**:
```json
{
  "resourceId": 5001,
  "changes": {
    "name": "John Doe Updated",
    "transferLimit": 15000
  }
}
```

**Response (Error)**:
```json
{
  "developerMessage": "Beneficiary not found with id: 9999",
  "httpStatusCode": "404",
  "defaultUserMessage": "Beneficiary not found",
  "userMessageGlobalisationCode": "error.msg.beneficiary.id.invalid",
  "errors": []
}
```

**Status**: Implemented in `BeneficiaryService.updateBeneficiary()`

---

## 5. Delete Beneficiary

### DELETE /self/beneficiaries/tpt/{beneficiaryId}

**Purpose**: Delete an existing beneficiary

**Request**:
```
DELETE /self/beneficiaries/tpt/5001
```

**Response (Success)**:
```json
{
  "resourceId": 5001
}
```

**Response (Error)**:
```json
{
  "developerMessage": "Beneficiary not found with id: 9999",
  "httpStatusCode": "404",
  "defaultUserMessage": "Beneficiary not found",
  "userMessageGlobalisationCode": "error.msg.beneficiary.id.invalid",
  "errors": []
}
```

**Status**: Implemented in `BeneficiaryService.deleteBeneficiary()`

---

## Kotlin Service Interface

```kotlin
interface BeneficiaryService {
    @GET(ApiEndPoints.BENEFICIARIES + "/tpt")
    fun beneficiaryList(): Flow<List<Beneficiary>>

    @GET(ApiEndPoints.BENEFICIARIES + "/tpt/template")
    fun beneficiaryTemplate(): Flow<BeneficiaryTemplate>

    @POST(ApiEndPoints.BENEFICIARIES + "/tpt")
    suspend fun createBeneficiary(@Body beneficiaryPayload: BeneficiaryPayload?): HttpResponse

    @PUT(ApiEndPoints.BENEFICIARIES + "/tpt/{beneficiaryId}")
    suspend fun updateBeneficiary(
        @Path("beneficiaryId") beneficiaryId: Long,
        @Body payload: BeneficiaryUpdatePayload?,
    ): HttpResponse

    @DELETE(ApiEndPoints.BENEFICIARIES + "/tpt/{beneficiaryId}")
    suspend fun deleteBeneficiary(@Path("beneficiaryId") beneficiaryId: Long): HttpResponse
}
```

---

## Kotlin Repository Interface

```kotlin
interface BeneficiaryRepository {

    fun beneficiaryTemplate(): Flow<DataState<BeneficiaryTemplate>>

    suspend fun createBeneficiary(beneficiaryPayload: BeneficiaryPayload?): DataState<String>

    suspend fun updateBeneficiary(
        beneficiaryId: Long?,
        payload: BeneficiaryUpdatePayload?,
    ): DataState<String>

    suspend fun deleteBeneficiary(beneficiaryId: Long?): DataState<String>

    fun beneficiaryList(): Flow<DataState<List<Beneficiary>>>
}
```

---

## Kotlin Repository Implementation

```kotlin
class BeneficiaryRepositoryImp(
    private val dataManager: DataManager,
    private val ioDispatcher: CoroutineDispatcher,
) : BeneficiaryRepository {

    override fun beneficiaryTemplate(): Flow<DataState<BeneficiaryTemplate>> = flow {
        try {
            dataManager.beneficiaryApi.beneficiaryTemplate()
                .collect { response ->
                    emit(DataState.Success(response))
                }
        } catch (exception: Exception) {
            emit(DataState.Error(exception))
        }
    }.flowOn(ioDispatcher)

    override suspend fun createBeneficiary(beneficiaryPayload: BeneficiaryPayload?): DataState<String> {
        return withContext(ioDispatcher) {
            try {
                val response = dataManager.beneficiaryApi.createBeneficiary(beneficiaryPayload)
                DataState.Success(response.bodyAsText())
            } catch (e: ClientRequestException) {
                val errorMessage = extractErrorMessage(e.response)
                DataState.Error(Exception(errorMessage), null)
            } catch (e: IOException) {
                DataState.Error(Exception("Network error: ${e.message ?: "Please check your connection"}"), null)
            } catch (e: ServerResponseException) {
                DataState.Error(Exception("Server error: ${e.message}"), null)
            }
        }
    }

    override suspend fun updateBeneficiary(
        beneficiaryId: Long?,
        payload: BeneficiaryUpdatePayload?,
    ): DataState<String> {
        return withContext(ioDispatcher) {
            try {
                val response = dataManager.beneficiaryApi.updateBeneficiary(beneficiaryId!!, payload)
                DataState.Success(response.bodyAsText())
            } catch (e: ClientRequestException) {
                val errorMessage = extractErrorMessage(e.response)
                DataState.Error(Exception(errorMessage), null)
            } catch (e: IOException) {
                DataState.Error(Exception("Network error: ${e.message ?: "Please check your connection"}"), null)
            } catch (e: ServerResponseException) {
                DataState.Error(Exception("Server error: ${e.message}"), null)
            }
        }
    }

    override suspend fun deleteBeneficiary(beneficiaryId: Long?): DataState<String> {
        return withContext(ioDispatcher) {
            try {
                val response = dataManager.beneficiaryApi.deleteBeneficiary(beneficiaryId!!)
                DataState.Success(response.bodyAsText())
            } catch (e: ClientRequestException) {
                val errorMessage = extractErrorMessage(e.response)
                DataState.Error(Exception(errorMessage), null)
            } catch (e: IOException) {
                DataState.Error(Exception("Network error: ${e.message ?: "Please check your connection"}"), null)
            } catch (e: ServerResponseException) {
                DataState.Error(Exception("Server error: ${e.message}"), null)
            }
        }
    }

    override fun beneficiaryList(): Flow<DataState<List<Beneficiary>>> = flow {
        try {
            dataManager.beneficiaryApi.beneficiaryList()
                .collect { response ->
                    emit(DataState.Success(response))
                }
        } catch (e: Exception) {
            emit(DataState.Error(e, null))
        }
    }.flowOn(ioDispatcher)
}
```

---

## API Summary Table

| Endpoint | Method | Service Method | Repository Method | Priority |
|----------|--------|----------------|-------------------|----------|
| /self/beneficiaries/tpt | GET | beneficiaryList() | beneficiaryList() | P0 |
| /self/beneficiaries/tpt/template | GET | beneficiaryTemplate() | beneficiaryTemplate() | P0 |
| /self/beneficiaries/tpt | POST | createBeneficiary() | createBeneficiary() | P0 |
| /self/beneficiaries/tpt/{id} | PUT | updateBeneficiary() | updateBeneficiary() | P0 |
| /self/beneficiaries/tpt/{id} | DELETE | deleteBeneficiary() | deleteBeneficiary() | P0 |

---

## Account Type Mapping

| ID | Code | Value | Description |
|----|------|-------|-------------|
| 0 | accountType.share | Share Account | Share/Equity account |
| 1 | accountType.loan | Loan Account | Loan disbursement account |
| 2 | accountType.savings | Savings Account | Regular savings account |

---

## Error Responses

| Status Code | Error | Description | User Message |
|-------------|-------|-------------|--------------|
| 400 | Bad Request | Invalid request payload | "Please check your input" |
| 401 | Unauthorized | Invalid/expired token | "Please login again" |
| 403 | Forbidden | Insufficient permissions | "Access denied" |
| 404 | Not Found | Beneficiary doesn't exist | "Beneficiary not found" |
| 409 | Conflict | Duplicate beneficiary | "Beneficiary already exists" |
| 500 | Server Error | Internal server error | "Service unavailable" |

**Error Response Format**:
```json
{
  "developerMessage": "Detailed error for debugging",
  "httpStatusCode": "400",
  "defaultUserMessage": "User-friendly error message",
  "userMessageGlobalisationCode": "error.msg.code",
  "errors": [
    {
      "developerMessage": "Field-specific error",
      "defaultUserMessage": "Invalid account number format",
      "userMessageGlobalisationCode": "validation.msg.accountNumber.invalid",
      "parameterName": "accountNumber"
    }
  ]
}
```

---

## Data Models Summary

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

### BeneficiaryPayload (Create)

```kotlin
@Serializable
@Parcelize
data class BeneficiaryPayload(
    val locale: String? = null,
    val name: String? = null,
    val accountNumber: String? = null,
    val accountType: Int? = 0,
    val transferLimit: Int? = 0,
    val officeName: String? = null,
) : Parcelable
```

### BeneficiaryUpdatePayload (Update)

```kotlin
@Serializable
data class BeneficiaryUpdatePayload(
    val name: String? = null,
    val transferLimit: Int = 0,
)
```

### BeneficiaryTemplate

```kotlin
@Serializable
@Parcelize
data class BeneficiaryTemplate(
    val accountTypeOptions: List<AccountTypeOption>? = null,
) : Parcelable
```

### AccountTypeOption

```kotlin
@Serializable
@Parcelize
data class AccountTypeOption(
    val id: Int? = null,
    val code: String? = null,
    val value: String? = null,
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

### BeneficiaryDetail (Helper)

```kotlin
class BeneficiaryDetail(
    val accountNumber: String?,
    val beneficiaryName: String?,
)
```

---

## Related Endpoints

These endpoints may be used in conjunction with beneficiary management:

| Endpoint | Method | Purpose |
|----------|--------|---------|
| /self/accounttransfers/template?type=tpt | GET | Get transfer template with beneficiary options |
| /self/accounttransfers?type=tpt | POST | Execute transfer to beneficiary |

---

## Changelog

| Date | Change |
|------|--------|
| 2025-12-29 | Initial production-level API documentation based on implementation |
