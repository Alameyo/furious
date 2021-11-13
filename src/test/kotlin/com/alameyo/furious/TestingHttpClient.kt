package com.alameyo.furious

import com.alameyo.furiouscinema.http.GenericHttpClient
import com.google.gson.JsonObject
import java.net.URI.create

class TestingHttpClient(private val prefix: String = "http://localhost:8080/furious") {
    private val genericHttpClient = GenericHttpClient()

    fun get(endpoint: String) = genericHttpClient.httpGet(create("$prefix$endpoint"))

    fun post(endpoint: String, body: JsonObject) = genericHttpClient.httpPost(create("$prefix$endpoint"), body.toString())
}
