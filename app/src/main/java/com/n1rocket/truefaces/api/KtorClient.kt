package com.n1rocket.truefaces.api

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.HttpTimeout
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.ANDROID
import io.ktor.client.features.logging.DEFAULT
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

@OptIn(ExperimentalSerializationApi::class)
internal object KtorClient {

    private const val CONTENT_TYPE = "Content-Type"
    private const val TIMEOUT = 30000L

    private val client = HttpClient(Android) {
        defaultRequest {
            //header(CONTENT_TYPE, "application/json")
        }
        install(Logging) {
            logger = object: Logger {
                override fun log(message: String) {
                    Napier.v(message, null, "HTTP Client")
                }
            }
            level = LogLevel.ALL
        }.also { Napier.base(DebugAntilog()) }
        install(JsonFeature) {
            serializer = KotlinxSerializer(
                Json {
                    isLenient = true
                    ignoreUnknownKeys = true
                    explicitNulls = false
                    encodeDefaults = true
                }
            )
        }
        // Timeout
        install(HttpTimeout) {
            requestTimeoutMillis = TIMEOUT
            connectTimeoutMillis = TIMEOUT
            socketTimeoutMillis = TIMEOUT
        }
        expectSuccess = true
    }

    val getInstance = client
}
