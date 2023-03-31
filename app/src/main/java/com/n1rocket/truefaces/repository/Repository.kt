package com.n1rocket.truefaces.repository

import com.n1rocket.truefaces.api.ApiError
import com.n1rocket.truefaces.api.ApiException
import com.n1rocket.truefaces.api.ApiResult
import com.n1rocket.truefaces.api.ApiSuccess
import com.n1rocket.truefaces.datasources.IPreferencesDataSource
import com.n1rocket.truefaces.datasources.IRestDataSource
import com.n1rocket.truefaces.ui.screens.login.LoginResponse
import io.ktor.client.features.ClientRequestException

@Suppress("TooGenericExceptionCaught")
class Repository(
    private val dataSource: IRestDataSource,
    private val preferencesDataSource: IPreferencesDataSource,
) : IRepository {
    override suspend fun getImages(): ApiResult<String> {
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

    override fun isLogged() = preferencesDataSource.isLogged()

    override suspend fun saveToken(accessToken: String) {
        preferencesDataSource.saveToken(accessToken)
    }
}
