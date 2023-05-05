package com.example.url_shortener

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class UrlShortenerService {
    private val client = HttpClient(OkHttp) {
        install(JsonFeature) {
            serializer = KotlinxSerializer(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    suspend fun shortenThisUrl(longUrl: String): String {
        val response = client.post<ShortenUrlResponse>("https://cleanuri.com/api/v1/shorten") {
            contentType(ContentType.Application.Json)
            body = ShortenUrlRequest(longUrl)
        }
        return response.result_url
    }

    @Serializable
    data class ShortenUrlRequest(val url: String)

    @Serializable
    data class ShortenUrlResponse(val result_url: String)

}
