package com.n1rocket.truefaces.repository

import com.n1rocket.truefaces.api.ApiError
import com.n1rocket.truefaces.api.ApiException
import com.n1rocket.truefaces.api.ApiResult
import com.n1rocket.truefaces.api.ApiSuccess
import com.n1rocket.truefaces.datasources.IRestDataSource
import io.ktor.client.features.ClientRequestException

@Suppress("TooGenericExceptionCaught")
class Repository(
    private val dataSource: IRestDataSource,
) : IRepository {
    override suspend fun uploadImage(name: String, byteArray: ByteArray): ApiResult<String> {
        return try {
            val response = dataSource.uploadImage(name, byteArray)
            ApiSuccess(response)
        } catch (e: ClientRequestException) {
            ApiError(e.response.status.value, e.message)
        } catch (e: Exception) {
            ApiException(e)
        }
    }
}
