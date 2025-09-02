# Ktorfit Networking Module Setup
This module provides a convenient way to configure and use Ktorfit with a consistent HTTP client setup and response handling using Result wrappers for both suspend and Flow return types.

## Getting Started
To use this module, you need to create a Ktorfit instance and provide it with a configured HttpClient and converter factories.

### Ktorfit Setup Example
```yaml
val ktorfit = Ktorfit.Builder()
    .httpClient(httpClient(setupDefaultHttpClient(baseUrl = "https://your.api.com")))
    .converterFactories(
        ResultSuspendConverterFactory(),
        ResultFlowConverterFactory()
    )
    .build()
```
## HttpClient Configuration
When configuring the HttpClient, you have two options:

### Option 1: Provide a custom config
You can define your own plugins and setup manually:
```yaml
val config: HttpClientConfig<*>.() -> Unit = {
    install(Auth) { /* ... */ }
    install(ContentNegotiation) { /* ... */ }
    install(HttpTimeout) { /* ... */ }
    // other plugins...
}
```
val client = httpClient(config)

### Option 2: Use our default config
We provide `setupDefaultHttpClient()` which simplifies configuration. Only `baseUrl` is required. All other parameters are optional.
```yaml
val client = httpClient(
    setupDefaultHttpClient(
        baseUrl = "https://your.api.com"
    )
)
```

| Parameter                 | Type                          | Description                                                                              |
|---------------------------|-------------------------------|------------------------------------------------------------------------------------------|
| baseUrl                   | `String`                      | Required. The base URL for requests                                                      |
| authRequiredUrl           | `List<String>	`             | Domains that require auth headers                                                        |
| defaultHeaders            | `Map<String, String>`         | Headers to include in every request                                                      |
| requestTimeout            | `Long`                        | Millis before timing out a request. Default: 60_000                                      |
| socketTimeout             | `Long`                        | Millis before timing out a socket. Default: 60_000                                       |
| httpLogger                | `Logger`                      | Ktor logger. Default: Logger.DEFAULT                                                     |
| httpLogLevel              | `LogLevel`                    | Logging level. Default: LogLevel.ALL                                                     |
| loggableHosts             | `List<String>`                | A list of hostnames. Only requests made to these hosts will be logged.                   |
| sensitiveHeaders          | `List<String>`                | Headers to redact in logs. Default: Authorization                                        |
| jsonConfig                | `Json`                        | Customize JSON parsing. Default: lenient, ignoresUnknownKeys, prettyPrint, explicitNulls |
| basicCredentialsProvider  | `() -> BasicAuthCredentials`  | Supplies basic auth(username, password) credentials                                      |
| digestCredentialsProvider | `() -> DigestAuthCredentials` | Supplies digest auth credentials                                                         |
| bearerTokensProvider      | `() -> BearerTokens`          | Supplies bearer tokens                                                                   |
| bearerRefreshProvider     | `() -> BearerTokens`          | Refreshes bearer tokens if needed                                                        |

## Converter Factories
To handle wrapping network responses into a Result<Success, Error> model, use converters.

### Option 1: Use your own converters
You can write your own as shown in [Ktorfit Docs – Custom Converters](https://foso.github.io/Ktorfit/converters/responseconverter/)

### Option 2: Use built-in converters from this module
We provide:

**ResultSuspendConverterFactory**
Wraps suspend functions into:
```yaml
interface ApiService {
    @GET("users")
    suspend fun getUsers(): Result<List<User>, RemoteError>
}
```
**ResultFlowConverterFactory**
Wraps Flow-returning functions:
```yaml
interface ApiService {
    @GET("items")
    fun getItems(): Flow<Result<List<Item>, RemoteError>>
}
```
## Result & Error Wrapping
The following sealed class and enum are used:
```yaml
sealed interface Result<out D, out E : RemoteError> {
    data class Success<out D>(val data: D) : Result<D, Nothing>
    data class Error<out E : RemoteError>(val error: E) : Result<Nothing, E>
}
```
```yaml
enum class RemoteError {
    BAD_REQUEST,
    NOT_FOUND,
    UNAUTHORIZED,
    REQUEST_TIMEOUT,
    TOO_MANY_REQUESTS,
    SERVER,
    SERIALIZATION,
    UNKNOWN
}
```

## Usage Example
Define an API interface
```yaml
interface UserApi {
    @GET("users")
    suspend fun getUsers(): Result<List<User>, RemoteError>

    @GET("groups")
    fun getGroups(): Flow<Result<List<Group>, RemoteError>>
}
```
Create an instance
```yaml
val ktorfit = Ktorfit.Builder()
    .httpClient(httpClient(setupDefaultHttpClient(baseUrl = "https://your.api.com")))
    .converterFactories(ResultSuspendConverterFactory(), ResultFlowConverterFactory())
    .build()

val userApi = ktorfit.createUserApi()
```
You can now call:
```yaml
val usersResult = userApi.getUsers() // suspend function

userApi.getGroups().collect { result ->
    when (result) {
        is Result.Success -> { /* handle data */ }
        is Result.Error -> { /* handle error */ }
    }
}
```

## Summary
This module aims to make Ktorfit easy, safe, and robust by providing:

- Built-in Result<T, RemoteError> wrappers.

- Cleanly configured HttpClient.

- Support for both suspend and Flow return types.

Use the default config if you want to skip boilerplate, or customize to suit your needs.

## Demo video
https://github.com/user-attachments/assets/23b70168-a0a3-42fe-8b06-23b0ae34fc44