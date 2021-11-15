package com.alameyo.furiouscinema.repositories

import com.alameyo.furiouscinema.JsonFields.COMMENT
import com.alameyo.furiouscinema.JsonFields.MOVIE_ID
import com.alameyo.furiouscinema.JsonFields.RATE
import com.alameyo.furiouscinema.JsonFields.REVIEWS
import com.alameyo.furiouscinema.getCollection
import com.google.gson.JsonObject
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Projections.excludeId
import org.bson.Document
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class MovieRepository(@Autowired private val database: MongoDatabase) {
    private val reviewsCollection = database.getCollection(REVIEWS)

    fun createMovieReviewDocument(movieId: String, review: JsonObject): Document {
        return Document().apply {
            append(MOVIE_ID.fieldName, movieId)
            append(RATE.fieldName, review.get(RATE.fieldName).asInt)
            append(COMMENT.fieldName, review.get(COMMENT.fieldName)?.asString ?: "")
        }
    }

    fun commitReview(reviewDocument: Document) = reviewsCollection.insertOne(reviewDocument).wasAcknowledged()

    fun getReviews(movieId: String) = reviewsCollection.find(eq(MOVIE_ID.fieldName, movieId)).projection(excludeId()).toList()

    fun getReviews() = reviewsCollection.find().projection(excludeId()).toList()

    fun getAverageRatingForMovie(movieId: String): Double {
        val reviews = reviewsCollection.find(eq(MOVIE_ID.fieldName, movieId)).projection(excludeId()).toList()
        var sum = 0.0
        var elements = 0.0
        reviews.forEach {
            sum += it[RATE.fieldName].toString().toInt()
            elements++
        }
        return when {
            elements>0 -> sum/elements
            else -> -1.0
        }
    }
}
