package com.superawesome.sharedcode.api

import com.superawesome.sharedcode.model.Todo
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.DEFAULT
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.get
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

internal val kotlinxSerializer = KotlinxSerializer(
    Json(
        JsonConfiguration(isLenient = true, ignoreUnknownKeys = true)
    )
)

internal val ktorClient = HttpClient {
    install(JsonFeature) {
        serializer = kotlinxSerializer
    }
    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.ALL
    }
}

internal const val BASE_URL = "https://jsonplaceholder.typicode.com/"

class TodosApi(private val client: HttpClient = ktorClient) {
    suspend fun getTodos(): List<Todo> = client.get(BASE_URL + "todos")
}