# Error Handling Reference

> **Purpose**: Exception types, error extraction, and HTTP status code handling
> **Derived from**: Design layer API.md files and repository implementations
> **Used by**: Client layer repository implementations

---

## Table of Contents
1. [Exception Types](#exception-types)
2. [Error Response Format](#error-response-format)
3. [Error Extraction](#error-extraction)
4. [HTTP Status Codes](#http-status-codes)
5. [Error Handling Patterns](#error-handling-patterns)
6. [User-Friendly Messages](#user-friendly-messages)

---

## Exception Types

### Ktor Client Exceptions

| Exception | Package | HTTP Codes | When Thrown |
|-----------|---------|------------|-------------|
| `ClientRequestException` | `io.ktor.client.plugins` | 400-499 | Client errors (bad request, unauthorized, not found) |
| `ServerResponseException` | `io.ktor.client.plugins` | 500-599 | Server errors (internal error, service unavailable) |
| `IOException` | `java.io` | N/A | Network errors (no connection, timeout) |

### Import Statements

```kotlin
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import java.io.IOException
```

---

## Error Response Format

### Fineract Standard Error Response

```json
{
    "developerMessage": "Detailed technical error message for debugging",
    "httpStatusCode": "400",
    "defaultUserMessage": "User-friendly error message",
    "userMessageGlobalisationCode": "error.msg.beneficiary.account.not.found",
    "errors": [
        {
            "developerMessage": "Field-specific error details",
            "defaultUserMessage": "Invalid account number format",
            "userMessageGlobalisationCode": "validation.msg.accountNumber.invalid",
            "parameterName": "accountNumber"
        }
    ]
}
```

### Error Response DTO

```kotlin
@Serializable
data class ErrorResponse(
    val developerMessage: String? = null,
    val httpStatusCode: String? = null,
    val defaultUserMessage: String? = null,
    val userMessageGlobalisationCode: String? = null,
    val errors: List<FieldError>? = null,
)

@Serializable
data class FieldError(
    val developerMessage: String? = null,
    val defaultUserMessage: String? = null,
    val userMessageGlobalisationCode: String? = null,
    val parameterName: String? = null,
)
```

---

## Error Extraction

### Basic Error Extraction

```kotlin
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

private suspend fun extractErrorMessage(response: HttpResponse): String {
    return try {
        val body = response.bodyAsText()
        val json = Json { ignoreUnknownKeys = true }
        val errorResponse = json.decodeFromString<ErrorResponse>(body)

        // Priority: defaultUserMessage > developerMessage > fallback
        errorResponse.defaultUserMessage
            ?: errorResponse.developerMessage
            ?: "An error occurred"
    } catch (e: Exception) {
        "An error occurred"
    }
}
```

### Field-Level Error Extraction

```kotlin
private suspend fun extractFieldErrors(response: HttpResponse): Map<String, String> {
    return try {
        val body = response.bodyAsText()
        val json = Json { ignoreUnknownKeys = true }
        val errorResponse = json.decodeFromString<ErrorResponse>(body)

        errorResponse.errors?.associate { error ->
            (error.parameterName ?: "unknown") to (error.defaultUserMessage ?: "Invalid value")
        } ?: emptyMap()
    } catch (e: Exception) {
        emptyMap()
    }
}
```

### Error Extraction with Logging

```kotlin
private suspend fun extractErrorMessageWithLogging(response: HttpResponse): String {
    return try {
        val body = response.bodyAsText()
        val json = Json { ignoreUnknownKeys = true }
        val errorResponse = json.decodeFromString<ErrorResponse>(body)

        // Log developer message for debugging
        Log.e("API_ERROR", "Status: ${errorResponse.httpStatusCode}")
        Log.e("API_ERROR", "Developer: ${errorResponse.developerMessage}")
        Log.e("API_ERROR", "User: ${errorResponse.defaultUserMessage}")

        errorResponse.defaultUserMessage ?: "An error occurred"
    } catch (e: Exception) {
        Log.e("API_ERROR", "Failed to parse error: ${e.message}")
        "An error occurred"
    }
}
```

---

## HTTP Status Codes

### Client Errors (4xx)

| Code | Name | Description | User Message |
|------|------|-------------|--------------|
| 400 | Bad Request | Invalid request payload | "Please check your input" |
| 401 | Unauthorized | Invalid/expired token | "Please login again" |
| 403 | Forbidden | Insufficient permissions | "Access denied" |
| 404 | Not Found | Resource doesn't exist | "Not found" |
| 409 | Conflict | Duplicate resource | "Already exists" |
| 422 | Unprocessable | Validation failed | "Invalid data" |

### Server Errors (5xx)

| Code | Name | Description | User Message |
|------|------|-------------|--------------|
| 500 | Internal Server Error | Server bug | "Service unavailable" |
| 502 | Bad Gateway | Upstream error | "Service temporarily unavailable" |
| 503 | Service Unavailable | Server overloaded | "Service temporarily unavailable" |
| 504 | Gateway Timeout | Upstream timeout | "Request timed out" |

### Network Errors

| Error | Cause | User Message |
|-------|-------|--------------|
| UnknownHostException | No DNS resolution | "No internet connection" |
| ConnectException | Connection refused | "Unable to connect to server" |
| SocketTimeoutException | Request timeout | "Request timed out" |
| SSLException | Certificate error | "Secure connection failed" |

---

## Error Handling Patterns

### Pattern 1: Basic Try-Catch

```kotlin
override suspend fun createEntity(payload: EntityPayload?): DataState<String> {
    return withContext(ioDispatcher) {
        try {
            val response = dataManager.entityApi.createEntity(payload)
            DataState.Success(response.bodyAsText())
        } catch (e: ClientRequestException) {
            val errorMessage = extractErrorMessage(e.response)
            DataState.Error(Exception(errorMessage), null)
        } catch (e: IOException) {
            DataState.Error(
                Exception("Network error: ${e.message ?: "Please check your connection"}"),
                null
            )
        } catch (e: ServerResponseException) {
            DataState.Error(Exception("Server error: ${e.message}"), null)
        }
    }
}
```

### Pattern 2: With Status Code Handling

```kotlin
override suspend fun createEntity(payload: EntityPayload?): DataState<String> {
    return withContext(ioDispatcher) {
        try {
            val response = dataManager.entityApi.createEntity(payload)
            DataState.Success(response.bodyAsText())
        } catch (e: ClientRequestException) {
            when (e.response.status.value) {
                400 -> DataState.Error(Exception("Invalid request"), null)
                401 -> DataState.Error(Exception("Please login again"), null)
                403 -> DataState.Error(Exception("Access denied"), null)
                404 -> DataState.Error(Exception("Not found"), null)
                409 -> DataState.Error(Exception("Already exists"), null)
                else -> {
                    val errorMessage = extractErrorMessage(e.response)
                    DataState.Error(Exception(errorMessage), null)
                }
            }
        } catch (e: IOException) {
            DataState.Error(Exception("Network error"), null)
        } catch (e: ServerResponseException) {
            DataState.Error(Exception("Server error"), null)
        }
    }
}
```

### Pattern 3: Flow-Based Error Handling

```kotlin
override fun getEntityList(): Flow<DataState<List<Entity>>> = flow {
    try {
        dataManager.entityApi.getEntityList()
            .collect { response ->
                emit(DataState.Success(response))
            }
    } catch (e: Exception) {
        emit(DataState.Error(e, e.message))
    }
}.flowOn(ioDispatcher)
```

### Pattern 4: With Retry Logic

```kotlin
override suspend fun createEntityWithRetry(
    payload: EntityPayload?,
    maxRetries: Int = 3,
): DataState<String> {
    var lastException: Exception? = null

    repeat(maxRetries) { attempt ->
        try {
            val response = dataManager.entityApi.createEntity(payload)
            return DataState.Success(response.bodyAsText())
        } catch (e: IOException) {
            lastException = e
            delay(1000L * (attempt + 1)) // Exponential backoff
        } catch (e: ServerResponseException) {
            lastException = e
            delay(1000L * (attempt + 1))
        } catch (e: ClientRequestException) {
            // Don't retry client errors
            val errorMessage = extractErrorMessage(e.response)
            return DataState.Error(Exception(errorMessage), null)
        }
    }

    return DataState.Error(lastException ?: Exception("Unknown error"), null)
}
```

---

## User-Friendly Messages

### Message Mapping

```kotlin
object ErrorMessages {

    fun fromHttpStatus(code: Int): String = when (code) {
        400 -> "Please check your input and try again"
        401 -> "Your session has expired. Please login again"
        403 -> "You don't have permission to perform this action"
        404 -> "The requested resource was not found"
        409 -> "This item already exists"
        422 -> "Please check the form for errors"
        500 -> "Something went wrong. Please try again later"
        502, 503 -> "Service is temporarily unavailable"
        504 -> "The request timed out. Please try again"
        else -> "An unexpected error occurred"
    }

    fun fromNetworkError(e: IOException): String = when {
        e.message?.contains("Unable to resolve host") == true ->
            "No internet connection"
        e.message?.contains("timeout") == true ->
            "Connection timed out"
        e.message?.contains("Connection refused") == true ->
            "Unable to connect to server"
        else ->
            "Network error. Please check your connection"
    }
}
```

### Usage in Repository

```kotlin
catch (e: ClientRequestException) {
    val userMessage = ErrorMessages.fromHttpStatus(e.response.status.value)
    DataState.Error(Exception(userMessage), null)
}
catch (e: IOException) {
    val userMessage = ErrorMessages.fromNetworkError(e)
    DataState.Error(Exception(userMessage), null)
}
```

---

## Common Error Codes by Feature

### Authentication

| Code | Meaning | Response |
|------|---------|----------|
| 400 | Invalid credentials format | "Invalid username or password format" |
| 401 | Wrong credentials | "Incorrect username or password" |
| 403 | Account locked | "Your account has been locked" |

### Beneficiary

| Code | Meaning | Response |
|------|---------|----------|
| 400 | Invalid account number | "Account number not found" |
| 404 | Beneficiary not found | "Beneficiary not found" |
| 409 | Duplicate beneficiary | "Beneficiary already exists" |

### Transfer

| Code | Meaning | Response |
|------|---------|----------|
| 400 | Insufficient funds | "Insufficient balance" |
| 400 | Exceeds limit | "Transfer exceeds your limit" |
| 403 | Transfer not allowed | "Transfer not permitted" |

### Savings/Loan Account

| Code | Meaning | Response |
|------|---------|----------|
| 400 | Invalid application | "Application data is invalid" |
| 403 | Not eligible | "Not eligible for this product" |
| 404 | Account not found | "Account not found" |

---

## Testing Error Handling

### Unit Test Example

```kotlin
@Test
fun `createBeneficiary returns error on 400 response`() = runTest {
    // Given
    val errorResponse = """
        {
            "httpStatusCode": "400",
            "defaultUserMessage": "Account not found"
        }
    """.trimIndent()

    coEvery {
        mockService.createBeneficiary(any())
    } throws ClientRequestException(
        MockHttpResponse(HttpStatusCode.BadRequest, errorResponse),
        "Bad Request"
    )

    // When
    val result = repository.createBeneficiary(payload)

    // Then
    assertTrue(result is DataState.Error)
    assertEquals("Account not found", (result as DataState.Error).exception.message)
}
```

---

## Related Files

- Client Patterns: [CLIENT_PATTERNS.md](CLIENT_PATTERNS.md)
- API Reference: [API_REFERENCE.md](API_REFERENCE.md)
- API Index: [API_INDEX.md](API_INDEX.md)

---

## Changelog

| Date | Change |
|------|--------|
| 2025-01-05 | Created with error patterns from codebase analysis |
