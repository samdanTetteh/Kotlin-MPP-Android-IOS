package com.superawesome.sharedcode.api

import com.superawesome.sharedcode.model.Todo
import com.superawesome.sharedcode.runTest
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.features.ClientRequestException
import io.ktor.client.features.json.JsonFeature
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith


class ApiTest {

    @JsName("ClientErrorTest")
    @Test
    fun `when http client throws error, throws exception`() = runTest {
        // GIVEN
        val mockEngine = MockEngine { respondError(HttpStatusCode.NotFound) }

        val httpClient = HttpClient(mockEngine) {
            install(JsonFeature) {
                serializer = kotlinxSerializer
            }
        }

        val api = TodoApi(httpClient)

        // WHEN/THEN
        assertFailsWith<ClientRequestException> { api.getTodoData() }
    }


    @Test
    fun `when http client succeeds, returns expected todo data`() = runTest {
        // GIVEN
        val testResponseString = """
    [{
      "id": 1,
      "title": "Task 1",
      "completed": false
    }]     
    """.trimIndent()

        val mockEngine = MockEngine {
            respond(
                testResponseString,
                HttpStatusCode.OK,
                headersOf("Content-Type", ContentType.Application.Json.toString())
            )
        }

        val httpClient = HttpClient(mockEngine) {
            install(JsonFeature) {
                serializer = kotlinxSerializer
            }
        }

        val api = TodoApi(httpClient)

        val expectedMembers = listOf(
            Todo(
                id = 1L,
                title = "Task 1",
                completed = false
            )
        )

        // WHEN
        val actualTodoData = api.getTodoData()

        // THEN
        assertEquals(expectedMembers, actualTodoData)
    }


    @Test
    fun `when json has unknown fields, returns expected members`() = runTest {
        // GIVEN
        val testResponseString = """
    [{
      "id": 1,
      "title": "Task 2",
      "completed": true,
      "unknown_field": "foo"
    }]     
    """.trimIndent()

        val mockEngine = MockEngine {
            respond(
                testResponseString,
                HttpStatusCode.OK,
                headersOf("Content-Type", ContentType.Application.Json.toString())
            )
        }

        val httpClient = HttpClient(mockEngine) {
            install(JsonFeature) {
                serializer = kotlinxSerializer
            }
        }

        val githubApi = TodoApi(httpClient)

        val expectedMembers = listOf(
            Todo(
                id = 1L,
                title = "Task 2",
                completed = true
            )
        )

        // WHEN
        val actualData = githubApi.getTodoData()

        // THEN
        assertEquals(expectedMembers, actualData)
    }

}