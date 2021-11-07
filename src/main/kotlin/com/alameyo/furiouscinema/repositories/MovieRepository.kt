package com.alameyo.furiouscinema.repositories

import com.google.gson.JsonObject
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Projections.excludeId
import org.bson.Document
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class MovieRepository(@Autowired private val database: MongoDatabase) {
    private val reviewsCollection = database.getCollection("reviews")

    fun reviewMovie(movieId: String, rate: JsonObject) {
        val reviewDocument = Document().apply {
            append("movieId", movieId)
            append("rate", rate.get("rate").asInt)
            append("comment", rate.get("comment")?.asString ?: "")
        }
        reviewsCollection.insertOne(reviewDocument)
    }
    fun getReviews(movieId: String) = reviewsCollection.find(eq("movieId", movieId)).projection(excludeId()).toList()

    fun getReviews() = reviewsCollection.find().projection(excludeId()).toList()
}
