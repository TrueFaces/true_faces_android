package com.n1rocket.truefaces.datasources

import android.util.Log
import com.n1rocket.truefaces.api.KtorClient
import com.n1rocket.truefaces.models.ImagesResponse
import com.n1rocket.truefaces.models.LoginResponse
import com.n1rocket.truefaces.models.MeResponse
import io.ktor.client.features.onUpload
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.http.ContentDisposition
import io.ktor.http.ContentType.MultiPart.FormData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.Parameters
import io.ktor.http.Url
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeFully

internal class RestDataSource(private val url: String) : IRestDataSource {
    private val httpClient by lazy { KtorClient.getInstance }
    override suspend fun getImages(token: String): List<ImagesResponse> {
        return httpClient.get(url = Url("$url/users/images")) {
            headers {
                append("Authorization", "Bearer $token")
            }
        }
    }

    override suspend fun uploadImage(byteArray: ByteArray, token: String): String {
        return httpClient.post("$url/images/upload") {
            headers {
                //append("Content-Type", ContentType.Application.Json)
                append("Authorization", "Bearer $token")
            }
            body = MultiPartFormDataContent(
                formData {
                    this.appendInput(
                        key = "file",
                        size = byteArray.size.toLong(),
                        headers = Headers.build {
                            append(HttpHeaders.ContentDisposition, "filename=file")
                        },
                    ) { buildPacket { writeFully(byteArray) } }
                }
            )
            onUpload { bytesSentTotal, contentLength ->
                println("Sent $bytesSentTotal bytes from $contentLength")
            }
        }
    }

    override suspend fun uploadAvatarBody(byteArray: ByteArray, token: String): String {
        return httpClient.post("$url/users/upload") {
            headers {
                //append("Content-Type", ContentType.Application.Json)
                append("Authorization", "Bearer $token")
            }
            body = MultiPartFormDataContent(
                formData {
                    this.appendInput(
                        key = "file",
                        size = byteArray.size.toLong(),
                        headers = Headers.build {
                            append(HttpHeaders.ContentDisposition, "filename=file")
                        },
                    ) { buildPacket { writeFully(byteArray) } }
                }
            )
            onUpload { bytesSentTotal, contentLength ->
                println("Sent $bytesSentTotal bytes from $contentLength")
            }
        }
    }

    override suspend fun uploadAvatar(byteArray: ByteArray, token: String): String {
        Log.d("RestDataSource", "Token: $token")

        return httpClient.submitFormWithBinaryData(
            url = "$url/users/upload",
            formData = formData {
                append("file", byteArray, Headers.build {
                    append(HttpHeaders.ContentType, "image/png")
                    append(HttpHeaders.ContentDisposition, "file=file")
                })
            }
        ) {
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
            onUpload { bytesSentTotal, contentLength ->
                println("Sent $bytesSentTotal bytes from $contentLength")
            }
        }
    }

    override suspend fun login(user: String, password: String): LoginResponse {
        return httpClient.submitForm(
            url = "$url/auth/login",
            formParameters = Parameters.build {
                append("username", user)
                append("password", password)
            }
        )
    }

    override suspend fun me(token: String): MeResponse {
        return httpClient.get(url = Url("$url/auth/me")) {
            headers {
                append("Authorization", "Bearer $token")
            }
        }
    }
}
