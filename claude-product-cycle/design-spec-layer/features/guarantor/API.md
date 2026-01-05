# Guarantor - API Reference

## Base URL
`https://tt.mifos.community/fineract-provider/api/v1/self/`

---

## Endpoints Required

### 1. Get Guarantor Template

**Endpoint**: `GET /loans/{loanId}/guarantors/template`

**Description**: Fetch the template containing guarantor type options for creating a new guarantor

**Path Parameters**:
| Parameter | Type | Description |
|-----------|------|-------------|
| loanId | Long | The loan account ID |

**Response**:
```json
{
  "guarantorType": {
    "id": 3,
    "code": "guarantor.external",
    "value": "External"
  },
  "guarantorTypeOptions": [
    {
      "id": 1,
      "code": "guarantor.existing.customer",
      "value": "Existing Customer"
    },
    {
      "id": 2,
      "code": "guarantor.staff",
      "value": "Staff Member"
    },
    {
      "id": 3,
      "code": "guarantor.external",
      "value": "External"
    }
  ]
}
```

**Kotlin DTO**:
```kotlin
@Serializable
data class GuarantorTemplatePayload(
    val guarantorType: GuarantorType? = null,
    val guarantorTypeOptions: ArrayList<GuarantorType>? = null,
)
```

**Status**: Implemented in GuarantorService

---

### 2. Get Guarantor List

**Endpoint**: `GET /loans/{loanId}/guarantors`

**Description**: Fetch all guarantors associated with a loan account

**Path Parameters**:
| Parameter | Type | Description |
|-----------|------|-------------|
| loanId | Long | The loan account ID |

**Response**:
```json
[
  {
    "id": 1,
    "loanId": 101,
    "firstname": "John",
    "lastname": "Doe",
    "city": "New York",
    "guarantorType": {
      "id": 3,
      "code": "guarantor.external",
      "value": "External"
    },
    "joinedDate": [2023, 6, 15],
    "status": true
  },
  {
    "id": 2,
    "loanId": 101,
    "firstname": "Emma",
    "lastname": "Smith",
    "city": "Los Angeles",
    "guarantorType": {
      "id": 3,
      "code": "guarantor.external",
      "value": "External"
    },
    "joinedDate": [2023, 8, 20],
    "status": true
  }
]
```

**Kotlin DTO**:
```kotlin
@Serializable
@Parcelize
data class GuarantorPayload(
    val id: Long? = 0,
    val city: String? = null,
    val lastname: String? = null,
    val guarantorType: @RawValue GuarantorType? = null,
    val firstname: String? = null,
    val joinedDate: List<Int>? = null,
    val loanId: Long? = null,
    val status: Boolean? = true,
) : Parcelable
```

**Status**: Implemented in GuarantorService (currently using fake data)

---

### 3. Create Guarantor

**Endpoint**: `POST /loans/{loanId}/guarantors`

**Description**: Add a new guarantor to a loan account

**Path Parameters**:
| Parameter | Type | Description |
|-----------|------|-------------|
| loanId | Long | The loan account ID |

**Request**:
```json
{
  "guarantorTypeId": 3,
  "firstname": "Michael",
  "lastname": "Brown",
  "city": "Chicago"
}
```

**Kotlin DTO**:
```kotlin
data class GuarantorApplicationPayload(
    @SerialName("guarantorTypeId")
    val guarantorTypeId: Long? = null,

    @SerialName("firstname")
    val firstName: String? = null,

    @SerialName("lastname")
    val lastName: String? = null,

    @SerialName("city")
    val city: String? = "",
)
```

**Response**:
```json
{
  "resourceId": 123,
  "loanId": 101
}
```

**Status**: Implemented in GuarantorService

---

### 4. Update Guarantor

**Endpoint**: `PUT /loans/{loanId}/guarantors/{guarantorId}`

**Description**: Update an existing guarantor's information

**Path Parameters**:
| Parameter | Type | Description |
|-----------|------|-------------|
| loanId | Long | The loan account ID |
| guarantorId | Long | The guarantor ID to update |

**Request**:
```json
{
  "guarantorTypeId": 3,
  "firstname": "Michael",
  "lastname": "Brown Jr.",
  "city": "San Francisco"
}
```

**Response**:
```json
{
  "resourceId": 123,
  "changes": {
    "lastname": "Brown Jr.",
    "city": "San Francisco"
  }
}
```

**Status**: Implemented in GuarantorService

---

### 5. Delete Guarantor

**Endpoint**: `DELETE /loans/{loanId}/guarantors/{guarantorId}`

**Description**: Remove a guarantor from a loan account

**Path Parameters**:
| Parameter | Type | Description |
|-----------|------|-------------|
| loanId | Long | The loan account ID |
| guarantorId | Long | The guarantor ID to delete |

**Response**:
```json
{
  "resourceId": 123,
  "loanId": 101
}
```

**Status**: Implemented in GuarantorService

---

## Kotlin DTOs

### GuarantorType
```kotlin
@Serializable
@Parcelize
class GuarantorType(
    val id: Long? = null,
    val value: String? = null,
    val code: String? = null,
) : Parcelable
```

### GuarantorPayload
```kotlin
@Serializable
@Parcelize
data class GuarantorPayload(
    val id: Long? = 0,
    val city: String? = null,
    val lastname: String? = null,
    val guarantorType: @RawValue GuarantorType? = null,
    val firstname: String? = null,
    val joinedDate: List<Int>? = null,
    val loanId: Long? = null,
    val status: Boolean? = true,
) : Parcelable
```

### GuarantorTemplatePayload
```kotlin
@Serializable
data class GuarantorTemplatePayload(
    val guarantorType: GuarantorType? = null,
    val guarantorTypeOptions: ArrayList<GuarantorType>? = null,
)
```

### GuarantorApplicationPayload
```kotlin
data class GuarantorApplicationPayload(
    @SerialName("guarantorTypeId")
    val guarantorTypeId: Long? = null,

    @SerialName("firstname")
    val firstName: String? = null,

    @SerialName("lastname")
    val lastName: String? = null,

    @SerialName("city")
    val city: String? = "",
)
```

---

## API Summary

| Endpoint | Method | Service | Repository | Status |
|----------|--------|---------|------------|--------|
| /loans/{loanId}/guarantors/template | GET | GuarantorService | GuarantorRepository | Implemented |
| /loans/{loanId}/guarantors | GET | GuarantorService | GuarantorRepository | Implemented (using fake data) |
| /loans/{loanId}/guarantors | POST | GuarantorService | GuarantorRepository | Implemented |
| /loans/{loanId}/guarantors/{guarantorId} | PUT | GuarantorService | GuarantorRepository | Implemented |
| /loans/{loanId}/guarantors/{guarantorId} | DELETE | GuarantorService | GuarantorRepository | Implemented |

---

## Error Responses

| Status Code | Error | Description |
|-------------|-------|-------------|
| 400 | Bad Request | Invalid payload or missing required fields |
| 401 | Unauthorized | User not authenticated |
| 403 | Forbidden | User does not have permission for this loan |
| 404 | Not Found | Loan or guarantor not found |
| 500 | Server Error | Internal server error |

---

## Service Interface

```kotlin
interface GuarantorService {
    @GET("loans/{loanId}/guarantors/template")
    fun getGuarantorTemplate(@Path("loanId") loanId: Long): Flow<GuarantorTemplatePayload>

    @GET("loans/{loanId}/guarantors")
    fun getGuarantorList(@Path("loanId") loanId: Long): Flow<List<GuarantorPayload>>

    @POST("loans/{loanId}/guarantors")
    suspend fun createGuarantor(
        @Path("loanId") loanId: Long,
        @Body payload: GuarantorApplicationPayload?,
    ): HttpResponse

    @PUT("loans/{loanId}/guarantors/{guarantorId}")
    suspend fun updateGuarantor(
        @Body payload: GuarantorApplicationPayload?,
        @Path("loanId") loanId: Long,
        @Path("guarantorId") guarantorId: Long,
    ): HttpResponse

    @DELETE("loans/{loanId}/guarantors/{guarantorId}")
    suspend fun deleteGuarantor(
        @Path("loanId") loanId: Long,
        @Path("guarantorId") guarantorId: Long,
    ): HttpResponse
}
```

---

## Repository Interface

```kotlin
interface GuarantorRepository {
    fun getGuarantorTemplate(loanId: Long?): Flow<DataState<GuarantorTemplatePayload?>>

    suspend fun createGuarantor(
        loanId: Long?,
        payload: GuarantorApplicationPayload?,
    ): DataState<String>

    suspend fun updateGuarantor(
        payload: GuarantorApplicationPayload?,
        loanId: Long?,
        guarantorId: Long?,
    ): DataState<String>

    suspend fun deleteGuarantor(loanId: Long?, guarantorId: Long?): DataState<String>

    fun getGuarantorList(loanId: Long): Flow<DataState<List<GuarantorPayload?>?>>
}
```

---

## Notes

1. **Fake Data Source**: The `getGuarantorList` endpoint currently returns fake demo data in `GuarantorRepositoryImp` as the real API is commented out.

2. **Ktorfit Integration**: The service uses Ktorfit annotations for HTTP client integration with Ktor.

3. **DataState Pattern**: All repository methods wrap responses in `DataState<T>` for consistent error handling:
   - `DataState.Success<T>` - Successful response with data
   - `DataState.Error` - Error with exception and message
   - `DataState.Loading` - Loading state

4. **Flow-based API**: GET endpoints return `Flow<T>` for reactive data handling.
