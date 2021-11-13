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

    fun createMovieReviewDocument(movieId: String, review: JsonObject): Document {
        return Document().apply {
            append("movieId", movieId)
            append("rate", review.get("rate").asInt)
            append("comment", review.get("comment")?.asString ?: "")
        }
    }

    fun commitReview(reviewDocument: Document) = reviewsCollection.insertOne(reviewDocument).wasAcknowledged()

    fun getReviews(movieId: String) = reviewsCollection.find(eq("movieId", movieId)).projection(excludeId()).toList()

    fun getReviews() = reviewsCollection.find().projection(excludeId()).toList()

    fun getAverageRatingForMovie(movieId: String): Double {
        val reviews = reviewsCollection.find(eq("movieId", movieId)).projection(excludeId()).toList()
        var sum = 0.0
        var elements = 0.0
        reviews.forEach {
            sum += it["rate"].toString().toInt()
            elements++
        }
        return when {
            elements>0 -> sum/elements
            else -> -1.0
        }
    }
}
