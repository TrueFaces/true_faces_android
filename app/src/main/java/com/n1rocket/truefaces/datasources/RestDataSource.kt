package com.n1rocket.truefaces.datasources

import com.n1rocket.truefaces.api.KtorClient
import io.ktor.client.features.onUpload
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.Parameters
import io.ktor.http.Url
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeFully

internal class RestDataSource(private val url: String) : IRestDataSource {
    private val httpClient by lazy { KtorClient.getInstance }
    override suspend fun testRepo(): String {
        return httpClient.get(url = Url(url))
    }

    override suspend fun uploadImage(name: String, byteArray: ByteArray): String {
        return httpClient.post("$url/uploadfile/") {
            headers {
                //append("Content-Type", ContentType.Application.Json)
                //append("Authorization", "Bearer $token")
            }
            body = MultiPartFormDataContent(
                formData {
                    this.appendInput(
                        key = "file",
                        size = byteArray.size.toLong(),
                        headers = Headers.build {
                            append(HttpHeaders.ContentDisposition, "filename=${name}")
                        },
                    ) { buildPacket { writeFully(byteArray) } }
                }
            )
            onUpload { bytesSentTotal, contentLength ->
                println("Sent $bytesSentTotal bytes from $contentLength")
            }
        }
    }

    override suspend fun login(user: String, password: String): String {
        return httpClient.submitForm(
            url = "$url/login/",
            formParameters = Parameters.build {
                append("user", user)
                append("password", password)
            }
        )
    }
}
