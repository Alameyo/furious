package com.alameyo.furiouscinema.http

import com.google.gson.JsonObject
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.net.URI.create

@Component
class FuriousHttpClient {

    private val genericHttpClient = GenericHttpClient()
    @Value("\${omdapi.token}")
    private lateinit var token: String
    @Value("\${omdapi.endpoint}")
    private lateinit var endpoint: String

    fun fetchMovieDetails(movieId: String): Any {
        val url = create("$endpoint?apikey=$token&i=$movieId")
        return genericHttpClient.httpGet(url)?.body() ?: JsonObject()
    }
}