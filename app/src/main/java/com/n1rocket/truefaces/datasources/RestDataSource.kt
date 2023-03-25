package com.n1rocket.truefaces.datasources

import com.n1rocket.truefaces.api.KtorClient
import io.ktor.client.features.onUpload
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.host
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.Url

internal class RestDataSource(private val url: String) : IRestDataSource {
    private val httpClient by lazy { KtorClient.getInstance }
    override suspend fun testRepo(): String {
        return httpClient.get(url = Url("https://apifast-2-r0282118.deta.app/"))
    }

    override suspend fun uploadImage(name: String, byteArray: ByteArray): String {
        return httpClient.submitFormWithBinaryData(
            url = url,
            formData = formData {
                //the line below is just an example of how to send other parameters in the same request
                append("name", name)
                append("image", byteArray, Headers.build {
                    append(HttpHeaders.ContentType, "image/jpeg")
                    append(HttpHeaders.ContentDisposition, "filename=image.png")
                })
            }
        ) {
            onUpload { bytesSentTotal, contentLength ->
                println("Sent $bytesSentTotal bytes from $contentLength")
            }
        }
    }
}
