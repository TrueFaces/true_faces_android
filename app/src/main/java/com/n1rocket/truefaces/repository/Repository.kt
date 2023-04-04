package com.n1rocket.truefaces.repository

import com.n1rocket.truefaces.api.ApiError
import com.n1rocket.truefaces.api.ApiException
import com.n1rocket.truefaces.api.ApiResult
import com.n1rocket.truefaces.api.ApiSuccess
import com.n1rocket.truefaces.datasources.IPreferencesDataSource
import com.n1rocket.truefaces.datasources.IRestDataSource
import com.n1rocket.truefaces.models.ImagesResponse
import com.n1rocket.truefaces.models.MeResponse
import com.n1rocket.truefaces.models.LoginResponse
import io.ktor.client.features.ClientRequestException
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

@Suppress("TooGenericExceptionCaught")
class Repository(
    private val dataSource: IRestDataSource,
    private val preferencesDataSource: IPreferencesDataSource,
) : IRepository {
    override suspend fun getImages(): ApiResult<List<ImagesResponse>> {
        return try {
            val response = dataSource.getImages(preferencesDataSource.getToken())
            ApiSuccess(response)
        } catch (e: ClientRequestException) {
            ApiError(e.response.status.value, e.message)
        } catch (e: Exception) {
            ApiException(e)
        }
    }

    override suspend fun uploadImage(name: String, byteArray: ByteArray): ApiResult<String> {
        return try {
            val response = dataSource.uploadImage(name, byteArray, preferencesDataSource.getToken())
            ApiSuccess(response)
        } catch (e: ClientRequestException) {
            ApiError(e.response.status.value, e.message)
        } catch (e: Exception) {
            ApiException(e)
        }
    }

    override suspend fun login(user: String, password: String): ApiResult<LoginResponse> {
        return try {
            val response = dataSource.login(user, password)
            ApiSuccess(response)
        } catch (e: ClientRequestException) {
            ApiError(e.response.status.value, e.message)
        } catch (e: Exception) {
            ApiException(e)
        }
    }

    override suspend fun me(): ApiResult<MeResponse> {
        return try {
            val response = dataSource.me(preferencesDataSource.getToken())
            ApiSuccess(response)
        } catch (e: ClientRequestException) {
            checkAuthorized(e)
            ApiError(e.response.status.value, e.message)
        } catch (e: Exception) {
            ApiException(e)
        }
    }

    override fun getTokenFlow(viewModelScope: CoroutineScope): StateFlow<String> =
        preferencesDataSource.getTokenFlow(viewModelScope)

    private fun checkAuthorized(exception: ClientRequestException) {
        if (exception.response.status == HttpStatusCode.Unauthorized) {
            preferencesDataSource.clear()
        }
    }

    override fun isLogged() = preferencesDataSource.isLogged()

    override suspend fun saveToken(accessToken: String) {
        preferencesDataSource.saveToken(accessToken)
    }

    override fun logout() {
        preferencesDataSource.clear()
    }
}
