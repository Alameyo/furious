package com.alameyo.furiouscinema.http

import java.lang.System.setProperty
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublishers.ofString
import java.net.http.HttpRequest.newBuilder
import java.net.http.HttpResponse
import java.net.http.HttpResponse.BodyHandlers.ofString
import javax.net.ssl.SSLParameters

class GenericHttpClient {
    init {
        setProperty("jdk.tls.namedGroups", "secp521r1")
    }

    private val strictSSLParameters = SSLParameters().apply {
        protocols = arrayOf("TLSv1.3")
    }

    private val httpClient = HttpClient.newBuilder().sslParameters(strictSSLParameters).build()

    fun httpGet(addressUri: URI): HttpResponse<String>? {
        val httpRequest = newBuilder().uri(addressUri).build()
        return sendRequest(httpRequest)
    }

    fun httpPost(addressUri: URI, body: String): HttpResponse<String>? {
        val httpRequest = newBuilder().uri(addressUri).POST(ofString(body)).build()
        return sendRequest(httpRequest)
    }

    fun httpPut(addressUri: URI, body: String, vararg headers: String = arrayOf()): HttpResponse<String>? {
        val httpRequest = newBuilder().uri(addressUri).headers(*headers).PUT(ofString(body)).build()
        return sendRequest(httpRequest)
    }

    private fun sendRequest(request: HttpRequest) = httpClient.send(request, ofString())
}
