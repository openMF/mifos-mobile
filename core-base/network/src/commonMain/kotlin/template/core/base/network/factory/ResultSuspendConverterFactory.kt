/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package template.core.base.network.factory

import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.converter.Converter
import de.jensklingenberg.ktorfit.converter.KtorfitResult
import de.jensklingenberg.ktorfit.converter.TypeData
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import kotlinx.serialization.SerializationException
import template.core.base.network.NetworkError
import template.core.base.network.NetworkResult

/**
 * A custom [Converter.Factory] for Ktorfit that provides a suspend
 * response converter which wraps successful or error HTTP responses into a
 * sealed [NetworkResult] type.
 *
 * This is useful for abstracting error handling logic across your network
 * layer while providing strong typing for both success and failure
 * outcomes.
 *
 * This converter handles:
 * - HTTP 2xx responses by deserializing the response body into the
 *   expected type.
 * - Known HTTP error codes like 400, 401, 404, etc., by mapping them to
 *   [NetworkError] types.
 * - Deserialization issues via [SerializationException].
 * - Unknown failures via [KtorfitResult.Failure].
 *
 * Example usage:
 * ```kotlin
 * interface ApiService {
 *     @GET("users")
 *     suspend fun getUsers(): Result<List<User>, RemoteError>
 * }
 * ```
 */
@Suppress("NestedBlockDepth")
class ResultSuspendConverterFactory : Converter.Factory {

    /**
     * Creates a [Converter.SuspendResponseConverter] that wraps an HTTP
     * response into a [NetworkResult] type.
     *
     * @param typeData Metadata about the expected response type.
     * @param ktorfit The [Ktorfit] instance requesting this converter.
     * @return A [Converter.SuspendResponseConverter] if the return type is
     *    `Result`, or `null` otherwise.
     */
    override fun suspendResponseConverter(
        typeData: TypeData,
        ktorfit: Ktorfit,
    ): Converter.SuspendResponseConverter<HttpResponse, *>? {
        if (typeData.typeInfo.type == NetworkResult::class) {
            val successType = typeData.typeArgs.first().typeInfo
            return object :
                Converter.SuspendResponseConverter<HttpResponse, NetworkResult<Any, NetworkError>> {

                /**
                 * Converts a [KtorfitResult] into a [NetworkResult], handling success and
                 * various failure scenarios.
                 *
                 * @param result The response wrapped in [KtorfitResult].
                 * @return A [NetworkResult.Success] if the response is successful, or a
                 *    [NetworkResult.Error] if an error occurred.
                 */
                override suspend fun convert(result: KtorfitResult): NetworkResult<Any, NetworkError> {
                    return when (result) {
                        is KtorfitResult.Failure -> {
                            println("Failure: " + result.throwable.message)
                            NetworkResult.Error(NetworkError.UNKNOWN)
                        }

                        is KtorfitResult.Success -> {
                            val status = result.response.status.value

                            when (status) {
                                in 200..209 -> {
                                    try {
                                        val data = result.response.body(successType) as Any
                                        NetworkResult.Success(data)
                                    } catch (e: NoTransformationFoundException) {
                                        NetworkResult.Error(NetworkError.SERIALIZATION)
                                    } catch (e: SerializationException) {
                                        println("Serialization error: ${e.message}")
                                        NetworkResult.Error(NetworkError.SERIALIZATION)
                                    }
                                }

                                400 -> NetworkResult.Error(NetworkError.BAD_REQUEST)
                                401 -> NetworkResult.Error(NetworkError.UNAUTHORIZED)
                                404 -> NetworkResult.Error(NetworkError.NOT_FOUND)
                                408 -> NetworkResult.Error(NetworkError.REQUEST_TIMEOUT)
                                429 -> NetworkResult.Error(NetworkError.TOO_MANY_REQUESTS)
                                in 500..599 -> NetworkResult.Error(NetworkError.SERVER)
                                else -> {
                                    println("Status code $status")
                                    NetworkResult.Error(NetworkError.UNKNOWN)
                                }
                            }
                        }
                    }
                }
            }
        }
        return null
    }
}
