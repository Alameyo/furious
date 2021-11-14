package com.alameyo.furious

import com.alameyo.furiouscinema.http.GenericHttpClient
import com.google.gson.JsonObject
import java.net.URI.create
import java.net.http.HttpResponse
import java.util.Base64.getEncoder

class TestingHttpClient(private val prefix: String = "http://localhost:8080/furious") {
    private val genericHttpClient = GenericHttpClient()

    fun get(endpoint: String) = genericHttpClient.httpGet(create("$prefix$endpoint"))

    fun post(endpoint: String, body: JsonObject) = genericHttpClient.httpPost(create("$prefix$endpoint"), body.toString())

    fun put(endpoint: String, body: JsonObject, authentication: Pair<String, String>): HttpResponse<String>? {
        val basicAuth = basicAuth(authentication.first, authentication.second)
        return genericHttpClient.httpPut(create("$prefix$endpoint"), body.toString(), "Authorization", basicAuth)
    }

    private fun basicAuth(username: String, password: String) = "Basic " + getEncoder().encodeToString("$username:$password".toByteArray())
}
